package com.leyou.web;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.pojo.Brand;
import com.leyou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * author:  niceyoo
 * blog:    https://cnblogs.com/niceyoo
 * desc:    品牌 controller
 */
@RestController
@RequestMapping("brand")
class BrandController {

    @Autowired
    private BrandService mBrandService;

    /**
     * 分类、关键字等查询品牌
     * @param page 当前页码
     * @param rows 每页大小
     * @param sortBy 排序字段
     * @param desc 是否为降序
     * @param key 搜索关键字
     * @return total+items+totalPage
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryCategoryListByPid(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key){

        PageResult<Brand> result = mBrandService.queryBrandByPage(page, rows, sortBy, desc, key);
        if (result == null || result.getItems() == null || result.getItems().size() < 1) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 添加品牌
     * @param brand
     * @param cids
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
        mBrandService.saveBrand(brand, cids);
        return ResponseEntity.ok(null);
    }

    /**
     * 修改品牌
     * @param brand
     * @param cids
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> changeBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
        mBrandService.changeBrand(brand, cids);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 删除品牌
     * @param map
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteBrand(@RequestBody Map<String, Long> map) {
        Long bid = map.get("bid");
        mBrandService.deleteBrand(bid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}