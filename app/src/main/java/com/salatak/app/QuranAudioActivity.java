package com.salatak.app;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class QuranAudioActivity extends AppCompatActivity {
    private static final String CACHE_KEY = "cached_reciters_json";
    private static final String PREFS_NAME = "SalatakPrefs";
    private static final String RECITERS_URL = "https://raw.githubusercontent.com/MesterAbdAlrhmanMohmed/moslemTools_GUI/main/data/json/reciters.json";
    private List<String> allReciterNames = new ArrayList();
    private ProgressBar progressLoading;
    private LinearLayout recitersContainer;
    private JSONObject recitersJson;
    private RequestQueue requestQueue;
    private EditText searchEditText;

    private void displayReciters(List<String> list) {
        this.recitersContainer.removeAllViews();
        for (String str : list) {
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
            linearLayout.setOnClickListener(new j0(4, this, str));
            this.recitersContainer.addView(linearLayout);
        }
    }

    private int dp(int i2) {
        return (int) ((i2 * getResources().getDisplayMetrics().density) + 0.5f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void filterReciters(String str) {
        if (str.isEmpty()) {
            displayReciters(this.allReciterNames);
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (String str2 : this.allReciterNames) {
            if (str2.contains(str)) {
                arrayList.add(str2);
            }
        }
        displayReciters(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$displayReciters$4(String str, View view) {
        try {
            JSONObject jSONObject = this.recitersJson.getJSONObject(str);
            Intent intent = new Intent(this, (Class<?>) QuranSurahListActivity.class);
            intent.putExtra("reciter_name", str);
            intent.putExtra("surahs_json", jSONObject.toString());
            startActivity(intent);
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في فتح القارئ", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadReciters$2(String str) {
        getSharedPreferences(PREFS_NAME, 0).edit().putString(CACHE_KEY, str).apply();
        parseAndDisplayReciters(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadReciters$3(VolleyError volleyError) {
        String string = getSharedPreferences(PREFS_NAME, 0).getString(CACHE_KEY, null);
        if (string != null) {
            parseAndDisplayReciters(string);
        } else {
            this.progressLoading.setVisibility(8);
            Toast.makeText(this, "فشل تحميل القراء. تأكد من الاتصال بالإنترنت", 1).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        try {
            startActivity(new Intent(this, (Class<?>) QuranDownloadsActivity.class));
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في فتح التنزيلات", 0).show();
        }
    }

    private void loadReciters() {
        this.progressLoading.setVisibility(0);
        this.requestQueue.add(new StringRequest(0, RECITERS_URL, new q1(this), new q1(this)));
    }

    private void parseAndDisplayReciters(String str) {
        try {
            this.recitersJson = new JSONObject(str);
            this.allReciterNames.clear();
            Iterator<String> keys = this.recitersJson.keys();
            while (keys.hasNext()) {
                this.allReciterNames.add(keys.next());
            }
            Collections.sort(this.allReciterNames);
            this.progressLoading.setVisibility(8);
            displayReciters(this.allReciterNames);
        } catch (Exception e2) {
            e2.printStackTrace();
            this.progressLoading.setVisibility(8);
            Toast.makeText(this, "خطأ في تحليل البيانات", 0).show();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            Window window = getWindow();
            window.setStatusBarColor(-15918294);
            window.setNavigationBarColor(-15918294);
            setContentView(R.layout.activity_quran_audio);
            this.recitersContainer = (LinearLayout) findViewById(R.id.recitersContainer);
            this.progressLoading = (ProgressBar) findViewById(R.id.progressLoading);
            this.searchEditText = (EditText) findViewById(R.id.etSearch);
            final int i2 = 0;
            ((ImageView) findViewById(R.id.btnBack)).setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.p1
                public final /* synthetic */ QuranAudioActivity b;

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
            final int i3 = 1;
            ((LinearLayout) findViewById(R.id.downloadsBar)).setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.p1
                public final /* synthetic */ QuranAudioActivity b;

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
            this.searchEditText.addTextChangedListener(new TextWatcher() { // from class: com.salatak.app.QuranAudioActivity.1
                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                }

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
                    QuranAudioActivity.this.filterReciters(charSequence.toString());
                }
            });
            this.requestQueue = Volley.newRequestQueue(this);
            loadReciters();
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في تحميل القرآن الكريم", 0).show();
        }
    }
}
