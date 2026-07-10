package com.salatak.app;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import java.io.File;

/* loaded from: classes2.dex */
public class LocationSelectionActivity extends AppCompatActivity {
    private static final String KEY_WAITING_ALARM = "waiting_alarm";
    private static final String KEY_WAITING_OVERLAY = "waiting_overlay";
    private static final int REQUEST_ALARM = 204;
    private static final int REQUEST_LOCATION_PERMISSION = 200;
    private static final int REQUEST_MANUAL_LOCATION = 201;
    private static final int REQUEST_NOTIFICATION = 203;
    private static final int REQUEST_OVERLAY = 202;
    private RadioButton radioAutomatic;
    private RadioButton radioManual;
    private TextView tvSelectedCity;
    private boolean useManual = false;
    private boolean waitingForOverlay = false;
    private boolean waitingForAlarm = false;

    private void checkAlarmAndFinish() {
        AlarmManager alarmManager;
        boolean canScheduleExactAlarms;
        if (Build.VERSION.SDK_INT >= 31 && (alarmManager = (AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM)) != null) {
            canScheduleExactAlarms = alarmManager.canScheduleExactAlarms();
            if (!canScheduleExactAlarms) {
                return;
            }
        }
        this.waitingForAlarm = false;
        finishOnboarding();
    }

    private void createAppDirectory() {
        try {
            File externalFilesDir = Build.VERSION.SDK_INT >= 33 ? getExternalFilesDir(null) : new File("/storage/emulated/0/Android/Technopedia");
            if (externalFilesDir == null || externalFilesDir.exists()) {
                return;
            }
            externalFilesDir.mkdirs();
        } catch (Exception unused) {
        }
    }

    private void finishOnboarding() {
        try {
            if (Build.VERSION.SDK_INT <= 32) {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 999);
            }
        } catch (Exception unused) {
        }
        createAppDirectory();
        saveOnboardingComplete();
        startActivity(new Intent(this, (Class<?>) MainActivity.class));
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        selectAutomatic();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        openManualLocation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$2(View view) {
        onNextPressed();
    }

    private void onNextPressed() {
        if (this.useManual) {
            requestOverlayPermission();
        } else {
            requestLocationPermissionThenOverlay();
        }
    }

    private void openManualLocation() {
        startActivityForResult(new Intent(this, (Class<?>) ManualLocationActivity.class), REQUEST_MANUAL_LOCATION);
    }

    private void proceedAfterOverlay() {
        if (Build.VERSION.SDK_INT < 33 || ContextCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS") == 0) {
            requestAlarmPermission();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.POST_NOTIFICATIONS"}, REQUEST_NOTIFICATION);
        }
    }

    private void refreshCityLabel() {
        if (this.tvSelectedCity == null) {
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("SalatakPrefs", 0);
        if (!sharedPreferences.getBoolean("manual_location_enabled", false)) {
            this.tvSelectedCity.setText("ابحث عن مدينتك أو بلدك");
            this.tvSelectedCity.setTextColor(-5592406);
            return;
        }
        String string = sharedPreferences.getString("manual_location_name", "");
        TextView textView = this.tvSelectedCity;
        if (string.isEmpty()) {
            string = "تم تحديد موقع يدوي";
        }
        textView.setText(string);
        this.tvSelectedCity.setTextColor(-2052805);
        this.useManual = true;
        RadioButton radioButton = this.radioManual;
        if (radioButton != null) {
            radioButton.setChecked(true);
        }
        RadioButton radioButton2 = this.radioAutomatic;
        if (radioButton2 != null) {
            radioButton2.setChecked(false);
        }
    }

    private void requestAlarmPermission() {
        AlarmManager alarmManager;
        boolean canScheduleExactAlarms;
        if (Build.VERSION.SDK_INT >= 31 && (alarmManager = (AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM)) != null) {
            canScheduleExactAlarms = alarmManager.canScheduleExactAlarms();
            if (!canScheduleExactAlarms) {
                this.waitingForAlarm = true;
                Intent intent = new Intent("android.settings.REQUEST_SCHEDULE_EXACT_ALARM");
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_ALARM);
                return;
            }
        }
        finishOnboarding();
    }

