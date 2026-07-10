package com.salatak.app;

/* loaded from: classes2.dex */
public final /* synthetic */ class g implements Runnable {
    public final /* synthetic */ int b;
    public final /* synthetic */ AdhanOverlayService c;

    public /* synthetic */ g(AdhanOverlayService adhanOverlayService, int i2) {
        this.b = i2;
        this.c = adhanOverlayService;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                this.c.dismissOverlay();
                break;
            default:
                this.c.lambda$dismissOverlay$4();
                break;
        }
    }
}
