package com.salatak.app;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class StoriesActivity extends AppCompatActivity {
    private static final String COMPANIONS_URL = "https://raw.githubusercontent.com/MesterAbdAlrhmanMohmed/moslemTools_GUI/main/data/json/islamicBooks/elShabahLibe.json";
    private static final String PREFS_NAME = "SalatakPrefs";
    private static final String PROPHETS_URL = "https://raw.githubusercontent.com/MesterAbdAlrhmanMohmed/moslemTools_GUI/main/data/json/prophetStories.json";
    private JSONObject companionsData;
    private EditText etSearch;
    private JSONObject prophetsData;
    private LinearLayout storiesContainer;
    private TextView tabCompanions;
    private TextView tabProphets;
    private int currentTab = 0;
    private List<String> prophetKeys = new ArrayList();
    private List<String> companionPartKeys = new ArrayList();
    private boolean showingDownloads = false;
    private boolean showingBookmarks = false;

    private void addBookmarkItem(final String str, final String str2, String str3, String str4, int i2, final int i3) {
        String str5;
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(dpToPx(16), dpToPx(12), dpToPx(16), dpToPx(12));
        linearLayout.setBackgroundResource(R.drawable.radio_station_background);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.bottomMargin = dpToPx(6);
        linearLayout.setLayoutParams(layoutParams);
        TextView textView = new TextView(this);
        textView.setText(str);
        textView.setTextSize(2, 15.0f);
        textView.setTextColor(Color.parseColor("#E0AD3B"));
        textView.setTypeface(null, 1);
        linearLayout.addView(textView);
        if (str2 != null && !str2.isEmpty()) {
            TextView textView2 = new TextView(this);
            if (str2.length() > 100) {
                str5 = str2.substring(0, 100) + "...";
            } else {
                str5 = str2;
            }
            textView2.setText(str5);
            textView2.setTextSize(2, 13.0f);
            textView2.setTextColor(Color.parseColor("#8899AA"));
            textView2.setMaxLines(2);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
            layoutParams2.topMargin = dpToPx(4);
            textView2.setLayoutParams(layoutParams2);
            linearLayout.addView(textView2);
        }
        linearLayout.setOnClickListener(new w(this, str3, str4, i2, 2));
        linearLayout.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.salatak.app.k2
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean lambda$addBookmarkItem$13;
                lambda$addBookmarkItem$13 = StoriesActivity.this.lambda$addBookmarkItem$13(str, i3, str2, view);
                return lambda$addBookmarkItem$13;
            }
        });
        this.storiesContainer.addView(linearLayout);
    }

    private void addDownloadedItem(String str, File file) {
        TextView textView = new TextView(this);
        textView.setText(str);
        textView.setTextSize(2, 16.0f);
        textView.setTextColor(Color.parseColor("#DDDDDD"));
        textView.setPadding(dpToPx(16), dpToPx(14), dpToPx(16), dpToPx(14));
        textView.setBackgroundResource(R.drawable.radio_station_background);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.bottomMargin = dpToPx(6);
        textView.setLayoutParams(layoutParams);
        textView.setOnClickListener(new c2(1, str, this, file));
        textView.setOnLongClickListener(new m(this, str, file, 2));
        this.storiesContainer.addView(textView);
    }

    private void addEmptyText(String str) {
        TextView textView = new TextView(this);
        textView.setText(str);
        textView.setTextSize(2, 16.0f);
        textView.setTextColor(Color.parseColor("#8899AA"));
        textView.setGravity(17);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = dpToPx(40);
        textView.setLayoutParams(layoutParams);
        this.storiesContainer.addView(textView);
    }

    private void addStoryItem(final String str, final String str2, final String str3) {
        TextView textView = new TextView(this);
        textView.setText(str);
        textView.setTextSize(2, 16.0f);
        textView.setTextColor(Color.parseColor("#DDDDDD"));
        textView.setPadding(dpToPx(16), dpToPx(14), dpToPx(16), dpToPx(14));
        textView.setBackgroundResource(R.drawable.radio_station_background);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.bottomMargin = dpToPx(6);
        textView.setLayoutParams(layoutParams);
        textView.setClickable(true);
        textView.setFocusable(true);
        if (isStoryDownloaded(str3)) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_download, 0);
            textView.setCompoundDrawablePadding(dpToPx(8));
        }
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.l2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StoriesActivity.this.lambda$addStoryItem$6(str2, str3, str, view);
            }
        });
        textView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.salatak.app.m2
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean lambda$addStoryItem$7;
                lambda$addStoryItem$7 = StoriesActivity.this.lambda$addStoryItem$7(str, str2, str3, view);
                return lambda$addStoryItem$7;
            }
        });
        this.storiesContainer.addView(textView);
    }

    private void copyText(String str) {
        ((ClipboardManager) getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("text", str));
        Toast.makeText(this, "تم النسخ", 0).show();
    }

    private void deleteDownloadedStory(String str) {
        File[] listFiles;
        File storiesDownloadDir = getStoriesDownloadDir();
        if (storiesDownloadDir.exists() && (listFiles = storiesDownloadDir.listFiles()) != null) {
            for (File file : listFiles) {
                if (file.getName().endsWith(".json")) {
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                        StringBuilder sb = new StringBuilder();
                        while (true) {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                break;
                            } else {
                                sb.append(readLine);
                            }
                        }
                        bufferedReader.close();
                        if (str.equals(new JSONObject(sb.toString()).optString("key"))) {
                            file.delete();
                            Toast.makeText(this, "تم الحذف", 0).show();
                            return;
                        }
                        continue;
                    } catch (Exception unused) {
                        continue;
                    }
                }
            }
        }
    }

    private void downloadStory(String str, String str2, String str3) {
        Toast.makeText(this, "جاري التحميل...", 0).show();
        new Thread(new androidx.media3.exoplayer.source.h(this, str, str2, str3, 2)).start();
    }

    private int dpToPx(int i2) {
        return (int) (i2 * getResources().getDisplayMetrics().density);
    }

    private JSONObject fetchJson(String str, String str2) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(15000);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
            }
            bufferedReader.close();
            httpURLConnection.disconnect();
            String sb2 = sb.toString();
            if (sb2.startsWith("\ufeff")) {
                sb2 = sb2.substring(1);
            }
            getSharedPreferences(PREFS_NAME, 0).edit().putString(str2, sb2).apply();
            return new JSONObject(sb2);
        } catch (Exception e2) {
            e2.printStackTrace();
            try {
                String string = getSharedPreferences(PREFS_NAME, 0).getString(str2, null);
                if (string != null) {
                    return new JSONObject(string);
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            return null;
        }
    }

    private String formatPartName(String str) {
        return str.replace("الجزء", "الجزء ");
    }

    private File getStoriesDownloadDir() {
        return new File(getExternalFilesDir(null), "stories_downloads");
    }

    private boolean isStoryDownloaded(String str) {
        File[] listFiles;
        File storiesDownloadDir = getStoriesDownloadDir();
        if (!storiesDownloadDir.exists() || (listFiles = storiesDownloadDir.listFiles()) == null) {
            return false;
        }
        for (File file : listFiles) {
            if (file.getName().endsWith(".json")) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        sb.append(readLine);
                    }
                    bufferedReader.close();
                    if (str.equals(new JSONObject(sb.toString()).optString("key"))) {
                        return true;
                    }
                } catch (Exception unused) {
                    continue;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addBookmarkItem$12(int i2, String str, DialogInterface dialogInterface, int i3) {
        if (i3 != 0) {
            copyText(str);
        } else {
            removeBookmark(i2);
            refreshList();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$addBookmarkItem$13(String str, final int i2, final String str2, View view) {
        new AlertDialog.Builder(this, 4).setTitle(str).setItems(new String[]{"حذف العلامة", "نسخ النص"}, new DialogInterface.OnClickListener() { // from class: com.salatak.app.n2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i3) {
                StoriesActivity.this.lambda$addBookmarkItem$12(i2, str2, dialogInterface, i3);
            }
        }).show();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$addDownloadedItem$10(String str, File file, View view) {
        new AlertDialog.Builder(this, 4).setTitle(str).setItems(new String[]{"حذف", "نسخ الاسم"}, new m0(this, file, str, 2)).show();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addDownloadedItem$8(String str, File file, View view) {
        openDownloadedStory(str, file);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addDownloadedItem$9(File file, String str, DialogInterface dialogInterface, int i2) {
        if (i2 != 0) {
            copyText(str);
            return;
        }
        file.delete();
        refreshList();
        Toast.makeText(this, "تم الحذف", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addStoryItem$6(String str, String str2, String str3, View view) {
        lambda$addBookmarkItem$11(str, str2, str3, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$addStoryItem$7(String str, String str2, String str3, View view) {
        showStoryLongPressMenu(str, str2, str3);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$downloadStory$15() {
        Toast.makeText(this, "لم يتم العثور على المحتوى", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$downloadStory$16() {
        Toast.makeText(this, "تم التحميل", 0).show();
        refreshList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$downloadStory$17() {
        Toast.makeText(this, "خطأ في التحميل", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0084 A[Catch: Exception -> 0x0032, TryCatch #0 {Exception -> 0x0032, blocks: (B:2:0x0000, B:4:0x001c, B:7:0x0022, B:9:0x0028, B:10:0x007a, B:12:0x0084, B:14:0x0089, B:17:0x0090, B:20:0x0035, B:22:0x003f, B:24:0x0045, B:25:0x00c5, B:27:0x004d, B:30:0x0053, B:32:0x0059, B:33:0x0063, B:35:0x006d, B:37:0x0073), top: B:1:0x0000 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public /* synthetic */ void lambda$downloadStory$18(java.lang.String r4, java.lang.String r5, java.lang.String r6) {
        /*
            r3 = this;
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch: java.lang.Exception -> L32
            r0.<init>()     // Catch: java.lang.Exception -> L32
            java.lang.String r1 = "type"
            r0.put(r1, r4)     // Catch: java.lang.Exception -> L32
            java.lang.String r1 = "key"
            r0.put(r1, r5)     // Catch: java.lang.Exception -> L32
            java.lang.String r1 = "displayName"
            r0.put(r1, r6)     // Catch: java.lang.Exception -> L32
            java.lang.String r1 = "prophet"
            boolean r4 = r4.equals(r1)     // Catch: java.lang.Exception -> L32
            if (r4 == 0) goto L4d
            org.json.JSONObject r4 = r3.prophetsData     // Catch: java.lang.Exception -> L32
            java.lang.String r1 = "content"
            if (r4 == 0) goto L35
            boolean r4 = r4.has(r5)     // Catch: java.lang.Exception -> L32
            if (r4 == 0) goto L35
            org.json.JSONObject r4 = r3.prophetsData     // Catch: java.lang.Exception -> L32
            java.lang.String r4 = r4.getString(r5)     // Catch: java.lang.Exception -> L32
            r0.put(r1, r4)     // Catch: java.lang.Exception -> L32
            goto L7a
        L32:
            r4 = move-exception
            goto Lcf
        L35:
            java.lang.String r4 = "https://raw.githubusercontent.com/MesterAbdAlrhmanMohmed/moslemTools_GUI/main/data/json/prophetStories.json"
            java.lang.String r2 = "cached_prophets_json"
            org.json.JSONObject r4 = r3.fetchJson(r4, r2)     // Catch: java.lang.Exception -> L32
            if (r4 == 0) goto Lc5
            boolean r2 = r4.has(r5)     // Catch: java.lang.Exception -> L32
            if (r2 == 0) goto Lc5
            java.lang.String r4 = r4.getString(r5)     // Catch: java.lang.Exception -> L32
            r0.put(r1, r4)     // Catch: java.lang.Exception -> L32
            goto L7a
        L4d:
            org.json.JSONObject r4 = r3.companionsData     // Catch: java.lang.Exception -> L32
            java.lang.String r1 = "pages"
            if (r4 == 0) goto L63
            boolean r4 = r4.has(r5)     // Catch: java.lang.Exception -> L32
            if (r4 == 0) goto L63
            org.json.JSONObject r4 = r3.companionsData     // Catch: java.lang.Exception -> L32
            org.json.JSONArray r4 = r4.getJSONArray(r5)     // Catch: java.lang.Exception -> L32
            r0.put(r1, r4)     // Catch: java.lang.Exception -> L32
            goto L7a
        L63:
            java.lang.String r4 = "https://raw.githubusercontent.com/MesterAbdAlrhmanMohmed/moslemTools_GUI/main/data/json/islamicBooks/elShabahLibe.json"
            java.lang.String r2 = "cached_companions_json"
            org.json.JSONObject r4 = r3.fetchJson(r4, r2)     // Catch: java.lang.Exception -> L32
            if (r4 == 0) goto Lc5
            boolean r2 = r4.has(r5)     // Catch: java.lang.Exception -> L32
            if (r2 == 0) goto Lc5
            org.json.JSONArray r4 = r4.getJSONArray(r5)     // Catch: java.lang.Exception -> L32
            r0.put(r1, r4)     // Catch: java.lang.Exception -> L32
        L7a:
            java.io.File r4 = r3.getStoriesDownloadDir()     // Catch: java.lang.Exception -> L32
            boolean r1 = r4.exists()     // Catch: java.lang.Exception -> L32
            if (r1 != 0) goto L87
            r4.mkdirs()     // Catch: java.lang.Exception -> L32
        L87:
            if (r6 == 0) goto L90
            boolean r1 = r6.isEmpty()     // Catch: java.lang.Exception -> L32
            if (r1 != 0) goto L90
            r5 = r6
        L90:
            java.io.File r6 = new java.io.File     // Catch: java.lang.Exception -> L32
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L32
            r1.<init>()     // Catch: java.lang.Exception -> L32
            r1.append(r5)     // Catch: java.lang.Exception -> L32
            java.lang.String r5 = ".json"
            r1.append(r5)     // Catch: java.lang.Exception -> L32
            java.lang.String r5 = r1.toString()     // Catch: java.lang.Exception -> L32
            r6.<init>(r4, r5)     // Catch: java.lang.Exception -> L32
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch: java.lang.Exception -> L32
            r4.<init>(r6)     // Catch: java.lang.Exception -> L32
            java.lang.String r5 = r0.toString()     // Catch: java.lang.Exception -> L32
            java.lang.String r6 = "UTF-8"
            byte[] r5 = r5.getBytes(r6)     // Catch: java.lang.Exception -> L32
            r4.write(r5)     // Catch: java.lang.Exception -> L32
            r4.close()     // Catch: java.lang.Exception -> L32
            com.salatak.app.j2 r4 = new com.salatak.app.j2     // Catch: java.lang.Exception -> L32
            r5 = 2
            r4.<init>(r3, r5)     // Catch: java.lang.Exception -> L32
            r3.runOnUiThread(r4)     // Catch: java.lang.Exception -> L32
            goto Ldb
        Lc5:
            com.salatak.app.j2 r4 = new com.salatak.app.j2     // Catch: java.lang.Exception -> L32
            r5 = 1
            r4.<init>(r3, r5)     // Catch: java.lang.Exception -> L32
            r3.runOnUiThread(r4)     // Catch: java.lang.Exception -> L32
            return
        Lcf:
            r4.printStackTrace()
            com.salatak.app.j2 r4 = new com.salatak.app.j2
            r5 = 3
            r4.<init>(r3, r5)
            r3.runOnUiThread(r4)
        Ldb:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.salatak.app.StoriesActivity.lambda$downloadStory$18(java.lang.String, java.lang.String, java.lang.String):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadData$5() {
        this.prophetsData = fetchJson(PROPHETS_URL, "cached_prophets_json");
        this.companionsData = fetchJson(COMPANIONS_URL, "cached_companions_json");
        if (this.prophetsData != null) {
            this.prophetKeys.clear();
            Iterator<String> keys = this.prophetsData.keys();
            while (keys.hasNext()) {
                this.prophetKeys.add(keys.next());
            }
        }
        if (this.companionsData != null) {
            this.companionPartKeys.clear();
            Iterator<String> keys2 = this.companionsData.keys();
            while (keys2.hasNext()) {
                this.companionPartKeys.add(keys2.next());
            }
        }
        runOnUiThread(new j2(this, 4));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        this.showingBookmarks = false;
        this.showingDownloads = !this.showingDownloads;
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(this.showingDownloads ? "التحميلات" : "القصص");
        refreshList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$2(View view) {
        this.showingDownloads = false;
        this.showingBookmarks = !this.showingBookmarks;
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(this.showingBookmarks ? "العلامات المرجعية" : "القصص");
        refreshList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$3(View view) {
        this.currentTab = 0;
        this.showingDownloads = false;
        this.showingBookmarks = false;
        updateTabUI();
        refreshList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$4(View view) {
        this.currentTab = 1;
        this.showingDownloads = false;
        this.showingBookmarks = false;
        updateTabUI();
        refreshList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showStoryLongPressMenu$14(boolean z2, String str, String str2, String str3, DialogInterface dialogInterface, int i2) {
        if (i2 != 0) {
            copyText(str3);
        } else if (!z2) {
            downloadStory(str2, str, str3);
        } else {
            deleteDownloadedStory(str);
            refreshList();
        }
    }

    private void loadData() {
        new Thread(new j2(this, 0)).start();
    }

    private void openDownloadedStory(String str, File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    bufferedReader.close();
                    JSONObject jSONObject = new JSONObject(sb.toString());
                    String string = jSONObject.getString("type");
                    String string2 = jSONObject.getString("key");
                    Intent intent = new Intent(this, (Class<?>) StoryReaderActivity.class);
                    intent.putExtra("type", string);
                    intent.putExtra("key", string2);
                    intent.putExtra("displayName", str);
                    intent.putExtra("page", 0);
                    intent.putExtra("offline_data", sb.toString());
                    startActivity(intent);
                    return;
                }
                sb.append(readLine);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في فتح الملف", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: openStory, reason: merged with bridge method [inline-methods] */
    public void lambda$addBookmarkItem$11(String str, String str2, String str3, int i2) {
        Intent intent = new Intent(this, (Class<?>) StoryReaderActivity.class);
        intent.putExtra("type", str);
        intent.putExtra("key", str2);
        intent.putExtra("displayName", str3);
        intent.putExtra("page", i2);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshList() {
        this.storiesContainer.removeAllViews();
        String lowerCase = this.etSearch.getText().toString().trim().toLowerCase();
        if (this.showingBookmarks) {
            showBookmarksList(lowerCase);
            return;
        }
        if (this.showingDownloads) {
            showDownloadsList(lowerCase);
        } else if (this.currentTab == 0) {
            showProphetsList(lowerCase);
        } else {
            showCompanionsList(lowerCase);
        }
    }

    private void removeBookmark(int i2) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
            JSONArray jSONArray = new JSONArray(sharedPreferences.getString("story_bookmarks", "[]"));
            JSONArray jSONArray2 = new JSONArray();
            for (int i3 = 0; i3 < jSONArray.length(); i3++) {
                if (i3 != i2) {
                    jSONArray2.put(jSONArray.getJSONObject(i3));
                }
            }
            sharedPreferences.edit().putString("story_bookmarks", jSONArray2.toString()).apply();
            Toast.makeText(this, "تم حذف العلامة", 0).show();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void showBookmarksList(String str) {
        try {
            JSONArray jSONArray = new JSONArray(getSharedPreferences(PREFS_NAME, 0).getString("story_bookmarks", "[]"));
            if (jSONArray.length() == 0) {
                addEmptyText("لا توجد علامات مرجعية");
                return;
            }
            boolean z2 = false;
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i2);
                String string = jSONObject.getString("storyName");
                int i3 = jSONObject.getInt("page");
                String string2 = jSONObject.getString("type");
                String string3 = jSONObject.getString("key");
                String optString = jSONObject.optString("preview", "");
                String str2 = string + " - صفحة " + (i3 + 1);
                if (str.isEmpty() || str2.toLowerCase().contains(str) || optString.toLowerCase().contains(str)) {
                    addBookmarkItem(str2, optString, string2, string3, i3, i2);
                    z2 = true;
                }
            }
            if (z2) {
                return;
            }
            addEmptyText("لا توجد نتائج");
        } catch (Exception unused) {
            addEmptyText("لا توجد علامات مرجعية");
        }
    }

    private void showCompanionsList(String str) {
        if (this.companionsData == null) {
            addEmptyText("جاري التحميل...");
            return;
        }
        boolean z2 = false;
        for (String str2 : this.companionPartKeys) {
            String formatPartName = formatPartName(str2);
            if (str.isEmpty() || formatPartName.toLowerCase().contains(str) || str2.toLowerCase().contains(str)) {
                try {
                    addStoryItem(formatPartName + " (" + this.companionsData.getJSONArray(str2).length() + " صفحة)", "companion", str2);
                } catch (Exception unused) {
                    addStoryItem(formatPartName, "companion", str2);
                }
                z2 = true;
            }
        }
        if (z2) {
            return;
        }
        addEmptyText("لا توجد نتائج");
    }

    private void showDownloadsList(String str) {
        File storiesDownloadDir = getStoriesDownloadDir();
        if (!storiesDownloadDir.exists() || storiesDownloadDir.listFiles() == null || storiesDownloadDir.listFiles().length == 0) {
            addEmptyText("لا توجد تحميلات");
            return;
        }
        boolean z2 = false;
        for (File file : storiesDownloadDir.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                String replace = file.getName().replace(".json", "");
                if (str.isEmpty() || replace.toLowerCase().contains(str)) {
                    addDownloadedItem(replace, file);
                    z2 = true;
                }
            }
        }
        if (z2) {
            return;
        }
        addEmptyText("لا توجد نتائج");
    }

    private void showLoading() {
        this.storiesContainer.removeAllViews();
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        layoutParams.topMargin = dpToPx(40);
        progressBar.setLayoutParams(layoutParams);
        this.storiesContainer.addView(progressBar);
    }

    private void showProphetsList(String str) {
        if (this.prophetsData == null) {
            addEmptyText("جاري التحميل...");
            return;
        }
        boolean z2 = false;
        for (String str2 : this.prophetKeys) {
            if (str.isEmpty() || str2.toLowerCase().contains(str)) {
                addStoryItem(str2, "prophet", str2);
                z2 = true;
            }
        }
        if (z2) {
            return;
        }
        addEmptyText("لا توجد نتائج");
    }

    private void showStoryLongPressMenu(final String str, final String str2, final String str3) {
        final boolean isStoryDownloaded = isStoryDownloaded(str3);
        String[] strArr = new String[2];
        if (isStoryDownloaded) {
            strArr[0] = "حذف التحميل";
            strArr[1] = "نسخ الاسم";
        } else {
            strArr[0] = "تحميل";
            strArr[1] = "نسخ الاسم";
        }
        new AlertDialog.Builder(this, 4).setTitle(str).setItems(strArr, new DialogInterface.OnClickListener() { // from class: com.salatak.app.p2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                StoriesActivity.this.lambda$showStoryLongPressMenu$14(isStoryDownloaded, str3, str2, str, dialogInterface, i2);
            }
        }).show();
    }

    private void updateTabUI() {
        if (this.currentTab == 0) {
            this.tabProphets.setTextColor(Color.parseColor("#E0AD3B"));
            this.tabProphets.setTypeface(null, 1);
            this.tabCompanions.setTextColor(Color.parseColor("#8899AA"));
            this.tabCompanions.setTypeface(null, 0);
        } else {
            this.tabCompanions.setTextColor(Color.parseColor("#E0AD3B"));
            this.tabCompanions.setTypeface(null, 1);
            this.tabProphets.setTextColor(Color.parseColor("#8899AA"));
            this.tabProphets.setTypeface(null, 0);
        }
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText("القصص");
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_stories);
        getWindow().setStatusBarColor(Color.parseColor("#0D1B2A"));
        getWindow().setNavigationBarColor(Color.parseColor("#0D1B2A"));
        this.storiesContainer = (LinearLayout) findViewById(R.id.storiesContainer);
        this.tabProphets = (TextView) findViewById(R.id.tabProphets);
        this.tabCompanions = (TextView) findViewById(R.id.tabCompanions);
        this.etSearch = (EditText) findViewById(R.id.etSearch);
        ImageView imageView = (ImageView) findViewById(R.id.btnBack);
        ImageView imageView2 = (ImageView) findViewById(R.id.btnDownloads);
        ImageView imageView3 = (ImageView) findViewById(R.id.btnBookmarks);
        final int i2 = 0;
        imageView.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.o2
            public final /* synthetic */ StoriesActivity b;

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
                    case 2:
                        this.b.lambda$onCreate$2(view);
                        break;
                    case 3:
                        this.b.lambda$onCreate$3(view);
                        break;
                    default:
                        this.b.lambda$onCreate$4(view);
                        break;
                }
            }
        });
        final int i3 = 1;
        imageView2.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.o2
            public final /* synthetic */ StoriesActivity b;

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
                    case 2:
                        this.b.lambda$onCreate$2(view);
                        break;
                    case 3:
                        this.b.lambda$onCreate$3(view);
                        break;
                    default:
                        this.b.lambda$onCreate$4(view);
                        break;
                }
            }
        });
        final int i4 = 2;
        imageView3.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.o2
            public final /* synthetic */ StoriesActivity b;

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
                    case 2:
                        this.b.lambda$onCreate$2(view);
                        break;
                    case 3:
                        this.b.lambda$onCreate$3(view);
                        break;
                    default:
                        this.b.lambda$onCreate$4(view);
                        break;
                }
            }
        });
        final int i5 = 3;
        this.tabProphets.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.o2
            public final /* synthetic */ StoriesActivity b;

            {
                this.b = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                switch (i5) {
                    case 0:
                        this.b.lambda$onCreate$0(view);
                        break;
                    case 1:
                        this.b.lambda$onCreate$1(view);
                        break;
                    case 2:
                        this.b.lambda$onCreate$2(view);
                        break;
                    case 3:
                        this.b.lambda$onCreate$3(view);
                        break;
                    default:
                        this.b.lambda$onCreate$4(view);
                        break;
                }
            }
        });
        final int i6 = 4;
        this.tabCompanions.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.o2
            public final /* synthetic */ StoriesActivity b;

            {
                this.b = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                switch (i6) {
                    case 0:
                        this.b.lambda$onCreate$0(view);
                        break;
                    case 1:
                        this.b.lambda$onCreate$1(view);
                        break;
                    case 2:
                        this.b.lambda$onCreate$2(view);
                        break;
                    case 3:
                        this.b.lambda$onCreate$3(view);
                        break;
                    default:
                        this.b.lambda$onCreate$4(view);
                        break;
                }
            }
        });
        this.etSearch.addTextChangedListener(new TextWatcher() { // from class: com.salatak.app.StoriesActivity.1
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                StoriesActivity.this.refreshList();
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i7, int i8, int i9) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i7, int i8, int i9) {
            }
        });
        showLoading();
        loadData();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (this.prophetsData == null && this.companionsData == null) {
            return;
        }
        refreshList();
    }
}
