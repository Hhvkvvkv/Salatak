package com.batoulapps.adhan.data;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/* loaded from: classes.dex */
public class CalendarUtil {
    public static Date add(Date date, int i2, int i3) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);
        calendar.add(i3, i2);
        return calendar.getTime();
    }

    public static boolean isLeapYear(int i2) {
        return i2 % 4 == 0 && (i2 % 100 != 0 || i2 % 400 == 0);
    }

    public static Date resolveTime(DateComponents dateComponents) {
        return resolveTime(dateComponents.year, dateComponents.month, dateComponents.day);
    }

    public static Date roundedMinute(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);
        calendar.set(12, (int) (calendar.get(12) + Math.round(calendar.get(13) / 60.0d)));
        calendar.set(13, 0);
        return calendar.getTime();
    }

    private static Date resolveTime(int i2, int i3, int i4) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(i2, i3 - 1, i4, 0, 0, 0);
        return calendar.getTime();
    }
}
