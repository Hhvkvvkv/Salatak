package com.salatak.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.salatak.app.helpers.DiagnosticLogger;

/* loaded from: classes2.dex */
public class SettingsActivity extends AppCompatActivity {
    private Button btnSelectAudioAzkar;
    private LinearLayout layoutAdhan;
    private LinearLayout layoutAzkar;
    private LinearLayout layoutAzkarAudio;
    private LinearLayout layoutAzkarAutoMode;
    private LinearLayout layoutAzkarSettings;
    private LinearLayout layoutAzkarTextNotification;
    private LinearLayout layoutAzkarVibration;
    private LinearLayout layoutFlipToSilence;
    private LinearLayout layoutLED;
    private LinearLayout layoutManualLocation;
    private LinearLayout layoutNotificationOnly;
    private LinearLayout layoutNotifications;
    private LinearLayout layoutShakeToSilence;
    private LinearLayout layoutSummerTime;
    private LinearLayout layoutTapToSilence;
    private LinearLayout layoutUseMediaVolume;
    private LinearLayout layoutVibration;
    private LinearLayout layoutVolumeToSilence;
    private SharedPreferences prefs;
    private Spinner spinnerAzkarInterval;
    private Spinner spinnerCalculationMethod;
    private Spinner spinnerHighLatitude;
    private Spinner spinnerMadhab;
    private Spinner spinnerTimeFormat;
    private SwitchCompat switchAdhan;
    private SwitchCompat switchAzkar;
    private SwitchCompat switchAzkarAudio;
    private SwitchCompat switchAzkarAutoMode;
    private SwitchCompat switchAzkarTextNotification;
    private SwitchCompat switchAzkarVibration;
    private SwitchCompat switchFlipToSilence;
    private SwitchCompat switchLED;
    private SwitchCompat switchNotificationOnly;
    private SwitchCompat switchNotifications;
    private SwitchCompat switchShakeToSilence;
    private SwitchCompat switchSummerTime;
    private SwitchCompat switchTapToSilence;
    private SwitchCompat switchUseMediaVolume;
    private SwitchCompat switchVibration;
    private SwitchCompat switchVolumeToSilence;
    private TextView tvManualLocationStatus;

    public static class GoldSpinnerAdapter extends ArrayAdapter<String> {
        private static final int COLOR_BG_DROP = -14016758;
        private static final int COLOR_BG_SEL = -15068152;
        private static final int COLOR_TEXT = -666250;

        public GoldSpinnerAdapter(Context context, String[] strArr) {
            super(context, android.R.layout.simple_spinner_item, strArr);
        }

        private View applyStyle(View view, boolean z2) {
            try {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                if (textView != null) {
                    textView.setTextColor(COLOR_TEXT);
                    textView.setBackgroundColor(z2 ? COLOR_BG_DROP : COLOR_BG_SEL);
                    textView.setPadding(32, 20, 32, 20);
                    textView.setTextSize(15.0f);
                }
            } catch (Exception unused) {
            }
            return view;
        }

