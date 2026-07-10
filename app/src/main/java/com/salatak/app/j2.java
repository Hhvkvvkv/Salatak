package com.salatak.app;

/* loaded from: classes2.dex */
public final /* synthetic */ class j2 implements Runnable {
    public final /* synthetic */ int b;
    public final /* synthetic */ StoriesActivity c;

    public /* synthetic */ j2(StoriesActivity storiesActivity, int i2) {
        this.b = i2;
        this.c = storiesActivity;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                this.c.lambda$loadData$5();
                break;
            case 1:
                this.c.lambda$downloadStory$15();
                break;
            case 2:
                this.c.lambda$downloadStory$16();
                break;
            case 3:
                this.c.lambda$downloadStory$17();
                break;
            default:
                this.c.refreshList();
                break;
        }
    }
}
