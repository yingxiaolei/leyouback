package com.leyou.service;

import com.leyou.common.vo.PageResult;
import com.leyou.pojo.Brand;

import java.util.List;

/**
 * 品牌service
 */
public interface BrandService {
    PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key);

    void saveBrand(Brand brand, List<Long> cids);

    void changeBrand(Brand brand, List<Long> cids);

    void deleteBrand(Long bid);

    List<Brand> queryBrandByCategory(Long cid);

    List<Brand> queryBrandByIds(List<Long> bids);
}
