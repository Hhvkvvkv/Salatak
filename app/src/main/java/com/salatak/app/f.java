package com.salatak.app;

/* loaded from: classes2.dex */
public final /* synthetic */ class f implements Runnable {
    public final /* synthetic */ int b;

    public /* synthetic */ f(int i2) {
        this.b = i2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                AdhanAlarmReceiver.stopCurrentSound();
                break;
            case 1:
                AdhanAlarmReceiver.stopCurrentSound();
                break;
            default:
                AdhanAlarmReceiver.stopCurrentSound();
                break;
        }
    }
}
