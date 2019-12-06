package com.alliky.core.db.sqlite;

import android.text.TextUtils;

import com.alliky.core.db.table.ColumnUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:54
 */
public class WhereBuilder {
    private final List<String> whereItems;

    private WhereBuilder() {
        this.whereItems = new ArrayList<String>();
    }

    /**
     * create new instance
     */
    public static WhereBuilder b() {
        return new WhereBuilder();
    }

    /**
     * create new instance
     *
     * @param op operator: "=","LIKE","IN","BETWEEN"...
     */
    public static WhereBuilder b(String columnName, String op, Object value) {
        WhereBuilder result = new WhereBuilder();
        result.appendCondition(null, columnName, op, value);
        return result;
    }

    /**
     * add AND condition
     *
     * @param op operator: "=","LIKE","IN","BETWEEN"...
     */
    public WhereBuilder and(String columnName, String op, Object value) {
        appendCondition(whereItems.size() == 0 ? null : "AND", columnName, op, value);
        return this;
    }

    /**
     * add AND condition
     *
     * @param where expr("[AND] (" + where.toString() + ")")
     */
    public WhereBuilder and(WhereBuilder where) {
        String condition = whereItems.size() == 0 ? " " : "AND ";
        return expr(condition + "(" + where.toString() + ")");
    }

    /**
     * add OR condition
     *
     * @param op operator: "=","LIKE","IN","BETWEEN"...
     */
    public WhereBuilder or(String columnName, String op, Object value) {
        appendCondition(whereItems.size() == 0 ? null : "OR", columnName, op, value);
        return this;
    }

    /**
     * add OR condition
     *
     * @param where expr("[OR] (" + where.toString() + ")")
     */
    public WhereBuilder or(WhereBuilder where) {
        String condition = whereItems.size() == 0 ? " " : "OR ";
        return expr(condition + "(" + where.toString() + ")");
    }

    public WhereBuilder expr(String expr) {
        whereItems.add(" " + expr);
        return this;
    }

    public int getWhereItemSize() {
        return whereItems.size();
    }

    @Override
    public String toString() {
        if (whereItems.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String item : whereItems) {
            sb.append(item);
        }
        return sb.toString();
    }

    private void appendCondition(String conj, String columnName, String op, Object value) {
        StringBuilder builder = new StringBuilder();

        if (whereItems.size() > 0) {
            builder.append(" ");
        }

        // append conj
        if (!TextUtils.isEmpty(conj)) {
            builder.append(conj).append(" ");
        }

        // append columnName
        builder.append("\"").append(columnName).append("\"");

        // convert op
        if ("!=".equals(op)) {
            op = "<>";
        } else if ("==".equals(op)) {
            op = "=";
        }

        // append op & value
        if (value == null) {
            if ("=".equals(op)) {
                builder.append(" IS NULL");
            } else if ("<>".equals(op)) {
                builder.append(" IS NOT NULL");
            } else {
                builder.append(" ").append(op).append(" NULL");
            }
        } else {
            builder.append(" ").append(op).append(" ");

            if ("IN".equalsIgnoreCase(op)) {
                Iterable<?> items = null;
                if (value instanceof Iterable) {
                    items = (Iterable<?>) value;
                } else if (value.getClass().isArray()) {
                    int len = Array.getLength(value);
                    List<Object> arrayList = new ArrayList<Object>(len);
                    for (int i = 0; i < len; i++) {
                        arrayList.add(Array.get(value, i));
                    }
                    items = arrayList;
                }
                if (items != null) {
                    StringBuilder inSb = new StringBuilder("(");
                    for (Object item : items) {
                        Object itemColValue = ColumnUtils.convert2DbValueIfNeeded(item);
                        if (ColumnUtils.isTextColumnDbType(itemColValue)) {
                            String valueStr = ColumnUtils.convert2SafeExpr(itemColValue);
                            inSb.append("'").append(valueStr).append("'");
                        } else {
                            inSb.append(itemColValue);
                        }
                        inSb.append(",");
                    }
                    if (inSb.length() > 1) {
                        inSb.deleteCharAt(inSb.length() - 1);
                    }
                    inSb.append(")");
                    builder.append(inSb.toString());
                } else {
                    throw new IllegalArgumentException("value must be an Array or an Iterable.");
                }
            } else if ("BETWEEN".equalsIgnoreCase(op)) {
                Iterable<?> items = null;
                if (value instanceof Iterable) {
                    items = (Iterable<?>) value;
                } else if (value.getClass().isArray()) {
                    int len = Array.getLength(value);
                    List<Object> arrayList = new ArrayList<Object>(len);
                    for (int i = 0; i < len; i++) {
                        arrayList.add(Array.get(value, i));
                    }
                    items = arrayList;
                }
                if (items != null) {
                    Iterator<?> iterator = items.iterator();
                    if (!iterator.hasNext())
                        throw new IllegalArgumentException("value must contains tow items.");
                    Object start = iterator.next();
                    if (!iterator.hasNext())
                        throw new IllegalArgumentException("value must contains tow items.");
                    Object end = iterator.next();

                    Object startColValue = ColumnUtils.convert2DbValueIfNeeded(start);
                    Object endColValue = ColumnUtils.convert2DbValueIfNeeded(end);

                    if (ColumnUtils.isTextColumnDbType(startColValue)) {
                        String startStr = ColumnUtils.convert2SafeExpr(startColValue);
                        String endStr = ColumnUtils.convert2SafeExpr(endColValue);
                        builder.append("'").append(startStr).append("'");
                        builder.append(" AND ");
                        builder.append("'").append(endStr).append("'");
                    } else {
                        builder.append(startColValue);
                        builder.append(" AND ");
                        builder.append(endColValue);
                    }
                } else {
                    throw new IllegalArgumentException("value must be an Array or an Iterable.");
                }
            } else {
                value = ColumnUtils.convert2DbValueIfNeeded(value);
                if (ColumnUtils.isTextColumnDbType(value)) {
                    String valueStr = ColumnUtils.convert2SafeExpr(value);
                    builder.append("'").append(valueStr).append("'");
                } else {
                    builder.append(value);
                }
            }
        }
        whereItems.add(builder.toString());
    }
}
