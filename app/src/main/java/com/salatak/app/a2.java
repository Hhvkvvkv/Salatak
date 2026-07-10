package com.salatak.app;

/* loaded from: classes2.dex */
public final /* synthetic */ class a2 implements Runnable {
    public final /* synthetic */ int b;
    public final /* synthetic */ QuranPlayerService c;

    public /* synthetic */ a2(QuranPlayerService quranPlayerService, int i2) {
        this.b = i2;
        this.c = quranPlayerService;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                this.c.lambda$notifyStateCallback$0();
                break;
            default:
                this.c.lambda$notifyTrackCallback$1();
                break;
        }
    }
}
