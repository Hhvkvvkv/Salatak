package com.salatak.app;

/* loaded from: classes2.dex */
public final /* synthetic */ class c0 implements Runnable {
    public final /* synthetic */ int b;
    public final /* synthetic */ AzkarForegroundService c;

    public /* synthetic */ c0(AzkarForegroundService azkarForegroundService, int i2) {
        this.b = i2;
        this.c = azkarForegroundService;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                this.c.lambda$showComboBoxOverlay$2();
                break;
            case 1:
                this.c.lambda$dismissWithAnimation$9();
                break;
            default:
                this.c.lambda$dismissWithAnimation$8();
                break;
        }
    }
}
