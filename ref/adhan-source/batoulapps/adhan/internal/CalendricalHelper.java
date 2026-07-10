package com.batoulapps.adhan.internal;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/* loaded from: classes.dex */
class CalendricalHelper {
    public static double julianCentury(double d) {
        return (d - 2451545.0d) / 36525.0d;
    }

    public static double julianDay(int i2, int i3, int i4) {
        return julianDay(i2, i3, i4, 0.0d);
    }

    public static double julianDay(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);
        return julianDay(calendar.get(1), calendar.get(2) + 1, calendar.get(5), (calendar.get(12) / 60.0d) + calendar.get(11));
    }

    public static double julianDay(int i2, int i3, int i4, double d) {
        if (i3 <= 2) {
            i2--;
        }
        if (i3 <= 2) {
            i3 += 12;
        }
        double d2 = (d / 24.0d) + i4;
        int i5 = i2 / 100;
        return (((((int) ((i2 + 4716) * 365.25d)) + ((int) ((i3 + 1) * 30.6001d))) + d2) + ((i5 / 4) + (2 - i5))) - 1524.5d;
    }
}
