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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class UpdatesFragment extends Fragment {
    private NewsAdapter adapter;
    private Button btnReportProblem;
    private Button btnUpdateNow;
    private LinearLayout layoutUpdateAvailable;
    private ProgressBar progressLoading;
    private RecyclerView rvNews;
    private TextView tvEmpty;
    private TextView tvUpdateInfo;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private List<JSONObject> newsList = new ArrayList();
    private String latestApkUrl = "";
    private String latestVersion = "";

    public class NewsAdapter extends RecyclerView.Adapter<ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvDate;
            TextView tvDescription;
            TextView tvTitle;

            public ViewHolder(@NonNull View view) {
                super(view);
                this.tvTitle = (TextView) view.findViewById(R.id.tvNewsTitle);
                this.tvDate = (TextView) view.findViewById(R.id.tvNewsDate);
                this.tvDescription = (TextView) view.findViewById(R.id.tvNewsDescription);
            }
        }

        public NewsAdapter() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$0(JSONObject jSONObject, View view) {
            Intent intent = new Intent(UpdatesFragment.this.getContext(), (Class<?>) NewsDetailActivity.class);
            intent.putExtra("title", jSONObject.optString("title", ""));
            intent.putExtra("date", jSONObject.optString("date", ""));
            intent.putExtra("description", jSONObject.optString("description", ""));
            UpdatesFragment.this.startActivity(intent);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return UpdatesFragment.this.newsList.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i2) {
            try {
                JSONObject jSONObject = (JSONObject) UpdatesFragment.this.newsList.get(i2);
                viewHolder.tvTitle.setText(jSONObject.optString("title", ""));
                viewHolder.tvDate.setText(jSONObject.optString("date", ""));
                viewHolder.tvDescription.setVisibility(8);
                viewHolder.itemView.setOnClickListener(new j0(2, this, jSONObject));
            } catch (Exception unused) {
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i2) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news, viewGroup, false));
        }
    }

    private void downloadAndInstallUpdate() {
        if (this.latestApkUrl.isEmpty()) {
            Toast.makeText(getContext(), "لا يوجد تحديث متاح", 0).show();
            return;
        }
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update_progress, (ViewGroup) null);
        final ProgressBar progressBar = (ProgressBar) inflate.findViewById(R.id.progressDownload);
        final TextView textView = (TextView) inflate.findViewById(R.id.tvProgressPercent);
        final AlertDialog create = new AlertDialog.Builder(getContext()).setView(inflate).setCancelable(false).create();
        create.show();
        try {
            final DownloadManager downloadManager = (DownloadManager) requireContext().getSystemService("download");
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(this.latestApkUrl));
            request.setTitle("تحديث صلاتك v" + this.latestVersion);
            request.setDescription("جاري تحميل التحديث...");
            request.setDestinationInExternalFilesDir(getContext(), Environment.DIRECTORY_DOWNLOADS, "salatak_update.apk");
            request.setNotificationVisibility(0);
            final long enqueue = downloadManager.enqueue(request);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() { // from class: com.salatak.app.UpdatesFragment.1
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
                        UpdatesFragment.this.installApk();
                    } else if (i2 == 16) {
                        create.dismiss();
                        Toast.makeText(UpdatesFragment.this.getContext(), "فشل التحميل", 0).show();
                    } else {
                        handler.postDelayed(this, 500L);
                    }
                    query2.close();
                }
            });
        } catch (Exception e2) {
            create.dismiss();
            Toast.makeText(getContext(), "خطأ: " + e2.getMessage(), 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void installApk() {
        try {
            File file = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "salatak_update.apk");
            if (!file.exists()) {
                Toast.makeText(getContext(), "ملف التحديث غير موجود", 0).show();
                return;
            }
            Intent intent = new Intent("android.intent.action.VIEW");
            Uri uriForFile = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".fileprovider", file);
            intent.addFlags(1);
            intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
            intent.addFlags(268435456);
            startActivity(intent);
        } catch (Exception e2) {
            Toast.makeText(getContext(), "خطأ في التثبيت: " + e2.getMessage(), 0).show();
        }
    }

    private boolean isNewerVersion(String str, String str2) {
        try {
            String[] split = str.trim().split("\\.");
            String[] split2 = str2.trim().split("\\.");
            int max = Math.max(split.length, split2.length);
            int i2 = 0;
            while (i2 < max) {
                int parseInt = i2 < split.length ? Integer.parseInt(split[i2].replaceAll("[^0-9]", "")) : 0;
                int parseInt2 = i2 < split2.length ? Integer.parseInt(split2[i2].replaceAll("[^0-9]", "")) : 0;
                if (parseInt > parseInt2) {
                    return true;
                }
                if (parseInt < parseInt2) {
                    return false;
                }
                i2++;
            }
            return false;
        } catch (Exception unused) {
            return !str.equals(str2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadData$2(String str, String str2, View view) {
        Intent intent = new Intent(getContext(), (Class<?>) NewsDetailActivity.class);
        intent.putExtra("title", str);
        intent.putExtra("date", "");
        intent.putExtra("description", str2);
        intent.putExtra("show_update_button", true);
        intent.putExtra("apk_url", this.latestApkUrl);
        intent.putExtra("version", this.latestVersion);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadData$3(List list, String str, String str2, String str3, String str4) {
        String str5;
        if (isAdded()) {
            this.progressLoading.setVisibility(8);
            this.newsList.clear();
            this.newsList.addAll(list);
            this.adapter.notifyDataSetChanged();
            if (list.isEmpty()) {
                this.tvEmpty.setVisibility(0);
            }
            this.latestApkUrl = str;
            this.latestVersion = str2;
            if (str.isEmpty()) {
                return;
            }
            try {
                str5 = requireContext().getPackageManager().getPackageInfo(requireContext().getPackageName(), 0).versionName;
            } catch (Exception unused) {
                str5 = "";
            }
            if (str2.isEmpty() || !isNewerVersion(str2, str5)) {
                this.layoutUpdateAvailable.setVisibility(8);
                return;
            }
            this.layoutUpdateAvailable.setVisibility(0);
            this.tvUpdateInfo.setText("تحديث جديد لتطبيق صلاتك بإصدار رقم ".concat(str2));
            this.btnUpdateNow.setVisibility(8);
            this.layoutUpdateAvailable.setOnClickListener(new c2(2, str3, this, str4));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadData$4() {
        String str;
        String str2;
        String str3;
        final String str4;
        final String str5;
        String str6;
        String str7 = "";
        JSONArray news = GitHubApi.getNews();
        JSONArray updates = GitHubApi.getUpdates();
        final ArrayList arrayList = new ArrayList();
        for (int length = news.length() - 1; length >= 0; length--) {
            try {
                arrayList.add(news.getJSONObject(length));
            } catch (Exception unused) {
            }
        }
        try {
            if (updates.length() > 0) {
                JSONObject jSONObject = updates.getJSONObject(updates.length() - 1);
                str = jSONObject.optString("apk_url", "");
                try {
                    str2 = jSONObject.optString("version", "");
                    try {
                        str3 = jSONObject.optString("title", "");
                        try {
                            str6 = jSONObject.optString("description", "");
                            str7 = str;
                        } catch (Exception unused2) {
                            str4 = "";
                            str5 = str;
                            final String str8 = str2;
                            final String str9 = str3;
                            this.mainHandler.post(new Runnable() { // from class: com.salatak.app.x2
                                @Override // java.lang.Runnable
                                public final void run() {
                                    UpdatesFragment.this.lambda$loadData$3(arrayList, str5, str8, str9, str4);
                                }
                            });
                        }
                    } catch (Exception unused3) {
                        str3 = "";
                    }
                } catch (Exception unused4) {
                    str2 = "";
                    str3 = str2;
                    str4 = "";
                    str5 = str;
                    final String str82 = str2;
                    final String str92 = str3;
                    this.mainHandler.post(new Runnable() { // from class: com.salatak.app.x2
                        @Override // java.lang.Runnable
                        public final void run() {
                            UpdatesFragment.this.lambda$loadData$3(arrayList, str5, str82, str92, str4);
                        }
                    });
                }
            } else {
                str6 = "";
                str2 = str6;
                str3 = str2;
            }
            str5 = str7;
            str4 = str6;
        } catch (Exception unused5) {
            str = "";
            str2 = str;
        }
        final String str822 = str2;
        final String str922 = str3;
        this.mainHandler.post(new Runnable() { // from class: com.salatak.app.x2
            @Override // java.lang.Runnable
            public final void run() {
                UpdatesFragment.this.lambda$loadData$3(arrayList, str5, str822, str922, str4);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateView$0(View view) {
        startActivity(new Intent(getContext(), (Class<?>) ReportLoginActivity.class));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateView$1(View view) {
        downloadAndInstallUpdate();
    }

    private void loadData() {
        this.progressLoading.setVisibility(0);
        this.tvEmpty.setVisibility(8);
        Executors.newSingleThreadExecutor().execute(new b2(6, this));
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_updates, viewGroup, false);
        this.rvNews = (RecyclerView) inflate.findViewById(R.id.rvNews);
        this.progressLoading = (ProgressBar) inflate.findViewById(R.id.progressLoading);
        this.tvEmpty = (TextView) inflate.findViewById(R.id.tvEmpty);
        this.layoutUpdateAvailable = (LinearLayout) inflate.findViewById(R.id.layoutUpdateAvailable);
        this.tvUpdateInfo = (TextView) inflate.findViewById(R.id.tvUpdateInfo);
        this.btnUpdateNow = (Button) inflate.findViewById(R.id.btnUpdateNow);
        this.btnReportProblem = (Button) inflate.findViewById(R.id.btnReportProblem);
        this.rvNews.setLayoutManager(new LinearLayoutManager(getContext()));
        NewsAdapter newsAdapter = new NewsAdapter();
        this.adapter = newsAdapter;
        this.rvNews.setAdapter(newsAdapter);
        final int i2 = 0;
        this.btnReportProblem.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.w2
            public final /* synthetic */ UpdatesFragment b;

            {
                this.b = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                switch (i2) {
                    case 0:
                        this.b.lambda$onCreateView$0(view);
                        break;
                    default:
                        this.b.lambda$onCreateView$1(view);
                        break;
                }
            }
        });
        final int i3 = 1;
        this.btnUpdateNow.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.w2
            public final /* synthetic */ UpdatesFragment b;

            {
                this.b = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                switch (i3) {
                    case 0:
                        this.b.lambda$onCreateView$0(view);
                        break;
                    default:
                        this.b.lambda$onCreateView$1(view);
                        break;
                }
            }
        });
        loadData();
        return inflate;
    }
}
