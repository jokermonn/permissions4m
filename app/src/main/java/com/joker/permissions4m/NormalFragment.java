package com.joker.permissions4m;


import android.Manifest;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.joker.annotation.PermissionsCustomRationale;
import com.joker.annotation.PermissionsDenied;
import com.joker.annotation.PermissionsGranted;
import com.joker.annotation.PermissionsNonRationale;
import com.joker.api.Permissions4M;
import com.joker.api.wrapper.Wrapper;
import com.joker.permissions4m.other.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class NormalFragment extends Fragment {
    private static final int SENSORS_CODE = 700;
    private static final int LOCATION_CODE = 800;
    private static final int CALENDAR_CODE = 900;
    private static final int SMS_CODE = 500;
    private static final int AUDIO_CODE = 600;
    private static final int PHONE_STATE_CODE = 1000;
    private Button mSmsButton;
    private Button mOneButton;
    private Button mStateButton;

    public NormalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_normal, container, false);
        mSmsButton = (Button) view.findViewById(R.id.btn_sms);
        mOneButton = (Button) view.findViewById(R.id.btn_one);
        mStateButton = (Button) view.findViewById(R.id.btn_state);

        // 短信申请
        mSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(NormalFragment.this)
                        .requestPermissions(Manifest.permission.READ_SMS)
                        .requestCodes(SMS_CODE)
                        .requestPageType(Permissions4M.PageType.ANDROID_SETTING_PAGE)
                        .request();
            }
        });

        // 同步申请
        mOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(NormalFragment.this)
                        .requestPermissions(Manifest.permission.BODY_SENSORS, Manifest.permission
                                .ACCESS_FINE_LOCATION, Manifest.permission.READ_CALENDAR)
                        .requestCodes(SENSORS_CODE, LOCATION_CODE, CALENDAR_CODE)
                        .requestListener(new Wrapper.PermissionRequestListener() {
                            @Override
                            public void permissionGranted(int code) {
                                switch (code) {
                                    case LOCATION_CODE:
                                        ToastUtil.show("地理位置权限授权成功 in fragment with annotation");
                                        Log.e("TAG", "permissionGranted: 地理位置权限授权成功 ");
                                        break;
                                    case SENSORS_CODE:
                                        ToastUtil.show("传感器权限授权成功 in fragment with annotation");
                                        Log.e("TAG", "permissionGranted: 传感器权限授权成功 ");
                                        break;
                                    case CALENDAR_CODE:
                                        ToastUtil.show("读取日历权限授权成功 in fragment with annotation");
                                        Log.e("TAG", "permissionGranted: 读取日历权限授权成功 ");
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
                                        Log.e("TAG", "permissionDenied: 地理位置权限授权失败 ");
                                        break;
                                    case SENSORS_CODE:
                                        ToastUtil.show("传感器权限授权失败 in fragment with annotation");
                                        Log.e("TAG", "permissionDenied: 传感器权限授权失败 ");
                                        break;
                                    case CALENDAR_CODE:
                                        ToastUtil.show("读取日历权限授权失败 in fragment with annotation");
                                        Log.e("TAG", "permissionDenied: 读取日历权限授权失败 ");
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
                                        Log.e("TAG", "permissionRationale: 请开启地理位置权限 ");
                                        break;
                                    case SENSORS_CODE:
                                        ToastUtil.show("请开启传感器权限 in fragment with annotation");
                                        Log.e("TAG", "permissionRationale: 请开启传感器权限 ");
                                        break;
                                    case CALENDAR_CODE:
                                        ToastUtil.show("请开启读取日历权限 in fragment with annotation");
                                        Log.e("TAG", "permissionRationale: 请开启读取日历权限 ");
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
            }
        });

        mStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(NormalFragment.this)
                        .requestPermissions(Manifest.permission.READ_PHONE_STATE)
                        .requestCodes(PHONE_STATE_CODE)
                        .requestListener(new Wrapper.PermissionRequestListener() {
                            @Override
                            public void permissionGranted(int code) {
                                ToastUtil.show("读取手机状态权限成功 in activity with listener");
                            }

                            @Override
                            public void permissionDenied(int code) {
                                ToastUtil.show("读取手机状态权失败 in activity with listener");
                            }

                            @Override
                            public void permissionRationale(int code) {
                                ToastUtil.show("请打开读取手机状态权限 in activity with listener");
                            }
                        })
                        .requestPageType(Permissions4M.PageType.MANAGER_PAGE)
                        .requestPage(new Wrapper.PermissionPageListener() {
                            @Override
                            public void pageIntent(final Intent intent) {
                                new AlertDialog.Builder(getActivity())
                                        .setMessage("傻逼用户，我们需要您开启读取手机状态权限：\n请点击前往设置页面\n(in activity with " +
                                                "listener)")
                                        .setPositiveButton("前往设置页面", new DialogInterface.OnClickListener() {
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
                        .requestCustomRationaleListener(new Wrapper.PermissionCustomRationaleListener() {
                            @Override
                            public void permissionCustomRationale(int code) {
                                new AlertDialog.Builder(getActivity())
                                        .setMessage("手机状态权限申请：\n我们需要您开启手机状态权限(in fragment with annotation)")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Permissions4M.get(NormalFragment.this)
                                                        .requestOnRationale()
                                                        .requestPermissions(Manifest.permission
                                                                .READ_PHONE_STATE)
                                                        .requestCodes(PHONE_STATE_CODE)
                                                        .request();
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
                                        .requestPermissions(Manifest.permission.READ_SMS)
                                        .requestCodes(SMS_CODE)
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
    }

    @PermissionsNonRationale({SMS_CODE})
    public void nonRationale(int code, final Intent intent) {
        switch (code) {
            case SMS_CODE:
                new AlertDialog.Builder(getActivity())
                        .setMessage("傻逼用户，我们需要您开启读取短信权限申请：\n请点击前往设置页面\n(in fragment with annotation)")
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
