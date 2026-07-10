package com.salatak.app;

import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public final /* synthetic */ class w implements View.OnClickListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f534a;
    public final /* synthetic */ String b;
    public final /* synthetic */ int c;
    public final /* synthetic */ AppCompatActivity d;

    /* renamed from: e, reason: collision with root package name */
    public final /* synthetic */ Object f535e;

    public /* synthetic */ w(AppCompatActivity appCompatActivity, String str, String str2, int i2, int i3) {
        this.f534a = i3;
        this.d = appCompatActivity;
        this.b = str;
        this.f535e = str2;
        this.c = i2;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.f534a) {
            case 0:
                ((AzkarDetailActivity) this.d).lambda$loadAzkar$1(this.c, (ImageView) this.f535e, this.b, view);
                break;
            case 1:
                ((QuranSurahListActivity) this.d).lambda$displaySurahs$2(this.b, (String) this.f535e, this.c, view);
                break;
            default:
                ((StoriesActivity) this.d).lambda$addBookmarkItem$11(this.b, (String) this.f535e, this.c, view);
                break;
        }
    }

    public /* synthetic */ w(AzkarDetailActivity azkarDetailActivity, int i2, ImageView imageView, String str) {
        this.f534a = 0;
        this.d = azkarDetailActivity;
        this.c = i2;
        this.f535e = imageView;
        this.b = str;
    }
}
