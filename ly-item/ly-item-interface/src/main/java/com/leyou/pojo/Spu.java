package com.leyou.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * author:  niceyoo
 * blog:    https://cnblogs.com/niceyoo
 * desc:    商品聚合信息最小单元：品牌、分类、标题、是否上架
 */
@Table(name = "tb_spu")
@Data
public class Spu {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    // 标题
    private String title;
    // 子标题
    private String subTitle;
    // 1级类目id
    private Long cid1;
    // 2级类目id
    private Long cid2;
    // 3级类目id
    private Long cid3;
    // 商品所属品牌id
    private Long brandId;
    // 是否上架，0下架，1上架
    private Boolean saleable;
    // 是否有效，0已删除，1有效
    private Boolean valid;
    // 添加时间
    private Date createTime;
    // 最后修改时间
    @JsonIgnore
    private Date lastUpdateTime;
    //spu所属的分类名称
    @Transient
    private String cname;
    //spu所属品牌名
    @Transient
    private String bname;
    //spu详情
    @Transient
    private SpuDetail spuDetail;
    //sku集合
    @Transient
    private List<Sku> skus;
}
