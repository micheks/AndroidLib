package com.alliky.core.net.loader;

import com.alliky.core.cache.DiskCacheEntity;
import com.alliky.core.net.request.UriRequest;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:22
 */
/*package*/ class IntegerLoader extends Loaders<Integer> {
    @Override
    public Loaders<Integer> newInstance() {
        return new IntegerLoader();
    }

    @Override
    public Integer load(UriRequest request) throws Throwable {
        request.sendRequest();
        return request.getResponseCode();
    }

    @Override
    public Integer loadFromCache(final DiskCacheEntity cacheEntity) throws Throwable {
        return null;
    }

    @Override
    public void save2Cache(UriRequest request) {

    }
}