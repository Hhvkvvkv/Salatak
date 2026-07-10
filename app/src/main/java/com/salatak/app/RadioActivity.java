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
import androidx.media3.common.MimeTypes;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class RadioActivity extends AppCompatActivity {
    private static final String[][] CATEGORIES = {new String[]{"إذاعات إسلامية", "https://api.websim.ai/blobs/0195597c-01bf-79f4-bc51-bc15764be264.html", "json"}, new String[]{"إذاعات القراء", "https://api.websim.ai/blobs/019541da-3fb1-7ed5-b4fb-62e2641270fb.html", "html_station"}, new String[]{"القراءات العشر", "https://api.websim.ai/blobs/019541f9-8ec2-723b-877c-7d641b67a8b6.html", "html_radio"}, new String[]{"تلاوات متميزة", "https://api.websim.ai/blobs/019541fb-cfd5-71ec-94d5-f526950b4318.html", "html_radio"}, new String[]{"الأدعية والأذكار", "https://api.websim.ai/blobs/01954211-9f53-7e2c-af4a-cc72fea8490c.html", "html_radio"}, new String[]{"الفتاوى", "https://api.websim.ai/blobs/01954213-2300-7b0e-8f81-580c6fb727f1.html", "html_radio"}, new String[]{"السيرة والقصص", "https://api.websim.ai/blobs/01954214-9cbd-723b-a30a-9733ce77ca60.html", "html_radio"}, new String[]{"تفسير القرآن", "https://api.websim.ai/blobs/01954216-f5ca-7c52-a5b8-46ca62eed377.html", "html_tafsir"}, new String[]{"مواسم الخير", "https://api.websim.ai/blobs/01954219-6870-7fe4-ab9a-462872a4673f.html", "html_radio"}};
    private ProgressBar progressLoading;
    private int selectedTabIndex = 0;
    private LinearLayout stationsContainer;
    private LinearLayout tabsContainer;
    private TextView tvStatus;

    private void displayStations(List<String[]> list) {
        this.stationsContainer.removeAllViews();
        for (String[] strArr : list) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(0);
            linearLayout.setGravity(16);
            linearLayout.setBackgroundResource(R.drawable.radio_station_background);
            linearLayout.setPadding(dp(16), dp(14), dp(16), dp(14));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            layoutParams.setMargins(0, 0, 0, dp(8));
            linearLayout.setLayoutParams(layoutParams);
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.ic_radio);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(dp(32), dp(32));
            layoutParams2.setMargins(0, 0, dp(12), 0);
            imageView.setLayoutParams(layoutParams2);
            linearLayout.addView(imageView);
            TextView textView = new TextView(this);
            textView.setText(strArr[0]);
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
            linearLayout.setOnClickListener(new c2(0, strArr[0], this, strArr[1]));
            this.stationsContainer.addView(linearLayout);
        }
    }

    private int dp(int i2) {
        return (int) ((i2 * getResources().getDisplayMetrics().density) + 0.5f);
    }

    private String fetchUrl(String str) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setConnectTimeout(15000);
        httpURLConnection.setReadTimeout(15000);
        httpURLConnection.setRequestMethod("GET");
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                bufferedReader.close();
                httpURLConnection.disconnect();
                return sb.toString();
            }
            sb.append(readLine);
            sb.append("\n");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$displayStations$5(String str, String str2, View view) {
        Intent intent = new Intent(this, (Class<?>) RadioPlayerActivity.class);
        intent.putExtra(RadioService.EXTRA_STATION_TITLE, str);
        intent.putExtra(RadioService.EXTRA_STREAM_URL, str2);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadCategory$2(List list) {
        this.progressLoading.setVisibility(8);
        if (!list.isEmpty()) {
            displayStations(list);
        } else {
            this.tvStatus.setText("لم يتم العثور على إذاعات");
            this.tvStatus.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadCategory$3() {
        this.progressLoading.setVisibility(8);
        this.tvStatus.setText("فشل تحميل الإذاعات. تأكد من الاتصال بالإنترنت");
        this.tvStatus.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadCategory$4(String str, String str2) {
        try {
            String fetchUrl = fetchUrl(str);
            runOnUiThread(new e0(7, this, str2.equals("json") ? parseJsonStations(fetchUrl) : str2.equals("html_station") ? parseHtmlStationTitle(fetchUrl) : str2.equals("html_tafsir") ? parseHtmlTafsir(fetchUrl) : parseHtmlRadioItem(fetchUrl)));
        } catch (Exception e2) {
            e2.printStackTrace();
            runOnUiThread(new b2(4, this));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupTabs$1(int i2, View view) {
        if (i2 != this.selectedTabIndex) {
            this.selectedTabIndex = i2;
            updateTabSelection();
            loadCategory(i2);
        }
    }

    private void loadCategory(int i2) {
        this.stationsContainer.removeAllViews();
        this.progressLoading.setVisibility(0);
        this.tvStatus.setVisibility(8);
        String[] strArr = CATEGORIES[i2];
        new Thread(new b0(this, strArr[1], strArr[2], 5)).start();
    }

    private List<String[]> parseHtmlRadioItem(String str) {
        return parseTitleAndSource(str, "radio-title", "source");
    }

    private List<String[]> parseHtmlStationTitle(String str) {
        return parseTitleAndSource(str, "station-title", "source");
    }

    private List<String[]> parseHtmlTafsir(String str) {
        return parseTitleAndSource(str, "station-title", MimeTypes.BASE_TYPE_AUDIO);
    }

    private List<String[]> parseJsonStations(String str) {
        ArrayList arrayList = new ArrayList();
        try {
            Matcher matcher = Pattern.compile("\\[\"([^\"]+)\",\\s*\"([^\"]+)\"\\]").matcher(str);
            while (matcher.find()) {
                arrayList.add(new String[]{matcher.group(1), matcher.group(2)});
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }

    private List<String[]> parseTitleAndSource(String str, String str2, String str3) {
        ArrayList arrayList = new ArrayList();
        try {
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            Matcher matcher = Pattern.compile("<div\\s+class=\"" + str2 + "\">(.*?)</div>").matcher(str);
            while (matcher.find()) {
                arrayList2.add(matcher.group(1).trim());
            }
            Matcher matcher2 = (str3.equals("source") ? Pattern.compile("<source\\s+src=\"([^\"]+)\"") : Pattern.compile("<audio[^>]+src=\"([^\"]+)\"")).matcher(str);
            while (matcher2.find()) {
                arrayList3.add(matcher2.group(1).trim());
            }
            int min = Math.min(arrayList2.size(), arrayList3.size());
            for (int i2 = 0; i2 < min; i2++) {
                arrayList.add(new String[]{(String) arrayList2.get(i2), (String) arrayList3.get(i2)});
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }

    private void setupTabs() {
        this.tabsContainer.removeAllViews();
        final int i2 = 0;
        while (true) {
            String[][] strArr = CATEGORIES;
            if (i2 >= strArr.length) {
                return;
            }
            TextView textView = new TextView(this);
            textView.setText(strArr[i2][0]);
            textView.setTextSize(14.0f);
            textView.setGravity(17);
            textView.setPadding(dp(16), dp(8), dp(16), dp(8));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.setMargins(dp(4), 0, dp(4), 0);
            textView.setLayoutParams(layoutParams);
            if (i2 == this.selectedTabIndex) {
                textView.setBackgroundResource(R.drawable.radio_tab_selected);
                textView.setTextColor(-2052805);
            } else {
                textView.setBackgroundResource(R.drawable.radio_tab_background);
                textView.setTextColor(-7824982);
            }
            textView.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.d2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    RadioActivity.this.lambda$setupTabs$1(i2, view);
                }
            });
            this.tabsContainer.addView(textView);
            i2++;
        }
    }

    private void updateTabSelection() {
        for (int i2 = 0; i2 < this.tabsContainer.getChildCount(); i2++) {
            TextView textView = (TextView) this.tabsContainer.getChildAt(i2);
            if (i2 == this.selectedTabIndex) {
                textView.setBackgroundResource(R.drawable.radio_tab_selected);
                textView.setTextColor(-2052805);
            } else {
                textView.setBackgroundResource(R.drawable.radio_tab_background);
                textView.setTextColor(-7824982);
            }
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            Window window = getWindow();
            window.setStatusBarColor(-15918294);
            window.setNavigationBarColor(-15918294);
            setContentView(R.layout.activity_radio);
            this.tabsContainer = (LinearLayout) findViewById(R.id.tabsContainer);
            this.stationsContainer = (LinearLayout) findViewById(R.id.stationsContainer);
            this.progressLoading = (ProgressBar) findViewById(R.id.progressLoading);
            this.tvStatus = (TextView) findViewById(R.id.tvStatus);
            ((ImageView) findViewById(R.id.btnBack)).setOnClickListener(new y0(7, this));
            setupTabs();
            loadCategory(0);
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في تحميل الراديو", 0).show();
        }
    }
}
