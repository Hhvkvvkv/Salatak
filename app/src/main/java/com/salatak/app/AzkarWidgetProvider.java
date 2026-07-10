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

/* loaded from: classes2.dex */
public class AzkarWidgetProvider extends AppWidgetProvider {
    private static final String ACTION_UPDATE_AZKAR = "com.salatak.app.WIDGET_AZKAR_UPDATE";

    private void scheduleNextUpdate(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        Intent intent = new Intent(context, (Class<?>) AzkarWidgetProvider.class);
        intent.setAction(ACTION_UPDATE_AZKAR);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, 201326592);
        if (alarmManager != null) {
            alarmManager.set(3, SystemClock.elapsedRealtime() + 60000, broadcast);
        }
    }

    private static void updateWidget(Context context, AppWidgetManager appWidgetManager, int i2) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_azkar);
        remoteViews.setOnClickPendingIntent(R.id.widgetAzkarRoot, PendingIntent.getActivity(context, 0, new Intent(context, (Class<?>) MainActivity.class), AccessibilityEventCompat.TYPE_VIEW_TARGETED_BY_SCROLL));
        try {
            remoteViews.setTextViewText(R.id.tvAzkarWidgetText, AzkarService.getRandomAzkar(context));
        } catch (Exception unused) {
            remoteViews.setTextViewText(R.id.tvAzkarWidgetText, "سبحان الله وبحمده سبحان الله العظيم");
        }
        appWidgetManager.updateAppWidget(i2, remoteViews);
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onDisabled(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        Intent intent = new Intent(context, (Class<?>) AzkarWidgetProvider.class);
        intent.setAction(ACTION_UPDATE_AZKAR);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, 603979776);
        if (alarmManager == null || broadcast == null) {
            return;
        }
        alarmManager.cancel(broadcast);
    }

    @Override // android.appwidget.AppWidgetProvider, android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_UPDATE_AZKAR.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            for (int i2 : appWidgetManager.getAppWidgetIds(new ComponentName(context, (Class<?>) AzkarWidgetProvider.class))) {
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