    private void requestLocationPermissionThenOverlay() {
        boolean z2 = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0;
        boolean z3 = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0;
        if (z2 || z3) {
            requestOverlayPermission();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}, 200);
        }
    }

    private void requestOverlayPermission() {
        if (Settings.canDrawOverlays(this)) {
            proceedAfterOverlay();
            return;
        }
        Toast.makeText(this, "يرجى السماح بالظهور فوق التطبيقات الأخرى لتفعيل الأذان", 1).show();
        this.waitingForOverlay = true;
        startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), REQUEST_OVERLAY);
    }

    private void saveOnboardingComplete() {
        getSharedPreferences("SalatakPrefs", 0).edit().putBoolean("onboarding_complete", true).apply();
    }

    private void selectAutomatic() {
        this.useManual = false;
        RadioButton radioButton = this.radioAutomatic;
        if (radioButton != null) {
            radioButton.setChecked(true);
        }
        RadioButton radioButton2 = this.radioManual;
        if (radioButton2 != null) {
            radioButton2.setChecked(false);
        }
        SharedPreferences.Editor edit = getSharedPreferences("SalatakPrefs", 0).edit();
        edit.putBoolean("manual_location_enabled", false);
        edit.apply();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i2, int i3, Intent intent) {
        super.onActivityResult(i2, i3, intent);
        if (i2 != REQUEST_MANUAL_LOCATION) {
            if (i2 == REQUEST_OVERLAY) {
                this.waitingForOverlay = false;
                proceedAfterOverlay();
                return;
            } else {
                if (i2 == REQUEST_ALARM) {
                    this.waitingForAlarm = false;
                    checkAlarmAndFinish();
                    return;
                }
                return;
            }
        }
        if (getSharedPreferences("SalatakPrefs", 0).getBoolean("manual_location_enabled", false)) {
            this.useManual = true;
            RadioButton radioButton = this.radioManual;
            if (radioButton != null) {
                radioButton.setChecked(true);
            }
            RadioButton radioButton2 = this.radioAutomatic;
            if (radioButton2 != null) {
                radioButton2.setChecked(false);
            }
        }
        refreshCityLabel();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            getWindow().setStatusBarColor(ViewCompat.MEASURED_STATE_MASK);
            getWindow().setNavigationBarColor(ViewCompat.MEASURED_STATE_MASK);
            setContentView(R.layout.activity_location_selection);
            if (bundle != null) {
                this.waitingForOverlay = bundle.getBoolean(KEY_WAITING_OVERLAY, false);
                this.waitingForAlarm = bundle.getBoolean(KEY_WAITING_ALARM, false);
                this.useManual = bundle.getBoolean("useManual", false);
            }
            this.radioAutomatic = (RadioButton) findViewById(R.id.radioAutomatic);
            this.radioManual = (RadioButton) findViewById(R.id.radioManual);
            this.tvSelectedCity = (TextView) findViewById(R.id.tvSelectedCity);
            View findViewById = findViewById(R.id.optionAutomatic);
            View findViewById2 = findViewById(R.id.optionManual);
            CardView cardView = (CardView) findViewById(R.id.btnNextLocation);
            if (findViewById != null) {
                final int i2 = 0;
                findViewById.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.k0
                    public final /* synthetic */ LocationSelectionActivity b;

                    {
                        this.b = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        switch (i2) {
                            case 0:
                                this.b.lambda$onCreate$0(view);
                                break;
                            case 1:
                                this.b.lambda$onCreate$1(view);
                                break;
                            default:
                                this.b.lambda$onCreate$2(view);
                                break;
                        }
                    }
                });
            }
            if (findViewById2 != null) {
                final int i3 = 1;
                findViewById2.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.k0
                    public final /* synthetic */ LocationSelectionActivity b;

                    {
                        this.b = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        switch (i3) {
                            case 0:
                                this.b.lambda$onCreate$0(view);
                                break;
                            case 1:
                                this.b.lambda$onCreate$1(view);
                                break;
                            default:
                                this.b.lambda$onCreate$2(view);
                                break;
                        }
                    }
                });
            }
            if (cardView != null) {
                final int i4 = 2;
                cardView.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.k0
                    public final /* synthetic */ LocationSelectionActivity b;

                    {
                        this.b = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        switch (i4) {
                            case 0:
                                this.b.lambda$onCreate$0(view);
                                break;
                            case 1:
                                this.b.lambda$onCreate$1(view);
                                break;
                            default:
                                this.b.lambda$onCreate$2(view);
                                break;
                        }
                    }
                });
            }
            refreshCityLabel();
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في تحميل الواجهة", 0).show();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i2, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i2, strArr, iArr);
        if (i2 == 200) {
            requestOverlayPermission();
        } else if (i2 == REQUEST_NOTIFICATION) {
            requestAlarmPermission();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        refreshCityLabel();
        if (this.waitingForOverlay) {
            this.waitingForOverlay = false;
            proceedAfterOverlay();
        } else if (this.waitingForAlarm) {
            checkAlarmAndFinish();
        }
    }

    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean(KEY_WAITING_OVERLAY, this.waitingForOverlay);
        bundle.putBoolean(KEY_WAITING_ALARM, this.waitingForAlarm);
        bundle.putBoolean("useManual", this.useManual);
    }
}
