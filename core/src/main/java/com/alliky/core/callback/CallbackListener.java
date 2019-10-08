package com.alliky.core.callback;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/10/8 18:24
 */
public interface CallbackListener <T> {
    /**
     * 当任务成功的时候回调
     *
     * @param result 任务请求结果
     */
    void onSuccess(T result);

    /**
     * 当任务失败的时候回调
     *
     * @param code 错误码
     * @param msg  错误消息
     */
    void onError(int code, String msg);

    /**
     *
     * @param t
     */
    void onFailure(Throwable t);

}
