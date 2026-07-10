package com.salatak.app;

/* loaded from: classes2.dex */
public final /* synthetic */ class a0 implements Runnable {
    public final /* synthetic */ int b;
    public final /* synthetic */ AzkarDisplayActivity c;
    public final /* synthetic */ String d;

    public /* synthetic */ a0(AzkarDisplayActivity azkarDisplayActivity, String str, int i2) {
        this.b = i2;
        this.c = azkarDisplayActivity;
        this.d = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                this.c.lambda$announceForScreenReader$8(this.d);
                break;
            case 1:
                this.c.lambda$animateEntrance$4(this.d);
                break;
            default:
                this.c.lambda$onCreate$1(this.d);
                break;
        }
    }
}
