package com.salatak.app;

import android.widget.ImageView;

/* loaded from: classes2.dex */
public final /* synthetic */ class d0 implements Runnable {
    public final /* synthetic */ int b;
    public final /* synthetic */ AzkarForegroundService c;
    public final /* synthetic */ ImageView d;

    public /* synthetic */ d0(AzkarForegroundService azkarForegroundService, ImageView imageView, int i2) {
        this.b = i2;
        this.c = azkarForegroundService;
        this.d = imageView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                this.c.lambda$animateFlowers$4(this.d);
                break;
            case 1:
                this.c.lambda$animateFlowers$5(this.d);
                break;
            default:
                this.c.lambda$animateFlowers$6(this.d);
                break;
        }
    }
}
