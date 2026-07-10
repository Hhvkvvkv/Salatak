package com.salatak.app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/* loaded from: classes2.dex */
public class AzkarTestHelper {
    private static final String TAG = "AzkarTestHelper";

    public static void testAzkarNotification(Context context) {
        try {
            Log.d(TAG, "Testing azkar notification...");
            Intent intent = new Intent(context, (Class<?>) AzkarBroadcastReceiver.class);
            intent.setAction("com.salatak.app.SHOW_AZKAR");
            context.sendBroadcast(intent);
            Log.d(TAG, "Test notification broadcast sent");
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error testing azkar notification: "), TAG);
        }
    }
}
