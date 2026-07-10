package com.salatak.app;

import android.view.View;

/* loaded from: classes2.dex */
public final /* synthetic */ class f1 implements View.OnClickListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f464a;
    public final /* synthetic */ PrayerSettingsActivity b;

    public /* synthetic */ f1(PrayerSettingsActivity prayerSettingsActivity, int i2) {
        this.f464a = i2;
        this.b = prayerSettingsActivity;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.f464a) {
            case 0:
                this.b.lambda$setupListeners$4(view);
                break;
            case 1:
                this.b.lambda$setupListeners$14(view);
                break;
            case 2:
                this.b.lambda$setupListeners$16(view);
                break;
            case 3:
                this.b.lambda$setupListeners$6(view);
                break;
            case 4:
                this.b.lambda$setupListeners$8(view);
                break;
            case 5:
                this.b.lambda$setupListeners$9(view);
                break;
            case 6:
                this.b.lambda$setupListeners$10(view);
                break;
            case 7:
                this.b.lambda$setupListeners$11(view);
                break;
            case 8:
                this.b.lambda$setupListeners$12(view);
                break;
            default:
                this.b.lambda$initViews$3(view);
                break;
        }
    }
}
