package com.joker.permissions4m;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class SingleActivity extends AppCompatActivity {
    private static final int LOCATION_CODE = 30;
    private static final int CAMERA_CODE = 40;
    private Button mLocationButton;
    private Button mCameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        mLocationButton = (Button) findViewById(R.id.btn_location);
        mCameraButton = (Button) findViewById(R.id.btn_camera);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new SingleFragment())
                .commit();

        // 单个申请
        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(SingleActivity.this)
                        .requestForce(true)
                        .requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .requestCode(LOCATION_CODE)
                        .request();
            }
        });

        // 自定义单个申请
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.get(SingleActivity.this)
                        .requestPermission(Manifest.permission.CAMERA)
                        .requestForce(true)
                        .requestCode(CAMERA_CODE)
                        .request();
            }
        });
    }

    //====================================================================
    @PermissionsGranted(LOCATION_CODE)
    public void contactGranted() {
        ToastUtil.show("读取地理位置权限成功 in activity with annotation");
    }

    @PermissionsDenied(LOCATION_CODE)
    public void contactDenied() {
        ToastUtil.show("读取地理位置权限失败 in activity with annotation");
    }

    @PermissionsRationale(LOCATION_CODE)
    public void contactRationale() {
        ToastUtil.show("请开启读取地理位置权限 in activity with annotation");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(SingleActivity.this, requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //====================================================================
    @PermissionsGranted(CAMERA_CODE)
    public void cameraGranted() {
        ToastUtil.show("相机权限授权成功 in activity with annotation");
    }

    @PermissionsDenied(CAMERA_CODE)
    public void cameraDenied() {
        ToastUtil.show("相机权限授权失败 in activity with annotation");
    }

    @PermissionsCustomRationale(CAMERA_CODE)
    public void cameraCustomRationale() {
        new AlertDialog.Builder(this)
                .setMessage("相机权限申请：\n我们需要您开启相机信息权限(in activity with annotation)")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 请自行处理申请权限，两者方法等价
                        // 方法1.使用框架封装方法
                        Permissions4M.get(SingleActivity.this)
                                .requestPermission(Manifest.permission.CAMERA)
                                .requestCode(CAMERA_CODE)
                                .request();
                        // 方法2.使用自身方法
//                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest
//                                .permission.CAMERA}, CAMERA_CODE);
                    }
                })
                .show();
    }
}
