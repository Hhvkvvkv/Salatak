package com.salatak.app;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class PermissionsActivity extends AppCompatActivity {
    private static final int ALARM_PERMISSION_REQUEST_CODE = 103;
    private static final String KEY_WAITING_FOR_ALARM = "waiting_for_alarm_permission";
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 102;
    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 101;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private CardView btnAllowPermissions;
    private boolean waitingForAlarmPermission = false;

    private void checkAlarmPermissionAndFinish() {
        AlarmManager alarmManager;
        boolean canScheduleExactAlarms;
        if (Build.VERSION.SDK_INT >= 31 && (alarmManager = (AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM)) != null) {
            canScheduleExactAlarms = alarmManager.canScheduleExactAlarms();
            if (!canScheduleExactAlarms) {
                Toast.makeText(this, "يرجى منح إذن التنبيهات الدقيقة لتلقي تنبيهات الصلاة في الوقت المحدد", 1).show();
                return;
            }
        }
        this.waitingForAlarmPermission = false;
        finishPermissions();
    }

    private void createAppDirectory() {
        try {
            if (Build.VERSION.SDK_INT >= 33) {
                File externalFilesDir = getExternalFilesDir(null);
                if (externalFilesDir != null && !externalFilesDir.exists()) {
                    externalFilesDir.mkdirs();
                }
            } else {
                File file = new File("/storage/emulated/0/Android/Technopedia");
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void finishPermissions() {
        createAppDirectory();
        saveOnboardingComplete();
        goToLocationSelection();
    }

    private void goToLocationSelection() {
        try {
            startActivity(new Intent(this, (Class<?>) MainActivity.class));
            finish();
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في الانتقال للشاشة الرئيسية", 0).show();
        }
    }

    private void requestAlarmPermission() {
        boolean canScheduleExactAlarms;
        if (Build.VERSION.SDK_INT < 31) {
            finishPermissions();
            return;
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM);
        if (alarmManager != null) {
            canScheduleExactAlarms = alarmManager.canScheduleExactAlarms();
            if (!canScheduleExactAlarms) {
                Toast.makeText(this, "يرجى السماح للتطبيق بجدولة التنبيهات الدقيقة لمواقيت الصلاة", 1).show();
                this.waitingForAlarmPermission = true;
                Intent intent = new Intent("android.settings.REQUEST_SCHEDULE_EXACT_ALARM");
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ALARM_PERMISSION_REQUEST_CODE);
                return;
            }
        }
        finishPermissions();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT < 33) {
            requestAlarmPermission();
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS") == 0) {
            requestAlarmPermission();
        } else {
            Toast.makeText(this, "يرجى السماح بالإشعارات لتلقي تنبيهات الصلاة", 1).show();
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.POST_NOTIFICATIONS"}, 102);
        }
    }

    private void requestOverlayPermission() {
        if (Settings.canDrawOverlays(this)) {
            requestNotificationPermission();
            return;
        }
        Toast.makeText(this, "يرجى السماح بالظهور فوق التطبيقات الأخرى", 1).show();
        startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), OVERLAY_PERMISSION_REQUEST_CODE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestPermissions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("android.permission.ACCESS_FINE_LOCATION");
        arrayList.add("android.permission.ACCESS_COARSE_LOCATION");
        if (Build.VERSION.SDK_INT <= 32) {
            arrayList.add("android.permission.WRITE_EXTERNAL_STORAGE");
            arrayList.add("android.permission.READ_EXTERNAL_STORAGE");
        }
        ActivityCompat.requestPermissions(this, (String[]) arrayList.toArray(new String[0]), 100);
    }

    private void saveOnboardingComplete() {
        SharedPreferences.Editor edit = getSharedPreferences("SalatakPrefs", 0).edit();
        edit.putBoolean("onboarding_complete", true);
        edit.apply();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i2, int i3, Intent intent) {
        super.onActivityResult(i2, i3, intent);
        if (i2 != OVERLAY_PERMISSION_REQUEST_CODE) {
            if (i2 == ALARM_PERMISSION_REQUEST_CODE) {
                checkAlarmPermissionAndFinish();
            }
        } else if (Settings.canDrawOverlays(this)) {
            requestNotificationPermission();
        } else {
            Toast.makeText(this, "لم يتم منح إذن الظهور فوق التطبيقات", 0).show();
            requestNotificationPermission();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            setContentView(R.layout.activity_permissions);
            if (bundle != null) {
                this.waitingForAlarmPermission = bundle.getBoolean(KEY_WAITING_FOR_ALARM, false);
            }
            CardView cardView = (CardView) findViewById(R.id.btnAllowPermissions);
            this.btnAllowPermissions = cardView;
            if (cardView != null) {
                cardView.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.PermissionsActivity.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        try {
                            PermissionsActivity.this.requestPermissions();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            Toast.makeText(PermissionsActivity.this, "خطأ في طلب الأذونات", 0).show();
                        }
                    }
                });
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في تحميل الواجهة", 0).show();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i2, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i2, strArr, iArr);
        if (i2 != 100) {
            if (i2 == 102) {
                requestAlarmPermission();
                return;
            }
            return;
        }
        for (int i3 : iArr) {
            if (i3 != 0) {
                Toast.makeText(this, "يرجى السماح بالأذونات لاستخدام التطبيق", 1).show();
                return;
            }
        }
        requestOverlayPermission();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (!getSharedPreferences("SalatakPrefs", 0).getBoolean("onboarding_complete", false) && this.waitingForAlarmPermission) {
            checkAlarmPermissionAndFinish();
        }
    }

    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean(KEY_WAITING_FOR_ALARM, this.waitingForAlarmPermission);
    }
}
