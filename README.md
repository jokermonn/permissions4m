# Permissions4M #
意为 Permissions for M，基于 hongyangAndroid 的 [MPermissions](https://github.com/hongyangAndroid/MPermissions) 项目二次开发，使用编译时注解，较运行时注解效率更高。另较原有项目有以下升级：

- 支持 java8
- 支持一行代码同步请求多个权限申请
- 支持多种回调函数，代码可以更简洁

权限申请官方文档：[在运行时请求权限](https://developer.android.google.cn/training/permissions/requesting.html)

# 如何使用 #

* 注意事项
	* [必加的二次权限申请回调](#must_add)
	* [同步请求多个权限申请注意事项](#sync_request)
	* [单个权限申请注意事项](#notice_single)
	* [多个权限申请注意事项](#multiple_single)

* Activity
    * [单个权限申请](#single_activity)
    * [多个权限申请](#multiple_activity)
    * [同步请求多个权限申请](#sync_activity)
    * [单个权限申请自定义](#single_custom_activity)
    * [多个权限申请自定义](#mutiple_custom_activity)
    * [同步多个权限申请自定义](#sync_request_activity)
 
* Fragment
    * [单个权限申请](#single_fragment)
    * [多个权限申请](#multiple_fragment)
    * [同步请求多个权限申请](#sync_fragment)
    * [单个权限申请自定义](#single_custom_fragment)
    * [多个权限申请自定义](#mutiple_custom_fragment)
    * [同步多个权限申请自定义](#sync_request_fragment)

## 注意事项 ##

- **请不要忘了在 `AndroidManifest.xml` 中添加权限声明！！！**
- **请不要忘了在 `AndroidManifest.xml` 中添加权限声明！！！**
- **请不要忘了在 `AndroidManifest.xml` 中添加权限声明！！！**

<h3 id="must_add">必加的二次权限申请回调</h3>

在 Activity 或 Fragment 中，需要手动添加 `onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)` 方法以支持权限申请回调，代码如下即可：

	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

<h3 id="sync_request">同步请求多个权限申请注意事项</h3>

同步请求多个权限时，回调函数请使用[多个权限申请](#multiple_single)格式，即应将回调结果放至同一函数中，以 `code` 区别：

	// 权限申请成功回调
	@PermissionsGranted({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioGranted(int code) {
        switch (code) {
            case SMS_CODE:
                ToastUtil.show("短信权限申请成功 in fragment");
                break;
            case AUDIO_CODE:
                ToastUtil.show("录音权限申请成功 in fragment");
                break;
            default:
                break;
        }
    }

而不可如下：

	// 权限申请成功回调
	@PermissionsGranted(SMS_CODE)
    public void smsGranted() {
        ToastUtil.show("短信权限申请成功 in fragment");
    }

	// 权限申请成功回调
	@PermissionsGranted(AUDIO_CODE)
    public void audioGranted() {
        ToastUtil.show("录音权限申请成功 in fragment");
    }

<h3 id="notice_single">单个权限申请注意事项</h3>

针对**单个权限申请**，注解所修饰的方法是不含参数的，**应如下**：

	@PermissionsGranted(CONTACT_CODE)
    public void contactGranted() {
        ToastUtil.show("读取联系人权限成功");
    }

而如下是**不可取**的：

	// 注意方法体中含有形式参数
	@PermissionsGranted(CONTACT_CODE)
    public void contactGranted(int code) {
        ToastUtil.show("读取联系人权限成功");
    }

<h3 id="multiple_single">多个权限申请注意事项</h3>

针对**多个权限申请**，注解所修饰的方法是含参数的，**应如下**：

	@PermissionsGranted({STORAGE_CODE, CALL_CODE})
    public void storageAndCallGranted(int code) {
        switch (code) {
            case STORAGE_CODE:
                ToastUtil.show("设备存储权限授权成功");
                break;
            case CALL_CODE:
                ToastUtil.show("通话权限授权成功");
                break;
        }
    }

而如下是**不可取**的：

	// 注意方法体中不含形式参数
	@PermissionsGranted({STORAGE_CODE, CALL_CODE})
    public void storageAndCallGranted() {
    }

## Activity ##

<h3 id="single_activity">单个权限申请</h3>
	
	private static final int CONTACT_CODE = 3;

	// 单个申请
    mContactsButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Permissions4M.requestPermission(MainActivity.this, Manifest.permission.READ_CONTACTS,CONTACT_CODE);
        }
    });

	// 注册回调权限申请函数
	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

	// 权限申请成功
	@PermissionsGranted(CONTACT_CODE)
    public void contactGranted() {
        ToastUtil.show("读取联系人权限成功");
    }

	// 权限申请失败
    @PermissionsDenied(CONTACT_CODE)
    public void contactDenied() {
        ToastUtil.show("读取联系人权限失败");
    }

	// 二次申请权限时调用
    @PermissionsRationale(CONTACT_CODE)
    public void contactRationale() {
        ToastUtil.show("请开启读取联系人权限");
    }

<h3 id="multiple_activity">多个权限申请</h3>

	private static final int STORAGE_CODE = 1;
    private static final int CALL_CODE = 2;

	mCallButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Permissions4M.requestPermission(MainActivity.this, Manifest.permission.CALL_PHONE,CALL_CODE);
        }
    });
    mStorageButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Permissions4M.requestPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE,STORAGE_CODE);
            }
    });

	// 注册回调权限申请函数
	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

	// 权限申请成功
	@PermissionsGranted({STORAGE_CODE, CALL_CODE})
    public void storageAndCallGranted(int code) {
        switch (code) {
            case STORAGE_CODE:
                ToastUtil.show("设备存储权限授权成功");
                break;
            case CALL_CODE:
                ToastUtil.show("通话权限授权成功");
                break;
        }
    }

	// 权限申请失败
    @PermissionsDenied({STORAGE_CODE, CALL_CODE})
    public void storageAndCallDenied(int code) {
        switch (code) {
            case STORAGE_CODE:
                ToastUtil.show("设备存储权限授权失败");
                break;
            case CALL_CODE:
                ToastUtil.show("通话权限授权失败");
                break;
        }
    }

	// 二次申请权限时调用
    @PermissionsRationale({STORAGE_CODE, CALL_CODE})
    public void storageAndCallRationale(int code) {
        switch (code) {
            case STORAGE_CODE:
                ToastUtil.show("请开启设备存储权限授权");
                break;
            case CALL_CODE:
                ToastUtil.show("请开启通话权限授权");
                break;
        }
    }

<h3 id="sync_activity">同步请求多个权限申请</h3>

1.首先在 Activity 上添加注解，如下：

	@PermissionsRequestSync(
		permission = {Manifest.permission.BODY_SENSORS,
						Manifest.permission.ACCESS_FINE_LOCATION,
							Manifest.permission.READ_CALENDAR},
		value = {SENSORS_CODE,
					LOCATION_CODE,
						CALENDAR_CODE})
	public class MainActivity extends AppcompatActivity

注解中需要添加两个数组，permission 数组放入需要同步申请的权限，value 数组放入相对应的结果码，顺序无关

2.使用如下代码开始同步申请权限：

	Permissions4M.syncRequestPermissions(MainFragment.this);

申请顺序将会参考你所写的权限的顺序，例如如上的顺序是 `Manifest.permission.BODY_SENSORS` -> `Manifest.permission.ACCESS_FINE_LOCATION` -> `Manifest.permission.READ_CALENDAR`。

3.请求回调函数请使用以下格式撰写（**不支持将回调函数分开**）：

	// 权限申请成功时回调
	@PermissionsGranted({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncGranted(int code) {
        switch (code) {
            case LOCATION_CODE:
                ToastUtil.show("地理位置权限授权成功 in activity");
                break;
            case SENSORS_CODE:
                ToastUtil.show("传感器权限授权成功 in activity");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("读取日历权限授权成功 in activity");
                break;
            default:
                break;
        }
    }

	// 权限申请失败时回调
    @PermissionsDenied({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncDenied(int code) {
        switch (code) {
            case LOCATION_CODE:
                ToastUtil.show("地理位置权限授权失败 in activity");
                break;
            case SENSORS_CODE:
                ToastUtil.show("传感器权限授权失败 in activity");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("读取日历权限授权失败 in activity");
                break;
            default:
                break;
        }
    }

	// 二次权限申请时回调
    @PermissionsRationale({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncRationale(int code) {
        switch (code) {
            case LOCATION_CODE:
                ToastUtil.show("请开启地理位置权限 in activity");
                break;
            case SENSORS_CODE:
                ToastUtil.show("请开启传感器权限 in activity");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("请开启读取日历权限 in activity");
                break;
            default:
                break;
        }
    }

<h3 id="single_custom_activity">单个权限申请自定义</h3>

	private static final int CAMERA_CODE = 4;

	mCameraButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Permissions4M.requestPermission(MainActivity.this, Manifest.permission.CAMERA, CAMERA_CODE);
        }
    });

	// 注册回调权限申请函数
	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

	// 权限申请成功
	@PermissionsGranted(CAMERA_CODE)
    public void cameraGranted() {
        ToastUtil.show("相机权限授权成功");
    }

	// 权限申请失败
    @PermissionsDenied(CAMERA_CODE)
    public void cameraDenied() {
        ToastUtil.show("相机权限授权失败");
    }

	// 二次申请权限时回调
    @PermissionsCustomRationale(CAMERA_CODE)
    public void cameraCustomRationale() {
        new AlertDialog.Builder(this)
                .setMessage("相机权限申请：\n我们需要您开启相机信息权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 请自行处理申请权限，两者方法等价
                        // 方法1.使用框架封装方法
                        Permissions4M.requestPermissionOnCustomRationale(MainActivity.this, new String[]{Manifest
                                .permission.CAMERA}, CAMERA_CODE);
                        // 方法2.使用自身方法
						// ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest
						// .permission.CAMERA}, CAMERA_CODE);
                    }
                })
                .show();
    }

<h3 id="mutiple_custom_activity">多个权限申请自定义</h3>

	private static final int SMS_CODE = 5;
    private static final int AUDIO_CODE = 6;

	mSmsButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Permissions4M.requestPermission(MainActivity.this, Manifest.permission.READ_SMS, SMS_CODE);
        }
    });
    mAudioButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Permissions4M.requestPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO,AUDIO_CODE);
        }
    });

	// 注册回调权限申请函数
	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

	// 权限申请成功
	@PermissionsGranted({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioGranted(int code) {
        switch (code) {
            case SMS_CODE:
                ToastUtil.show("短信权限申请成功");
                break;
            case AUDIO_CODE:
                ToastUtil.show("录音权限申请成功");
                break;
            default:
                break;
        }
    }

	// 权限申请失败
    @PermissionsDenied({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioDenied(int code) {
        switch (code) {
            case SMS_CODE:
                ToastUtil.show("短信权限申请失败");
                break;
            case AUDIO_CODE:
                ToastUtil.show("录音权限申请失败");
                break;
            default:
                break;
        }
    }

	// 二次申请权限时回调
    @PermissionsCustomRationale({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioCustomRationale(int code) {
        switch (code) {
            case SMS_CODE:
                new AlertDialog.Builder(this)
                        .setMessage("短信权限申请：\n我们需要您开启短信权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 请自行处理申请权限，两者方法等价
                                // 方法1.使用框架封装方法
                                Permissions4M.requestPermissionOnCustomRationale(MainActivity.this, new String[]{Manifest
                                        .permission.READ_SMS}, SMS_CODE);
                                // 方法2.使用自身方法
								// ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest
								// .permission.READ_SMS}, SMS_CODE);
                            }
                        })
                        .show();
                break;
            case AUDIO_CODE:
                new AlertDialog.Builder(this)
                        .setMessage("录音权限申请：\n我们需要您开启录音权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 请自行处理申请权限
                                // 请自行处理申请权限，两者方法等价
                                // 方法1.使用框架封装方法
                                Permissions4M.requestPermissionOnCustomRationale(MainActivity.this, new String[]{Manifest
                                        .permission.RECORD_AUDIO}, AUDIO_CODE);
                                // 方法2.使用自身方法
								// ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest
								// .permission.RECORD_AUDIO}, AUDIO_CODE);
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }

<h3 id="sync_request_activity">同步多个权限申请自定义</h3>

同[多个权限申请自定义](#mutiple_custom_activity)，参考代码如下：

	// 同步申请
        mOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.syncRequestPermissions(MainActivity.this);
            }
        });

	@PermissionsCustomRationale({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncCustomRationale(int code) {
        switch (code) {
            case LOCATION_CODE:
                new AlertDialog.Builder(this)
                        .setMessage("地理位置权限申请：\n我们需要您开启地理位置权限(in activity)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Permissions4M.requestPermissionOnCustomRationale(MainActivity.this, new
                                        String[]{Manifest
                                        .permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
                            }
                        })
                        .show();
                break;
            case SENSORS_CODE:
                new AlertDialog.Builder(this)
                        .setMessage("传感器权限申请：\n我们需要您开启传感器权限(in activity)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Permissions4M.requestPermissionOnCustomRationale(MainActivity.this, new
                                        String[]{Manifest
                                        .permission.BODY_SENSORS}, SENSORS_CODE);
                            }
                        })
                        .show();
                break;
            case CALENDAR_CODE:
                new AlertDialog.Builder(this)
                        .setMessage("读取日历权限申请：\n我们需要您开启读取日历权限(in activity)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Permissions4M.requestPermissionOnCustomRationale(MainActivity.this, new
                                        String[]{Manifest
                                        .permission.READ_CALENDAR}, CALENDAR_CODE);
                            }
                        })
                        .show();
                break;
        }
	}

## Fragment ##

<h3 id="single_fragment">单个权限申请</h3>

	private static final int CONTACT_CODE = 3;

	mContactsButton.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Permissions4M.requestPermission(MainFragment.this, Manifest.permission.READ_CONTACTS,CONTACT_CODE);
       }
    });

	// 注册回调权限申请函数
	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

	// 权限申请成功回调
	@PermissionsGranted(CONTACT_CODE)
    public void contactGranted() {
        ToastUtil.show("读取联系人权限成功 in fragment");
    }

	// 权限申请失败回调
    @PermissionsDenied(CONTACT_CODE)
    public void contactDenied() {
        ToastUtil.show("读取联系人权限失败 in fragment");
    }

	// 二次申请回调
    @PermissionsRationale(CONTACT_CODE)
    public void contactRationale() {
        ToastUtil.show("请开启读取联系人权限 in fragment");
    }

<h3 id="multiple_fragment">多个权限申请</h3>

	private static final int STORAGE_CODE = 1;
    private static final int CALL_CODE = 2;

	mCallButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Permissions4M.requestPermission(MainFragment.this, Manifest.permission.CALL_PHONE,CALL_CODE);
        }
    });
    mStorageButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Permissions4M.requestPermission(MainFragment.this, Manifest.permission.WRITE_EXTERNAL_STORAGE,STORAGE_CODE);
        }
    });

	// 注册回调权限申请函数
	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

	// 权限申请成功回调
	@PermissionsGranted({STORAGE_CODE, CALL_CODE})
    public void storageAndCallGranted(int code) {
        switch (code) {
            case STORAGE_CODE:
                ToastUtil.show("设备存储权限授权成功 in fragment");
                break;
            case CALL_CODE:
                ToastUtil.show("通话权限授权成功 in fragment");
                break;
        }
    }

	// 权限申请失败回调
    @PermissionsDenied({STORAGE_CODE, CALL_CODE})
    public void storageAndCallDenied(int code) {
        switch (code) {
            case STORAGE_CODE:
                ToastUtil.show("设备存储权限授权失败 in fragment");
                break;
            case CALL_CODE:
                ToastUtil.show("通话权限授权失败 in fragment");
                break;
        }
    }

	// 二次申请回调
    @PermissionsRationale({STORAGE_CODE, CALL_CODE})
    public void storageAndCallRationale(int code) {
        switch (code) {
            case STORAGE_CODE:
                ToastUtil.show("请开启设备存储权限授权 in fragment");
                break;
            case CALL_CODE:
                ToastUtil.show("请开启通话权限授权 in fragment");
                break;
        }
    }

