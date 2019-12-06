package com.alliky.core.net.loader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:06
 */
public final class LoaderFactory {
    private LoaderFactory() {
    }

    /**
     * key: loadType
     */
    private static final HashMap<Type, Loaders> converterHashMap = new HashMap<Type, Loaders>();

    static {
        converterHashMap.put(JSONObject.class, new JSONObjectLoader());
        converterHashMap.put(JSONArray.class, new JSONArrayLoader());
        converterHashMap.put(String.class, new StringLoader());
        converterHashMap.put(File.class, new FileLoader());
        converterHashMap.put(byte[].class, new ByteArrayLoader());
        converterHashMap.put(InputStream.class, new InputStreamLoader());

        BooleanLoader booleanLoader = new BooleanLoader();
        converterHashMap.put(boolean.class, booleanLoader);
        converterHashMap.put(Boolean.class, booleanLoader);

        IntegerLoader integerLoader = new IntegerLoader();
        converterHashMap.put(int.class, integerLoader);
        converterHashMap.put(Integer.class, integerLoader);
    }

    public static Loaders<?> getLoader(Type type) {
        Loaders<?> result = converterHashMap.get(type);
        if (result == null) {
            result = new ObjectLoader(type);
        } else {
            result = result.newInstance();
        }
        return result;
    }

    public static <T> void registerLoader(Type type, Loaders<T> loader) {
        converterHashMap.put(type, loader);
    }
}
