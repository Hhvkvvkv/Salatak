package com.salatak.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.media3.exoplayer.upstream.CmcdData;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/* loaded from: classes2.dex */
public class ClockWidgetProvider extends AppWidgetProvider {
    private static final String ACTION_UPDATE_CLOCK = "com.salatak.app.WIDGET_CLOCK_UPDATE";

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

    private static String convertToArabicNumerals(String str) {
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

    private void scheduleNextUpdate(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        Intent intent = new Intent(context, (Class<?>) ClockWidgetProvider.class);
        intent.setAction(ACTION_UPDATE_CLOCK);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, 201326592);
        if (alarmManager != null) {
            alarmManager.set(3, SystemClock.elapsedRealtime() + 60000, broadcast);
        }
    }

    private static void updateWidget(Context context, AppWidgetManager appWidgetManager, int i2) {
        int i3;
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_clock);
        remoteViews.setOnClickPendingIntent(R.id.widgetClockRoot, PendingIntent.getActivity(context, 0, new Intent(context, (Class<?>) MainActivity.class), AccessibilityEventCompat.TYPE_VIEW_TARGETED_BY_SCROLL));
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm", Locale.US);
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY, new Locale("ar"));
            String convertToArabicNumerals = convertToArabicNumerals(simpleDateFormat.format(calendar.getTime()));
            String format = simpleDateFormat2.format(calendar.getTime());
            remoteViews.setTextViewText(R.id.tvClockWidgetTime, convertToArabicNumerals);
            remoteViews.setTextViewText(R.id.tvClockWidgetAmPm, format);
            String[] strArr = {"محرم", "صفر", "ربيع الأول", "ربيع الثاني", "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان", "رمضان", "شوال", "ذو القعدة", "ذو الحجة"};
            int[] calculateHijri = calculateHijri(calendar);
            if (calculateHijri[0] > 0 && (i3 = calculateHijri[1]) >= 1 && i3 <= 12) {
                remoteViews.setTextViewText(R.id.tvClockWidgetDate, convertToArabicNumerals(String.valueOf(calculateHijri[0])) + " " + strArr[calculateHijri[1] - 1] + " " + convertToArabicNumerals(String.valueOf(calculateHijri[2])) + " هـ");
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        appWidgetManager.updateAppWidget(i2, remoteViews);
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onDisabled(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        Intent intent = new Intent(context, (Class<?>) ClockWidgetProvider.class);
        intent.setAction(ACTION_UPDATE_CLOCK);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, 603979776);
        if (alarmManager == null || broadcast == null) {
            return;
        }
        alarmManager.cancel(broadcast);
    }

    @Override // android.appwidget.AppWidgetProvider, android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_UPDATE_CLOCK.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            for (int i2 : appWidgetManager.getAppWidgetIds(new ComponentName(context, (Class<?>) ClockWidgetProvider.class))) {
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
