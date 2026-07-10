package com.salatak.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.salatak.app.helpers.PrayerTimesCalculator;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/* loaded from: classes2.dex */
public class PrayerReminderScheduler {
    private static final int ADHAN_REQUEST_CODE_OFFSET = 10000;
    private static final int IQAMA_REQUEST_CODE_OFFSET = 20000;
    private static final String TAG = "PrayerReminderScheduler";

    public static boolean canScheduleExactAlarms(Context context) {
        boolean canScheduleExactAlarms;
        if (Build.VERSION.SDK_INT < 31) {
            return true;
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        if (alarmManager != null) {
            canScheduleExactAlarms = alarmManager.canScheduleExactAlarms();
            if (canScheduleExactAlarms) {
                return true;
            }
        }
        return false;
    }

    public static void cancelAdhanForPrayer(Context context, String str) {
        try {
            PendingIntent broadcast = PendingIntent.getBroadcast(context, str.hashCode() + 10000, new Intent(context, (Class<?>) AdhanAlarmReceiver.class), 201326592);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
            if (alarmManager != null) {
                alarmManager.cancel(broadcast);
                broadcast.cancel();
                Log.d(TAG, "Cancelled adhan for ".concat(str));
            }
        } catch (Exception e2) {
            Log.e(TAG, "Error cancelling adhan for " + str, e2);
        }
    }

    public static void cancelAllReminders(Context context) {
        String[] strArr = {"fajr", "dhuhr", "asr", "maghrib", "isha"};
        for (int i2 = 0; i2 < 5; i2++) {
            String str = strArr[i2];
            cancelReminderForPrayer(context, str);
            cancelAdhanForPrayer(context, str);
            cancelIqamaForPrayer(context, str);
        }
        Log.d(TAG, "All reminders, adhan and iqama alarms cancelled");
    }

    public static void cancelIqamaForPrayer(Context context, String str) {
        try {
            PendingIntent broadcast = PendingIntent.getBroadcast(context, str.hashCode() + 20000, new Intent(context, (Class<?>) IqamaReceiver.class), 201326592);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
            if (alarmManager != null) {
                alarmManager.cancel(broadcast);
                broadcast.cancel();
                Log.d(TAG, "Cancelled iqama for ".concat(str));
            }
        } catch (Exception e2) {
            Log.e(TAG, "Error cancelling iqama for " + str, e2);
        }
    }

    public static void cancelReminderForPrayer(Context context, String str) {
        try {
            PendingIntent broadcast = PendingIntent.getBroadcast(context, str.hashCode(), new Intent(context, (Class<?>) PrayerReminderReceiver.class), 201326592);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
            if (alarmManager != null) {
                alarmManager.cancel(broadcast);
                broadcast.cancel();
                Log.d(TAG, "Cancelled reminder for ".concat(str));
            }
        } catch (Exception e2) {
            Log.e(TAG, "Error cancelling reminder for " + str, e2);
        }
    }

    public static void scheduleAdhanAlarms(Context context) {
        Log.d(TAG, "=== Starting to schedule adhan alarms ===");
        try {
            if (!canScheduleExactAlarms(context)) {
                Log.e(TAG, "Cannot schedule exact alarms - permission not granted");
                return;
            }
            SharedPreferences sharedPreferences = context.getSharedPreferences("SalatakPrefs", 0);
            PrayerTimesCalculator.PrayerTimesResult calculatePrayerTimes = PrayerTimesCalculator.calculatePrayerTimes(context, sharedPreferences.getFloat("latitude", 21.4225f), sharedPreferences.getFloat("longitude", 39.8262f));
            if (calculatePrayerTimes == null) {
                Log.e(TAG, "Failed to calculate prayer times for adhan");
                return;
            }
            scheduleAdhanForPrayer(context, sharedPreferences, "fajr", "الفجر", calculatePrayerTimes.fajr24h);
            scheduleAdhanForPrayer(context, sharedPreferences, "dhuhr", "الظهر", calculatePrayerTimes.dhuhr24h);
            scheduleAdhanForPrayer(context, sharedPreferences, "asr", "العصر", calculatePrayerTimes.asr24h);
            scheduleAdhanForPrayer(context, sharedPreferences, "maghrib", "المغرب", calculatePrayerTimes.maghrib24h);
            scheduleAdhanForPrayer(context, sharedPreferences, "isha", "العشاء", calculatePrayerTimes.isha24h);
            Log.d(TAG, "=== Finished scheduling adhan alarms ===");
        } catch (Exception e2) {
            Log.e(TAG, "Error scheduling adhan alarms", e2);
        }
    }

    private static void scheduleAdhanForPrayer(Context context, SharedPreferences sharedPreferences, String str, String str2, String str3) {
        try {
            if (!sharedPreferences.getBoolean(str + "_adhan_enabled", false)) {
                cancelAdhanForPrayer(context, str);
                return;
            }
            if (str3 != null && !str3.isEmpty() && !str3.equals("--:--")) {
                String[] split = str3.split(":");
                if (split.length != 2) {
                    Log.e(TAG, "Invalid 24h time format for adhan: ".concat(str3));
                    return;
                }
                int parseInt = Integer.parseInt(split[0]);
                int parseInt2 = Integer.parseInt(split[1]);
                Calendar calendar = Calendar.getInstance();
                calendar.set(11, parseInt);
                calendar.set(12, parseInt2);
                calendar.set(13, 0);
                calendar.set(14, 0);
                if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                    calendar.add(5, 1);
                    Log.d(TAG, "Adhan time passed, scheduling for tomorrow");
                }
                Intent intent = new Intent(context, (Class<?>) AdhanAlarmReceiver.class);
                intent.putExtra("prayer_name", str2);
                intent.putExtra("prayer_key", str);
                PendingIntent broadcast = PendingIntent.getBroadcast(context, str.hashCode() + 10000, intent, 201326592);
                scheduleExactAlarm(context, calendar.getTimeInMillis(), broadcast, "Adhan " + str2);
                return;
            }
            Log.e(TAG, "Invalid prayer time for adhan: " + str3);
        } catch (Exception e2) {
            Log.e(TAG, "Error scheduling adhan for " + str2, e2);
        }
    }

