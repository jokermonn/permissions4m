package com.joker.api.apply;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.joker.api.apply.util.AudioRecordManager;
import com.joker.api.support.PermissionsPageManager;

import java.io.File;
import java.io.IOException;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.SENSOR_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by joker on 2017/8/9.
 */

public class PermissionsChecker {
    private static final String TAG = "permissions4m";
    private static final String TAG_NUMBER = "110";

    /**
     * ensure whether permission granted
     *
     * @param activity
     * @param permission
     * @return true if granted else denied
     */
    public static boolean isPermissionGranted(Activity activity, String permission) {
        try {
            switch (permission) {
                case Manifest.permission.READ_CONTACTS:
                    return checkReadContacts(activity);
                case Manifest.permission.WRITE_CONTACTS:
                    return checkWriteContacts(activity);
                case Manifest.permission.GET_ACCOUNTS:
                    return true;

                case Manifest.permission.READ_CALL_LOG:
                    return checkReadCallLog(activity);
                case Manifest.permission.READ_PHONE_STATE:
                    return checkReadPhoneState(activity);
                case Manifest.permission.CALL_PHONE:
                    return true;
                case Manifest.permission.WRITE_CALL_LOG:
                    return checkWriteCallLog(activity);
                case Manifest.permission.USE_SIP:
                    return true;
                // can not apply
                case Manifest.permission.PROCESS_OUTGOING_CALLS:
                    return true;
                case Manifest.permission.ADD_VOICEMAIL:
                    return true;

                case Manifest.permission.READ_CALENDAR:
                    return checkReadCalendar(activity);
                case Manifest.permission.WRITE_CALENDAR:
                    return true;

                case Manifest.permission.BODY_SENSORS:
                    return checkBodySensors(activity);

                case Manifest.permission.CAMERA:
                    return true;

                case Manifest.permission.ACCESS_COARSE_LOCATION:
                case Manifest.permission.ACCESS_FINE_LOCATION:
                    return checkLocation(activity);

                case Manifest.permission.READ_EXTERNAL_STORAGE:
                    return checkReadStorage(activity);
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    return checkWriteStorage(activity);

                case Manifest.permission.RECORD_AUDIO:
                    return checkRecordAudio(activity);

                case Manifest.permission.READ_SMS:
                    return checkReadSms(activity);
                case Manifest.permission.SEND_SMS:
                case Manifest.permission.RECEIVE_WAP_PUSH:
                case Manifest.permission.RECEIVE_MMS:
                case Manifest.permission.RECEIVE_SMS:
                    return true;
                default:
                    return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "throwing exception in PermissionChecker:  ", e);
            return false;
        }
    }

    private static boolean checkRecordAudio(Activity activity) {
        try {
            AudioRecordManager.getInstance().startRecord(activity.getCacheDir().getPath() + TAG +
                    ".3gp");
            AudioRecordManager.getInstance().stopRecord();
            AudioRecordManager.getInstance().deleteFile();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean checkReadCalendar(Activity activity) {
        Cursor cursor = activity.getContentResolver().query(Uri.parse("content://com" +
                ".android.calendar/calendars"), null, null, null, null);
        if (cursor != null) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkWriteCallLog(Activity activity) {
        ContentResolver contentResolver = activity.getContentResolver();
        ContentValues content = new ContentValues();
        content.put(CallLog.Calls.TYPE, CallLog.Calls.INCOMING_TYPE);
        content.put(CallLog.Calls.NUMBER, "13333333");
        content.put(CallLog.Calls.DATE, 20140808);
        content.put(CallLog.Calls.NEW, "0");
        contentResolver.insert(Uri.parse("content://call_log/calls"), content);

        contentResolver.delete(Uri.parse("content://call_log/calls"), "number = ?", new
                String[]{"13333333"});

        return true;
    }

    private static boolean checkReadSms(Activity activity) throws Exception {
        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            if (PermissionsPageManager.isXiaoMi()) {
                if (canNotGetContactsInfo(cursor)) {
                    return false;
                }
            }
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkWriteStorage(Activity activity) throws Exception {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getPath(), TAG);
        if (!file.exists()) {
            boolean newFile;
            try {
                newFile = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return newFile;
        } else {
            return file.delete();
        }
    }

    private static boolean checkReadStorage(Activity activity) throws Exception {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getPath());
        File[] files = file.listFiles();
        return files != null;
    }

    private static boolean checkLocation(Activity activity) throws Exception {
        LocationManager locationManager = (LocationManager) activity.getSystemService
                (LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        // fuck XIAOMI!
        if (PermissionsPageManager.isXiaoMi()) {
            double latitude = location.getLatitude();
        }
        return true;
    }

    private static boolean checkBodySensors(Activity activity) throws Exception {
        SensorManager sensorManager = (SensorManager) activity.getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor((Sensor.TYPE_ACCELEROMETER));
        SensorEventListener listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.unregisterListener(listener, sensor);

        return true;
    }

    private static boolean checkReadPhoneState(Activity activity) throws Exception {
        TelephonyManager service = (TelephonyManager) activity.getSystemService
                (TELEPHONY_SERVICE);
        return !TextUtils.isEmpty(service.getDeviceId());
    }

    private static boolean checkReadCallLog(Activity activity) throws Exception {
        Cursor cursor = activity.getContentResolver().query(Uri.parse
                        ("content://call_log/calls"), null, null,
                null, null);
        if (cursor != null) {
            if (PermissionsPageManager.isXiaoMi()) {
                if (canNotGetContactsInfo(cursor)) {
                    return false;
                }
            }
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkWriteContacts(Activity activity) throws Exception {
        if (checkReadContacts(activity)) {
            // write some info
            ContentValues values = new ContentValues();
            ContentResolver contentResolver = activity.getContentResolver();
            Uri rawContactUri = contentResolver.insert(ContactsContract.RawContacts
                    .CONTENT_URI, values);
            long rawContactId = ContentUris.parseId(rawContactUri);
            values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds
                    .StructuredName.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, TAG);
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, TAG_NUMBER);
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, values);

            // delete info
            Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
            ContentResolver resolver = activity.getContentResolver();
            Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Contacts.Data._ID},
                    "display_name=?", new String[]{TAG}, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int id = cursor.getInt(0);
                    resolver.delete(uri, "display_name=?", new String[]{TAG});
                    uri = Uri.parse("content://com.android.contacts/data");
                    resolver.delete(uri, "raw_contact_id=?", new String[]{id + ""});
                }
                cursor.close();
            }
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkReadContacts(Activity activity) throws Exception {
        Cursor cursor = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone
                .CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            if (PermissionsPageManager.isXiaoMi()) {
                if (canNotGetContactsInfo(cursor)) {
                    cursor.close();
                    return false;
                }
            }
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    /**
     * in XIAOMI
     * 1.denied {@link android.Manifest.permission#READ_CONTACTS} permission
     * ---->cursor.getCount == 0
     * 2.granted {@link android.Manifest.permission#READ_CONTACTS} permission
     * ---->cursor.getCount return real count in contacts
     *
     * so when there are no user or permission denied, it will return 0
     * @param cursor
     * @return true if can not get info
     */
    private static boolean canNotGetContactsInfo(Cursor cursor) {
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                return TextUtils.isEmpty(cursor.getString(numberIndex));
            }
            return false;
        } else {
            return true;
        }
    }
}
