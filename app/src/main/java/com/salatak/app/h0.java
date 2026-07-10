package com.salatak.app;

/* loaded from: classes2.dex */
public final /* synthetic */ class h0 implements Runnable {
    public final /* synthetic */ int b;
    public final /* synthetic */ ChatActivity c;
    public final /* synthetic */ Exception d;

    public /* synthetic */ h0(ChatActivity chatActivity, Exception exc, int i2) {
        this.b = i2;
        this.c = chatActivity;
        this.d = exc;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                this.c.lambda$sendTextMessage$7(this.d);
                break;
            default:
                this.c.lambda$sendVoiceMessage$10(this.d);
                break;
        }
    }
}
