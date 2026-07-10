package com.salatak.app;

import android.view.View;

/* loaded from: classes2.dex */
public final /* synthetic */ class i2 implements View.OnClickListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f475a;
    public final /* synthetic */ SettingsActivity b;

    public /* synthetic */ i2(SettingsActivity settingsActivity, int i2) {
        this.f475a = i2;
        this.b = settingsActivity;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.f475a) {
            case 0:
                this.b.lambda$setupManualLocation$4(view);
                break;
            case 1:
                this.b.lambda$setupDiagnosticButtons$2(view);
                break;
            default:
                this.b.lambda$setupDiagnosticButtons$3(view);
                break;
        }
    }
}
