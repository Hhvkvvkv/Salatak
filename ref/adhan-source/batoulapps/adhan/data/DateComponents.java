package com.batoulapps.adhan.data;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/* loaded from: classes.dex */
public class DateComponents {
    public final int day;
    public final int month;
    public final int year;

    public DateComponents(int i2, int i3, int i4) {
        this.year = i2;
        this.month = i3;
        this.day = i4;
    }

    public static DateComponents from(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new DateComponents(calendar.get(1), calendar.get(2) + 1, calendar.get(5));
    }

    public static DateComponents fromUTC(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);
        return new DateComponents(calendar.get(1), calendar.get(2) + 1, calendar.get(5));
    }
}
