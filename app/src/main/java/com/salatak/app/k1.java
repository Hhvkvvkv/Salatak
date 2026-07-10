package com.salatak.app;

import android.content.DialogInterface;

/* loaded from: classes2.dex */
public final /* synthetic */ class k1 implements DialogInterface.OnClickListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f485a;

    public /* synthetic */ k1(int i2) {
        this.f485a = i2;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i2) {
        switch (this.f485a) {
            case 0:
                dialogInterface.dismiss();
                break;
            default:
                dialogInterface.dismiss();
                break;
        }
    }
}
