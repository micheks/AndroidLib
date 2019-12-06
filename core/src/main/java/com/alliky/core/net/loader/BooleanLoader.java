package com.alliky.core.net.loader;

import com.alliky.core.cache.DiskCacheEntity;
import com.alliky.core.net.request.UriRequest;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:22
 */
/*package*/ class BooleanLoader extends Loaders<Boolean> {

    @Override
    public Loaders<Boolean> newInstance() {
        return new BooleanLoader();
    }

    @Override
    public Boolean load(final UriRequest request) throws Throwable {
        request.sendRequest();
        return request.getResponseCode() < 300;
    }

    @Override
    public Boolean loadFromCache(final DiskCacheEntity cacheEntity) throws Throwable {
        return null;
    }

    @Override
    public void save2Cache(final UriRequest request) {

    }
}

