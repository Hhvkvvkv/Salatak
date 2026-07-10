package com.salatak.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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

/* loaded from: classes2.dex */
public class IqamaReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "iqama_channel";
    private static final String TAG = "IqamaReceiver";
    private static MediaPlayer mediaPlayer;
    private static PowerManager.WakeLock wakeLock;

    private void acquireWakeLock(Context context) {
        try {
            PowerManager powerManager = (PowerManager) context.getSystemService("power");
            if (powerManager != null) {
                PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(268435457, "Salatak:IqamaAlarm");
                wakeLock = newWakeLock;
                newWakeLock.acquire(300000L);
                Log.d(TAG, "WakeLock acquired");
            }
        } catch (Exception e2) {
            Log.e(TAG, "Error acquiring WakeLock", e2);
        }
    }

    private static AudioAttributes buildAudioAttributes(Context context) {
        return useMediaVolume(context) ? new AudioAttributes.Builder().setUsage(1).setContentType(2).build() : new AudioAttributes.Builder().setUsage(4).setContentType(4).setFlags(1).build();
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel u2 = a.u();
            u2.setDescription("تنبيهات إقامة الصلاة");
            u2.enableVibration(true);
            u2.setSound(null, null);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(u2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$playIqamaSound$0(MediaPlayer mediaPlayer2) {
        Log.d(TAG, "Iqama playback completed");
        stopCurrentSound();
        releaseWakeLock();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$playIqamaSound$1(MediaPlayer mediaPlayer2, int i2, int i3) {
        Log.e(TAG, "Iqama MediaPlayer error: what=" + i2 + ", extra=" + i3);
        stopCurrentSound();
        releaseWakeLock();
        return true;
    }

    private void playIqamaSound(Context context) {
        try {
            stopCurrentSound();
            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor openFd = context.getAssets().openFd("prayer_reminders/iqama.m4a");
            mediaPlayer.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
            openFd.close();
            setVolume(context);
            mediaPlayer.setAudioAttributes(buildAudioAttributes(context));
            mediaPlayer.setLooping(false);
            mediaPlayer.setOnCompletionListener(new t(1));
            mediaPlayer.setOnErrorListener(new u(1));
            mediaPlayer.prepare();
            mediaPlayer.start();
            Log.d(TAG, "Iqama started playing");
        } catch (Exception e2) {
            Log.e(TAG, "Error playing iqama", e2);
            stopCurrentSound();
            releaseWakeLock();
        }
    }

    private static void releaseWakeLock() {
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

    private static void setVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(MimeTypes.BASE_TYPE_AUDIO);
        if (audioManager == null || useMediaVolume(context)) {
            return;
        }
        audioManager.setStreamVolume(4, audioManager.getStreamMaxVolume(4), 0);
    }

    private void showNotification(Context context, String str) {
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            if (notificationManager == null) {
                return;
            }
            String str2 = "🕌 إقامة صلاة " + str;
            String str3 = "حان وقت إقامة صلاة " + str;
            NotificationCompat.Builder visibility = new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(str2).setContentText(str3).setStyle(new NotificationCompat.BigTextStyle().bigText(str3)).setPriority(1).setCategory(NotificationCompat.CATEGORY_ALARM).setAutoCancel(true).setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, (Class<?>) MainActivity.class), 201326592)).setSound(null).setVisibility(1);
            if (context.getSharedPreferences("SalatakPrefs", 0).getBoolean("vibration_enabled", true)) {
                visibility.setVibrate(new long[]{0, 500, 200, 500});
            }
            notificationManager.notify(("iqama_" + str).hashCode(), visibility.build());
            Log.d(TAG, "Iqama notification shown: " + str2);
        } catch (Exception e2) {
            Log.e(TAG, "Error showing iqama notification", e2);
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
                Log.d(TAG, "Iqama MediaPlayer released");
            } catch (Exception e2) {
                Log.e(TAG, "Error stopping iqama sound", e2);
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
                vibrator.vibrate(new long[]{0, 500, 200, 500}, -1);
            }
        } catch (Exception e2) {
            Log.e(TAG, "Error vibrating", e2);
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "=== Iqama Alarm Received ===");
        try {
            String stringExtra = intent.getStringExtra("prayer_name");
            String stringExtra2 = intent.getStringExtra("prayer_key");
            if (stringExtra != null && stringExtra2 != null) {
                if (!context.getSharedPreferences("SalatakPrefs", 0).getBoolean(stringExtra2.concat("_iqama_enabled"), false)) {
                    Log.d(TAG, "Iqama disabled for " + stringExtra2 + ", skipping");
                    return;
                }
                Log.d(TAG, "Iqama for: ".concat(stringExtra));
                acquireWakeLock(context);
                createNotificationChannel(context);
                showNotification(context, stringExtra);
                playIqamaSound(context);
                vibrate(context);
                return;
            }
            Log.e(TAG, "Missing prayer info!");
        } catch (Exception e2) {
            Log.e(TAG, "Error in onReceive", e2);
        }
    }
}
