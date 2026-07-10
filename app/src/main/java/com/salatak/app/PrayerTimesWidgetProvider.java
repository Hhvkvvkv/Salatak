package com.salatak.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import com.batoulapps.adhan.Prayer;
import com.google.android.material.timepicker.TimeModel;
import com.salatak.app.helpers.PrayerTimesCalculator;
import java.util.Calendar;

/* loaded from: classes2.dex */
public class PrayerTimesWidgetProvider extends AppWidgetProvider {
    private static final String ACTION_UPDATE = "com.salatak.app.WIDGET_PRAYER_UPDATE";

    /* renamed from: com.salatak.app.PrayerTimesWidgetProvider$1, reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$batoulapps$adhan$Prayer;

        static {
            int[] iArr = new int[Prayer.values().length];
            $SwitchMap$com$batoulapps$adhan$Prayer = iArr;
            try {
                iArr[Prayer.FAJR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$Prayer[Prayer.SUNRISE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$Prayer[Prayer.DHUHR.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$Prayer[Prayer.ASR.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$Prayer[Prayer.MAGHRIB.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$Prayer[Prayer.ISHA.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    private static int[] calculateHijri(Calendar calendar) {
        int i2 = calendar.get(5);
        int i3 = calendar.get(2);
        int i4 = i3 + 1;
        int i5 = calendar.get(1);
        if (i4 <= 2) {
            i5--;
            i4 = i3 + 13;
        }
        int i6 = i5 / 100;
        int i7 = (((((((int) ((i5 + 4716) * 365.25d)) + ((int) ((i4 + 1) * 30.6001d))) + i2) + ((i6 / 4) + (2 - i6))) - 1939332) - (((int) ((r9 - 1939333) / 10631.0d)) * 10631)) + 354;
        int i8 = ((i7 - (((int) ((30 - r2) / 15.0d)) * ((int) ((r2 * 17719) / 50.0d)))) - (((int) (((((int) (i7 / 5670.0d)) * ((int) ((i7 * 43) / 15238.0d))) + (((int) ((10985 - i7) / 5316.0d)) * ((int) ((i7 * 50) / 17719.0d)))) / 16.0d)) * ((int) ((r2 * 15238) / 43.0d)))) + 29;
        int i9 = (i8 * 24) / 709;
        return new int[]{i8 - ((i9 * 709) / 24), i9, ((r9 * 30) + r2) - 30};
    }

    public static String convertToArabicNumerals(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c < '0' || c > '9') {
                sb.append(c);
            } else {
                sb.append((char) (c + 1584));
            }
        }
        return sb.toString();
    }

    private static String getNextPrayerTimeFormatted(PrayerTimesCalculator.PrayerTimesResult prayerTimesResult) {
        Prayer prayer = prayerTimesResult.nextPrayer;
        if (prayer == null) {
            return "";
        }
        switch (AnonymousClass1.$SwitchMap$com$batoulapps$adhan$Prayer[prayer.ordinal()]) {
        }
        return "";
    }

    private void scheduleNextUpdate(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        Intent intent = new Intent(context, (Class<?>) PrayerTimesWidgetProvider.class);
        intent.setAction(ACTION_UPDATE);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, 201326592);
        if (alarmManager != null) {
            alarmManager.set(3, SystemClock.elapsedRealtime() + 60000, broadcast);
        }
    }

    private static void updateWidget(Context context, AppWidgetManager appWidgetManager, int i2) {
        double d;
        double d2;
        int i3;
        String str;
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_prayer_times);
        remoteViews.setOnClickPendingIntent(R.id.widgetPrayerRoot, PendingIntent.getActivity(context, 0, new Intent(context, (Class<?>) MainActivity.class), AccessibilityEventCompat.TYPE_VIEW_TARGETED_BY_SCROLL));
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("SalatakPrefs", 0);
            try {
                try {
                    d2 = sharedPreferences.getFloat("latitude", 0.0f);
                    d = sharedPreferences.getFloat("longitude", 0.0f);
                } catch (Exception unused) {
                    d = 0.0d;
                    d2 = 0.0d;
                }
            } catch (ClassCastException unused2) {
                d2 = Double.parseDouble(sharedPreferences.getString("latitude", "0"));
                d = Double.parseDouble(sharedPreferences.getString("longitude", "0"));
            }
            if (d2 != 0.0d || d != 0.0d) {
                PrayerTimesCalculator.PrayerTimesResult calculatePrayerTimes = PrayerTimesCalculator.calculatePrayerTimes(context, d2, d);
                remoteViews.setTextViewText(R.id.tvWidgetFajr, calculatePrayerTimes.fajr);
                remoteViews.setTextViewText(R.id.tvWidgetDhuhr, calculatePrayerTimes.dhuhr);
                remoteViews.setTextViewText(R.id.tvWidgetAsr, calculatePrayerTimes.asr);
                remoteViews.setTextViewText(R.id.tvWidgetMaghrib, calculatePrayerTimes.maghrib);
                remoteViews.setTextViewText(R.id.tvWidgetIsha, calculatePrayerTimes.isha);
                remoteViews.setTextViewText(R.id.tvWidgetNextPrayerName, PrayerTimesCalculator.getPrayerNameArabic(calculatePrayerTimes.nextPrayer));
                remoteViews.setTextViewText(R.id.tvWidgetNextPrayerTime, getNextPrayerTimeFormatted(calculatePrayerTimes));
                long currentTimeMillis = calculatePrayerTimes.nextPrayerTime - System.currentTimeMillis();
                if (currentTimeMillis > 0) {
                    long j2 = currentTimeMillis / 60000;
                    long j3 = j2 / 60;
                    long j4 = j2 % 60;
                    if (j3 > 0) {
                        str = convertToArabicNumerals(String.valueOf(j3)) + ":" + convertToArabicNumerals(String.format(TimeModel.ZERO_LEADING_NUMBER_FORMAT, Long.valueOf(j4)));
                    } else {
                        str = convertToArabicNumerals(String.valueOf(j4)) + " د";
                    }
                    remoteViews.setTextViewText(R.id.tvWidgetCountdown, str);
                    remoteViews.setTextViewText(R.id.tvWidgetNextPrayer, "متبقي");
                } else {
                    remoteViews.setTextViewText(R.id.tvWidgetCountdown, "الآن");
                    remoteViews.setTextViewText(R.id.tvWidgetNextPrayer, "حان الوقت");
                }
                String[] strArr = {"محرم", "صفر", "ربيع الأول", "ربيع الثاني", "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان", "رمضان", "شوال", "ذو القعدة", "ذو الحجة"};
                int[] calculateHijri = calculateHijri(Calendar.getInstance());
                if (calculateHijri[0] > 0 && (i3 = calculateHijri[1]) >= 1 && i3 <= 12) {
                    remoteViews.setTextViewText(R.id.tvWidgetDate, convertToArabicNumerals(String.valueOf(calculateHijri[0])) + " " + strArr[calculateHijri[1] - 1]);
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        appWidgetManager.updateAppWidget(i2, remoteViews);
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onDisabled(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        Intent intent = new Intent(context, (Class<?>) PrayerTimesWidgetProvider.class);
        intent.setAction(ACTION_UPDATE);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, 603979776);
        if (alarmManager == null || broadcast == null) {
            return;
        }
        alarmManager.cancel(broadcast);
    }

    @Override // android.appwidget.AppWidgetProvider, android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_UPDATE.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            for (int i2 : appWidgetManager.getAppWidgetIds(new ComponentName(context, (Class<?>) PrayerTimesWidgetProvider.class))) {
                updateWidget(context, appWidgetManager, i2);
            }
            scheduleNextUpdate(context);
        }
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] iArr) {
        for (int i2 : iArr) {
            updateWidget(context, appWidgetManager, i2);
        }
        scheduleNextUpdate(context);
    }
}
