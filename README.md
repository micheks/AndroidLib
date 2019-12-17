# AndroidLib
Android 一个基本类库，里面封装了一些常用工具，网络框架等。

## 如何使用？

### 步骤一：在项目build.gradle加上
```
repositories {
    maven { url 'https://jitpack.io' }
}
```
### 步骤二：在moduble的build.gradle加上

```
dependencies {
    implementation 'com.github.wxianing:AndroidLib:v1.1'
}
```

## 代码具体调用说明

### 一、要使用该框架，则必须先在Application中初始化配置，否则使用到配置APP会闪退，配置代码如下： 

``` 
public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化常用配置
        Kylin.init(this)
                .withLoaderDelayed(500)
                .withApiHost("http://api.alliky.com/")//你的服务域名或者IP
                .withInterceptor(new DebugInterceptor("test", R.raw.test))
                .configure();
    }
}

``` 

### 二、网络请求使用

#### 1.post请求方式使用

``` 
HttpClient.builder()
        .url("user/biz/specialCar/category")//请求地址url，不包含域名端口
        .params("cityName", "深圳市")//参数，可添加多个
        .loader(this)//loading加载动画
        .success(new ISuccess() {
            @Override
            public void onSuccess(String response) {
                //成功回调
                LogUtil.i(response);
            }
        })
        .failure(new IFailure() {
            @Override
            public void onFailure(Throwable t) {
                 //失败回调
            }
        })
        .error(new IError() {
            @Override
            public void onError(int code, String msg) {
                 //错误回调
            }
        })
        .build()
        .post();
                
``` 

#### 2.get请求方式使用

``` 
HttpClient.builder()
        .url("user/biz/specialCar/category")//请求地址url，不包含域名端口
        .params("cityName", "深圳市")//参数，可添加多个
        .loader(this)//loading加载动画
        .success(new ISuccess() {
            @Override
            public void onSuccess(String response) {
                 //成功回调
                 LogUtil.i(response);
            }
        })
        .failure(new IFailure() {
            @Override
            public void onFailure(Throwable t) {
                 //失败回调
            }
        })
        .error(new IError() {
            @Override
            public void onError(int code, String msg) {
                 //错误回调
            }
        })
        .build()
        .get();
                
``` 

#### 3.文件上传

``` 
HttpClient.builder()
        .url("user/biz/specialCar/category")//请求地址url，不包含域名端口
        .params("cityName", "深圳市")//参数，可添加多个
        .file(file)//要上传的文件
        .loader(this)//loading加载动画
        .success(new ISuccess() {
            @Override
            public void onSuccess(String response) {
                //成功回调
                LogUtil.i(response);
            }
        })
        .failure(new IFailure() {
            @Override
            public void onFailure(Throwable t) {
                //失败回调
            }
        })
        .error(new IError() {
            @Override
            public void onError(int code, String msg) {
                //错误回调
            }
        })
        .build()
        .upload();

``` 

#### 4.下载文件

``` 
HttpClient.builder()
        .url("user/biz/specialCar/category")//请求地址url，不包含域名端口
        .params("cityName", "深圳市")//参数，可添加多个
        .extension("apk")//文件后缀名
        .dir("/")//保存到文件夹
        .loader(this)//loading加载动画
        .success(new ISuccess() {
            @Override
            public void onSuccess(String response) {
                //成功回调
                LogUtil.i(response);
            }
        })
        .failure(new IFailure() {
            @Override
            public void onFailure(Throwable t) {
                //失败回调
            }
        })
        .error(new IError() {
            @Override
            public void onError(int code, String msg) {
                //错误回调
            }
        })
        .build()
        .download();
                
``` 

### 三、打开相机或者从相册中选择照片

#### 1.在onCreate方法中初始化PhotoUtil

``` 
//拍照货相册选择回调
PhotoUtils.getInstance().init(this, false, new PhotoUtil.OnSelectListener() {
    @Override
    public void onFinish(File outputFile, Uri outputUri) {
        //TODO 返回图片文件
    }
});

``` 

#### 2.在onActivityResult 调用  PhotoUtils.getInstance().bindForResult(requestCode, resultCode, data);

``` 
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    PhotoUtils.getInstance().bindForResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
}
``` 

