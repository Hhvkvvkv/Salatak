package com.salatak.app;

import com.salatak.app.QuranPlayerActivity;
import com.salatak.app.RadioPlayerActivity;

/* loaded from: classes2.dex */
public final /* synthetic */ class z1 implements Runnable {
    public final /* synthetic */ int b;
    public final /* synthetic */ int c;
    public final /* synthetic */ boolean d;

    /* renamed from: e, reason: collision with root package name */
    public final /* synthetic */ boolean f555e;

    /* renamed from: f, reason: collision with root package name */
    public final /* synthetic */ Object f556f;

    public /* synthetic */ z1(Object obj, int i2, boolean z2, boolean z3, int i3) {
        this.b = i3;
        this.f556f = obj;
        this.c = i2;
        this.d = z2;
        this.f555e = z3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                ((QuranPlayerActivity.AnonymousClass2.AnonymousClass1) this.f556f).lambda$onStateChanged$0(this.c, this.d, this.f555e);
                break;
            default:
                ((RadioPlayerActivity.AnonymousClass1) this.f556f).lambda$onServiceConnected$0(this.c, this.d, this.f555e);
                break;
        }
    }
}
