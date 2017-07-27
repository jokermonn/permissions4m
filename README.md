# Permissions4M #
意为 Permissions for M，基于 hongyangAndroid 的 [MPermissions](https://github.com/hongyangAndroid/MPermissions) 项目二次开发，项目使用编译时注解，较运行时注解效率更高。权限申请官方文档：[在运行时请求权限](https://developer.android.google.cn/training/permissions/requesting.html)

# 如何使用 #

[注意事项](#notice)

- Activity
 - [单个权限申请](#single_activity)
 - [多个权限申请](#multiple_activity)
 - [单个权限申请自定义](#single_custom_activity)
 - [多个权限申请自定义](#mutiple_custom_activity)

<h2 id="notice">注意事项</h2>

针对**单个权限申请**，注解所修饰的方法是不含参数的，应如下：

	// 权限申请成功
	@PermissionsGranted(CONTACT_CODE)
    public void contactGranted() {
        ToastUtil.show("读取联系人权限成功");
    }

而如下是不可取的：

	// 权限申请成功
	@PermissionsGranted(CONTACT_CODE)
	// 注意方法体中含有形式参数
    ~~public void contactGranted(int code) {~~
        ToastUtil.show("读取联系人权限成功");
    }

针对**多个权限申请**，注解所修饰的方法是含参数的，应如下：

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

而如下是不可取的：

	// 权限申请成功
	@PermissionsGranted({STORAGE_CODE, CALL_CODE})
	// 注意方法体中不含形式参数
    ~~public void storageAndCallGranted() {~~
        switch (code) {
            case STORAGE_CODE:
                ToastUtil.show("设备存储权限授权成功");
                break;
            case CALL_CODE:
                ToastUtil.show("通话权限授权成功");
                break;
        }
    }

<h2 id="single_activity">单个权限申请</h2>
	
	private static final int CONTACT_CODE = 3;

	// 单个申请
    mContactsButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Permissions4M.requestPermission(MainActivity.this, Manifest.permission.READ_CONTACTS,CONTACT_CODE);
        }
    });

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

<h2 id="multiple_activity">多个权限申请</h2>

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

<h2 id="single_custom_activity">单个权限申请自定义</h2>

	private static final int CAMERA_CODE = 4;

	mCameraButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Permissions4M.requestPermission(MainActivity.this, Manifest.permission.CAMERA, CAMERA_CODE);
        }
    });

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
                        // 请自行处理申请权限
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest
                                .permission.CAMERA}, CAMERA_CODE);
                    }
                })
                .show();
    }

<h2 id="mutiple_custom_activity">多个权限申请自定义</h2>

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
                                // 请自行处理申请权限
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest
                                        .permission.READ_SMS}, SMS_CODE);
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
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest
                                        .permission.RECORD_AUDIO}, AUDIO_CODE);
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }