package com.alliky.core.net.loader;

import android.text.TextUtils;

import com.alliky.core.cache.DiskCacheEntity;
import com.alliky.core.net.request.UriRequest;
import com.alliky.core.net.params.RequestParams;
import com.alliky.core.util.IOUtil;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:10
 */
/*package*/ class StringLoader extends Loaders<String> {

    private String charset = "UTF-8";
    private String resultStr = null;

    @Override
    public Loaders<String> newInstance() {
        return new StringLoader();
    }

    @Override
    public void setParams(final RequestParams params) {
        if (params != null) {
            String charset = params.getCharset();
            if (!TextUtils.isEmpty(charset)) {
                this.charset = charset;
            }
        }
    }

    @Override
    public String load(final UriRequest request) throws Throwable {
        request.sendRequest();
        resultStr = IOUtil.readStr(request.getInputStream(), charset);
        return resultStr;
    }

    @Override
    public String loadFromCache(final DiskCacheEntity cacheEntity) throws Throwable {
        if (cacheEntity != null) {
            return cacheEntity.getTextContent();
        }

        return null;
    }

    @Override
    public void save2Cache(UriRequest request) {
        saveStringCache(request, resultStr);
    }
}

