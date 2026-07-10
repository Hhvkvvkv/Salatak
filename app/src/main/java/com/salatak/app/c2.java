package com.salatak.app;

import android.view.View;
import java.io.File;

/* loaded from: classes2.dex */
public final /* synthetic */ class c2 implements View.OnClickListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f457a;
    public final /* synthetic */ String b;
    public final /* synthetic */ Object c;
    public final /* synthetic */ Object d;

    public /* synthetic */ c2(int i2, String str, Object obj, Object obj2) {
        this.f457a = i2;
        this.c = obj;
        this.b = str;
        this.d = obj2;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.f457a) {
            case 0:
                ((RadioActivity) this.c).lambda$displayStations$5(this.b, (String) this.d, view);
                break;
            case 1:
                ((StoriesActivity) this.c).lambda$addDownloadedItem$8(this.b, (File) this.d, view);
                break;
            default:
                ((UpdatesFragment) this.c).lambda$loadData$2(this.b, (String) this.d, view);
                break;
        }
    }
}
