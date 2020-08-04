package com.qys.training.base.enumerate;

/**
 * 业务代码枚举
 */
public enum BizCodeEnum {

    SUCCESS("SUCCESS","操作成功"),
    ERROR("ERROR","操作失败"),
    UNKNOWN_ERROR("UNKNOWN_ERROR","未知异常"),
    LOST_PARAM("LOST_PARAM", "缺少参数"),
    USER_EXISTED("USER_EXISTED", "用户已存在"),
    WRONG_PARAM("WRONG_PARAM", "参数错误"),
    FILE_TOO_LARGE("FILE_TOO_LARGE", "文件过大"),
    FILE_NOT_EXISTED("FILE_NOT_EXISTED", "文件不存在")

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
