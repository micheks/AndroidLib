package com.alliky.core.net;

/**
 * Author wxianing
 * date 2018/6/26
 */
public enum HttpMethod {
//    GET,
//    POST,
//    POST_RAW,
//    PUT,
//    PUT_RAW,
//    DELETE,
//    UPLOAD,


    GET("GET"),
    POST("POST"),
    POST_RAW("POST_RAW"),
    PUT("PUT"),
    PUT_RAW("PUT_RAW"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    UPLOAD("UPLOAD");

    private final String value;

    HttpMethod(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static boolean permitsRetry(HttpMethod method) {
        return method == GET;
    }

    public static boolean permitsCache(HttpMethod method) {
        return method == GET || method == POST;
    }

    public static boolean permitsRequestBody(HttpMethod method) {
        return method == null
                || method == POST
                || method == PUT
                || method == PATCH
                || method == DELETE;
    }

}