        @Override // android.widget.ArrayAdapter, android.widget.BaseAdapter, android.widget.SpinnerAdapter
        public View getDropDownView(int i2, View view, ViewGroup viewGroup) {
            return applyStyle(super.getDropDownView(i2, view, viewGroup), true);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int i2, View view, ViewGroup viewGroup) {
            return applyStyle(super.getView(i2, view, viewGroup), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$playAzkarAudio$0(MediaPlayer mediaPlayer) {
        try {
            mediaPlayer.release();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$playAzkarAudio$1(MediaPlayer mediaPlayer, int i2, int i3) {
        try {
            mediaPlayer.release();
            return true;
        } catch (Exception e2) {
            e2.printStackTrace();
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupDiagnosticButtons$2(View view) {
        try {
            DiagnosticLogger.log(this, "SETTINGS", "المستخدم طلب مشاركة السجل");
            if (DiagnosticLogger.readLog(this).equals("لا يوجد سجل بعد.")) {
                Toast.makeText(this, "السجل فارغ — افتح التطبيق أولاً وجرّب المشكلة ثم شارك", 1).show();
            } else {
                DiagnosticLogger.shareLog(this);
            }
        } catch (Exception unused) {
            Toast.makeText(this, "خطأ في مشاركة السجل", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupDiagnosticButtons$3(View view) {
        try {
            DiagnosticLogger.clearLog(this);
            Toast.makeText(this, "تم مسح السجل", 0).show();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupManualLocation$4(View view) {
        startActivity(new Intent(this, (Class<?>) ManualLocationActivity.class));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playAzkarAudio(String str) {
        try {
            AssetFileDescriptor openFd = getAssets().openFd("azkar_audio/" + str);
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
            openFd.close();
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(2).setUsage(1).build());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new t(2));
            mediaPlayer.setOnErrorListener(new u(2));
            mediaPlayer.start();
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في تشغيل الذكر الصوتي", 0).show();
        }
    }

    private void setupAudioAzkarButton() {
        try {
            Button button = this.btnSelectAudioAzkar;
            if (button != null) {
                button.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.SettingsActivity.39
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SettingsActivity.this.showAudioAzkarDialog();
                    }
                });
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void setupAzkarIntervalSpinner() {
        try {
            if (this.spinnerAzkarInterval != null && this.prefs != null) {
                final int[] iArr = {1, 5, 10, 15, 30, 45, 60, 90, 120};
                this.spinnerAzkarInterval.setAdapter((SpinnerAdapter) new GoldSpinnerAdapter(this, new String[]{"1 دقيقة", "5 دقائق", "10 دقائق", "15 دقيقة", "30 دقيقة", "45 دقيقة", "60 دقيقة (ساعة)", "90 دقيقة", "120 دقيقة (ساعتان)"}));
                int i2 = this.prefs.getInt("azkar_interval_minutes", 30);
                int i3 = 0;
                while (true) {
                    if (i3 >= 9) {
                        break;
                    }
                    if (iArr[i3] == i2) {
                        this.spinnerAzkarInterval.setSelection(i3);
                        break;
                    }
                    i3++;
                }
                this.spinnerAzkarInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.salatak.app.SettingsActivity.38
                    @Override // android.widget.AdapterView.OnItemSelectedListener
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i4, long j2) {
                        try {
                            SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                            edit.putInt("azkar_interval_minutes", iArr[i4]);
                            edit.apply();
                            if (SettingsActivity.this.switchAzkar == null || !SettingsActivity.this.switchAzkar.isChecked()) {
                                return;
                            }
                            AzkarService.scheduleAzkar(SettingsActivity.this, iArr[i4]);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }

                    @Override // android.widget.AdapterView.OnItemSelectedListener
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في إعداد الأذكار", 0).show();
        }
    }

    private void setupCalculationMethodSpinner() {
        try {
            if (this.spinnerCalculationMethod != null && this.prefs != null) {
                final String[] strArr = {"MuslimWorldLeague", "Egyptian", "Karachi", "UmmAlQura", "Dubai", "Qatar", "Kuwait", "Singapore", "Turkey", "Tehran", "NorthAmerica", "Jordan", "Syria", "Palestine", "Iraq", "Lebanon", "Yemen", "Libya", "Tunisia", "Algeria", "Morocco", "Sudan", "Bahrain", "Oman", "Malaysia", "Indonesia", "Brunei", "Bangladesh", "India", "Nigeria", "Mauritania", "Somalia", "Afghanistan", "Uzbekistan", "Bosnia", "France", "Russia", "Maldives", "SouthAfrica", "SaudiGeneral"};
                this.spinnerCalculationMethod.setAdapter((SpinnerAdapter) new GoldSpinnerAdapter(this, new String[]{"رابطة العالم الإسلامي", "الهيئة المصرية العامة للمساحة", "جامعة العلوم الإسلامية - كراتشي", "أم القرى - مكة المكرمة", "الهيئة العامة للشؤون الإسلامية - الإمارات", "قطر", "الكويت", "سنغافورة - MUIS", "رئاسة الشؤون الدينية - تركيا", "مؤسسة الجيوفيزياء - طهران", "ISNA - أمريكا الشمالية", "دائرة الإفتاء العام - الأردن", "وزارة الأوقاف - سوريا", "دار الإفتاء - فلسطين", "ديوان الوقف السني - العراق", "دار الفتوى - لبنان", "وزارة الأوقاف - اليمن", "الهيئة العامة للأوقاف - ليبيا", "وزارة الشؤون الدينية - تونس", "وزارة الشؤون الدينية - الجزائر", "وزارة الأوقاف والشؤون الإسلامية - المغرب", "هيئة الشؤون الدينية - السودان", "هيئة الأوقاف - البحرين", "وزارة الأوقاف والشؤون الدينية - عمان", "إدارة الشؤون الإسلامية - ماليزيا (JAKIM)", "وزارة الشؤون الدينية - إندونيسيا (KEMENAG)", "وزارة الشؤون الدينية - بروناي", "المؤسسة الإسلامية - بنغلاديش", "المركز الإسلامي - الهند", "الهيئة الإسلامية - نيجيريا", "وزارة الشؤون الإسلامية - موريتانيا", "الشؤون الإسلامية - الصومال", "وزارة الأوقاف - أفغانستان", "الإدارة الدينية - أوزبكستان", "المشيخة الإسلامية - البوسنة والهرسك", "المسجد الكبير - باريس (فرنسا)", "الإدارة الروحية للمسلمين - روسيا", "وزارة الشؤون الإسلامية - جزر المالديف", "المجلس القضائي الإسلامي - جنوب أفريقيا", "الرئاسة العامة للبحوث - السعودية"}));
                String string = this.prefs.getString("calculation_method", "MuslimWorldLeague");
                int i2 = 0;
                while (true) {
                    if (i2 >= 40) {
                        break;
                    }
                    if (strArr[i2].equals(string)) {
                        this.spinnerCalculationMethod.setSelection(i2);
                        break;
                    }
                    i2++;
                }
                this.spinnerCalculationMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.salatak.app.SettingsActivity.2
                    @Override // android.widget.AdapterView.OnItemSelectedListener
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i3, long j2) {
                        try {
                            SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                            edit.putString("calculation_method", strArr[i3]);
                            edit.apply();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }

                    @Override // android.widget.AdapterView.OnItemSelectedListener
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في إعداد طرق الحساب", 0).show();
        }
    }

    private void setupDiagnosticButtons() {
        try {
            Button button = (Button) findViewById(R.id.btnShareDiagnostic);
            Button button2 = (Button) findViewById(R.id.btnClearDiagnostic);
            if (button != null) {
                button.setOnClickListener(new i2(this, 1));
            }
            if (button2 != null) {
                button2.setOnClickListener(new i2(this, 2));
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void setupHighLatitudeSpinner() {
        try {
            if (this.spinnerHighLatitude != null && this.prefs != null) {
                final String[] strArr = {"none", "MiddleOfTheNight", "SeventhOfTheNight", "TwilightAngle"};
                this.spinnerHighLatitude.setAdapter((SpinnerAdapter) new GoldSpinnerAdapter(this, new String[]{"بدون تعديل", "منتصف الليل", "سُبع الليل", "زاوية الشفق"}));
                String string = this.prefs.getString("high_latitude_rule", "none");
                int i2 = 0;
                while (true) {
                    if (i2 >= 4) {
                        break;
                    }
                    if (strArr[i2].equals(string)) {
                        this.spinnerHighLatitude.setSelection(i2);
                        break;
                    }
                    i2++;
                }
                this.spinnerHighLatitude.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.salatak.app.SettingsActivity.4
                    @Override // android.widget.AdapterView.OnItemSelectedListener
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i3, long j2) {
                        try {
                            SettingsActivity.this.prefs.edit().putString("high_latitude_rule", strArr[i3]).apply();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }

                    @Override // android.widget.AdapterView.OnItemSelectedListener
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void setupMadhabSpinner() {
        try {
            if (this.spinnerMadhab != null && this.prefs != null) {
                final String[] strArr = {"Shafi", "Hanafi"};
                this.spinnerMadhab.setAdapter((SpinnerAdapter) new GoldSpinnerAdapter(this, new String[]{"شافعي", "حنفي"}));
                String string = this.prefs.getString("madhab", "Shafi");
                int i2 = 0;
                while (true) {
                    if (i2 >= 2) {
                        break;
                    }
                    if (strArr[i2].equals(string)) {
                        this.spinnerMadhab.setSelection(i2);
                        break;
                    }
                    i2++;
                }
                this.spinnerMadhab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.salatak.app.SettingsActivity.3
                    @Override // android.widget.AdapterView.OnItemSelectedListener
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i3, long j2) {
                        try {
                            SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                            edit.putString("madhab", strArr[i3]);
                            edit.apply();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }

                    @Override // android.widget.AdapterView.OnItemSelectedListener
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في إعداد المذاهب", 0).show();
        }
    }

    private void setupManualLocation() {
        try {
            updateManualLocationStatus();
            LinearLayout linearLayout = this.layoutManualLocation;
            if (linearLayout != null) {
                linearLayout.setOnClickListener(new i2(this, 0));
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void setupSwitches() {
        LinearLayout linearLayout;
        try {
            SharedPreferences sharedPreferences = this.prefs;
            if (sharedPreferences == null) {
                return;
            }
            SwitchCompat switchCompat = this.switchNotifications;
            if (switchCompat != null && this.layoutNotifications != null) {
                switchCompat.setChecked(sharedPreferences.getBoolean("notifications_enabled", true));
                updateLayoutContentDescription(this.layoutNotifications, "تفعيل الإشعارات", this.switchNotifications.isChecked());
                this.layoutNotifications.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.SettingsActivity.6
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SettingsActivity.this.switchNotifications.toggle();
                    }
                });
                this.switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.salatak.app.SettingsActivity.7
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                        try {
                            SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                            edit.putBoolean("notifications_enabled", z2);
                            edit.apply();
                            SettingsActivity settingsActivity = SettingsActivity.this;
                            settingsActivity.updateLayoutContentDescription(settingsActivity.layoutNotifications, "تفعيل الإشعارات", z2);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }
            SwitchCompat switchCompat2 = this.switchVibration;
            if (switchCompat2 != null && this.layoutVibration != null) {
                switchCompat2.setChecked(this.prefs.getBoolean("vibration_enabled", true));
                updateLayoutContentDescription(this.layoutVibration, "اهتزاز الهاتف", this.switchVibration.isChecked());
                this.layoutVibration.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.SettingsActivity.8
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SettingsActivity.this.switchVibration.toggle();
                    }
                });
                this.switchVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.salatak.app.SettingsActivity.9
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                        try {
                            SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                            edit.putBoolean("vibration_enabled", z2);
                            edit.apply();
                            SettingsActivity settingsActivity = SettingsActivity.this;
                            settingsActivity.updateLayoutContentDescription(settingsActivity.layoutVibration, "اهتزاز الهاتف", z2);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }
            SwitchCompat switchCompat3 = this.switchLED;
            if (switchCompat3 != null && this.layoutLED != null) {
                switchCompat3.setChecked(this.prefs.getBoolean("led_enabled", false));
                updateLayoutContentDescription(this.layoutLED, "مؤشر LED", this.switchLED.isChecked());
                this.layoutLED.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.SettingsActivity.10
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SettingsActivity.this.switchLED.toggle();
                    }
                });
                this.switchLED.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.salatak.app.SettingsActivity.11
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                        try {
                            SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                            edit.putBoolean("led_enabled", z2);
                            edit.apply();
                            SettingsActivity settingsActivity = SettingsActivity.this;
                            settingsActivity.updateLayoutContentDescription(settingsActivity.layoutLED, "مؤشر LED", z2);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }
            SwitchCompat switchCompat4 = this.switchAdhan;
            if (switchCompat4 != null && this.layoutAdhan != null) {
                switchCompat4.setChecked(this.prefs.getBoolean("adhan_enabled", true));
                updateLayoutContentDescription(this.layoutAdhan, "تشغيل صوت الأذان", this.switchAdhan.isChecked());
                this.layoutAdhan.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.SettingsActivity.12
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SettingsActivity.this.switchAdhan.toggle();
                    }
                });
                this.switchAdhan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.salatak.app.SettingsActivity.13
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                        try {
                            SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                            edit.putBoolean("adhan_enabled", z2);
                            edit.apply();
                            SettingsActivity settingsActivity = SettingsActivity.this;
                            settingsActivity.updateLayoutContentDescription(settingsActivity.layoutAdhan, "تشغيل صوت الأذان", z2);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }
            SwitchCompat switchCompat5 = this.switchFlipToSilence;
            if (switchCompat5 != null && this.layoutFlipToSilence != null) {
                switchCompat5.setChecked(this.prefs.getBoolean("flip_to_silence_enabled", true));
                updateLayoutContentDescription(this.layoutFlipToSilence, "إيقاف الأذان عند قلب الهاتف", this.switchFlipToSilence.isChecked());
                this.layoutFlipToSilence.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.SettingsActivity.14
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SettingsActivity.this.switchFlipToSilence.toggle();
                    }
                });
                this.switchFlipToSilence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.salatak.app.SettingsActivity.15
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                        try {
                            SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                            edit.putBoolean("flip_to_silence_enabled", z2);
                            edit.apply();
                            SettingsActivity settingsActivity = SettingsActivity.this;
                            settingsActivity.updateLayoutContentDescription(settingsActivity.layoutFlipToSilence, "إيقاف الأذان عند قلب الهاتف", z2);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }
            SwitchCompat switchCompat6 = this.switchShakeToSilence;
            if (switchCompat6 != null && this.layoutShakeToSilence != null) {
                switchCompat6.setChecked(this.prefs.getBoolean("shake_to_silence_enabled", false));
                updateLayoutContentDescription(this.layoutShakeToSilence, "إيقاف الأذان عند هز الهاتف", this.switchShakeToSilence.isChecked());
                this.layoutShakeToSilence.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.SettingsActivity.16
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SettingsActivity.this.switchShakeToSilence.toggle();
                    }
                });
                this.switchShakeToSilence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.salatak.app.SettingsActivity.17
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                        try {
                            SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                            edit.putBoolean("shake_to_silence_enabled", z2);
                            edit.apply();
                            SettingsActivity settingsActivity = SettingsActivity.this;
                            settingsActivity.updateLayoutContentDescription(settingsActivity.layoutShakeToSilence, "إيقاف الأذان عند هز الهاتف", z2);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }
            SwitchCompat switchCompat7 = this.switchTapToSilence;
            if (switchCompat7 != null && this.layoutTapToSilence != null) {
                switchCompat7.setChecked(this.prefs.getBoolean("tap_to_silence_enabled", false));
                updateLayoutContentDescription(this.layoutTapToSilence, "إيقاف الأذان بالنقر على ظهر الهاتف", this.switchTapToSilence.isChecked());
                this.layoutTapToSilence.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.SettingsActivity.18
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SettingsActivity.this.switchTapToSilence.toggle();
                    }
                });
                this.switchTapToSilence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.salatak.app.SettingsActivity.19
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                        try {
                            SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                            edit.putBoolean("tap_to_silence_enabled", z2);
                            edit.apply();
                            SettingsActivity settingsActivity = SettingsActivity.this;
                            settingsActivity.updateLayoutContentDescription(settingsActivity.layoutTapToSilence, "إيقاف الأذان بالنقر على ظهر الهاتف", z2);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }
            SwitchCompat switchCompat8 = this.switchNotificationOnly;
            if (switchCompat8 != null && this.layoutNotificationOnly != null) {
                switchCompat8.setChecked(this.prefs.getBoolean("notification_only_mode", false));
                updateLayoutContentDescription(this.layoutNotificationOnly, "إشعار فقط بدون نافذة منبثقة", this.switchNotificationOnly.isChecked());
                this.layoutNotificationOnly.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.SettingsActivity.20
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SettingsActivity.this.switchNotificationOnly.toggle();
                    }
                });
                this.switchNotificationOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.salatak.app.SettingsActivity.21
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                        try {
                            SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                            edit.putBoolean("notification_only_mode", z2);
                            edit.apply();
                            SettingsActivity settingsActivity = SettingsActivity.this;
                            settingsActivity.updateLayoutContentDescription(settingsActivity.layoutNotificationOnly, "إشعار فقط بدون نافذة منبثقة", z2);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }
            SwitchCompat switchCompat9 = this.switchVolumeToSilence;
            if (switchCompat9 != null && this.layoutVolumeToSilence != null) {
                switchCompat9.setChecked(this.prefs.getBoolean("volume_to_silence_enabled", false));
                updateLayoutContentDescription(this.layoutVolumeToSilence, "إيقاف الأذان عند رفع الصوت", this.switchVolumeToSilence.isChecked());
                this.layoutVolumeToSilence.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.SettingsActivity.22
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SettingsActivity.this.switchVolumeToSilence.toggle();
                    }
                });
                this.switchVolumeToSilence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.salatak.app.SettingsActivity.23
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                        try {
                            SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                            edit.putBoolean("volume_to_silence_enabled", z2);
                            edit.apply();
                            SettingsActivity settingsActivity = SettingsActivity.this;
                            settingsActivity.updateLayoutContentDescription(settingsActivity.layoutVolumeToSilence, "إيقاف الأذان عند رفع الصوت", z2);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }
            SwitchCompat switchCompat10 = this.switchUseMediaVolume;
            if (switchCompat10 != null && this.layoutUseMediaVolume != null) {
                switchCompat10.setChecked(this.prefs.getBoolean("use_media_volume", false));
                updateLayoutContentDescription(this.layoutUseMediaVolume, "مخرجات صوت الوسائط", this.switchUseMediaVolume.isChecked());
                this.layoutUseMediaVolume.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.SettingsActivity.24
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SettingsActivity.this.switchUseMediaVolume.toggle();
                    }
                });
                this.switchUseMediaVolume.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.salatak.app.SettingsActivity.25
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                        try {
                            SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                            edit.putBoolean("use_media_volume", z2);
                            edit.apply();
                            SettingsActivity settingsActivity = SettingsActivity.this;
                            settingsActivity.updateLayoutContentDescription(settingsActivity.layoutUseMediaVolume, "مخرجات صوت الوسائط", z2);
                            if (z2) {
                                Toast.makeText(SettingsActivity.this, "سيتم استخدام مستوى صوت الوسائط للتنبيهات", 0).show();
                            } else {
                                Toast.makeText(SettingsActivity.this, "سيتم استخدام مستوى صوت المنبه للتنبيهات", 0).show();
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }
            SwitchCompat switchCompat11 = this.switchSummerTime;
            if (switchCompat11 != null && this.layoutSummerTime != null) {
                switchCompat11.setChecked(this.prefs.getBoolean("summer_time_enabled", false));
                updateLayoutContentDescription(this.layoutSummerTime, "التوقيت الصيفي", this.switchSummerTime.isChecked());
                this.layoutSummerTime.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.SettingsActivity.26
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SettingsActivity.this.switchSummerTime.toggle();
                    }
                });
                this.switchSummerTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.salatak.app.SettingsActivity.27
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                        try {
                            SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                            edit.putBoolean("summer_time_enabled", z2);
                            edit.apply();
                            SettingsActivity settingsActivity = SettingsActivity.this;
                            settingsActivity.updateLayoutContentDescription(settingsActivity.layoutSummerTime, "التوقيت الصيفي", z2);
                            PrayerReminderScheduler.scheduleAllAlarms(SettingsActivity.this);
                            Toast.makeText(SettingsActivity.this, "تم تحديث أوقات الصلاة", 0).show();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }
            SwitchCompat switchCompat12 = this.switchAzkar;
            int i2 = 8;
            if (switchCompat12 != null && this.layoutAzkar != null) {
                switchCompat12.setChecked(this.prefs.getBoolean("azkar_enabled", false));
                updateLayoutContentDescription(this.layoutAzkar, "تفعيل الأذكار", this.switchAzkar.isChecked());
                LinearLayout linearLayout2 = this.layoutAzkarSettings;
                if (linearLayout2 != null) {
                    linearLayout2.setVisibility(this.switchAzkar.isChecked() ? 0 : 8);
                }
                this.layoutAzkar.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.SettingsActivity.28
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SettingsActivity.this.switchAzkar.toggle();
                    }
                });
                this.switchAzkar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.salatak.app.SettingsActivity.29
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                        if (z2) {
                            try {
                                if (!Settings.canDrawOverlays(SettingsActivity.this)) {
                                    SettingsActivity.this.switchAzkar.setChecked(false);
                                    Toast.makeText(SettingsActivity.this, "سيتم نقلك لإعدادات التطبيق لتفعيل الظهور فوق التطبيقات الأخرى", 1).show();
                                    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                                    intent.setData(Uri.parse("package:" + SettingsActivity.this.getPackageName()));
                                    SettingsActivity.this.startActivity(intent);
                                    return;
                                }
                                if (!AzkarService.canScheduleExactAlarms(SettingsActivity.this)) {
                                    Toast.makeText(SettingsActivity.this, "تنبيه: لم يتم منح إذن التنبيهات الدقيقة. سيتم استخدام تنبيهات تقريبية. للحصول على دقة أفضل، اضغط هنا لمنح الإذن.", 1).show();
                                }
                            } catch (Exception e2) {
                                e2.printStackTrace();
                                return;
                            }
                        }
                        SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                        edit.putBoolean("azkar_enabled", z2);
                        edit.apply();
                        SettingsActivity settingsActivity = SettingsActivity.this;
                        settingsActivity.updateLayoutContentDescription(settingsActivity.layoutAzkar, "تفعيل الأذكار", z2);
                        if (SettingsActivity.this.layoutAzkarSettings != null) {
                            SettingsActivity.this.layoutAzkarSettings.setVisibility(z2 ? 0 : 8);
                        }
                        if (!z2) {
                            AzkarService.cancelAzkar(SettingsActivity.this);
                            Toast.makeText(SettingsActivity.this, "تم إيقاف الأذكار", 0).show();
                            return;
                        }
                        AzkarService.scheduleAzkar(SettingsActivity.this, SettingsActivity.this.prefs.getInt("azkar_interval_minutes", 30));
                        if (AzkarService.canScheduleExactAlarms(SettingsActivity.this)) {
                            Toast.makeText(SettingsActivity.this, "تم تفعيل الأذكار", 0).show();
                        } else {
                            Toast.makeText(SettingsActivity.this, "تم تفعيل الأذكار (بتنبيهات تقريبية)", 0).show();
                        }
                    }
                });
            }
            SwitchCompat switchCompat13 = this.switchAzkarTextNotification;
            if (switchCompat13 != null && this.layoutAzkarTextNotification != null) {
                switchCompat13.setChecked(this.prefs.getBoolean("azkar_text_notification_enabled", true));
                updateLayoutContentDescription(this.layoutAzkarTextNotification, "إشعارات نصية للأذكار", this.switchAzkarTextNotification.isChecked());
                this.layoutAzkarTextNotification.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.SettingsActivity.30
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SettingsActivity.this.switchAzkarTextNotification.toggle();
                    }
                });
                this.switchAzkarTextNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.salatak.app.SettingsActivity.31
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                        try {
                            SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                            edit.putBoolean("azkar_text_notification_enabled", z2);
                            edit.apply();
                            SettingsActivity settingsActivity = SettingsActivity.this;
                            settingsActivity.updateLayoutContentDescription(settingsActivity.layoutAzkarTextNotification, "إشعارات نصية للأذكار", z2);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }
            SwitchCompat switchCompat14 = this.switchAzkarVibration;
            if (switchCompat14 != null && this.layoutAzkarVibration != null) {
                switchCompat14.setChecked(this.prefs.getBoolean("azkar_vibration_enabled", true));
                updateLayoutContentDescription(this.layoutAzkarVibration, "اهتزاز الأذكار", this.switchAzkarVibration.isChecked());
                this.layoutAzkarVibration.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.SettingsActivity.32
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SettingsActivity.this.switchAzkarVibration.toggle();
                    }
                });
                this.switchAzkarVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.salatak.app.SettingsActivity.33
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                        try {
                            SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                            edit.putBoolean("azkar_vibration_enabled", z2);
                            edit.apply();
                            SettingsActivity settingsActivity = SettingsActivity.this;
                            settingsActivity.updateLayoutContentDescription(settingsActivity.layoutAzkarVibration, "اهتزاز الأذكار", z2);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }
            SwitchCompat switchCompat15 = this.switchAzkarAudio;
            if (switchCompat15 != null && this.layoutAzkarAudio != null) {
                switchCompat15.setChecked(this.prefs.getBoolean("azkar_audio_enabled", false));
                updateLayoutContentDescription(this.layoutAzkarAudio, "أذكار صوتية", this.switchAzkarAudio.isChecked());
                boolean isChecked = this.switchAzkarAudio.isChecked();
                LinearLayout linearLayout3 = this.layoutAzkarAutoMode;
                if (linearLayout3 != null) {
                    linearLayout3.setVisibility(isChecked ? 0 : 8);
                }
                boolean z2 = this.prefs.getBoolean("azkar_audio_auto_mode", true);
                SwitchCompat switchCompat16 = this.switchAzkarAutoMode;
                if (switchCompat16 != null) {
                    switchCompat16.setChecked(z2);
                }
                Button button = this.btnSelectAudioAzkar;
                if (button != null) {
                    if (isChecked && !z2) {
                        i2 = 0;
                    }
                    button.setVisibility(i2);
                }
                this.layoutAzkarAudio.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.SettingsActivity.34
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SettingsActivity.this.switchAzkarAudio.toggle();
                    }
                });
                this.switchAzkarAudio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.salatak.app.SettingsActivity.35
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z3) {
                        try {
                            SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                            edit.putBoolean("azkar_audio_enabled", z3);
                            edit.apply();
                            SettingsActivity settingsActivity = SettingsActivity.this;
                            settingsActivity.updateLayoutContentDescription(settingsActivity.layoutAzkarAudio, "أذكار صوتية", z3);
                            int i3 = 8;
                            if (SettingsActivity.this.layoutAzkarAutoMode != null) {
                                SettingsActivity.this.layoutAzkarAutoMode.setVisibility(z3 ? 0 : 8);
                            }
                            boolean z4 = SettingsActivity.this.prefs.getBoolean("azkar_audio_auto_mode", true);
                            if (SettingsActivity.this.btnSelectAudioAzkar != null) {
                                Button button2 = SettingsActivity.this.btnSelectAudioAzkar;
                                if (z3 && !z4) {
                                    i3 = 0;
                                }
                                button2.setVisibility(i3);
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
            }
            if (this.switchAzkarAutoMode == null || (linearLayout = this.layoutAzkarAutoMode) == null) {
                return;
            }
            linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.SettingsActivity.36
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    SettingsActivity.this.switchAzkarAutoMode.toggle();
                }
            });
            this.switchAzkarAutoMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.salatak.app.SettingsActivity.37
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z3) {
                    try {
                        SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                        edit.putBoolean("azkar_audio_auto_mode", z3);
                        edit.apply();
                        if (SettingsActivity.this.btnSelectAudioAzkar != null) {
                            SettingsActivity.this.btnSelectAudioAzkar.setVisibility(!z3 ? 0 : 8);
                        }
                        if (z3) {
                            Toast.makeText(SettingsActivity.this, "تم تفعيل الوضع التلقائي - أذكار عشوائية", 0).show();
                        } else {
                            Toast.makeText(SettingsActivity.this, "اختر ذكر محدد للتشغيل", 0).show();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            });
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في إعداد المفاتيح", 0).show();
        }
    }

    private void setupTimeFormatSpinner() {
        try {
            if (this.spinnerTimeFormat != null && this.prefs != null) {
                final String[] strArr = {"12", "24"};
                this.spinnerTimeFormat.setAdapter((SpinnerAdapter) new GoldSpinnerAdapter(this, new String[]{"12 ساعة (صباحاً/مساءً)", "24 ساعة"}));
                String string = this.prefs.getString("time_format", "12");
                int i2 = 0;
                while (true) {
                    if (i2 >= 2) {
                        break;
                    }
                    if (strArr[i2].equals(string)) {
                        this.spinnerTimeFormat.setSelection(i2);
                        break;
                    }
                    i2++;
                }
                this.spinnerTimeFormat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.salatak.app.SettingsActivity.5
                    @Override // android.widget.AdapterView.OnItemSelectedListener
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i3, long j2) {
                        try {
                            SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                            edit.putString("time_format", strArr[i3]);
                            edit.apply();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }

                    @Override // android.widget.AdapterView.OnItemSelectedListener
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في إعداد تنسيق الساعة", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAudioAzkarDialog() {
        try {
            final String[] strArr = {"الحمد لله", "لا حول ولا قوة إلا بالله", "استغفر الله", "مقلب القلوب", "الصلاة على محمد", "الصلاة على محمد 2", "الصلاة على محمد وآل محمد", "الصلاة على محمد وآله", "سبحانك وبحمدك", "الله أكبر", "التوحيد", "سبحان الله", "التسبيح والحمد", "التسبيح والحمد 2", "تبارك الله"};
            final String[] strArr2 = {"hamd.mp3", "haoqala.mp3", "istghfar.mp3", "moqalib_alquloub.mp3", "sal_ala_muhammad.mp3", "sal_ala_muhammad_2.mp3", "sal_ala_muhammad_waali_muhammad.mp3", "sal_ala_muhammad_waalih.mp3", "sobhank_wbihamdik.mp3", "takbir.mp3", "taohid.mp3", "tasbih.mp3", "tasbih_wahamd.mp3", "tasbih_wahamd_2.mp3", "tbarakt.mp3"};
            final int[] iArr = {this.prefs.getInt("selected_audio_azkar_index", 0)};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("اختر الذكر الصوتي");
            builder.setSingleChoiceItems(strArr, iArr[0], new DialogInterface.OnClickListener() { // from class: com.salatak.app.SettingsActivity.40
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    iArr[0] = i2;
                    SettingsActivity.this.playAzkarAudio(strArr2[i2]);
                }
            });
            builder.setPositiveButton("حفظ", new DialogInterface.OnClickListener() { // from class: com.salatak.app.SettingsActivity.41
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    SharedPreferences.Editor edit = SettingsActivity.this.prefs.edit();
                    edit.putInt("selected_audio_azkar_index", iArr[0]);
                    edit.putString("selected_audio_azkar_file", strArr2[iArr[0]]);
                    edit.putString("selected_audio_azkar_text", strArr[iArr[0]]);
                    edit.apply();
                    Toast.makeText(SettingsActivity.this, "تم حفظ الذكر الصوتي: " + strArr[iArr[0]], 0).show();
                }
            });
            builder.setNegativeButton("إلغاء", (DialogInterface.OnClickListener) null);
            AlertDialog create = builder.create();
            create.show();
            styleAzkarDialogGold(create);
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في عرض قائمة الأذكار", 0).show();
        }
    }

    private void styleAzkarDialogGold(AlertDialog alertDialog) {
        TextView textView;
        if (alertDialog == null || alertDialog.getWindow() == null) {
            return;
        }
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(-15068152));
        try {
            int identifier = getResources().getIdentifier("alertTitle", TtmlNode.ATTR_ID, "android");
            if (identifier > 0 && (textView = (TextView) alertDialog.findViewById(identifier)) != null) {
                textView.setTextColor(-666250);
            }
        } catch (Exception unused) {
        }
        try {
            Button button = alertDialog.getButton(-1);
            Button button2 = alertDialog.getButton(-2);
            if (button != null) {
                button.setTextColor(-666250);
            }
            if (button2 != null) {
                button2.setTextColor(-3888819);
            }
        } catch (Exception unused2) {
        }
        try {
            ListView listView = alertDialog.getListView();
            if (listView != null) {
                listView.setBackgroundColor(-15068152);
                listView.setDivider(new ColorDrawable(-12767728));
                listView.setDividerHeight(1);
                listView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() { // from class: com.salatak.app.SettingsActivity.42
                    @Override // android.view.ViewGroup.OnHierarchyChangeListener
                    public void onChildViewAdded(View view, View view2) {
                        if (view2 instanceof TextView) {
                            ((TextView) view2).setTextColor(-666250);
                        }
                    }

                    @Override // android.view.ViewGroup.OnHierarchyChangeListener
                    public void onChildViewRemoved(View view, View view2) {
                    }
                });
            }
        } catch (Exception unused3) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLayoutContentDescription(LinearLayout linearLayout, String str, boolean z2) {
        if (linearLayout != null) {
            linearLayout.setContentDescription((z2 ? "مفعّل" : "غير مفعّل") + " — " + str);
        }
    }

    private void updateManualLocationStatus() {
        SharedPreferences sharedPreferences;
        try {
            if (this.tvManualLocationStatus != null && (sharedPreferences = this.prefs) != null) {
                if (!sharedPreferences.getBoolean("manual_location_enabled", false)) {
                    this.tvManualLocationStatus.setText("غير مفعّل - يستخدم الموقع التلقائي");
                    this.tvManualLocationStatus.setTextColor(-2132228000);
                    return;
                }
                String string = this.prefs.getString("manual_location_name", "");
                if (string.isEmpty()) {
                    this.tvManualLocationStatus.setText("مفعّل - موقع يدوي");
                } else {
                    this.tvManualLocationStatus.setText("مفعّل: ".concat(string));
                }
                this.tvManualLocationStatus.setTextColor(-11751600);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            setContentView(R.layout.activity_settings);
            getWindow().setStatusBarColor(-15068152);
            getWindow().setNavigationBarColor(-15068152);
            this.prefs = getSharedPreferences("SalatakPrefs", 0);
            ImageView imageView = (ImageView) findViewById(R.id.btnBack);
            TextView textView = (TextView) findViewById(R.id.tvTitle);
            this.spinnerCalculationMethod = (Spinner) findViewById(R.id.spinnerCalculationMethod);
            this.spinnerMadhab = (Spinner) findViewById(R.id.spinnerMadhab);
            this.spinnerHighLatitude = (Spinner) findViewById(R.id.spinnerHighLatitude);
            this.spinnerTimeFormat = (Spinner) findViewById(R.id.spinnerTimeFormat);
            this.spinnerAzkarInterval = (Spinner) findViewById(R.id.spinnerAzkarInterval);
            this.layoutNotifications = (LinearLayout) findViewById(R.id.layoutNotifications);
            this.layoutVibration = (LinearLayout) findViewById(R.id.layoutVibration);
            this.layoutLED = (LinearLayout) findViewById(R.id.layoutLED);
            this.layoutAdhan = (LinearLayout) findViewById(R.id.layoutAdhan);
            this.layoutFlipToSilence = (LinearLayout) findViewById(R.id.layoutFlipToSilence);
            this.layoutAzkar = (LinearLayout) findViewById(R.id.layoutAzkar);
            this.layoutAzkarSettings = (LinearLayout) findViewById(R.id.layoutAzkarSettings);
            this.layoutAzkarTextNotification = (LinearLayout) findViewById(R.id.layoutAzkarTextNotification);
            this.layoutAzkarVibration = (LinearLayout) findViewById(R.id.layoutAzkarVibration);
            this.layoutAzkarAudio = (LinearLayout) findViewById(R.id.layoutAzkarAudio);
            this.layoutAzkarAutoMode = (LinearLayout) findViewById(R.id.layoutAzkarAutoMode);
            this.layoutSummerTime = (LinearLayout) findViewById(R.id.layoutSummerTime);
            this.layoutVolumeToSilence = (LinearLayout) findViewById(R.id.layoutVolumeToSilence);
            this.layoutUseMediaVolume = (LinearLayout) findViewById(R.id.layoutUseMediaVolume);
            this.layoutNotificationOnly = (LinearLayout) findViewById(R.id.layoutNotificationOnly);
            this.switchNotificationOnly = (SwitchCompat) findViewById(R.id.switchNotificationOnly);
            this.layoutShakeToSilence = (LinearLayout) findViewById(R.id.layoutShakeToSilence);
            this.switchShakeToSilence = (SwitchCompat) findViewById(R.id.switchShakeToSilence);
            this.layoutTapToSilence = (LinearLayout) findViewById(R.id.layoutTapToSilence);
            this.switchTapToSilence = (SwitchCompat) findViewById(R.id.switchTapToSilence);
            this.layoutManualLocation = (LinearLayout) findViewById(R.id.layoutManualLocation);
            this.tvManualLocationStatus = (TextView) findViewById(R.id.tvManualLocationStatus);
            this.switchNotifications = (SwitchCompat) findViewById(R.id.switchNotifications);
            this.switchVibration = (SwitchCompat) findViewById(R.id.switchVibration);
            this.switchLED = (SwitchCompat) findViewById(R.id.switchLED);
            this.switchAdhan = (SwitchCompat) findViewById(R.id.switchAdhan);
            this.switchFlipToSilence = (SwitchCompat) findViewById(R.id.switchFlipToSilence);
            this.switchSummerTime = (SwitchCompat) findViewById(R.id.switchSummerTime);
            this.switchVolumeToSilence = (SwitchCompat) findViewById(R.id.switchVolumeToSilence);
            this.switchUseMediaVolume = (SwitchCompat) findViewById(R.id.switchUseMediaVolume);
            this.switchAzkar = (SwitchCompat) findViewById(R.id.switchAzkar);
            this.switchAzkarTextNotification = (SwitchCompat) findViewById(R.id.switchAzkarTextNotification);
            this.switchAzkarVibration = (SwitchCompat) findViewById(R.id.switchAzkarVibration);
            this.switchAzkarAudio = (SwitchCompat) findViewById(R.id.switchAzkarAudio);
            this.switchAzkarAutoMode = (SwitchCompat) findViewById(R.id.switchAzkarAutoMode);
            this.btnSelectAudioAzkar = (Button) findViewById(R.id.btnSelectAudioAzkar);
            if (textView != null) {
                textView.setText("صلاتك - الإعدادات");
            }
            if (imageView != null) {
                imageView.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.SettingsActivity.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SettingsActivity.this.finish();
                    }
                });
            }
            setupCalculationMethodSpinner();
            setupMadhabSpinner();
            setupHighLatitudeSpinner();
            setupTimeFormatSpinner();
            setupAzkarIntervalSpinner();
            setupSwitches();
            setupAudioAzkarButton();
            setupManualLocation();
            setupDiagnosticButtons();
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في تحميل الإعدادات", 0).show();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        updateManualLocationStatus();
    }
}
