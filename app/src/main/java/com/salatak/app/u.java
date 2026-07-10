package com.salatak.app;

import android.media.MediaPlayer;

/* loaded from: classes2.dex */
public final /* synthetic */ class u implements MediaPlayer.OnErrorListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f524a;

    public /* synthetic */ u(int i2) {
        this.f524a = i2;
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public final boolean onError(MediaPlayer mediaPlayer, int i2, int i3) {
        boolean lambda$playAudioAzkar$1;
        boolean lambda$playIqamaSound$1;
        boolean lambda$playAzkarAudio$1;
        switch (this.f524a) {
            case 0:
                lambda$playAudioAzkar$1 = AzkarBroadcastReceiver.lambda$playAudioAzkar$1(mediaPlayer, i2, i3);
                return lambda$playAudioAzkar$1;
            case 1:
                lambda$playIqamaSound$1 = IqamaReceiver.lambda$playIqamaSound$1(mediaPlayer, i2, i3);
                return lambda$playIqamaSound$1;
            default:
                lambda$playAzkarAudio$1 = SettingsActivity.lambda$playAzkarAudio$1(mediaPlayer, i2, i3);
                return lambda$playAzkarAudio$1;
        }
    }
}
