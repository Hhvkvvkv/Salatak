package com.salatak.app;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/* loaded from: classes2.dex */
public final /* synthetic */ class f0 implements Response.ErrorListener {
    @Override // com.android.volley.Response.ErrorListener
    public final void onErrorResponse(VolleyError volleyError) {
        AzkarService.lambda$fetchAzkarNow$1(volleyError);
    }
}
