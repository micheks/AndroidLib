package com.alliky.core.ex;

import java.io.IOException;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:18
 */
public class BaseException extends IOException {
    private static final long serialVersionUID = 1L;

    public BaseException() {
        super();
    }

    public BaseException(String detailMessage) {
        super(detailMessage);
    }

    public BaseException(String detailMessage, Throwable throwable) {
        super(detailMessage);
        this.initCause(throwable);
    }

    public BaseException(Throwable throwable) {
        super(throwable.getMessage());
        this.initCause(throwable);
    }
}
