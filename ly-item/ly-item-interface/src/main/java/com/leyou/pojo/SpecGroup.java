package com.leyou.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * author:  niceyoo
 * blog:    https://cnblogs.com/niceyoo
 * desc:    规格组
 *              - 一个商品分类下有多个规格组
 *              - 一个规格组下有多个规格参数
 */
@Data
@Table(name = "tb_spec_group")
public class SpecGroup {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long cid;// 商品分类id
    private String name;// 规格组名称

    @Transient
    private List<SpecParam> params;
}
