package com.salatak.app;

import android.media.MediaPlayer;

/* loaded from: classes2.dex */
public final /* synthetic */ class x0 implements MediaPlayer.OnPreparedListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f540a;

    public /* synthetic */ x0(int i2) {
        this.f540a = i2;
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public final void onPrepared(MediaPlayer mediaPlayer) {
        switch (this.f540a) {
            case 0:
                mediaPlayer.start();
                break;
            case 1:
                mediaPlayer.start();
                break;
            case 2:
                mediaPlayer.start();
                break;
            case 3:
                mediaPlayer.start();
                break;
            default:
                mediaPlayer.start();
                break;
        }
    }
}