#### 3.相机调用
``` 
 PhotoUtils.getInstance().camera();
``` 
#### 4.打开相册
``` 
 PhotoUtils.getInstance().gallery();
``` 
#### 5.图片裁剪
``` 
  /**
     * @param activity    当前activity
     * @param orgUri      剪裁原图的Uri
     * @param desUri      剪裁后的图片的Uri
     * @param requestCode 剪裁图片的请求码
     */
 PhotoUtils.getInstance().cropImageUri(Activity activity, Uri orgUri, Uri desUri, int requestCode);
``` 
### 三、标题栏
#### 1.布局中引入控件
``` 
<com.alliky.core.widget.TitleBar
    xmlns:titlebar="http://schemas.android.com/apk/res-auto"
    android:id="@+id/titlebar"                     
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    titlebar:titleBarColor="color"             // 标题栏背景颜色
    titlebar:fillStatusBar="boolean"           // 填充状态栏，true时，标题栏会创建一块和系统状态栏同高的视图，用于沉浸式标题栏
    titlebar:statusBarColor="color"            // 使用沉浸式标题栏时，标题栏显示的颜色
    titlebar:statusBarMode="dark|light"        // 状态栏图标模式，默认是暗色图标
    titlebar:titleBarHeight="dimension"        // 标题栏高度
    titlebar:showBottomLine="boolean"          // 是否显示标题栏底部的分割线   
    titlebar:bottomLineColor="color"           // 标题栏分割线颜色
    titlebar:bottomShadowHeight="dimension"    // 底部阴影高度 showBottomLine = false时有效
    titlebar:leftType="none|textView|imageButton|customView"    // 左侧视图类型：无|文字|按钮|自定义视图
    titlebar:leftText="string"                 // 左侧文字leftType= textView有效
    titlebar:leftTextColor="color"             // 左侧文字颜色
    titlebar:leftTextSize="dimension"          // 左侧文字大小
    titlebar:leftDrawable ="reference"         // leftType= textView时，对应的drawableLeft图片
    titlebar:leftDrawablePadding="dimension"   // leftType= textView时，对应的drawablePadding
    titlebar:leftImageResource="reference"     // leftType= imageButton时，左侧按钮对应的图片资源引用
    titlebar:leftCustomView ="reference"       // leftType= customView时，左侧布局资源引用
    titlebar:rightType="none|textView|imageButton|customView"   // 右侧视图类型：无|文字|按钮|自定义视图
    titlebar:rightText="string"                // 右侧文字rightType= textView有效
    titlebar:rightTextColor="color"            // 右侧文字颜色
    titlebar:rightTextSize="dimension"         // 右侧文字大小
    titlebar:rightImageResource="reference"    // rightType= imageButton时，右侧按钮对应的图片资源引用
    titlebar:rightCustomView="reference"       // rightType= customView时，右侧布局资源引用
    titlebar:centerType="none|textView|searchView|customView"   // 中间视图类型：无|文字|搜索框|自定义视图
    titlebar:centerSearchRightType="voice|delete"               // 搜索框右侧按钮类型：语音按钮|删除按钮
    titlebar:centerText="string"               // 标题文字centerType = textView有效
    titlebar:centerTextColor="color"           // 标题文字颜色
    titlebar:centerTextSize="dimension"        // 标题文字大小
    titlebar:centerTextMarquee="boolean"       // 标题文字跑马灯效果，默认true
    titlebar:centerSubText="string"            // 副标题文字
    titlebar:centerSubTextColor="color"        // 副标题文字颜色
    titlebar:centerSubTextSize="dimension"     // 副标题文字大小
    titlebar:centerSearchEdiable="boolean"     // 搜索框是否可以输入，对应centerType =searchView
    titlebar:centerSearchBg="reference"        // 搜索框背景
    titlebar:centerCustomView="reference"/>    // 中间自定义视图
``` 
#### 2.监听标题栏操作
##### (1)点击事件
``` 
titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
    @Override
    public void onClicked(View v, int action, String extra) {
        if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
            ...
        }
        // CommonTitleBar.ACTION_LEFT_TEXT;        // 左边TextView被点击
        // CommonTitleBar.ACTION_LEFT_BUTTON;      // 左边ImageBtn被点击
        // CommonTitleBar.ACTION_RIGHT_TEXT;       // 右边TextView被点击
        // CommonTitleBar.ACTION_RIGHT_BUTTON;     // 右边ImageBtn被点击
        // CommonTitleBar.ACTION_SEARCH;           // 搜索框被点击,搜索框不可输入的状态下会被触发
        // CommonTitleBar.ACTION_SEARCH_SUBMIT;    // 搜索框输入状态下,键盘提交触发，此时，extra为输入内容
        // CommonTitleBar.ACTION_SEARCH_VOICE;     // 语音按钮被点击
        // CommonTitleBar.ACTION_SEARCH_DELETE;    // 搜索删除按钮被点击
        // CommonTitleBar.ACTION_CENTER_TEXT;      // 中间文字点击
    }
});
``` 
##### (2)双击事件
```
titleBar.setDoubleClickListener(new CommonTitleBar.OnTitleBarDoubleClickListener() {
    @Override
    public void onClicked(View v) {
        Toast.makeText(MainActivity.this, "Titlebar double clicked!", Toast.LENGTH_SHORT).show();
    }
});
```
##### (3)自定义布局事件(以右侧自定义布局为例)
```
View rightCustomLayout = titleBar.getRightCustomView();
rightCustomLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

    }
});
// 布局child view添加监听事件
rightCustomLayout.findViewById(R.id.子控件ID).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

    }
});
```
#### 3.动态切换状态栏图标颜色
```
titleBar.toggleStatusBarMode();
```  
#### 4.注意点
##### (1). 如果出现全屏与键盘的冲突导致的键盘被遮挡问题，请在Activity.onAttachedToWindow()方法中加入如下代码，或在布局根节点加入 fitSystemWindow=true
```
  @Override
  public void onAttachedToWindow() {
      super.onAttachedToWindow();
      KeyboardConflictCompat.assistWindow(getWindow());
  }
```  
##### (2). 若出现页面其他输入组件无法自动获取焦点的情况，请修改配置titlebar:centerTextMarquee="false"  

