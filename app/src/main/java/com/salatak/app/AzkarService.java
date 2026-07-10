package com.salatak.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.Random;
import org.json.JSONArray;

/* loaded from: classes2.dex */
public class AzkarService {
    private static final String AZKAR_API_URL = "https://sherifbots.serv00.net/Api/azkar.php";
    private static final long FETCH_INTERVAL = 86400000;
    private static final String PREFS_AZKAR_LIST = "azkar_list";
    private static final String PREFS_LAST_FETCH = "azkar_last_fetch";
    private static final String PREFS_NAME = "SalatakPrefs";
    private static final String TAG = "AzkarService";

    public static boolean canDrawOverlays(Context context) {
        return Settings.canDrawOverlays(context);
    }

    public static boolean canScheduleExactAlarms(Context context) {
        boolean canScheduleExactAlarms;
        if (Build.VERSION.SDK_INT < 31) {
            return true;
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        if (alarmManager == null) {
            return false;
        }
        canScheduleExactAlarms = alarmManager.canScheduleExactAlarms();
        return canScheduleExactAlarms;
    }

    public static void cancelAzkar(Context context) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
            PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, new Intent(context, (Class<?>) AzkarBroadcastReceiver.class), 201326592);
            if (alarmManager != null) {
                alarmManager.cancel(broadcast);
                Log.d(TAG, "Azkar cancelled");
            }
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error cancelling azkar: "), TAG);
        }
    }

    public static void fetchAzkarFromAPI(Context context) {
        try {
            if (System.currentTimeMillis() - context.getSharedPreferences(PREFS_NAME, 0).getLong(PREFS_LAST_FETCH, 0L) < FETCH_INTERVAL) {
                Log.d(TAG, "Using cached azkar data");
            } else {
                fetchAzkarNow(context);
            }
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error in fetchAzkarFromAPI: "), TAG);
        }
    }

    public static void fetchAzkarFromAPIIfNeeded(Context context) {
        try {
            if (context.getSharedPreferences(PREFS_NAME, 0).getString(PREFS_AZKAR_LIST, "").isEmpty()) {
                Log.d(TAG, "No cached azkar, fetching from API...");
                fetchAzkarNow(context);
            }
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error in fetchAzkarFromAPIIfNeeded: "), TAG);
        }
    }

    private static void fetchAzkarNow(Context context) {
        try {
            Volley.newRequestQueue(context).add(new StringRequest(0, AZKAR_API_URL, new f2(context), new f0()));
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error in fetchAzkarNow: "), TAG);
        }
    }

    private static String getDefaultAzkar() {
        return new String[]{"سبحان الله والحمد لله ولا إله إلا الله والله أكبر", "اللهم صل وسلم على نبينا محمد", "استغفر الله العظيم وأتوب إليه", "لا إله إلا الله وحده لا شريك له، له الملك وله الحمد وهو على كل شيء قدير", "سبحان الله وبحمده، سبحان الله العظيم", "حسبي الله ونعم الوكيل", "لا حول ولا قوة إلا بالله", "اللهم إني أسألك العفو والعافية في الدنيا والآخرة", "اللهم إني أعوذ بك من الهم والحزن، وأعوذ بك من العجز والكسل", "ربنا آتنا في الدنيا حسنة وفي الآخرة حسنة وقنا عذاب النار", "اللهم إني أسألك علماً نافعاً، ورزقاً طيباً، وعملاً متقبلاً", "اللهم اغفر لي ذنبي كله، دقه وجله، وأوله وآخره، وعلانيته وسره", "اللهم إني أسألك من الخير كله عاجله وآجله، ما علمت منه وما لم أعلم", "اللهم إني أعوذ بك من الشر كله عاجله وآجله، ما علمت منه وما لم أعلم", "اللهم اهدني فيمن هديت، وعافني فيمن عافيت، وتولني فيمن توليت", "اللهم إني أعوذ برضاك من سخطك، وبمعافاتك من عقوبتك", "اللهم إني أسألك الثبات في الأمر، والعزيمة على الرشد", "اللهم إني أسألك موجبات رحمتك، وعزائم مغفرتك", "اللهم اجعل القرآن الكريم ربيع قلبي، ونور صدري", "اللهم إني أسألك حبك، وحب من يحبك، وحب عمل يقربني إلى حبك", "اللهم إني أعوذ بك من زوال نعمتك، وتحول عافيتك، وفجاءة نقمتك", "اللهم إني أسألك فعل الخيرات، وترك المنكرات، وحب المساكين", "اللهم آت نفسي تقواها، وزكها أنت خير من زكاها", "اللهم إني أسألك الهدى والتقى، والعفاف والغنى", "اللهم ألهمني رشدي، وأعذني من شر نفسي", "اللهم إني أعوذ بك من شر ما عملت، ومن شر ما لم أعمل", "اللهم اجعلني من الشاكرين، واجعلني من الذاكرين", "اللهم أعني على ذكرك وشكرك وحسن عبادتك", "اللهم إني أسألك خير هذا اليوم، وأعوذ بك من شره", "اللهم بارك لي في يومي هذا، واجعله يوماً مباركاً علي"}[new Random().nextInt(30)];
    }

    public static String getRandomAudioFile() {
        String[] strArr = {"hamd.mp3", "haoqala.mp3", "istghfar.mp3", "moqalib_alquloub.mp3", "sal_ala_muhammad.mp3", "sal_ala_muhammad_2.mp3", "sal_ala_muhammad_waali_muhammad.mp3", "sal_ala_muhammad_waalih.mp3", "sobhank_wbihamdik.mp3", "takbir.mp3", "taohid.mp3", "tasbih.mp3", "tasbih_wahamd.mp3", "tasbih_wahamd_2.mp3", "tbarakt.mp3"};
        try {
            String str = strArr[new Random().nextInt(15)];
            Log.d(TAG, "Selected random audio file: " + str);
            return str;
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error selecting random audio file: "), TAG);
            return strArr[0];
        }
    }

    public static String getRandomAzkar(Context context) {
        try {
            String string = context.getSharedPreferences(PREFS_NAME, 0).getString(PREFS_AZKAR_LIST, "");
            if (string.isEmpty()) {
                Log.d(TAG, "No azkar found in cache, using default azkar");
                return getDefaultAzkar();
            }
            String[] split = string.split("\\|\\|\\|");
            if (split.length <= 0) {
                return getDefaultAzkar();
            }
            String trim = split[new Random().nextInt(split.length)].trim();
            Log.d(TAG, "Selected random azkar from " + split.length + " azkar");
            return trim;
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error getting random azkar: "), TAG);
            return getDefaultAzkar();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$fetchAzkarNow$0(Context context, String str) {
        try {
            JSONArray jSONArray = new JSONArray(str);
            StringBuilder sb = new StringBuilder();
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                String optString = jSONArray.getJSONObject(i2).optString("text", "");
                if (!optString.isEmpty()) {
                    sb.append(optString);
                    if (i2 < jSONArray.length() - 1) {
                        sb.append("|||");
                    }
                }
            }
            SharedPreferences.Editor edit = context.getSharedPreferences(PREFS_NAME, 0).edit();
            edit.putString(PREFS_AZKAR_LIST, sb.toString());
            edit.putLong(PREFS_LAST_FETCH, System.currentTimeMillis());
            edit.apply();
            Log.d(TAG, "Azkar data fetched and cached successfully - " + jSONArray.length() + " azkar");
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error parsing azkar response: "), TAG);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$fetchAzkarNow$1(VolleyError volleyError) {
        StringBuilder sb = new StringBuilder("Error fetching azkar from API: ");
        sb.append(volleyError.getMessage() != null ? volleyError.getMessage() : "Unknown error");
        Log.e(TAG, sb.toString());
    }

    public static void requestExactAlarmPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 31) {
            try {
                Intent intent = new Intent("android.settings.REQUEST_SCHEDULE_EXACT_ALARM");
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                intent.setFlags(268435456);
                context.startActivity(intent);
            } catch (Exception e2) {
                android.support.v4.media.l.p(e2, new StringBuilder("Error requesting exact alarm permission: "), TAG);
            }
        }
    }

    public static void scheduleAzkar(Context context, int i2) {
        try {
            cancelAzkar(context);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
            if (alarmManager == null) {
                Log.e(TAG, "AlarmManager is null");
                return;
            }
            Intent intent = new Intent(context, (Class<?>) AzkarBroadcastReceiver.class);
            intent.setAction("com.salatak.app.SHOW_AZKAR");
            PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, 201326592);
            long j2 = 60000 * i2;
            long elapsedRealtime = SystemClock.elapsedRealtime() + j2;
            boolean canScheduleExactAlarms = Build.VERSION.SDK_INT >= 31 ? alarmManager.canScheduleExactAlarms() : true;
            if (canScheduleExactAlarms) {
                alarmManager.setExactAndAllowWhileIdle(2, elapsedRealtime, broadcast);
                Log.d(TAG, "Exact alarm scheduled with setExactAndAllowWhileIdle for " + i2 + " minutes");
            } else if (canScheduleExactAlarms) {
                alarmManager.setExact(2, elapsedRealtime, broadcast);
                Log.d(TAG, "Exact alarm scheduled for " + i2 + " minutes");
            } else {
                alarmManager.setRepeating(2, elapsedRealtime, j2, broadcast);
                Log.w(TAG, "Exact alarm permission not granted - using inexact repeating alarm for " + i2 + " minutes");
            }
            fetchAzkarFromAPI(context);
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error scheduling azkar: "), TAG);
        }
    }

    public static void showTestAzkarNotification(Context context) {
        try {
            Intent intent = new Intent(context, (Class<?>) AzkarBroadcastReceiver.class);
            intent.setAction("com.salatak.app.SHOW_AZKAR");
            context.sendBroadcast(intent);
            Log.d(TAG, "Test azkar notification triggered manually");
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error showing test azkar: "), TAG);
        }
    }
}
