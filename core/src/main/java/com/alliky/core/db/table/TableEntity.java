package com.alliky.core.db.table;

import android.database.Cursor;
import android.text.TextUtils;

import com.alliky.core.annotation.Table;
import com.alliky.core.db.DbManager;
import com.alliky.core.db.sqlite.SqlInfo;
import com.alliky.core.db.sqlite.SqlInfoBuilder;
import com.alliky.core.ex.DbException;
import com.alliky.core.util.IOUtil;
import com.alliky.core.util.Logger;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 11:07
 */
public final class TableEntity<T> {

    private final DbManager db;
    private final String name;
    private final String onCreated;
    private final Class<T> entityType;
    private final Constructor<T> constructor;
    private ColumnEntity id;
    private volatile Boolean tableCheckedStatus;

    /**
     * key: columnName
     */
    private final LinkedHashMap<String, ColumnEntity> columnMap;

    /*package*/ TableEntity(DbManager db, Class<T> entityType) throws Throwable {
        this.db = db;
        this.entityType = entityType;

        Table table = entityType.getAnnotation(Table.class);
        if (table == null) {
            throw new DbException("missing @Table on " + entityType.getName());
        }
        this.name = table.name();
        this.onCreated = table.onCreated();

        try {
            this.constructor = entityType.getConstructor();
            this.constructor.setAccessible(true);
        } catch (Throwable ex) {
            throw new DbException("missing no-argument constructor for the table: " + this.name);
        }

        this.columnMap = TableUtils.findColumnMap(entityType);
        for (ColumnEntity column : columnMap.values()) {
            if (column.isId()) {
                this.id = column;
                break;
            }
        }
    }

    public T createEntity() throws Throwable {
        return this.constructor.newInstance();
    }

    public boolean tableIsExists() throws DbException {
        return tableIsExists(false);
    }

    public boolean tableIsExists(boolean forceCheckFromDb) throws DbException {
        if (tableCheckedStatus != null && (tableCheckedStatus || !forceCheckFromDb)) {
            return tableCheckedStatus;
        }

        Cursor cursor = db.execQuery("SELECT COUNT(*) AS c FROM sqlite_master WHERE type='table' AND name='" + name + "'");
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    int count = cursor.getInt(0);
                    if (count > 0) {
                        tableCheckedStatus = true;
                        return tableCheckedStatus;
                    }
                }
            } catch (Throwable e) {
                throw new DbException(e);
            } finally {
                IOUtil.closeQuietly(cursor);
            }
        }

        tableCheckedStatus = false;
        return tableCheckedStatus;
    }

    public void createTableIfNotExists() throws DbException {
        if (tableCheckedStatus != null && tableCheckedStatus) return;
        synchronized (entityType) {
            if (!this.tableIsExists(true)) {
                SqlInfo sqlInfo = SqlInfoBuilder.buildCreateTableSqlInfo(this);
                db.execNonQuery(sqlInfo);
                tableCheckedStatus = true;

                if (!TextUtils.isEmpty(onCreated)) {
                    db.execNonQuery(onCreated);
                }

                DbManager.TableCreateListener listener = db.getDaoConfig().getTableCreateListener();
                if (listener != null) {
                    try {
                        listener.onTableCreated(db, this);
                    } catch (Throwable ex) {
                        Logger.e(ex.getMessage(), ex.getMessage());
                    }
                }
            }
        }
    }

    public DbManager getDb() {
        return db;
    }

    public String getName() {
        return name;
    }

    public Class<T> getEntityType() {
        return entityType;
    }

    public String getOnCreated() {
        return onCreated;
    }

    public ColumnEntity getId() {
        return id;
    }

    public LinkedHashMap<String, ColumnEntity> getColumnMap() {
        return columnMap;
    }

    /*package*/ void setTableCheckedStatus(boolean tableCheckedStatus) {
        this.tableCheckedStatus = tableCheckedStatus;
    }

    @Override
    public String toString() {
        return name;
    }
}