###  四、自定义PopupWindow 的使用
 ```  
 mPopupWindow = new CustomPopupWindow.Builder(this)
                     .setContentView(R.layout.pop_item_photo_layout)//设置布局
                     .setwidth(LinearLayout.LayoutParams.MATCH_PARENT)//设置宽
                     .setheight(LinearLayout.LayoutParams.WRAP_CONTENT)//设置高
                     .setFouse(true)//设置是否获取焦点
                     .setOutSideCancel(true)//设置点击外部是否消失
                     .builder();
 
 
     private void dismissPopWindow() {
         if (null != mPopupWindow) {
             mPopupWindow.dismiss();
         }
     }
 
     private void showPopWindow() {
         if (!mPopupWindow.isShowing()) {
             maskimgView.setVisibility(View.VISIBLE);
             mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
         } else {
             mPopupWindow.dismiss();
         }
     }
 
```

### 五、自定义弹窗CustomDialog

#### 1.全局配置
在完成引入库后，首先需要进行一些预先配置，诸如对话框组件整体的风格、主题和字体等，它们都可以在一个工具类中进行配置，说明如下：
```
import com.kongzue.dialog.util.DialogSettings;

DialogSettings.isUseBlur = (boolean);                   //是否开启模糊效果，默认关闭
DialogSettings.style = (DialogSettings.STYLE);          //全局主题风格，提供三种可选风格，STYLE_MATERIAL, STYLE_KONGZUE, STYLE_IOS
DialogSettings.theme = (DialogSettings.THEME);          //全局对话框明暗风格，提供两种可选主题，LIGHT, DARK
DialogSettings.tipTheme = (DialogSettings.THEME);       //全局提示框明暗风格，提供两种可选主题，LIGHT, DARK
DialogSettings.titleTextInfo = (TextInfo);              //全局标题文字样式
DialogSettings.contentTextInfo = (TextInfo);            //全局正文文字样式
DialogSettings.buttonTextInfo = (TextInfo);             //全局默认按钮文字样式
DialogSettings.buttonPositiveTextInfo = (TextInfo);     //全局焦点按钮文字样式（一般指确定按钮）
DialogSettings.inputInfo = (InputInfo);                 //全局输入框文本样式
DialogSettings.backgroundColor = (ColorInt);            //全局对话框背景颜色，值0时不生效
DialogSettings.cancelable = (boolean);                  //全局对话框默认是否可以点击外围遮罩区域或返回键关闭，此开关不影响提示框（TipDialog）以及等待框（TipDialog）
DialogSettings.cancelableTipDialog = (boolean);         //全局提示框及等待框（WaitDialog、TipDialog）默认是否可以关闭
DialogSettings.DEBUGMODE = (boolean);                   //是否允许打印日志
DialogSettings.blurAlpha = (int);                       //开启模糊后的透明度（0~255）
DialogSettings.systemDialogStyle = (styleResId);        //自定义系统对话框style，注意设置此功能会导致原对话框风格和动画失效
DialogSettings.dialogLifeCycleListener = (DialogLifeCycleListener);  //全局Dialog生命周期监听器
DialogSettings.defaultCancelButtonText = (String);      //设置 BottomDialog 和 ShareDialog 默认“取消”按钮的文字
DialogSettings.tipBackgroundResId = (drawableResId);    //设置 TipDialog 和 WaitDialog 的背景资源
DialogSettings.tipTextInfo = (InputInfo);               //设置 TipDialog 和 WaitDialog 文字样式

//检查 Renderscript 兼容性，若设备不支持，DialogSettings.isUseBlur 会自动关闭；
boolean renderscriptSupport = DialogSettings.checkRenderscriptSupport(context)

DialogSettings.init(context);                           //初始化清空 BaseDialog 队列
```

如果需要开启模糊效果，即 DialogSettings.isUseBlur = true; 需要进行额外 renderscript 配置，需要注意的是在部分低配置手机上此功能效率可能存在问题。

在 app 的 build.gradle 中添加以下代码：
```
android {
    ...
    defaultConfig {
        ...

        renderscriptTargetApi 17
        renderscriptSupportModeEnabled true
    }
}
```
#### 2.Dialog使用

##### (1).基本消息对话框

提供日常消息展示，区分为单按钮、双按钮和三按钮的效果。


以下范例通过参数快速创建一个基本的消息对话框：
```
MessageDialog.show(MainActivity.this, "提示", "这是一条消息", "确定");
```

额外的，MessageDialog 还提供多种参数的构建方法，方便快速创建合适的对话框：
```
MessageDialog.show(MainActivity.this, "提示", "这是一条双按钮消息", "确定", "取消");

MessageDialog.show(MainActivity.this, "提示", "这是一条三按钮消息", "确定", "取消", "其他");
```

