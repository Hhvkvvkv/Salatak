package com.salatak.app;

import android.widget.ImageView;
import com.salatak.app.QuranPlayerService;
import com.salatak.app.RadioService;

/* loaded from: classes2.dex */
public final /* synthetic */ class b2 implements Runnable {
    public final /* synthetic */ int b;
    public final /* synthetic */ Object c;

    public /* synthetic */ b2(int i2, Object obj) {
        this.b = i2;
        this.c = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                ((QuranPlayerService.AnonymousClass1) this.c).lambda$onPlayerError$0();
                break;
            case 1:
                ((RadioService.AnonymousClass1) this.c).lambda$onPlayerError$0();
                break;
            case 2:
                AzkarForegroundService.lambda$animateFlowers$3((ImageView) this.c);
                break;
            case 3:
                ((PrayerTimesFragment) this.c).stopLocationUpdates();
                break;
            case 4:
                ((RadioActivity) this.c).lambda$loadCategory$3();
                break;
            case 5:
                ((RadioService) this.c).lambda$notifyCallback$0();
                break;
            default:
                ((UpdatesFragment) this.c).lambda$loadData$4();
                break;
        }
    }
}
