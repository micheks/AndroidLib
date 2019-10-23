package com.alliky.core.dialog.util.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.alliky.core.dialog.interfaces.OnNotificationClickListener;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/10/8 14:47
 */
public class NotifyToastShadowView extends RelativeLayout {
    
    private Activity activity;
    private int notifyHeight;
    private OnNotificationClickListener onNotificationClickListener;
    private boolean dispatchTouchEvent = true;
    
    public NotifyToastShadowView(Context context) {
        super(context);
    }
    
    public NotifyToastShadowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public NotifyToastShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (dispatchTouchEvent) {
            if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_DOWN || ev.getAction() == MotionEvent.ACTION_UP) {
                if (ev.getY() < notifyHeight) {
                    if (onNotificationClickListener != null) onNotificationClickListener.onClick();
                    onNotificationClickListener = null;
                    return super.dispatchTouchEvent(ev);
                } else {
                    if (activity != null) activity.dispatchTouchEvent(ev);
                    return false;
                }
            } else {
                return super.dispatchTouchEvent(ev);
            }
        }else{
            return super.dispatchTouchEvent(ev);
        }
    }
    
    public void setParent(Context c) {
        if (c instanceof Activity) this.activity = (Activity) c;
    }
    
    public void setNotifyHeight(int notifyHeight) {
        this.notifyHeight = notifyHeight;
    }
    
    public void setOnNotificationClickListener(OnNotificationClickListener onNotificationClickListener) {
        this.onNotificationClickListener = onNotificationClickListener;
    }
    
    public void setDispatchTouchEvent(boolean dispatchTouchEvent) {
        this.dispatchTouchEvent = dispatchTouchEvent;
    }
}
