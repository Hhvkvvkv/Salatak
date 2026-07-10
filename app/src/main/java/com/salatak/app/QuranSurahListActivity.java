package com.salatak.app;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class QuranSurahListActivity extends AppCompatActivity {
    private ProgressBar progressLoading;
    private String reciterName;
    private LinearLayout surahsContainer;
    private String surahsJsonStr;

    private void displaySurahs() {
        try {
            JSONObject jSONObject = new JSONObject(this.surahsJsonStr);
            ArrayList arrayList = new ArrayList();
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                arrayList.add(new String[]{next, jSONObject.getString(next)});
            }
            Collections.sort(arrayList, new s1(this, 2));
            this.progressLoading.setVisibility(8);
            this.surahsContainer.removeAllViews();
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                String[] strArr = (String[]) arrayList.get(i2);
                String str = strArr[0];
                String str2 = strArr[1];
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(0);
                linearLayout.setGravity(16);
                linearLayout.setBackgroundResource(R.drawable.radio_station_background);
                linearLayout.setPadding(dp(16), dp(14), dp(16), dp(14));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
                layoutParams.setMargins(0, 0, 0, dp(8));
                linearLayout.setLayoutParams(layoutParams);
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.drawable.ic_quran_audio);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(dp(32), dp(32));
                layoutParams2.setMargins(0, 0, dp(12), 0);
                imageView.setLayoutParams(layoutParams2);
                linearLayout.addView(imageView);
                TextView textView = new TextView(this);
                textView.setText(str);
                textView.setTextColor(-2236963);
                textView.setTextSize(15.0f);
                textView.setMaxLines(1);
                textView.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
                linearLayout.addView(textView);
                int extractSurahNumber = extractSurahNumber(str);
                File file = new File(new File(getExternalFilesDir("quran"), this.reciterName), extractSurahNumber + ".mp3");
                TextView textView2 = new TextView(this);
                textView2.setTextSize(14.0f);
                if (file.exists()) {
                    textView2.setText("✓");
                    textView2.setTextColor(-11751600);
                } else {
                    textView2.setText("⬇");
                    textView2.setTextColor(-7824982);
                }
                LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-2, -2);
                layoutParams3.setMargins(dp(8), 0, 0, 0);
                textView2.setLayoutParams(layoutParams3);
                linearLayout.addView(textView2);
                linearLayout.setClickable(true);
                linearLayout.setFocusable(true);
                try {
                    TypedArray obtainStyledAttributes = obtainStyledAttributes(new int[]{android.R.attr.selectableItemBackground});
                    Drawable drawable = obtainStyledAttributes.getDrawable(0);
                    obtainStyledAttributes.recycle();
                    if (drawable != null) {
                        linearLayout.setForeground(drawable);
                    }
                } catch (Exception unused) {
                }
                linearLayout.setOnClickListener(new w(this, str, str2, i2, 1));
                this.surahsContainer.addView(linearLayout);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في عرض السور", 0).show();
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$displaySurahs$1(String[] strArr, String[] strArr2) {
        return Integer.compare(extractSurahNumber(strArr[0]), extractSurahNumber(strArr2[0]));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$displaySurahs$2(String str, String str2, int i2, View view) {
        Intent intent = new Intent(this, (Class<?>) QuranPlayerActivity.class);
        intent.putExtra("reciter_name", this.reciterName);
        intent.putExtra("surah_name", str);
        intent.putExtra("surah_url", str2);
        intent.putExtra("surahs_json", this.surahsJsonStr);
        intent.putExtra("surah_index", i2);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        finish();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            Window window = getWindow();
            window.setStatusBarColor(-15918294);
            window.setNavigationBarColor(-15918294);
            setContentView(R.layout.activity_quran_surah_list);
            this.surahsContainer = (LinearLayout) findViewById(R.id.surahsContainer);
            this.progressLoading = (ProgressBar) findViewById(R.id.progressLoading);
            ((ImageView) findViewById(R.id.btnBack)).setOnClickListener(new y0(6, this));
            TextView textView = (TextView) findViewById(R.id.tvReciterName);
            this.reciterName = getIntent().getStringExtra("reciter_name");
            this.surahsJsonStr = getIntent().getStringExtra("surahs_json");
            String str = this.reciterName;
            if (str != null) {
                textView.setText(str);
            }
            if (this.surahsJsonStr != null) {
                displaySurahs();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في تحميل السور", 0).show();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (this.surahsJsonStr != null) {
            displaySurahs();
        }
    }
}
