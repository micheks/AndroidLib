package com.alliky.core.db.sqlite;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:58
 */
public enum ColumnDbType {

    INTEGER("INTEGER"), REAL("REAL"), TEXT("TEXT"), BLOB("BLOB");

    private String value;

    ColumnDbType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

