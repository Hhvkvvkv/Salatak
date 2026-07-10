package com.salatak.app;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;

/* loaded from: classes2.dex */
public final /* synthetic */ class m implements View.OnLongClickListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f490a;
    public final /* synthetic */ AppCompatActivity b;
    public final /* synthetic */ String c;
    public final /* synthetic */ Object d;

    public /* synthetic */ m(AppCompatActivity appCompatActivity, String str, Object obj, int i2) {
        this.f490a = i2;
        this.b = appCompatActivity;
        this.c = str;
        this.d = obj;
    }

    @Override // android.view.View.OnLongClickListener
    public final boolean onLongClick(View view) {
        boolean lambda$loadCategories$3;
        boolean lambda$showDownloadedCategories$5;
        boolean lambda$addDownloadedItem$10;
        switch (this.f490a) {
            case 0:
                lambda$loadCategories$3 = ((AzkarActivity) this.b).lambda$loadCategories$3(this.c, (String) this.d, view);
                return lambda$loadCategories$3;
            case 1:
                lambda$showDownloadedCategories$5 = ((AzkarActivity) this.b).lambda$showDownloadedCategories$5(this.c, (String) this.d, view);
                return lambda$showDownloadedCategories$5;
            default:
                lambda$addDownloadedItem$10 = ((StoriesActivity) this.b).lambda$addDownloadedItem$10(this.c, (File) this.d, view);
                return lambda$addDownloadedItem$10;
        }
    }
}
