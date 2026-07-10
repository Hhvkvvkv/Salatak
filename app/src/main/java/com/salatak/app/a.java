package com.salatak.app;

import android.app.NotificationChannel;

/* loaded from: classes2.dex */
public abstract /* synthetic */ class a {
    public static /* synthetic */ NotificationChannel A() {
        return new NotificationChannel("radio_service_channel", "الإذاعة", 2);
    }

    public static /* synthetic */ NotificationChannel a() {
        return new NotificationChannel("adhan_alarm_channel", "الأذان", 4);
    }

    public static /* synthetic */ NotificationChannel h() {
        return new NotificationChannel("adhan_overlay_channel", "تنبيهات الصلاة", 2);
    }

    public static /* synthetic */ NotificationChannel q() {
        return new NotificationChannel("azkar_notifications", "إشعارات الأذكار", 4);
    }

    public static /* synthetic */ NotificationChannel s() {
        return new NotificationChannel("azkar_service_channel", "خدمة الأذكار", 2);
    }

    public static /* synthetic */ NotificationChannel u() {
        return new NotificationChannel("iqama_channel", "الإقامة", 4);
    }

    public static /* synthetic */ NotificationChannel w() {
        return new NotificationChannel("prayer_reminder_channel", "تنبيهات الصلاة", 4);
    }

    public static /* synthetic */ NotificationChannel y() {
        return new NotificationChannel("quran_player_channel", "مشغل القرآن", 2);
    }
}
