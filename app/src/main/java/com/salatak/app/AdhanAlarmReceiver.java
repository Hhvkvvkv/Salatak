package com.salatak.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.media3.common.MimeTypes;
import androidx.media3.datasource.cache.ContentMetadata;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import java.io.File;
import java.io.IOException;

/* loaded from: classes2.dex */
public class AdhanAlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "adhan_alarm_channel";
    private static final String TAG = "AdhanAlarmReceiver";
    private static MediaPlayer mediaPlayer = null;
    private static SensorEventListener proximitySensorListener = null;
    private static Context sContext = null;
    private static String sPrayerKey = null;
    private static float sVolumeFloat = 1.0f;
    private static SensorManager sensorManager;
    private static SensorEventListener shakeSensorListener;
    private static SensorEventListener tapSensorListener;
    private static BroadcastReceiver volumeReceiver;
    private static PowerManager.WakeLock wakeLock;
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static boolean proximityTriggered = false;
    private static boolean shakeTriggered = false;
    private static boolean tapTriggered = false;

    /* renamed from: com.salatak.app.AdhanAlarmReceiver$1, reason: invalid class name */
    public class AnonymousClass1 implements SensorEventListener {
        final /* synthetic */ float val$threshold;
        private int readingCount = 0;
        private long nearStartTime = 0;

        public AnonymousClass1(float f2) {
            this.val$threshold = f2;
        }

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i2) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (!AdhanAlarmReceiver.proximityTriggered && sensorEvent.sensor.getType() == 8) {
                boolean z2 = true;
                int i2 = this.readingCount + 1;
                this.readingCount = i2;
                float f2 = sensorEvent.values[0];
                if (f2 >= this.val$threshold && f2 != 0.0f) {
                    z2 = false;
                }
                if (i2 <= 2) {
                    Log.d(AdhanAlarmReceiver.TAG, "Proximity initial reading #" + this.readingCount + ": dist=" + f2 + " near=" + z2);
                    return;
                }
                if (!z2) {
                    if (this.nearStartTime != 0) {
                        Log.d(AdhanAlarmReceiver.TAG, "Proximity: far again, dist=" + f2);
                    }
                    this.nearStartTime = 0L;
                    return;
                }
                if (this.nearStartTime == 0) {
                    this.nearStartTime = System.currentTimeMillis();
                    Log.d(AdhanAlarmReceiver.TAG, "Proximity: near detected, dist=" + f2);
                    return;
                }
                long currentTimeMillis = System.currentTimeMillis() - this.nearStartTime;
                if (currentTimeMillis >= 250) {
                    Log.d(AdhanAlarmReceiver.TAG, "Proximity: near for " + currentTimeMillis + "ms, stopping adhan");
                    AdhanAlarmReceiver.proximityTriggered = true;
                    AdhanAlarmReceiver.mainHandler.post(new f(0));
                }
            }
        }
    }

    /* renamed from: com.salatak.app.AdhanAlarmReceiver$2, reason: invalid class name */
    public class AnonymousClass2 implements SensorEventListener {
        private static final float SHAKE_THRESHOLD = 12.0f;
        private long lastShakeTime = 0;
        private int shakeCount = 0;

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i2) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (!AdhanAlarmReceiver.shakeTriggered && sensorEvent.sensor.getType() == 1) {
                float[] fArr = sensorEvent.values;
                float f2 = fArr[0];
                float f3 = fArr[1];
                float f4 = fArr[2];
                if (((float) Math.sqrt((f4 * f4) + ((f3 * f3) + (f2 * f2)))) - 9.80665f > SHAKE_THRESHOLD) {
                    long currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - this.lastShakeTime < 1000) {
                        int i2 = this.shakeCount + 1;
                        this.shakeCount = i2;
                        if (i2 >= 2) {
                            Log.d(AdhanAlarmReceiver.TAG, "Shake detected, stopping adhan");
                            AdhanAlarmReceiver.shakeTriggered = true;
                            AdhanAlarmReceiver.mainHandler.post(new f(1));
                        }
                    } else {
                        this.shakeCount = 1;
                    }
                    this.lastShakeTime = currentTimeMillis;
                }
            }
        }
    }

    /* renamed from: com.salatak.app.AdhanAlarmReceiver$3, reason: invalid class name */
    public class AnonymousClass3 implements SensorEventListener {
        private long lastTapTime = 0;
        private int tapCount = 0;
        private float lastMagnitude = 0.0f;
        private boolean wasStill = true;

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int i2) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (!AdhanAlarmReceiver.tapTriggered && sensorEvent.sensor.getType() == 1) {
                float[] fArr = sensorEvent.values;
                float f2 = fArr[0];
                float f3 = fArr[1];
                float f4 = fArr[2];
                float f5 = f4 * f4;
                float sqrt = (float) Math.sqrt(f5 + (f3 * f3) + (f2 * f2));
                float abs = Math.abs(sqrt - this.lastMagnitude);
                this.lastMagnitude = sqrt;
                if (abs <= 3.0f || abs >= 10.0f || !this.wasStill) {
                    if (abs < 0.5f) {
                        this.wasStill = true;
                        return;
                    }
                    return;
                }
                long currentTimeMillis = System.currentTimeMillis();
                long j2 = currentTimeMillis - this.lastTapTime;
                if (j2 < 800 && j2 > 100) {
                    int i2 = this.tapCount + 1;
                    this.tapCount = i2;
                    if (i2 >= 2) {
                        Log.d(AdhanAlarmReceiver.TAG, "Back tap detected (" + this.tapCount + " taps), stopping adhan");
                        AdhanAlarmReceiver.tapTriggered = true;
                        AdhanAlarmReceiver.mainHandler.post(new f(2));
                    }
                } else if (j2 >= 800) {
                    this.tapCount = 1;
                }
                this.lastTapTime = currentTimeMillis;
                this.wasStill = false;
            }
        }
    }

    private void acquireWakeLock(Context context) {
        try {
            PowerManager powerManager = (PowerManager) context.getSystemService("power");
            if (powerManager != null) {
                PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(268435457, "Salatak:AdhanAlarm");
                wakeLock = newWakeLock;
                newWakeLock.acquire(600000L);
                Log.d(TAG, "WakeLock acquired");
            }
        } catch (Exception e2) {
            Log.e(TAG, "Error acquiring WakeLock", e2);
        }
    }

    private static AudioAttributes buildAudioAttributes(Context context) {
        return useMediaVolume(context) ? new AudioAttributes.Builder().setUsage(1).setContentType(2).build() : new AudioAttributes.Builder().setUsage(4).setContentType(4).build();
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel a2 = a.a();
            a2.setDescription("تنبيهات الأذان عند دخول وقت الصلاة");
            a2.enableVibration(true);
            a2.setSound(null, null);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(a2);
            }
        }
    }

    private String getNowSoundFileName(String str) {
        str.getClass();
        switch (str) {
            case "sunrise":
                return "gom3anow.mp3";
            case "asr":
                return "asrnow.mp3";
            case "fajr":
                return "fagenow.mp3";
            case "isha":
                return "eshaanow.mp3";
            case "dhuhr":
                return "zohrnow.mp3";
            case "maghrib":
                return "maghribnow.mp3";
            default:
                return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playAdhanSound$2(Context context, String str, MediaPlayer mediaPlayer2) {
        Log.d(TAG, "Adhan playback completed");
        stopCurrentSound();
        if (!context.getSharedPreferences("SalatakPrefs", 0).getBoolean(str + "_dua_after_adhan_enabled", false)) {
            releaseWakeLock();
            return;
        }
        Log.d(TAG, "Playing dua after adhan for " + str);
        playDuaAfterAdhan(context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$playAdhanSound$3(MediaPlayer mediaPlayer2, int i2, int i3) {
        Log.e(TAG, "MediaPlayer error: what=" + i2 + ", extra=" + i3);
        stopCurrentSound();
        releaseWakeLock();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$playAdhanSound$4(Context context, MediaPlayer mediaPlayer2) {
        if (!useMediaVolume(context)) {
            float f2 = sVolumeFloat;
            mediaPlayer2.setVolume(f2, f2);
        }
        mediaPlayer2.start();
        setVolume(context);
        Log.d(TAG, "Adhan started playing (volume=" + sVolumeFloat + ")");
        registerVolumeReceiver(context);
        registerProximitySensor(context);
        registerShakeSensor(context);
        registerTapSensor(context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playAnnouncementThenAdhan$0(Context context, String str, MediaPlayer mediaPlayer2) {
        Log.d(TAG, "Announcement playback completed, now playing adhan sound");
        stopCurrentSound();
        playAdhanSound(context, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$playAnnouncementThenAdhan$1(Context context, String str, MediaPlayer mediaPlayer2, int i2, int i3) {
        Log.e(TAG, "Announcement MediaPlayer error: what=" + i2 + ", extra=" + i3);
        stopCurrentSound();
        playAdhanSound(context, str);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playDuaAfterAdhan$5(MediaPlayer mediaPlayer2) {
        Log.d(TAG, "Dua after adhan playback completed");
        stopCurrentSound();
        releaseWakeLock();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$playDuaAfterAdhan$6(MediaPlayer mediaPlayer2, int i2, int i3) {
        Log.e(TAG, "Dua MediaPlayer error: what=" + i2 + ", extra=" + i3);
        stopCurrentSound();
        releaseWakeLock();
        return true;
    }

    private void playAdhanSound(final Context context, String str) {
        String str2;
        File file;
        try {
            try {
                String string = context.getSharedPreferences("SalatakPrefs", 0).getString(str + "_adhan_sound_id", "");
                if (string.isEmpty()) {
                    Log.d(TAG, "No adhan sound selected, using default notification");
                    releaseWakeLock();
                    return;
                }
                stopCurrentSound();
                mediaPlayer = new MediaPlayer();
                File file2 = new File(context.getFilesDir(), "adhan_sounds/" + string + ".mp3");
                boolean startsWith = string.startsWith(ContentMetadata.KEY_CUSTOM_PREFIX);
                if (startsWith) {
                    str2 = "No URL found for adhan: ";
                    file = new File(context.getFilesDir(), "adhan_sounds/".concat(string));
                } else {
                    str2 = "No URL found for adhan: ";
                    file = null;
                }
                if (startsWith && file != null && file.exists()) {
                    mediaPlayer.setDataSource(file.getAbsolutePath());
                    Log.d(TAG, "Playing custom adhan from: " + file.getAbsolutePath());
                } else if (file2.exists()) {
                    mediaPlayer.setDataSource(file2.getAbsolutePath());
                    Log.d(TAG, "Playing adhan from local file: " + file2.getAbsolutePath());
                } else {
                    try {
                        AssetFileDescriptor openFd = context.getAssets().openFd("adhan_sounds/" + string + ".mp3");
                        mediaPlayer.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
                        openFd.close();
                        Log.d(TAG, "Playing adhan from bundled asset: ".concat(string));
                    } catch (IOException unused) {
                        String urlById = AdhanSoundsData.getUrlById(string);
                        if (urlById == null || urlById.isEmpty()) {
                            Log.e(TAG, str2.concat(string));
                            releaseWakeLock();
                            return;
                        } else {
                            mediaPlayer.setDataSource(urlById);
                            Log.d(TAG, "Streaming adhan from URL: ".concat(urlById));
                        }
                    }
                }
                setVolume(context);
                mediaPlayer.setAudioAttributes(buildAudioAttributes(context));
                mediaPlayer.setLooping(false);
                mediaPlayer.setOnCompletionListener(new c(this, context, str, 0));
                mediaPlayer.setOnErrorListener(new d(this, 0));
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.salatak.app.e
                    @Override // android.media.MediaPlayer.OnPreparedListener
                    public final void onPrepared(MediaPlayer mediaPlayer2) {
                        AdhanAlarmReceiver.lambda$playAdhanSound$4(context, mediaPlayer2);
                    }
                });
            } catch (IOException e2) {
                Log.e(TAG, "IOException while playing adhan", e2);
                stopCurrentSound();
                releaseWakeLock();
            }
        } catch (Exception e3) {
            Log.e(TAG, "Exception while playing adhan", e3);
            stopCurrentSound();
            releaseWakeLock();
        }
    }

    private void playAnnouncementThenAdhan(final Context context, final String str) {
        String nowSoundFileName = getNowSoundFileName(str);
        if (nowSoundFileName == null) {
            Log.d(TAG, "No announcement file for prayer: " + str + ", playing adhan directly");
            playAdhanSound(context, str);
            return;
        }
        try {
            stopCurrentSound();
            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor openFd = context.getAssets().openFd("prayer_reminders/".concat(nowSoundFileName));
            mediaPlayer.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
            openFd.close();
            setVolume(context);
            mediaPlayer.setAudioAttributes(buildAudioAttributes(context));
            mediaPlayer.setLooping(false);
            mediaPlayer.setOnCompletionListener(new c(this, context, str, 1));
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.salatak.app.b
                @Override // android.media.MediaPlayer.OnErrorListener
                public final boolean onError(MediaPlayer mediaPlayer2, int i2, int i3) {
                    boolean lambda$playAnnouncementThenAdhan$1;
                    lambda$playAnnouncementThenAdhan$1 = AdhanAlarmReceiver.this.lambda$playAnnouncementThenAdhan$1(context, str, mediaPlayer2, i2, i3);
                    return lambda$playAnnouncementThenAdhan$1;
                }
            });
            mediaPlayer.prepare();
            mediaPlayer.start();
            Log.d(TAG, "Playing announcement: ".concat(nowSoundFileName));
        } catch (IOException e2) {
            Log.e(TAG, "IOException playing announcement, falling back to adhan", e2);
            stopCurrentSound();
            playAdhanSound(context, str);
        } catch (Exception e3) {
            Log.e(TAG, "Exception playing announcement, falling back to adhan", e3);
            stopCurrentSound();
            playAdhanSound(context, str);
        }
    }

    private void playDuaAfterAdhan(Context context) {
        try {
            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor openFd = context.getAssets().openFd("prayer_reminders/dua_after_adhan.m4a");
            mediaPlayer.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
            openFd.close();
            setVolume(context);
            mediaPlayer.setAudioAttributes(buildAudioAttributes(context));
            mediaPlayer.setLooping(false);
            mediaPlayer.setOnCompletionListener(new v0(2, this));
            mediaPlayer.setOnErrorListener(new d(this, 1));
            mediaPlayer.prepare();
            mediaPlayer.start();
            Log.d(TAG, "Playing dua after adhan");
        } catch (Exception e2) {
            Log.e(TAG, "Error playing dua after adhan", e2);
            stopCurrentSound();
            releaseWakeLock();
        }
    }

    private static void registerProximitySensor(Context context) {
        if (context.getSharedPreferences("SalatakPrefs", 0).getBoolean("flip_to_silence_enabled", true)) {
            proximityTriggered = false;
            try {
                SensorManager sensorManager2 = (SensorManager) context.getSystemService("sensor");
                sensorManager = sensorManager2;
                if (sensorManager2 == null) {
                    return;
                }
                Sensor defaultSensor = sensorManager2.getDefaultSensor(8);
                if (defaultSensor == null) {
                    Log.d(TAG, "No proximity sensor available");
                    return;
                }
                float maximumRange = defaultSensor.getMaximumRange();
                float min = Math.min(5.0f, 0.5f * maximumRange);
                Log.d(TAG, "Proximity sensor: max=" + maximumRange + ", threshold=" + min);
                AnonymousClass1 anonymousClass1 = new AnonymousClass1(min);
                proximitySensorListener = anonymousClass1;
                sensorManager.registerListener(anonymousClass1, defaultSensor, 0);
                Log.d(TAG, "Proximity sensor registered for flip-to-silence");
            } catch (Exception e2) {
                Log.e(TAG, "Error registering proximity sensor", e2);
            }
        }
    }

    private static void registerShakeSensor(Context context) {
        if (context.getSharedPreferences("SalatakPrefs", 0).getBoolean("shake_to_silence_enabled", false)) {
            shakeTriggered = false;
            try {
                if (sensorManager == null) {
                    sensorManager = (SensorManager) context.getSystemService("sensor");
                }
                SensorManager sensorManager2 = sensorManager;
                if (sensorManager2 == null) {
                    return;
                }
                Sensor defaultSensor = sensorManager2.getDefaultSensor(1);
                if (defaultSensor == null) {
                    Log.d(TAG, "No accelerometer available");
                    return;
                }
                AnonymousClass2 anonymousClass2 = new AnonymousClass2();
                shakeSensorListener = anonymousClass2;
                sensorManager.registerListener(anonymousClass2, defaultSensor, 2);
                Log.d(TAG, "Shake sensor registered for shake-to-silence");
            } catch (Exception e2) {
                Log.e(TAG, "Error registering shake sensor", e2);
            }
        }
    }

    private static void registerTapSensor(Context context) {
        if (context.getSharedPreferences("SalatakPrefs", 0).getBoolean("tap_to_silence_enabled", false)) {
            tapTriggered = false;
            try {
                if (sensorManager == null) {
                    sensorManager = (SensorManager) context.getSystemService("sensor");
                }
                SensorManager sensorManager2 = sensorManager;
                if (sensorManager2 == null) {
                    return;
                }
                Sensor defaultSensor = sensorManager2.getDefaultSensor(1);
                if (defaultSensor == null) {
                    Log.d(TAG, "No accelerometer available for tap detection");
                    return;
                }
                AnonymousClass3 anonymousClass3 = new AnonymousClass3();
                tapSensorListener = anonymousClass3;
                sensorManager.registerListener(anonymousClass3, defaultSensor, 1);
                Log.d(TAG, "Tap sensor registered for tap-to-silence");
            } catch (Exception e2) {
                Log.e(TAG, "Error registering tap sensor", e2);
            }
        }
    }

    private static void registerVolumeReceiver(Context context) {
        if (volumeReceiver == null && context.getSharedPreferences("SalatakPrefs", 0).getBoolean("volume_to_silence_enabled", false)) {
            volumeReceiver = new BroadcastReceiver() { // from class: com.salatak.app.AdhanAlarmReceiver.4
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context2, Intent intent) {
                    if ("android.media.VOLUME_CHANGED_ACTION".equals(intent.getAction())) {
                        Log.d(AdhanAlarmReceiver.TAG, "Volume changed, stopping adhan");
                        AdhanAlarmReceiver.stopCurrentSound();
                    }
                }
            };
            try {
                context.getApplicationContext().registerReceiver(volumeReceiver, new IntentFilter("android.media.VOLUME_CHANGED_ACTION"));
                Log.d(TAG, "Volume receiver registered");
            } catch (Exception e2) {
                Log.e(TAG, "Error registering volume receiver", e2);
            }
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

    private static void setVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(MimeTypes.BASE_TYPE_AUDIO);
        if (audioManager == null) {
            return;
        }
        if (useMediaVolume(context)) {
            sVolumeFloat = 1.0f;
            return;
        }
        int i2 = context.getSharedPreferences("SalatakPrefs", 0).getInt(sPrayerKey + "_adhan_volume", 80);
        sVolumeFloat = ((float) i2) / 100.0f;
        int streamMaxVolume = audioManager.getStreamMaxVolume(4);
        int max = Math.max(1, Math.round(((float) streamMaxVolume) * sVolumeFloat));
        audioManager.setStreamVolume(4, max, 0);
        MediaPlayer mediaPlayer2 = mediaPlayer;
        if (mediaPlayer2 != null) {
            try {
                float f2 = sVolumeFloat;
                mediaPlayer2.setVolume(f2, f2);
            } catch (Exception unused) {
            }
        }
        Log.d(TAG, "Volume set: stream=" + max + "/" + streamMaxVolume + ", player=" + sVolumeFloat + " (" + i2 + "%) for " + sPrayerKey);
    }

    private void showNotification(Context context, String str) {
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            if (notificationManager == null) {
                return;
            }
            String str2 = "🕌 حان وقت أذان " + str;
            String str3 = "حان الآن موعد أذان صلاة " + str;
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
            notificationManager.notify(("adhan_" + str).hashCode(), visibility.build());
            Log.d(TAG, "Adhan notification shown: " + str2);
        } catch (Exception e2) {
            Log.e(TAG, "Error showing adhan notification", e2);
        }
    }

    public static void stopCurrentSound() {
        unregisterVolumeReceiver();
        unregisterProximitySensor();
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
        try {
            PowerManager.WakeLock wakeLock2 = wakeLock;
            if (wakeLock2 == null || !wakeLock2.isHeld()) {
                return;
            }
            wakeLock.release();
            wakeLock = null;
            Log.d(TAG, "WakeLock released from stopCurrentSound");
        } catch (Exception e3) {
            Log.e(TAG, "Error releasing WakeLock from stopCurrentSound", e3);
        }
    }

    private static void unregisterProximitySensor() {
        SensorEventListener sensorEventListener;
        SensorEventListener sensorEventListener2;
        SensorEventListener sensorEventListener3;
        SensorManager sensorManager2 = sensorManager;
        if (sensorManager2 != null && (sensorEventListener3 = proximitySensorListener) != null) {
            try {
                sensorManager2.unregisterListener(sensorEventListener3);
                Log.d(TAG, "Proximity sensor unregistered");
            } catch (Exception e2) {
                Log.e(TAG, "Error unregistering proximity sensor", e2);
            }
            proximitySensorListener = null;
        }
        SensorManager sensorManager3 = sensorManager;
        if (sensorManager3 != null && (sensorEventListener2 = shakeSensorListener) != null) {
            try {
                sensorManager3.unregisterListener(sensorEventListener2);
                Log.d(TAG, "Shake sensor unregistered");
            } catch (Exception e3) {
                Log.e(TAG, "Error unregistering shake sensor", e3);
            }
            shakeSensorListener = null;
        }
        SensorManager sensorManager4 = sensorManager;
        if (sensorManager4 != null && (sensorEventListener = tapSensorListener) != null) {
            try {
                sensorManager4.unregisterListener(sensorEventListener);
                Log.d(TAG, "Tap sensor unregistered");
            } catch (Exception e4) {
                Log.e(TAG, "Error unregistering tap sensor", e4);
            }
            tapSensorListener = null;
        }
        if (proximitySensorListener == null && shakeSensorListener == null && tapSensorListener == null) {
            sensorManager = null;
        }
    }

    private static void unregisterVolumeReceiver() {
        Context context;
        BroadcastReceiver broadcastReceiver = volumeReceiver;
        if (broadcastReceiver == null || (context = sContext) == null) {
            return;
        }
        try {
            context.unregisterReceiver(broadcastReceiver);
            Log.d(TAG, "Volume receiver unregistered");
        } catch (Exception e2) {
            Log.e(TAG, "Error unregistering volume receiver", e2);
        }
        volumeReceiver = null;
    }

    private static boolean useMediaVolume(Context context) {
        return context.getSharedPreferences("SalatakPrefs", 0).getBoolean("use_media_volume", false);
    }

    private void vibrate(Context context) {
        Vibrator vibrator;
        try {
            if (context.getSharedPreferences("SalatakPrefs", 0).getBoolean("vibration_enabled", true) && (vibrator = (Vibrator) context.getSystemService("vibrator")) != null && vibrator.hasVibrator()) {
                vibrator.vibrate(new long[]{0, 500, 200, 500, 200, 500}, -1);
            }
        } catch (Exception e2) {
            Log.e(TAG, "Error vibrating", e2);
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "=== Adhan Alarm Received ===");
        sContext = context.getApplicationContext();
        try {
            String stringExtra = intent.getStringExtra("prayer_name");
            String stringExtra2 = intent.getStringExtra("prayer_key");
            Log.d(TAG, "Prayer: " + stringExtra + ", Key: " + stringExtra2);
            if (stringExtra != null && stringExtra2 != null) {
                sPrayerKey = stringExtra2;
                SharedPreferences sharedPreferences = context.getSharedPreferences("SalatakPrefs", 0);
                if (!sharedPreferences.getBoolean(stringExtra2.concat("_adhan_enabled"), false)) {
                    Log.d(TAG, "Adhan disabled for ".concat(stringExtra2));
                    return;
                }
                boolean z2 = true;
                boolean z3 = sharedPreferences.getBoolean("adhan_enabled", true);
                acquireWakeLock(context);
                if (!"none".equals(sharedPreferences.getString(stringExtra2.concat("_adhan_sound_id"), "")) && z3) {
                    z2 = false;
                }
                createNotificationChannel(context);
                showNotification(context, stringExtra);
                vibrate(context);
                if (sharedPreferences.getBoolean("notification_only_mode", false)) {
                    Log.d(TAG, "Notification-only mode: skipping overlay");
                } else {
                    AdhanOverlayService.showAdhanOverlay(context, stringExtra);
                }
                if (z2) {
                    Log.d(TAG, "Silent mode (no muezzin) — showing overlay only");
                    releaseWakeLock();
                } else {
                    playAnnouncementThenAdhan(context, stringExtra2);
                }
                if (sharedPreferences.getBoolean(stringExtra2.concat("_iqama_enabled"), false)) {
                    int i2 = sharedPreferences.getInt(stringExtra2.concat("_iqama_minutes"), 10);
                    PrayerReminderScheduler.scheduleIqama(context, stringExtra2, stringExtra, i2);
                    Log.d(TAG, "Iqama scheduled for " + stringExtra + " in " + i2 + " minutes");
                }
                try {
                    PrayerReminderScheduler.scheduleAllAlarms(context);
                } catch (Exception e2) {
                    Log.e(TAG, "Error rescheduling alarms", e2);
                }
                Log.d(TAG, "=== Adhan alarm processing complete ===");
                return;
            }
            Log.e(TAG, "Missing prayer info!");
        } catch (Exception e3) {
            Log.e(TAG, "Error in onReceive", e3);
        }
    }
}
