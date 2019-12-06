package com.alliky.core.db.converter;

import android.database.Cursor;

import com.alliky.core.db.sqlite.ColumnDbType;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 11:04
 */
public class SqlDateColumnConverter implements ColumnConverter<java.sql.Date> {
    @Override
    public java.sql.Date getFieldValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : new java.sql.Date(cursor.getLong(index));
    }

    @Override
    public Object fieldValue2DbValue(java.sql.Date fieldValue) {
        if (fieldValue == null) return null;
        return fieldValue.getTime();
    }

    @Override
    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}

