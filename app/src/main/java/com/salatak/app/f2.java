package com.salatak.app;

import android.content.Context;
import android.net.Uri;
import androidx.activity.result.ActivityResultCallback;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.Response;
import com.salatak.app.RadioPlayerActivity;
import com.salatak.app.RadioService;

/* loaded from: classes2.dex */
public final /* synthetic */ class f2 implements RadioService.RadioCallback, Response.Listener, ActivityResultCallback, SwipeRefreshLayout.OnRefreshListener {
    public final /* synthetic */ Object b;

    public /* synthetic */ f2(Object obj) {
        this.b = obj;
    }

    @Override // androidx.activity.result.ActivityResultCallback
    public void onActivityResult(Object obj) {
        ((MuezzinActivity) this.b).lambda$onCreate$0((Uri) obj);
    }

    @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
    public void onRefresh() {
        ((PrayerTimesFragment) this.b).lambda$setupSwipeRefresh$0();
    }

    @Override // com.android.volley.Response.Listener
    public void onResponse(Object obj) {
        AzkarService.lambda$fetchAzkarNow$0((Context) this.b, (String) obj);
    }

    @Override // com.salatak.app.RadioService.RadioCallback
    public void onStateChanged(int i2, boolean z2, boolean z3) {
        ((RadioPlayerActivity.AnonymousClass1) this.b).lambda$onServiceConnected$1(i2, z2, z3);
    }
}
