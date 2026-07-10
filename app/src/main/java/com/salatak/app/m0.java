package com.salatak.app;

import android.content.DialogInterface;
import android.location.Address;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;

/* loaded from: classes2.dex */
public final /* synthetic */ class m0 implements DialogInterface.OnClickListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f491a;
    public final /* synthetic */ AppCompatActivity b;
    public final /* synthetic */ Object c;
    public final /* synthetic */ Object d;

    public /* synthetic */ m0(AppCompatActivity appCompatActivity, Object obj, Object obj2, int i2) {
        this.f491a = i2;
        this.b = appCompatActivity;
        this.c = obj;
        this.d = obj2;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i2) {
        switch (this.f491a) {
            case 0:
                ((ManualLocationActivity) this.b).lambda$showActivateDialog$7((Address) this.c, (String) this.d, dialogInterface, i2);
                break;
            case 1:
                ((QuranPlayerActivity) this.b).lambda$deleteCurrentSurah$15((File) this.c, (File) this.d, dialogInterface, i2);
                break;
            default:
                ((StoriesActivity) this.b).lambda$addDownloadedItem$9((File) this.c, (String) this.d, dialogInterface, i2);
                break;
        }
    }
}
