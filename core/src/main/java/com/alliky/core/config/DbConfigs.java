package com.alliky.core.config;

import com.alliky.core.db.DbManager;
import com.alliky.core.ex.DbException;
import com.alliky.core.util.Logger;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 11:24
 */
public enum  DbConfigs {
    HTTP(new DbManager.DaoConfig()
            .setDbName("http_cache.db")
            .setDbVersion(2)
            .setDbOpenListener(new DbManager.DbOpenListener() {
        @Override
        public void onDbOpened(DbManager db) {
            db.getDatabase().enableWriteAheadLogging();
        }
    })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
        @Override
        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
            try {
                db.dropDb(); // 默认删除所有表
            } catch (DbException ex) {
                Logger.e(ex.getMessage(), ex.getMessage());
            }
        }
    })),

    COOKIE(new DbManager.DaoConfig()
            .setDbName("http_cookie.db")
            .setDbVersion(1)
            .setDbOpenListener(new DbManager.DbOpenListener() {
        @Override
        public void onDbOpened(DbManager db) {
            db.getDatabase().enableWriteAheadLogging();
        }
    })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
        @Override
        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
            try {
                db.dropDb(); // 默认删除所有表
            } catch (DbException ex) {
                Logger.e(ex.getMessage(), ex.getMessage());
            }
        }
    }));

    private DbManager.DaoConfig config;

    DbConfigs(DbManager.DaoConfig config) {
        this.config = config;
    }

    public DbManager.DaoConfig getConfig() {
        return config;
    }
}
