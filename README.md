[![apk](https://img.shields.io/badge/apk-download-orange.svg)](https://github.com/jokermonn/permissions4m/blob/master/app-debug.apk?raw=true)
[![GitHub stars](https://img.shields.io/github/stars/jokermonn/permissions4m.svg?style=social)](https://img.shields.io/github/stars/jokermonn/permissions4m.svg?style=social)

[![platform](https://img.shields.io/badge/platform-android-brightgreen.svg)](https://developer.android.com/index.html)
[![license](https://img.shields.io/badge/license-Apach2.0-green.svg)](https://github.com/jokermonn/permissions4m/blob/master/LICENSE.txt)

[![lib](https://img.shields.io/badge/lib-1.0.9-blue.svg)](https://github.com/jokermonn/permissions4m/releases/tag/1.0.9-lib)
[![processor](https://img.shields.io/badge/processor-1.0.9-blue.svg)](https://github.com/jokermonn/permissions4m/releases/tag/1.0.9-processor)
[![annotation](https://img.shields.io/badge/annotation-1.0.3-blue.svg)](https://jcenter.bintray.com/com/jokermonn/permissions4m-annotation/1.0.3/)

# 中文|[ENGLISH](https://github.com/jokermonn/permissions4m/blob/master/README_EN.md) #

# 前言 #

- **还在为 `ActivityCompat.shouldShowRequestPermissionRationale(Activity, String)` 无法弹出权限申请对话框困惑么？Permissions4M 让它必须弹出来**
- **还在为明明授权失败，却回调的是权限申请成功方法而苦恼么？ Permissions4M 让它必须回调期望的方法**
- **还在为用户拒绝且不再提示权限申请对话框而烦躁么？ Permissions4M 让它跳转到手机管家权限设置界面**

# Permissions4M #
意为 Permissions for M，基于 hongyangAndroid 的 [MPermissions](https://github.com/hongyangAndroid/MPermissions) 项目二次开发，使用编译时注解，较运行时注解效率更高。另较原有项目有以下升级：

- 支持 java8
- **支持一行代码同步请求多个权限**
- 支持多种回调函数，代码可以更简洁
- **支持大量国产机型适配**

权限申请官方文档：[在运行时请求权限](https://developer.android.google.cn/training/permissions/requesting.html)

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
      compile 'com.github.jokermonn:permissions4m:1.0.9-lib'
      annotationProcessor 'com.github.jokermonn:permissions4m:1.0.9-processor'
	}

# 使用文档 #

* 注意事项
	* [**必加的二次权限申请回调**](#must_add)
* [接口回调](#annotation)
* [Listener 回调](#listener)
* [同步申请](#sync)
* 关于项目
	* [help me](#help)
	* [国产畸形权限适配扩展](#extend)
	* [项目答疑](#problem)

# 版本迭代 #

- TODO:[help me](#help)

- v1.0.8
	- 修复内存泄露

- v1.0.6
	- 修复代理类空指针异常

- v1.0.5
	- 低版本默认授权成功
	- 更改最低版兼容 sdk 到`11`

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

<h3 id="xiaomi_contacts"></h3>

<h1 id="annotation">注解回调</h1>

在需要权限申请的地方调用

	Permissions4M.get(MainActivity.this)
				// 是否强制弹出权限申请对话框，建议为 true
                .requestForce(true)
				// 权限
                .requestPermission(Manifest.permission.RECORD_AUDIO)
				// 权限码
                .requestCode(AUDIO_CODE)
				// 如果需要使用 @PermissionNonRationale 注解的话，建议添加如下一行
				// 返回的 intent 是跳转至**系统设置页面**
                // .requestPageType(Permissions4M.PageType.MANAGER_PAGE)
				// 返回的 intent 是跳转至**手机管家页面**
				// .requestPageType(Permissions4M.PageType.ANDROID_SETTING_PAGE)
                .request();

如：

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Permissions4M.get(MainActivity.this)
                    .requestForce(true)
					.requestPageType(Permissions4M.PageType.MANAGER_PAGE)
                    .requestPermission(Manifest.permission.RECORD_AUDIO)
                    .requestCode(AUDIO_CODE)
                    .request();
        }
    });

然后将会回调相应的 [`@PermissionsGranted`](#granted)、[`@PermissionsDenied`](#denied)、[`@PermissionsRationale`](#rationale)/[`PermissionsCustomRationale`](#custom_rationale)、[`@PermissionsNonRationale`](#non_rationale) 所修饰的方法

<h2 id="granted">@PermissionsGranted</h2>

授权**成功**时回调，注解中需要传入参数，分为两种情况：

- 单参数：`@PermissionsGranted(LOCATION_CODE)`，被修饰函数无需传入参数，例：
>

	@PermissionsGranted(LOCATION_CODE)
    public void granted() {
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

<h2 id="denied">@PermissionsDenied</h2>

授权**失败**时回调，注解中需要传入参数，分为两种情况：

- 单参数：`@PermissionsDenied(LOCATION_CODE)`，被修饰函数无需传入参数，例：
>

	@PermissionsDenied(LOCATION_CODE)
    public void denied() {
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

<h2 id="rationale">@PermissionsRationale</h2>
二次授权时回调，用于解释为何需要此权限，注解中需要传入参数，分为两种情况：

- 单参数：`@PermissionsRationale(LOCATION_CODE)`，被修饰函数无需传入参数，例：
>

	@PermissionsRationale(LOCATION_CODE)
    public void rationale() {
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

**注：系统弹出权限申请 dialog 与 toast 提示是异步操作，所以如果存在希望自行弹出一个 dialog 后（或其他同步需求）再弹出系统对话框，那么请使用 [@PermissionsCustomRationale](#custom_rationale)**

<h2 id="custom_rationale">@PermissionsCustomRationale</h2>

二次授权时回调，用于解释为何需要此权限，注解中需要传入参数，分为两种情况：

- 单参数：`@PermissionsCustomRationale(LOCATION_CODE)`，被修饰函数无需传入参数，例：
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
                                        .requestPermission(Manifest.permission.READ_SMS)
                                        .requestCode(SMS_CODE)
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
                                        .requestPermission(Manifest.permission.READ_SMS)
                                        .requestCode(SMS_CODE)
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
                                        .requestPermission(Manifest.permission.RECORD_AUDIO)
                                        .requestCode(AUDIO_CODE)
                                        .request();
                            }
                        })
                        .show();
                break;
            default:
                break;
        }

注：除上述以外的 dialog，开发者可以自定义其他展示效果，调用权限申请时请使用，否则会陷入无限调用自定义 Rationale 循环中： 

	Permissions4M.get(MainActivity.this)
			// 务必添加下列一行
          .requestOnRationale()
          .requestPermission(Manifest.permission.RECORD_AUDIO)
          .requestCode(AUDIO_CODE)
          .request();

<h2 id="non_rationale">@PermissionsNonRationale</h2>

用户太傻逼，**拒绝权限**且**不再提示**（[国产畸形权限适配扩展](#extend)）情况下调用，此时意味着无论是 [@PermissionsCustomRationale](#custom_rationale) 或者 [@PermissionsRationale](#rationale) 都不会被调用，无法给予用户提示，此时该注解修饰的函数被调用，注解中需要传入参数，分为两种情况：

- 单参数：`@PermissionsNonRationale(LOCATION_CODE)`，被修饰函数只需传入 Intent 参数，例：
>

	@PermissionsNonRationale({LOCATION_CODE})
	public void nonRationale(Intent intent) {
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

Intent 类型为两种，一种是跳转至**系统设置页面**，另一种是跳至**手机管家页面**，而具体的设置方法请参考 [注解回调](#annotationm) 中 `.requestPageType(int)` 设置方法。

<h1 id="listener"> Listener 回调</h1>

例：

	Permissions4M.get(MainActivity.this)
		// 是否强制弹出权限申请对话框
    	.requestForce(true)
		// 权限
    	.requestPermission(Manifest.permission.READ_CONTACTS)
		// 权限码
    	.requestCode(READ_CONTACTS_CODE)
		// 权限请求结果
    	.requestCallback(new Wrapper.PermissionRequestListener() {
       		@Override
        	public void permissionGranted() {
            	ToastUtil.show("读取通讯录权限成功 in activity with listener");
        	}

        	@Override
        	public void permissionDenied() {
            	ToastUtil.show("读取通讯录权失败 in activity with listener");
        	}

        	@Override
        	public void permissionRationale() {
            	ToastUtil.show("请打开读取通讯录权限 in activity with listener");
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

<h1 id="sync">同步申请</h1>

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

注：同步申请默认强制申请(`requestForce(true)`)，同步申请不支持 `@PermissionsNonRationale`

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

- 小米
>
大概是我最想 f**k 的机型了吧，适配之路异常凶险，此机型 `ActivityCompat.shouldShowRequestPermissionRationale(Activity, String)` 永远返回 false，意味着不能够在用户拒绝之后给予提示。且不一定弹出权限申请对话框。

- OPPO
>
此机型，针对 `ContextCompat.checkSelfPermission(Context, String)` 判断是根据是否 `AndroidManifest.xml` 中声明了该权限来决定返回值，如果 `AndroidManifest.xml` 中声明了该权限，那么就将直接返回已授予权限（但实际上权限可能授予也可能未授予），且不一定弹出权限申请对话框。

- 华为
>
部分权限的 `ActivityCompat.shouldShowRequestPermissionRationale(Activity, String)` 返回 false，故权限被拒后将会调用 `@PermissionsNonRationale` 所修饰的函数

<h2 id="problem">项目答疑</h2>

**1.** Listener 回调暂不支持自定义 Rationale

**2.** 同步申请不支持 `@PermissionsNonRationale` 回调（假设同步申请的权限为 A -> B -> C，那么当 B 被拒时，应该是弹出对话框让用户继续申请还是应该继续 C 权限申请？）

**3.** 为什么不支持权限组申请？国产手机有部分畸形适配，虽然可以以一组权限的格式进行申请授权，但是却可以分别关闭同组权限内单个权限，如果要针对被拒绝的权限做回调，那么代码会显得很冗余。

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
