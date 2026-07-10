package com.salatak.app;

/* loaded from: classes2.dex */
public final /* synthetic */ class y implements Runnable {
    public final /* synthetic */ int b;
    public final /* synthetic */ AzkarDisplayActivity c;

    public /* synthetic */ y(AzkarDisplayActivity azkarDisplayActivity, int i2) {
        this.b = i2;
        this.c = azkarDisplayActivity;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                this.c.lambda$closeWithAnimation$9();
                break;
            case 1:
                this.c.lambda$animateEntrance$3();
                break;
            case 2:
                this.c.lambda$onCreate$2();
                break;
            default:
                this.c.finish();
                break;
        }
    }
}
