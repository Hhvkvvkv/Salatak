package com.salatak.app;

import android.view.View;

/* loaded from: classes2.dex */
public final /* synthetic */ class l implements View.OnClickListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f487a;
    public final /* synthetic */ AzkarActivity b;
    public final /* synthetic */ String c;
    public final /* synthetic */ String d;

    public /* synthetic */ l(AzkarActivity azkarActivity, String str, String str2, int i2) {
        this.f487a = i2;
        this.b = azkarActivity;
        this.c = str;
        this.d = str2;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.f487a) {
            case 0:
                this.b.lambda$loadCategories$2(this.c, this.d, view);
                break;
            default:
                this.b.lambda$showDownloadedCategories$4(this.c, this.d, view);
                break;
        }
    }
}
