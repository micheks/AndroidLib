# AndroidLib
Android 一个基本类库，里面封装了一些常用工具，网络框架等。

### 如何使用？

#### 步骤一：在项目build.gradle加上
```
repositories {
    maven { url 'https://jitpack.io' }
}
```
#### 步骤二：在moduble的build.gradle加上

```
dependencies {
    implementation 'com.github.wxianing:AndroidLib:v1.1'
}
```

### 代码具体调用说明

#### 一、要使用该框架，则必须先在Application中初始化配置，否则使用到配置APP会闪退，配置代码如下： 

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

#### 二、网络请求使用

##### 1.post请求方式使用

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

##### 2.get请求方式使用

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

##### 3.文件上传

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

##### 4.下载文件

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

#### 三、打开相机或者从相册中选择照片

##### 1.在onCreate方法中初始化PhotoUtil

``` 
//拍照货相册选择回调
PhotoUtils.getInstance().init(this, false, new PhotoUtil.OnSelectListener() {
    @Override
    public void onFinish(File outputFile, Uri outputUri) {
        //TODO 返回图片文件
    }
});

``` 

##### 2.在onActivityResult 调用  PhotoUtils.getInstance().bindForResult(requestCode, resultCode, data);

``` 
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    PhotoUtils.getInstance().bindForResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
}
``` 

##### 3.相机调用
``` 
 PhotoUtils.getInstance().camera();
``` 
##### 4.打开相册
``` 
 PhotoUtils.getInstance().gallery();
``` 
##### 5.图片裁剪
``` 
  /**
     * @param activity    当前activity
     * @param orgUri      剪裁原图的Uri
     * @param desUri      剪裁后的图片的Uri
     * @param requestCode 剪裁图片的请求码
     */
 PhotoUtils.getInstance().cropImageUri(Activity activity, Uri orgUri, Uri desUri, int requestCode);
``` 
#### 标题栏
##### 1.布局中引入控件
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
##### 2.监听标题栏操作
###### (1)点击事件
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
###### (2)双击事件
```
titleBar.setDoubleClickListener(new CommonTitleBar.OnTitleBarDoubleClickListener() {
    @Override
    public void onClicked(View v) {
        Toast.makeText(MainActivity.this, "Titlebar double clicked!", Toast.LENGTH_SHORT).show();
    }
});
```
###### (3)自定义布局事件(以右侧自定义布局为例)
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
##### 3.动态切换状态栏图标颜色
```
titleBar.toggleStatusBarMode();
```  
##### 4.注意点
###### (1). 如果出现全屏与键盘的冲突导致的键盘被遮挡问题，请在Activity.onAttachedToWindow()方法中加入如下代码，或在布局根节点加入 fitSystemWindow=true
```
  @Override
  public void onAttachedToWindow() {
      super.onAttachedToWindow();
      KeyboardConflictCompat.assistWindow(getWindow());
  }
```  
###### (2). 若出现页面其他输入组件无法自动获取焦点的情况，请修改配置titlebar:centerTextMarquee="false"  
