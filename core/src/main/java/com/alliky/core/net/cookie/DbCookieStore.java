package com.alliky.core.net.cookie;

import android.text.TextUtils;

import com.alliky.core.config.DbConfigs;
import com.alliky.core.db.DbManager;
import com.alliky.core.db.Selector;
import com.alliky.core.db.sqlite.WhereBuilder;
import com.alliky.core.db.table.DbModel;
import com.alliky.core.net.common.task.PriorityExecutor;
import com.alliky.core.net.Utils;
import com.alliky.core.util.Logger;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 11:28
 */
public enum DbCookieStore implements CookieStore {

    INSTANCE;

    private DbManager db;
    private final Executor trimExecutor = new PriorityExecutor(1, true);
    private static final int LIMIT_COUNT = 5000; // 限制最多5000条数据

    private long lastTrimTime = 0L;
    private static final long TRIM_TIME_SPAN = 1000;

    DbCookieStore() {
        Utils.task().run(new Runnable() {
            @Override
            public void run() {
                tryInit();
            }
        });
    }

    private void tryInit() {
        if (db == null) {
            synchronized (this) {
                if (db == null) {
                    try {
                        db = Utils.getDb(DbConfigs.COOKIE.getConfig());
                        db.delete(CookieEntity.class,
                                WhereBuilder.b("expiry", "=", -1L));
                    } catch (Throwable ex) {
                        Logger.e(ex.getMessage(), ex.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Add one cookie into cookie store.
     */
    @Override
    public void add(URI uri, HttpCookie cookie) {
        if (cookie == null) {
            return;
        }

        tryInit();

        uri = getEffectiveURI(uri);

        try {
            db.replace(new CookieEntity(uri, cookie));
        } catch (Throwable ex) {
            Logger.e(ex.getMessage(), ex.getMessage());
        }

        trimSize();
    }


    /**
     * Get all cookies, which:
     * 1) given uri domain-matches with, or, associated with
     * 2) given uri when added to the cookie store.
     * 3) not expired.
     * See RFC 2965 sec. 3.3.4 for more detail.
     */
    @Override
    public List<HttpCookie> get(URI uri) {
        // argument can't be null
        if (uri == null) {
            throw new NullPointerException("uri is null");
        }

        tryInit();

        uri = getEffectiveURI(uri);

        List<HttpCookie> rt = new ArrayList<HttpCookie>();

        try {

            Selector<CookieEntity> selector = db.selector(CookieEntity.class);

            WhereBuilder where = WhereBuilder.b();

            String host = uri.getHost();
            if (!TextUtils.isEmpty(host)) {
                WhereBuilder subWhere = WhereBuilder.b("domain", "=", host).or("domain", "=", "." + host);
                int firstDot = host.indexOf(".");
                int lastDot = host.lastIndexOf(".");
                if (firstDot > 0 && lastDot > firstDot) {
                    String domain = host.substring(firstDot, host.length());
                    if (!TextUtils.isEmpty(domain)) {
                        subWhere.or("domain", "=", domain);
                    }
                }
                where.and(subWhere);
            }

            String path = uri.getPath();
            if (!TextUtils.isEmpty(path)) {
                WhereBuilder subWhere = WhereBuilder.b("path", "=", path)
                        .or("path", "=", "/").or("path", "=", null);
                int lastSplit = path.lastIndexOf("/");
                while (lastSplit > 0) {
                    path = path.substring(0, lastSplit);
                    subWhere.or("path", "=", path);
                    lastSplit = path.lastIndexOf("/");
                }

                where.and(subWhere);
            }

            where.or("uri", "=", uri.toString());

            List<CookieEntity> cookieEntityList = selector.where(where).findAll();
            if (cookieEntityList != null) {
                for (CookieEntity cookieEntity : cookieEntityList) {
                    if (!cookieEntity.isExpired()) {
                        rt.add(cookieEntity.toHttpCookie());
                    }
                }
            }
        } catch (Throwable ex) {
            Logger.e(ex.getMessage(), ex.getMessage());
        }
        return rt;
    }

    /**
     * Get all cookies in cookie store, except those have expired
     */
    @Override
    public List<HttpCookie> getCookies() {
        tryInit();

        List<HttpCookie> rt = new ArrayList<HttpCookie>();

        try {
            List<CookieEntity> cookieEntityList = db.findAll(CookieEntity.class);
            if (cookieEntityList != null) {
                for (CookieEntity cookieEntity : cookieEntityList) {
                    if (!cookieEntity.isExpired()) {
                        rt.add(cookieEntity.toHttpCookie());
                    }
                }
            }
        } catch (Throwable ex) {
            Logger.e(ex.getMessage(), ex.getMessage());
        }


        return rt;
    }

    /**
     * Get all URIs, which are associated with at least one cookie
     * of this cookie store.
     */
    @Override
    public List<URI> getURIs() {
        tryInit();

        List<URI> uris = new ArrayList<URI>();

        try {
            List<DbModel> uriList =
                    db.selector(CookieEntity.class).select("uri").findAll();
            if (uriList != null) {
                for (DbModel model : uriList) {
                    String uri = model.getString("uri");
                    if (!TextUtils.isEmpty(uri)) {
                        try {
                            uris.add(new URI(uri));
                        } catch (Throwable ex) {
                            Logger.e(ex.getMessage(), ex.getMessage());
                            try {
                                db.delete(CookieEntity.class, WhereBuilder.b("uri", "=", uri));
                            } catch (Throwable throwable) {
                                Logger.e(throwable.getMessage(), throwable.getMessage());
                            }
                        }
                    }
                }
            }
        } catch (Throwable ex) {
            Logger.e(ex.getMessage(), ex.getMessage());
        }

        return uris;
    }


    /**
     * Remove a cookie from store
     */
    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        if (cookie == null) {
            return true;
        }

        tryInit();

        boolean modified = false;
        try {
            WhereBuilder where = WhereBuilder.b("name", "=", cookie.getName());

            String domain = cookie.getDomain();
            if (!TextUtils.isEmpty(domain)) {
                where.and("domain", "=", domain);
            }

            String path = cookie.getPath();
            if (!TextUtils.isEmpty(path)) {
                if (path.length() > 1 && path.endsWith("/")) {
                    path = path.substring(0, path.length() - 1);
                }
                where.and("path", "=", path);
            }

            db.delete(CookieEntity.class, where);

            modified = true;
        } catch (Throwable ex) {
            Logger.e(ex.getMessage(), ex.getMessage());
        }

        return modified;
    }


    /**
     * Remove all cookies in this cookie store.
     */
    @Override
    public boolean removeAll() {
        tryInit();

        try {
            db.delete(CookieEntity.class);
        } catch (Throwable ex) {
            Logger.e(ex.getMessage(), ex.getMessage());
        }
        return true;
    }

    private void trimSize() {
        trimExecutor.execute(new Runnable() {
            @Override
            public void run() {
                tryInit();

                long current = System.currentTimeMillis();
                if (current - lastTrimTime < TRIM_TIME_SPAN) {
                    return;
                } else {
                    lastTrimTime = current;
                }

                // delete expires
                try {
                    db.delete(CookieEntity.class, WhereBuilder
                            .b("expiry", "<", System.currentTimeMillis())
                            .and("expiry", "!=", -1L));
                } catch (Throwable ex) {
                    Logger.e(ex.getMessage(), ex.getMessage());
                }

                // trim by limit count
                try {
                    int count = (int) db.selector(CookieEntity.class).count();
                    if (count > LIMIT_COUNT + 10) {
                        List<CookieEntity> rmList = db.selector(CookieEntity.class)
                                .where("expiry", "!=", -1L).orderBy("expiry")
                                .limit(count - LIMIT_COUNT).findAll();
                        if (rmList != null) {
                            db.delete(rmList);
                        }
                    }
                } catch (Throwable ex) {
                    Logger.e(ex.getMessage(), ex.getMessage());
                }
            }
        });
    }

    private URI getEffectiveURI(final URI uri) {
        URI effectiveURI = null;
        try {
            effectiveURI = new URI("http",
                    uri.getHost(),
                    uri.getPath(),
                    null,  // query component
                    null   // fragment component
            );
        } catch (Throwable ex) {
            Logger.w(ex.getMessage(), ex.getMessage());
            effectiveURI = uri;
        }

        return effectiveURI;
    }
}

