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

一.在Application中初始化配置 

``` 
public class MyApplication extends BaseApplication {
        
            @Override
            public void onCreate() {
                super.onCreate();
                //初始化常用配置
                Kylin.init(this)
                        .withLoaderDelayed(500)
                        .withApiHost("http://192.168.2.9:88/")
                        .withInterceptor(new DebugInterceptor("test", R.raw.test))
                        .configure();
            }
        }
``` 
