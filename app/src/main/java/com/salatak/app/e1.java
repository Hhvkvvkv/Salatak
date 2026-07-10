package com.salatak.app;

import android.content.DialogInterface;

/* loaded from: classes2.dex */
public final /* synthetic */ class e1 implements DialogInterface.OnClickListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f462a;
    public final /* synthetic */ PrayerSettingsActivity b;

    public /* synthetic */ e1(PrayerSettingsActivity prayerSettingsActivity, int i2) {
        this.f462a = i2;
        this.b = prayerSettingsActivity;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i2) {
        switch (this.f462a) {
            case 0:
                this.b.lambda$checkAndRequestAlarmPermission$2(dialogInterface, i2);
                break;
            default:
                this.b.lambda$showTimeAdjustmentDialog$19(dialogInterface, i2);
                break;
        }
    }
}
