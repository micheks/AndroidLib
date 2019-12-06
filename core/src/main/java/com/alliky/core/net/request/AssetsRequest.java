package com.alliky.core.net.request;

import android.content.Context;

import com.alliky.core.net.params.RequestParams;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:43
 */
public class AssetsRequest  extends ResRequest {

    public AssetsRequest(RequestParams params, Type loadType) throws Throwable {
        super(params, loadType);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (inputStream == null) {
            Context context = params.getContext();
            String assetsPath = queryUrl.replace("assets://", "");
            inputStream = context.getResources().getAssets().open(assetsPath);
            contentLength = inputStream.available();
        }
        return inputStream;
    }
}
