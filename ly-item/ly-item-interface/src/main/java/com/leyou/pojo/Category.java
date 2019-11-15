package com.leyou.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * author:  niceyoo
 * blog:    https://cnblogs.com/niceyoo
 * desc:    商品分类
 */
@Data
@Table(name = "tb_category")
public class Category {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    /** 分类名称 */
    private String name;
    /** 父级id */
    private Long parentId;
    /** 是否是父级 */
    private Boolean isParent;
    /** 排序 */
    private Integer sort;

}