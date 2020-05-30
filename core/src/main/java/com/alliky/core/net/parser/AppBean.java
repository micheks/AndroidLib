package com.alliky.core.net.parser;

/**
 * @Description TODO
 * @Author wxianing
 * @Date 2020/4/14
 */
public class AppBean<T> {

    private int status;
    private String message;
    private T data;
    private boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean {
    }
}
