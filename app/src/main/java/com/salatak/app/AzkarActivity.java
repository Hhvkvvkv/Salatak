package com.salatak.app;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import androidx.media3.extractor.text.ttml.TtmlNode;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class AzkarActivity extends AppCompatActivity {
    private JSONArray allCategories;
    private LinearLayout container;
    private boolean showingDownloads = false;
    private TextView tvToolbarTitle;

    private String buildCategoryTextContent(String str, String str2) {
        try {
            JSONArray categoryContent = getCategoryContent(str);
            if (categoryContent == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("📿 ");
            sb.append(str2);
            sb.append("\n\n");
            for (int i2 = 0; i2 < categoryContent.length(); i2++) {
                JSONObject jSONObject = categoryContent.getJSONObject(i2);
                sb.append(jSONObject.getString("text"));
                int optInt = jSONObject.optInt("times", 1);
                if (optInt > 1) {
                    sb.append("\n(");
                    sb.append(optInt);
                    sb.append(" مرات)");
                }
                if (i2 < categoryContent.length() - 1) {
                    sb.append("\n\n---\n\n");
                }
            }
            return sb.toString();
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private void copyCategoryText(String str, String str2) {
        try {
            String buildCategoryTextContent = buildCategoryTextContent(str, str2);
            if (buildCategoryTextContent == null) {
                Toast.makeText(this, "لم يتم العثور على النص", 0).show();
            } else {
                ((ClipboardManager) getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(str2, buildCategoryTextContent));
                Toast.makeText(this, "تم نسخ النص", 0).show();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في النسخ", 0).show();
        }
    }

    private LinearLayout createCategoryItem(String str, boolean z2) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setBackgroundResource(R.drawable.radio_station_background);
        linearLayout.setPadding(dp(16), dp(14), dp(16), dp(14));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(0, 0, 0, dp(8));
        linearLayout.setLayoutParams(layoutParams);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.ic_azkar);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(dp(32), dp(32));
        layoutParams2.setMargins(0, 0, dp(12), 0);
        imageView.setLayoutParams(layoutParams2);
        imageView.setImportantForAccessibility(2);
        linearLayout.addView(imageView);
        TextView textView = new TextView(this);
        textView.setText(str);
        textView.setTextColor(-2236963);
        textView.setTextSize(15.0f);
        textView.setMaxLines(2);
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
        linearLayout.addView(textView);
        if (z2) {
            ImageView imageView2 = new ImageView(this);
            imageView2.setImageResource(R.drawable.ic_download);
            imageView2.setColorFilter(-11751600);
            imageView2.setLayoutParams(new LinearLayout.LayoutParams(dp(20), dp(20)));
            imageView2.setImportantForAccessibility(2);
            linearLayout.addView(imageView2);
        }
        linearLayout.setClickable(true);
        linearLayout.setFocusable(true);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(z2 ? " - محمّل" : "");
        linearLayout.setContentDescription(sb.toString());
        try {
            TypedArray obtainStyledAttributes = obtainStyledAttributes(new int[]{android.R.attr.selectableItemBackground});
            Drawable drawable = obtainStyledAttributes.getDrawable(0);
            obtainStyledAttributes.recycle();
            if (drawable != null) {
                linearLayout.setForeground(drawable);
            }
        } catch (Exception unused) {
        }
        return linearLayout;
    }

    private void deleteCategory(String str) {
        File file = new File(getExternalFilesDir("azkar_downloads"), str);
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File file2 : listFiles) {
                    file2.delete();
                }
            }
            file.delete();
        }
    }

    private void downloadCategory(String str, final String str2) {
        JSONArray jSONArray;
        int i2 = 0;
        try {
            if (this.allCategories != null) {
                for (int i3 = 0; i3 < this.allCategories.length(); i3++) {
                    JSONObject jSONObject = this.allCategories.getJSONObject(i3);
                    if (jSONObject.getString(TtmlNode.ATTR_ID).equals(str)) {
                        jSONArray = jSONObject.getJSONArray("content");
                        break;
                    }
                }
            }
            jSONArray = null;
            if (jSONArray == null) {
                return;
            }
            final ArrayList arrayList = new ArrayList();
            final ArrayList arrayList2 = new ArrayList();
            for (int i4 = 0; i4 < jSONArray.length(); i4++) {
                String optString = jSONArray.getJSONObject(i4).optString(MimeTypes.BASE_TYPE_AUDIO, "");
                if (!optString.isEmpty()) {
                    arrayList.add(optString);
                    arrayList2.add(Integer.valueOf(i4));
                }
            }
            final File file = new File(getExternalFilesDir("azkar_downloads"), str);
            if (!file.exists()) {
                file.mkdirs();
            }
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("name", str2);
            jSONObject2.put("content", jSONArray);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(file, "data.json"));
            fileOutputStream.write(jSONObject2.toString().getBytes("UTF-8"));
            fileOutputStream.close();
            if (arrayList.isEmpty()) {
                Toast.makeText(this, "تم حفظ النص بنجاح", 0).show();
                loadCategories();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this, 4);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(1);
            linearLayout.setPadding(dp(24), dp(16), dp(24), dp(8));
            TextView textView = new TextView(this);
            textView.setText("جاري تحميل " + str2);
            textView.setTextSize(15.0f);
            textView.setTextColor(-2236963);
            textView.setGravity(17);
            linearLayout.addView(textView);
            final ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            progressBar.setMax(arrayList.size());
            progressBar.setProgress(0);
            progressBar.setProgressTintList(ColorStateList.valueOf(-2052805));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, dp(8));
            layoutParams.setMargins(0, dp(12), 0, dp(8));
            progressBar.setLayoutParams(layoutParams);
            linearLayout.addView(progressBar);
            final TextView textView2 = new TextView(this);
            textView2.setText("0 / " + arrayList.size());
            textView2.setTextSize(13.0f);
            textView2.setTextColor(-7824982);
            textView2.setGravity(17);
            linearLayout.addView(textView2);
            builder.setView(linearLayout);
            final boolean[] zArr = {false};
            builder.setNegativeButton("إلغاء", new q(i2, zArr));
            builder.setCancelable(false);
            final AlertDialog create = builder.create();
            create.show();
            final int size = arrayList.size();
            new Thread(new Runnable() { // from class: com.salatak.app.s
                @Override // java.lang.Runnable
                public final void run() {
                    AlertDialog alertDialog = create;
                    AzkarActivity.this.lambda$downloadCategory$13(size, zArr, arrayList, arrayList2, file, progressBar, textView2, alertDialog, str2);
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

    private JSONArray getCategoryContent(String str) {
        try {
            File file = new File(getExternalFilesDir("azkar_downloads"), str + "/data.json");
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] bArr = new byte[(int) file.length()];
                fileInputStream.read(bArr);
                fileInputStream.close();
                return new JSONObject(new String(bArr, "UTF-8")).getJSONArray("content");
            }
            if (this.allCategories == null) {
                return null;
            }
            for (int i2 = 0; i2 < this.allCategories.length(); i2++) {
                JSONObject jSONObject = this.allCategories.getJSONObject(i2);
                if (jSONObject.getString(TtmlNode.ATTR_ID).equals(str)) {
                    return jSONObject.getJSONArray("content");
                }
            }
            return null;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private String getCategoryNameFromData(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bArr = new byte[(int) file.length()];
            fileInputStream.read(bArr);
            fileInputStream.close();
            return new JSONObject(new String(bArr, "UTF-8")).optString("name", null);
        } catch (Exception unused) {
            return null;
        }
    }

    private boolean isCategoryDownloaded(String str) {
        return new File(getExternalFilesDir("azkar_downloads"), android.support.v4.media.l.g(str, "/data.json")).exists();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$downloadCategory$10(ProgressBar progressBar, int i2, TextView textView, int i3) {
        progressBar.setProgress(i2);
        textView.setText(i2 + " / " + i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$downloadCategory$11(ProgressBar progressBar, int i2, TextView textView, int i3) {
        progressBar.setProgress(i2);
        textView.setText(i2 + " / " + i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$downloadCategory$12(AlertDialog alertDialog, boolean[] zArr, int i2, int i3, String str) {
        alertDialog.dismiss();
        if (zArr[0]) {
            Toast.makeText(this, "تم الإلغاء - تم تحميل " + i2 + " من " + i3, 0).show();
        } else {
            Toast.makeText(this, "تم تحميل " + str + " بنجاح", 0).show();
        }
        loadCategories();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00e6, code lost:
    
        if (r12 == null) goto L53;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public /* synthetic */ void lambda$downloadCategory$13(final int r17, final boolean[] r18, java.util.List r19, java.util.List r20, java.io.File r21, final android.widget.ProgressBar r22, final android.widget.TextView r23, final android.app.AlertDialog r24, final java.lang.String r25) {
        /*
            Method dump skipped, instructions count: 290
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.salatak.app.AzkarActivity.lambda$downloadCategory$13(int, boolean[], java.util.List, java.util.List, java.io.File, android.widget.ProgressBar, android.widget.TextView, android.app.AlertDialog, java.lang.String):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$downloadCategory$9(boolean[] zArr, DialogInterface dialogInterface, int i2) {
        zArr[0] = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadCategories$2(String str, String str2, View view) {
        Intent intent = new Intent(this, (Class<?>) AzkarDetailActivity.class);
        intent.putExtra("category_id", str);
        intent.putExtra("category_name", str2);
        intent.putExtra("offline_mode", false);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$loadCategories$3(String str, String str2, View view) {
        showCategoryLongPressDialog(str, str2);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        if (!this.showingDownloads) {
            finish();
            return;
        }
        this.showingDownloads = false;
        this.tvToolbarTitle.setText("الأذكار");
        loadCategories();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        if (this.showingDownloads) {
            this.showingDownloads = false;
            this.tvToolbarTitle.setText("الأذكار");
            loadCategories();
        } else {
            this.showingDownloads = true;
            this.tvToolbarTitle.setText("التحميلات");
            showDownloadedCategories();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showCategoryLongPressDialog$6(String str, String str2, DialogInterface dialogInterface, int i2) {
        if (isCategoryDownloaded(str)) {
            if (i2 == 0) {
                copyCategoryText(str, str2);
            }
        } else if (i2 == 0) {
            downloadCategory(str, str2);
        } else if (i2 == 1) {
            copyCategoryText(str, str2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showDownloadedCategories$4(String str, String str2, View view) {
        Intent intent = new Intent(this, (Class<?>) AzkarDetailActivity.class);
        intent.putExtra("category_id", str);
        intent.putExtra("category_name", str2);
        intent.putExtra("offline_mode", true);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$showDownloadedCategories$5(String str, String str2, View view) {
        showDownloadedLongPressDialog(str, str2);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showDownloadedLongPressDialog$7(String str, DialogInterface dialogInterface, int i2) {
        deleteCategory(str);
        Toast.makeText(this, "تم الحذف", 0).show();
        showDownloadedCategories();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showDownloadedLongPressDialog$8(String str, String str2, DialogInterface dialogInterface, int i2) {
        if (i2 == 0) {
            new AlertDialog.Builder(this, 4).setTitle("حذف " + str).setMessage("هل تريد حذف هذا الذكر نصياً وصوتياً؟").setPositiveButton("حذف", new p(this, str2, 0)).setNegativeButton("إلغاء", (DialogInterface.OnClickListener) null).show();
            return;
        }
        if (i2 == 1) {
            copyCategoryText(str2, str);
        } else if (i2 == 2) {
            shareCategoryText(str2, str);
        }
    }

    private void loadCategories() {
        this.container.removeAllViews();
        if (this.allCategories == null) {
            return;
        }
        for (int i2 = 0; i2 < this.allCategories.length(); i2++) {
            try {
                JSONObject jSONObject = this.allCategories.getJSONObject(i2);
                String string = jSONObject.getString(TtmlNode.ATTR_ID);
                String string2 = jSONObject.getString("name");
                LinearLayout createCategoryItem = createCategoryItem(string2, isCategoryDownloaded(string));
                createCategoryItem.setOnClickListener(new l(this, string, string2, 0));
                createCategoryItem.setOnLongClickListener(new m(this, string, string2, 0));
                this.container.addView(createCategoryItem);
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
        }
    }

    private String loadJsonFromAssets() {
        try {
            InputStream open = getAssets().open("athkar.json");
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return new String(bArr, "UTF-8");
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private void shareCategoryText(String str, String str2) {
        try {
            String buildCategoryTextContent = buildCategoryTextContent(str, str2);
            if (buildCategoryTextContent == null) {
                Toast.makeText(this, "لم يتم العثور على النص", 0).show();
                return;
            }
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", str2);
            intent.putExtra("android.intent.extra.TEXT", buildCategoryTextContent);
            startActivity(Intent.createChooser(intent, "مشاركة"));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void showCategoryLongPressDialog(String str, String str2) {
        new AlertDialog.Builder(this, 4).setTitle(str2).setItems(isCategoryDownloaded(str) ? new String[]{"نسخ النص"} : new String[]{"تحميل", "نسخ النص"}, new i(this, str, str2, 1)).show();
    }

    private void showDownloadedCategories() {
        this.container.removeAllViews();
        File externalFilesDir = getExternalFilesDir("azkar_downloads");
        if (externalFilesDir == null || !externalFilesDir.exists()) {
            showEmpty("لا توجد تحميلات بعد\nاضغط مطوّلاً على أي عنوان لتحميله");
            return;
        }
        File[] listFiles = externalFilesDir.listFiles(new o());
        if (listFiles == null || listFiles.length == 0) {
            showEmpty("لا توجد تحميلات بعد\nاضغط مطوّلاً على أي عنوان لتحميله");
            return;
        }
        boolean z2 = false;
        for (File file : listFiles) {
            File file2 = new File(file, "data.json");
            if (file2.exists()) {
                String name = file.getName();
                String categoryNameFromData = getCategoryNameFromData(file2);
                if (categoryNameFromData == null) {
                    categoryNameFromData = android.support.v4.media.l.t("ذكر ", name);
                }
                LinearLayout createCategoryItem = createCategoryItem(categoryNameFromData, true);
                int i2 = 1;
                createCategoryItem.setOnClickListener(new l(this, name, categoryNameFromData, i2));
                createCategoryItem.setOnLongClickListener(new m(this, name, categoryNameFromData, i2));
                this.container.addView(createCategoryItem);
                z2 = true;
            }
        }
        if (z2) {
            return;
        }
        showEmpty("لا توجد تحميلات بعد\nاضغط مطوّلاً على أي عنوان لتحميله");
    }

    private void showDownloadedLongPressDialog(String str, String str2) {
        new AlertDialog.Builder(this, 4).setTitle(str2).setItems(new String[]{"حذف", "نسخ النص", "مشاركة"}, new i(this, str2, str, 0)).show();
    }

    private void showEmpty(String str) {
        this.container.removeAllViews();
        TextView textView = new TextView(this);
        textView.setText(str);
        textView.setTextColor(-7824982);
        textView.setTextSize(16.0f);
        textView.setGravity(17);
        textView.setPadding(dp(32), dp(64), dp(32), dp(64));
        this.container.addView(textView);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (!this.showingDownloads) {
            super.onBackPressed();
            return;
        }
        this.showingDownloads = false;
        this.tvToolbarTitle.setText("الأذكار");
        loadCategories();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            Window window = getWindow();
            window.setStatusBarColor(-15918294);
            window.setNavigationBarColor(-15918294);
            setContentView(R.layout.activity_azkar);
            this.tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
            this.container = (LinearLayout) findViewById(R.id.azkarContainer);
            final int i2 = 0;
            ((ImageView) findViewById(R.id.btnBack)).setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.n
                public final /* synthetic */ AzkarActivity b;

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
            ((LinearLayout) findViewById(R.id.btnDownloadsContainer)).setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.n
                public final /* synthetic */ AzkarActivity b;

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
            String loadJsonFromAssets = loadJsonFromAssets();
            if (loadJsonFromAssets != null) {
                this.allCategories = new JSONArray(loadJsonFromAssets);
            }
            loadCategories();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (this.showingDownloads) {
            showDownloadedCategories();
        }
    }
}
