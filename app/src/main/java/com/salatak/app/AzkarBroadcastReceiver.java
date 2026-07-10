package com.salatak.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import androidx.media3.common.MimeTypes;

/* loaded from: classes2.dex */
public class AzkarBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION_SHOW_AZKAR = "com.salatak.app.SHOW_AZKAR";
    private static final String CHANNEL_ID = "azkar_notifications";
    private static final int NOTIFICATION_ID = 2001;
    private static final String TAG = "AzkarBroadcastReceiver";

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("SalatakPrefs", 0);
            boolean z2 = sharedPreferences.getBoolean("azkar_vibration_enabled", true);
            boolean z3 = sharedPreferences.getBoolean("vibration_enabled", true);
            boolean z4 = sharedPreferences.getBoolean("led_enabled", false);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            if (notificationManager != null) {
                notificationManager.deleteNotificationChannel("azkar_notifications");
            }
            NotificationChannel q2 = a.q();
            q2.setDescription("إشعارات لعرض الأذكار الإسلامية - تظهر كنافذة منبثقة");
            q2.enableLights(true);
            q2.setLightColor(-16776961);
            q2.setShowBadge(true);
            q2.setBypassDnd(false);
            q2.setLockscreenVisibility(1);
            q2.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, new AudioAttributes.Builder().setContentType(4).setUsage(5).build());
            if (z2 && z3) {
                q2.enableVibration(true);
                q2.setVibrationPattern(new long[]{0, 500, 200, 500});
            } else {
                q2.enableVibration(false);
            }
            if (z4) {
                q2.enableLights(true);
                q2.setLightColor(-16776961);
            } else {
                q2.enableLights(false);
            }
            NotificationManager notificationManager2 = (NotificationManager) context.getSystemService(NotificationManager.class);
            if (notificationManager2 != null) {
                notificationManager2.createNotificationChannel(q2);
                Log.d(TAG, "Notification channel created with IMPORTANCE_HIGH for heads-up display");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$playAudioAzkar$0(MediaPlayer mediaPlayer) {
        try {
            mediaPlayer.release();
            Log.d(TAG, "MediaPlayer released after completion");
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error releasing MediaPlayer: "), TAG);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$playAudioAzkar$1(MediaPlayer mediaPlayer, int i2, int i3) {
        Log.e(TAG, "MediaPlayer error: what=" + i2 + ", extra=" + i3);
        try {
            mediaPlayer.release();
            return true;
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error releasing MediaPlayer on error: "), TAG);
            return true;
        }
    }

    private void playAudioAzkar(Context context, String str, boolean z2) {
        Vibrator vibrator;
        int i2 = 0;
        try {
            AssetFileDescriptor openFd = context.getAssets().openFd("azkar_audio/" + str);
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
            openFd.close();
            boolean z3 = context.getSharedPreferences("SalatakPrefs", 0).getBoolean("use_media_volume", false);
            AudioAttributes.Builder contentType = new AudioAttributes.Builder().setContentType(2);
            if (z3) {
                contentType.setUsage(1);
            } else {
                contentType.setUsage(4);
                AudioManager audioManager = (AudioManager) context.getSystemService(MimeTypes.BASE_TYPE_AUDIO);
                if (audioManager != null) {
                    audioManager.setStreamVolume(4, audioManager.getStreamMaxVolume(4), 0);
                }
            }
            mediaPlayer.setAudioAttributes(contentType.build());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new t(i2));
            mediaPlayer.setOnErrorListener(new u(i2));
            mediaPlayer.start();
            Log.d(TAG, "Audio azkar playing from file: " + str);
            boolean z4 = context.getSharedPreferences("SalatakPrefs", 0).getBoolean("vibration_enabled", true);
            if (z2 && z4 && (vibrator = (Vibrator) context.getSystemService("vibrator")) != null && vibrator.hasVibrator()) {
                vibrator.vibrate(new long[]{0, 500, 200, 500}, -1);
            }
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error playing audio azkar from file: "), TAG);
        }
    }

    private void showAzkarNotification(Context context) {
        try {
            createNotificationChannel(context);
            String randomAzkar = AzkarService.getRandomAzkar(context);
            SharedPreferences sharedPreferences = context.getSharedPreferences("SalatakPrefs", 0);
            boolean z2 = sharedPreferences.getBoolean("azkar_text_notification_enabled", true);
            boolean z3 = sharedPreferences.getBoolean("azkar_vibration_enabled", true);
            boolean z4 = sharedPreferences.getBoolean("azkar_audio_enabled", false);
            if (z2) {
                AdhanOverlayService.showAzkarOverlay(context, randomAzkar);
                Log.d(TAG, "Azkar overlay shown via AdhanOverlayService: " + randomAzkar);
            }
            if (z4) {
                String string = sharedPreferences.getString("selected_audio_azkar_file", "");
                if (string.isEmpty()) {
                    string = AzkarService.getRandomAudioFile();
                }
                playAudioAzkar(context, string, z3);
            }
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error showing azkar notification: "), TAG);
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            Log.d(TAG, "Received broadcast: " + action);
            if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("SalatakPrefs", 0);
                if (sharedPreferences.getBoolean("azkar_enabled", false)) {
                    AzkarService.scheduleAzkar(context, sharedPreferences.getInt("azkar_interval_minutes", 30));
                    AzkarService.fetchAzkarFromAPIIfNeeded(context);
                    Log.d(TAG, "Azkar scheduled after boot");
                }
                PrayerReminderScheduler.scheduleAllAlarms(context);
                Log.d(TAG, "Prayer reminders and adhan alarms scheduled after boot");
                return;
            }
            if (ACTION_SHOW_AZKAR.equals(action)) {
                SharedPreferences sharedPreferences2 = context.getSharedPreferences("SalatakPrefs", 0);
                if (!sharedPreferences2.getBoolean("azkar_enabled", false)) {
                    Log.d(TAG, "Azkar disabled, not showing");
                    return;
                }
                int i2 = sharedPreferences2.getInt("azkar_interval_minutes", 30);
                AzkarService.scheduleAzkar(context, i2);
                Log.d(TAG, "Next azkar scheduled for " + i2 + " minutes");
                showAzkarNotification(context);
            }
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error in onReceive: "), TAG);
        }
    }
}
