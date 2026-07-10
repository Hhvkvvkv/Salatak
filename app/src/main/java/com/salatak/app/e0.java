package com.salatak.app;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.salatak.app.helpers.PrayerTimesCalculator;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public final /* synthetic */ class e0 implements Runnable {
    public final /* synthetic */ int b;
    public final /* synthetic */ Object c;
    public final /* synthetic */ Object d;

    public /* synthetic */ e0(int i2, Object obj, Object obj2) {
        this.b = i2;
        this.c = obj;
        this.d = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.b) {
            case 0:
                ((AzkarForegroundService) this.c).lambda$announceForScreenReader$7((String) this.d);
                break;
            case 1:
                ((ImageView) this.c).setImageBitmap((Bitmap) this.d);
                break;
            case 2:
                ((ChatActivity) this.c).lambda$sendTextMessage$8((String) this.d);
                break;
            case 3:
                ((ChatActivity) this.c).lambda$loadMessages$4((ArrayList) this.d);
                break;
            case 4:
                ((ManualLocationActivity) this.c).lambda$performSearch$5((String) this.d);
                break;
            case 5:
                ((ManualLocationActivity) this.c).lambda$performSearch$4((ArrayList) this.d);
                break;
            case 6:
                ((PrayerTimesFragment) this.c).lambda$calculateAndDisplayPrayerTimes$2((PrayerTimesCalculator.PrayerTimesResult) this.d);
                break;
            case 7:
                ((RadioActivity) this.c).lambda$loadCategory$2((List) this.d);
                break;
            default:
                ((ReportLoginActivity) this.c).lambda$doRegister$3((Exception) this.d);
                break;
        }
    }
}
