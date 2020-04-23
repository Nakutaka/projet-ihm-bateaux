package com.example.project.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;

@Entity
public class Date {    //2010-04-16 15:15:17
    public long time;
    public int year, month, day, hour, min, sec;

    public Date(long time, int year, int month, int day, int hour, int min, int sec) {
        this.time = time;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }

    @Ignore
    public Date(Calendar now) {
        this.time = now.getTimeInMillis();
        this.year = now.get(Calendar.YEAR);
        this.month = now.get(Calendar.MONTH) +1;
        this.day = now.get(Calendar.DAY_OF_MONTH);
        this.hour = now.get(Calendar.HOUR_OF_DAY);
        this.min = now.get(Calendar.MINUTE);
        this.sec = now.get(Calendar.SECOND);
    }

    public long getTime() {
        return time;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public int getSec() {
        return sec;
    }

    public String getHourOnly() {
        return String.format("%02d:%02d", hour, min);//Locale
    }

    public String getFullHour() {
        return String.format("%02d:%02d:%02d", hour, min, sec);//Locale
    }

    public String getDayAndMonth() {
        return String.format("%02d-%02d", month, day);//Locale
    }

    public String getDateOnly() {
        return String.format("%d-%02d-%02d", year, month, day);//Locale
    }

    public String getFullDate() {
        return String.format("%d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, min, sec);//Locale
    }
}