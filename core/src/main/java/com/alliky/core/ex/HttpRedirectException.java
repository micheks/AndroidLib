package com.alliky.core.ex;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:47
 */
public class HttpRedirectException extends HttpException {
    private static final long serialVersionUID = 1L;

    public HttpRedirectException(int code, String detailMessage, String result) {
        super(code, detailMessage);
        this.setResult(result);
    }
}

