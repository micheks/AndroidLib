package com.alliky.core.db.converter;

import android.database.Cursor;

import com.alliky.core.db.sqlite.ColumnDbType;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 11:02
 */
public class FloatColumnConverter implements ColumnConverter<Float> {
    @Override
    public Float getFieldValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : cursor.getFloat(index);
    }

    @Override
    public Object fieldValue2DbValue(Float fieldValue) {
        return fieldValue;
    }

    @Override
    public ColumnDbType getColumnDbType() {
        return ColumnDbType.REAL;
    }
}
