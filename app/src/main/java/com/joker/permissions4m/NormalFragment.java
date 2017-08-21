package com.joker.permissions4m;


import android.Manifest;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.joker.annotation.PermissionsNonRationale;
import com.joker.annotation.PermissionsRationale;
import com.joker.annotation.PermissionsRequestSync;
import com.joker.api.Permissions4M;
import com.joker.api.wrapper.ListenerWrapper;
import com.joker.api.wrapper.Wrapper;
import com.joker.permissions4m.other.ToastUtil;

import static com.joker.permissions4m.NormalFragment.CALENDAR_CODE;
import static com.joker.permissions4m.NormalFragment.LOCATION_CODE;
import static com.joker.permissions4m.NormalFragment.SENSORS_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
@PermissionsRequestSync(permission = {Manifest.permission.BODY_SENSORS, Manifest.permission
        .ACCESS_FINE_LOCATION, Manifest.permission.READ_CALENDAR},
        value = {SENSORS_CODE, LOCATION_CODE, CALENDAR_CODE})
public class NormalFragment extends Fragment {
    public static final int CALENDAR_CODE = 700;
    public static final int SENSORS_CODE = 800;
    public static final int LOCATION_CODE = 900;
    private static final int READ_CALENDAR_CODE = 200;
    private static final int SMS_CODE = 500;
    private static final int AUDIO_CODE = 600;
    private static final int STORAGE_CODE = 1000;
    private Button mCalendar;
    private Button mSmsButton;
    private Button mOneButton;
    private Button mStorageButton;

    public NormalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_normal, container, false);
        mSmsButton = (Button) view.findViewById(R.id.btn_sms);
        mOneButton = (Button) view.findViewById(R.id.btn_one);
        mStorageButton = (Button) view.findViewById(R.id.btn_storage);
        mCalendar = (Button) view.findViewById(R.id.btn_calendar);

        // 读取日历申请
        mCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(NormalFragment.this)
                        .requestPermission(Manifest.permission.READ_CALENDAR)
                        .requestForce(true)
                        .requestPageType(Permissions4M.PageType.MANAGER_PAGE)
                        .requestCode(READ_CALENDAR_CODE)
                        .request();
            }
        });

        // 短信申请
        mSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(NormalFragment.this)
                        .requestPermission(Manifest.permission.READ_SMS)
                        .requestForce(true)
                        .requestPageType(Permissions4M.PageType.ANDROID_SETTING_PAGE)
                        .requestCode(SMS_CODE)
                        .request();
            }
        });

        // 同步申请
        mOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(NormalFragment.this)
                        .requestSync();
            }
        });

        mStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(NormalFragment.this)
                        .requestForce(true)
                        .requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .requestCode(STORAGE_CODE)
                        .requestCallback(new ListenerWrapper.PermissionRequestListener() {
                            @Override
                            public void permissionGranted() {
                                ToastUtil.show("读取存储卡权限成功 in activity with listener");
                            }

                            @Override
                            public void permissionDenied() {
                                ToastUtil.show("读取存储卡权失败 in activity with listener");
                            }

                            @Override
                            public void permissionRationale() {
                                ToastUtil.show("请打开读取存储卡权限 in activity with listener");
                            }
                        })
                        .requestPageType(Permissions4M.PageType.MANAGER_PAGE)
                        .requestPage(new Wrapper.PermissionPageListener() {
                            @Override
                            public void pageIntent(final Intent intent) {
                                new AlertDialog.Builder(getActivity())
                                        .setMessage("读取存储卡权限申请：\n我们需要您开启读取存储卡权限(in activity with listener)")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        })
                        .request();
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(NormalFragment.this, requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //====================================================================
    @PermissionsGranted({READ_CALENDAR_CODE, LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void storageAndCallGranted(int code) {
        switch (code) {
            case READ_CALENDAR_CODE:
                ToastUtil.show("读取日历权限授权成功 in fragment with annotation");
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

    @PermissionsDenied({READ_CALENDAR_CODE, LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void storageAndCallDenied(int code) {
        switch (code) {
            case READ_CALENDAR_CODE:
                ToastUtil.show("读取日历权限授权失败 in fragment with annotation");
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

    @PermissionsRationale({READ_CALENDAR_CODE, LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void storageAndCallRationale(int code) {
        switch (code) {
            case READ_CALENDAR_CODE:
                ToastUtil.show("请开启读取日历权限授权 in fragment with annotation");
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
                                Permissions4M.get(NormalFragment.this)
                                        .requestOnRationale()
                                        .requestPermission(Manifest.permission.READ_SMS)
                                        .requestCode(SMS_CODE)
                                        .request();
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
                                Permissions4M.get(NormalFragment.this)
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
    }

    @PermissionsNonRationale({SMS_CODE, READ_CALENDAR_CODE})
    public void nonRationale(int code, final Intent intent) {
        switch (code) {
            case SMS_CODE:
                new AlertDialog.Builder(getActivity())
                        .setMessage("短信权限申请：\n我们需要您开启短信权限(in fragment with annotation)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case READ_CALENDAR_CODE:
                new AlertDialog.Builder(getActivity())
                        .setMessage("读取日历权限申请：\n我们需要您开启读取日历权限(in fragment with annotation)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
    }
}
