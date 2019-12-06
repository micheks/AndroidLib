package com.alliky.core.net.loader;

import android.text.TextUtils;

import com.alliky.core.cache.DiskCacheEntity;
import com.alliky.core.net.request.UriRequest;
import com.alliky.core.net.callback.ProgressHandler;
import com.alliky.core.net.params.RequestParams;

import java.util.Date;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 9:57
 */
public abstract class Loaders<T> {
    protected ProgressHandler progressHandler;

    public void setParams(final RequestParams params) {
    }

    public void setProgressHandler(final ProgressHandler callbackHandler) {
        this.progressHandler = callbackHandler;
    }

    protected void saveStringCache(UriRequest request, String resultStr) {
        saveCacheInternal(request, resultStr, null);
    }

    protected void saveByteArrayCache(UriRequest request, byte[] resultData) {
        saveCacheInternal(request, null, resultData);
    }

    public abstract Loaders<T> newInstance();

    public abstract T load(final UriRequest request) throws Throwable;

    public abstract T loadFromCache(final DiskCacheEntity cacheEntity) throws Throwable;

    public abstract void save2Cache(final UriRequest request);

    private void saveCacheInternal(UriRequest request, String resultStr, byte[] resultData) {
        if (!TextUtils.isEmpty(resultStr) || (resultData != null && resultData.length > 0)) {
            DiskCacheEntity entity = new DiskCacheEntity();
            entity.setKey(request.getCacheKey());
            entity.setLastAccess(System.currentTimeMillis());
            entity.setEtag(request.getETag());
            entity.setExpires(request.getExpiration());
            entity.setLastModify(new Date(request.getLastModified()));
            entity.setTextContent(resultStr);
            entity.setBytesContent(resultData);
//            LruDiskCache.getDiskCache(request.getParams().getCacheDirName()).put(entity);
        }
    }
}
