package com.salatak.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import androidx.core.app.NotificationCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.DefaultLoadControl;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;

/* loaded from: classes2.dex */
public class RadioService extends Service {
    public static final String ACTION_PAUSE = "com.salatak.app.RADIO_PAUSE";
    public static final String ACTION_PLAY = "com.salatak.app.RADIO_PLAY";
    public static final String ACTION_STOP = "com.salatak.app.RADIO_STOP";
    public static final String ACTION_TOGGLE = "com.salatak.app.RADIO_TOGGLE";
    private static final String CHANNEL_ID = "radio_service_channel";
    public static final String EXTRA_STATION_TITLE = "station_title";
    public static final String EXTRA_STREAM_URL = "stream_url";
    private static final int MAX_RETRIES = 5;
    private static final int NOTIFICATION_ID = 9999;
    private static final long RETRY_DELAY_MS = 3000;
    private RadioCallback callback;
    private String currentUrl;
    private ExoPlayer player;
    private String currentTitle = "";
    private int currentState = 1;
    private boolean hasError = false;
    private final IBinder binder = new RadioBinder();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private int retryCount = 0;

    /* renamed from: com.salatak.app.RadioService$1, reason: invalid class name */
    public class AnonymousClass1 implements Player.Listener {
        public AnonymousClass1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPlayerError$0() {
            if (RadioService.this.player == null || RadioService.this.currentUrl == null) {
                return;
            }
            RadioService.this.hasError = false;
            RadioService.this.notifyCallback();
            RadioService radioService = RadioService.this;
            radioService.setMediaAndPrepare(radioService.currentUrl);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onIsPlayingChanged(boolean z2) {
            RadioService.this.updateNotification();
            RadioService.this.notifyCallback();
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPlaybackStateChanged(int i2) {
            RadioService.this.currentState = i2;
            RadioService.this.hasError = false;
            if (i2 == 3) {
                RadioService.this.retryCount = 0;
            }
            RadioService.this.updateNotification();
            RadioService.this.notifyCallback();
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPlayerError(PlaybackException playbackException) {
            RadioService.this.hasError = true;
            RadioService.this.currentState = 1;
            RadioService.this.notifyCallback();
            if (RadioService.this.retryCount >= 5) {
                RadioService.this.updateNotification();
                RadioService.this.notifyCallback();
            } else {
                RadioService.this.retryCount++;
                RadioService.this.mainHandler.postDelayed(new b2(1, this), RadioService.this.retryCount * 3000);
            }
        }
    }

    public class RadioBinder extends Binder {
        public RadioBinder() {
        }

        public RadioService getService() {
            return RadioService.this;
        }
    }

    public interface RadioCallback {
        void onStateChanged(int i2, boolean z2, boolean z3);
    }

    private Notification buildNotification() {
        Intent intent = new Intent(this, (Class<?>) RadioPlayerActivity.class);
        intent.putExtra(EXTRA_STREAM_URL, this.currentUrl);
        intent.putExtra(EXTRA_STATION_TITLE, this.currentTitle);
        intent.setFlags(603979776);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 201326592);
        ExoPlayer exoPlayer = this.player;
        boolean z2 = exoPlayer != null && exoPlayer.isPlaying();
        boolean z3 = this.hasError;
        String str = (!z3 || this.retryCount < 5) ? z3 ? "جاري إعادة المحاولة..." : this.currentState == 2 ? "جاري التحميل..." : z2 ? "يعمل الآن" : "متوقف" : "فشل الاتصال";
        Intent intent2 = new Intent(this, (Class<?>) RadioService.class);
        intent2.setAction(ACTION_TOGGLE);
        PendingIntent service = PendingIntent.getService(this, 1, intent2, 201326592);
        Intent intent3 = new Intent(this, (Class<?>) RadioService.class);
        intent3.setAction(ACTION_STOP);
        return new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.ic_radio).setContentTitle(this.currentTitle).setContentText(str).setContentIntent(activity).setOngoing(z2).setShowWhen(false).setStyle(new NotificationCompat.BigTextStyle().bigText(str)).addAction(z2 ? R.drawable.ic_pause_circle : R.drawable.ic_play_circle, z2 ? "إيقاف" : "تشغيل", service).addAction(R.drawable.ic_close, "إغلاق", PendingIntent.getService(this, 2, intent3, 201326592)).build();
    }

