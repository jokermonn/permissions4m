package com.joker.permissions4m;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.joker.api.Permissions4M;
import com.joker.permissions4m.other.ToastUtil;

// support Fragment
public class SupportFragment extends Fragment {
    private static final int LOCATION_CODE = 13;
    private static final int CAMERA_CODE = 14;
    private Button mLocationButton;
    private Button mCameraButton;

    public SupportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        mLocationButton = (Button) view.findViewById(R.id.btn_location);
        mCameraButton = (Button) view.findViewById(R.id.btn_camera);

        // 地理位置申请
        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(SupportFragment.this)
                        .requestUnderM(true)
                        .requestPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                        .requestCodes(LOCATION_CODE)
                        .requestPageType(Permissions4M.PageType.MANAGER_PAGE)
                        .request();
            }
        });

        // 相机申请
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(SupportFragment.this)
                        .requestPermissions(Manifest.permission.CAMERA)
                        .requestCodes(CAMERA_CODE)
                        .requestPageType(Permissions4M.PageType.MANAGER_PAGE)
                        .request();
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(SupportFragment.this, requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //====================================================================
    @PermissionsGranted(LOCATION_CODE)
    public void contactGranted() {
        ToastUtil.show("读取地理位置权限成功 in fragment");
    }

    @PermissionsDenied(LOCATION_CODE)
    public void contactDenied() {
        ToastUtil.show("读取地理位置权限失败 in fragment");
    }

    @PermissionsRationale(LOCATION_CODE)
    public void contactRationale() {
        ToastUtil.show("请开启读取地理位置权限 in fragment");
    }

    //====================================================================
    @PermissionsGranted(CAMERA_CODE)
    public void cameraGranted() {
        ToastUtil.show("相机权限授权成功 in fragment");
    }

    @PermissionsDenied(CAMERA_CODE)
    public void cameraDenied() {
        ToastUtil.show("相机权限授权失败 in fragment");
    }

    @PermissionsCustomRationale(CAMERA_CODE)
    public void cameraCustomRationale() {
        new AlertDialog.Builder(getActivity())
                .setMessage("相机权限申请：\n我们需要您开启相机信息权限(in fragment)")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Permissions4M.get(SupportFragment.this)
                                .requestOnRationale()
                                .requestPermissions(Manifest.permission.CAMERA)
                                .requestCodes(CAMERA_CODE)
                                .request();
                    }
                })
                .show();
    }

    @PermissionsNonRationale({LOCATION_CODE, CAMERA_CODE})
    public void non(int code, final Intent intent) {
        switch (code) {
            case LOCATION_CODE:
                new AlertDialog.Builder(getActivity())
                        .setMessage("读取地理位置权限申请：\n我们需要您开启读取地理位置权限(in fragment with annotation)")
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
            case CAMERA_CODE:
                new AlertDialog.Builder(getActivity())
                        .setMessage("读取相机权限申请：\n我们需要您开启读取相机权限(in fragment with annotation)")
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
