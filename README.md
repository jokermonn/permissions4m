[![apk](https://img.shields.io/badge/apk-download-orange.svg)](https://github.com/jokermonn/permissions4m/blob/master/app-debug.apk?raw=true)
[![GitHub stars](https://img.shields.io/github/stars/jokermonn/permissions4m.svg?style=social)](https://img.shields.io/github/stars/jokermonn/permissions4m.svg?style=social)

[![platform](https://img.shields.io/badge/platform-android-brightgreen.svg)](https://developer.android.com/index.html)
[![license](https://img.shields.io/badge/license-Apach2.0-green.svg)](https://github.com/jokermonn/permissions4m/blob/master/LICENSE.txt)

[![lib](https://img.shields.io/badge/lib-2.1.2-blue.svg)](https://github.com/jokermonn/permissions4m/releases/tag/2.1.2-lib)
[![processor](https://img.shields.io/badge/processor-2.1.2-blue.svg)](https://github.com/jokermonn/permissions4m/releases/tag/2.1.2-processor)
[![annotation](https://img.shields.io/badge/annotation-1.0.3-blue.svg)](https://jcenter.bintray.com/com/jokermonn/permissions4m-annotation/1.0.3/)

# 中文|[ENGLISH](https://github.com/jokermonn/permissions4m/blob/master/README_EN.md) #

# 前言 #

- **还在为 `ActivityCompat.shouldShowRequestPermissionRationale(Activity, String)` 无法弹出权限申请对话框困惑么？permissions4m 让它必须弹出来**
- **还在为明明授权失败，却回调的是权限申请成功方法而苦恼么？permissions4m 让它必须回调期望的方法**
- **还在为用户拒绝且不再提示权限申请对话框而烦躁么？permissions4m 让它跳转到手机管家权限设置界面**
- **还在为5.0权限申请而烦恼么？permissions4m 帮你解决它**

# permissions4m #
意为 Permissions for M，基于 hongyangAndroid 的 [MPermissions](https://github.com/hongyangAndroid/MPermissions) 项目二次开发，使用编译时注解，较运行时注解效率更高。起初目的是仅作为纯粹的 [Andriod 编译时注解项目](http://blog.csdn.net/ziwang_/article/details/76576495)，较原有项目有以下升级：

- 支持 java8
- **支持一行代码同步请求多个权限**
- 支持多种回调函数，代码可以更简洁

后 permissions4m 为适配国产机型而迭代，目前：

- **支持国产机型适配**
- **支持国产机型5.0权限申请**

权限申请官方文档：[在运行时请求权限](https://developer.android.google.cn/training/permissions/requesting.html)

<h2>permissions4m 与 AndPermission</h2>

<h4>AndPermission 申请框架</h4>

**AndPermission 申请地理位置（小米）/AndPermission 申请联系人（小米）/AndPermission 申请手机状态（小米）/AndPermission 申请短信、日历（OPPO A57）：**

[`图片1`](http://img.blog.csdn.net/20170826090347772?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveml3YW5nXw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)、
[`图片2`](http://img.blog.csdn.net/20170826090413608?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveml3YW5nXw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)、
[`图片3`](http://img.blog.csdn.net/20170826090441440?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveml3YW5nXw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)、
[`图片4`](http://img.blog.csdn.net/20170820222553015?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveml3YW5nXw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

<h4>permissions4m</h4>

**permissions4m 申请地理位置（小米）/permissions4m 申请联系人（小米）/permissions4m 申请手机状态（小米）/permissions4m 申请短信、日历（OPPO A57）：**

[`图片1`](http://img.blog.csdn.net/20170826093257873?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveml3YW5nXw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)、
[`图片2`](http://img.blog.csdn.net/20170826093413798?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveml3YW5nXw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)、
[`图片3`](http://img.blog.csdn.net/20170826133058525?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveml3YW5nXw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)、
[`图片4`](http://img.blog.csdn.net/20170820222606746?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveml3YW5nXw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

# 引入依赖 #

## Gradle 依赖 ##

`project` 中的 `build.gradle`：

	buildscript {
      // ...
	}

	allprojects {
      repositories {
        // 请添加如下一行
        maven { url 'https://jitpack.io' }
      }
	}

`app` 中的 `build.gradle`：

	dependencies {
      compile 'com.github.jokermonn:permissions4m:2.1.2-lib'
      annotationProcessor 'com.github.jokermonn:permissions4m:2.1.2-processor'
	}

# 使用文档 #

* 注意事项
	* [**必加的二次权限申请回调**](#must_add)
* [接口回调](#annotation)
	* [单个权限申请](#single)
	* [多个权限同步申请](#mutli)
* [Listener 回调](#listener)
	* [单个权限申请](#sinlge_listener)
	* [多个权限同步申请](#mutli_listener)
* 关于项目
	* [help me](#help)
	* [国产畸形权限适配扩展](#extend)
	* [解决方案](#solu)
	* [项目答疑](#problem)

# 版本迭代 #
<h3 id="todo">TODO</h3>

- TODO:
	- [help me](#help)
	- 增强魅族权限申请
	- 增强各5.0+权限申请
	- **由于作者最近忙于秋招，更新若不及时，望见谅，希望各位开发者共同维护这个库，毕竟作者个人手机有限、精力有限。该库源码真的很简单！**

<h3 id="history">迭代历史</h3>

- v2.1.2
	- 增强地理位置权限申请
	- 添加 `code` 参数于 pageIntent 函数
	
- v2.1.1
	- 支持 oppo 5.0+ 权限申请
	- 修复魅族空指针异常
	
- v2.1.0
	- 支持 listener 自定义 rationale
	- 支持 listener 多权限同步申请
- v2.0.4
	- 支持手动关闭5.0权限申请
	- 修复代理类空指针异常
- v2.0.0
	- 修复 fragment/support fragment 中低于6.0版本注解回调无效
	- 修复 activity 强制申请回调失效
	- 修复 activity 自定义二次回调失效
	- 增强函数回调方式
	- 增强录音权限申请
	- **适配魅族5.0权限申请**
- v1.1.2
	- 增强小米中的 `READ_CONTACTS` 申请([国产畸形权限适配](#extends))
	- 增强小米中的 `PHONE_STATE` 申请
	- 增强小米中的 `READ_SMS` 申请
- v1.0.9
	- 修复内存泄露

- v1.0.6
	- 修复代理类空指针异常

- v1.0.5
	- 低版本默认授权成功
	- 支持兼容 sdk 到`11`

- v1.0.3
	- 修复 `WRITE_CONTACTS` 权限 bug
	- 截止于[第99次 commits](https://github.com/jokermonn/permissions4m/tree/476f9e547799b1e1e42455ce4a7810f1b1c9bee6)

- v1.0.2
	- 适配大量国产机型，包括小米、OPPO、华为等
	- 更改为流式 API
	- 支持 Listener 接口回调
	- 截止于[第81次 commits](https://github.com/jokermonn/permissions4m/tree/937c380f73ac67b028c2e98f296e9a79a982c8e4)

- v1.0.0

	- 截止于[第37次 commits](https://github.com/jokermonn/permissions4m/tree/bda771f9470df7b061c915e3daaea1e787381f71)

# 注意事项 #

<h3 id="must_add">必加的二次权限申请回调</h3>

在 Activity 或 Fragment 中，需要手动添加 `onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)` 方法以支持权限申请回调，代码如下即可：

	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

<h1 id="annotation">注解回调</h1>

<h2 id="single">单个权限申请</h2>

	Permissions4M.get(MainActivity.this)
		// 是否强制弹出权限申请对话框，建议设置为 true，默认为 true
                // .requestForce(true)
		// 是否支持 5.0 权限申请，默认为 false
		// .requestUnderM(false)
		// 权限，单权限申请仅只能填入一个
        .requestPermissions(Manifest.permission.RECORD_AUDIO)
		// 权限码
        .requestCodes(AUDIO_CODE)
		// 如果需要使用 @PermissionNonRationale 注解的话，建议添加如下一行
		// 返回的 intent 是跳转至**系统设置页面**
        // .requestPageType(Permissions4M.PageType.MANAGER_PAGE)
		// 返回的 intent 是跳转至**手机管家页面**
		// .requestPageType(Permissions4M.PageType.ANDROID_SETTING_PAGE)
        .request();

将会回调相应的 [`@PermissionsGranted`](#granted)、[`@PermissionsDenied`](#denied)、[`@PermissionsRationale`](#rationale)/[`PermissionsCustomRationale`](#custom_rationale)、[`@PermissionsNonRationale`](#non_rationale) 所修饰的方法

<h2 id="mutli">多个权限同步申请</h2>

- 使用 `@PermissionsRequestSync` 修饰 Activity 或 Fragment

- 传入两组参数
	- value 数组：请求码
	- permission 数组：请求权限
 
- 使用 `Permissions4M.get(MainActivity.this).requestSync();` 进行同步权限申请

例：参考 sample 中 [MainActivity](https://github.com/jokermonn/permissions4m/blob/master/app/src/main/java/com/joker/permissions4m/MainActivity.java) 上的设置 ——

	@PermissionsRequestSync(
		permission = {Manifest.permission.BODY_SENSORS, 
						Manifest.permission.ACCESS_FINE_LOCATION, 
							Manifest.permission.READ_CALENDAR},
        value = {SENSORS_CODE, 
					LOCATION_CODE, 
						CALENDAR_CODE})
	public class MainActivity extends AppCompatActivity

注：**同步申请默认强制申请(`requestForce(true)`)，同步申请不支持 `@PermissionsNonRationale`**

<h3 id="granted">@PermissionsGranted</h3>

授权**成功**时回调，注解中需要传入参数，分为两种情况：

- 单参数：`@PermissionsGranted(LOCATION_CODE)`，被修饰函数可不传入参数，例：
>

	@PermissionsGranted(LOCATION_CODE)
    public void granted() {
		ToastUtil.show("地理位置授权成功");
	}

>传参的话必须是 int 型
>

	@PermissionsGranted(LOCATION_CODE)
    public void granted(int code) {
		ToastUtil.show("地理位置授权成功");
	}

- 多参数：`@PermissionsGranted({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})`，被修饰函数需要传入一个 int 参数，例：
>

	@PermissionsGranted({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void granted(int code) {
        switch (code) {
            case LOCATION_CODE:
                ToastUtil.show("地理位置权限授权成功");
                break;
            case SENSORS_CODE:
                ToastUtil.show("传感器权限授权成功");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("读取日历权限授权成功");
                break;
            default:
                break;
        }
    }

<h3 id="denied">@PermissionsDenied</h3>

授权**失败**时回调，注解中需要传入参数，分为两种情况：

- 单参数：`@PermissionsDenied(LOCATION_CODE)`，被修饰函数可不传入参数，例：
>

	@PermissionsDenied(LOCATION_CODE)
    public void denied() {
		ToastUtil.show("地理位置授权失败");
	}

>传参的话必须是 int 型
>

	@PermissionsDenied(LOCATION_CODE)
    public void denied(int code) {
		ToastUtil.show("地理位置授权失败");
	}

- 多参数：`@PermissionsDenied({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})`，被修饰函数需要传入一个 int 参数，例：
>

	@PermissionsDenied({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void denied(int code) {
        switch (code) {
            case LOCATION_CODE:
                ToastUtil.show("地理位置权限授权失败");
                break;
            case SENSORS_CODE:
                ToastUtil.show("传感器权限授权失败");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("读取日历权限授权失败");
                break;
            default:
                break;
        }
    }

<h3 id="rationale">@PermissionsRationale</h3>
二次授权时回调，用于解释为何需要此权限，注解中需要传入参数，分为两种情况：

- 单参数：`@PermissionsRationale(LOCATION_CODE)`，被修饰函数可不传入参数，例：
>

	@PermissionsRationale(LOCATION_CODE)
    public void rationale() {
		ToastUtil.show("请开启读取地理位置权限");
	}

>传参的话必须是 int 型
>

	@PermissionsRationale(LOCATION_CODE)
    public void denied(int code) {
		ToastUtil.show("请开启读取地理位置权限");
	}

- 多参数：`@PermissionsRationale({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})`，被修饰函数需要传入一个 int 参数，例：
>

	@PermissionsRationale({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void rationale(int code) {
        switch (code) {
            case LOCATION_CODE:
                ToastUtil.show("请开启地理位置权限授权");
                break;
            case SENSORS_CODE:
                ToastUtil.show("请开启传感器权限授权");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("请开启读取日历权限授权");
                break;
            default:
                break;
        }

**注：系统弹出权限申请 dialog 与 toast 提示是异步操作，所以如果希望自行弹出一个 dialog 后（或其他同步需求）再弹出系统对话框，那么请使用 [@PermissionsCustomRationale](#custom_rationale)**

<h3 id="custom_rationale">@PermissionsCustomRationale</h3>

二次授权时回调，用于解释为何需要此权限，注解中需要传入参数，分为两种情况：

- 单参数：`@PermissionsCustomRationale(LOCATION_CODE)`，被修饰函数可不传入参数，例：
>

	@PermissionsCustomRationale(LOCATION_CODE)
    public void cusRationale() {
		new AlertDialog.Builder(this)
                        .setMessage("读取地理位置权限申请：\n我们需要您开启读取地理位置权限(in activity with annotation)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Permissions4M.get(MainActivity.this)
										// 注意添加 .requestOnRationale()
                                        .requestOnRationale()
                                        .requestPermissions(Manifest.permission.READ_SMS)
                                        .requestCodes(SMS_CODE)
                                        .request();
                            }
                        })
                        .show();
	}

>传参的话必须是 int 型
>

	@PermissionsCustomRationale(LOCATION_CODE)
    public void cusRationale(int code) {
		new AlertDialog.Builder(this)
                        .setMessage("读取地理位置权限申请：\n我们需要您开启读取地理位置权限(in activity with annotation)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Permissions4M.get(MainActivity.this)
										// 注意添加 .requestOnRationale()
                                        .requestOnRationale()
                                        .requestPermissions(Manifest.permission.READ_SMS)
                                        .requestCodes(SMS_CODE)
                                        .request();
                            }
                        })
                        .show();
	}

- 多参数：`@PermissionsCustomRationale({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})`，被修饰函数需要传入一个 int 参数，例：
>

	@PermissionsCustomRationale({SMS_CODE, AUDIO_CODE})
    public void cusRationale(int code) {
        switch (code) {
            case SMS_CODE:
                new AlertDialog.Builder(this)
                        .setMessage("短信权限申请：\n我们需要您开启短信权限(in activity with annotation)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Permissions4M.get(MainActivity.this)
										// 注意添加 .requestOnRationale()
                                        .requestOnRationale()
                                        .requestPermissions(Manifest.permission.READ_SMS)
                                        .requestCodes(SMS_CODE)
                                        .request();
                            }
                        })
                        .show();
                break;
            case AUDIO_CODE:
                new AlertDialog.Builder(this)
                        .setMessage("录音权限申请：\n我们需要您开启录音权限(in activity with annotation)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Permissions4M.get(MainActivity.this)
										// 注意添加 .requestOnRationale()
                                        .requestOnRationale()
                                        .requestPermissions(Manifest.permission.RECORD_AUDIO)
                                        .requestCodes(AUDIO_CODE)
                                        .request();
                            }
                        })
                        .show();
                break;
            default:
                break;
        }

注：**除上述以外的 dialog，开发者可以自定义其他展示效果，调用权限申请时请使用如下代码，否则会陷入无限调用自定义 Rationale 循环中**： 

	Permissions4M.get(MainActivity.this)
			// 务必添加下列一行
          .requestOnRationale()
          .requestPermissions(Manifest.permission.RECORD_AUDIO)
          .requestCodes(AUDIO_CODE)
          .request();

<h3 id="non_rationale">@PermissionsNonRationale</h3>

用户太傻逼，**拒绝权限**且**不再提示**（[国产畸形权限适配扩展](#extend)）情况下调用，此时意味着无论是 [@PermissionsCustomRationale](#custom_rationale) 或者 [@PermissionsRationale](#rationale) 都不会被调用，无法给予用户提示。permission 将会返回一个跳转至 **手机管家界面**或者**应用设置界面**的 intent，具体的设置方法请参考 [注解回调](#annotationm) 中 `.requestPageType(int)` 设置方法。。此时该注解修饰的函数被调用，注解中需要传入参数，分为两种情况：

- 单参数：`@PermissionsNonRationale(LOCATION_CODE)`，被修饰函数可只传入 Intent 参数，例：
>

	@PermissionsNonRationale({LOCATION_CODE})
	public void nonRationale(Intent intent) {
		startActivity(intent);
	}

> 也可传入 int 参数和 Intent 参数，例：
>

	@PermissionsNonRationale({LOCATION_CODE})
	public void nonRationale(int code, Intent intent) {
		startActivity(intent);
	}

- 多参数：`@PermissionsNonRationale(AUDIO_CODE, CALL_LOG_CODE)`，被修饰函数需传入 int 参数和 Intent 参数，例：
>

	@PermissionsNonRationale({AUDIO_CODE, CALL_LOG_CODE})
    public void nonRationale(int code, final Intent intent) {
        switch (code) {
            case AUDIO_CODE:
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("读取录音权限申请：\n我们需要您开启读取录音权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                break;
            case CALL_LOG_CODE:
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("读取通话记录权限申请：\n我们需要您开启读取通话记录权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }

注：**请勿对同步请求的权限使用该注解，理由见[项目答疑](#problem)第1条**

<h1 id="listener">Listener 回调</h1>

<h2 id="sinlge_listener">单个权限申请</h2>

	Permissions4M.get(MainActivity.this)
		// 是否强制弹出权限申请对话框，建议为 true，默认为 true
    	// .requestForce(true)
		// 是否支持 5.0 权限申请，默认为 false
		// .requestUnderM(false)
		// 权限
    	.requestPermissions(Manifest.permission.READ_CONTACTS)
		// 权限码
    	.requestCodes(READ_CONTACTS_CODE)
		// 权限请求结果
    	.requestListener(new Wrapper.PermissionRequestListener() {
       		@Override
        	public void permissionGranted(int code) {
            	ToastUtil.show("读取通讯录权限成功 in activity with listener");
        	}

        	@Override
        	public void permissionDenied(int code) {
            	ToastUtil.show("读取通讯录权失败 in activity with listener");
        	}

        	@Override
        	public void permissionRationale(int code) {
            	ToastUtil.show("请打开读取通讯录权限 in activity with listener");
        	}
    	})
		// 二次请求时回调
		.requestCustomRationaleListener(new Wrapper.PermissionCustomRationaleListener() {
                @Override
                public void permissionCustomRationale(int code) {
                    new AlertDialog.Builder(getActivity())
                       .setMessage("通讯录权限申请：\n我们需要您开启通讯录权限(in fragment with annotation)")
                       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               Permissions4M.get(NormalFragment.this)
                                          .requestOnRationale()
                                          .requestPermissions(Manifest.permission.READ_PHONE_STATE)
                                          .requestCodes(PHONE_STATE_CODE)
                                          .request();
                            }
                        })
                        .show();
                 }
        })
		// 权限完全被禁时回调函数中返回 intent 类型（手机管家界面）
    	.requestPageType(Permissions4M.PageType.MANAGER_PAGE)
		// 权限完全被禁时回调函数中返回 intent 类型（系统设置界面）
		//.requestPageType(Permissions4M.PageType.ANDROID_SETTING_PAGE)
		// 权限完全被禁时回调，接口函数中的参数 Intent 是由上一行决定的
    	.requestPage(new Wrapper.PermissionPageListener() {
	        	@Override
	        	public void pageIntent(final Intent intent) {
	        	    new AlertDialog.Builder(MainActivity.this)
	        	    .setMessage("读取通讯录权限申请：\n我们需要您开启读取通讯录权限(in activity with listener)")
	        	    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	        	        @Override
	        	        public void onClick(DialogInterface dialog, int which) {
	        	            startActivity(intent);
	        	        }
	        	    })
	        	    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	        	        @Override
	        	        public void onClick(DialogInterface dialog, int which) {
	        	            dialog.cancel();
	        	        }
	        	    })
	        	    .show();
	        	}
	    	})
    	.request();

<h2 id="mutli_listener">多个权限同步申请</h2>

	Permissions4M.get(NormalFragment.this)
            .requestPermissions(Manifest.permission.BODY_SENSORS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CALENDAR)
            .requestCodes(SENSORS_CODE, LOCATION_CODE, CALENDAR_CODE)
            .requestListener(new Wrapper.PermissionRequestListener() {
                @Override
                public void permissionGranted(int code) {
                    switch (code) {
                        case LOCATION_CODE:
                            ToastUtil.show("地理位置权限授权成功 in fragment with annotation");
                            break;
                        case SENSORS_CODE:
                            ToastUtil.show("传感器权限授权成功 in fragment with annotation");
                            break;
                        case CALENDAR_CODE:
                            ToastUtil.show("读取日历权限授权成功 in fragment with annotation");
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void permissionDenied(int code) {
                    switch (code) {
                        case LOCATION_CODE:
                            ToastUtil.show("地理位置权限授权失败 in fragment with annotation");
                            break;
                        case SENSORS_CODE:
                            ToastUtil.show("传感器权限授权失败 in fragment with annotation");
                            break;
                        case CALENDAR_CODE:
                            ToastUtil.show("读取日历权限授权失败 in fragment with annotation");
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void permissionRationale(int code) {
                    switch (code) {
                        case LOCATION_CODE:
                            ToastUtil.show("请开启地理位置权限 in fragment with annotation");
                            break;
                        case SENSORS_CODE:
                            ToastUtil.show("请开启传感器权限 in fragment with annotation");
                            break;
                        case CALENDAR_CODE:
                            ToastUtil.show("请开启读取日历权限 in fragment with annotation");
                            break;
                        default:
                            break;
                    }
                }
            })
            .requestCustomRationaleListener(new Wrapper.PermissionCustomRationaleListener() {
                @Override
                public void permissionCustomRationale(int code) {
                    switch (code) {
                        case LOCATION_CODE:
                            ToastUtil.show("请开启地理位置权限 in fragment with annotation");
                            Log.e("TAG", "permissionRationale: 请开启地理位置权限 ");

                            new AlertDialog.Builder(getActivity())
                                    .setMessage("地理位置权限权限申请：\n我们需要您开启地理位置权限(in fragment with " +
                                            "annotation)")
                                    .setPositiveButton("确定", new DialogInterface
                                            .OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Permissions4M.get(NormalFragment.this)
                                                    .requestOnRationale()
                                                    .requestPermissions(Manifest.permission
                                                            .ACCESS_FINE_LOCATION)
                                                    .requestCodes(LOCATION_CODE)
                                                    .request();
                                        }
                                    })
                                    .show();
                            break;
                        case SENSORS_CODE:
                            ToastUtil.show("请开启传感器权限 in fragment with annotation");
                            Log.e("TAG", "permissionRationale: 请开启传感器权限 ");

                            new AlertDialog.Builder(getActivity())
                                    .setMessage("传感器权限申请：\n我们需要您开启传感器权限(in fragment with " +
                                            "annotation)")
                                    .setPositiveButton("确定", new DialogInterface
                                            .OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Permissions4M.get(NormalFragment.this)
                                                    .requestOnRationale()
                                                    .requestPermissions(Manifest.permission
                                                            .BODY_SENSORS)
                                                    .requestCodes(SENSORS_CODE)
                                                    .request();
                                        }
                                    })
                                    .show();
                            break;
                        case CALENDAR_CODE:
                            ToastUtil.show("请开启读取日历权限 in fragment with annotation");
                            Log.e("TAG", "permissionRationale: 请开启读取日历权限 ");

                            new AlertDialog.Builder(getActivity())
                                    .setMessage("日历权限申请：\n我们需要您开启日历权限(in fragment with " +
                                            "annotation)")
                                    .setPositiveButton("确定", new DialogInterface
                                            .OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Permissions4M.get(NormalFragment.this)
                                                    .requestOnRationale()
                                                    .requestPermissions(Manifest.permission
                                                            .READ_CALENDAR)
                                                    .requestCodes(CALENDAR_CODE)
                                                    .request();
                                        }
                                    })
                                    .show();
                            break;
                        default:
                            break;
                    }

                }
            })
            .request();

注：**同步申请不支持 `PermissionPageListener` 回调，理由见[项目答疑](#problem)第1条**

<h2 id="pro">混淆</h2>
1.如果你是使用 listener 可以不必混淆

2.如果你是使用 annotation ，如下：
>
	-dontwarn com.joker.api.**
	-keep class com.joker.api.** {*;}
	-keep interface com.joker.api.** { *; }
	-keep class **$$PermissionsProxy { *; }

<h2 id="help">help me</h2>

**1.** 作者司里没有几部测试机，所以写到这一步之后就需要各位开发者共同努力，如果你在开发过程中使用了 vivo、魅族等权限适配也很畸形的手机，请联系作者或提交 issue 或 pull request。需要提交的资料包含：

- 机型、Android 版本
- 手机管家设置界面的包名（可使用 [AndroidTracker](https://github.com/fashare2015/ActivityTracker)）
- 权限申请的流程（`ContextCompat.checkSelfPermission(Context, String)` 返回结果是正常结果吗？                                `ActivityCompat.requestPermissions(Activity, String[], int)` 能否正常弹出权限申请对话框？ `Fragment.requestPermissions(String[], int)` 能否正常弹出权限申请对话框？`ActivityCompat.shouldShowRequestPermissionRationale(Activity, String)` 是否是正常结果，还是说跟小米一样，永远只会 false？

**2.** 目前针对主流权限的强制对话框弹出已经基本完成，但苦于作者功力有限，所以对于涉及到使用以下权限的代码暂时并未完善，如果各位开发者知道能够触发以下权限的代码，可以及时联系作者完善项目：

- Manifest.permission.GET_ACCOUNTS
- Manifest.permission.USE_SIP
- Manifest.permission.ADD_VOICEMAIL
- Manifest.permission.WRITE_CALENDAR
- Manifest.permission.CAMERA
- Manifest.permission.SEND_SMS
- Manifest.permission.RECEIVE_WAP_PUSH
- Manifest.permission.RECEIVE_MMS
- Manifest.permission.RECEIVE_SMS

<h2 id="extend">国产畸形权限适配扩展</h2>

**总结：**

- 小米
>
目前小米通讯录授权在授权失败时的原因有两种，可能是**用户手机中不存在联系人**，可能是用户拒绝授权，所以建议针对小米机型应给予相应的提示，原因参考下方[解决方案](#solu)

- OPPO
>
部分机型针对 `ContextCompat.checkSelfPermission(Context, String)` 判断是根据是否 `AndroidManifest.xml` 中声明了该权限来决定返回值，如果 `AndroidManifest.xml` 中声明了该权限，那么就将直接返回已授予权限（但实际上权限可能授予也可能未授予，国产小米和魅族等手机在低于6.0版本以下也是这样设计的），且不一定弹出权限申请对话框。

- 华为
>
部分权限的 `ActivityCompat.shouldShowRequestPermissionRationale(Activity, String)` 返回 false，故权限被拒后将会调用 `@PermissionsNonRationale` 所修饰的函数

- 魅族5.0+
>
目前手头上就几部5.0+版本的魅族测试机，遂测之。笔者国产中知道5.0+有权限管理的只有小米和魅族，5.0+权限申请原理可参考下方[解决方案](#solu)

<h2 id="solu">解决方案</h2>

- 国产机型的权限申请特点：

>**6.0**：国产大部分机型手机的申请权限实际上应该细致地分为**申请权限**和**应用权限** 。它们的 `ContextCompat.checkSelfPermission(Context, String)` 判断是根据是否 `AndroidManifest.xml` 中声明了该权限来决定返回值，在 `AndroidManifest.xml` 中声明了权限就返回 true，当然也会有一些会返回 false，这个是**申请权限**的过程。而真正对话框的弹出是在开发者**应用权限**的过程中，什么叫做应用权限？就是调用了会触发权限的代码，这个时候就会激活对话框，但是如果仅到这里那就 **too young too simple** 了，当傻逼用户点击拒绝授权时，还是可能会回调授权成功的方法。另外，国产机大部分权限是有三个状态——询问、允许、拒绝——大部分权限都是询问状态，但是有些权限默认是允许状态，有些是拒绝状态，这就导致了调用 `ContextCompat.checkSelfPermission(Context, String)` 方法时会更畸形，例如小米手机的获取 `READ_PHONE_STATE` 状态，默认是授予状态，但是如果傻逼用户手动设置为拒绝之后，就很麻烦了

>**5.0**：此时 google 还未着手处理动态权限申请这么个东西，但是我们牛(sha)逼(bi)的小米、魅族等厂商就开始提前设置了强大的权限管理，所以 6.0 权限申请代码在 5.0 上压根不管用，但是说来也简单，5.0 的权限申请对话框激活就是靠触发危险权限代码，然后根据返回值来判断权限是否获取到了（不同手机的返回值判断方式不同，此处需要一一定制）

- permissions4m 解决方案：

>在 **6.0** 上，permissions4m 首先调用原生**申请权限**，如果回调的结果是权限成功的话，那么为了确保真的获得到了这个权限，permissions4m 增加了一层**应用权限**的过程来确保是否真的授权，而在这个过程中权限申请对话框也是会被弹出的

>在 **5.0** 上，permissions4m 直接通过触发危险代码来激活权限申请对话框，然后根据返回值来判断是否授权成功

>当然，当傻逼用户完全拒绝权限时，permissions4m 将会回调响应函数返回一个可以跳转至**手机管家**或**系统设置**的 intent，方便傻逼用户打开权限。另外，由于作者个人能力有限，目前仅通过了小米、OPPO、魅族、华为等机型测试，不过这里面的机型已经可以代表许多国产机型的通病了。另外一点就是还有部分涉及到危险权限的代码并未补全，请参考 [help me](#help) 第二条

<h2 id="problem">项目答疑</h2>

**1.** 同步申请不支持 `@PermissionsNonRationale` / `PermissionPageListener` 回调（假设同步申请的权限为 A -> B -> C，那么当 B 被拒时，应该是弹出跳转权限的对话框还是申请权限 C 的对话框？）

**2.** 为什么不支持权限组申请？国产手机有部分畸形适配，虽然可以以一组权限的格式进行申请授权，但是却可以分别关闭同组权限内单个权限，如果要针对被拒绝的权限做回调，那么代码会显得很冗余。

<h2 id="job">find job/求职</h2>
寻实习一份，请发至我邮箱 `jokerzoc.cn@gmail.com`，诚挚感谢

## License ##

	Copyright 2016 joker
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	    http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
