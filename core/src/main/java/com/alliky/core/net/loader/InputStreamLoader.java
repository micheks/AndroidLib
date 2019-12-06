package com.alliky.core.net.loader;

import com.alliky.core.cache.DiskCacheEntity;
import com.alliky.core.net.request.UriRequest;

import java.io.InputStream;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:21
 * * 建议配合使用,
 * * 将PrepareType设置为InputStream, 以便在PrepareCallback#prepare中做耗时的数据任务处理.
 */
/*package*/ class InputStreamLoader extends Loaders<InputStream> {

    @Override
    public Loaders<InputStream> newInstance() {
        return new InputStreamLoader();
    }

    @Override
    public InputStream load(final UriRequest request) throws Throwable {
        request.sendRequest();
        return request.getInputStream();
    }

    @Override
    public InputStream loadFromCache(final DiskCacheEntity cacheEntity) throws Throwable {
        return null;
    }

    @Override
    public void save2Cache(final UriRequest request) {
    }
}