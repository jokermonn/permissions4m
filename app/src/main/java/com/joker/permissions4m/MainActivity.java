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
    private static final int CONTACT_CODE = 3;
    private Button mCallButton;
    private Button mStorageButton;
    private Button mContactsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCallButton = (Button) findViewById(R.id.btn_call);
        mStorageButton = (Button) findViewById(R.id.btn_storage);
        mContactsButton = (Button) findViewById(R.id.btn_contacts);

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
        mContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M.requestPermission(MainActivity.this, Manifest.permission.READ_CONTACTS,
                        CONTACT_CODE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionsGranted({STORAGE_CODE, CALL_CODE})
    public void storageAndCallGranted(int code) {
        switch (code) {
            case STORAGE_CODE:
                Toast.makeText(this, "设备存储权限授权成功", Toast.LENGTH_SHORT).show();
                break;
            case CALL_CODE:
                Toast.makeText(this, "通话权限授权成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @PermissionsDenied({STORAGE_CODE, CALL_CODE})
    public void storageAndCallDenied(int code) {
        switch (code) {
            case STORAGE_CODE:
                Toast.makeText(this, "设备存储权限授权失败", Toast.LENGTH_SHORT).show();
                break;
            case CALL_CODE:
                Toast.makeText(this, "通话权限授权失败", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @PermissionsRationale({STORAGE_CODE, CALL_CODE})
    public void storageAndCallRationale(int code) {
        switch (code) {
            case STORAGE_CODE:
                Toast.makeText(this, "请开启设备存储权限授权", Toast.LENGTH_SHORT).show();
                break;
            case CALL_CODE:
                Toast.makeText(this, "请开启通话权限授权", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @PermissionsGranted(CONTACT_CODE)
    public void contactGranted() {
        Toast.makeText(this, "读取联系人权限成功", Toast.LENGTH_SHORT).show();
    }

    @PermissionsDenied(CONTACT_CODE)
    public void contactDenied() {
        Toast.makeText(this, "读取联系人权限失败", Toast.LENGTH_SHORT).show();

    }

    @PermissionsRationale(CONTACT_CODE)
    public void contactRationale() {
        Toast.makeText(this, "请开启读取联系人权限", Toast.LENGTH_SHORT).show();

    }
}
