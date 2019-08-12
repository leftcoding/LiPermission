### Permission

##### 项目文件 build.gradle 文件中，添加

```java
implementation 'android.leftcoding:permission:1.0.2'
```

##### 申请系统权限，需要事先在`AndroidManifest.xml` 中，进行权限申请

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="android.permission">
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
</manifest>
```

##### 开始使用

```java
 LiPermission.with(context)
                .runtime()
                .checkPermission(Permissions.WRITE_EXTERNAL_STORAGE)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
                        // 授权合理说明
                        // to do some things
                    }
                })
                .setCallback(new RequestCallback() {
                    @Override
                    public void onGranted(List<String> list) {
                       // 授权成功 ...
                        // to do some things
                    }

                    @Override
                    public void onDenied(List<String> list) {
                      // 授权永久被禁止
                        if (LiPermission.hasAlwaysDeniedPermission(context, list)) {
                            // 打开权限设置界面
                            LiPermission.with(SplashActivity.this).launcher().start(100);
                            return;
                        }
                      // 授权失败 ...
                      // to do some things
                    }
                })
                .start();
```

