package com.joker.permissions4m;


import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.joker.annotation.PermissionsCustomRationale;
import com.joker.annotation.PermissionsDenied;
import com.joker.annotation.PermissionsGranted;
import com.joker.annotation.PermissionsRationale;
import com.joker.api.Permissions4M;
import com.joker.permissions4m.other.ToastUtil;


public class SingleFragment extends Fragment {
    public static final int CALENDAR_CODE = 7;
    public static final int SENSORS_CODE = 8;
    public static final int LOCATION_CODE = 9;
    private static final int CONTACT_CODE = 3;
    private static final int CAMERA_CODE = 4;
    private Button mContactsButton;
    private Button mCameraButton;

    public SingleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single, container, false);
        mContactsButton = (Button) view.findViewById(R.id.btn_contacts);
        mCameraButton = (Button) view.findViewById(R.id.btn_camera);

        // 单个申请
        mContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.requestPermission(SingleFragment.this, Manifest.permission.READ_CONTACTS,
                        CONTACT_CODE);
            }
        });

        // 自定义单个申请
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.requestPermission(SingleFragment.this, Manifest.permission.CAMERA,
                        CAMERA_CODE);
            }
        });

        return view;
    }

    //====================================================================
    @PermissionsGranted(CONTACT_CODE)
    public void contactGranted() {
        ToastUtil.show("读取联系人权限成功 in fragment");
    }

    @PermissionsDenied(CONTACT_CODE)
    public void contactDenied() {
        ToastUtil.show("读取联系人权限失败 in fragment");
    }

    @PermissionsRationale(CONTACT_CODE)
    public void contactRationale() {
        ToastUtil.show("请开启读取联系人权限 in fragment");
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
                        // 请自行处理申请权限，两种方法等价
                        // 方法1，使用框架已封装的方法
                        Permissions4M.requestPermissionOnCustomRationale(SingleFragment.this, new
                                String[]{Manifest
                                .permission.CAMERA}, CAMERA_CODE);
                        // 方法2，使用自身方法
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            MainFragment.this.requestPermissions(new String[]{Manifest
//                                    .permission.CAMERA}, CAMERA_CODE);
//                        }
                    }
                })
                .show();
    }
}
