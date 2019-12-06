package com.alliky.core.net.callback;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 9:41
 *
 * 进度控制接口, updateProgress方式中ProgressCallback#onLoading.
 *
 * 默认最长间隔300毫秒调用一次.
 */
public interface ProgressHandler {
    /**
     * @return continue
     */
    boolean updateProgress(long total, long current, boolean forceUpdateUI);
}