package com.salatak.app;

import android.content.DialogInterface;

/* loaded from: classes2.dex */
public final /* synthetic */ class i implements DialogInterface.OnClickListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f471a;
    public final /* synthetic */ AzkarActivity b;
    public final /* synthetic */ String c;
    public final /* synthetic */ String d;

    public /* synthetic */ i(AzkarActivity azkarActivity, String str, String str2, int i2) {
        this.f471a = i2;
        this.b = azkarActivity;
        this.c = str;
        this.d = str2;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i2) {
        switch (this.f471a) {
            case 0:
                this.b.lambda$showDownloadedLongPressDialog$8(this.c, this.d, dialogInterface, i2);
                break;
            default:
                this.b.lambda$showCategoryLongPressDialog$6(this.c, this.d, dialogInterface, i2);
                break;
        }
    }
}
