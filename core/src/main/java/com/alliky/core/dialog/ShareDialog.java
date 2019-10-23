package com.alliky.core.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.alliky.core.R;
import com.alliky.core.dialog.interfaces.OnDismissListener;
import com.alliky.core.dialog.interfaces.OnShowListener;
import com.alliky.core.dialog.util.BaseDialog;
import com.alliky.core.dialog.util.DialogSettings;
import com.alliky.core.dialog.util.TextInfo;
import com.alliky.core.dialog.util.view.BlurView;
import com.alliky.core.dialog.util.view.IOSItemImageView;
import com.alliky.core.dialog.util.view.TableLayout;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.alliky.core.dialog.util.DialogSettings.blurAlpha;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/10/8 14:47
 */
public class ShareDialog extends BaseDialog {
    
    private OnItemClickListener onItemClickListener;
    
    private String title = "分享";
    private List<Item> items;
    private String cancelButtonText = DialogSettings.defaultCancelButtonText;
    
    private TextInfo titleTextInfo;
    private TextInfo itemTextInfo;
    private TextInfo cancelButtonTextInfo;
    
    private LinearLayout boxBody;
    private RelativeLayout boxShare;
    private TextView txtTitle;
    private RelativeLayout boxCustom;
    private ImageView titleSplitLine;
    private ViewGroup boxItem;
    private ViewGroup boxCancel;
    private TextView btnCancel;
    
    private ShareDialog() {
    }
    
    public static ShareDialog build(@NonNull AppCompatActivity context) {
        synchronized (ShareDialog.class) {
            ShareDialog shareDialog = new ShareDialog();
            shareDialog.log("装载分享框: " + shareDialog.toString() );
            shareDialog.context = new WeakReference<>(context);
            
            switch (shareDialog.style) {
                case STYLE_IOS:
                    shareDialog.build(shareDialog, R.layout.dialog_share_ios);
                    break;
                case STYLE_KONGZUE:
                    shareDialog.build(shareDialog, R.layout.dialog_share);
                    break;
                case STYLE_MATERIAL:
                    shareDialog.build(shareDialog, R.layout.dialog_share_material);
                    break;
            }
            return shareDialog;
        }
    }
    
    public static ShareDialog show(@NonNull AppCompatActivity context, List<Item> itemList, OnItemClickListener onItemClickListener) {
        ShareDialog shareDialog = build(context);
        shareDialog.items = itemList;
        shareDialog.onItemClickListener = onItemClickListener;
        shareDialog.show();
        return shareDialog;
    }
    
    private View rootView;
    
    @Override
    public void bindView(View rootView) {
        log("启动分享框 -> " + toString());
        this.rootView = rootView;
        if (boxCustom != null) boxCustom.removeAllViews();
        boxBody = rootView.findViewById(R.id.box_body);
        boxShare = rootView.findViewById(R.id.box_share);
        txtTitle = rootView.findViewById(R.id.txt_title);
        boxCustom = rootView.findViewById(R.id.box_custom);
        titleSplitLine = rootView.findViewById(R.id.title_split_line);
        boxItem = rootView.findViewById(R.id.box_item);
        boxCancel = rootView.findViewById(R.id.box_cancel);
        btnCancel = rootView.findViewById(R.id.btn_cancel);
        
        refreshView();
        if (onShowListener != null) onShowListener.onShow(this);
    }
    
    private BlurView blurContent;
    private BlurView blurCancel;
    
    private boolean isTouchDown;
    private float touchDownY;
    
