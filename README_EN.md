[![apk](https://img.shields.io/badge/apk-download-orange.svg)](https://github.com/jokermonn/permissions4m/blob/master/jars/app-debug.apk?raw=true)
[![GitHub stars](https://img.shields.io/github/stars/jokermonn/permissions4m.svg?style=social)](https://img.shields.io/github/stars/jokermonn/permissions4m.svg?style=social)

[![platform](https://img.shields.io/badge/platform-android-brightgreen.svg)](https://developer.android.com/index.html)
[![license](https://img.shields.io/badge/license-Apach2.0-green.svg)](https://github.com/jokermonn/permissions4m/blob/master/LICENSE.txt)

[![lib](https://img.shields.io/badge/lib-1.0.0-blue.svg)](https://github.com/jokermonn/permissions4m/releases/tag/1.0.0-lib)
[![processor](https://img.shields.io/badge/processor-1.0.0-blue.svg)](https://github.com/jokermonn/permissions4m/releases/tag/1.0.0-processor)
[![annotation](https://img.shields.io/badge/annotation-1.0.0-blue.svg)](https://github.com/jokermonn/permissions4m/releases/tag/1.0.0-annotation)

# [中文](https://github.com/jokermonn/permissions4m/blob/master/README.md)|ENGLISH #

# Permissions4M #
Permissions for M, which is based on the secondary development of hongyangAndroid [MPermissions](https://github.com/hongyangAndroid/MPermissions), and applies Compiling-time annotations, so it is more effective than Run-time annotations. Compared to the former version, this one improves as follows:

- java 8 supported
- multi permission requests, synchronously  
- multi callbacks supported, clean requestCode
Official doc for permission requests ：[Requesting Permissions at Run Time](https://developer.android.com/training/permissions/requesting.html)

# Feature #
- one line requestCode, multi permission requests, synchronously：

![](http://imglf0.nosdn.127.net/img/MXFneHJjVkcvalFpQTlzdEQxZWZSdjVJN1Y2TzRsS1NkTkw3YXVUR0xHMGZ6bkdONXpQZU5nPT0.gif)

- custom dialog when request permission second time：

![](http://imglf.nosdn.127.net/img/MXFneHJjVkcvalFpQTlzdEQxZWZSdUdNY0xKWklvc3E0OTQ2Z2VQSytKQTAvLzUvaGc2dDN3PT0.gif)

# Dependencies #

## jitpack Dependencies ##

`build.gradle` in `project`：

	buildscript {
      // ...
	}

	allprojects {
      repositories {
        // add next line of codes
        maven { url 'https://jitpack.io' }
      }
	}

 `build.gradle` in `app` ：

	dependencies {
      compile 'com.github.jokermonn:permissions4m:1.0.0-lib'
      annotationProcessor 'com.github.jokermonn:permissions4m:1.0.0-processor'
	}

## jar/aar Dependencies ##

jar/aar is being used here, you can download the following jars[link](https://github.com/jokermonn/Permissions4M/tree/master/jars), then put them into your `lib` folder, and add the following codes in `app.gradle`:

	android {
		// ...
	}

	repositories {
	    flatDir {
	        dirs 'libs'
	    }
	}

	dependencies {
		// add codes as follows
		compile(name: 'permissions4m-api', ext: 'aar')
		compile files('libs/permissions4m-annotation.jar')
		annotationProcessor files('libs/permissions4m-processor.jar')
	}

# usage doc #

* tips
	* [multi permissions request callbacks](#must_add)
	* [tips for sync multi permissions requests](#sync_request)
	* [tips for single permission request](#notice_single)
	* [tips for multi permissions requests func](#multiple_single)

* Activity
    * [single permission request](#single_activity)
    * [multi permission requests](#multiple_activity)
    * [multi sync permission requests](#sync_activity)
    * [single custom permission request](#single_custom_activity)
    * [multi custom permission requests](#multiple_custom_activity)
    * [multi sync custom permission requests](#sync_request_activity)
 
* Fragment
    * [single permission request](#single_fragment)
    * [multi permissions requests](#multiple_fragment)
    * [multi sync permissions requests](#sync_fragment)
    * [single custom permission request](#single_custom_fragment)
    * [multi custom permissions requests](#multi_custom_fragment)
    * [multi sync custom permissions requests](#sync_request_fragment)

## tips ##

<h3 id="must_add">multi permissions request callbacks</h3>

 In Activity/Fragment, add `onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)` to support permission request callbacks, codes are as follows:

	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

<h3 id="sync_request">tips for sync multi permission requests</h3>

When you are requesting multi sync custom permissions, please use this form of callback function [multi permission requests](#multiple_single), which requires that we should put the result into the same function, show by `requestCode` :

	// successful permission request callback
	@PermissionsGranted({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioGranted(int requestCode) {
        switch (requestCode) {
            case SMS_CODE:
                ToastUtil.show("successful request of msg in fragment");
                break;
            case AUDIO_CODE:
                ToastUtil.show("successful request of voice in fragment");
                break;
            default:
                break;
        }
    }

follows are being forbidden:

	// successful permission request callback
	@PermissionsGranted(SMS_CODE)
    public void smsGranted() {
        ToastUtil.show("successful request of msg in fragment");
    }

	//  successful permission request callback
	@PermissionsGranted(AUDIO_CODE)
    public void audioGranted() {
        ToastUtil.show("successful request of voice in fragment");
    }

<h3 id="notice_single">tips for single permission request</h3>

**single permission request**, no params exists in functions decorated by annotations, **just as follows**：

	@PermissionsGranted(CONTACT_CODE)
    public void contactGranted() {
        ToastUtil.show("success in reading contacts'permission");
    }

**but follows are being forbidden**:

	// notice the formal params:
    @PermissionsGranted(CONTACT_CODE)
    public void contactGranted(int requestCode) {
        ToastUtil.show("CONTACTS granted");
    }

<h3 id="multiple_single">tips for multi permissions requests func</h3>

**multi permission requests**, functions decorated by annotations contain params, **just as follows**：

	@PermissionsGranted({STORAGE_CODE, CALL_CODE})
    public void storageAndCallGranted(int requestCode) {
        switch (requestCode) {
            case STORAGE_CODE:
                ToastUtil.show("success in device reservation's authorization");
                break;
            case CALL_CODE:
                ToastUtil.show("success in device reservation's authorization");
                break;
        }
    }

**but follows are being forbidden**:

	// notice the formal params:
	@PermissionsGranted({STORAGE_CODE, CALL_CODE})
    public void storageAndCallGranted() {
    }

other way, u could use the func of [tips for single permission request](#notice_single) for every permission.

## Activity ##

<h3 id="single_activity">single permission request</h3>
	
	private static final int CONTACT_CODE = 3;

	// single permission
    mContactsButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Permissions4M.requestPermission(MainActivity.this, Manifest.permission.READ_CONTACTS,CONTACT_CODE);
        }
    });

	// register callbacks
	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

	// granted
	@PermissionsGranted(CONTACT_CODE)
    public void contactGranted() {
        ToastUtil.show("CONTACT granted");
    }

	// denied
    @PermissionsDenied(CONTACT_CODE)
    public void contactDenied() {
        ToastUtil.show("CONTACT denied");
    }

	// show rationale
    @PermissionsRationale(CONTACT_CODE)
    public void contactRationale() {
        ToastUtil.show("please open CONTACT permission");
    }

<h3 id="multiple_activity">multi permissions requests</h3>

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

	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

	@PermissionsGranted({STORAGE_CODE, CALL_CODE})
    public void storageAndCallGranted(int requestCode) {
        switch (requestCode) {
            case STORAGE_CODE:
                ToastUtil.show("STORAGE granted");
                break;
            case CALL_CODE:
                ToastUtil.show("CALL granted");
                break;
        }
    }

    @PermissionsDenied({STORAGE_CODE, CALL_CODE})
    public void storageAndCallDenied(int requestCode) {
        switch (requestCode) {
            case STORAGE_CODE:
                ToastUtil.show("STORAGE denied");
                break;
            case CALL_CODE:
                ToastUtil.show("CALL denied");
                break;
        }
    }

    @PermissionsRationale({STORAGE_CODE, CALL_CODE})
    public void storageAndCallRationale(int requestCode) {
        switch (requestCode) {
            case STORAGE_CODE:
                ToastUtil.show("please open STORAGE");
                break;
            case CALL_CODE:
                ToastUtil.show("please open CALL");
                break;
        }
    }

<h3 id="sync_activity">multi sync permission requests</h3>

1.add annotations in your activities as follows:

	@PermissionsRequestSync(
		permission = {Manifest.permission.BODY_SENSORS,
						Manifest.permission.ACCESS_FINE_LOCATION,
							Manifest.permission.READ_CALENDAR},
		value = {SENSORS_CODE,
					LOCATION_CODE,
						CALENDAR_CODE})
	public class MainActivity extends AppcompatActivity

annotations require two arraies; permission array requires the sync request authority; value array requires its corresponding result requestCode;the order of elements in the array is linked with the order of request.

2.let's begin sync permission request wit the following requestCode:

	Permissions4M.syncRequestPermissions(MainFragment.this);

the request order is refered to the order of your authority, e.g: `Manifest.permission.BODY_SENSORS` -> `Manifest.permission.ACCESS_FINE_LOCATION` -> `Manifest.permission.READ_CALENDAR`。

3.the following format is being used in the request callback function.（**split callbacks unsupported**）：

	@PermissionsGranted({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncGranted(int requestCode) {
        switch (requestCode) {
            case LOCATION_CODE:
                ToastUtil.show("LOCATION granted in activity");
                break;
            case SENSORS_CODE:
                ToastUtil.show("SENSORS granted in activity");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("CALENDAR granted in activity");
                break;
            default:
                break;
        }
    }

    @PermissionsDenied({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncDenied(int requestCode) {
        switch (requestCode) {
            case LOCATION_CODE:
                ToastUtil.show("LOCATED denied in activity");
                break;
            case SENSORS_CODE:
                ToastUtil.show("SENSOR denied in activity");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("CALENDAR denied in activity");
                break;
            default:
                break;
        }
    }

    @PermissionsRationale({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncRationale(int requestCode) {
        switch (requestCode) {
            case LOCATION_CODE:
                ToastUtil.show("please open LOCATION in activity");
                break;
            case SENSORS_CODE:
                ToastUtil.show("please open SENSOR in activity");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("please open CALENDAR in activity");
                break;
            default:
                break;
        }
    }

<h3 id="single_custom_activity">single custom permission request</h3>

	private static final int CAMERA_CODE = 4;

	mCameraButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Permissions4M.requestPermission(MainActivity.this, Manifest.permission.CAMERA, CAMERA_CODE);
        }
    });

	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

	@PermissionsGranted(CAMERA_CODE)
    public void cameraGranted() {
        ToastUtil.show("CAMERA granted");
    }

    @PermissionsDenied(CAMERA_CODE)
    public void cameraDenied() {
        ToastUtil.show("CAMERA denied");
    }

    @PermissionsCustomRationale(CAMERA_CODE)
    public void cameraCustomRationale() {
        new AlertDialog.Builder(this)
                .setMessage("CAMERA PERMISSION info：\n we need it")
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // please handle the request by yourself
                        // method 1: use the framework method
                        Permissions4M.requestPermissionOnCustomRationale(MainActivity.this, new String[]{Manifest
                                .permission.CAMERA}, CAMERA_CODE);
                        // method 2: use the follow method, same to method 1
						// ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest
						// .permission.CAMERA}, CAMERA_CODE);
                    }
                })
                .show();
    }

<h3 id="multiple_custom_activity">multi custom permissions requests</h3>

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

	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

	@PermissionsGranted({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioGranted(int requestCode) {
        switch (requestCode) {
            case SMS_CODE:
                ToastUtil.show("SMS granted");
                break;
            case AUDIO_CODE:
                ToastUtil.show("AUDIO granted");
                break;
            default:
                break;
        }
    }

    @PermissionsDenied({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioDenied(int requestCode) {
        switch (requestCode) {
            case SMS_CODE:
                ToastUtil.show("SMS denied");
                break;
            case AUDIO_CODE:
                ToastUtil.show("AUDIO denied");
                break;
            default:
                break;
        }
    }

    @PermissionsCustomRationale({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioCustomRationale(int requestCode) {
        switch (requestCode) {
            case SMS_CODE:
                new AlertDialog.Builder(this)
                        .setMessage("SMS PERMISSION request：\n we need it")
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // please handle the request by yourself
                        // method 1: use the framework method
                                Permissions4M.requestPermissionOnCustomRationale(MainActivity.this, new String[]{Manifest
                                        .permission.READ_SMS}, SMS_CODE);
                                // method 2: use the follow method, same to method 1
								// ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest
								// .permission.READ_SMS}, SMS_CODE);
                            }
                        })
                        .show();
                break;
            case AUDIO_CODE:
                new AlertDialog.Builder(this)
                        .setMessage("AUDIO PERMISSION request：\n we need it")
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // please handle the request by yourself
                        // method 1: use the framework method
                                Permissions4M.requestPermissionOnCustomRationale(MainActivity.this, new String[]{Manifest
                                        .permission.RECORD_AUDIO}, AUDIO_CODE);
                                // method 2: use the follow method, same to method 1
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

<h3 id="sync_request_activity">multi sync custom permission requests</h3>

reference:[multi custom permissions requests](#mutiple_custom_activity), codes are as follows：

	// sync request
        mOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.syncRequestPermissions(MainActivity.this);
            }
        });

	@PermissionsCustomRationale({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncCustomRationale(int requestCode) {
        switch (requestCode) {
            case LOCATION_CODE:
                new AlertDialog.Builder(this)
                        .setMessage("LOCATION PERMISSION request：\n we need it(in activity)")
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
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
                        .setMessage("SENSOR PERMISSION request：\n we need it(in activity)")
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
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
                        .setMessage("SENSOR PERMISSION request：\n we need it(in activity)")
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
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

<h3 id="single_fragment">single permission request</h3>

	private static final int CONTACT_CODE = 3;

	mContactsButton.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Permissions4M.requestPermission(MainFragment.this, Manifest.permission.READ_CONTACTS,CONTACT_CODE);
       }
    });

	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

	@PermissionsGranted(CONTACT_CODE)
    public void contactGranted() {
        ToastUtil.show("CONTACT granted in fragment");
    }

    @PermissionsDenied(CONTACT_CODE)
    public void contactDenied() {
        ToastUtil.show("CONTACT denied in fragment");
    }

    @PermissionsRationale(CONTACT_CODE)
    public void contactRationale() {
        ToastUtil.show("please open CONTACT permission in fragment");
    }

<h3 id="multiple_fragment">multi permissions requests</h3>

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

	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

	@PermissionsGranted({STORAGE_CODE, CALL_CODE})
    public void storageAndCallGranted(int requestCode) {
        switch (requestCode) {
            case STORAGE_CODE:
                ToastUtil.show("STORAGE granted in fragment");
                break;
            case CALL_CODE:
                ToastUtil.show("CALL granted in fragment");
                break;
        }
    }

    @PermissionsDenied({STORAGE_CODE, CALL_CODE})
    public void storageAndCallDenied(int requestCode) {
        switch (requestCode) {
            case STORAGE_CODE:
                ToastUtil.show("STORAGE denied in fragment");
                break;
            case CALL_CODE:
                ToastUtil.show("CALL denied in fragment");
                break;
        }
    }

    @PermissionsRationale({STORAGE_CODE, CALL_CODE})
    public void storageAndCallRationale(int requestCode) {
        switch (requestCode) {
            case STORAGE_CODE:
                ToastUtil.show("please open STORAGE permission in fragment");
                break;
            case CALL_CODE:
                ToastUtil.show("please open CALL permission in fragment");
                break;
        }
    }

<h3 id="sync_fragment">multi sync permissions requests</h3>

1.add annotations in Fragement as follows:

	@PermissionsRequestSync(
		permission = {Manifest.permission.BODY_SENSORS,
						Manifest.permission.ACCESS_FINE_LOCATION,
							Manifest.permission.READ_CALENDAR},
		value = {SENSORS_CODE,
					LOCATION_CODE,
						CALENDAR_CODE})
	public class MainFragment extends Fragment

 annotations require two arraies; permission array requires the sync request authority; value array requires its corresponding result requestCode; the order of elements in the array is linked with the order of request.

2.let's begin sync permission request using the following requestCode:

	Permissions4M.syncRequestPermissions(MainFragment.this);
the request order is refered to the order of your authority, e.g:  `Manifest.permission.BODY_SENSORS` -> `Manifest.permission.ACCESS_FINE_LOCATION` -> `Manifest.permission.READ_CALENDAR`。

3.the following format is being used in the request callback function.（**callback split unsupported**）：

	@PermissionsGranted({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncGranted(int requestCode) {
        switch (requestCode) {
            case LOCATION_CODE:
                ToastUtil.show("LOCATION granted in activity");
                break;
            case SENSORS_CODE:
                ToastUtil.show("SENSOR granted in activity");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("CALENDAR granted in activity");
                break;
            default:
                break;
        }
    }

    @PermissionsDenied({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncDenied(int requestCode) {
        switch (requestCode) {
            case LOCATION_CODE:
                ToastUtil.show("LOCATION denied in activity");
                break;
            case SENSORS_CODE:
                ToastUtil.show("SENSOR denied in activity");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("CALENDAR denied in activity");
                break;
            default:
                break;
        }
    }

    @PermissionsRationale({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncRationale(int requestCode) {
        switch (requestCode) {
            case LOCATION_CODE:
                ToastUtil.show("please open LOCATION permission in activity");
                break;
            case SENSORS_CODE:
                ToastUtil.show("please open SENSOR permission in activity");
                break;
            case CALENDAR_CODE:
                ToastUtil.show("please open CALENDAR permission in activity");
                break;
            default:
                break;
        }
    }

<h3 id="single_custom_fragment">single custom permission request</h3>

	private static final int CAMERA_CODE = 4;

	mCameraButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Permissions4M.requestPermission(MainFragment.this, Manifest.permission.CAMERA, CAMERA_CODE);
        }
    });

	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

	@PermissionsGranted(CAMERA_CODE)
    public void cameraGranted() {
        ToastUtil.show("CAMERA granted in fragment");
    }

    @PermissionsDenied(CAMERA_CODE)
    public void cameraDenied() {
        ToastUtil.show("CAMERA denied in fragment");
    }

    @PermissionsCustomRationale(CAMERA_CODE)
    public void cameraCustomRationale() {
        new AlertDialog.Builder(getActivity())
                .setMessage("CAMERA PERMISSION request：\n we need it(in fragment)")
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       Permissions4M.requestPermissionOnCustomRationale(MainFragment.this, new
                                String[]{Manifest
                                .permission.CAMERA}, CAMERA_CODE);

						// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						// MainFragment.this.requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_CODE);
						// }
                    }
                })
                .show();
    }

<h3 id="multiple_custom_fragment">multi custom permissions requests</h3>

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

	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

	@PermissionsGranted({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioGranted(int requestCode) {
        switch (requestCode) {
            case SMS_CODE:
                ToastUtil.show("SMS granted in fragment");
                break;
            case AUDIO_CODE:
                ToastUtil.show("AUDIO granted in fragment");
                break;
            default:
                break;
        }
    }

    @PermissionsDenied({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioDenied(int requestCode) {
        switch (requestCode) {
            case SMS_CODE:
                ToastUtil.show("SMS denied in fragment");
                break;
            case AUDIO_CODE:
                ToastUtil.show("AUDIO denied in fragment");
                break;
            default:
                break;
        }
    }

    @PermissionsCustomRationale({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioCustomRationale(int requestCode) {
        switch (requestCode) {
            case SMS_CODE:
                new AlertDialog.Builder(getActivity())
                        .setMessage("SMS PERMISSION request：\n we need it(in fragment)")
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Permissions4M.requestPermissionOnCustomRationale(MainFragment.this, new
                                        String[]{Manifest
                                        .permission.READ_SMS}, SMS_CODE);

								// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
								// MainFragment.this.requestPermissions(new String[]{Manifest.permission.READ_SMS}, SMS_CODE);
								// }
                            }
                        })
                        .show();
                break;
            case AUDIO_CODE:
                new AlertDialog.Builder(getActivity())
                        .setMessage("AUDIO PERMISSION request：\n we need it(in fragment)")
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Permissions4M.requestPermissionOnCustomRationale(MainFragment.this, new
                                        String[]{Manifest
                                        .permission.RECORD_AUDIO}, AUDIO_CODE);

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

<h3 id="sync_request_fragment">multi sync custom permissions requests</h3>

reference:Activity's [multi sync custom permission requests](#sync_request_activity)

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
