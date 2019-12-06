package com.alliky.core.net.body;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 9:38
 */
public interface RequestBody {

    long getContentLength();

    void setContentType(String contentType);

    String getContentType();

    void writeTo(OutputStream out) throws IOException;
}
