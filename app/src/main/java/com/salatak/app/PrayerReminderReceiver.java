package com.salatak.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.media3.common.MimeTypes;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import java.io.IOException;

/* loaded from: classes2.dex */
public class PrayerReminderReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "prayer_reminder_channel";
    private static final String TAG = "PrayerReminderReceiver";
    private static MediaPlayer mediaPlayer;
    private static PowerManager.WakeLock wakeLock;

    private void acquireWakeLock(Context context) {
        try {
            PowerManager powerManager = (PowerManager) context.getSystemService("power");
            if (powerManager != null) {
                PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(268435457, "Salatak:PrayerReminder");
                wakeLock = newWakeLock;
                newWakeLock.acquire(60000L);
                Log.d(TAG, "WakeLock acquired");
            }
        } catch (Exception e2) {
            Log.e(TAG, "Error acquiring WakeLock", e2);
        }
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel w2 = a.w();
            w2.setDescription("تنبيهات قبل مواقيت الصلاة");
            w2.enableVibration(true);
            w2.setVibrationPattern(new long[]{0, 500, 200, 500});
            w2.setSound(null, null);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(w2);
                Log.d(TAG, "Notification channel created");
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0075, code lost:
    
        if (r7.equals("sunrise") == false) goto L8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String getSoundFileName(android.content.Context r6, java.lang.String r7) {
        /*
            r5 = this;
            java.lang.String r0 = "maghrib"
            java.lang.String r1 = "asr"
            java.lang.String r2 = "SalatakPrefs"
            r3 = 0
            android.content.SharedPreferences r6 = r6.getSharedPreferences(r2, r3)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r7)
            java.lang.String r4 = "_reminder_sound"
            r2.append(r4)
            java.lang.String r2 = r2.toString()
            java.lang.String r4 = "soon"
            java.lang.String r6 = r6.getString(r2, r4)
            java.lang.String r2 = "now"
            boolean r6 = r6.equals(r2)
            if (r6 == 0) goto L2d
            java.lang.String r6 = "now.mp3"
            goto L2f
        L2d:
            java.lang.String r6 = "soon.mp3"
        L2f:
            r7.getClass()
            r2 = -1
            int r4 = r7.hashCode()
            switch(r4) {
                case -1856560363: goto L6f;
                case 96896: goto L66;
                case 3135299: goto L5b;
                case 3241891: goto L50;
                case 95566139: goto L45;
                case 829014902: goto L3c;
                default: goto L3a;
            }
        L3a:
            r3 = r2
            goto L78
        L3c:
            boolean r7 = r7.equals(r0)
            if (r7 != 0) goto L43
            goto L3a
        L43:
            r3 = 5
            goto L78
        L45:
            java.lang.String r3 = "dhuhr"
            boolean r7 = r7.equals(r3)
            if (r7 != 0) goto L4e
            goto L3a
        L4e:
            r3 = 4
            goto L78
        L50:
            java.lang.String r3 = "isha"
            boolean r7 = r7.equals(r3)
            if (r7 != 0) goto L59
            goto L3a
        L59:
            r3 = 3
            goto L78
        L5b:
            java.lang.String r3 = "fajr"
            boolean r7 = r7.equals(r3)
            if (r7 != 0) goto L64
            goto L3a
        L64:
            r3 = 2
            goto L78
        L66:
            boolean r7 = r7.equals(r1)
            if (r7 != 0) goto L6d
            goto L3a
        L6d:
            r3 = 1
            goto L78
        L6f:
            java.lang.String r4 = "sunrise"
            boolean r7 = r7.equals(r4)
            if (r7 != 0) goto L78
            goto L3a
        L78:
            switch(r3) {
                case 0: goto L9c;
                case 1: goto L97;
                case 2: goto L90;
                case 3: goto L89;
                case 4: goto L82;
                case 5: goto L7d;
                default: goto L7b;
            }
        L7b:
            r6 = 0
            return r6
        L7d:
            java.lang.String r6 = r0.concat(r6)
            return r6
        L82:
            java.lang.String r7 = "zohr"
            java.lang.String r6 = r7.concat(r6)
            return r6
        L89:
            java.lang.String r7 = "eshaa"
            java.lang.String r6 = r7.concat(r6)
            return r6
        L90:
            java.lang.String r7 = "fagr"
            java.lang.String r6 = r7.concat(r6)
            return r6
        L97:
            java.lang.String r6 = r1.concat(r6)
            return r6
        L9c:
            java.lang.String r7 = "gom3a"
            java.lang.String r6 = r7.concat(r6)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.salatak.app.PrayerReminderReceiver.getSoundFileName(android.content.Context, java.lang.String):java.lang.String");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playReminderSound$0(MediaPlayer mediaPlayer2) {
        Log.d(TAG, "Sound playback completed");
        stopCurrentSound();
        releaseWakeLock();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$playReminderSound$1(MediaPlayer mediaPlayer2, int i2, int i3) {
        Log.e(TAG, "MediaPlayer error: what=" + i2 + ", extra=" + i3);
        stopCurrentSound();
        releaseWakeLock();
        return true;
    }

    private void playReminderSound(Context context, String str) {
        try {
            String soundFileName = getSoundFileName(context, str);
            if (soundFileName == null) {
                Log.e(TAG, "No sound file for prayer: " + str);
                return;
            }
            Log.d(TAG, "Playing sound: ".concat(soundFileName));
            stopCurrentSound();
            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor openFd = context.getAssets().openFd("prayer_reminders/".concat(soundFileName));
            if (openFd == null) {
                Log.e(TAG, "Asset file descriptor is null");
                return;
            }
            mediaPlayer.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
            openFd.close();
            boolean useMediaVolume = useMediaVolume(context);
            AudioManager audioManager = (AudioManager) context.getSystemService(MimeTypes.BASE_TYPE_AUDIO);
            if (audioManager != null && !useMediaVolume) {
                int streamMaxVolume = audioManager.getStreamMaxVolume(4);
                audioManager.setStreamVolume(4, streamMaxVolume, 0);
                Log.d(TAG, "Volume set to max: " + streamMaxVolume);
            }
            mediaPlayer.setAudioAttributes(useMediaVolume ? new AudioAttributes.Builder().setUsage(1).setContentType(2).build() : new AudioAttributes.Builder().setUsage(4).setContentType(4).setFlags(1).build());
            mediaPlayer.setLooping(false);
            mediaPlayer.setOnCompletionListener(new v0(4, this));
            mediaPlayer.setOnErrorListener(new w0(2, this));
            mediaPlayer.prepare();
            mediaPlayer.start();
            Log.d(TAG, "Sound started playing successfully");
        } catch (IOException e2) {
            Log.e(TAG, "IOException while playing sound", e2);
            stopCurrentSound();
            releaseWakeLock();
        } catch (Exception e3) {
            Log.e(TAG, "Exception while playing sound", e3);
            stopCurrentSound();
            releaseWakeLock();
        }
    }

    private void releaseWakeLock() {
        try {
            PowerManager.WakeLock wakeLock2 = wakeLock;
            if (wakeLock2 == null || !wakeLock2.isHeld()) {
                return;
            }
            wakeLock.release();
            wakeLock = null;
            Log.d(TAG, "WakeLock released");
        } catch (Exception e2) {
            Log.e(TAG, "Error releasing WakeLock", e2);
        }
    }

    private void showNotification(Context context, String str, int i2) {
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            if (notificationManager == null) {
                Log.e(TAG, "NotificationManager is null");
                return;
            }
            String str2 = "🕌 تنبيه صلاة " + str;
            String str3 = "باقي " + i2 + " دقيقة على أذان " + str;
            PendingIntent activity = PendingIntent.getActivity(context, 0, new Intent(context, (Class<?>) MainActivity.class), 201326592);
            SharedPreferences sharedPreferences = context.getSharedPreferences("SalatakPrefs", 0);
            boolean z2 = sharedPreferences.getBoolean("vibration_enabled", true);
            boolean z3 = sharedPreferences.getBoolean("led_enabled", false);
            NotificationCompat.Builder visibility = new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(str2).setContentText(str3).setStyle(new NotificationCompat.BigTextStyle().bigText(str3)).setPriority(2).setCategory(NotificationCompat.CATEGORY_ALARM).setAutoCancel(true).setContentIntent(activity).setSound(null).setVisibility(1);
            if (z2) {
                visibility.setVibrate(new long[]{0, 500, 200, 500});
            } else {
                visibility.setVibrate(new long[]{0});
            }
            if (z3) {
                visibility.setLights(-16711936, PathInterpolatorCompat.MAX_NUM_POINTS, PathInterpolatorCompat.MAX_NUM_POINTS);
            }
            notificationManager.notify(str.hashCode(), visibility.build());
            Log.d(TAG, "Notification shown: " + str2);
        } catch (Exception e2) {
            Log.e(TAG, "Error showing notification", e2);
        }
    }

    public static void stopCurrentSound() {
        MediaPlayer mediaPlayer2 = mediaPlayer;
        if (mediaPlayer2 != null) {
            try {
                if (mediaPlayer2.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
                Log.d(TAG, "MediaPlayer released");
            } catch (Exception e2) {
                Log.e(TAG, "Error stopping sound", e2);
            }
        }
    }

    private static boolean useMediaVolume(Context context) {
        return context.getSharedPreferences("SalatakPrefs", 0).getBoolean("use_media_volume", false);
    }

    private void vibrate(Context context) {
        Vibrator vibrator;
        try {
            if (context.getSharedPreferences("SalatakPrefs", 0).getBoolean("vibration_enabled", true) && (vibrator = (Vibrator) context.getSystemService("vibrator")) != null && vibrator.hasVibrator()) {
                vibrator.vibrate(new long[]{0, 500, 200, 500, 200, 500}, -1);
                Log.d(TAG, "Vibration triggered");
            }
        } catch (Exception e2) {
            Log.e(TAG, "Error vibrating", e2);
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "=== Prayer Reminder Received ===");
        try {
            String stringExtra = intent.getStringExtra("prayer_name");
            String stringExtra2 = intent.getStringExtra("prayer_key");
            int intExtra = intent.getIntExtra("minutes_before", 5);
            Log.d(TAG, "Prayer: " + stringExtra + ", Key: " + stringExtra2 + ", Minutes: " + intExtra);
            if (stringExtra != null && stringExtra2 != null) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("SalatakPrefs", 0);
                boolean z2 = sharedPreferences.getBoolean(stringExtra2.concat("_reminder_enabled"), false);
                Log.d(TAG, "Reminder enabled for " + stringExtra2 + ": " + z2);
                if (!z2) {
                    Log.d(TAG, "Reminder disabled, skipping");
                    return;
                }
                acquireWakeLock(context);
                createNotificationChannel(context);
                showNotification(context, stringExtra, intExtra);
                playReminderSound(context, stringExtra2);
                vibrate(context);
                if (sharedPreferences.getBoolean("notification_only_mode", false)) {
                    Log.d(TAG, "Notification-only mode: skipping reminder overlay");
                } else {
                    AdhanOverlayService.showReminderOverlay(context, stringExtra, intExtra);
                }
                try {
                    PrayerReminderScheduler.scheduleAllAlarms(context);
                } catch (Exception e2) {
                    Log.e(TAG, "Error rescheduling alarms", e2);
                }
                Log.d(TAG, "=== Reminder processing complete ===");
                return;
            }
            Log.e(TAG, "Missing prayer info!");
        } catch (Exception e3) {
            Log.e(TAG, "Error in onReceive", e3);
        }
    }
}
