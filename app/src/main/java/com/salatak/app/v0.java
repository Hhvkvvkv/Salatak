package com.salatak.app;

import android.media.MediaPlayer;
import com.salatak.app.MuezzinActivity;

/* loaded from: classes2.dex */
public final /* synthetic */ class v0 implements MediaPlayer.OnCompletionListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f527a;
    public final /* synthetic */ Object b;

    public /* synthetic */ v0(int i2, Object obj) {
        this.f527a = i2;
        this.b = obj;
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public final void onCompletion(MediaPlayer mediaPlayer) {
        switch (this.f527a) {
            case 0:
                ((MuezzinActivity.DownloadsAdapter) this.b).lambda$getView$2(mediaPlayer);
                break;
            case 1:
                ((MuezzinActivity.MuezzinAdapter) this.b).lambda$getView$1(mediaPlayer);
                break;
            case 2:
                ((AdhanAlarmReceiver) this.b).lambda$playDuaAfterAdhan$5(mediaPlayer);
                break;
            case 3:
                ((ChatActivity) this.b).lambda$playAudio$14(mediaPlayer);
                break;
            case 4:
                ((PrayerReminderReceiver) this.b).lambda$playReminderSound$0(mediaPlayer);
                break;
            default:
                ((PrayerSettingsActivity) this.b).lambda$playTestSound$24(mediaPlayer);
                break;
        }
    }
}
