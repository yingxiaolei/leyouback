package com.leyou.common.exception;

import com.leyou.common.enums.ExceptionEnum;

import lombok.Getter;

/**
 * @author bystander
 * @date 2018/9/15
 *
 * 自定义异常类 - 配合 ControllerAdvice 使用
 */
@Getter
public class LyException extends RuntimeException {

    private ExceptionEnum exceptionEnum;

    public LyException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

}