    private String cleanStreamUrl(String str) {
        int indexOf;
        if (str == null) {
            return null;
        }
        String trim = str.trim();
        return ((trim.contains("rj-tok=") || trim.contains("rj-ttl=")) && (indexOf = trim.indexOf(63)) > 0) ? trim.substring(0, indexOf) : trim;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel A = a.A();
            A.setDescription("إشعار تشغيل الإذاعة");
            A.setSound(null, null);
            A.enableVibration(false);
            A.setShowBadge(false);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(A);
            }
        }
    }

    private void initPlayer() {
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer != null) {
            exoPlayer.release();
        }
        ExoPlayer build = new ExoPlayer.Builder(this).setLoadControl(new DefaultLoadControl.Builder().setBufferDurationsMs(15000, 60000, 2500, DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS).setPrioritizeTimeOverSizeThresholds(true).build()).setMediaSourceFactory(new DefaultMediaSourceFactory(new DefaultDataSource.Factory(this, new DefaultHttpDataSource.Factory().setUserAgent("SalatakApp/3.6 (Android)").setConnectTimeoutMs(15000).setReadTimeoutMs(15000).setAllowCrossProtocolRedirects(true)))).build();
        this.player = build;
        build.setWakeMode(2);
        this.player.addListener(new AnonymousClass1());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyCallback$0() {
        RadioCallback radioCallback = this.callback;
        if (radioCallback != null) {
            int i2 = this.currentState;
            ExoPlayer exoPlayer = this.player;
            radioCallback.onStateChanged(i2, exoPlayer != null && exoPlayer.isPlaying(), this.hasError);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyCallback() {
        if (this.callback != null) {
            this.mainHandler.post(new b2(5, this));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setMediaAndPrepare(String str) {
        if (this.player == null) {
            return;
        }
        this.player.setMediaItem(MediaItem.fromUri(Uri.parse(cleanStreamUrl(str))));
        this.player.prepare();
        this.player.setPlayWhenReady(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, buildNotification());
        }
    }

    public String getCurrentTitle() {
        return this.currentTitle;
    }

    public String getCurrentUrl() {
        return this.currentUrl;
    }

    public int getPlayerState() {
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer != null) {
            return exoPlayer.getPlaybackState();
        }
        return 1;
    }

    public boolean hasError() {
        return this.hasError;
    }

    public boolean isPlaying() {
        ExoPlayer exoPlayer = this.player;
        return exoPlayer != null && exoPlayer.isPlaying();
    }

    public boolean isRetrying() {
        int i2;
        return this.hasError && (i2 = this.retryCount) > 0 && i2 < 5;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.binder;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        initPlayer();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        this.mainHandler.removeCallbacksAndMessages(null);
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer != null) {
            exoPlayer.release();
            this.player = null;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x007d, code lost:
    
        return 2;
     */
    @Override // android.app.Service
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onStartCommand(android.content.Intent r5, int r6, int r7) {
        /*
            r4 = this;
            r6 = 2
            if (r5 != 0) goto L4
            return r6
        L4:
            java.lang.String r7 = r5.getAction()
            java.lang.String r0 = "station_title"
            java.lang.String r1 = "stream_url"
            if (r7 != 0) goto L1e
            java.lang.String r7 = r5.getStringExtra(r1)
            java.lang.String r5 = r5.getStringExtra(r0)
            if (r7 == 0) goto L1d
            if (r5 == 0) goto L1d
            r4.playStream(r7, r5)
        L1d:
            return r6
        L1e:
            r2 = -1
            int r3 = r7.hashCode()
            switch(r3) {
                case -1090343963: goto L48;
                case -1090246477: goto L3d;
                case 268922821: goto L32;
                case 558766917: goto L27;
                default: goto L26;
            }
        L26:
            goto L52
        L27:
            java.lang.String r3 = "com.salatak.app.RADIO_PAUSE"
            boolean r7 = r7.equals(r3)
            if (r7 != 0) goto L30
            goto L52
        L30:
            r2 = 3
            goto L52
        L32:
            java.lang.String r3 = "com.salatak.app.RADIO_TOGGLE"
            boolean r7 = r7.equals(r3)
            if (r7 != 0) goto L3b
            goto L52
        L3b:
            r2 = r6
            goto L52
        L3d:
            java.lang.String r3 = "com.salatak.app.RADIO_STOP"
            boolean r7 = r7.equals(r3)
            if (r7 != 0) goto L46
            goto L52
        L46:
            r2 = 1
            goto L52
        L48:
            java.lang.String r3 = "com.salatak.app.RADIO_PLAY"
            boolean r7 = r7.equals(r3)
            if (r7 != 0) goto L51
            goto L52
        L51:
            r2 = 0
        L52:
            switch(r2) {
                case 0: goto L66;
                case 1: goto L62;
                case 2: goto L5e;
                case 3: goto L56;
                default: goto L55;
            }
        L55:
            goto L7d
        L56:
            androidx.media3.exoplayer.ExoPlayer r5 = r4.player
            if (r5 == 0) goto L7d
            r5.pause()
            goto L7d
        L5e:
            r4.togglePlayPause()
            goto L7d
        L62:
            r4.stopPlayback()
            goto L7d
        L66:
            java.lang.String r7 = r5.getStringExtra(r1)
            java.lang.String r5 = r5.getStringExtra(r0)
            if (r7 == 0) goto L76
            if (r5 == 0) goto L76
            r4.playStream(r7, r5)
            goto L7d
        L76:
            androidx.media3.exoplayer.ExoPlayer r5 = r4.player
            if (r5 == 0) goto L7d
            r5.play()
        L7d:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.salatak.app.RadioService.onStartCommand(android.content.Intent, int, int):int");
    }

    public void playStream(String str, String str2) {
        this.currentUrl = str;
        this.currentTitle = str2;
        this.retryCount = 0;
        this.hasError = false;
        startForeground(NOTIFICATION_ID, buildNotification());
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer != null) {
            exoPlayer.stop();
            this.player.clearMediaItems();
            setMediaAndPrepare(str);
        }
        notifyCallback();
    }

    public void setCallback(RadioCallback radioCallback) {
        this.callback = radioCallback;
    }

    public void stopPlayback() {
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer != null) {
            exoPlayer.stop();
            this.player.clearMediaItems();
        }
        stopForeground(true);
        stopSelf();
    }

    public void togglePlayPause() {
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer == null) {
            return;
        }
        if (!this.hasError && exoPlayer.getPlaybackState() != 1) {
            if (this.player.isPlaying()) {
                this.player.pause();
                return;
            } else {
                this.player.play();
                return;
            }
        }
        String str = this.currentUrl;
        if (str != null) {
            this.retryCount = 0;
            this.hasError = false;
            setMediaAndPrepare(str);
            notifyCallback();
        }
    }
}
