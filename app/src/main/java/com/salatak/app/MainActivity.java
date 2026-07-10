package com.salatak.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/* loaded from: classes2.dex */
public class MainActivity extends AppCompatActivity {
    private ImageView btnSettings;
    private LinearLayout btnTabMore;
    private LinearLayout btnTabPrayerTimes;
    private LinearLayout btnTabQibla;
    private LinearLayout btnTabUpdates;
    private int currentTab = 0;
    private MoreFragment moreFragment;
    private PrayerTimesFragment prayerTimesFragment;
    private QiblaFragment qiblaFragment;
    private UpdatesFragment updatesFragment;

    private void enableAllPrayerReminders() {
        try {
            SharedPreferences.Editor edit = getSharedPreferences("SalatakPrefs", 0).edit();
            String[] strArr = {"fajr", "dhuhr", "asr", "maghrib", "isha"};
            for (int i2 = 0; i2 < 5; i2++) {
                String str = strArr[i2];
                edit.putBoolean(str + "_reminder_enabled", true);
                edit.putInt(str + "_reminder_minutes", 10);
                edit.putString(str + "_reminder_sound", "soon");
                edit.putBoolean(str + "_adhan_enabled", true);
                if (str.equals("fajr")) {
                    edit.putString(str + "_adhan_sound_id", "athan_3");
                } else {
                    edit.putString(str + "_adhan_sound_id", "athan_2");
                }
            }
            edit.apply();
            Toast.makeText(this, "تم تفعيل تنبيهات الصلاة تلقائياً", 1).show();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void initViews() {
        try {
            this.btnSettings = (ImageView) findViewById(R.id.btnSettings);
            this.btnTabPrayerTimes = (LinearLayout) findViewById(R.id.btnTabPrayerTimes);
            this.btnTabQibla = (LinearLayout) findViewById(R.id.btnTabQibla);
            this.btnTabUpdates = (LinearLayout) findViewById(R.id.btnTabUpdates);
            this.btnTabMore = (LinearLayout) findViewById(R.id.btnTabMore);
            ImageView imageView = this.btnSettings;
            if (imageView != null) {
                imageView.setOnClickListener(new l0(this, 4));
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في تهيئة الواجهة", 0).show();
        }
    }

    private boolean isOnboardingComplete() {
        return getSharedPreferences("SalatakPrefs", 0).getBoolean("onboarding_complete", false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$0(View view) {
        try {
            startActivity(new Intent(this, (Class<?>) SettingsActivity.class));
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في فتح الإعدادات", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupTabs$1(View view) {
        if (this.currentTab != 0) {
            this.currentTab = 0;
            showPrayerTimesFragment();
            updateTabUI();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupTabs$2(View view) {
        if (this.currentTab != 1) {
            this.currentTab = 1;
            showQiblaFragment();
            updateTabUI();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupTabs$3(View view) {
        if (this.currentTab != 2) {
            this.currentTab = 2;
            showUpdatesFragment();
            updateTabUI();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupTabs$4(View view) {
        if (this.currentTab != 3) {
            this.currentTab = 3;
            showMoreFragment();
            updateTabUI();
        }
    }

    private void scheduleRemindersIfNeeded() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("SalatakPrefs", 0);
            if (sharedPreferences.getBoolean("prayer_reminders_initialized", false)) {
                PrayerReminderScheduler.scheduleAllAlarms(this);
            } else {
                enableAllPrayerReminders();
                PrayerReminderScheduler.scheduleAllAlarms(this);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putBoolean("prayer_reminders_initialized", true);
                edit.apply();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void setupTabs() {
        this.btnTabPrayerTimes.setOnClickListener(new l0(this, 0));
        this.btnTabQibla.setOnClickListener(new l0(this, 1));
        this.btnTabUpdates.setOnClickListener(new l0(this, 2));
        this.btnTabMore.setOnClickListener(new l0(this, 3));
    }

    private void showMoreFragment() {
        if (this.moreFragment == null) {
            this.moreFragment = new MoreFragment();
        }
        switchFragment(this.moreFragment);
    }

    private void showPrayerTimesFragment() {
        if (this.prayerTimesFragment == null) {
            this.prayerTimesFragment = new PrayerTimesFragment();
        }
        switchFragment(this.prayerTimesFragment);
    }

    private void showQiblaFragment() {
        if (this.qiblaFragment == null) {
            this.qiblaFragment = new QiblaFragment();
        }
        switchFragment(this.qiblaFragment);
    }

    private void showUpdatesFragment() {
        if (this.updatesFragment == null) {
            this.updatesFragment = new UpdatesFragment();
        }
        switchFragment(this.updatesFragment);
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.fragmentContainer, fragment);
        beginTransaction.commit();
    }

    private void updateTabUI() {
        this.btnTabPrayerTimes.setBackgroundResource(this.currentTab == 0 ? R.drawable.tab_background_selected : R.drawable.tab_background);
        this.btnTabQibla.setBackgroundResource(this.currentTab == 1 ? R.drawable.tab_background_selected : R.drawable.tab_background);
        this.btnTabUpdates.setBackgroundResource(this.currentTab == 2 ? R.drawable.tab_background_selected : R.drawable.tab_background);
        this.btnTabMore.setBackgroundResource(this.currentTab == 3 ? R.drawable.tab_background_selected : R.drawable.tab_background);
        this.btnTabPrayerTimes.setContentDescription(this.currentTab == 0 ? "مواقيت الصلاة، محدد" : "مواقيت الصلاة");
        this.btnTabQibla.setContentDescription(this.currentTab == 1 ? "القبلة، محدد" : "القبلة");
        this.btnTabUpdates.setContentDescription(this.currentTab == 2 ? "آخر التحديثات، محدد" : "آخر التحديثات");
        this.btnTabMore.setContentDescription(this.currentTab == 3 ? "المزيد، محدد" : "المزيد");
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            if (!isOnboardingComplete()) {
                startActivity(new Intent(this, (Class<?>) WelcomeActivity.class));
                finish();
                return;
            }
            Window window = getWindow();
            window.setStatusBarColor(-15918294);
            window.setNavigationBarColor(-15918294);
            setContentView(R.layout.activity_main);
            initViews();
            setupTabs();
            if (bundle == null) {
                showPrayerTimesFragment();
            }
            scheduleRemindersIfNeeded();
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في بدء التطبيق: " + e2.getMessage(), 1).show();
        }
    }
}
