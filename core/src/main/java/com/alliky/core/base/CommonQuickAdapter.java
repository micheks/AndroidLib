package com.alliky.core.base;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @Description: DOTO 通用适配器
 * @Author: wxianing
 * @CreateDate: 2019/9/29 17:18
 */
public abstract class CommonQuickAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public CommonQuickAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, T item) {
        convertView(holder, item);
    }

    public abstract void convertView(BaseViewHolder holder, T item);
}
