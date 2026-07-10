package com.salatak.app;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/* loaded from: classes2.dex */
public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    private void rescheduleWidgetAlarms(Context context) {
        try {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, (Class<?>) PrayerTimesWidgetProvider.class));
            if (appWidgetIds != null && appWidgetIds.length > 0) {
                new PrayerTimesWidgetProvider().onUpdate(context, appWidgetManager, appWidgetIds);
                Log.d(TAG, "Prayer times widget alarms rescheduled");
            }
            int[] appWidgetIds2 = appWidgetManager.getAppWidgetIds(new ComponentName(context, (Class<?>) ClockWidgetProvider.class));
            if (appWidgetIds2 != null && appWidgetIds2.length > 0) {
                new ClockWidgetProvider().onUpdate(context, appWidgetManager, appWidgetIds2);
                Log.d(TAG, "Clock widget alarms rescheduled");
            }
            int[] appWidgetIds3 = appWidgetManager.getAppWidgetIds(new ComponentName(context, (Class<?>) AzkarWidgetProvider.class));
            if (appWidgetIds3 != null && appWidgetIds3.length > 0) {
                new AzkarWidgetProvider().onUpdate(context, appWidgetManager, appWidgetIds3);
                Log.d(TAG, "Azkar widget alarms rescheduled");
            }
            int[] appWidgetIds4 = appWidgetManager.getAppWidgetIds(new ComponentName(context, (Class<?>) CalendarWidgetProvider.class));
            if (appWidgetIds4 == null || appWidgetIds4.length <= 0) {
                return;
            }
            new CalendarWidgetProvider().onUpdate(context, appWidgetManager, appWidgetIds4);
            Log.d(TAG, "Calendar widget updated after boot");
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error rescheduling widget alarms: "), TAG);
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent == null || !"android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            return;
        }
        try {
            Log.d(TAG, "Boot completed - rescheduling alarms");
            SharedPreferences sharedPreferences = context.getSharedPreferences("SalatakPrefs", 0);
            if (sharedPreferences.getBoolean("azkar_enabled", false)) {
                AzkarService.scheduleAzkar(context, sharedPreferences.getInt("azkar_interval_minutes", 30));
                AzkarService.fetchAzkarFromAPIIfNeeded(context);
                Log.d(TAG, "Azkar scheduled after boot");
            }
            PrayerReminderScheduler.scheduleAllAlarms(context);
            Log.d(TAG, "Prayer reminders and adhan alarms scheduled after boot");
            rescheduleWidgetAlarms(context);
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error handling boot: "), TAG);
        }
    }
}
