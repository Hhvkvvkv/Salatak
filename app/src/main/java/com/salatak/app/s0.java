package com.salatak.app;

import android.widget.ImageView;
import android.widget.ProgressBar;
import com.salatak.app.AdhanSoundsData;

/* loaded from: classes2.dex */
public final /* synthetic */ class s0 implements Runnable {
    public final /* synthetic */ int b = 1;
    public final /* synthetic */ MuezzinActivity c;
    public final /* synthetic */ ProgressBar d;

    /* renamed from: e, reason: collision with root package name */
    public final /* synthetic */ ImageView f518e;

    /* renamed from: f, reason: collision with root package name */
    public final /* synthetic */ AdhanSoundsData.AdhanSound f519f;

    public /* synthetic */ s0(MuezzinActivity muezzinActivity, ProgressBar progressBar, ImageView imageView, AdhanSoundsData.AdhanSound adhanSound) {
        this.c = muezzinActivity;
        this.d = progressBar;
        this.f518e = imageView;
        this.f519f = adhanSound;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                this.c.lambda$downloadAdhan$8(this.f519f, this.d, this.f518e);
                break;
            default:
                this.c.lambda$downloadAdhan$6(this.d, this.f518e, this.f519f);
                break;
        }
    }

    public /* synthetic */ s0(MuezzinActivity muezzinActivity, AdhanSoundsData.AdhanSound adhanSound, ProgressBar progressBar, ImageView imageView) {
        this.c = muezzinActivity;
        this.f519f = adhanSound;
        this.d = progressBar;
        this.f518e = imageView;
    }
}
