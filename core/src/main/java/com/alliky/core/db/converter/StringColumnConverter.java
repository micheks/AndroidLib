package com.alliky.core.db.converter;

import android.database.Cursor;

import com.alliky.core.db.sqlite.ColumnDbType;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 11:04
 */
public class StringColumnConverter implements ColumnConverter<String> {
    @Override
    public String getFieldValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : cursor.getString(index);
    }

    @Override
    public Object fieldValue2DbValue(String fieldValue) {
        return fieldValue;
    }

    @Override
    public ColumnDbType getColumnDbType() {
        return ColumnDbType.TEXT;
    }
}

