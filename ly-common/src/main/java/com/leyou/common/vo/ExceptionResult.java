package com.leyou.common.vo;

import com.leyou.common.enums.ExceptionEnum;
import lombok.Data;

/**
 * @author niceyoo
 *
 * 自定义异常结果类
 */

@Data
public class ExceptionResult {

    /**
     * 返回的状态码
     */
    private int status;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 时间戳
     */
    private long timestamp;

    public ExceptionResult(ExceptionEnum em) {
        this.status = em.getCode();
        this.message = em.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}
