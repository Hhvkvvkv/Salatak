package com.salatak.app;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class StoryReaderActivity extends AppCompatActivity {
    private static final String COMPANIONS_URL = "https://raw.githubusercontent.com/MesterAbdAlrhmanMohmed/moslemTools_GUI/main/data/json/islamicBooks/elShabahLibe.json";
    private static final int PAGE_CHAR_LIMIT = 2000;
    private static final String PREFS_NAME = "SalatakPrefs";
    private static final String PROPHETS_URL = "https://raw.githubusercontent.com/MesterAbdAlrhmanMohmed/moslemTools_GUI/main/data/json/prophetStories.json";
    private ImageView btnBack;
    private TextView btnNext;
    private TextView btnPrev;
    private String displayName;
    private String key;
    private ScrollView scrollView;
    private TextView tvContent;
    private TextView tvPageInfo;
    private TextView tvToolbarTitle;
    private String type;
    private int currentPage = 0;
    private List<String> pages = new ArrayList();

    private void addBookmark(String str) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
            JSONArray jSONArray = new JSONArray(sharedPreferences.getString("story_bookmarks", "[]"));
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i2);
                if (jSONObject.getString("key").equals(this.key) && jSONObject.getString("type").equals(this.type) && jSONObject.getInt("page") == this.currentPage) {
                    Toast.makeText(this, "العلامة موجودة بالفعل", 0).show();
                    return;
                }
            }
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("type", this.type);
            jSONObject2.put("key", this.key);
            String str2 = this.displayName;
            if (str2 == null) {
                str2 = this.key;
            }
            jSONObject2.put("storyName", str2);
            jSONObject2.put("page", this.currentPage);
            if (str.length() > 150) {
                str = str.substring(0, 150);
            }
            jSONObject2.put("preview", str);
            jSONArray.put(jSONObject2);
            sharedPreferences.edit().putString("story_bookmarks", jSONArray.toString()).apply();
            Toast.makeText(this, "تم إضافة العلامة المرجعية", 0).show();
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في الحفظ", 0).show();
        }
    }

    private JSONObject fetchJsonObject(String str) {
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
            return new JSONObject(sb2);
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadFromNetwork$5() {
        this.tvContent.setText("لم يتم العثور على القصة");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadFromNetwork$6() {
        this.tvContent.setText("لم يتم العثور على الجزء");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadFromNetwork$7() {
        this.tvContent.setText("خطأ في تحميل البيانات");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadFromNetwork$8() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
            JSONObject jSONObject = null;
            if (this.type.equals("prophet")) {
                String string = sharedPreferences.getString("cached_prophets_json", null);
                if (string != null) {
                    try {
                        jSONObject = new JSONObject(string);
                    } catch (Exception unused) {
                    }
                }
                if (jSONObject != null) {
                    if (!jSONObject.has(this.key)) {
                    }
                    if (jSONObject != null || !jSONObject.has(this.key)) {
                        runOnUiThread(new s2(this, 0));
                        return;
                    }
                    splitTextIntoPages(jSONObject.getString(this.key));
                }
                jSONObject = fetchJsonObject(PROPHETS_URL);
                if (jSONObject != null) {
                    sharedPreferences.edit().putString("cached_prophets_json", jSONObject.toString()).apply();
                }
                if (jSONObject != null) {
                }
                runOnUiThread(new s2(this, 0));
                return;
            }
            String string2 = sharedPreferences.getString("cached_companions_json", null);
            if (string2 != null) {
                try {
                    jSONObject = new JSONObject(string2);
                } catch (Exception unused2) {
                }
            }
            if ((jSONObject == null || !jSONObject.has(this.key)) && (jSONObject = fetchJsonObject(COMPANIONS_URL)) != null) {
                sharedPreferences.edit().putString("cached_companions_json", jSONObject.toString()).apply();
            }
            if (jSONObject == null || !jSONObject.has(this.key)) {
                runOnUiThread(new s2(this, 1));
                return;
            }
            JSONArray jSONArray = jSONObject.getJSONArray(this.key);
            this.pages.clear();
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                this.pages.add(jSONArray.getString(i2));
            }
            if (this.currentPage >= this.pages.size()) {
                this.currentPage = 0;
            }
            runOnUiThread(new s2(this, 2));
        } catch (Exception e2) {
            e2.printStackTrace();
            runOnUiThread(new s2(this, 3));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        showPageJumpDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$2(View view) {
        if (this.currentPage < this.pages.size() - 1) {
            this.currentPage++;
            showPage();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$3(View view) {
        int i2 = this.currentPage;
        if (i2 > 0) {
            this.currentPage = i2 - 1;
            showPage();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onCreate$4(View view) {
        showTextLongPressMenu();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPageJumpDialog$9(NumberPicker numberPicker, DialogInterface dialogInterface, int i2) {
        this.currentPage = numberPicker.getValue() - 1;
        showPage();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showTextLongPressMenu$10(String str, DialogInterface dialogInterface, int i2) {
        if (i2 == 0) {
            addBookmark(str);
        } else {
            ((ClipboardManager) getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("story", str));
            Toast.makeText(this, "تم النسخ", 0).show();
        }
    }

    private void loadFromNetwork() {
        new Thread(new s2(this, 4)).start();
    }

    private void loadFromOfflineData(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (this.type.equals("prophet")) {
                splitTextIntoPages(jSONObject.getString("content"));
            } else {
                JSONArray jSONArray = jSONObject.getJSONArray("pages");
                this.pages.clear();
                for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                    this.pages.add(jSONArray.getString(i2));
                }
            }
            if (this.currentPage >= this.pages.size()) {
                this.currentPage = 0;
            }
            showPage();
        } catch (Exception e2) {
            e2.printStackTrace();
            this.tvContent.setText("خطأ في قراءة البيانات");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPage() {
        if (this.pages.isEmpty()) {
            this.tvContent.setText("لا يوجد محتوى");
            this.tvPageInfo.setText("0 / 0");
            TextView textView = this.tvToolbarTitle;
            String str = this.displayName;
            if (str == null) {
                str = "";
            }
            textView.setText(str);
            return;
        }
        this.tvContent.setText(this.pages.get(this.currentPage));
        this.scrollView.scrollTo(0, 0);
        StringBuilder sb = new StringBuilder();
        String str2 = this.displayName;
        if (str2 == null) {
            str2 = this.key;
        }
        sb.append(str2);
        sb.append(" - صفحة ");
        sb.append(this.currentPage + 1);
        this.tvToolbarTitle.setText(sb.toString());
        this.tvPageInfo.setText((this.currentPage + 1) + " / " + this.pages.size());
        this.btnPrev.setAlpha(this.currentPage > 0 ? 1.0f : 0.3f);
        this.btnNext.setAlpha(this.currentPage < this.pages.size() + (-1) ? 1.0f : 0.3f);
    }

    private void showPageJumpDialog() {
        if (this.pages.isEmpty()) {
            return;
        }
        NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(this.pages.size());
        numberPicker.setValue(this.currentPage + 1);
        numberPicker.setWrapSelectorWheel(false);
        new AlertDialog.Builder(this, 4).setTitle("الانتقال إلى صفحة").setView(numberPicker).setPositiveButton("انتقال", new p(this, numberPicker, 1)).setNegativeButton("إلغاء", (DialogInterface.OnClickListener) null).show();
    }

    private void showTextLongPressMenu() {
        String str = this.pages.isEmpty() ? "" : this.pages.get(this.currentPage);
        if (str.isEmpty()) {
            return;
        }
        new AlertDialog.Builder(this, 4).setTitle("خيارات").setItems(new String[]{"إضافة علامة مرجعية", "نسخ النص"}, new p(this, str, 2)).show();
    }

    private void splitTextIntoPages(String str) {
        this.pages.clear();
        String replace = str.replace("\n.\n", "\n\n").replace("\n. \n", "\n\n");
        String[] split = replace.split("\n\n");
        StringBuilder sb = new StringBuilder();
        for (String str2 : split) {
            String trim = str2.trim();
            if (!trim.isEmpty()) {
                if (trim.length() + sb.length() > 2000 && sb.length() > 0) {
                    this.pages.add(sb.toString().trim());
                    sb = new StringBuilder();
                }
                if (sb.length() > 0) {
                    sb.append("\n\n");
                }
                sb.append(trim);
            }
        }
        if (sb.length() > 0) {
            this.pages.add(sb.toString().trim());
        }
        if (this.pages.isEmpty()) {
            this.pages.add(replace.trim());
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_story_reader);
        getWindow().setStatusBarColor(Color.parseColor("#0D1B2A"));
        getWindow().setNavigationBarColor(Color.parseColor("#111D2B"));
        this.tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        this.tvContent = (TextView) findViewById(R.id.tvContent);
        this.btnNext = (TextView) findViewById(R.id.btnNext);
        this.btnPrev = (TextView) findViewById(R.id.btnPrev);
        this.tvPageInfo = (TextView) findViewById(R.id.tvPageInfo);
        this.scrollView = (ScrollView) findViewById(R.id.scrollView);
        this.btnBack = (ImageView) findViewById(R.id.btnBack);
        this.type = getIntent().getStringExtra("type");
        this.key = getIntent().getStringExtra("key");
        this.displayName = getIntent().getStringExtra("displayName");
        this.currentPage = getIntent().getIntExtra("page", 0);
        if (this.type == null || this.key == null) {
            Toast.makeText(this, "بيانات غير صالحة", 0).show();
            finish();
            return;
        }
        final int i2 = 1;
        this.btnBack.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.q2
            public final /* synthetic */ StoryReaderActivity b;

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
                        this.b.lambda$onCreate$0(view);
                        break;
                    case 2:
                        this.b.lambda$onCreate$1(view);
                        break;
                    default:
                        this.b.lambda$onCreate$2(view);
                        break;
                }
            }
        });
        final int i3 = 2;
        this.tvToolbarTitle.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.q2
            public final /* synthetic */ StoryReaderActivity b;

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
                        this.b.lambda$onCreate$0(view);
                        break;
                    case 2:
                        this.b.lambda$onCreate$1(view);
                        break;
                    default:
                        this.b.lambda$onCreate$2(view);
                        break;
                }
            }
        });
        final int i4 = 3;
        this.btnNext.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.q2
            public final /* synthetic */ StoryReaderActivity b;

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
                        this.b.lambda$onCreate$0(view);
                        break;
                    case 2:
                        this.b.lambda$onCreate$1(view);
                        break;
                    default:
                        this.b.lambda$onCreate$2(view);
                        break;
                }
            }
        });
        final int i5 = 0;
        this.btnPrev.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.q2
            public final /* synthetic */ StoryReaderActivity b;

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
                        this.b.lambda$onCreate$0(view);
                        break;
                    case 2:
                        this.b.lambda$onCreate$1(view);
                        break;
                    default:
                        this.b.lambda$onCreate$2(view);
                        break;
                }
            }
        });
        this.tvContent.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.salatak.app.r2
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean lambda$onCreate$4;
                lambda$onCreate$4 = StoryReaderActivity.this.lambda$onCreate$4(view);
                return lambda$onCreate$4;
            }
        });
        this.tvContent.setText("جاري التحميل...");
        TextView textView = this.tvToolbarTitle;
        String str = this.displayName;
        if (str == null) {
            str = "";
        }
        textView.setText(str);
        String stringExtra = getIntent().getStringExtra("offline_data");
        if (stringExtra != null) {
            loadFromOfflineData(stringExtra);
        } else {
            loadFromNetwork();
        }
    }
}
