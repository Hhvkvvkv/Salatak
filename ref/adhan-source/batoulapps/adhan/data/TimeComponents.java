package com.batoulapps.adhan.data;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/* loaded from: classes.dex */
public class TimeComponents {
    public final int hours;
    public final int minutes;
    public final int seconds;

    private TimeComponents(int i2, int i3, int i4) {
        this.hours = i2;
        this.minutes = i3;
        this.seconds = i4;
    }

    public static TimeComponents fromDouble(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            return null;
        }
        double floor = Math.floor(d);
        double floor2 = Math.floor((d - floor) * 60.0d);
        return new TimeComponents((int) floor, (int) floor2, (int) Math.floor((d - ((floor2 / 60.0d) + floor)) * 60.0d * 60.0d));
    }

    public Date dateComponents(DateComponents dateComponents) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(1, dateComponents.year);
        calendar.set(2, dateComponents.month - 1);
        calendar.set(5, dateComponents.day);
        calendar.set(11, 0);
        calendar.set(12, this.minutes);
        calendar.set(13, this.seconds);
        calendar.add(11, this.hours);
        return calendar.getTime();
    }
}