消息对话框的按钮回调方法提供了一个 return 值用于判断点击按钮后是否需要关闭对话框，默认 return false 会关闭当前的输入对话框，若 return true 则点击该按钮后不会关闭：
```
MessageDialog.show(MainActivity.this, "更多功能", "点击左边的按钮是无法关掉此对话框的，Kongzue Dialog提供的回调函数可以轻松帮你实现你想要的判断功能", "点我关闭", "我是关不掉的")
        .setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(BaseDialog baseDialog, View v) {
                return true;                    //位于“取消”位置的按钮点击后无法关闭对话框
            }
        });
```

此功能便于做选择判断，在合理的事件触发后可允许关闭对话框。

也可以通过 build(...) 方法创建，并定制更多效果：
```
MessageDialog.build(MainActivity.this)
        .setStyle(DialogSettings.STYLE.STYLE_MATERIAL)
        .setTheme(DialogSettings.THEME.DARK)
        .setTitle("定制化对话框")
        .setMessage("我是内容")
        .setOkButton("OK", new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了OK！", Toast.LENGTH_SHORT).show();
                return false;
            }
        })
        .show();
```

需注意的是，只有修改主题风格的 setStyle(...) 和 setTheme(...) 方法必须在使用 build(...) 创建时才可以修改。

一些特殊需求中可能用到需要纵向排列按钮的三按钮消息框，则可以通过以下方法实现：
```
MessageDialog
        .show(MainActivity.this, "纵向排列", "如果你正在使用iOS风格或Kongzue风格，这里的按钮可以纵向排列，以方便提供更多选择", "还不错", "有点意思", "还有呢？")
        .setButtonOrientation(LinearLayout.VERTICAL);
```

💡 额外说明，除了默认的 setOnOkButtonClickListener(...) 方法以外，你还可以这样写：
```
//仅设置文字
.setOkButton("知道了")
        
//设置文字同时设置回调
.setOkButton("知道了", new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了知道了！", Toast.LENGTH_SHORT).show();
                return false;
            }
        })
        
//仅设置回调
.setOkButton(new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了知道了！", Toast.LENGTH_SHORT).show();
                return false;
            }
        })
        
//使用资源 id 设置文字
.setOkButton(R.string.iknow)

//其他你能想到的同样支持...
```

##### (2).输入对话框

提供额外输入需求的对话框组件，可控制输入内容类型，并在点击按钮时判断是否关闭对话框。

以下范例通过参数快速创建一个基本的输入对话框：
```
InputDialog.show(MainActivity.this, "输入对话框", "输入一些内容", "确定");
```

InputDialog 与 MessageDialog 类似也提供多种构建方法，在此不再赘述。

输入对话框的按钮回调中会直接返回当前输入的文本内容：
```
InputDialog.show(MainActivity.this, "提示", "请输入密码", "确定", "取消")
        .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
            @Override
            public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                //inputStr 即当前输入的文本
                return false;
            }
        })
```

如果需要修改输入框的提示语（HintText）或内容（InputText），可以使用以下方法：
```
InputDialog.show(MainActivity.this, "输入对话框", "输入一些内容", "确定")
        .setInputText("123456")
        .setHintText("请输入密码")
;
```

如需控制输入内容的字号、颜色、输入长度、文本类型，可以通过以下方法实现：
```
InputDialog.show(MainActivity.this, "输入对话框", "请输入6位密码", "确定")
        .setInputInfo(new InputInfo()
                              .setMAX_LENGTH(6)     //限制最大输入长度
                              .setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)     //仅输入密码类型
                              .setTextInfo(new TextInfo()       //设置文字样式
                                                   .setFontColor(Color.RED)     //修改文字样式颜色为红色
                              )
                              .setMultipleLines(true)       //支持多行输入
        )
;
```

备注：TextInfo（com.kongzue.dialog.util.TextInfo）类提供了基本的文字样式控制，InputInfo（com.kongzue.dialog.util.InputInfo）类提供了基础的输入文字类型控制。

##### (3).等待和提示对话框
等待提示对话框提供居中于屏幕阻断操作的等待和状态提示功能。

使用以下代码构建等待对话框：
```
WaitDialog.show(MainActivity.this, "请稍候...");
```

使用以下代码构建提示对话框：
```
TipDialog.show(MainActivity.this, "警告提示", TipDialog.TYPE.WARNING);
```

TipDialog 自带三种类型的提示图标（TipDialog.TYPE），可通过参数设置指定：
```
TipDialog.TYPE.SUCCESS                                  //对勾提示图
TipDialog.TYPE.WARNING                                  //感叹号提示图
TipDialog.TYPE.ERROR                                    //错误叉提示图
```

也可以通过如下代码设置自定义的图片：
```
TipDialog.show(MainActivity.this, "警告提示", R.mipmap.img_tip);        //入参自定义图片资源文件
```

额外的，可以通过以下语句设置 TipDialog 自动关闭的时长（单位：毫秒）：
```
TipDialog.show(MainActivity.this, "成功！", TipDialog.TYPE.SUCCESS)
        .setTipTime(5000);
```

