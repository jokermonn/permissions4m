package com.joker.permissions4m;


import android.Manifest;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.joker.annotation.PermissionsCustomRationale;
import com.joker.annotation.PermissionsDenied;
import com.joker.annotation.PermissionsGranted;
import com.joker.annotation.PermissionsRationale;
import com.joker.annotation.PermissionsRequestSync;
import com.joker.api.Permissions4M;
import com.joker.permissions4m.other.ToastUtil;

import static com.joker.permissions4m.MainFragment.CALENDAR_CODE;
import static com.joker.permissions4m.MainFragment.LOCATION_CODE;
import static com.joker.permissions4m.MainFragment.SENSORS_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
@PermissionsRequestSync(permission = {Manifest.permission.BODY_SENSORS, Manifest.permission
        .ACCESS_FINE_LOCATION, Manifest.permission.READ_CALENDAR},
        value = {SENSORS_CODE, LOCATION_CODE, CALENDAR_CODE})
public class MainFragment extends Fragment {
    public static final int CALENDAR_CODE = 700;
    public static final int SENSORS_CODE = 800;
    public static final int LOCATION_CODE = 900;
    private static final int STORAGE_CODE = 100;
    private static final int READ_CALL_LOG = 200;
    private static final int SMS_CODE = 500;
    private static final int AUDIO_CODE = 600;
    private Button mCallButton;
    private Button mStorageButton;
    private Button mSmsButton;
    private Button mAudioButton;
    private Button mOneButton;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mCallButton = (Button) view.findViewById(R.id.btn_call);
        mStorageButton = (Button) view.findViewById(R.id.btn_storage);
        mSmsButton = (Button) view.findViewById(R.id.btn_sms);
        mAudioButton = (Button) view.findViewById(R.id.btn_audio);
        mOneButton = (Button) view.findViewById(R.id.btn_one);

        // 多个申请
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(MainFragment.this)
                        .requestPermission(Manifest.permission.READ_CALL_LOG)
                        .requestForce(true)
                        .requestCode(READ_CALL_LOG)
                        .request();
            }
        });
        mStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(MainFragment.this)
                        .requestForce(true)
                        .requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .requestCode(STORAGE_CODE)
                        .request();
            }
        });

        // 自定义多个申请
        mSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(MainFragment.this)
                        .requestPermission(Manifest.permission.READ_SMS)
                        .requestForce(true)
                        .requestCode(SMS_CODE)
                        .request();
            }
        });
        mAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(MainFragment.this)
                        .requestPermission(Manifest.permission.RECORD_AUDIO)
                        .requestForce(true)
                        .requestCode(AUDIO_CODE)
                        .request();
            }
        });

        // 同步申请
        mOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(MainFragment.this)
                        .requestSync();
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainFragment.this, requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //====================================================================
    @PermissionsGranted({STORAGE_CODE, READ_CALL_LOG, LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void storageAndCallGranted(int code) {
        switch (code) {
            case STORAGE_CODE:
                ToastUtil.show("设备存储权限授权成功 in fragment with annotation");
                break;
            case READ_CALL_LOG:
                ToastUtil.show("读取通话记录权限授权成功 in fragment with annotation");
                break;
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

    @PermissionsDenied({STORAGE_CODE, READ_CALL_LOG, LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void storageAndCallDenied(int code) {
        switch (code) {
            case STORAGE_CODE:
                ToastUtil.show("设备存储权限授权失败 in fragment with annotation");
                break;
            case READ_CALL_LOG:
                ToastUtil.show("读取通话记录权限授权失败 in fragment with annotation");
                break;
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

    @PermissionsRationale({STORAGE_CODE, READ_CALL_LOG, LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void storageAndCallRationale(int code) {
        switch (code) {
            case STORAGE_CODE:
                ToastUtil.show("请开启设备存储权限授权 in fragment with annotation");
                break;
            case READ_CALL_LOG:
                ToastUtil.show("请开启读取通话记录权限授权 in fragment with annotation");
                break;
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


    //====================================================================
    @PermissionsGranted({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioGranted(int code) {
        switch (code) {
            case SMS_CODE:
                ToastUtil.show("短信权限申请成功 in fragment with annotation");
                break;
            case AUDIO_CODE:
                ToastUtil.show("录音权限申请成功 in fragment with annotation");
                break;
            default:
                break;
        }
    }

    @PermissionsDenied({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioDenied(int code) {
        switch (code) {
            case SMS_CODE:
                ToastUtil.show("短信权限申请失败 in fragment with annotation");
                break;
            case AUDIO_CODE:
                ToastUtil.show("录音权限申请失败 in fragment with annotation");
                break;
            default:
                break;
        }
    }

    @PermissionsCustomRationale({SMS_CODE, AUDIO_CODE})
    public void smsAndAudioCustomRationale(int code) {
        switch (code) {
            case SMS_CODE:
                new AlertDialog.Builder(getActivity())
                        .setMessage("短信权限申请：\n我们需要您开启短信权限(in fragment with annotation)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 请自行处理申请权限，两种方法等价
                                // 方法1，使用框架已封装的方法
                                Permissions4M.get(MainFragment.this)
                                        .requestForce(true)
                                        .requestPermission(Manifest.permission.READ_SMS)
                                        .requestCode(SMS_CODE)
                                        .request();
                                // 方法2，使用自身方法
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                    MainFragment.this.requestPermissions(new String[]{Manifest
//                                            .permission.READ_SMS}, SMS_CODE);
//                                }
                            }
                        })
                        .show();
                break;
            case AUDIO_CODE:
                new AlertDialog.Builder(getActivity())
                        .setMessage("录音权限申请：\n我们需要您开启录音权限(in fragment with annotation)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 请自行处理申请权限，两种方法等价
                                // 方法1，使用框架已封装的方法
                                Permissions4M.get(MainFragment.this)
                                        .requestForce(true)
                                        .requestPermission(Manifest.permission.RECORD_AUDIO)
                                        .requestCode(AUDIO_CODE)
                                        .request();
                                // 方法2，使用自身方法
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                    MainFragment.this.requestPermissions(new String[]{Manifest
//                                            .permission.RECORD_AUDIO}, AUDIO_CODE);
//                                }
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }
}