<h3 id="sync_fragment">同步请求多个权限申请</h3>

1.首先在 Fragment 上添加注解，如下：

	@PermissionsRequestSync(
		permission = {Manifest.permission.BODY_SENSORS,
						Manifest.permission.ACCESS_FINE_LOCATION,
							Manifest.permission.READ_CALENDAR},
		value = {SENSORS_CODE,
					LOCATION_CODE,
						CALENDAR_CODE})
	public class MainFragment extends Fragment

注解中需要添加两个数组，permission 数组放入需要同步申请的权限，value 数组放入相对应的结果码，顺序无关

2.使用如下代码开始同步申请权限：

	Permissions4M.syncRequestPermissions(MainFragment.this);

申请顺序将会参考你所写的权限的顺序，例如如上的顺序是 `Manifest.permission.BODY_SENSORS` -> `Manifest.permission.ACCESS_FINE_LOCATION` -> `Manifest.permission.READ_CALENDAR`。

3.请求回调函数请使用以下格式撰写（**不支持将回调函数分开**）：

	// 权限申请成功时回调
	@PermissionsGranted({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncGranted(int code) {
        switch (code) {
            case LOCATION_CODE:
                ToastUtil.show("地理位置权限授权成功 in activity");
                break;
            case SENSORS_CODE:
                ToastUtil.show("传感器权限授权成功 in activity");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("读取日历权限授权成功 in activity");
                break;
            default:
                break;
        }
    }

	// 权限申请失败时回调
    @PermissionsDenied({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncDenied(int code) {
        switch (code) {
            case LOCATION_CODE:
                ToastUtil.show("地理位置权限授权失败 in activity");
                break;
            case SENSORS_CODE:
                ToastUtil.show("传感器权限授权失败 in activity");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("读取日历权限授权失败 in activity");
                break;
            default:
                break;
        }
    }

	// 二次权限申请时回调
    @PermissionsRationale({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncRationale(int code) {
        switch (code) {
            case LOCATION_CODE:
                ToastUtil.show("请开启地理位置权限 in activity");
                break;
            case SENSORS_CODE:
                ToastUtil.show("请开启传感器权限 in activity");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("请开启读取日历权限 in activity");
                break;
            default:
                break;
        }
    }

<h3 id="single_custom_fragment">单个权限申请自定义</h3>

	private static final int CAMERA_CODE = 4;

	mCameraButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Permissions4M.requestPermission(MainFragment.this, Manifest.permission.CAMERA, CAMERA_CODE);
        }
    });

	// 注册回调权限申请函数
	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

	// 权限申请成功回调
	@PermissionsGranted(CAMERA_CODE)
    public void cameraGranted() {
        ToastUtil.show("相机权限授权成功 in fragment");
    }

	// 权限申请失败回调
    @PermissionsDenied(CAMERA_CODE)
    public void cameraDenied() {
        ToastUtil.show("相机权限授权失败 in fragment");
    }

	// 二次申请回调
    @PermissionsCustomRationale(CAMERA_CODE)
    public void cameraCustomRationale() {
        new AlertDialog.Builder(getActivity())
                .setMessage("相机权限申请：\n我们需要您开启相机信息权限(in fragment)")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 请自行处理申请权限，两种方法等价
                        // 方法1，使用框架已封装的方法
                        Permissions4M.requestPermissionOnCustomRationale(MainFragment.this, new
                                String[]{Manifest
                                .permission.CAMERA}, CAMERA_CODE);
                        // 方法2，使用自身方法
						// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						// MainFragment.this.requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_CODE);
						// }
                    }
                })
                .show();
    }

