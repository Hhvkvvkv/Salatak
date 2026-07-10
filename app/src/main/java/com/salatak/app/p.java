package com.salatak.app;

import android.content.DialogInterface;
import android.widget.NumberPicker;
import androidx.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public final /* synthetic */ class p implements DialogInterface.OnClickListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f500a;
    public final /* synthetic */ AppCompatActivity b;
    public final /* synthetic */ Object c;

    public /* synthetic */ p(AppCompatActivity appCompatActivity, Object obj, int i2) {
        this.f500a = i2;
        this.b = appCompatActivity;
        this.c = obj;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i2) {
        switch (this.f500a) {
            case 0:
                ((AzkarActivity) this.b).lambda$showDownloadedLongPressDialog$7((String) this.c, dialogInterface, i2);
                break;
            case 1:
                ((StoryReaderActivity) this.b).lambda$showPageJumpDialog$9((NumberPicker) this.c, dialogInterface, i2);
                break;
            default:
                ((StoryReaderActivity) this.b).lambda$showTextLongPressMenu$10((String) this.c, dialogInterface, i2);
                break;
        }
    }
}
