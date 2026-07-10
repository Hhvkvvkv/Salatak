package com.salatak.app;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/* loaded from: classes2.dex */
public final /* synthetic */ class q1 implements Response.Listener, Response.ErrorListener {
    public final /* synthetic */ QuranAudioActivity b;

    public /* synthetic */ q1(QuranAudioActivity quranAudioActivity) {
        this.b = quranAudioActivity;
    }

    @Override // com.android.volley.Response.ErrorListener
    public void onErrorResponse(VolleyError volleyError) {
        this.b.lambda$loadReciters$3(volleyError);
    }

    @Override // com.android.volley.Response.Listener
    public void onResponse(Object obj) {
        this.b.lambda$loadReciters$2((String) obj);
    }
}
