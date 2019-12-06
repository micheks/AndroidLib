package com.alliky.core.ex;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:17
 */
public class FileLockedException extends BaseException {
    private static final long serialVersionUID = 1L;

    public FileLockedException(String detailMessage) {
        super(detailMessage);
    }
}