    @Override
    public void refreshView() {
        if (cancelButtonTextInfo == null) cancelButtonTextInfo = buttonTextInfo;
        if (titleTextInfo == null) titleTextInfo = super.titleTextInfo;
        if (itemTextInfo == null) itemTextInfo = messageTextInfo;
        if (cancelButtonText==null) cancelButtonText = "取消";
        
        if (rootView != null) {
            switch (style) {
                case STYLE_IOS:
                    
                    final int bkgResId, blurFrontColor;
                    if (theme == DialogSettings.THEME.LIGHT) {
                        bkgResId = R.drawable.rect_menu_bkg_ios;
                        blurFrontColor = Color.argb(blurAlpha, 244, 245, 246);
                        btnCancel.setBackgroundResource(R.drawable.button_menu_ios_light);
                        titleSplitLine.setBackgroundColor(context.get().getResources().getColor(R.color.dialogSplitIOSLight));
                    } else {
                        bkgResId = R.drawable.rect_menu_bkg_ios;
                        blurFrontColor = Color.argb(blurAlpha + 10, 22, 22, 22);
                        btnCancel.setBackgroundResource(R.drawable.button_menu_ios_dark);
                        titleSplitLine.setBackgroundColor(context.get().getResources().getColor(R.color.dialogSplitIOSDark));
                    }
                    
                    if (DialogSettings.isUseBlur) {
                        boxShare.post(new Runnable() {
                            @Override
                            public void run() {
                                blurContent = new BlurView(context.get(), null);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, boxShare.getHeight());
                                blurContent.setOverlayColor(blurFrontColor);
                                blurContent.setRadius(context.get(), 11, 11);
                                boxShare.addView(blurContent, 0, params);
                            }
                        });
                        boxCancel.post(new Runnable() {
                            @Override
                            public void run() {
                                blurCancel = new BlurView(context.get(), null);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, boxCancel.getHeight());
                                blurCancel.setOverlayColor(blurFrontColor);
                                blurCancel.setRadius(context.get(), 11, 11);
                                boxCancel.addView(blurCancel, 0, params);
                            }
                        });
                    } else {
                        boxShare.setBackgroundResource(bkgResId);
                        boxCancel.setBackgroundResource(bkgResId);
                    }
                    
                    if (items != null) {
                        boxItem.removeAllViews();
                        for (int i = 0; i < items.size(); i++) {
                            final Item item = items.get(i);
                            View itemView = LayoutInflater.from(context.get()).inflate(R.layout.item_share_ios, null);
                            
                            final IOSItemImageView imgIcon = itemView.findViewById(R.id.img_icon);
                            TextView txtLabel = itemView.findViewById(R.id.txt_label);
                            
                            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.get().getResources(), zoomImg(item.getIcon(), dip2px(57), dip2px(57)));
                            roundedBitmapDrawable.setCornerRadius(dip2px(13));
                            imgIcon.setImageDrawable(roundedBitmapDrawable);
                            txtLabel.setText(item.getText());
                            
                            if (theme == DialogSettings.THEME.DARK) {
                                txtLabel.setTextColor(Color.WHITE);
                            } else {
                                txtLabel.setTextColor(Color.BLACK);
                            }
                            
                            final int index = i;
                            itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (onItemClickListener != null) {
                                        if (!onItemClickListener.onClick(ShareDialog.this, index, item)) {
                                            doDismiss();
                                        }
                                    } else {
                                        doDismiss();
                                    }
                                }
                            });
                            
                            itemView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    switch (event.getAction()) {
                                        case MotionEvent.ACTION_DOWN:
                                            imgIcon.setFilter();
                                            break;
                                        case MotionEvent.ACTION_UP:
                                            imgIcon.removeFilter();
                                            break;
                                        case MotionEvent.ACTION_CANCEL:
                                            imgIcon.removeFilter();
                                            break;
                                    }
                                    return false;
                                }
                            });
                            
                            boxItem.addView(itemView);
                        }
                    }
                    break;
                case STYLE_MATERIAL:
                    if (items != null) {
                        boxItem.removeAllViews();
                        for (int i = 0; i < items.size(); i++) {
                            final Item item = items.get(i);
                            final View itemView = LayoutInflater.from(context.get()).inflate(R.layout.item_share_material, null);
                            
                            final ImageView imgIcon = itemView.findViewById(R.id.img_icon);
                            TextView txtLabel = itemView.findViewById(R.id.txt_label);
                            
                            imgIcon.setImageBitmap(item.getIcon());
                            txtLabel.setText(item.getText());
                            
                            final int index = i;
                            itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (onItemClickListener != null) {
                                        if (!onItemClickListener.onClick(ShareDialog.this, index, item)) {
                                            doDismiss();
                                        }
                                    } else {
                                        doDismiss();
                                    }
                                }
                            });
                            
                            boxItem.addView(itemView);
                        }
                        
                        Window window = dialog.getDialog().getWindow();
                        WindowManager windowManager = context.get().getWindowManager();
                        Display display = windowManager.getDefaultDisplay();
                        WindowManager.LayoutParams lp = window.getAttributes();
                        lp.width = display.getWidth();
                        lp.height = display.getHeight() - getStatusBarHeight();
                        window.setGravity(Gravity.BOTTOM);
                        window.setAttributes(lp);
                        
                        boxBody.setY(boxBody.getHeight());
                        boxBody.post(new Runnable() {
                            @Override
                            public void run() {
                                boxBody.animate().setDuration(300).translationY(boxBody.getHeight() / 2);
                            }
                        });
                        
                        boxBody.setOnTouchListener(materialScrollTouchListener);
                        
                        rootView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                doDismiss();
                            }
                        });
                        
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            dialog.getDialog().getWindow().setNavigationBarColor(Color.WHITE);
                            boxBody.setPadding(0, 0, 0, getNavigationBarHeight());
                        }
                    }
                    break;
                case STYLE_KONGZUE:
                    if (items != null) {
                        boxItem.removeAllViews();
                        ((TableLayout) boxItem).setAutoHeight(true);
                        for (int i = 0; i < items.size(); i++) {
                            final Item item = items.get(i);
                            final View itemView = LayoutInflater.from(context.get()).inflate(R.layout.item_share, null);
                            itemView.setBackgroundColor(Color.WHITE);
                            
                            final IOSItemImageView imgIcon = itemView.findViewById(R.id.img_icon);
                            TextView txtLabel = itemView.findViewById(R.id.txt_label);
                            
                            imgIcon.setImageBitmap(item.getIcon());
                            txtLabel.setText(item.getText());
                            
                            final int index = i;
                            itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (onItemClickListener != null) {
                                        if (!onItemClickListener.onClick(ShareDialog.this, index, item)) {
                                            doDismiss();
                                        }
                                    } else {
                                        doDismiss();
                                    }
                                }
                            });
                            
                            itemView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    switch (event.getAction()) {
                                        case MotionEvent.ACTION_DOWN:
                                            imgIcon.setFilter();
                                            break;
                                        case MotionEvent.ACTION_UP:
                                            imgIcon.removeFilter();
                                            break;
                                        case MotionEvent.ACTION_CANCEL:
                                            imgIcon.removeFilter();
                                            break;
                                    }
                                    return false;
                                }
                            });
                            
                            boxItem.addView(itemView);
                        }
    
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Window window = dialog.getDialog().getWindow();
                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            dialog.getDialog().getWindow().setNavigationBarColor(Color.WHITE);
                            boxBody.setPadding(0, 0, 0, getNavigationBarHeight());
                        }
                    }
                    break;
            }
            
            if (!isNull(title)) {
                txtTitle.setText(title);
                txtTitle.setVisibility(View.VISIBLE);
                if (titleSplitLine != null) titleSplitLine.setVisibility(View.VISIBLE);
            }
            
            if (btnCancel != null) {
                btnCancel.setText(cancelButtonText);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doDismiss();
                    }
                });
            }
            if (customView != null) {
                boxCustom.removeAllViews();
                boxCustom.addView(customView);
                if (onBindView != null) onBindView.onBind(this, customView);
                boxCustom.setVisibility(View.VISIBLE);
                if (titleSplitLine != null) titleSplitLine.setVisibility(View.VISIBLE);
            } else {
                boxCustom.setVisibility(View.GONE);
            }
        }
    }
    
    private float boxBodyOldY;
    private int step;
    
    private View.OnTouchListener materialScrollTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isTouchDown = true;
                    touchDownY = event.getY();
                    boxBodyOldY = boxBody.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isTouchDown) {
                        float deltaY = event.getY() - touchDownY;
                        float aimY = boxBody.getY() + deltaY;
                        if (aimY < 0) aimY = 0;
                        boxBody.setY(aimY);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (isTouchDown) {
                        float deltaY = boxBody.getY() - boxBodyOldY;
                        if (deltaY < -dip2px(50)) {
                            //向上
                            switch (step) {
                                case 0:
                                    boxBody.animate().setDuration(300).translationY(0);
                                    step = 1;
                                    break;
                            }
                        }
                        if (deltaY > dip2px(150)) {
                            boxBody.animate().setDuration(300).translationY(boxBody.getHeight()).withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    doDismiss();
                                }
                            });
                        } else if (deltaY > dip2px(50)) {
                            //向下
                            switch (step) {
                                case 0:
                                    boxBody.animate().setDuration(300).translationY(boxBody.getHeight()).withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            doDismiss();
                                        }
                                    });
                                    break;
                                case 1:
                                    boxBody.animate().setDuration(300).translationY(boxBody.getHeight() / 2);
                                    step = 0;
                                    break;
                            }
                        }
                        if (deltaY >= -dip2px(50) && deltaY <= dip2px(50)) {
                            boxBody.animate().setDuration(300).translationY(boxBodyOldY);
                            step = 0;
                        }
                    }
                    isTouchDown = false;
                    break;
            }
            return true;
        }
    };
    
    public List<Item> getItems() {
        return items;
    }
    
    public ShareDialog setItems(List<Item> items) {
        this.items = items;
        refreshView();
        return this;
    }
    
    public ShareDialog addItem(Item item) {
        if (items == null) items = new ArrayList<>();
        items.add(item);
        refreshView();
        return this;
    }
    
    @Override
    public void show() {
        showDialog();
    }
    
    public static class Item {
        private Bitmap icon;
        private String text;
        
        public Item(Bitmap icon, String text) {
            this.icon = icon;
            this.text = text;
        }
        
        public Item(Context context, int iconResId, String text) {
            this.icon = BitmapFactory.decodeResource(context.getResources(), iconResId);
            this.text = text;
        }
        
        public Bitmap getIcon() {
            return icon;
        }
        
        public Item setIcon(Bitmap icon) {
            this.icon = icon;
            return this;
        }
        
        public String getText() {
            return text;
        }
        
        public Item setText(String text) {
            this.text = text;
            return this;
        }
    
        @Override
        public String toString() {
            return "Item{" +
                    "text='" + text + '\'' +
                    '}';
        }
    }
    
    private Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
    
    //其他设置
    public DialogSettings.STYLE getStyle() {
        return style;
    }
    
    public ShareDialog setStyle(DialogSettings.STYLE style) {
        if (isAlreadyShown) {
            error("必须使用 build(...) 方法创建时，才可以使用 setStyle(...) 来修改对话框主题或风格。");
            return this;
        }
        
        this.style = style;
        switch (this.style) {
            case STYLE_IOS:
                build(this, R.layout.dialog_share_ios);
                break;
            case STYLE_KONGZUE:
                build(this, R.layout.dialog_share);
                break;
            case STYLE_MATERIAL:
                build(this, R.layout.dialog_share_material);
                break;
        }
        
        return this;
    }
    
    public DialogSettings.THEME getTheme() {
        return theme;
    }
    
    public ShareDialog setTheme(DialogSettings.THEME theme) {
        
        if (isAlreadyShown) {
            error("必须使用 build(...) 方法创建时，才可以使用 setTheme(...) 来修改对话框主题或风格。");
            return this;
        }
        
        this.theme = theme;
        refreshView();
        return this;
    }
    
    public String getTitle() {
        return title;
    }
    
    public ShareDialog setTitle(String title) {
        this.title = title;
        refreshView();
        return this;
    }
    
    public ShareDialog setTitle(int titleResId) {
        this.title = context.get().getString(titleResId);
        refreshView();
        return this;
    }
    
    public String getCancelButtonText() {
        return cancelButtonText;
    }
    
    public ShareDialog setCancelButtonText(String cancelButtonText) {
        this.cancelButtonText = cancelButtonText;
        refreshView();
        return this;
    }
    
    public ShareDialog setCancelButtonText(int cancelButtonTextResId) {
        this.cancelButtonText = context.get().getString(cancelButtonTextResId);
        refreshView();
        return this;
    }
    
    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }
    
    public ShareDialog setTitleTextInfo(TextInfo menuTitleTextInfo) {
        this.titleTextInfo = menuTitleTextInfo;
        refreshView();
        return this;
    }
    
    public TextInfo getItemTextInfo() {
        return itemTextInfo;
    }
    
    public ShareDialog setItemTextInfo(TextInfo itemTextInfo) {
        this.itemTextInfo = itemTextInfo;
        refreshView();
        return this;
    }
    
    public TextInfo getCancelButtonTextInfo() {
        return cancelButtonTextInfo;
    }
    
    public ShareDialog setCancelButtonTextInfo(TextInfo cancelButtonTextInfo) {
        this.cancelButtonTextInfo = cancelButtonTextInfo;
        refreshView();
        return this;
    }
    
    public OnDismissListener getOnDismissListener() {
        return onDismissListener == null ? new OnDismissListener() {
            @Override
            public void onDismiss() {
            
            }
        } : onDismissListener;
    }
    
    public ShareDialog setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }
    
    public OnShowListener getOnShowListener() {
        return onShowListener == null ? new OnShowListener() {
            @Override
            public void onShow(BaseDialog dialog) {
            
            }
        } : onShowListener;
    }
    
    public ShareDialog setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
        return this;
    }
    
    public View getCustomView() {
        return customView;
    }
    
    public ShareDialog setCustomView(View customView) {
        this.customView = customView;
        refreshView();
        return this;
    }
    
    private OnBindView onBindView;
    
    public ShareDialog setCustomView(int customViewLayoutId, OnBindView onBindView) {
        customView = LayoutInflater.from(context.get()).inflate(customViewLayoutId, null);
        this.onBindView = onBindView;
        refreshView();
        return this;
    }
    
    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }
    
    public ShareDialog setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }
    
    public interface OnBindView {
        void onBind(ShareDialog shareDialog, View v);
    }
    
    public interface OnItemClickListener {
        boolean onClick(ShareDialog shareDialog, int index, Item item);
    }
    
    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.get().getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public ShareDialog setCustomDialogStyleId(int customDialogStyleId) {
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
