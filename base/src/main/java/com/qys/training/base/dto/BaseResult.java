package com.qys.training.base.dto;

import com.qys.training.base.enumerate.BizCodeEnum;

/**
 * 基础返回结果
 */
public class BaseResult {

    // 业务代码
    private String code;
    // 业务描述
    private String description;
    // 数据
    private Object data;

    public static BaseResult build(String code, String description, Object data) {
        BaseResult result = new BaseResult();
        result.code = code;
        result.description = description;
        result.data = data;
        return result;
    }

    public static BaseResult build(BizCodeEnum bizCode, Object data) {
        BaseResult result = new BaseResult();
        result.code = bizCode.getCode();
        result.description = bizCode.getDescription();
        result.data = data;
        return result;
    }

    public static BaseResult success(){
        return build(BizCodeEnum.SUCCESS,null);
    }

    public static BaseResult success(Object data){
        return build(BizCodeEnum.SUCCESS,data);
    }

    public static BaseResult error(){
        return build(BizCodeEnum.ERROR,null);
    }

    public static BaseResult error(Object data){
        return build(BizCodeEnum.ERROR,data);
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
