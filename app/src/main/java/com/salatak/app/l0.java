package com.salatak.app;

import android.view.View;

/* loaded from: classes2.dex */
public final /* synthetic */ class l0 implements View.OnClickListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f488a;
    public final /* synthetic */ MainActivity b;

    public /* synthetic */ l0(MainActivity mainActivity, int i2) {
        this.f488a = i2;
        this.b = mainActivity;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.f488a) {
            case 0:
                this.b.lambda$setupTabs$1(view);
                break;
            case 1:
                this.b.lambda$setupTabs$2(view);
                break;
            case 2:
                this.b.lambda$setupTabs$3(view);
                break;
            case 3:
                this.b.lambda$setupTabs$4(view);
                break;
            default:
                this.b.lambda$initViews$0(view);
                break;
        }
    }
}
