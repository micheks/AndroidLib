package com.alliky.core.net.loader;

import com.alliky.core.cache.DiskCacheEntity;
import com.alliky.core.net.request.UriRequest;
import com.alliky.core.util.IOUtil;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:20
 */
/*package*/ class ByteArrayLoader extends Loaders<byte[]> {

    private byte[] resultData;

    @Override
    public Loaders<byte[]> newInstance() {
        return new ByteArrayLoader();
    }

    @Override
    public byte[] load(final UriRequest request) throws Throwable {
        request.sendRequest();
        resultData = IOUtil.readBytes(request.getInputStream());
        return resultData;
    }

    @Override
    public byte[] loadFromCache(final DiskCacheEntity cacheEntity) throws Throwable {
        if (cacheEntity != null) {
            byte[] data = cacheEntity.getBytesContent();
            if (data != null && data.length > 0) {
                return data;
            }
        }
        return null;
    }

    @Override
    public void save2Cache(final UriRequest request) {
        saveByteArrayCache(request, resultData);
    }
}
