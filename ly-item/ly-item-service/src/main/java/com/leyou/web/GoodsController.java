package com.leyou.web;

import com.leyou.common.vo.PageResult;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.service.GoodsService;
import com.leyou.vo.SpuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * author:  niceyoo
 * blog:    https://cnblogs.com/niceyoo
 * desc:    商品
 */
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /*
     * key :搜索的关键字
     * saleable : 是否上架
     * page : 页码
     * rows : 每页展示的数量
     * */
    @GetMapping("spu/page") //spu/page?key=&saleable=true&page=1&rows=5
    public ResponseEntity<PageResult<SpuVo>> querySpuByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    ) {
        PageResult<SpuVo> pageResult = goodsService.querySpuByPage(key, saleable, page, rows);
        if (pageResult != null && pageResult.getItems() != null && pageResult.getItems().size() != 0) {
            return ResponseEntity.ok(pageResult);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 新增商品
     * @param spu
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveGoods(@RequestBody SpuVo spu) {
        try {
            this.goodsService.saveGoods(spu);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 修改更新商品
     * @param spu
     * @return
     */
    @PutMapping
    @PostMapping
    public ResponseEntity<Void> updateGoods(@RequestBody SpuVo spu) {
        try {
            this.goodsService.updateGoods(spu);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*修改商品数据回显查询 ---开始---*/
    /**
     * 查询spudetail   spu/detail/2
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("spuId") Long spuId) {

        SpuDetail spuDetail = this.goodsService.querySpuDetailById(spuId);
        if (spuDetail == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(spuDetail);
    }

    /**
     * 根据spuId查询sku信息  sku/list?id=2
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long id) {
        List<Sku> skus = this.goodsService.querySkuBySpuId(id);
        if (skus == null || skus.size() < 1) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(skus);
    }
    /*商品数据回显---结束----*/


    /**
     * 根据spuId查询Spu
     * @param spuId
     * @return
     */
    @GetMapping("spu/{spuId}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("spuId") Long spuId) {
        Spu spu = goodsService.querySpuById(spuId);
        if (spu == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(spu);
    }
}
