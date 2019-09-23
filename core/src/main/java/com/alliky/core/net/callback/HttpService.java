package com.alliky.core.net.callback;

import java.util.WeakHashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Author wxianing
 * date 2018/6/26
 */
public interface HttpService {

    @GET
    Call<String> get(@Url String url, @QueryMap WeakHashMap<String, Object> params, @HeaderMap WeakHashMap<String, String> headers);

    @FormUrlEncoded
    @POST
    Call<String> post(@Url String url, @FieldMap WeakHashMap<String, Object> params, @HeaderMap WeakHashMap<String, String> headers);

    @POST
    Call<String> postRaw(@Url String url, @Body RequestBody body, @HeaderMap WeakHashMap<String, String> headers);

    @FormUrlEncoded
    @PUT
    Call<String> put(@Url String url, @FieldMap WeakHashMap<String, Object> params, @HeaderMap WeakHashMap<String, String> headers);

    @PUT
    Call<String> putRaw(@Url String url, @Body RequestBody body, @HeaderMap WeakHashMap<String, String> headers);

    @DELETE
    Call<String> delete(@Url String url, @QueryMap WeakHashMap<String, Object> params, @HeaderMap WeakHashMap<String, String> headers);

    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url, @QueryMap WeakHashMap<String, Object> params);

    @Multipart
    @POST
    Call<String> upload(@Url String url, @Part MultipartBody.Part file, @HeaderMap WeakHashMap<String, String> headers);

}
