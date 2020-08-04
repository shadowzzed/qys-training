package com.qys.training.base.exception;

/**
 * 契约锁自定义异常
 */
public class QysException extends RuntimeException {

    // 代号
    private String code;
    // 描述
    private String description;

    // 构造器
    public QysException(String code,String description){
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
