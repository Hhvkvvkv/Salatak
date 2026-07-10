package com.salatak.app;

import android.content.DialogInterface;

/* loaded from: classes2.dex */
public final /* synthetic */ class h1 implements DialogInterface.OnClickListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f469a;
    public final /* synthetic */ PrayerSettingsActivity b;
    public final /* synthetic */ int[] c;
    public final /* synthetic */ String[] d;

    public /* synthetic */ h1(PrayerSettingsActivity prayerSettingsActivity, int[] iArr, String[] strArr, int i2) {
        this.f469a = i2;
        this.b = prayerSettingsActivity;
        this.c = iArr;
        this.d = strArr;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i2) {
        switch (this.f469a) {
            case 0:
                this.b.lambda$showReminderTimeDialog$20(this.c, this.d, dialogInterface, i2);
                break;
            default:
                this.b.lambda$showIqamaTimeDialog$22(this.c, this.d, dialogInterface, i2);
                break;
        }
    }
}
