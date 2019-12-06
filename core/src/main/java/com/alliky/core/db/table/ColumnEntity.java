package com.alliky.core.db.table;

import android.database.Cursor;

import com.alliky.core.annotation.Column;
import com.alliky.core.db.converter.ColumnConverter;
import com.alliky.core.db.converter.ColumnConverterFactory;
import com.alliky.core.db.sqlite.ColumnDbType;
import com.alliky.core.util.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 11:08
 */
public final class ColumnEntity {

    protected final String name;
    private final String property;
    private final boolean isId;
    private final boolean isAutoId;

    protected final Method getMethod;
    protected final Method setMethod;

    protected final Field columnField;
    protected final ColumnConverter columnConverter;

    /* package */ ColumnEntity(Class<?> entityType, Field field, Column column) {
        field.setAccessible(true);

        this.columnField = field;
        this.name = column.name();
        this.property = column.property();
        this.isId = column.isId();

        Class<?> fieldType = field.getType();
        this.isAutoId = this.isId && column.autoGen() && ColumnUtils.isAutoIdType(fieldType);
        this.columnConverter = ColumnConverterFactory.getColumnConverter(fieldType);


        this.getMethod = ColumnUtils.findGetMethod(entityType, field);
        if (this.getMethod != null && !this.getMethod.isAccessible()) {
            this.getMethod.setAccessible(true);
        }
        this.setMethod = ColumnUtils.findSetMethod(entityType, field);
        if (this.setMethod != null && !this.setMethod.isAccessible()) {
            this.setMethod.setAccessible(true);
        }
    }

    public void setValueFromCursor(Object entity, Cursor cursor, int index) {
        Object value = columnConverter.getFieldValue(cursor, index);
        if (value == null) return;

        if (setMethod != null) {
            try {
                setMethod.invoke(entity, value);
            } catch (Throwable e) {
                Logger.e(e.getMessage(), e.getMessage());
            }
        } else {
            try {
                this.columnField.set(entity, value);
            } catch (Throwable e) {
                Logger.e(e.getMessage(), e.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Object getColumnValue(Object entity) {
        Object fieldValue = getFieldValue(entity);
        if (this.isAutoId && (fieldValue.equals(0L) || fieldValue.equals(0))) {
            return null;
        }
        return columnConverter.fieldValue2DbValue(fieldValue);
    }

    public void setAutoIdValue(Object entity, long value) {
        Object idValue = value;
        if (ColumnUtils.isInteger(columnField.getType())) {
            idValue = (int) value;
        }

        if (setMethod != null) {
            try {
                setMethod.invoke(entity, idValue);
            } catch (Throwable e) {
                Logger.e(e.getMessage(), e.getMessage());
            }
        } else {
            try {
                this.columnField.set(entity, idValue);
            } catch (Throwable e) {
                Logger.e(e.getMessage(), e.getMessage());
            }
        }
    }

    public Object getFieldValue(Object entity) {
        Object fieldValue = null;
        if (entity != null) {
            if (getMethod != null) {
                try {
                    fieldValue = getMethod.invoke(entity);
                } catch (Throwable e) {
                    Logger.e(e.getMessage(), e.getMessage());
                }
            } else {
                try {
                    fieldValue = this.columnField.get(entity);
                } catch (Throwable e) {
                    Logger.e(e.getMessage(), e.getMessage());
                }
            }
        }
        return fieldValue;
    }

    public String getName() {
        return name;
    }

    public String getProperty() {
        return property;
    }

    public boolean isId() {
        return isId;
    }

    public boolean isAutoId() {
        return isAutoId;
    }

    public Field getColumnField() {
        return columnField;
    }

    public ColumnConverter getColumnConverter() {
        return columnConverter;
    }

    public ColumnDbType getColumnDbType() {
        return columnConverter.getColumnDbType();
    }

    @Override
    public String toString() {
        return name;
    }
}

