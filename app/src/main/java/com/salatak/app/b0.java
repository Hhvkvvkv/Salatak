package com.salatak.app;

import android.app.AlertDialog;
import android.content.ContextWrapper;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public final /* synthetic */ class b0 implements Runnable {
    public final /* synthetic */ int b;
    public final /* synthetic */ Object c;
    public final /* synthetic */ ContextWrapper d;

    /* renamed from: e, reason: collision with root package name */
    public final /* synthetic */ Object f452e;

    public /* synthetic */ b0(ContextWrapper contextWrapper, KeyEvent.Callback callback, Object obj, int i2) {
        this.b = i2;
        this.d = contextWrapper;
        this.f452e = callback;
        this.c = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                ((AzkarForegroundService) this.d).lambda$showComboBoxOverlay$1((LinearLayout) this.f452e, (String) this.c);
                break;
            case 1:
                ((ChatActivity) this.d).lambda$loadImageAsync$13((String) this.c, (ImageView) this.f452e);
                break;
            case 2:
                ((MuezzinActivity) this.d).lambda$downloadAdhan$7((ProgressBar) this.f452e, (ImageView) this.c);
                break;
            case 3:
                ((QuranPlayerActivity) this.d).lambda$downloadCurrentSurah$12((AlertDialog) this.f452e, (String) this.c);
                break;
            case 4:
                ((QuranPlayerActivity) this.d).lambda$downloadCurrentSurah$13((AlertDialog) this.f452e, (Exception) this.c);
                break;
            default:
                ((RadioActivity) this.d).lambda$loadCategory$4((String) this.c, (String) this.f452e);
                break;
        }
    }

    public /* synthetic */ b0(AppCompatActivity appCompatActivity, String str, Object obj, int i2) {
        this.b = i2;
        this.d = appCompatActivity;
        this.c = str;
        this.f452e = obj;
    }
}
