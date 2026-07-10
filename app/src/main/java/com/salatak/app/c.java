package com.salatak.app;

import android.content.Context;
import android.media.MediaPlayer;

/* loaded from: classes2.dex */
public final /* synthetic */ class c implements MediaPlayer.OnCompletionListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f454a;
    public final /* synthetic */ AdhanAlarmReceiver b;
    public final /* synthetic */ Context c;
    public final /* synthetic */ String d;

    public /* synthetic */ c(AdhanAlarmReceiver adhanAlarmReceiver, Context context, String str, int i2) {
        this.f454a = i2;
        this.b = adhanAlarmReceiver;
        this.c = context;
        this.d = str;
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public final void onCompletion(MediaPlayer mediaPlayer) {
        switch (this.f454a) {
            case 0:
                this.b.lambda$playAdhanSound$2(this.c, this.d, mediaPlayer);
                break;
            default:
                this.b.lambda$playAnnouncementThenAdhan$0(this.c, this.d, mediaPlayer);
                break;
        }
    }
}