如果连续使用，两者会有衔接的效果。

如需手动关闭，执行对应的 dismiss() 方法即可。

需要注意的是，WaitDialog 本质上继承自 TipDialog，它们属于长时间提示功能的组件，且内存中只会创建一次，不会重复创建直到被 dismiss() 关闭，但因为该组件使用 DialogFragment 构建，请勿需担心造成 WindowLeaked 错误问题。

WaitDialog 和 TipDialog 的主题可以通过以下代码自定义：
```
WaitDialog.show(MainActivity.this, null)
        .setTheme(DialogSettings.THEME.LIGHT);      //强制指定为亮色模式
```

##### (4).底部菜单
即从屏幕底部弹出的可选择的菜单。

使用以下代码构建底部菜单：
```
BottomMenu.show(MainActivity.this, new String[]{"菜单1", "菜单2", "菜单3"}, new OnMenuItemClickListener() {
    @Override
    public void onClick(String text, int index) {
        //返回参数 text 即菜单名称，index 即菜单索引
    }
});
```

BottomMenu 可以通过 String[] 集合创建，也可以通过 List<String> 创建。

要为底部菜单加上标题，可以使用以下语句：
```
BottomMenu.show(MainActivity.this, new String[]{"菜单1", "菜单2", "菜单3"}, new OnMenuItemClickListener() {
    @Override
    public void onClick(String text, int index) {
        log("点击了：" + text);
    }
})
.setTitle("这里是标题文字");
```

底部菜单也允许自定义菜单 Adapter 以支持您自己的菜单样式，您可以设置自定义的继承自 BaseAdapter 的菜单 Adapter 给菜单 ListView，此时您无需设置 StringArray 或 List<String> 的集合。

另外请注意，使用自定义菜单 Adapter 的情况下可能导致使用 iOS 风格时无法有效的支持顶部菜单、底部菜单的上下圆角裁切，您可以尝试参考 BottomMenu.IOSMenuArrayAdapter 来编写您的自定义 Adapter。

使用自定义菜单 Adapter 可以使用以下语句：
```
List<String> datas = new ArrayList<>();
datas.add("菜单1");
datas.add("菜单2");
datas.add("菜单3");

//您自己的Adapter
BaseAdapter baseAdapter = new ArrayAdapter(MainActivity.this, com.kongzue.dialog.R.layout.item_bottom_menu_kongzue, datas);    

BottomMenu.show(MainActivity.this, baseAdapter, new OnMenuItemClickListener() {
    @Override
    public void onClick(String text, int index) {
        //注意此处的 text 返回为自定义 Adapter.getItem(position).toString()，如需获取自定义Object，请尝试 datas.get(index)
        toast(text);
    }
});
```

⚠ 特别说明：

Material 风格的 BottomDialog 默认不支持“取消”按钮，按照设计规范，使用下滑手势关闭。

##### (5).通知
这里的通知并非系统通知，且不具备在您的设备通知栏中持久显示的特性，它本质上是通过对 Toast 进行修改实现的跨界面屏幕顶部提示条。

不依赖于界面显示，也不会打断用户操作，可作为即时通迅 IM 类软件跨界面消息提醒，或者用于网络错误状态提示。


使用以下代码快速构建通知：
```
Notification.show(MainActivity.this, "提示", "提示信息");
```

需要加入图标与点击、关闭事件：
```
Notification.show(MainActivity.this, "提示", "提示信息", R.mipmap.ico_wechat).setOnNotificationClickListener(new OnNotificationClickListener() {
    @Override
    public void onClick() {
        MessageDialog.show(MainActivity.this, "提示", "点击了消息");
    }
}).setOnDismissListener(new OnDismissListener() {
    @Override
    public void onDismiss() {
        log("消息溜走了");
    }
});
```

##### (6).分享对话框
分享对话框会从屏幕底部弹出，并提供图标加文字的选择分享列表。

要使用分享对话框，需要先创建分享 Item：
```
List<ShareDialog.Item> itemList = new ArrayList<>();
itemList.add(new ShareDialog.Item(MainActivity.this ,R.mipmap.img_email_ios,"邮件"));
itemList.add(new ShareDialog.Item(MainActivity.this ,R.mipmap.img_qq_ios,"QQ"));
itemList.add(new ShareDialog.Item(MainActivity.this ,R.mipmap.img_wechat_ios,"微信"));
itemList.add(new ShareDialog.Item(MainActivity.this ,R.mipmap.img_weibo_ios,"微博"));
itemList.add(new ShareDialog.Item(MainActivity.this ,R.mipmap.img_memorandum_ios,"添加到“备忘录”"));
itemList.add(new ShareDialog.Item(MainActivity.this ,R.mipmap.img_remind_ios,"提醒事项"));
```

然后创建分享对话框及监听点击事件：
```
ShareDialog.show(MainActivity.this, itemList, new ShareDialog.OnItemClickListener() {
    @Override
    public boolean onClick(ShareDialog shareDialog, int index, ShareDialog.Item item) {
        toast("点击了：" + item.getText());
        return false;
    }
});
```

