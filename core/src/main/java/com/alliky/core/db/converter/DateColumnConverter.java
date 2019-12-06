package com.alliky.core.db.converter;

import android.database.Cursor;

import com.alliky.core.db.sqlite.ColumnDbType;

import java.util.Date;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 11:01
 */
public class DateColumnConverter implements ColumnConverter<Date> {
    @Override
    public Date getFieldValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : new Date(cursor.getLong(index));
    }

    @Override
    public Object fieldValue2DbValue(Date fieldValue) {
        if (fieldValue == null) return null;
        return fieldValue.getTime();
    }

    @Override
    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
