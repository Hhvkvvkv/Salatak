package com.salatak.app;

import android.media.MediaPlayer;

/* loaded from: classes2.dex */
public final /* synthetic */ class t implements MediaPlayer.OnCompletionListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f520a;

    public /* synthetic */ t(int i2) {
        this.f520a = i2;
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public final void onCompletion(MediaPlayer mediaPlayer) {
        switch (this.f520a) {
            case 0:
                AzkarBroadcastReceiver.lambda$playAudioAzkar$0(mediaPlayer);
                break;
            case 1:
                IqamaReceiver.lambda$playIqamaSound$0(mediaPlayer);
                break;
            default:
                SettingsActivity.lambda$playAzkarAudio$0(mediaPlayer);
                break;
        }
    }
}
