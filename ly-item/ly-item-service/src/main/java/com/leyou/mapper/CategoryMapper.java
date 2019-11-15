package com.leyou.mapper;

import java.util.List;

import com.leyou.pojo.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;
/**
 * author:  niceyoo
 * blog:    https://cnblogs.com/niceyoo
 * desc:    商品分类的 mapper
 */

public interface CategoryMapper extends Mapper<Category> , SelectByIdListMapper<Category, Long> {

    @Select("select c.* from tb_category c inner join tb_category_brand cb on c.id = cb.category_id where cb.brand_id=#{brandId}")
    List<Category> queryBrandCategoryByBid(@Param("brandId") Long brandId);

}