<h3 id="mutiple_custom_fragment">多个权限申请自定义</h3>

	private static final int SMS_CODE = 5;
    private static final int AUDIO_CODE = 6;

	mSmsButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Permissions4M.requestPermission(MainFragment.this, Manifest.permission.READ_SMS, SMS_CODE);
        }
    });
    mAudioButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Permissions4M.requestPermission(MainFragment.this, Manifest.permission.RECORD_AUDIO,
                    AUDIO_CODE);
        }
    });

	// 注册回调权限申请函数
	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

	// 权限申请成功回调
	@PermissionsGranted({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioGranted(int code) {
        switch (code) {
            case SMS_CODE:
                ToastUtil.show("短信权限申请成功 in fragment");
                break;
            case AUDIO_CODE:
                ToastUtil.show("录音权限申请成功 in fragment");
                break;
            default:
                break;
        }
    }

	// 权限申请失败回调
    @PermissionsDenied({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioDenied(int code) {
        switch (code) {
            case SMS_CODE:
                ToastUtil.show("短信权限申请失败 in fragment");
                break;
            case AUDIO_CODE:
                ToastUtil.show("录音权限申请失败 in fragment");
                break;
            default:
                break;
        }
    }

	// 二次申请回调
    @PermissionsCustomRationale({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioCustomRationale(int code) {
        switch (code) {
            case SMS_CODE:
                new AlertDialog.Builder(getActivity())
                        .setMessage("短信权限申请：\n我们需要您开启短信权限(in fragment)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 请自行处理申请权限，两种方法等价
                                // 方法1，使用框架已封装的方法
                                Permissions4M.requestPermissionOnCustomRationale(MainFragment.this, new
                                        String[]{Manifest
                                        .permission.READ_SMS}, SMS_CODE);
                                // 方法2，使用自身方法
								// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
								// MainFragment.this.requestPermissions(new String[]{Manifest.permission.READ_SMS}, SMS_CODE);
								// }
                            }
                        })
                        .show();
                break;
            case AUDIO_CODE:
                new AlertDialog.Builder(getActivity())
                        .setMessage("录音权限申请：\n我们需要您开启录音权限(in fragment)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 请自行处理申请权限，两种方法等价
                                // 方法1，使用框架已封装的方法
                                Permissions4M.requestPermissionOnCustomRationale(MainFragment.this, new
                                        String[]{Manifest
                                        .permission.RECORD_AUDIO}, AUDIO_CODE);
                                // 方法2，使用自身方法
								// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
								// MainFragment.this.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_CODE);
								// }
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }

<h3 id="sync_request_fragment">同步多个权限申请自定义</h3>

请参考 Activity 版本：[同步多个请求申请自定义](#sync_request_activity)

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
