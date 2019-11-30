package com.leyou.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.vo.PageResult;
import com.leyou.mapper.*;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.pojo.Stock;
import com.leyou.vo.SpuVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * author:  niceyoo
 * blog:    https://cnblogs.com/niceyoo
 * desc:    商品service
 */
@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private RabbitTemplate amqpTemplate;

    private Logger logger = LoggerFactory.getLogger(GoodsService.class);

    /**
     * 关键字查询商品
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    public PageResult<SpuVo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();  //创建条件对象
        PageHelper.startPage(page, rows);
        //动态查询标题
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");  //条件搜索
        }
        //是否过滤上下架
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);   //是否上下架
        }
        Page<Spu> spuList = (Page<Spu>) goodsMapper.selectByExample(example);
        //通过Stream流的map方法把流中的每一个元素转换成另一个类型
        Stream<SpuVo> spuBoStream = spuList.getResult().stream().map(spu -> {
            SpuVo spubo = new SpuVo();
            BeanUtils.copyProperties(spu, spubo); //把一个对象的属性拷贝到另一个对象
            //获取商品分类名称
            List<String> categoryNames = categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            //获取商品的品牌名称
            String BrandName = brandMapper.selectByPrimaryKey(spu.getBrandId()).getName();
            spubo.setCname(StringUtils.join(categoryNames, "/"));//商品所属分类名
            spubo.setBname(BrandName);//商品的品牌名
            return spubo;
        });
        //把Stream流转换成集合
        List<SpuVo> spuVoList = spuBoStream.collect(Collectors.toList());
        return new PageResult<>(spuList.getTotal(), new Long(spuList.getPages()), spuVoList);
    }

    /**
     * 新增商品service
     * @param spu
     */
    @Transactional
    public void saveGoods(SpuVo spu) {
        //保存spu
        spu.setSaleable(true);//设置是否可出售
        spu.setValid(true);//逻辑删除用
        spu.setCreateTime(new Date());//设置创建时间
        spu.setLastUpdateTime(spu.getCreateTime());//设置最后修改时间
        this.spuMapper.insert(spu);

        //保存spu详情
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        this.spuDetailMapper.insert(spuDetail);

        //保存skus和库存
        this.saveSkuAndStock(spu.getSkus(), spu.getId());
        //发送消息通知搜索服务
        this.sendMessage(spu.getId(), "insert");
    }

    /**
     * 保存skus和库存
     * @param skus
     * @param spuId
     */
    private void saveSkuAndStock(List<Sku> skus, Long spuId) {
        for (Sku sku : skus) {
            //保存sku
            sku.setSpuId(spuId);
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            skuMapper.insert(sku);
            //保存sku 的库存信息
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insert(stock);
        }
    }

    /**
     * 修改商品
     * @param spu
     */
    @Transactional
    public void updateGoods(SpuVo spu) {
        //直接修改spu
        spu.setLastUpdateTime(new Date());
        this.spuMapper.updateByPrimaryKeySelective(spu);
        //直接修改spuDetail
        this.spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        //获取前端提交最新的skus
        List<Sku> newSkus = spu.getSkus();
        //获取数据库旧的skus
        List<Sku> oldSkus = this.querySkuBySpuId(spu.getId());//数据库中之前对应spu下面所有的sku
        //这里存在用户新加的sku,和更改已存在的sku,和用户删掉了的sku.需要做相应处理
        newSkus.forEach(newSku -> {
            if (newSku.getId() == null) {//保存新加的sku,判断sku的id是否为空,为空就是用户新加的,不为空就是修改已经有的sku
                newSku.setSpuId(spu.getId());
                newSku.setCreateTime(new Date());
                newSku.setLastUpdateTime(newSku.getCreateTime());
                //保存sku
                skuMapper.insertSelective(newSku);
                //保存sku的库存
                Stock stock = new Stock();
                stock.setSkuId(newSku.getId());
                stock.setStock(newSku.getStock());
                stockMapper.insertSelective(stock);
            } else {//表示要被修改的
                newSku.setLastUpdateTime(new Date());
                newSku.setSpuId(spu.getId());
                //修改sku
                skuMapper.updateByPrimaryKeySelective(newSku);
                //修改sku库存
                Stock stock = new Stock();
                stock.setStock(newSku.getStock());
                stock.setSkuId(newSku.getId());
                stockMapper.updateByPrimaryKeySelective(stock);
                //如果用户把已有的sku删掉了我们也需要在数据库里删掉
                // 在旧的skus中移除修改的,剩下的就是被用户删掉的,在下方作下架处理
                //removeIf方法,传入一个数据判断者predicate,把集合中符合条件的数据给删掉
                // 迭代oldSkus中的每个元素与newSku相比较.返回true就删除该oldSku
                oldSkus.removeIf(oldSku -> oldSku.getId().longValue() == newSku.getId().longValue());
              /*  for (int i = 0; i < oldSkus.size(); i++) {
                    Sku oldsku = oldSkus.get(i);
                    if (oldsku.getId().longValue() == newSku.getId().longValue()) {
                        oldSkus.remove(i);
                        i--;
                    }
                }*/
            }

        });
        //把剩余的做下架处理
        oldSkus.forEach(oldSku -> {
            oldSku.setEnable(false);
            oldSku.setLastUpdateTime(new Date());
            this.skuMapper.updateByPrimaryKeySelective(oldSku);
        });
        //发送消息通知搜索服务
        this.sendMessage(spu.getId(), "update");
    }

    /**
     * 根据spuId查询Sku
     * @param id
     * @return
     */
    public List<Sku> querySkuBySpuId(Long id) {
        Sku record = new Sku();
        record.setSpuId(id);
        List<Sku> skus = this.skuMapper.select(record);
        //同时查询出Sku的库存信息 库存表Stock的主键就是SkuId
        skus.forEach(sku -> {
            Stock stock = this.stockMapper.selectByPrimaryKey(sku.getId());
            sku.setStock(stock.getStock());
        });
        return skus;
    }

    /**
     * 根据spuId查询spu
     * @param spuId
     * @return
     */
    public Spu querySpuById(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        return spu;
    }

    /**
     * 根据spuId查询spuDetail,spuId是spuDetail的主键
     * @param spuId
     * @return
     */
    public SpuDetail querySpuDetailById(Long spuId) {
        return this.spuDetailMapper.selectByPrimaryKey(spuId);

    }

    /**
     * 发送消息到rabbitmq
     * @param id
     * @param type
     */
    private void sendMessage(Long id, String type) {
        // 发送消息
        try {
            this.amqpTemplate.convertAndSend("item." + type, id);
        } catch (Exception e) {
            logger.error("{}商品消息发送异常，商品id：{}", type, id, e);
        }
    }

}
