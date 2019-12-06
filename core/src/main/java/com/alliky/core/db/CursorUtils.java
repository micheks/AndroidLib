package com.alliky.core.db;

import android.database.Cursor;

import com.alliky.core.db.table.ColumnEntity;
import com.alliky.core.db.table.DbModel;
import com.alliky.core.db.table.TableEntity;

import java.util.HashMap;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 11:15
 */
/*package*/ final class CursorUtils {

    public static <T> T getEntity(TableEntity<T> table, final Cursor cursor) throws Throwable {
        T entity = table.createEntity();
        HashMap<String, ColumnEntity> columnMap = table.getColumnMap();
        int columnCount = cursor.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            String columnName = cursor.getColumnName(i);
            ColumnEntity column = columnMap.get(columnName);
            if (column != null) {
                column.setValueFromCursor(entity, cursor, i);
            }
        }
        return entity;
    }

    public static DbModel getDbModel(final Cursor cursor) {
        DbModel result = new DbModel();
        int columnCount = cursor.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            result.add(cursor.getColumnName(i), cursor.getString(i));
        }
        return result;
    }
}

