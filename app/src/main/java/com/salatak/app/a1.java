package com.salatak.app;

import com.salatak.app.MuezzinActivity;

/* loaded from: classes2.dex */
public final /* synthetic */ class a1 implements Runnable {
    public final /* synthetic */ int b;
    public final /* synthetic */ MuezzinActivity.MuezzinAdapter c;

    public /* synthetic */ a1(MuezzinActivity.MuezzinAdapter muezzinAdapter, int i2) {
        this.b = i2;
        this.c = muezzinAdapter;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                this.c.lambda$getView$2();
                break;
            default:
                this.c.lambda$getView$0();
                break;
        }
    }
}
