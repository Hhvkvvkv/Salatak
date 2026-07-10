package com.salatak.app;

import android.app.AlertDialog;
import android.view.View;
import com.salatak.app.AdhanSoundsData;
import com.salatak.app.ChatActivity;
import com.salatak.app.MuezzinActivity;
import com.salatak.app.UpdatesFragment;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public final /* synthetic */ class j0 implements View.OnClickListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f478a;
    public final /* synthetic */ Object b;
    public final /* synthetic */ Object c;

    public /* synthetic */ j0(int i2, Object obj, Object obj2) {
        this.f478a = i2;
        this.b = obj;
        this.c = obj2;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.f478a) {
            case 0:
                ((ChatActivity.ChatAdapter) this.b).lambda$onBindViewHolder$0((String) this.c, view);
                break;
            case 1:
                ((MuezzinActivity.MuezzinAdapter) this.b).lambda$getView$7((AdhanSoundsData.AdhanSound) this.c, view);
                break;
            case 2:
                ((UpdatesFragment.NewsAdapter) this.b).lambda$onBindViewHolder$0((JSONObject) this.c, view);
                break;
            case 3:
                ((AdhanOverlayService) this.b).lambda$showOverlay$3((String) this.c, view);
                break;
            case 4:
                ((QuranAudioActivity) this.b).lambda$displayReciters$4((String) this.c, view);
                break;
            case 5:
                ((QuranDownloadsActivity) this.b).lambda$loadDownloadedReciters$4((String) this.c, view);
                break;
            default:
                QuranPlayerActivity.lambda$downloadCurrentSurah$10((boolean[]) this.b, (AlertDialog) this.c, view);
                break;
        }
    }
}