    public static void scheduleAllAlarms(Context context) {
        schedulePrayerReminders(context);
        scheduleAdhanAlarms(context);
    }

    private static void scheduleExactAlarm(Context context, long j2, PendingIntent pendingIntent, String str) {
        boolean canScheduleExactAlarms;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        if (alarmManager == null) {
            Log.e(TAG, "AlarmManager is null for " + str);
            return;
        }
        if (Build.VERSION.SDK_INT < 31) {
            alarmManager.setExactAndAllowWhileIdle(0, j2, pendingIntent);
            Log.d(TAG, "✅ " + str + " scheduled using setExactAndAllowWhileIdle");
            return;
        }
        canScheduleExactAlarms = alarmManager.canScheduleExactAlarms();
        if (!canScheduleExactAlarms) {
            Log.e(TAG, "❌ Cannot schedule exact alarms - permission denied for " + str);
        } else {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(j2, pendingIntent), pendingIntent);
            Log.d(TAG, "✅ " + str + " scheduled using setAlarmClock (Android 12+)");
        }
    }

    public static void scheduleIqama(Context context, String str, String str2, int i2) {
        try {
            long currentTimeMillis = (i2 * 60 * 1000) + System.currentTimeMillis();
            Intent intent = new Intent(context, (Class<?>) IqamaReceiver.class);
            intent.putExtra("prayer_name", str2);
            intent.putExtra("prayer_key", str);
            scheduleExactAlarm(context, currentTimeMillis, PendingIntent.getBroadcast(context, str.hashCode() + 20000, intent, 201326592), "Iqama " + str2);
            Log.d(TAG, "Iqama scheduled for " + str2 + " in " + i2 + " minutes");
        } catch (Exception e2) {
            Log.e(TAG, "Error scheduling iqama for " + str2, e2);
        }
    }

    public static void schedulePrayerReminders(Context context) {
        Log.d(TAG, "=== Starting to schedule prayer reminders ===");
        try {
            if (!canScheduleExactAlarms(context)) {
                Log.e(TAG, "Cannot schedule exact alarms - permission not granted");
                return;
            }
            SharedPreferences sharedPreferences = context.getSharedPreferences("SalatakPrefs", 0);
            double d = sharedPreferences.getFloat("latitude", 21.4225f);
            double d2 = sharedPreferences.getFloat("longitude", 39.8262f);
            Log.d(TAG, "Location: " + d + ", " + d2);
            PrayerTimesCalculator.PrayerTimesResult calculatePrayerTimes = PrayerTimesCalculator.calculatePrayerTimes(context, d, d2);
            if (calculatePrayerTimes == null) {
                Log.e(TAG, "Failed to calculate prayer times");
                return;
            }
            Log.d(TAG, "Prayer times calculated successfully (24h format)");
            Log.d(TAG, "Fajr: " + calculatePrayerTimes.fajr24h);
            Log.d(TAG, "Dhuhr: " + calculatePrayerTimes.dhuhr24h);
            Log.d(TAG, "Asr: " + calculatePrayerTimes.asr24h);
            Log.d(TAG, "Maghrib: " + calculatePrayerTimes.maghrib24h);
            Log.d(TAG, "Isha: " + calculatePrayerTimes.isha24h);
            scheduleReminderForPrayer(context, sharedPreferences, "fajr", "الفجر", calculatePrayerTimes.fajr24h);
            scheduleReminderForPrayer(context, sharedPreferences, "dhuhr", "الظهر", calculatePrayerTimes.dhuhr24h);
            scheduleReminderForPrayer(context, sharedPreferences, "asr", "العصر", calculatePrayerTimes.asr24h);
            scheduleReminderForPrayer(context, sharedPreferences, "maghrib", "المغرب", calculatePrayerTimes.maghrib24h);
            scheduleReminderForPrayer(context, sharedPreferences, "isha", "العشاء", calculatePrayerTimes.isha24h);
            Log.d(TAG, "=== Finished scheduling prayer reminders ===");
        } catch (Exception e2) {
            Log.e(TAG, "Error scheduling prayer reminders", e2);
        }
    }

    private static void scheduleReminderForPrayer(Context context, SharedPreferences sharedPreferences, String str, String str2, String str3) {
        try {
            Log.d(TAG, "--- Processing " + str2 + " (" + str + ") ---");
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("_reminder_enabled");
            boolean z2 = sharedPreferences.getBoolean(sb.toString(), false);
            Log.d(TAG, "Reminder enabled: " + z2);
            if (!z2) {
                cancelReminderForPrayer(context, str);
                Log.d(TAG, "Reminder cancelled for " + str2);
                return;
            }
            int i2 = sharedPreferences.getInt(str + "_reminder_minutes", 5);
            StringBuilder sb2 = new StringBuilder("Minutes before: ");
            sb2.append(i2);
            Log.d(TAG, sb2.toString());
            if (str3 != null && !str3.isEmpty() && !str3.equals("--:--")) {
                String[] split = str3.split(":");
                if (split.length != 2) {
                    Log.e(TAG, "Invalid 24h time format: ".concat(str3));
                    return;
                }
                int parseInt = Integer.parseInt(split[0]);
                int parseInt2 = Integer.parseInt(split[1]);
                Calendar calendar = Calendar.getInstance();
                calendar.set(11, parseInt);
                calendar.set(12, parseInt2);
                calendar.set(13, 0);
                calendar.set(14, 0);
                calendar.add(12, -i2);
                if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                    calendar.add(5, 1);
                    Log.d(TAG, "Time already passed, scheduling for tomorrow");
                }
                Log.d(TAG, "Scheduled time: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(calendar.getTime()));
                Log.d(TAG, "Time until alarm: " + (((calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000) / 60) + " minutes");
                Intent intent = new Intent(context, (Class<?>) PrayerReminderReceiver.class);
                intent.putExtra("prayer_name", str2);
                intent.putExtra("prayer_key", str);
                intent.putExtra("minutes_before", i2);
                PendingIntent broadcast = PendingIntent.getBroadcast(context, str.hashCode(), intent, 201326592);
                scheduleExactAlarm(context, calendar.getTimeInMillis(), broadcast, "Reminder " + str2);
                return;
            }
            Log.e(TAG, "Invalid prayer time: " + str3);
        } catch (Exception e2) {
            Log.e(TAG, "Error scheduling reminder for " + str2, e2);
        }
    }
}
