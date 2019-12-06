package com.alliky.core.db.converter;

import android.database.Cursor;

import com.alliky.core.db.sqlite.ColumnDbType;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 11:03
 */
public class LongColumnConverter implements ColumnConverter<Long> {
    @Override
    public Long getFieldValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : cursor.getLong(index);
    }

    @Override
    public Object fieldValue2DbValue(Long fieldValue) {
        return fieldValue;
    }

    @Override
    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
