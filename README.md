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


