package com.salatak.app;

import android.media.MediaPlayer;

/* loaded from: classes2.dex */
public final /* synthetic */ class d implements MediaPlayer.OnErrorListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f458a;
    public final /* synthetic */ AdhanAlarmReceiver b;

    public /* synthetic */ d(AdhanAlarmReceiver adhanAlarmReceiver, int i2) {
        this.f458a = i2;
        this.b = adhanAlarmReceiver;
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public final boolean onError(MediaPlayer mediaPlayer, int i2, int i3) {
        boolean lambda$playAdhanSound$3;
        boolean lambda$playDuaAfterAdhan$6;
        switch (this.f458a) {
            case 0:
                lambda$playAdhanSound$3 = this.b.lambda$playAdhanSound$3(mediaPlayer, i2, i3);
                return lambda$playAdhanSound$3;
            default:
                lambda$playDuaAfterAdhan$6 = this.b.lambda$playDuaAfterAdhan$6(mediaPlayer, i2, i3);
                return lambda$playDuaAfterAdhan$6;
        }
    }
}