额外需要注意，iOS 风格模式下，默认会自动对图片进行圆角裁切，使用时只需要直接提供方形图标即可。

⚠ 特别说明：

Material 风格的 ShareDialog 默认不支持“取消”按钮，按照设计规范，使用下滑手势关闭。

#### 3.定制化

##### (1).自定义布局
对于任意一个对话框组件，Kongzue Dialog V3 提供了自定义布局功能，您可以使用一下代码来插入自定义布局：
```
//对于未实例化的布局：
MessageDialog.show(MainActivity.this, "提示", "这个窗口附带自定义布局", "知道了")
        .setCustomView(R.layout.layout_custom, new MessageDialog.OnBindView() {
            @Override
            public void onBind(MessageDialog dialog, View v) {
                //绑定布局事件，可使用v.findViewById(...)来获取子组件
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toast("点击了自定义布局");
                    }
                });
            }
        });
        
//对于已实例化的布局：
View customView;
MessageDialog.show(MainActivity.this, "提示", "这个窗口附带自定义布局", "知道了")
        .setCustomView(customView);
```

目前支持自定义子布局的有：消息对话框组件（MessageDialog）、底部菜单组件（BottomDialog）、输入框组件（InputDialog）、分享对话框（ShareDialog）和通知组件（Notification）

##### (2).自定义对话框
提供了完全自定义对话框方便快速实现特殊效果的对话框样式。


使用以下代码创建自定义对话框：
```
//对于未实例化的布局：
CustomDialog.show(MainActivity.this, R.layout.layout_custom_dialog, new CustomDialog.OnBindView() {
    @Override
    public void onBind(final CustomDialog dialog, View v) {
        ImageView btnOk = v.findViewById(R.id.btn_ok);
        
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.doDismiss();
            }
        });
    }
});

//对于已实例化的布局：
View customView;
CustomDialog.show(MainActivity.this, customView, new CustomDialog.OnBindView() {
    @Override
    public void onBind(final CustomDialog dialog, View v) {
        ImageView btnOk = v.findViewById(R.id.btn_ok);
        
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.doDismiss();
            }
        });
    }
});
```

其他方法
```
//全屏幕宽高
customDialog.setFullScreen(true);

//设置 CustomDialog 处于屏幕的位置
CustomDialog.setAlign(CustomDialog.ALIGN.BOTTOM)        //从屏幕底端出现
CustomDialog.setAlign(CustomDialog.ALIGN.TOP)           //从屏幕顶端出现
CustomDialog.setAlign(CustomDialog.ALIGN.DEFAULT)       //从屏幕中部出现
```

#### 4.监听事件
如果需要全局的控制所有对话框显示、隐藏触发事件，可以设置 <a href="#全局配置">全局设置</a> 中的 dialogLifeCycleListener 监听器，其中会返回所有对话框的生命周期管理，以便做相应处理：
```
DialogSettings.dialogLifeCycleListener = new DialogLifeCycleListener() {
    @Override
    public void onCreate(BaseDialog dialog) {
    
    }
    @Override
    public void onShow(BaseDialog dialog) {
    
    }
    @Override
    public void onDismiss(BaseDialog dialog) {
    
    }
}
```

要单独对某个对话框进行监听，可使用对应的 setOnShowListener(...) 及 setOnDismissListener(...) 进行处理，例如，在提示过后关闭本界面可以这样写：
```
TipDialog.show(MainActivity.this, "成功！", TipDialog.TYPE.SUCCESS).setOnDismissListener(new OnDismissListener() {
    @Override
    public void onDismiss() {
        finish();
    }
});
```

#### 5.自定义背景
目前 MessageDialog、InpurDialog、TipDialog、WaitDialog 支持使用以下方法自定义背景资源：
```
dialog.setBackgroundResId(int resId);
```

### TDialog
主要解决PopupWindow上input输入框复制粘贴问题，很大程度上也封装完善了个各种弹窗功能、自定义布局等。

