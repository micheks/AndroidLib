package com.alliky.core.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Author wxianing
 * date 2018/7/7
 */
public class CustomPopupWindow {

    private PopupWindow mPopupWindow;
    private View contentview;
    private static Context mContext;

    public CustomPopupWindow(Builder builder) {
        contentview = LayoutInflater.from(mContext).inflate(builder.contentviewid, null);
        mPopupWindow = new PopupWindow(contentview, builder.width, builder.height, builder.fouse);
        //需要跟 setBackGroundDrawable 结合
        mPopupWindow.setOutsideTouchable(builder.outsidecancel);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setAnimationStyle(builder.animstyle);
    }

    /**
     * popup 消失
     */
    public void dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    public boolean isShowing() {
        if (mPopupWindow != null) {
            return mPopupWindow.isShowing();
        }
        return false;
    }

    /**
     * 根据id获取view
     *
     * @param viewid
     * @return
     */
    public View getItemView(int viewid) {
        if (mPopupWindow != null) {
            return this.contentview.findViewById(viewid);
        }
        return null;
    }

    /**
     * 将弹出窗口固定在其上的视图
     *
     * @param view
     * @return
     */
    public CustomPopupWindow showAsDropDown(View view) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(view);
        }
        return this;
    }

    public CustomPopupWindow showAsDropDown(View anchor, int xoff, int yoff) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(anchor, xoff, yoff);
        }
        return this;
    }

    public CustomPopupWindow showAtLocation(View parent, int gravity, int xoff, int yoff) {
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(parent, gravity, xoff, yoff);
        }
        return this;
    }

    /**
     * 根据id设置焦点监听
     *
     * @param viewid
     * @param listener
     */
    public void setOnFocusListener(int viewid, View.OnFocusChangeListener listener) {
        View view = getItemView(viewid);
        view.setOnFocusChangeListener(listener);
    }

    /**
     * 监听popwindow消失
     *
     * @param onDismissListener
     */
    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        if (mPopupWindow != null) {
            mPopupWindow.setOnDismissListener(onDismissListener);
        }
    }

    /**
     * builder 类
     */
    public static class Builder {

        private int contentviewid;
        private int width;
        private int height;
        private boolean fouse;
        private boolean outsidecancel;
        private int animstyle;

        public Builder(Context context) {
            mContext = context;
        }
        //设置布局
        public Builder setContentView(int contentviewid) {
            this.contentviewid = contentviewid;
            return this;
        }
        //设置宽
        public Builder setwidth(int width) {
            this.width = width;
            return this;
        }
        //设置高
        public Builder setheight(int height) {
            this.height = height;
            return this;
        }
        //获取焦点
        public Builder setFouse(boolean fouse) {
            this.fouse = fouse;
            return this;
        }
        //取消外部点击消失
        public Builder setOutSideCancel(boolean outsidecancel) {
            this.outsidecancel = outsidecancel;
            return this;
        }
        //动画
        public Builder setAnimationStyle(int animstyle) {
            this.animstyle = animstyle;
            return this;
        }

        public CustomPopupWindow builder() {
            return new CustomPopupWindow(this);
        }
    }
}
