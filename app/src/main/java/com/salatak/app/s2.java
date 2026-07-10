package com.salatak.app;

/* loaded from: classes2.dex */
public final /* synthetic */ class s2 implements Runnable {
    public final /* synthetic */ int b;
    public final /* synthetic */ StoryReaderActivity c;

    public /* synthetic */ s2(StoryReaderActivity storyReaderActivity, int i2) {
        this.b = i2;
        this.c = storyReaderActivity;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                this.c.lambda$loadFromNetwork$5();
                break;
            case 1:
                this.c.lambda$loadFromNetwork$6();
                break;
            case 2:
                this.c.showPage();
                break;
            case 3:
                this.c.lambda$loadFromNetwork$7();
                break;
            default:
                this.c.lambda$loadFromNetwork$8();
                break;
        }
    }
}
