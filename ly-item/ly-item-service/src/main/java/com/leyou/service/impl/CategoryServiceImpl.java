package com.leyou.service.impl;

import com.leyou.mapper.CategoryMapper;
import com.leyou.pojo.Category;
import com.leyou.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pass on 2019/11/15.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryByParentId(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        List<Category> categorys = categoryMapper.select(category);
        return categorys;
    }

    //brand回显查询
    @Override
    public List<Category> queryBrandByBid(Long brandId) {
        List<Category> categories = categoryMapper.queryBrandCategoryByBid(brandId);
        return categories;
    }

    //获取商品分类名
    @Override
    public List<String> queryNamesByIds(List<Long> cids) {
        List<Category> categories = categoryMapper.selectByIdList(cids);
       /* StringBuilder stringBuilder = new StringBuilder();
        for (Category category : categories) {

            stringBuilder.append(category.getName() + "/");
        }
        String goodsName = stringBuilder.toString();
        // System.out.println(goodsName);
        return StringUtils.substringBeforeLast(goodsName, "/");*/
        List<String> categoryNames = categories.stream().map(category -> category.getName()).collect(Collectors.toList());
        return categoryNames;
    }

    @Override
    public List<Category> queryAllByCid3(Long id) {
        Category c3 = this.categoryMapper.selectByPrimaryKey(id);
        Category c2 = this.categoryMapper.selectByPrimaryKey(c3.getParentId());
        Category c1 = this.categoryMapper.selectByPrimaryKey(c2.getParentId());
        return Arrays.asList(c1, c2, c3);
    }

}
