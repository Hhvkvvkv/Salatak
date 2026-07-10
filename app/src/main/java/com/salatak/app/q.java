package com.salatak.app;

import android.content.DialogInterface;

/* loaded from: classes2.dex */
public final /* synthetic */ class q implements DialogInterface.OnClickListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f505a;
    public final /* synthetic */ Object b;

    public /* synthetic */ q(int i2, Object obj) {
        this.f505a = i2;
        this.b = obj;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i2) {
        switch (this.f505a) {
            case 0:
                AzkarActivity.lambda$downloadCategory$9((boolean[]) this.b, dialogInterface, i2);
                break;
            case 1:
                ((ManualLocationActivity) this.b).lambda$showRemoveConfirmDialog$8(dialogInterface, i2);
                break;
            case 2:
                ((MuezzinActivity) this.b).lambda$showFilterDialog$3(dialogInterface, i2);
                break;
            default:
                ((QuranPlayerActivity) this.b).lambda$showSpeedDialog$8(dialogInterface, i2);
                break;
        }
    }
}
