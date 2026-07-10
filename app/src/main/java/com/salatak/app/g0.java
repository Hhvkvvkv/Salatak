package com.salatak.app;

/* loaded from: classes2.dex */
public final /* synthetic */ class g0 implements Runnable {
    public final /* synthetic */ int b;
    public final /* synthetic */ ChatActivity c;

    public /* synthetic */ g0(ChatActivity chatActivity, int i2) {
        this.b = i2;
        this.c = chatActivity;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                this.c.lambda$sendTextMessage$6();
                break;
            case 1:
                this.c.lambda$loadMessages$5();
                break;
            case 2:
                this.c.lambda$sendVoiceMessage$9();
                break;
            default:
                this.c.lambda$sendVoiceMessage$11();
                break;
        }
    }
}
