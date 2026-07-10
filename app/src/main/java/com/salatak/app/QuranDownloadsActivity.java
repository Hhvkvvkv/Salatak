package com.salatak.app;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class QuranDownloadsActivity extends AppCompatActivity {
    private String currentReciter = null;
    private LinearLayout downloadsContainer;
    private TextView tvEmpty;

    private int dp(int i2) {
        return (int) ((i2 * getResources().getDisplayMetrics().density) + 0.5f);
    }

    private int extractNumber(String str) {
        try {
            return Integer.parseInt(str.replace(".mp3", "").replace(".meta", ""));
        } catch (Exception unused) {
            return 999;
        }
    }

    private String getSurahNameFromMeta(File file, int i2) {
        File file2 = new File(file, i2 + ".meta");
        if (!file2.exists()) {
            return null;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file2);
            byte[] bArr = new byte[(int) file2.length()];
            fileInputStream.read(bArr);
            fileInputStream.close();
            String[] split = new String(bArr, "UTF-8").split("\n");
            if (split.length > 0) {
                return split[0].trim();
            }
        } catch (Exception unused) {
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$loadDownloadedReciters$1(File file, String str) {
        return str.endsWith(".mp3");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$loadDownloadedReciters$2(File file, File file2) {
        return file.getName().compareTo(file2.getName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$loadDownloadedReciters$3(File file, String str) {
        return str.endsWith(".mp3");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadDownloadedReciters$4(String str, View view) {
        showReciterSurahs(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        if (this.currentReciter == null) {
            finish();
        } else {
            this.currentReciter = null;
            loadDownloadedReciters();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$showReciterSurahs$5(File file, String str) {
        return str.endsWith(".mp3");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$showReciterSurahs$6(File file, File file2) {
        return Integer.compare(extractNumber(file.getName()), extractNumber(file2.getName()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showReciterSurahs$7(String str, ArrayList arrayList, int i2, String str2, View view) {
        Intent intent = new Intent(this, (Class<?>) QuranPlayerActivity.class);
        intent.putExtra("reciter_name", str);
        intent.putExtra("surah_name", ((String[]) arrayList.get(i2))[0]);
        intent.putExtra("surah_url", ((String[]) arrayList.get(i2))[1]);
        intent.putExtra("surahs_json", str2);
        intent.putExtra("surah_index", i2);
        startActivity(intent);
    }

    private void loadDownloadedReciters() {
        this.downloadsContainer.removeAllViews();
        TextView textView = null;
        this.currentReciter = null;
        LinearLayout linearLayout = (LinearLayout) this.downloadsContainer.getParent().getParent();
        for (int i2 = 0; i2 < linearLayout.getChildCount(); i2++) {
            View childAt = linearLayout.getChildAt(i2);
            if (childAt instanceof LinearLayout) {
                LinearLayout linearLayout2 = (LinearLayout) childAt;
                for (int i3 = 0; i3 < linearLayout2.getChildCount(); i3++) {
                    if (linearLayout2.getChildAt(i3) instanceof TextView) {
                        TextView textView2 = (TextView) linearLayout2.getChildAt(i3);
                        if ("التنزيلات".equals(textView2.getText().toString()) || textView2.getTextSize() > getResources().getDisplayMetrics().density * 18.0f) {
                            textView = textView2;
                            break;
                        }
                    }
                }
                if (textView != null) {
                    break;
                }
            }
        }
        if (textView != null) {
            textView.setText("التنزيلات");
        }
        File externalFilesDir = getExternalFilesDir("quran");
        if (externalFilesDir == null || !externalFilesDir.exists()) {
            this.tvEmpty.setVisibility(0);
            this.tvEmpty.setText("لا توجد تنزيلات بعد");
            return;
        }
        File[] listFiles = externalFilesDir.listFiles(new o());
        if (listFiles == null || listFiles.length == 0) {
            this.tvEmpty.setVisibility(0);
            this.tvEmpty.setText("لا توجد تنزيلات بعد");
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (File file : listFiles) {
            File[] listFiles2 = file.listFiles(new r1(1));
            if (listFiles2 != null && listFiles2.length > 0) {
                arrayList.add(file);
            }
        }
        if (arrayList.isEmpty()) {
            this.tvEmpty.setVisibility(0);
            this.tvEmpty.setText("لا توجد تنزيلات بعد");
            return;
        }
        this.tvEmpty.setVisibility(8);
        Collections.sort(arrayList, new u1());
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            File file2 = (File) it.next();
            File[] listFiles3 = file2.listFiles(new r1(2));
            String name = file2.getName();
            int length = listFiles3 != null ? listFiles3.length : 0;
            LinearLayout linearLayout3 = new LinearLayout(this);
            linearLayout3.setOrientation(0);
            linearLayout3.setGravity(16);
            linearLayout3.setBackgroundResource(R.drawable.radio_station_background);
            linearLayout3.setPadding(dp(16), dp(14), dp(16), dp(14));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            layoutParams.setMargins(0, 0, 0, dp(8));
            linearLayout3.setLayoutParams(layoutParams);
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.ic_quran_audio);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(dp(32), dp(32));
            layoutParams2.setMargins(0, 0, dp(12), 0);
            imageView.setLayoutParams(layoutParams2);
            linearLayout3.addView(imageView);
            LinearLayout linearLayout4 = new LinearLayout(this);
            linearLayout4.setOrientation(1);
            linearLayout4.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
            TextView textView3 = new TextView(this);
            textView3.setText(name);
            textView3.setTextColor(-2236963);
            textView3.setTextSize(15.0f);
            textView3.setMaxLines(1);
            linearLayout4.addView(textView3);
            TextView textView4 = new TextView(this);
            textView4.setText(length + " سورة محملة");
            textView4.setTextColor(-7824982);
            textView4.setTextSize(12.0f);
            linearLayout4.addView(textView4);
            linearLayout3.addView(linearLayout4);
            linearLayout3.setClickable(true);
            linearLayout3.setFocusable(true);
            try {
                TypedArray obtainStyledAttributes = obtainStyledAttributes(new int[]{android.R.attr.selectableItemBackground});
                Drawable drawable = obtainStyledAttributes.getDrawable(0);
                obtainStyledAttributes.recycle();
                if (drawable != null) {
                    linearLayout3.setForeground(drawable);
                }
            } catch (Exception unused) {
            }
            linearLayout3.setOnClickListener(new j0(5, this, name));
            this.downloadsContainer.addView(linearLayout3);
        }
    }

    private void showReciterSurahs(final String str) {
        this.currentReciter = str;
        this.downloadsContainer.removeAllViews();
        LinearLayout linearLayout = (LinearLayout) this.downloadsContainer.getParent().getParent();
        for (int i2 = 0; i2 < linearLayout.getChildCount(); i2++) {
            View childAt = linearLayout.getChildAt(i2);
            if (childAt instanceof LinearLayout) {
                LinearLayout linearLayout2 = (LinearLayout) childAt;
                int i3 = 0;
                while (true) {
                    if (i3 >= linearLayout2.getChildCount()) {
                        break;
                    }
                    if (linearLayout2.getChildAt(i3) instanceof TextView) {
                        TextView textView = (TextView) linearLayout2.getChildAt(i3);
                        if (textView.getTextSize() > getResources().getDisplayMetrics().density * 18.0f) {
                            textView.setText(str);
                            break;
                        }
                    }
                    i3++;
                }
            }
        }
        File file = new File(getExternalFilesDir("quran"), str);
        if (!file.exists()) {
            this.tvEmpty.setVisibility(0);
            this.tvEmpty.setText("لا توجد سور محملة");
            return;
        }
        File[] listFiles = file.listFiles(new r1(0));
        if (listFiles == null || listFiles.length == 0) {
            this.tvEmpty.setVisibility(0);
            this.tvEmpty.setText("لا توجد سور محملة");
            return;
        }
        this.tvEmpty.setVisibility(8);
        ArrayList arrayList = new ArrayList(Arrays.asList(listFiles));
        Collections.sort(arrayList, new s1(this, 0));
        final ArrayList arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            File file2 = (File) it.next();
            int extractNumber = extractNumber(file2.getName());
            String surahNameFromMeta = getSurahNameFromMeta(file, extractNumber);
            if (surahNameFromMeta == null) {
                surahNameFromMeta = extractNumber + " سورة";
            }
            arrayList2.add(new String[]{surahNameFromMeta, file2.getAbsolutePath()});
        }
        JSONObject jSONObject = new JSONObject();
        try {
            Iterator it2 = arrayList2.iterator();
            while (it2.hasNext()) {
                String[] strArr = (String[]) it2.next();
                jSONObject.put(strArr[0], strArr[1]);
            }
        } catch (Exception unused) {
        }
        final String jSONObject2 = jSONObject.toString();
        for (int i4 = 0; i4 < arrayList2.size(); i4++) {
            String str2 = ((String[]) arrayList2.get(i4))[0];
            LinearLayout linearLayout3 = new LinearLayout(this);
            linearLayout3.setOrientation(0);
            linearLayout3.setGravity(16);
            linearLayout3.setBackgroundResource(R.drawable.radio_station_background);
            linearLayout3.setPadding(dp(16), dp(14), dp(16), dp(14));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            layoutParams.setMargins(0, 0, 0, dp(8));
            linearLayout3.setLayoutParams(layoutParams);
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.ic_quran_audio);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(dp(32), dp(32));
            layoutParams2.setMargins(0, 0, dp(12), 0);
            imageView.setLayoutParams(layoutParams2);
            linearLayout3.addView(imageView);
            TextView textView2 = new TextView(this);
            textView2.setText(str2);
            textView2.setTextColor(-2236963);
            textView2.setTextSize(15.0f);
            textView2.setMaxLines(1);
            textView2.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
            linearLayout3.addView(textView2);
            ImageView imageView2 = new ImageView(this);
            imageView2.setImageResource(R.drawable.ic_play_circle);
            imageView2.setLayoutParams(new LinearLayout.LayoutParams(dp(28), dp(28)));
            linearLayout3.addView(imageView2);
            linearLayout3.setClickable(true);
            linearLayout3.setFocusable(true);
            try {
                TypedArray obtainStyledAttributes = obtainStyledAttributes(new int[]{android.R.attr.selectableItemBackground});
                Drawable drawable = obtainStyledAttributes.getDrawable(0);
                obtainStyledAttributes.recycle();
                if (drawable != null) {
                    linearLayout3.setForeground(drawable);
                }
            } catch (Exception unused2) {
            }
            final int i5 = i4;
            linearLayout3.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.t1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    QuranDownloadsActivity.this.lambda$showReciterSurahs$7(str, arrayList2, i5, jSONObject2, view);
                }
            });
            this.downloadsContainer.addView(linearLayout3);
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.currentReciter == null) {
            super.onBackPressed();
        } else {
            this.currentReciter = null;
            loadDownloadedReciters();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            Window window = getWindow();
            window.setStatusBarColor(-15918294);
            window.setNavigationBarColor(-15918294);
            setContentView(R.layout.activity_quran_downloads);
            this.downloadsContainer = (LinearLayout) findViewById(R.id.downloadsContainer);
            this.tvEmpty = (TextView) findViewById(R.id.tvEmpty);
            ((ImageView) findViewById(R.id.btnBack)).setOnClickListener(new y0(5, this));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        String str = this.currentReciter;
        if (str != null) {
            showReciterSurahs(str);
        } else {
            loadDownloadedReciters();
        }
    }
}
