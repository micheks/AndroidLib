package com.alliky.core.util;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 9:39
 */
public class KeyValue {
    public final String key;
    public final Object value;

    public KeyValue(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 获取value的字符串值, 为null时返回空字符串
     */
    public String getValueStrOrEmpty() {
        return value == null ? "" : value.toString();
    }

    /**
     * 获取value的字符串值, 为null时返回null
     */
    public String getValueStrOrNull() {
        return value == null ? null : value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyValue keyValue = (KeyValue) o;

        return key == null ? keyValue.key == null : key.equals(keyValue.key);

    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "KeyValue{" + "key='" + key + '\'' + ", value=" + value + '}';
    }
}
