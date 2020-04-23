package com.example.project.data.model;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.UUID;

/*For device id

import libs
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.View;


final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

final String tmDevice, tmSerial, androidId;
tmDevice = "" + tm.getDeviceId();
tmSerial = "" + tm.getSimSerialNumber();
androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
String deviceId = deviceUuid.toString();


+ add this to your manifest:
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
 */

@Entity
public class Report {
    @NonNull
    @PrimaryKey
    public String id;
    public String device;
    public double latitude;
    public double longitude;

    @Embedded
    private Date date;

    //for Room db only
    public Report(String id, double latitude, double longitude, Date date, String device) {
        this.id = id;
        this.device = device;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }

    @Ignore
    public Report(double latitude, double longitude, Date date, String device) {
        this.id = makeId();
        this.device = device;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }

    @Ignore
    public Report(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private String makeId() {
        long time = Calendar.getInstance().getTimeInMillis();
        return UUID.randomUUID().toString()
                + "#"
                + (char) ((1.77 * time) % 256)
                + time*0.37;
    }

    public String getId() {
        return this.id;
    }

    public String getDevice() {
        return device;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getDate() {
        return date;
    }
}
