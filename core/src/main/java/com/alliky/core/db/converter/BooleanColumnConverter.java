package com.alliky.core.db.converter;

import android.database.Cursor;

import com.alliky.core.db.sqlite.ColumnDbType;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 11:00
 */
public class BooleanColumnConverter implements ColumnConverter<Boolean> {
    @Override
    public Boolean getFieldValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : cursor.getInt(index) == 1;
    }

    @Override
    public Object fieldValue2DbValue(Boolean fieldValue) {
        if (fieldValue == null) return null;
        return fieldValue ? 1 : 0;
    }

    @Override
    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
