package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * author:  niceyoo
 * blog:    https://cnblogs.com/niceyoo
 * desc:    通用异常 enum
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {

    PRICE_CANNOT_BE_NULL(400,"价格不能为空"),
    CATEGORY_NOT_FUND(404,"商品分类未查到"),
    BRAND_NOT_FOUND(404,"品牌不存在"),;

    private int code;
    private String msg;

}
