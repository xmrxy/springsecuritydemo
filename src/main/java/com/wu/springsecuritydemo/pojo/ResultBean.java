package com.wu.springsecuritydemo.pojo;

import net.sf.json.JSONObject;

/**
* @Author: Wgy
* @date: 2020/7/22 15:14
* Description:返回结果Bean
*/
public class ResultBean {

    private static final int SUCCESS_CODE=200;
    private static final String SUCCESS_MSG="成功";
    private static final int LOSS_AUTH_CODE=10000;
    private static final String LOSS_AUTH_MSG="鉴权失败";
    private static final int LOSS_FORMAT_CODE=10001;
    private static final String LOSS_FORMAT_MSG="报文格式错误";
    private static final int LOSS_ORDER_NOTEXIST_CODE=10002;
    private static final String LOSS_ORDER_NOTEXIST_MSG="订单不存在";
    private static final int LOSS_PARAM_ERR_CODE=10003;
    private static final String LOSS_PARAM_ERR_MSG="参数缺失";
    private static final int LOSS_DETAIL_ERR_CODE=10004;
    private static final String LOSS_DETAIL_ERR_MSG="修改detail失败";

    private int code;
    private String msg;
    private Object data;


    public ResultBean(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data="";
    }

    public ResultBean(int code, String msg,Object object) {
        this.code = code;
        this.msg = msg;
        this.data=object;
    }

    public static ResultBean SUCCESS_CUSTOM(int code, String msg,Object object){
        return new ResultBean(code,msg,object);
    }
    public static ResultBean LOSS_CUSTOM(int code, String msg,Object object){
        return new ResultBean(code,msg,object);
    }



    public static ResultBean SUCCESS_DATA(int code, String msg,Object object){
        return new ResultBean(code,msg,object);
    }

    public static ResultBean SUCCESS(){
        return new ResultBean(SUCCESS_CODE,SUCCESS_MSG);
    }
    public static ResultBean LOSS_UPDATE_DETAIL(){
        return new ResultBean(LOSS_DETAIL_ERR_CODE,LOSS_DETAIL_ERR_MSG);
    }
    public static ResultBean LOSS_AUTH(){
        return new ResultBean(LOSS_AUTH_CODE,LOSS_AUTH_MSG);
    }
    public static ResultBean LOSS_FORMAT(){
        return new ResultBean(LOSS_FORMAT_CODE,LOSS_FORMAT_MSG);
    }
    public static ResultBean NOTEXIST(){
        return new ResultBean(LOSS_ORDER_NOTEXIST_CODE,LOSS_ORDER_NOTEXIST_MSG);
    }
    public static ResultBean PARAM_ERR(){
        return new ResultBean(LOSS_PARAM_ERR_CODE,LOSS_PARAM_ERR_MSG);
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
