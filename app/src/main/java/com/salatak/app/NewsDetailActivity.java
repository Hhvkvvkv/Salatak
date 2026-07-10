package com.salatak.app;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import java.io.File;

/* loaded from: classes2.dex */
public class NewsDetailActivity extends AppCompatActivity {
    private String apkUrl = "";
    private String version = "";

    private void downloadAndInstallUpdate() {
        String str = this.apkUrl;
        if (str == null || str.isEmpty()) {
            Toast.makeText(this, "لا يوجد تحديث متاح", 0).show();
            return;
        }
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_update_progress, (ViewGroup) null);
        final ProgressBar progressBar = (ProgressBar) inflate.findViewById(R.id.progressDownload);
        final TextView textView = (TextView) inflate.findViewById(R.id.tvProgressPercent);
        final AlertDialog create = new AlertDialog.Builder(this).setView(inflate).setCancelable(false).create();
        create.show();
        try {
            final DownloadManager downloadManager = (DownloadManager) getSystemService("download");
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(this.apkUrl));
            request.setTitle("تحديث صلاتك v" + this.version);
            request.setDescription("جاري تحميل التحديث...");
            request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "salatak_update.apk");
            request.setNotificationVisibility(0);
            final long enqueue = downloadManager.enqueue(request);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() { // from class: com.salatak.app.NewsDetailActivity.1
                @Override // java.lang.Runnable
                public void run() {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor query2 = downloadManager.query(query);
                    if (query2 == null || !query2.moveToFirst()) {
                        handler.postDelayed(this, 500L);
                        return;
                    }
                    int columnIndex = query2.getColumnIndex("bytes_so_far");
                    int columnIndex2 = query2.getColumnIndex("total_size");
                    int columnIndex3 = query2.getColumnIndex(NotificationCompat.CATEGORY_STATUS);
                    long j2 = columnIndex >= 0 ? query2.getLong(columnIndex) : 0L;
                    long j3 = columnIndex2 >= 0 ? query2.getLong(columnIndex2) : 0L;
                    int i2 = columnIndex3 >= 0 ? query2.getInt(columnIndex3) : 0;
                    if (j3 > 0) {
                        int i3 = (int) ((j2 * 100) / j3);
                        progressBar.setProgress(i3);
                        textView.setText(i3 + "%");
                    }
                    if (i2 == 8) {
                        create.dismiss();
                        NewsDetailActivity.this.installApk();
                    } else if (i2 == 16) {
                        create.dismiss();
                        Toast.makeText(NewsDetailActivity.this, "فشل التحميل", 0).show();
                    } else {
                        handler.postDelayed(this, 500L);
                    }
                    query2.close();
                }
            });
        } catch (Exception e2) {
            create.dismiss();
            Toast.makeText(this, "خطأ: " + e2.getMessage(), 0).show();
        }
    }

    private String formatForScreenReader(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        String[] split = str.split("(?<=[.!?،؟\\.])\\s+");
        StringBuilder sb = new StringBuilder();
        for (String str2 : split) {
            String trim = str2.trim();
            if (!trim.isEmpty()) {
                sb.append(trim);
                sb.append(". ");
            }
        }
        return sb.toString().trim();
    }

    private String formatIntoParagraphs(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        String[] split = str.split("(?<=[.!?،؟\\.])\\s+");
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        for (String str2 : split) {
            String trim = str2.trim();
            if (!trim.isEmpty()) {
                sb.append(trim);
                i2++;
                if (i2 % 3 == 0) {
                    sb.append("\n\n");
                } else {
                    sb.append(" ");
                }
            }
        }
        return sb.toString().trim();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void installApk() {
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "salatak_update.apk");
            if (!file.exists()) {
                Toast.makeText(this, "ملف التحديث غير موجود", 0).show();
                return;
            }
            Intent intent = new Intent("android.intent.action.VIEW");
            Uri uriForFile = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
            intent.addFlags(1);
            intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
            intent.addFlags(268435456);
            startActivity(intent);
        } catch (Exception e2) {
            Toast.makeText(this, "خطأ في التثبيت: " + e2.getMessage(), 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        downloadAndInstallUpdate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        finish();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        String str;
        super.onCreate(bundle);
        Window window = getWindow();
        window.setStatusBarColor(-15918294);
        window.setNavigationBarColor(-15918294);
        setContentView(R.layout.activity_news_detail);
        String stringExtra = getIntent().getStringExtra("title");
        String stringExtra2 = getIntent().getStringExtra("date");
        String stringExtra3 = getIntent().getStringExtra("description");
        boolean booleanExtra = getIntent().getBooleanExtra("show_update_button", false);
        this.apkUrl = getIntent().getStringExtra("apk_url");
        this.version = getIntent().getStringExtra("version");
        TextView textView = (TextView) findViewById(R.id.tvDetailTitle);
        TextView textView2 = (TextView) findViewById(R.id.tvDetailDate);
        TextView textView3 = (TextView) findViewById(R.id.tvDetailContent);
        ImageView imageView = (ImageView) findViewById(R.id.btnBack);
        Button button = (Button) findViewById(R.id.btnUpdateNowDetail);
        if (stringExtra == null) {
            stringExtra = "";
        }
        textView.setText(stringExtra);
        if (stringExtra2 == null) {
            stringExtra2 = "";
        }
        textView2.setText(stringExtra2);
        if (stringExtra3 == null || stringExtra3.isEmpty()) {
            textView3.setText("لا يوجد محتوى");
            textView3.setContentDescription("لا يوجد محتوى");
        } else {
            String formatIntoParagraphs = formatIntoParagraphs(stringExtra3);
            String formatForScreenReader = formatForScreenReader(stringExtra3);
            textView3.setText(formatIntoParagraphs);
            textView3.setImportantForAccessibility(1);
            textView3.setContentDescription(formatForScreenReader);
        }
        if (booleanExtra && (str = this.apkUrl) != null && !str.isEmpty()) {
            button.setVisibility(0);
            final int i2 = 0;
            button.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.d1
                public final /* synthetic */ NewsDetailActivity b;

                {
                    this.b = this;
                }

                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    switch (i2) {
                        case 0:
                            this.b.lambda$onCreate$0(view);
                            break;
                        default:
                            this.b.lambda$onCreate$1(view);
                            break;
                    }
                }
            });
        }
        final int i3 = 1;
        imageView.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.d1
            public final /* synthetic */ NewsDetailActivity b;

            {
                this.b = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                switch (i3) {
                    case 0:
                        this.b.lambda$onCreate$0(view);
                        break;
                    default:
                        this.b.lambda$onCreate$1(view);
                        break;
                }
            }
        });
    }
}
