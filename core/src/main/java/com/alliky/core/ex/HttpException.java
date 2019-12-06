package com.alliky.core.ex;

import android.text.TextUtils;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:18
 */
public class HttpException extends BaseException {
    private static final long serialVersionUID = 1L;

    private int code;
    private String errorCode;
    private String customMessage;
    private String result;

    /**
     * @param code          The http response status code, 0 if the http request error and has no response.
     * @param detailMessage The http response message.
     */
    public HttpException(int code, String detailMessage) {
        super(detailMessage);
        this.code = code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.customMessage = message;
    }

    /**
     * @return The http response status code, 0 if the http request error and has no response.
     */
    public int getCode() {
        return code;
    }

    public String getErrorCode() {
        return errorCode == null ? String.valueOf(code) : errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        if (!TextUtils.isEmpty(customMessage)) {
            return customMessage;
        } else {
            return super.getMessage();
        }
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "errorCode: " + getErrorCode() + ", msg: " + getMessage() + ", result: " + result;
    }
}
