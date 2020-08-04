package com.qys.training.base.enumerate;

/**
 * 业务代码枚举
 */
public enum BizCodeEnum {

    SUCCESS("SUCCESS","操作成功"),
    ERROR("ERROR","操作失败"),
    UNKNOWN_ERROR("UNKNOWN_ERROR","未知异常")

    ;

    private String code;
    private String description;

    BizCodeEnum(String code,String description){
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
