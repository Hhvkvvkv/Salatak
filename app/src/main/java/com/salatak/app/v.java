package com.salatak.app;

import android.media.MediaPlayer;
import android.widget.ImageView;

/* loaded from: classes2.dex */
public final /* synthetic */ class v implements MediaPlayer.OnCompletionListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f526a;
    public final /* synthetic */ AzkarDetailActivity b;
    public final /* synthetic */ ImageView c;

    public /* synthetic */ v(AzkarDetailActivity azkarDetailActivity, ImageView imageView, int i2) {
        this.f526a = i2;
        this.b = azkarDetailActivity;
        this.c = imageView;
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public final void onCompletion(MediaPlayer mediaPlayer) {
        switch (this.f526a) {
            case 0:
                this.b.lambda$playLocalAudio$6(this.c, mediaPlayer);
                break;
            default:
                this.b.lambda$playOnlineAudio$3(this.c, mediaPlayer);
                break;
        }
    }
}
