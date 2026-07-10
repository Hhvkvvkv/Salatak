package com.salatak.app;

import android.media.MediaPlayer;
import com.salatak.app.MuezzinActivity;

/* loaded from: classes2.dex */
public final /* synthetic */ class w0 implements MediaPlayer.OnErrorListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f536a;
    public final /* synthetic */ Object b;

    public /* synthetic */ w0(int i2, Object obj) {
        this.f536a = i2;
        this.b = obj;
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public final boolean onError(MediaPlayer mediaPlayer, int i2, int i3) {
        boolean lambda$getView$4;
        boolean lambda$getView$3;
        boolean lambda$playReminderSound$1;
        boolean lambda$playTestSound$25;
        switch (this.f536a) {
            case 0:
                lambda$getView$4 = ((MuezzinActivity.DownloadsAdapter) this.b).lambda$getView$4(mediaPlayer, i2, i3);
                return lambda$getView$4;
            case 1:
                lambda$getView$3 = ((MuezzinActivity.MuezzinAdapter) this.b).lambda$getView$3(mediaPlayer, i2, i3);
                return lambda$getView$3;
            case 2:
                lambda$playReminderSound$1 = ((PrayerReminderReceiver) this.b).lambda$playReminderSound$1(mediaPlayer, i2, i3);
                return lambda$playReminderSound$1;
            default:
                lambda$playTestSound$25 = ((PrayerSettingsActivity) this.b).lambda$playTestSound$25(mediaPlayer, i2, i3);
                return lambda$playTestSound$25;
        }
    }
}
