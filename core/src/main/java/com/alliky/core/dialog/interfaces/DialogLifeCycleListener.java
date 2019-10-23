package com.alliky.core.dialog.interfaces;

import com.alliky.core.dialog.util.BaseDialog;
/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/10/8 14:47
 */
public interface DialogLifeCycleListener {

    void onCreate(BaseDialog dialog);

    void onShow(BaseDialog dialog);

    void onDismiss(BaseDialog dialog);

}
