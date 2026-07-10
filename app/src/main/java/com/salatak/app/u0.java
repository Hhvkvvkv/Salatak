package com.salatak.app;

import com.salatak.app.MuezzinActivity;

/* loaded from: classes2.dex */
public final /* synthetic */ class u0 implements Runnable {
    public final /* synthetic */ int b;
    public final /* synthetic */ MuezzinActivity.DownloadsAdapter c;

    public /* synthetic */ u0(MuezzinActivity.DownloadsAdapter downloadsAdapter, int i2) {
        this.b = i2;
        this.c = downloadsAdapter;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                this.c.lambda$getView$3();
                break;
            default:
                this.c.lambda$getView$1();
                break;
        }
    }
}
