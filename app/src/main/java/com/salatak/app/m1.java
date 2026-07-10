package com.salatak.app;

import android.view.View;

/* loaded from: classes2.dex */
public final /* synthetic */ class m1 implements View.OnClickListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f492a;
    public final /* synthetic */ PrayerTimesFragment b;
    public final /* synthetic */ String c;
    public final /* synthetic */ String d;

    public /* synthetic */ m1(PrayerTimesFragment prayerTimesFragment, String str, String str2, int i2) {
        this.f492a = i2;
        this.b = prayerTimesFragment;
        this.c = str;
        this.d = str2;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.f492a) {
            case 0:
                this.b.lambda$displayPrayerTimes$4(this.c, this.d, view);
                break;
            default:
                this.b.lambda$buildFallbackView$5(this.c, this.d, view);
                break;
        }
    }
}
