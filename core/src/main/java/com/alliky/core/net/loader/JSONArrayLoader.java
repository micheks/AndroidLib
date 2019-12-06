package com.alliky.core.net.loader;

import android.text.TextUtils;

import com.alliky.core.cache.DiskCacheEntity;
import com.alliky.core.net.request.UriRequest;
import com.alliky.core.net.params.RequestParams;
import com.alliky.core.util.IOUtil;

import org.json.JSONArray;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:09
 */
/*package*/ class JSONArrayLoader extends Loaders<JSONArray> {

    private String charset = "UTF-8";
    private String resultStr = null;

    @Override
    public Loaders<JSONArray> newInstance() {
        return new JSONArrayLoader();
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
    public JSONArray load(final UriRequest request) throws Throwable {
        request.sendRequest();
        resultStr = IOUtil.readStr(request.getInputStream(), charset);
        return new JSONArray(resultStr);
    }

    @Override
    public JSONArray loadFromCache(final DiskCacheEntity cacheEntity) throws Throwable {
        if (cacheEntity != null) {
            String text = cacheEntity.getTextContent();
            if (!TextUtils.isEmpty(text)) {
                return new JSONArray(text);
            }
        }

        return null;
    }

    @Override
    public void save2Cache(UriRequest request) {
        saveStringCache(request, resultStr);
    }
}
