package com.batoulapps.adhan;

/* loaded from: classes.dex */
public class PrayerAdjustments {
    public int asr;
    public int dhuhr;
    public int fajr;
    public int isha;
    public int maghrib;
    public int sunrise;

    public PrayerAdjustments() {
        this(0, 0, 0, 0, 0, 0);
    }

    public PrayerAdjustments(int i2, int i3, int i4, int i5, int i6, int i7) {
        this.fajr = i2;
        this.sunrise = i3;
        this.dhuhr = i4;
        this.asr = i5;
        this.maghrib = i6;
        this.isha = i7;
    }
}
