package com.joker.permissions4m;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.joker.annotation.PermissionsCustomRationale;
import com.joker.annotation.PermissionsDenied;
import com.joker.annotation.PermissionsGranted;
import com.joker.annotation.PermissionsRationale;
import com.joker.api.Permissions4M;
import com.joker.permissions4m.other.ToastUtil;

public class MainActivity extends AppCompatActivity {
    private static final int STORAGE_CODE = 1;
    private static final int CALL_CODE = 2;
    private static final int CONTACT_CODE = 3;
    private static final int CAMERA_CODE = 4;
    private static final int SMS_CODE = 5;
    private static final int AUDIO_CODE = 6;
    private Button mCallButton;
    private Button mStorageButton;
    private Button mContactsButton;
    private Button mCameraButton;
    private Button mSmsButton;
    private Button mAudioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCallButton = (Button) findViewById(R.id.btn_call);
        mContactsButton = (Button) findViewById(R.id.btn_contacts);
        mStorageButton = (Button) findViewById(R.id.btn_storage);
        mCameraButton = (Button) findViewById(R.id.btn_camera);
        mSmsButton = (Button) findViewById(R.id.btn_sms);
        mAudioButton = (Button) findViewById(R.id.btn_audio);

        // 单个申请
        mContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.requestPermission(MainActivity.this, Manifest.permission.READ_CONTACTS,
                        CONTACT_CODE);
            }
        });

        // 多个申请
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.requestPermission(MainActivity.this, Manifest.permission.CALL_PHONE,
                        CALL_CODE);
            }
        });
        mStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.requestPermission(MainActivity.this, Manifest.permission
                                .WRITE_EXTERNAL_STORAGE,
                        STORAGE_CODE);
            }
        });

        // 自定义单个申请
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.requestPermission(MainActivity.this, Manifest.permission.CAMERA, CAMERA_CODE);
            }
        });

        // 自定义多个申请
        mSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.requestPermission(MainActivity.this, Manifest.permission.READ_SMS, SMS_CODE);
            }
        });
        mAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.requestPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO,
                        AUDIO_CODE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //====================================================================
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

    //====================================================================
    @PermissionsGranted(CONTACT_CODE)
    public void contactGranted() {
        ToastUtil.show("读取联系人权限成功");
    }

    @PermissionsDenied(CONTACT_CODE)
    public void contactDenied() {
        ToastUtil.show("读取联系人权限失败");
    }

    @PermissionsRationale(CONTACT_CODE)
    public void contactRationale() {
        ToastUtil.show("请开启读取联系人权限");
    }

    //====================================================================
    @PermissionsGranted(CAMERA_CODE)
    public void cameraGranted() {
        ToastUtil.show("相机权限授权成功");
    }

    @PermissionsDenied(CAMERA_CODE)
    public void cameraDenied() {
        ToastUtil.show("相机权限授权失败");
    }

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

    //====================================================================
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
}
