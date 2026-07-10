package com.salatak.app;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationCompat;
import androidx.media3.datasource.cache.ContentMetadata;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.salatak.app.helpers.PrayerTimesCalculator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class PrayerSettingsActivity extends AppCompatActivity {
    private AlertDialog currentAdhanDialog;
    private int currentAdjustmentHours = 0;
    private int currentAdjustmentMinutes = 0;
    private ActivityResultLauncher<String[]> customAdhanPicker;
    private LinearLayout layoutAdhanNotification;
    private LinearLayout layoutAdhanSettings;
    private LinearLayout layoutAdhanVolume;
    private LinearLayout layoutDuaAfterAdhan;
    private LinearLayout layoutIqama;
    private LinearLayout layoutIqamaSettings;
    private LinearLayout layoutReminderBeforeAdhan;
    private LinearLayout layoutReminderSettings;
    private LinearLayout layoutSelectAdhanSound;
    private LinearLayout layoutSelectIqamaTime;
    private LinearLayout layoutSelectReminderSound;
    private LinearLayout layoutSelectReminderTime;
    private View layoutTimeAdjustment;
    private MediaPlayer mediaPlayer;
    private ActivityResultLauncher<Intent> muezzinLauncher;
    private String prayerKey;
    private String prayerName;
    private SharedPreferences prefs;
    private SeekBar seekBarAdhanVolume;
    private SwitchCompat switchAdhanNotification;
    private SwitchCompat switchDuaAfterAdhan;
    private SwitchCompat switchIqama;
    private SwitchCompat switchReminderBeforeAdhan;
    private TextView tvAdhanSoundValue;
    private TextView tvAdhanVolumeValue;
    private TextView tvIqamaTimeValue;
    private TextView tvReminderSoundValue;
    private TextView tvReminderTimeValue;
    private TextView tvTimeAdjustment;

    private void checkAndRequestAlarmPermission() {
        AlarmManager alarmManager;
        boolean canScheduleExactAlarms;
        if (Build.VERSION.SDK_INT < 31 || (alarmManager = (AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM)) == null) {
            return;
        }
        canScheduleExactAlarms = alarmManager.canScheduleExactAlarms();
        if (canScheduleExactAlarms) {
            return;
        }
        new AlertDialog.Builder(this).setTitle("تفعيل التنبيهات الدقيقة").setMessage("لتفعيل تنبيهات الصلاة، يرجى السماح للتطبيق بجدولة التنبيهات الدقيقة من إعدادات النظام.").setPositiveButton("فتح الإعدادات", new e1(this, 0)).setNegativeButton("لاحقاً", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String getFileDisplayName(android.net.Uri r8) {
        /*
            r7 = this;
            java.lang.String r0 = "مؤذن مخصص"
            android.content.ContentResolver r1 = r7.getContentResolver()     // Catch: java.lang.Exception -> L3b
            r5 = 0
            r6 = 0
            r3 = 0
            r4 = 0
            r2 = r8
            android.database.Cursor r8 = r1.query(r2, r3, r4, r5, r6)     // Catch: java.lang.Exception -> L3b
            if (r8 == 0) goto L43
            java.lang.String r1 = "_display_name"
            int r1 = r8.getColumnIndex(r1)     // Catch: java.lang.Exception -> L3b
            boolean r2 = r8.moveToFirst()     // Catch: java.lang.Exception -> L3b
            if (r2 == 0) goto L3e
            if (r1 < 0) goto L3e
            java.lang.String r1 = r8.getString(r1)     // Catch: java.lang.Exception -> L3b
            if (r1 == 0) goto L3f
            java.lang.String r2 = "."
            boolean r2 = r1.contains(r2)     // Catch: java.lang.Exception -> L39
            if (r2 == 0) goto L3f
            r2 = 46
            int r2 = r1.lastIndexOf(r2)     // Catch: java.lang.Exception -> L39
            r3 = 0
            java.lang.String r1 = r1.substring(r3, r2)     // Catch: java.lang.Exception -> L39
            goto L3f
        L39:
            r8 = move-exception
            goto L45
        L3b:
            r8 = move-exception
            r1 = r0
            goto L45
        L3e:
            r1 = r0
        L3f:
            r8.close()     // Catch: java.lang.Exception -> L39
            goto L48
        L43:
            r1 = r0
            goto L48
        L45:
            r8.printStackTrace()
        L48:
            if (r1 == 0) goto L4b
            r0 = r1
        L4b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.salatak.app.PrayerSettingsActivity.getFileDisplayName(android.net.Uri):java.lang.String");
    }

    private int[] getOriginalPrayerTime24h() {
        char c;
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("SalatakPrefs", 0);
            double d = sharedPreferences.getFloat("latitude", 21.4225f);
            double d2 = sharedPreferences.getFloat("longitude", 39.8262f);
            int i2 = this.prefs.getInt(this.prayerKey + "_adjustment", 0);
            this.prefs.edit().putInt(this.prayerKey + "_adjustment", 0).apply();
            PrayerTimesCalculator.PrayerTimesResult calculatePrayerTimes = PrayerTimesCalculator.calculatePrayerTimes(this, d, d2);
            this.prefs.edit().putInt(this.prayerKey + "_adjustment", i2).apply();
            String str = this.prayerKey;
            switch (str.hashCode()) {
                case -1856560363:
                    if (str.equals("sunrise")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 96896:
                    if (str.equals("asr")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 3135299:
                    if (str.equals("fajr")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 3241891:
                    if (str.equals("isha")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 95566139:
                    if (str.equals("dhuhr")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 829014902:
                    if (str.equals("maghrib")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            String str2 = c != 0 ? c != 1 ? c != 2 ? c != 3 ? c != 4 ? c != 5 ? null : calculatePrayerTimes.isha24h : calculatePrayerTimes.maghrib24h : calculatePrayerTimes.asr24h : calculatePrayerTimes.dhuhr24h : calculatePrayerTimes.sunrise24h : calculatePrayerTimes.fajr24h;
            if (str2 != null && str2.contains(":")) {
                String[] split = str2.split(":");
                return new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1])};
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return new int[]{0, 0};
    }

    private String getSoundFileName(String str, String str2) {
        String str3;
        str3 = str2.equals("now") ? "now.mp3" : "soon.mp3";
        str.getClass();
        switch (str) {
            case "sunrise":
                return "gom3a".concat(str3);
            case "asr":
                return "asr".concat(str3);
            case "fajr":
                return "fagr".concat(str3);
            case "isha":
                return "eshaa".concat(str3);
            case "dhuhr":
                return "zohr".concat(str3);
            case "maghrib":
                return "maghrib".concat(str3);
            default:
                return null;
        }
    }

    private void handleCustomAdhanSelected(Uri uri) {
        try {
            String str = ContentMetadata.KEY_CUSTOM_PREFIX + System.currentTimeMillis();
            File file = new File(getFilesDir(), "adhan_sounds");
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(file, str);
            InputStream openInputStream = getContentResolver().openInputStream(uri);
            if (openInputStream == null) {
                Toast.makeText(this, "فشل قراءة الملف", 0).show();
                return;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            byte[] bArr = new byte[4096];
            while (true) {
                int read = openInputStream.read(bArr);
                if (read == -1) {
                    break;
                } else {
                    fileOutputStream.write(bArr, 0, read);
                }
            }
            fileOutputStream.close();
            openInputStream.close();
            String fileDisplayName = getFileDisplayName(uri);
            SharedPreferences.Editor edit = this.prefs.edit();
            edit.putString(this.prayerKey + "_adhan_sound_id", str);
            edit.putString("custom_adhan_name_" + str, fileDisplayName);
            edit.apply();
            updateAdhanSoundText(str);
            PrayerReminderScheduler.scheduleAdhanAlarms(this);
            AlertDialog alertDialog = this.currentAdhanDialog;
            if (alertDialog != null && alertDialog.isShowing()) {
                this.currentAdhanDialog.dismiss();
            }
            Toast.makeText(this, "تم تفعيل: " + fileDisplayName, 0).show();
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "فشل استيراد الملف الصوتي", 0).show();
        }
    }

    private void initViews() {
        ImageView imageView = (ImageView) findViewById(R.id.btnBack);
        TextView textView = (TextView) findViewById(R.id.tvTitle);
        this.layoutReminderBeforeAdhan = (LinearLayout) findViewById(R.id.layoutReminderBeforeAdhan);
        this.layoutReminderSettings = (LinearLayout) findViewById(R.id.layoutReminderSettings);
        this.layoutSelectReminderTime = (LinearLayout) findViewById(R.id.layoutSelectReminderTime);
        this.layoutSelectReminderSound = (LinearLayout) findViewById(R.id.layoutSelectReminderSound);
        this.switchReminderBeforeAdhan = (SwitchCompat) findViewById(R.id.switchReminderBeforeAdhan);
        this.tvReminderTimeValue = (TextView) findViewById(R.id.tvReminderTimeValue);
        this.tvReminderSoundValue = (TextView) findViewById(R.id.tvReminderSoundValue);
        this.layoutAdhanNotification = (LinearLayout) findViewById(R.id.layoutAdhanNotification);
        this.layoutAdhanSettings = (LinearLayout) findViewById(R.id.layoutAdhanSettings);
        this.layoutSelectAdhanSound = (LinearLayout) findViewById(R.id.layoutSelectAdhanSound);
        this.switchAdhanNotification = (SwitchCompat) findViewById(R.id.switchAdhanNotification);
        this.tvAdhanSoundValue = (TextView) findViewById(R.id.tvAdhanSoundValue);
        this.layoutTimeAdjustment = findViewById(R.id.layoutTimeAdjustment);
        this.tvTimeAdjustment = (TextView) findViewById(R.id.tvTimeAdjustment);
        this.layoutAdhanVolume = (LinearLayout) findViewById(R.id.layoutAdhanVolume);
        this.seekBarAdhanVolume = (SeekBar) findViewById(R.id.seekBarAdhanVolume);
        this.tvAdhanVolumeValue = (TextView) findViewById(R.id.tvAdhanVolumeValue);
        this.layoutDuaAfterAdhan = (LinearLayout) findViewById(R.id.layoutDuaAfterAdhan);
        this.switchDuaAfterAdhan = (SwitchCompat) findViewById(R.id.switchDuaAfterAdhan);
        this.layoutIqama = (LinearLayout) findViewById(R.id.layoutIqama);
        this.layoutIqamaSettings = (LinearLayout) findViewById(R.id.layoutIqamaSettings);
        this.layoutSelectIqamaTime = (LinearLayout) findViewById(R.id.layoutSelectIqamaTime);
        this.switchIqama = (SwitchCompat) findViewById(R.id.switchIqama);
        this.tvIqamaTimeValue = (TextView) findViewById(R.id.tvIqamaTimeValue);
        if (textView != null) {
            textView.setText("إعدادات صلاة " + this.prayerName);
        }
        if (imageView != null) {
            imageView.setOnClickListener(new f1(this, 9));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkAndRequestAlarmPermission$2(DialogInterface dialogInterface, int i2) {
        try {
            Intent intent = new Intent("android.settings.REQUEST_SCHEDULE_EXACT_ALARM");
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception unused) {
            Toast.makeText(this, "فشل فتح الإعدادات", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$3(View view) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(Uri uri) {
        if (uri != null) {
            handleCustomAdhanSelected(uri);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(ActivityResult activityResult) {
        updateAdhanSoundText(this.prefs.getString(this.prayerKey + "_adhan_sound_id", ""));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playTestSound$24(MediaPlayer mediaPlayer) {
        stopMediaPlayer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$playTestSound$25(MediaPlayer mediaPlayer, int i2, int i3) {
        stopMediaPlayer();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$10(View view) {
        showAdhanSoundPickerDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$11(View view) {
        showTimeAdjustmentDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$12(View view) {
        this.switchDuaAfterAdhan.toggle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$13(CompoundButton compoundButton, boolean z2) {
        try {
            SharedPreferences.Editor edit = this.prefs.edit();
            edit.putBoolean(this.prayerKey + "_dua_after_adhan_enabled", z2);
            edit.apply();
            updateLayoutContentDescription(this.layoutDuaAfterAdhan, "تشغيل الدعاء بعد الأذان", z2);
            if (z2) {
                Toast.makeText(this, "تم تفعيل الدعاء بعد الأذان لصلاة " + this.prayerName, 0).show();
            } else {
                Toast.makeText(this, "تم إيقاف الدعاء بعد الأذان لصلاة " + this.prayerName, 0).show();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$14(View view) {
        this.switchIqama.toggle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$15(CompoundButton compoundButton, boolean z2) {
        try {
            SharedPreferences.Editor edit = this.prefs.edit();
            edit.putBoolean(this.prayerKey + "_iqama_enabled", z2);
            edit.apply();
            updateLayoutContentDescription(this.layoutIqama, "تفعيل إقامة الصلاة", z2);
            LinearLayout linearLayout = this.layoutIqamaSettings;
            if (linearLayout != null) {
                linearLayout.setVisibility(z2 ? 0 : 8);
            }
            if (!z2) {
                PrayerReminderScheduler.cancelIqamaForPrayer(this, this.prayerKey);
            }
            PrayerReminderScheduler.scheduleAllAlarms(this);
            if (z2) {
                Toast.makeText(this, "تم تفعيل الإقامة لصلاة " + this.prayerName, 0).show();
            } else {
                Toast.makeText(this, "تم إيقاف الإقامة لصلاة " + this.prayerName, 0).show();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$16(View view) {
        showIqamaTimeDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$4(View view) {
        this.switchReminderBeforeAdhan.toggle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$5(CompoundButton compoundButton, boolean z2) {
        try {
            SharedPreferences.Editor edit = this.prefs.edit();
            edit.putBoolean(this.prayerKey + "_reminder_enabled", z2);
            edit.apply();
            updateLayoutContentDescription(this.layoutReminderBeforeAdhan, "تفعيل التنبيه قبل الأذان", z2);
            LinearLayout linearLayout = this.layoutReminderSettings;
            if (linearLayout != null) {
                linearLayout.setVisibility(z2 ? 0 : 8);
            }
            PrayerReminderScheduler.schedulePrayerReminders(this);
            if (z2) {
                Toast.makeText(this, "تم تفعيل التنبيه لصلاة " + this.prayerName, 0).show();
            } else {
                Toast.makeText(this, "تم إيقاف التنبيه لصلاة " + this.prayerName, 0).show();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$6(View view) {
        this.switchAdhanNotification.toggle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$7(CompoundButton compoundButton, boolean z2) {
        try {
            SharedPreferences.Editor edit = this.prefs.edit();
            edit.putBoolean(this.prayerKey + "_adhan_enabled", z2);
            edit.apply();
            updateLayoutContentDescription(this.layoutAdhanNotification, "تفعيل تنبيه وقت الأذان", z2);
            LinearLayout linearLayout = this.layoutAdhanSettings;
            if (linearLayout != null) {
                linearLayout.setVisibility(z2 ? 0 : 8);
            }
            PrayerReminderScheduler.scheduleAdhanAlarms(this);
            if (z2) {
                Toast.makeText(this, "تم تفعيل الأذان لصلاة " + this.prayerName, 0).show();
            } else {
                Toast.makeText(this, "تم إيقاف الأذان لصلاة " + this.prayerName, 0).show();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$8(View view) {
        showReminderTimeDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$9(View view) {
        showReminderSoundDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showIqamaTimeDialog$22(int[] iArr, String[] strArr, DialogInterface dialogInterface, int i2) {
        try {
            SharedPreferences.Editor edit = this.prefs.edit();
            edit.putInt(this.prayerKey + "_iqama_minutes", iArr[i2]);
            edit.apply();
            updateIqamaTimeText(iArr[i2]);
            PrayerReminderScheduler.cancelIqamaForPrayer(this, this.prayerKey);
            PrayerReminderScheduler.scheduleAllAlarms(this);
            Toast.makeText(this, "تم تحديث وقت الإقامة إلى " + strArr[i2], 0).show();
            dialogInterface.dismiss();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showReminderTimeDialog$20(int[] iArr, String[] strArr, DialogInterface dialogInterface, int i2) {
        try {
            SharedPreferences.Editor edit = this.prefs.edit();
            edit.putInt(this.prayerKey + "_reminder_minutes", iArr[i2]);
            edit.apply();
            updateReminderTimeText(iArr[i2]);
            PrayerReminderScheduler.schedulePrayerReminders(this);
            Toast.makeText(this, "تم تحديث وقت التنبيه إلى " + strArr[i2], 0).show();
            dialogInterface.dismiss();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showTimeAdjustmentDialog$17(EditText editText, EditText editText2, int i2, int i3, TextView textView) {
        try {
            String trim = editText.getText().toString().trim();
            String trim2 = editText2.getText().toString().trim();
            int parseInt = (((!trim.isEmpty() ? Integer.parseInt(trim) : 0) * 60) + (trim2.isEmpty() ? 0 : Integer.parseInt(trim2))) - ((i2 * 60) + i3);
            textView.setText("الفرق: " + (parseInt > 0 ? "+" : "") + parseInt + " دقيقة");
        } catch (NumberFormatException unused) {
            textView.setText("الفرق: 0 دقيقة");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showTimeAdjustmentDialog$18(EditText editText, EditText editText2, int i2, int i3, DialogInterface dialogInterface, int i4) {
        try {
            String trim = editText.getText().toString().trim();
            String trim2 = editText2.getText().toString().trim();
            int parseInt = !trim.isEmpty() ? Integer.parseInt(trim) : 0;
            int parseInt2 = !trim2.isEmpty() ? Integer.parseInt(trim2) : 0;
            if (parseInt < 0) {
                parseInt = 0;
            }
            if (parseInt > 23) {
                parseInt = 23;
            }
            if (parseInt2 < 0) {
                parseInt2 = 0;
            }
            if (parseInt2 > 59) {
                parseInt2 = 59;
            }
            int i5 = ((parseInt * 60) + parseInt2) - ((i2 * 60) + i3);
            if (i5 > 720) {
                i5 -= 1440;
            }
            if (i5 < -720) {
                i5 += 1440;
            }
            this.currentAdjustmentHours = 0;
            this.currentAdjustmentMinutes = i5;
            saveAdjustment();
            Toast.makeText(this, "تم حفظ تعديل الوقت", 0).show();
        } catch (NumberFormatException unused) {
            Toast.makeText(this, "يرجى إدخال أرقام صحيحة", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showTimeAdjustmentDialog$19(DialogInterface dialogInterface, int i2) {
        this.currentAdjustmentHours = 0;
        this.currentAdjustmentMinutes = 0;
        saveAdjustment();
        Toast.makeText(this, "تم إعادة تعيين الوقت", 0).show();
    }

    private void loadSettings() {
        try {
            boolean z2 = this.prefs.getBoolean(this.prayerKey + "_reminder_enabled", false);
            int i2 = this.prefs.getInt(this.prayerKey + "_reminder_minutes", 5);
            String string = this.prefs.getString(this.prayerKey + "_reminder_sound", "soon");
            boolean z3 = this.prefs.getBoolean(this.prayerKey + "_adhan_enabled", false);
            String string2 = this.prefs.getString(this.prayerKey + "_adhan_sound_id", "");
            SwitchCompat switchCompat = this.switchReminderBeforeAdhan;
            if (switchCompat != null) {
                switchCompat.setChecked(z2);
            }
            LinearLayout linearLayout = this.layoutReminderSettings;
            int i3 = 8;
            if (linearLayout != null) {
                linearLayout.setVisibility(z2 ? 0 : 8);
            }
            SwitchCompat switchCompat2 = this.switchAdhanNotification;
            if (switchCompat2 != null) {
                switchCompat2.setChecked(z3);
            }
            LinearLayout linearLayout2 = this.layoutAdhanSettings;
            if (linearLayout2 != null) {
                linearLayout2.setVisibility(z3 ? 0 : 8);
            }
            boolean z4 = this.prefs.getBoolean(this.prayerKey + "_dua_after_adhan_enabled", false);
            SwitchCompat switchCompat3 = this.switchDuaAfterAdhan;
            if (switchCompat3 != null) {
                switchCompat3.setChecked(z4);
            }
            boolean z5 = this.prefs.getBoolean(this.prayerKey + "_iqama_enabled", false);
            int i4 = this.prefs.getInt(this.prayerKey + "_iqama_minutes", 10);
            SwitchCompat switchCompat4 = this.switchIqama;
            if (switchCompat4 != null) {
                switchCompat4.setChecked(z5);
            }
            LinearLayout linearLayout3 = this.layoutIqamaSettings;
            if (linearLayout3 != null) {
                linearLayout3.setVisibility(z5 ? 0 : 8);
            }
            updateIqamaTimeText(i4);
            updateReminderTimeText(i2);
            updateReminderSoundText(string);
            updateAdhanSoundText(string2);
            boolean z6 = this.prefs.getBoolean("use_media_volume", false);
            LinearLayout linearLayout4 = this.layoutAdhanVolume;
            if (linearLayout4 != null) {
                if (z3 && !z6) {
                    i3 = 0;
                }
                linearLayout4.setVisibility(i3);
            }
            int i5 = this.prefs.getInt(this.prayerKey + "_adhan_volume", 80);
            SeekBar seekBar = this.seekBarAdhanVolume;
            if (seekBar != null) {
                seekBar.setProgress(i5);
            }
            TextView textView = this.tvAdhanVolumeValue;
            if (textView != null) {
                textView.setText(i5 + "%");
            }
            int i6 = this.prefs.getInt(this.prayerKey + "_adjustment", 0);
            this.currentAdjustmentHours = 0;
            this.currentAdjustmentMinutes = i6;
            updateAdjustmentText();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void playTestSound(String str) {
        try {
            stopMediaPlayer();
            String soundFileName = getSoundFileName(this.prayerKey, str);
            if (soundFileName == null) {
                Toast.makeText(this, "صوت غير متوفر لهذه الصلاة", 0).show();
                return;
            }
            this.mediaPlayer = new MediaPlayer();
            AssetFileDescriptor openFd = getAssets().openFd("prayer_reminders/".concat(soundFileName));
            if (openFd == null) {
                Toast.makeText(this, "خطأ في تحميل الصوت", 0).show();
                return;
            }
            this.mediaPlayer.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
            openFd.close();
            this.mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setUsage(4).setContentType(4).build());
            this.mediaPlayer.setOnCompletionListener(new v0(5, this));
            this.mediaPlayer.setOnErrorListener(new w0(3, this));
            this.mediaPlayer.prepare();
            this.mediaPlayer.start();
            Toast.makeText(this, "جاري تشغيل الصوت...", 0).show();
        } catch (IOException e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في تشغيل الصوت", 0).show();
            stopMediaPlayer();
        }
    }

    private void saveAdjustment() {
        int i2 = (this.currentAdjustmentHours * 60) + this.currentAdjustmentMinutes;
        updateAdjustmentText();
        SharedPreferences.Editor edit = this.prefs.edit();
        edit.putInt(this.prayerKey + "_adjustment", i2);
        edit.apply();
        PrayerReminderScheduler.scheduleAllAlarms(this);
    }

    private void setupListeners() {
        SwitchCompat switchCompat;
        SwitchCompat switchCompat2;
        SwitchCompat switchCompat3;
        SwitchCompat switchCompat4;
        LinearLayout linearLayout = this.layoutReminderBeforeAdhan;
        if (linearLayout != null && (switchCompat4 = this.switchReminderBeforeAdhan) != null) {
            updateLayoutContentDescription(linearLayout, "تفعيل التنبيه قبل الأذان", switchCompat4.isChecked());
            this.layoutReminderBeforeAdhan.setOnClickListener(new f1(this, 0));
            final int i2 = 1;
            this.switchReminderBeforeAdhan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(this) { // from class: com.salatak.app.g1
                public final /* synthetic */ PrayerSettingsActivity b;

                {
                    this.b = this;
                }

                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public final void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                    switch (i2) {
                        case 0:
                            this.b.lambda$setupListeners$15(compoundButton, z2);
                            break;
                        case 1:
                            this.b.lambda$setupListeners$5(compoundButton, z2);
                            break;
                        case 2:
                            this.b.lambda$setupListeners$7(compoundButton, z2);
                            break;
                        default:
                            this.b.lambda$setupListeners$13(compoundButton, z2);
                            break;
                    }
                }
            });
        }
        LinearLayout linearLayout2 = this.layoutAdhanNotification;
        if (linearLayout2 != null && (switchCompat3 = this.switchAdhanNotification) != null) {
            updateLayoutContentDescription(linearLayout2, "تفعيل تنبيه وقت الأذان", switchCompat3.isChecked());
            this.layoutAdhanNotification.setOnClickListener(new f1(this, 3));
            final int i3 = 2;
            this.switchAdhanNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(this) { // from class: com.salatak.app.g1
                public final /* synthetic */ PrayerSettingsActivity b;

                {
                    this.b = this;
                }

                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public final void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                    switch (i3) {
                        case 0:
                            this.b.lambda$setupListeners$15(compoundButton, z2);
                            break;
                        case 1:
                            this.b.lambda$setupListeners$5(compoundButton, z2);
                            break;
                        case 2:
                            this.b.lambda$setupListeners$7(compoundButton, z2);
                            break;
                        default:
                            this.b.lambda$setupListeners$13(compoundButton, z2);
                            break;
                    }
                }
            });
        }
        LinearLayout linearLayout3 = this.layoutSelectReminderTime;
        if (linearLayout3 != null) {
            linearLayout3.setOnClickListener(new f1(this, 4));
        }
        LinearLayout linearLayout4 = this.layoutSelectReminderSound;
        if (linearLayout4 != null) {
            linearLayout4.setOnClickListener(new f1(this, 5));
        }
        LinearLayout linearLayout5 = this.layoutSelectAdhanSound;
        if (linearLayout5 != null) {
            linearLayout5.setOnClickListener(new f1(this, 6));
        }
        View view = this.layoutTimeAdjustment;
        if (view != null) {
            view.setOnClickListener(new f1(this, 7));
        }
        LinearLayout linearLayout6 = this.layoutDuaAfterAdhan;
        if (linearLayout6 != null && (switchCompat2 = this.switchDuaAfterAdhan) != null) {
            updateLayoutContentDescription(linearLayout6, "تشغيل الدعاء بعد الأذان", switchCompat2.isChecked());
            this.layoutDuaAfterAdhan.setOnClickListener(new f1(this, 8));
            final int i4 = 3;
            this.switchDuaAfterAdhan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(this) { // from class: com.salatak.app.g1
                public final /* synthetic */ PrayerSettingsActivity b;

                {
                    this.b = this;
                }

                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public final void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                    switch (i4) {
                        case 0:
                            this.b.lambda$setupListeners$15(compoundButton, z2);
                            break;
                        case 1:
                            this.b.lambda$setupListeners$5(compoundButton, z2);
                            break;
                        case 2:
                            this.b.lambda$setupListeners$7(compoundButton, z2);
                            break;
                        default:
                            this.b.lambda$setupListeners$13(compoundButton, z2);
                            break;
                    }
                }
            });
        }
        LinearLayout linearLayout7 = this.layoutIqama;
        if (linearLayout7 != null && (switchCompat = this.switchIqama) != null) {
            updateLayoutContentDescription(linearLayout7, "تفعيل إقامة الصلاة", switchCompat.isChecked());
            this.layoutIqama.setOnClickListener(new f1(this, 1));
            final int i5 = 0;
            this.switchIqama.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(this) { // from class: com.salatak.app.g1
                public final /* synthetic */ PrayerSettingsActivity b;

                {
                    this.b = this;
                }

                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public final void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                    switch (i5) {
                        case 0:
                            this.b.lambda$setupListeners$15(compoundButton, z2);
                            break;
                        case 1:
                            this.b.lambda$setupListeners$5(compoundButton, z2);
                            break;
                        case 2:
                            this.b.lambda$setupListeners$7(compoundButton, z2);
                            break;
                        default:
                            this.b.lambda$setupListeners$13(compoundButton, z2);
                            break;
                    }
                }
            });
        }
        LinearLayout linearLayout8 = this.layoutSelectIqamaTime;
        if (linearLayout8 != null) {
            linearLayout8.setOnClickListener(new f1(this, 2));
        }
        SeekBar seekBar = this.seekBarAdhanVolume;
        if (seekBar != null) {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.salatak.app.PrayerSettingsActivity.1
                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onProgressChanged(SeekBar seekBar2, int i6, boolean z2) {
                    if (PrayerSettingsActivity.this.tvAdhanVolumeValue != null) {
                        PrayerSettingsActivity.this.tvAdhanVolumeValue.setText(i6 + "%");
                    }
                }

                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onStartTrackingTouch(SeekBar seekBar2) {
                }

                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onStopTrackingTouch(SeekBar seekBar2) {
                    PrayerSettingsActivity.this.prefs.edit().putInt(PrayerSettingsActivity.this.prayerKey + "_adhan_volume", seekBar2.getProgress()).apply();
                }
            });
        }
    }

    private void showAdhanSoundPickerDialog() {
        try {
            stopMediaPlayer();
            Intent intent = new Intent(this, (Class<?>) MuezzinActivity.class);
            intent.putExtra("prayer_key", this.prayerKey);
            intent.putExtra("prayer_name", this.prayerName);
            this.muezzinLauncher.launch(intent);
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في فتح شاشة المؤذنين", 0).show();
        }
    }

    private void showIqamaTimeDialog() {
        try {
            String[] strArr = {"5 دقائق", "10 دقائق", "15 دقيقة", "20 دقيقة"};
            int[] iArr = {5, 10, 15, 20};
            int i2 = this.prefs.getInt(this.prayerKey + "_iqama_minutes", 10);
            int i3 = 0;
            while (true) {
                if (i3 >= 4) {
                    i3 = 1;
                    break;
                } else if (iArr[i3] == i2) {
                    break;
                } else {
                    i3++;
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("الإقامة بعد الأذان بـ");
            builder.setSingleChoiceItems(strArr, i3, new h1(this, iArr, strArr, 1));
            builder.setNegativeButton("إلغاء", new k1(1));
            AlertDialog create = builder.create();
            create.show();
            styleDialogGold(create);
            styleListDialogGold(create);
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في عرض خيارات الإقامة", 0).show();
        }
    }

    private void showReminderSoundDialog() {
        try {
            playTestSound("soon");
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في تشغيل الصوت", 0).show();
        }
    }

    private void showReminderTimeDialog() {
        try {
            String[] strArr = {"5 دقائق", "10 دقائق", "15 دقيقة", "20 دقيقة", "30 دقيقة"};
            int[] iArr = {5, 10, 15, 20, 30};
            int i2 = this.prefs.getInt(this.prayerKey + "_reminder_minutes", 5);
            int i3 = 0;
            while (true) {
                if (i3 >= 5) {
                    i3 = 0;
                    break;
                } else if (iArr[i3] == i2) {
                    break;
                } else {
                    i3++;
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("اختر وقت التنبيه قبل الأذان");
            builder.setSingleChoiceItems(strArr, i3, new h1(this, iArr, strArr, 0));
            builder.setNegativeButton("إلغاء", new k1(0));
            AlertDialog create = builder.create();
            create.show();
            styleDialogGold(create);
            styleListDialogGold(create);
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في عرض خيارات الوقت", 0).show();
        }
    }

    private void showTimeAdjustmentDialog() {
        try {
            View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_time_adjustment, (ViewGroup) null);
            final EditText editText = (EditText) inflate.findViewById(R.id.etHourValue);
            final EditText editText2 = (EditText) inflate.findViewById(R.id.etMinuteValue);
            final TextView textView = (TextView) inflate.findViewById(R.id.tvTotalAdjustment);
            TextView textView2 = (TextView) inflate.findViewById(R.id.tvPrayerCurrentTime);
            int[] originalPrayerTime24h = getOriginalPrayerTime24h();
            final int i2 = originalPrayerTime24h[0];
            final int i3 = originalPrayerTime24h[1];
            int i4 = (((((i2 * 60) + i3) + ((this.currentAdjustmentHours * 60) + this.currentAdjustmentMinutes)) % 1440) + 1440) % 1440;
            editText.setText(String.valueOf(i4 / 60));
            editText2.setText(String.valueOf(i4 % 60));
            if (textView2 != null) {
                textView2.setText("الوقت الأصلي: ".concat(String.format("%02d:%02d", Integer.valueOf(i2), Integer.valueOf(i3))));
            }
            final Runnable runnable = new Runnable() { // from class: com.salatak.app.i1
                @Override // java.lang.Runnable
                public final void run() {
                    PrayerSettingsActivity.lambda$showTimeAdjustmentDialog$17(editText, editText2, i2, i3, textView);
                }
            };
            runnable.run();
            TextWatcher textWatcher = new TextWatcher() { // from class: com.salatak.app.PrayerSettingsActivity.2
                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    runnable.run();
                }

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i5, int i6, int i7) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i5, int i6, int i7) {
                }
            };
            editText.addTextChangedListener(textWatcher);
            editText2.addTextChangedListener(textWatcher);
            AlertDialog create = new AlertDialog.Builder(this).setView(inflate).setPositiveButton("حفظ", new DialogInterface.OnClickListener() { // from class: com.salatak.app.j1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i5) {
                    PrayerSettingsActivity.this.lambda$showTimeAdjustmentDialog$18(editText, editText2, i2, i3, dialogInterface, i5);
                }
            }).setNegativeButton("إلغاء", (DialogInterface.OnClickListener) null).setNeutralButton("إعادة تعيين", new e1(this, 1)).create();
            create.show();
            styleDialogGold(create);
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في عرض نافذة التعديل", 0).show();
        }
    }

    private void stopMediaPlayer() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    this.mediaPlayer.stop();
                }
                this.mediaPlayer.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            this.mediaPlayer = null;
        }
    }

    private void styleDialogGold(AlertDialog alertDialog) {
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
            Button button3 = alertDialog.getButton(-3);
            if (button != null) {
                button.setTextColor(-666250);
            }
            if (button2 != null) {
                button2.setTextColor(-3888819);
            }
            if (button3 != null) {
                button3.setTextColor(-1521568);
            }
        } catch (Exception unused2) {
        }
    }

    private void styleListDialogGold(AlertDialog alertDialog) {
        if (alertDialog == null) {
            return;
        }
        try {
            ListView listView = alertDialog.getListView();
            if (listView != null) {
                listView.setBackgroundColor(-15068152);
                listView.setDivider(new ColorDrawable(-12767728));
                listView.setDividerHeight(1);
                for (int i2 = 0; i2 < listView.getChildCount(); i2++) {
                    View childAt = listView.getChildAt(i2);
                    if (childAt instanceof TextView) {
                        ((TextView) childAt).setTextColor(-666250);
                    }
                }
                listView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() { // from class: com.salatak.app.PrayerSettingsActivity.3
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
        } catch (Exception unused) {
        }
    }

    private void updateAdhanSoundText(String str) {
        if (this.tvAdhanSoundValue != null) {
            if (str == null || str.isEmpty()) {
                this.tvAdhanSoundValue.setText("لم يتم الاختيار");
                return;
            }
            if ("none".equals(str)) {
                this.tvAdhanSoundValue.setText("بدون مؤذن (تنبيه فقط)");
            } else if (!str.startsWith(ContentMetadata.KEY_CUSTOM_PREFIX)) {
                this.tvAdhanSoundValue.setText(AdhanSoundsData.getNameById(str));
            } else {
                this.tvAdhanSoundValue.setText(this.prefs.getString("custom_adhan_name_".concat(str), "مؤذن مخصص"));
            }
        }
    }

    private void updateAdjustmentText() {
        String str;
        TextView textView = this.tvTimeAdjustment;
        if (textView != null) {
            int i2 = (this.currentAdjustmentHours * 60) + this.currentAdjustmentMinutes;
            if (i2 == 0) {
                textView.setText("بدون تعديل");
                return;
            }
            String str2 = i2 > 0 ? "+" : "";
            int abs = Math.abs(i2);
            int i3 = abs / 60;
            int i4 = abs % 60;
            if (i3 <= 0 || i4 <= 0) {
                str = str2 + i2 + " دقيقة";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(i2);
                sb.append(" دقيقة (");
                sb.append(i2 < 0 ? "-" : "+");
                sb.append(i3);
                sb.append(" ساعة ");
                sb.append(i4);
                sb.append(" دقيقة)");
                str = sb.toString();
            }
            this.tvTimeAdjustment.setText(str);
        }
    }

    private void updateIqamaTimeText(int i2) {
        String str;
        if (this.tvIqamaTimeValue != null) {
            if (i2 == 5) {
                str = "5 دقائق";
            } else if (i2 == 10) {
                str = "10 دقائق";
            } else if (i2 == 15) {
                str = "15 دقيقة";
            } else if (i2 == 20) {
                str = "20 دقيقة";
            } else {
                str = i2 + " دقيقة";
            }
            this.tvIqamaTimeValue.setText(str);
            LinearLayout linearLayout = this.layoutSelectIqamaTime;
            if (linearLayout != null) {
                linearLayout.setContentDescription("الإقامة بعد الأذان بـ " + str);
            }
        }
    }

    private void updateLayoutContentDescription(LinearLayout linearLayout, String str, boolean z2) {
        if (linearLayout != null) {
            linearLayout.setContentDescription(str + ", " + (z2 ? "مفعّل" : "غير مفعّل"));
        }
    }

    private void updateReminderSoundText(String str) {
        TextView textView = this.tvReminderSoundValue;
        if (textView != null) {
            textView.setText("صوت \"قريباً\"");
            LinearLayout linearLayout = this.layoutSelectReminderSound;
            if (linearLayout != null) {
                linearLayout.setContentDescription("صوت التنبيه: صوت قريباً - اضغط لتجربة الصوت");
            }
        }
    }

    private void updateReminderTimeText(int i2) {
        String str;
        if (this.tvReminderTimeValue != null) {
            if (i2 == 5) {
                str = "5 دقائق";
            } else if (i2 == 10) {
                str = "10 دقائق";
            } else if (i2 == 15) {
                str = "15 دقيقة";
            } else if (i2 == 20) {
                str = "20 دقيقة";
            } else if (i2 == 30) {
                str = "30 دقيقة";
            } else {
                str = i2 + " دقيقة";
            }
            this.tvReminderTimeValue.setText(str);
            LinearLayout linearLayout = this.layoutSelectReminderTime;
            if (linearLayout != null) {
                linearLayout.setContentDescription("وقت التنبيه قبل الأذان: " + str);
            }
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            final int i2 = 0;
            this.customAdhanPicker = registerForActivityResult(new ActivityResultContracts.OpenDocument(), new ActivityResultCallback(this) { // from class: com.salatak.app.l1
                public final /* synthetic */ PrayerSettingsActivity c;

                {
                    this.c = this;
                }

                @Override // androidx.activity.result.ActivityResultCallback
                public final void onActivityResult(Object obj) {
                    switch (i2) {
                        case 0:
                            this.c.lambda$onCreate$0((Uri) obj);
                            break;
                        default:
                            this.c.lambda$onCreate$1((ActivityResult) obj);
                            break;
                    }
                }
            });
            final int i3 = 1;
            this.muezzinLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback(this) { // from class: com.salatak.app.l1
                public final /* synthetic */ PrayerSettingsActivity c;

                {
                    this.c = this;
                }

                @Override // androidx.activity.result.ActivityResultCallback
                public final void onActivityResult(Object obj) {
                    switch (i3) {
                        case 0:
                            this.c.lambda$onCreate$0((Uri) obj);
                            break;
                        default:
                            this.c.lambda$onCreate$1((ActivityResult) obj);
                            break;
                    }
                }
            });
            setContentView(R.layout.activity_prayer_settings);
            getWindow().setStatusBarColor(-15068152);
            getWindow().setNavigationBarColor(-15068152);
            this.prayerKey = getIntent().getStringExtra("prayer_key");
            String stringExtra = getIntent().getStringExtra("prayer_name");
            this.prayerName = stringExtra;
            if (this.prayerKey != null && stringExtra != null) {
                this.prefs = getSharedPreferences("SalatakPrefs", 0);
                initViews();
                setupListeners();
                loadSettings();
                checkAndRequestAlarmPermission();
                return;
            }
            Toast.makeText(this, "خطأ في تحميل إعدادات الصلاة", 0).show();
            finish();
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في تحميل الإعدادات", 0).show();
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        stopMediaPlayer();
    }
}
