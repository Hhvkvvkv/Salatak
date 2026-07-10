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
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.DefaultLoadControl;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class QuranPlayerService extends Service {
    public static final String ACTION_NEXT = "com.salatak.app.QURAN_NEXT";
    public static final String ACTION_PLAY_PAUSE = "com.salatak.app.QURAN_PLAY_PAUSE";
    public static final String ACTION_PREVIOUS = "com.salatak.app.QURAN_PREVIOUS";
    public static final String ACTION_STOP = "com.salatak.app.QURAN_STOP";
    private static final String CHANNEL_ID = "quran_player_channel";
    private static final int MAX_RETRIES = 3;
    private static final int NOTIFICATION_ID = 9998;
    private static final long RETRY_DELAY_MS = 3000;
    private QuranPlayerCallback callback;
    private String currentUrl;
    private ExoPlayer player;
    private String currentTitle = "";
    private String currentReciter = "";
    private int currentState = 1;
    private boolean hasError = false;
    private ArrayList<String[]> trackList = new ArrayList<>();
    private int currentIndex = 0;
    private final IBinder binder = new QuranBinder();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private int retryCount = 0;

    /* renamed from: com.salatak.app.QuranPlayerService$1, reason: invalid class name */
    public class AnonymousClass1 implements Player.Listener {
        public AnonymousClass1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPlayerError$0() {
            if (QuranPlayerService.this.player == null || QuranPlayerService.this.currentUrl == null) {
                return;
            }
            QuranPlayerService.this.hasError = false;
            QuranPlayerService.this.notifyStateCallback();
            QuranPlayerService quranPlayerService = QuranPlayerService.this;
            quranPlayerService.setMediaAndPrepare(quranPlayerService.currentUrl);
        }

        @Override // androidx.media3.common.Player.Listener
        public void onIsPlayingChanged(boolean z2) {
            QuranPlayerService.this.updateNotification();
            QuranPlayerService.this.notifyStateCallback();
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPlaybackStateChanged(int i2) {
            QuranPlayerService.this.currentState = i2;
            QuranPlayerService.this.hasError = false;
            if (i2 == 3) {
                QuranPlayerService.this.retryCount = 0;
            }
            if (i2 == 4) {
                QuranPlayerService.this.nextTrack();
            } else {
                QuranPlayerService.this.updateNotification();
                QuranPlayerService.this.notifyStateCallback();
            }
        }

        @Override // androidx.media3.common.Player.Listener
        public void onPlayerError(PlaybackException playbackException) {
            QuranPlayerService.this.hasError = true;
            QuranPlayerService.this.currentState = 1;
            QuranPlayerService.this.notifyStateCallback();
            if (QuranPlayerService.this.retryCount >= 3) {
                QuranPlayerService.this.updateNotification();
                QuranPlayerService.this.notifyStateCallback();
            } else {
                QuranPlayerService.this.retryCount++;
                QuranPlayerService.this.mainHandler.postDelayed(new b2(0, this), QuranPlayerService.this.retryCount * 3000);
            }
        }
    }

    public class QuranBinder extends Binder {
        public QuranBinder() {
        }

        public QuranPlayerService getService() {
            return QuranPlayerService.this;
        }
    }

    public interface QuranPlayerCallback {
        void onStateChanged(int i2, boolean z2, boolean z3);

        void onTrackChanged(String str, int i2);
    }

    private Notification buildNotification() {
        Intent intent = new Intent(this, (Class<?>) QuranPlayerActivity.class);
        intent.setFlags(603979776);
        intent.putExtra("reciter_name", this.currentReciter);
        intent.putExtra("surah_name", this.currentTitle);
        intent.putExtra("surah_url", this.currentUrl);
        intent.putExtra("surah_index", this.currentIndex);
        ArrayList<String[]> arrayList = this.trackList;
        if (arrayList != null && !arrayList.isEmpty()) {
            try {
                JSONObject jSONObject = new JSONObject();
                Iterator<String[]> it = this.trackList.iterator();
                while (it.hasNext()) {
                    String[] next = it.next();
                    jSONObject.put(next[0], next[1]);
                }
                intent.putExtra("surahs_json", jSONObject.toString());
            } catch (Exception unused) {
            }
        }
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 201326592);
        ExoPlayer exoPlayer = this.player;
        boolean z2 = exoPlayer != null && exoPlayer.isPlaying();
        Intent intent2 = new Intent(this, (Class<?>) QuranPlayerService.class);
        intent2.setAction(ACTION_PREVIOUS);
        PendingIntent service = PendingIntent.getService(this, 10, intent2, 201326592);
        Intent intent3 = new Intent(this, (Class<?>) QuranPlayerService.class);
        intent3.setAction(ACTION_PLAY_PAUSE);
        PendingIntent service2 = PendingIntent.getService(this, 11, intent3, 201326592);
        Intent intent4 = new Intent(this, (Class<?>) QuranPlayerService.class);
        intent4.setAction(ACTION_NEXT);
        return new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.ic_quran_audio).setContentTitle(this.currentTitle).setContentText(this.currentReciter).setContentIntent(activity).setOngoing(z2).setShowWhen(false).addAction(R.drawable.ic_skip_previous, "السابق", service).addAction(z2 ? R.drawable.ic_pause_circle : R.drawable.ic_play_circle, z2 ? "إيقاف" : "تشغيل", service2).addAction(R.drawable.ic_skip_next, "التالي", PendingIntent.getService(this, 12, intent4, 201326592)).build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel y2 = a.y();
            y2.setDescription("إشعار تشغيل القرآن الكريم");
            y2.setSound(null, null);
            y2.enableVibration(false);
            y2.setShowBadge(false);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(y2);
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
    public /* synthetic */ void lambda$notifyStateCallback$0() {
        QuranPlayerCallback quranPlayerCallback = this.callback;
        if (quranPlayerCallback != null) {
            int i2 = this.currentState;
            ExoPlayer exoPlayer = this.player;
            quranPlayerCallback.onStateChanged(i2, exoPlayer != null && exoPlayer.isPlaying(), this.hasError);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyTrackCallback$1() {
        QuranPlayerCallback quranPlayerCallback = this.callback;
        if (quranPlayerCallback != null) {
            quranPlayerCallback.onTrackChanged(this.currentTitle, this.currentIndex);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyStateCallback() {
        if (this.callback != null) {
            this.mainHandler.post(new a2(this, 0));
        }
    }

    private void notifyTrackCallback() {
        if (this.callback != null) {
            this.mainHandler.post(new a2(this, 1));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setMediaAndPrepare(String str) {
        if (this.player == null) {
            return;
        }
        this.player.setMediaItem(MediaItem.fromUri(Uri.parse(str)));
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

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public long getCurrentPosition() {
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer != null) {
            return exoPlayer.getCurrentPosition();
        }
        return 0L;
    }

    public String getCurrentReciter() {
        return this.currentReciter;
    }

    public String getCurrentTitle() {
        return this.currentTitle;
    }

    public String getCurrentUrl() {
        return this.currentUrl;
    }

    public long getDuration() {
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer == null) {
            return 0L;
        }
        long duration = exoPlayer.getDuration();
        if (duration == C.TIME_UNSET) {
            return 0L;
        }
        return duration;
    }

    public float getPlaybackSpeed() {
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer != null) {
            return exoPlayer.getPlaybackParameters().speed;
        }
        return 1.0f;
    }

    public int getPlayerState() {
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer != null) {
            return exoPlayer.getPlaybackState();
        }
        return 1;
    }

    public ArrayList<String[]> getTrackList() {
        return this.trackList;
    }

    public boolean isPlaying() {
        ExoPlayer exoPlayer = this.player;
        return exoPlayer != null && exoPlayer.isPlaying();
    }

    public void nextTrack() {
        ArrayList<String[]> arrayList = this.trackList;
        if (arrayList == null || arrayList.isEmpty() || this.currentIndex >= this.trackList.size() - 1) {
            return;
        }
        int i2 = this.currentIndex + 1;
        this.currentIndex = i2;
        String[] strArr = this.trackList.get(i2);
        playTrack(strArr[1], strArr[0], this.currentReciter);
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
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0052, code lost:
    
        return 2;
     */
    @Override // android.app.Service
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onStartCommand(android.content.Intent r2, int r3, int r4) {
        /*
            r1 = this;
            r3 = 2
            if (r2 != 0) goto L4
            return r3
        L4:
            java.lang.String r2 = r2.getAction()
            if (r2 != 0) goto Lb
            return r3
        Lb:
            r4 = -1
            int r0 = r2.hashCode()
            switch(r0) {
                case -1188573272: goto L35;
                case -1181378180: goto L2a;
                case 1702537252: goto L1f;
                case 1702700339: goto L14;
                default: goto L13;
            }
        L13:
            goto L3f
        L14:
            java.lang.String r0 = "com.salatak.app.QURAN_STOP"
            boolean r2 = r2.equals(r0)
            if (r2 != 0) goto L1d
            goto L3f
        L1d:
            r4 = 3
            goto L3f
        L1f:
            java.lang.String r0 = "com.salatak.app.QURAN_NEXT"
            boolean r2 = r2.equals(r0)
            if (r2 != 0) goto L28
            goto L3f
        L28:
            r4 = r3
            goto L3f
        L2a:
            java.lang.String r0 = "com.salatak.app.QURAN_PLAY_PAUSE"
            boolean r2 = r2.equals(r0)
            if (r2 != 0) goto L33
            goto L3f
        L33:
            r4 = 1
            goto L3f
        L35:
            java.lang.String r0 = "com.salatak.app.QURAN_PREVIOUS"
            boolean r2 = r2.equals(r0)
            if (r2 != 0) goto L3e
            goto L3f
        L3e:
            r4 = 0
        L3f:
            switch(r4) {
                case 0: goto L4f;
                case 1: goto L4b;
                case 2: goto L47;
                case 3: goto L43;
                default: goto L42;
            }
        L42:
            goto L52
        L43:
            r1.stopPlayback()
            goto L52
        L47:
            r1.nextTrack()
            goto L52
        L4b:
            r1.togglePlayPause()
            goto L52
        L4f:
            r1.previousTrack()
        L52:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.salatak.app.QuranPlayerService.onStartCommand(android.content.Intent, int, int):int");
    }

    public void playTrack(String str, String str2, String str3) {
        this.currentUrl = str;
        this.currentTitle = str2;
        this.currentReciter = str3;
        this.retryCount = 0;
        this.hasError = false;
        startForeground(NOTIFICATION_ID, buildNotification());
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer != null) {
            exoPlayer.stop();
            this.player.clearMediaItems();
            setMediaAndPrepare(str);
        }
        notifyStateCallback();
        notifyTrackCallback();
    }

    public void previousTrack() {
        int i2;
        ArrayList<String[]> arrayList = this.trackList;
        if (arrayList == null || arrayList.isEmpty() || (i2 = this.currentIndex) <= 0) {
            return;
        }
        int i3 = i2 - 1;
        this.currentIndex = i3;
        String[] strArr = this.trackList.get(i3);
        playTrack(strArr[1], strArr[0], this.currentReciter);
    }

    public void seekTo(long j2) {
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer != null) {
            exoPlayer.seekTo(j2);
        }
    }

    public void setCallback(QuranPlayerCallback quranPlayerCallback) {
        this.callback = quranPlayerCallback;
    }

    public void setPlaybackSpeed(float f2) {
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer != null) {
            exoPlayer.setPlaybackParameters(new PlaybackParameters(f2));
        }
    }

    public void setTrackList(ArrayList<String[]> arrayList, int i2) {
        this.trackList = arrayList;
        this.currentIndex = i2;
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
            notifyStateCallback();
        }
    }
}
