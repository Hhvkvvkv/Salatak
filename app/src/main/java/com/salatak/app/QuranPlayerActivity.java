package com.salatak.app;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.salatak.app.QuranPlayerActivity;
import com.salatak.app.QuranPlayerService;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class QuranPlayerActivity extends AppCompatActivity {
    private ImageView btnDownload;
    private ImageView btnForward;
    private ImageView btnNext;
    private ImageView btnPlayPause;
    private ImageView btnPrevious;
    private ImageView btnRewind;
    private TextView btnSpeed;
    private LinearLayout downloadContainer;
    private ProgressBar progressPlayer;
    private QuranPlayerService quranService;
    private String reciterName;
    private SeekBar seekBar;
    private LinearLayout speedContainer;
    private int surahIndex;
    private String surahName;
    private String surahUrl;
    private String surahsJsonStr;
    private TextView tvCurrentTime;
    private TextView tvDownloadLabel;
    private TextView tvDuration;
    private TextView tvPlayerStatus;
    private TextView tvReciterName;
    private TextView tvSurahName;
    private TextView tvToolbarTitle;
    private boolean isDownloaded = false;
    private boolean serviceBound = false;
    private ArrayList<String[]> trackList = new ArrayList<>();
    private final Handler seekHandler = new Handler(Looper.getMainLooper());
    private boolean userSeeking = false;
    private final float[] SPEED_OPTIONS = {0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f};
    private int currentSpeedIndex = 2;
    private final Runnable seekRunnable = new Runnable() { // from class: com.salatak.app.QuranPlayerActivity.1
        @Override // java.lang.Runnable
        public void run() {
            if (QuranPlayerActivity.this.serviceBound && QuranPlayerActivity.this.quranService != null && !QuranPlayerActivity.this.userSeeking) {
                long currentPosition = QuranPlayerActivity.this.quranService.getCurrentPosition();
                long duration = QuranPlayerActivity.this.quranService.getDuration();
                QuranPlayerActivity.this.seekBar.setMax((int) duration);
                QuranPlayerActivity.this.seekBar.setProgress((int) currentPosition);
                QuranPlayerActivity.this.tvCurrentTime.setText(QuranPlayerActivity.this.formatTime(currentPosition));
                QuranPlayerActivity.this.tvDuration.setText(QuranPlayerActivity.this.formatTime(duration));
            }
            QuranPlayerActivity.this.seekHandler.postDelayed(this, 500L);
        }
    };
    private final ServiceConnection serviceConnection = new ServiceConnection() { // from class: com.salatak.app.QuranPlayerActivity.2

        /* renamed from: com.salatak.app.QuranPlayerActivity$2$1, reason: invalid class name */
        public class AnonymousClass1 implements QuranPlayerService.QuranPlayerCallback {
            public AnonymousClass1() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onStateChanged$0(int i2, boolean z2, boolean z3) {
                QuranPlayerActivity.this.updateUI(i2, z2, z3);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onTrackChanged$1(int i2, String str) {
                QuranPlayerActivity.this.surahIndex = i2;
                QuranPlayerActivity.this.surahName = str;
                if (i2 >= 0 && i2 < QuranPlayerActivity.this.trackList.size()) {
                    QuranPlayerActivity quranPlayerActivity = QuranPlayerActivity.this;
                    quranPlayerActivity.surahUrl = ((String[]) quranPlayerActivity.trackList.get(i2))[1];
                }
                QuranPlayerActivity.this.tvSurahName.setText(str);
                QuranPlayerActivity.this.tvReciterName.setText(QuranPlayerActivity.this.reciterName);
                QuranPlayerActivity.this.tvToolbarTitle.setText(str);
                QuranPlayerActivity.this.updateDownloadDeleteButton();
            }

            @Override // com.salatak.app.QuranPlayerService.QuranPlayerCallback
            public void onStateChanged(int i2, boolean z2, boolean z3) {
                QuranPlayerActivity.this.runOnUiThread(new z1(this, i2, z2, z3, 0));
            }

            @Override // com.salatak.app.QuranPlayerService.QuranPlayerCallback
            public void onTrackChanged(final String str, final int i2) {
                QuranPlayerActivity.this.runOnUiThread(new Runnable() { // from class: com.salatak.app.y1
                    @Override // java.lang.Runnable
                    public final void run() {
                        QuranPlayerActivity.AnonymousClass2.AnonymousClass1.this.lambda$onTrackChanged$1(i2, str);
                    }
                });
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            QuranPlayerActivity.this.quranService = ((QuranPlayerService.QuranBinder) iBinder).getService();
            QuranPlayerActivity.this.serviceBound = true;
            QuranPlayerActivity.this.quranService.setCallback(new AnonymousClass1());
            if (QuranPlayerActivity.this.surahUrl == null || !QuranPlayerActivity.this.surahUrl.equals(QuranPlayerActivity.this.quranService.getCurrentUrl())) {
                QuranPlayerActivity.this.quranService.setTrackList(QuranPlayerActivity.this.trackList, QuranPlayerActivity.this.surahIndex);
                QuranPlayerActivity.this.quranService.playTrack(QuranPlayerActivity.this.surahUrl, QuranPlayerActivity.this.surahName, QuranPlayerActivity.this.reciterName);
            } else {
                QuranPlayerActivity.this.updateUI(QuranPlayerActivity.this.quranService.getPlayerState(), QuranPlayerActivity.this.quranService.isPlaying(), false);
                QuranPlayerActivity.this.tvSurahName.setText(QuranPlayerActivity.this.quranService.getCurrentTitle());
                QuranPlayerActivity.this.tvReciterName.setText(QuranPlayerActivity.this.quranService.getCurrentReciter());
                QuranPlayerActivity.this.tvToolbarTitle.setText(QuranPlayerActivity.this.quranService.getCurrentTitle());
                QuranPlayerActivity quranPlayerActivity = QuranPlayerActivity.this;
                quranPlayerActivity.surahIndex = quranPlayerActivity.quranService.getCurrentIndex();
            }
            QuranPlayerActivity.this.updateSpeedButton(QuranPlayerActivity.this.quranService.getPlaybackSpeed());
            QuranPlayerActivity.this.seekHandler.post(QuranPlayerActivity.this.seekRunnable);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            QuranPlayerActivity.this.serviceBound = false;
            QuranPlayerActivity.this.quranService = null;
        }
    };

    private void buildTrackList() {
        try {
            JSONObject jSONObject = new JSONObject(this.surahsJsonStr);
            ArrayList arrayList = new ArrayList();
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                arrayList.add(new String[]{next, jSONObject.getString(next)});
            }
            Collections.sort(arrayList, new s1(this, 1));
            this.trackList.clear();
            this.trackList.addAll(arrayList);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void deleteCurrentSurah() {
        try {
            int extractSurahNumber = extractSurahNumber(this.surahName);
            File file = new File(getExternalFilesDir("quran"), this.reciterName);
            File file2 = new File(file, extractSurahNumber + ".mp3");
            File file3 = new File(file, extractSurahNumber + ".meta");
            if (file2.exists()) {
                new AlertDialog.Builder(this, 4).setTitle("حذف السورة").setMessage("هل تريد حذف " + this.surahName + "؟").setPositiveButton("حذف", new m0(this, file2, file3, 1)).setNegativeButton("إلغاء", (DialogInterface.OnClickListener) null).show();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void downloadCurrentSurah() {
        try {
            final int extractSurahNumber = extractSurahNumber(this.surahName);
            final File file = new File(getExternalFilesDir("quran"), this.reciterName);
            final File file2 = new File(file, extractSurahNumber + ".mp3");
            final File file3 = new File(file, extractSurahNumber + ".meta");
            if (file2.exists()) {
                Toast.makeText(this, "تم التحميل مسبقاً", 0).show();
                return;
            }
            if (!file.exists()) {
                file.mkdirs();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this, 4);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(1);
            linearLayout.setPadding(dp(24), dp(20), dp(24), dp(20));
            linearLayout.setGravity(17);
            TextView textView = new TextView(this);
            textView.setText("جاري تحميل: " + this.surahName);
            textView.setTextColor(-2052805);
            textView.setTextSize(16.0f);
            textView.setGravity(17);
            linearLayout.addView(textView);
            final ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            progressBar.setMax(100);
            progressBar.setProgress(0);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, dp(8));
            layoutParams.setMargins(0, dp(16), 0, dp(8));
            progressBar.setLayoutParams(layoutParams);
            progressBar.setProgressTintList(ColorStateList.valueOf(-2052805));
            linearLayout.addView(progressBar);
            final TextView textView2 = new TextView(this);
            textView2.setText("0%  •  0 KB");
            textView2.setTextColor(-7824982);
            textView2.setTextSize(13.0f);
            textView2.setGravity(17);
            linearLayout.addView(textView2);
            builder.setView(linearLayout);
            builder.setCancelable(false);
            builder.setNegativeButton("إلغاء", (DialogInterface.OnClickListener) null);
            final AlertDialog create = builder.create();
            create.show();
            final boolean[] zArr = {false};
            create.getButton(-2).setOnClickListener(new j0(6, zArr, create));
            final String str = this.surahUrl;
            final String str2 = this.surahName;
            final String str3 = this.reciterName;
            new Thread(new Runnable() { // from class: com.salatak.app.x1
                @Override // java.lang.Runnable
                public final void run() {
                    QuranPlayerActivity.this.lambda$downloadCurrentSurah$14(file, extractSurahNumber, str, zArr, create, progressBar, textView2, file2, file3, str2, str3);
                }
            }).start();
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في التحميل", 0).show();
        }
    }

    private int dp(int i2) {
        return (int) ((i2 * getResources().getDisplayMetrics().density) + 0.5f);
    }

    private int extractSurahNumber(String str) {
        try {
            StringBuilder sb = new StringBuilder();
            for (char c : str.toCharArray()) {
                if (!Character.isDigit(c)) {
                    if (sb.length() > 0) {
                        break;
                    }
                } else {
                    sb.append(c);
                }
            }
            if (sb.length() > 0) {
                return Integer.parseInt(sb.toString());
            }
            return 999;
        } catch (Exception unused) {
            return 999;
        }
    }

    private String formatBytes(long j2) {
        if (j2 >= 1024) {
            return j2 < 1048576 ? String.format(Locale.US, "%.1f KB", Double.valueOf(j2 / 1024.0d)) : String.format(Locale.US, "%.1f MB", Double.valueOf(j2 / 1048576.0d));
        }
        return j2 + " B";
    }

    private String formatSpeed(float f2) {
        int i2 = (int) f2;
        if (f2 != i2) {
            return String.format(Locale.US, "%.2gx", Float.valueOf(f2));
        }
        return i2 + "x";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String formatTime(long j2) {
        if (j2 <= 0) {
            return "0:00";
        }
        long j3 = j2 / 1000;
        long j4 = j3 / 3600;
        long j5 = (j3 % 3600) / 60;
        long j6 = j3 % 60;
        return j4 > 0 ? String.format(Locale.US, "%d:%02d:%02d", Long.valueOf(j4), Long.valueOf(j5), Long.valueOf(j6)) : String.format(Locale.US, "%d:%02d", Long.valueOf(j5), Long.valueOf(j6));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$buildTrackList$9(String[] strArr, String[] strArr2) {
        return Integer.compare(extractSurahNumber(strArr[0]), extractSurahNumber(strArr2[0]));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteCurrentSurah$15(File file, File file2, DialogInterface dialogInterface, int i2) {
        file.delete();
        file2.delete();
        Toast.makeText(this, "تم الحذف", 0).show();
        updateDownloadDeleteButton();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$downloadCurrentSurah$10(boolean[] zArr, AlertDialog alertDialog, View view) {
        zArr[0] = true;
        alertDialog.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$downloadCurrentSurah$11(boolean[] zArr, AlertDialog alertDialog, int i2, ProgressBar progressBar, TextView textView, long j2, int i3) {
        if (zArr[0] || !alertDialog.isShowing()) {
            return;
        }
        if (i2 < 0) {
            textView.setText(formatBytes(j2) + " تم تحميلها");
            return;
        }
        progressBar.setProgress(i2);
        textView.setText(i2 + "%  •  " + formatBytes(j2) + " / " + formatBytes(i3));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$downloadCurrentSurah$12(AlertDialog alertDialog, String str) {
        alertDialog.dismiss();
        Toast.makeText(this, "تم تحميل " + str + " بنجاح", 0).show();
        updateDownloadDeleteButton();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$downloadCurrentSurah$13(AlertDialog alertDialog, Exception exc) {
        alertDialog.dismiss();
        Toast.makeText(this, "فشل التحميل: " + exc.getMessage(), 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:41:0x01b1 A[Catch: all -> 0x01bb, TRY_LEAVE, TryCatch #22 {all -> 0x01bb, blocks: (B:39:0x01a6, B:41:0x01b1), top: B:38:0x01a6 }] */
    /* JADX WARN: Removed duplicated region for block: B:49:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x01c7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x01c2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:66:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x01d5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x01d0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v19 */
    /* JADX WARN: Type inference failed for: r3v6 */
    /* JADX WARN: Type inference failed for: r3v8 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public /* synthetic */ void lambda$downloadCurrentSurah$14(java.io.File r24, int r25, java.lang.String r26, final boolean[] r27, final android.app.AlertDialog r28, final android.widget.ProgressBar r29, final android.widget.TextView r30, java.io.File r31, java.io.File r32, java.lang.String r33, java.lang.String r34) {
        /*
            Method dump skipped, instructions count: 478
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.salatak.app.QuranPlayerActivity.lambda$downloadCurrentSurah$14(java.io.File, int, java.lang.String, boolean[], android.app.AlertDialog, android.widget.ProgressBar, android.widget.TextView, java.io.File, java.io.File, java.lang.String, java.lang.String):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        QuranPlayerService quranPlayerService;
        if (!this.serviceBound || (quranPlayerService = this.quranService) == null) {
            return;
        }
        quranPlayerService.togglePlayPause();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$2(View view) {
        QuranPlayerService quranPlayerService;
        if (!this.serviceBound || (quranPlayerService = this.quranService) == null) {
            return;
        }
        quranPlayerService.nextTrack();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$3(View view) {
        QuranPlayerService quranPlayerService;
        if (!this.serviceBound || (quranPlayerService = this.quranService) == null) {
            return;
        }
        quranPlayerService.previousTrack();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$4(View view) {
        QuranPlayerService quranPlayerService;
        if (!this.serviceBound || (quranPlayerService = this.quranService) == null) {
            return;
        }
        long currentPosition = quranPlayerService.getCurrentPosition() + 10000;
        long duration = this.quranService.getDuration();
        if (duration > 0 && currentPosition > duration) {
            currentPosition = duration;
        }
        this.quranService.seekTo(currentPosition);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$5(View view) {
        QuranPlayerService quranPlayerService;
        if (!this.serviceBound || (quranPlayerService = this.quranService) == null) {
            return;
        }
        long currentPosition = quranPlayerService.getCurrentPosition() - 10000;
        if (currentPosition < 0) {
            currentPosition = 0;
        }
        this.quranService.seekTo(currentPosition);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$6(View view) {
        if (this.isDownloaded) {
            deleteCurrentSurah();
        } else {
            downloadCurrentSurah();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$7(View view) {
        showSpeedDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showSpeedDialog$8(DialogInterface dialogInterface, int i2) {
        QuranPlayerService quranPlayerService;
        this.currentSpeedIndex = i2;
        float f2 = this.SPEED_OPTIONS[i2];
        if (this.serviceBound && (quranPlayerService = this.quranService) != null) {
            quranPlayerService.setPlaybackSpeed(f2);
        }
        updateSpeedButton(f2);
        dialogInterface.dismiss();
    }

    private void showSpeedDialog() {
        String[] strArr = new String[this.SPEED_OPTIONS.length];
        int i2 = 0;
        while (true) {
            float[] fArr = this.SPEED_OPTIONS;
            if (i2 >= fArr.length) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, 4);
                builder.setTitle("سرعة التشغيل");
                builder.setSingleChoiceItems(strArr, this.currentSpeedIndex, new q(3, this));
                builder.show();
                return;
            }
            strArr[i2] = formatSpeed(fArr[i2]);
            i2++;
        }
    }

    private void startAndBindService() {
        startService(new Intent(this, (Class<?>) QuranPlayerService.class));
        bindService(new Intent(this, (Class<?>) QuranPlayerService.class), this.serviceConnection, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDownloadDeleteButton() {
        try {
            int extractSurahNumber = extractSurahNumber(this.surahName);
            boolean exists = new File(new File(getExternalFilesDir("quran"), this.reciterName), extractSurahNumber + ".mp3").exists();
            this.isDownloaded = exists;
            if (exists) {
                this.btnDownload.setImageResource(R.drawable.ic_delete);
                this.tvDownloadLabel.setText("حذف");
                this.tvDownloadLabel.setTextColor(-38037);
                this.downloadContainer.setContentDescription("حذف");
            } else {
                this.btnDownload.setImageResource(R.drawable.ic_download);
                this.tvDownloadLabel.setText("تحميل");
                this.tvDownloadLabel.setTextColor(-7824982);
                this.downloadContainer.setContentDescription("تحميل");
            }
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSpeedButton(float f2) {
        this.btnSpeed.setText(formatSpeed(f2));
        this.speedContainer.setContentDescription("السرعة " + formatSpeed(f2));
        int i2 = 0;
        while (true) {
            float[] fArr = this.SPEED_OPTIONS;
            if (i2 >= fArr.length) {
                return;
            }
            if (Math.abs(fArr[i2] - f2) < 0.01f) {
                this.currentSpeedIndex = i2;
                return;
            }
            i2++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUI(int i2, boolean z2, boolean z3) {
        if (isFinishing()) {
            return;
        }
        if (z3) {
            this.tvPlayerStatus.setText("خطأ - اضغط لإعادة المحاولة");
            this.tvPlayerStatus.setTextColor(-38037);
            this.progressPlayer.setVisibility(8);
            this.btnPlayPause.setImageResource(R.drawable.ic_play_circle);
            return;
        }
        if (i2 == 1) {
            this.tvPlayerStatus.setText("جاري الاتصال...");
            this.tvPlayerStatus.setTextColor(-7824982);
            this.progressPlayer.setVisibility(0);
            this.btnPlayPause.setImageResource(R.drawable.ic_pause_circle);
            return;
        }
        if (i2 == 2) {
            this.tvPlayerStatus.setText("جاري التحميل...");
            this.tvPlayerStatus.setTextColor(-2052805);
            this.progressPlayer.setVisibility(0);
            this.btnPlayPause.setImageResource(R.drawable.ic_pause_circle);
            return;
        }
        if (i2 != 3) {
            if (i2 != 4) {
                return;
            }
            this.tvPlayerStatus.setText("انتهى التشغيل");
            this.tvPlayerStatus.setTextColor(-7824982);
            this.progressPlayer.setVisibility(8);
            this.btnPlayPause.setImageResource(R.drawable.ic_play_circle);
            return;
        }
        this.progressPlayer.setVisibility(8);
        if (z2) {
            this.tvPlayerStatus.setText("يعمل الآن");
            this.tvPlayerStatus.setTextColor(-11751600);
            this.btnPlayPause.setImageResource(R.drawable.ic_pause_circle);
        } else {
            this.tvPlayerStatus.setText("متوقف مؤقتاً");
            this.tvPlayerStatus.setTextColor(-2052805);
            this.btnPlayPause.setImageResource(R.drawable.ic_play_circle);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            Window window = getWindow();
            window.setStatusBarColor(-15918294);
            window.setNavigationBarColor(-15918294);
            setContentView(R.layout.activity_quran_player);
            this.reciterName = getIntent().getStringExtra("reciter_name");
            this.surahName = getIntent().getStringExtra("surah_name");
            this.surahUrl = getIntent().getStringExtra("surah_url");
            this.surahsJsonStr = getIntent().getStringExtra("surahs_json");
            this.surahIndex = getIntent().getIntExtra("surah_index", 0);
            if (this.reciterName != null && this.surahName != null && this.surahUrl != null && this.surahsJsonStr != null) {
                this.tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
                this.tvSurahName = (TextView) findViewById(R.id.tvSurahName);
                this.tvReciterName = (TextView) findViewById(R.id.tvReciterName);
                this.tvPlayerStatus = (TextView) findViewById(R.id.tvPlayerStatus);
                this.tvCurrentTime = (TextView) findViewById(R.id.tvCurrentTime);
                this.tvDuration = (TextView) findViewById(R.id.tvDuration);
                this.progressPlayer = (ProgressBar) findViewById(R.id.progressPlayer);
                this.seekBar = (SeekBar) findViewById(R.id.seekBar);
                this.btnPlayPause = (ImageView) findViewById(R.id.btnPlayPause);
                this.btnNext = (ImageView) findViewById(R.id.btnNext);
                this.btnPrevious = (ImageView) findViewById(R.id.btnPrevious);
                this.btnForward = (ImageView) findViewById(R.id.btnForward);
                this.btnRewind = (ImageView) findViewById(R.id.btnRewind);
                this.btnDownload = (ImageView) findViewById(R.id.btnDownload);
                this.tvDownloadLabel = (TextView) findViewById(R.id.tvDownloadLabel);
                this.downloadContainer = (LinearLayout) findViewById(R.id.downloadContainer);
                this.speedContainer = (LinearLayout) findViewById(R.id.speedContainer);
                this.btnSpeed = (TextView) findViewById(R.id.btnSpeed);
                ImageView imageView = (ImageView) findViewById(R.id.btnBack);
                this.tvSurahName.setText(this.surahName);
                this.tvReciterName.setText(this.reciterName);
                this.tvToolbarTitle.setText(this.surahName);
                this.btnPlayPause.setImageResource(R.drawable.ic_pause_circle);
                buildTrackList();
                updateDownloadDeleteButton();
                final int i2 = 5;
                imageView.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.w1
                    public final /* synthetic */ QuranPlayerActivity b;

                    {
                        this.b = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        switch (i2) {
                            case 0:
                                this.b.lambda$onCreate$3(view);
                                break;
                            case 1:
                                this.b.lambda$onCreate$4(view);
                                break;
                            case 2:
                                this.b.lambda$onCreate$5(view);
                                break;
                            case 3:
                                this.b.lambda$onCreate$6(view);
                                break;
                            case 4:
                                this.b.lambda$onCreate$7(view);
                                break;
                            case 5:
                                this.b.lambda$onCreate$0(view);
                                break;
                            case 6:
                                this.b.lambda$onCreate$1(view);
                                break;
                            default:
                                this.b.lambda$onCreate$2(view);
                                break;
                        }
                    }
                });
                final int i3 = 6;
                this.btnPlayPause.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.w1
                    public final /* synthetic */ QuranPlayerActivity b;

                    {
                        this.b = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        switch (i3) {
                            case 0:
                                this.b.lambda$onCreate$3(view);
                                break;
                            case 1:
                                this.b.lambda$onCreate$4(view);
                                break;
                            case 2:
                                this.b.lambda$onCreate$5(view);
                                break;
                            case 3:
                                this.b.lambda$onCreate$6(view);
                                break;
                            case 4:
                                this.b.lambda$onCreate$7(view);
                                break;
                            case 5:
                                this.b.lambda$onCreate$0(view);
                                break;
                            case 6:
                                this.b.lambda$onCreate$1(view);
                                break;
                            default:
                                this.b.lambda$onCreate$2(view);
                                break;
                        }
                    }
                });
                final int i4 = 7;
                this.btnNext.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.w1
                    public final /* synthetic */ QuranPlayerActivity b;

                    {
                        this.b = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        switch (i4) {
                            case 0:
                                this.b.lambda$onCreate$3(view);
                                break;
                            case 1:
                                this.b.lambda$onCreate$4(view);
                                break;
                            case 2:
                                this.b.lambda$onCreate$5(view);
                                break;
                            case 3:
                                this.b.lambda$onCreate$6(view);
                                break;
                            case 4:
                                this.b.lambda$onCreate$7(view);
                                break;
                            case 5:
                                this.b.lambda$onCreate$0(view);
                                break;
                            case 6:
                                this.b.lambda$onCreate$1(view);
                                break;
                            default:
                                this.b.lambda$onCreate$2(view);
                                break;
                        }
                    }
                });
                final int i5 = 0;
                this.btnPrevious.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.w1
                    public final /* synthetic */ QuranPlayerActivity b;

                    {
                        this.b = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        switch (i5) {
                            case 0:
                                this.b.lambda$onCreate$3(view);
                                break;
                            case 1:
                                this.b.lambda$onCreate$4(view);
                                break;
                            case 2:
                                this.b.lambda$onCreate$5(view);
                                break;
                            case 3:
                                this.b.lambda$onCreate$6(view);
                                break;
                            case 4:
                                this.b.lambda$onCreate$7(view);
                                break;
                            case 5:
                                this.b.lambda$onCreate$0(view);
                                break;
                            case 6:
                                this.b.lambda$onCreate$1(view);
                                break;
                            default:
                                this.b.lambda$onCreate$2(view);
                                break;
                        }
                    }
                });
                final int i6 = 1;
                this.btnForward.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.w1
                    public final /* synthetic */ QuranPlayerActivity b;

                    {
                        this.b = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        switch (i6) {
                            case 0:
                                this.b.lambda$onCreate$3(view);
                                break;
                            case 1:
                                this.b.lambda$onCreate$4(view);
                                break;
                            case 2:
                                this.b.lambda$onCreate$5(view);
                                break;
                            case 3:
                                this.b.lambda$onCreate$6(view);
                                break;
                            case 4:
                                this.b.lambda$onCreate$7(view);
                                break;
                            case 5:
                                this.b.lambda$onCreate$0(view);
                                break;
                            case 6:
                                this.b.lambda$onCreate$1(view);
                                break;
                            default:
                                this.b.lambda$onCreate$2(view);
                                break;
                        }
                    }
                });
                final int i7 = 2;
                this.btnRewind.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.w1
                    public final /* synthetic */ QuranPlayerActivity b;

                    {
                        this.b = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        switch (i7) {
                            case 0:
                                this.b.lambda$onCreate$3(view);
                                break;
                            case 1:
                                this.b.lambda$onCreate$4(view);
                                break;
                            case 2:
                                this.b.lambda$onCreate$5(view);
                                break;
                            case 3:
                                this.b.lambda$onCreate$6(view);
                                break;
                            case 4:
                                this.b.lambda$onCreate$7(view);
                                break;
                            case 5:
                                this.b.lambda$onCreate$0(view);
                                break;
                            case 6:
                                this.b.lambda$onCreate$1(view);
                                break;
                            default:
                                this.b.lambda$onCreate$2(view);
                                break;
                        }
                    }
                });
                final int i8 = 3;
                this.downloadContainer.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.w1
                    public final /* synthetic */ QuranPlayerActivity b;

                    {
                        this.b = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        switch (i8) {
                            case 0:
                                this.b.lambda$onCreate$3(view);
                                break;
                            case 1:
                                this.b.lambda$onCreate$4(view);
                                break;
                            case 2:
                                this.b.lambda$onCreate$5(view);
                                break;
                            case 3:
                                this.b.lambda$onCreate$6(view);
                                break;
                            case 4:
                                this.b.lambda$onCreate$7(view);
                                break;
                            case 5:
                                this.b.lambda$onCreate$0(view);
                                break;
                            case 6:
                                this.b.lambda$onCreate$1(view);
                                break;
                            default:
                                this.b.lambda$onCreate$2(view);
                                break;
                        }
                    }
                });
                final int i9 = 4;
                this.speedContainer.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.w1
                    public final /* synthetic */ QuranPlayerActivity b;

                    {
                        this.b = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        switch (i9) {
                            case 0:
                                this.b.lambda$onCreate$3(view);
                                break;
                            case 1:
                                this.b.lambda$onCreate$4(view);
                                break;
                            case 2:
                                this.b.lambda$onCreate$5(view);
                                break;
                            case 3:
                                this.b.lambda$onCreate$6(view);
                                break;
                            case 4:
                                this.b.lambda$onCreate$7(view);
                                break;
                            case 5:
                                this.b.lambda$onCreate$0(view);
                                break;
                            case 6:
                                this.b.lambda$onCreate$1(view);
                                break;
                            default:
                                this.b.lambda$onCreate$2(view);
                                break;
                        }
                    }
                });
                this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.salatak.app.QuranPlayerActivity.3
                    @Override // android.widget.SeekBar.OnSeekBarChangeListener
                    public void onProgressChanged(SeekBar seekBar, int i10, boolean z2) {
                        if (z2) {
                            QuranPlayerActivity.this.tvCurrentTime.setText(QuranPlayerActivity.this.formatTime(i10));
                        }
                    }

                    @Override // android.widget.SeekBar.OnSeekBarChangeListener
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        QuranPlayerActivity.this.userSeeking = true;
                    }

                    @Override // android.widget.SeekBar.OnSeekBarChangeListener
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        QuranPlayerActivity.this.userSeeking = false;
                        if (!QuranPlayerActivity.this.serviceBound || QuranPlayerActivity.this.quranService == null) {
                            return;
                        }
                        QuranPlayerActivity.this.quranService.seekTo(seekBar.getProgress());
                    }
                });
                startAndBindService();
                return;
            }
            Toast.makeText(this, "خطأ في بيانات المشغل", 0).show();
            finish();
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في فتح المشغل", 0).show();
            finish();
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        this.seekHandler.removeCallbacksAndMessages(null);
        if (this.serviceBound) {
            QuranPlayerService quranPlayerService = this.quranService;
            if (quranPlayerService != null) {
                quranPlayerService.setCallback(null);
            }
            unbindService(this.serviceConnection);
            this.serviceBound = false;
        }
        super.onDestroy();
    }
}