#### TDialog在Activity或者Fragment中使用
```

        new TDialog.Builder(getSupportFragmentManager())
                .setLayoutRes(R.layout.dialog_click)    //设置弹窗展示的xml布局
//                .setDialogView(view)  //设置弹窗布局,直接传入View
                .setWidth(600)  //设置弹窗宽度(px)
                .setHeight(800)  //设置弹窗高度(px)
                .setScreenWidthAspect(this, 0.8f)   //设置弹窗宽度(参数aspect为屏幕宽度比例 0 - 1f)
                .setScreenHeightAspect(this, 0.3f)  //设置弹窗高度(参数aspect为屏幕宽度比例 0 - 1f)
                .setGravity(Gravity.CENTER)     //设置弹窗展示位置
                .setTag("DialogTest")   //设置Tag
                .setDimAmount(0.6f)     //设置弹窗背景透明度(0-1f)
                .setCancelableOutside(true)     //弹窗在界面外是否可以点击取消
                .setDialogAnimationRes(R.style.animate_dialog)  //设置弹窗动画
                .setOnDismissListener(new DialogInterface.OnDismissListener() { //弹窗隐藏时回调方法
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Toast.makeText(DiffentDialogActivity.this, "弹窗消失回调", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnBindViewListener(new OnBindViewListener() {   //通过BindViewHolder拿到控件对象,进行修改
                    @Override
                    public void bindView(BindViewHolder bindViewHolder) {
                        bindViewHolder.setText(R.id.tv_content, "abcdef");
                        bindViewHolder.setText(R.id.tv_title, "我是Title");
                    }
                })
                .addOnClickListener(R.id.btn_left, R.id.btn_right, R.id.tv_title)   //添加进行点击控件的id
                .setOnViewClickListener(new OnViewClickListener() {     //View控件点击事件回调
                    @Override
                    public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
                        switch (view.getId()) {
                            case R.id.btn_left:
                                Toast.makeText(DiffentDialogActivity.this, "left clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.btn_right:
                                Toast.makeText(DiffentDialogActivity.this, "right clicked", Toast.LENGTH_SHORT).show();
                                tDialog.dismiss();
                                break;
                            case R.id.tv_title:
                                Toast.makeText(DiffentDialogActivity.this, "title clicked", Toast.LENGTH_SHORT).show();
                                viewHolder.setText(R.id.tv_title, "Title点击了");
                                break;
                        }
                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return false;
                    }
                })
                .create()   //创建TDialog
                .show();    //展示

```
#### 添加动画姿势
```
新建补间动画文件
enter.xml
<?xml version="1.0" encoding="utf-8"?>
<translate xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="300"
    android:fromYDelta="100%p"
    android:toYDelta="0%p">

</translate>
exit.xml
<?xml version="1.0" encoding="utf-8"?>
<translate xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="300"
    android:fromYDelta="0%p"
    android:toYDelta="100%p">

</translate>
style.xml文件
<style name="animate_dialog">
    <item name="android:windowEnterAnimation">@anim/enter</item>
    <item name="android:windowExitAnimation">@anim/exit</item>
</style>
```
#### 使用方法解析
TDialog的实现原理和系统Dialog原理差不多,主要使用Builder设计模式实现
1.创建弹窗,传入xml布局文件或者传入View控件,且自己设置背景色,因为默认是透明背景色
```
new TDialog.Builder(getSupportFragmentManager())
        .setLayoutRes(R.layout.dialog_click)
        .setDialogView(view)
        .create()
        .show();
```
2.设置弹窗的宽高(如果不设置宽或者高,默认使用包裹内容的高度)
```
   new TDialog.Builder(getSupportFragmentManager())
            .setLayoutRes(R.layout.dialog_click)
            .setWidth(600)  //设置弹窗固定宽度(单位:px)
            .setHeight(800)//设置弹窗固定高度
            .setScreenWidthAspect(Activity.this,0.5f) //动态设置弹窗宽度为屏幕宽度百分比(取值0-1f)
            .setScreenHeightAspect(Activity.this,0.6f)//设置弹窗高度为屏幕高度百分比(取值0-1f)
            .create()
            .show();
```
3.设置弹窗展示的位置
```
.setGravity(Gravity.CENTER)
其他位置有:Gravity.Bottom / Gravity.LEFT等等和设置控件位置一样
```
4.设置弹窗背景色透明度(取值0-1f,0为全透明)
```
.setDimAmount(0.6f)
```
5.设置弹窗外部是否可以点击取消(默认可点击取消),和设置弹窗是否可以取消(默认可取消),弹窗隐藏时回调方法
```
.setCancelableOutside(true)
.setOnDismissListener(new DialogInterface.OnDismissListener() { //弹窗隐藏时回调方法
    @Override
    public void onDismiss(DialogInterface dialog) {
        Toast.makeText(DiffentDialogActivity.this, "弹窗隐藏回调", Toast.LENGTH_SHORT).show();
    }
})
```
6.当弹窗需要动态改变控件子view内容时,这里借鉴了RecyclerView.Adapter的设计思想,内部封装好一个BindViewHolder
```
.setOnBindViewListener(new OnBindViewListener() {
    @Override
    public void bindView(BindViewHolder bindViewHolder) {
        bindViewHolder.setText(R.id.tv_content, "abcdef");
    bindViewHolder.setText(R.id.tv_title,"我是Title");        
    }
})
```
7.监听弹窗子控件的点击事件,内部也是通过BindViewHolder实现
addOnClickListener(ids[])只需要将点击事件控件的id传入,并设置回调接口setOnViewClickListener()
```
.addOnClickListener(R.id.btn_right, R.id.tv_title)
.setOnViewClickListener(new OnViewClickListener() {
    @Override
    public void onViewClick(BindViewHolder viewHolder,View view1, TDialog tDialog) {
        switch (view1.getId()) {
            case R.id.btn_right:
                Toast.makeText(DialogEncapActivity.this, "btn_right", Toast.LENGTH_SHORT).show();
                tDialog.dismiss();
                break;
            case R.id.tv_title:
                Toast.makeText(DialogEncapActivity.this, "tv_title", Toast.LENGTH_SHORT).show();
                break;
        }
    }
})
```
8.设置弹窗动画
```
.setDialogAnimationRes(R.style.animate_dialog) 
```
9.监听返回键点击事件,需配合setCancelableOutside(false)方法一起使用
```
.setOnKeyListener(new DialogInterface.OnKeyListener() {
    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
           if (keyCode == KeyEvent.KEYCODE_BACK) {
                Toast.makeText(DiffentDialogActivity.this, "返回健无效，请强制升级后使用", Toast.LENGTH_SHORT).show();
                 return true;
           }
           return false;  //默认返回值
     }
})
```
a.列表弹窗-使用TListDialog,TListDialog继承自TDialog,可以使用父类所有的方法,并且扩展列表数据展示丰富setAdapter()和item点击事件回调方法setOnAdapterItemClickListener()
```
new TListDialog.Builder(getSupportFragmentManager())
        .setHeight(600)
        .setScreenWidthAspect(this, 0.8f)
        .setGravity(Gravity.CENTER)
        .setAdapter(new TBaseAdapter<String>(R.layout.item_simple_text, Arrays.asList(data)) {

            @Override
            protected void onBind(BindViewHolder holder, int position, String s) {
                holder.setText(R.id.tv, s);
            }
        })
        .setOnAdapterItemClickListener(new TBaseAdapter.OnAdapterItemClickListener<String>() {
            @Override
            public void onItemClick(BindViewHolder holder, int position, String s, TDialog tDialog) {
                Toast.makeText(DiffentDialogActivity.this, "click:" + s, Toast.LENGTH_SHORT).show();
                tDialog.dismiss();
            }
        })
        .create()
        .show();
```
#### 列表弹窗
为了方便使用:
1. 不用传入layoutRes布局文件,TDialog内部设置了一个默认的RecyclerView布局,且RecyclerView的控件id为recycler_view,背景为#ffffff
2. setAdapter(Adapter),设置recyclerview的adapter,为了封装Adapter的item点击事件,传入的adapter需要为TBaseAdapter的实现类
3. setOnAdapterItemClickListener(),设置adapter的点击事件
```
<?xml version="1.0" encoding="utf-8"?>
<android.support.v7.widget.RecyclerView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/recycler_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="#ffffff"
    android:orientation="vertical" />
```
TBaseAdapter实现:需要使用者传入item的xml布局,和List数据
```
public abstract class TBaseAdapter<T> extends RecyclerView.Adapter<BindViewHolder> {

    private final int layoutRes;
    private List<T> datas;
    private OnAdapterItemClickListener adapterItemClickListener;
    private TDialog dialog;

    protected abstract void onBind(BindViewHolder holder, int position, T t);

    public TBaseAdapter(@LayoutRes int layoutRes, List<T> datas) {
        this.layoutRes = layoutRes;
        this.datas = datas;
    }

    @Override
    public BindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BindViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false));
    }

    @Override
    public void onBindViewHolder(final BindViewHolder holder, final int position) {
        onBind(holder, position, datas.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterItemClickListener.onItemClick(holder, position, datas.get(position), dialog);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setTDialog(TDialog tDialog) {
        this.dialog = tDialog;
    }

    public interface OnAdapterItemClickListener<T> {
        void onItemClick(BindViewHolder holder, int position, T t, TDialog tDialog);
    }

    public void setOnAdapterItemClickListener(OnAdapterItemClickListener listener) {
        this.adapterItemClickListener = listener;
    }
}
```
#### 如果使用者需要使用自己的列表布局时,可以使用setListLayoutRes(layotuRes,LayoutManager)方法设置xml布局和布局管理器LayoutManager,切记xml布局中的RecyclerView的id必须设置为recycler_view(如效果图中的分享弹窗)
```
//底部分享
public void shareDialog(View view) {
    new TListDialog.Builder(getSupportFragmentManager())
            .setListLayoutRes(R.layout.dialog_share_recycler, LinearLayoutManager.HORIZONTAL)
            .setScreenWidthAspect(this, 1.0f)
            .setGravity(Gravity.BOTTOM)
            .setAdapter(new TBaseAdapter<String>(R.layout.item_share, Arrays.asList(sharePlatform)) {
                @Override
                protected void onBind(BindViewHolder holder, int position, String s) {
                    holder.setText(R.id.tv, s);
                }
            })
            .setOnAdapterItemClickListener(new TBaseAdapter.OnAdapterItemClickListener<String>() {
                @Override
                public void onItemClick(BindViewHolder holder, int position, String item, TDialog tDialog) {
                    Toast.makeText(DiffentDialogActivity.this, item, Toast.LENGTH_SHORT).show();
                    tDialog.dismiss();
                }
            })
            .create()
            .show();
}
```
自定义列表布局
```
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#242424">

    <TextView
        android:id="@+id/textView3"
        android:layout_width="0dp"
        android:layout_height="50dp"
        android:gravity="center"
        android:text="分享到"
        android:textColor="@color/white"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <android.support.v7.widget.RecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView3" />

</android.support.constraint.ConstraintLayout>
```