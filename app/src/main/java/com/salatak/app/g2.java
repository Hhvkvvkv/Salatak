package com.salatak.app;

/* loaded from: classes2.dex */
public final /* synthetic */ class g2 implements Runnable {
    public final /* synthetic */ int b;
    public final /* synthetic */ ReportLoginActivity c;
    public final /* synthetic */ String d;

    /* renamed from: e, reason: collision with root package name */
    public final /* synthetic */ String f466e;

    /* renamed from: f, reason: collision with root package name */
    public final /* synthetic */ String f467f;

    public /* synthetic */ g2(ReportLoginActivity reportLoginActivity, String str, String str2, String str3, int i2) {
        this.b = i2;
        this.c = reportLoginActivity;
        this.d = str;
        this.f466e = str2;
        this.f467f = str3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                String str = this.f466e;
                String str2 = this.f467f;
                this.c.lambda$doRegister$4(this.d, str, str2);
                break;
            default:
                this.c.lambda$doRegister$2(this.d, this.f466e, this.f467f);
                break;
        }
    }
}
