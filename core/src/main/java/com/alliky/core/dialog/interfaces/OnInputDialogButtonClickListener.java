package com.alliky.core.dialog.interfaces;

import android.view.View;

import com.alliky.core.dialog.util.BaseDialog;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/10/8 14:47
 */
public interface OnInputDialogButtonClickListener {
    
    boolean onClick(BaseDialog baseDialog, View v, String inputStr);
}
