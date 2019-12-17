package com.alliky.core.dialog.interfaces;

import android.view.View;

import com.alliky.core.dialog.TDialog;
import com.alliky.core.dialog.base.BindViewHolder;

public interface OnViewClickListener {
    void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog);
}
