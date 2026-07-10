package com.salatak.app;

import android.view.View;
import com.salatak.app.MuezzinActivity;

/* loaded from: classes2.dex */
public final /* synthetic */ class y0 implements View.OnClickListener {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f553a;
    public final /* synthetic */ Object b;

    public /* synthetic */ y0(int i2, Object obj) {
        this.f553a = i2;
        this.b = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.f553a) {
            case 0:
                ((MuezzinActivity.DownloadsAdapter) this.b).lambda$getView$0(view);
                break;
            case 1:
                ((AzkarDetailActivity) this.b).lambda$onCreate$0(view);
                break;
            case 2:
                ((AzkarDisplayActivity) this.b).lambda$onCreate$0(view);
                break;
            case 3:
                ((AzkarForegroundService) this.b).lambda$showComboBoxOverlay$0(view);
                break;
            case 4:
                ((PrayerTimesFragment) this.b).lambda$updateLocationBanner$1(view);
                break;
            case 5:
                ((QuranDownloadsActivity) this.b).lambda$onCreate$0(view);
                break;
            case 6:
                ((QuranSurahListActivity) this.b).lambda$onCreate$0(view);
                break;
            default:
                ((RadioActivity) this.b).lambda$onCreate$0(view);
                break;
        }
    }
}
