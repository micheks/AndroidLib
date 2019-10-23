package com.alliky.core.dialog;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/10/8 14:47
 */
public class WaitDialog extends TipDialog {
    
    private WaitDialog() {
    }
    
    public static TipDialog show(AppCompatActivity context, String message) {
        return TipDialog.showWait(context, message);
    }
    
    public static TipDialog show(AppCompatActivity context, int messageResId) {
        return TipDialog.showWait(context, messageResId);
    }
    
    @Override
    public void show() {
        setDismissEvent();
        showDialog();
    }
    
    public WaitDialog setCustomDialogStyleId(int customDialogStyleId) {
        if (isAlreadyShown) {
            error("必须使用 build(...) 方法创建时，才可以使用 setTheme(...) 来修改对话框主题或风格。");
            return this;
        }
        this.customDialogStyleId = customDialogStyleId;
        return this;
    }
    
    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }
}
