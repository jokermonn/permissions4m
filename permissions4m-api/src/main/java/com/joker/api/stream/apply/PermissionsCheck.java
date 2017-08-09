package com.joker.api.stream.apply;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.IOException;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.SENSOR_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by joker on 2017/8/9.
 */

public class PermissionsCheck {
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
                    return true;
                case Manifest.permission.USE_SIP:
                    return true;
                case Manifest.permission.PROCESS_OUTGOING_CALLS:
                    return true;
                case Manifest.permission.ADD_VOICEMAIL:
                    return true;
                case Manifest.permission.READ_CALENDAR:
                    return true;
                case Manifest.permission.BODY_SENSORS:
                    return checkBodySensors(activity);
                case Manifest.permission.ACCESS_COARSE_LOCATION:
                case Manifest.permission.ACCESS_FINE_LOCATION:
                    return checkLocation(activity);
                case Manifest.permission.READ_EXTERNAL_STORAGE:
                    return checkReadStorage(activity);
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    return checkWriteStorage(activity);
                case Manifest.permission.RECORD_AUDIO:
                    return true;
                case Manifest.permission.READ_SMS:
                    return checkReadSms(activity);
                case Manifest.permission.SEND_SMS:
                case Manifest.permission.RECEIVE_WAP_PUSH:
                case Manifest.permission.RECEIVE_MMS:
                case Manifest.permission.RECEIVE_SMS:
                    return true;
                default:
                    break;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private static boolean checkReadSms(Activity activity) throws Exception {
        Uri uri = Uri.parse("content://sms/");
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        Cursor cur = activity.getContentResolver().query(uri, projection, null, null, "date desc");
        if (cur != null) {
            cur.close();
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkWriteStorage(Activity activity) throws Exception {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getPath(), "permission");
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
        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
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
        service.getDeviceId();
        return true;
    }

    private static boolean checkReadCallLog(Activity activity) throws Exception {
        activity.getContentResolver();
        Cursor cursor = activity.getContentResolver().query(Uri.parse
                        ("content://call_log/calls"), null, null,
                null, null);
        if (cursor != null) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkWriteContacts(Activity activity) throws Exception {
        ContentValues values = new ContentValues();

        Uri rawContactUri = activity.getContentResolver().insert(ContactsContract.RawContacts
                .CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, "");
        activity.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        // TODO:delete contacts
        return false;
    }

    private static boolean checkReadContacts(Activity activity) throws Exception {
        Cursor phoneCursor = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone
                        .CONTENT_URI,
                null, null, null, null);
        if (phoneCursor != null) {
            phoneCursor.close();
            return true;
        } else {
            return false;
        }
    }
}
