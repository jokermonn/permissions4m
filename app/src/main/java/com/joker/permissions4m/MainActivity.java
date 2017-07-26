package com.joker.permissions4m;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.joker.annotation.PermissionsDenied;
import com.joker.annotation.PermissionsGranted;
import com.joker.annotation.PermissionsRationale;
import com.joker.api.Permissions4M;

public class MainActivity extends AppCompatActivity {
    private static final int STORAGE_CODE = 1;
    private static final int CALL_CODE = 2;
    private Button mTestButton;
    private Button mCallButton;
    private Button mStorageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCallButton = (Button) findViewById(R.id.btn_call);
        mStorageButton = (Button) findViewById(R.id.btn_storage);

        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.requestPermission(MainActivity.this, Manifest.permission.CALL_PHONE,
                        CALL_CODE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionsGranted(STORAGE_CODE)
    public void storageGranted() {
        Toast.makeText(this, "设备存储权限授权成功", Toast.LENGTH_SHORT).show();
    }

    @PermissionsDenied(STORAGE_CODE)
    public void storageDenied() {
        Toast.makeText(this, "设备存储权限授权失败", Toast.LENGTH_SHORT).show();
    }

    @PermissionsRationale(STORAGE_CODE)
    public void storageRationale() {
        Toast.makeText(this, "请开启设备存储权限哦", Toast.LENGTH_SHORT).show();
    }

    @PermissionsGranted(CALL_CODE)
    public void callGranted() {
        Toast.makeText(this, "通话权限授权成功", Toast.LENGTH_SHORT).show();
    }

    @PermissionsDenied(CALL_CODE)
    public void callDenied() {
        Toast.makeText(this, "通话权限授权失败", Toast.LENGTH_SHORT).show();
    }

    @PermissionsRationale(CALL_CODE)
    public void callRationale() {
        Toast.makeText(this, "请开启通话权限哦", Toast.LENGTH_SHORT).show();
    }
}
