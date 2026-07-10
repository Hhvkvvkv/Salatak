package com.salatak.app;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MimeTypes;
import androidx.media3.extractor.text.ttml.TtmlNode;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class AzkarDetailActivity extends AppCompatActivity {
    private String categoryId;
    private String categoryName;
    private LinearLayout container;
    private ImageView currentPlayingIcon;
    private int currentPlayingIndex = -1;
    private MediaPlayer mediaPlayer;
    private boolean offlineMode;

    private int dp(int i2) {
        return (int) ((i2 * getResources().getDisplayMetrics().density) + 0.5f);
    }

    private boolean hasLocalAudio(int i2) {
        return new File(getExternalFilesDir("azkar_downloads"), this.categoryId + "/" + i2 + ".mp3").exists();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAzkar$1(int i2, ImageView imageView, String str, View view) {
        if (hasLocalAudio(i2)) {
            playLocalAudio(i2, imageView);
        } else {
            if (str.isEmpty()) {
                return;
            }
            playOnlineAudio(str, i2, imageView);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playLocalAudio$6(ImageView imageView, MediaPlayer mediaPlayer) {
        imageView.setImageResource(R.drawable.ic_play_circle);
        this.currentPlayingIndex = -1;
        this.currentPlayingIcon = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playOnlineAudio$3(ImageView imageView, MediaPlayer mediaPlayer) {
        imageView.setImageResource(R.drawable.ic_play_circle);
        this.currentPlayingIndex = -1;
        this.currentPlayingIcon = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$playOnlineAudio$4(ImageView imageView, MediaPlayer mediaPlayer, int i2, int i3) {
        Toast.makeText(this, "خطأ في تشغيل الصوت", 0).show();
        imageView.setImageResource(R.drawable.ic_play_circle);
        this.currentPlayingIndex = -1;
        return true;
    }

    private void loadAzkar() {
        this.container.removeAllViews();
        try {
            JSONArray loadContent = loadContent();
            if (loadContent == null) {
                TextView textView = new TextView(this);
                textView.setText("لم يتم العثور على الأذكار");
                textView.setTextColor(-7824982);
                textView.setTextSize(16.0f);
                textView.setGravity(17);
                textView.setPadding(dp(32), dp(64), dp(32), dp(64));
                this.container.addView(textView);
                return;
            }
            for (int i2 = 0; i2 < loadContent.length(); i2++) {
                JSONObject jSONObject = loadContent.getJSONObject(i2);
                String string = jSONObject.getString("text");
                int optInt = jSONObject.optInt("times", 1);
                String optString = jSONObject.optString(MimeTypes.BASE_TYPE_AUDIO, "");
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(1);
                linearLayout.setBackgroundResource(R.drawable.radio_station_background);
                linearLayout.setPadding(dp(16), dp(14), dp(16), dp(14));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
                layoutParams.setMargins(0, 0, 0, dp(10));
                linearLayout.setLayoutParams(layoutParams);
                TextView textView2 = new TextView(this);
                textView2.setText(string);
                textView2.setTextColor(-2236963);
                textView2.setTextSize(16.0f);
                textView2.setLineSpacing(dp(4), 1.0f);
                linearLayout.addView(textView2);
                LinearLayout linearLayout2 = new LinearLayout(this);
                linearLayout2.setOrientation(0);
                linearLayout2.setGravity(16);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                layoutParams2.setMargins(0, dp(8), 0, 0);
                linearLayout2.setLayoutParams(layoutParams2);
                if (optInt > 1) {
                    TextView textView3 = new TextView(this);
                    textView3.setText("التكرار: " + optInt);
                    textView3.setTextColor(-2052805);
                    textView3.setTextSize(12.0f);
                    textView3.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
                    linearLayout2.addView(textView3);
                } else {
                    View view = new View(this);
                    view.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1.0f));
                    linearLayout2.addView(view);
                }
                if (!optString.isEmpty() || hasLocalAudio(i2)) {
                    ImageView imageView = new ImageView(this);
                    imageView.setImageResource(R.drawable.ic_play_circle);
                    LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(dp(28), dp(28));
                    layoutParams3.setMargins(dp(8), 0, 0, 0);
                    imageView.setLayoutParams(layoutParams3);
                    imageView.setContentDescription("تشغيل الذكر");
                    linearLayout2.addView(imageView);
                    linearLayout.setOnClickListener(new w(this, i2, imageView, optString));
                }
                linearLayout.addView(linearLayout2);
                this.container.addView(linearLayout);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private JSONArray loadContent() {
        try {
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (!this.offlineMode) {
            String loadJsonFromAssets = loadJsonFromAssets();
            if (loadJsonFromAssets == null) {
                return null;
            }
            JSONArray jSONArray = new JSONArray(loadJsonFromAssets);
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i2);
                if (jSONObject.getString(TtmlNode.ATTR_ID).equals(this.categoryId)) {
                    return jSONObject.getJSONArray("content");
                }
            }
            return null;
        }
        File file = new File(getExternalFilesDir("azkar_downloads"), this.categoryId + "/data.json");
        if (!file.exists()) {
            return null;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bArr = new byte[(int) file.length()];
        fileInputStream.read(bArr);
        fileInputStream.close();
        return new JSONObject(new String(bArr, "UTF-8")).getJSONArray("content");
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

    private void playLocalAudio(int i2, ImageView imageView) {
        MediaPlayer mediaPlayer;
        try {
            if (this.currentPlayingIndex == i2 && (mediaPlayer = this.mediaPlayer) != null && mediaPlayer.isPlaying()) {
                this.mediaPlayer.pause();
                imageView.setImageResource(R.drawable.ic_play_circle);
                this.currentPlayingIndex = -1;
                return;
            }
            stopCurrentAudio();
            File file = new File(getExternalFilesDir("azkar_downloads"), this.categoryId + "/" + i2 + ".mp3");
            if (!file.exists()) {
                Toast.makeText(this, "الملف غير موجود", 0).show();
                return;
            }
            this.currentPlayingIndex = i2;
            this.currentPlayingIcon = imageView;
            imageView.setImageResource(R.drawable.ic_pause_circle);
            MediaPlayer mediaPlayer2 = new MediaPlayer();
            this.mediaPlayer = mediaPlayer2;
            mediaPlayer2.setDataSource(file.getAbsolutePath());
            this.mediaPlayer.setOnPreparedListener(new x0(2));
            this.mediaPlayer.setOnCompletionListener(new v(this, imageView, 0));
            this.mediaPlayer.prepare();
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في تشغيل الصوت", 0).show();
        }
    }

    private void playOnlineAudio(String str, int i2, final ImageView imageView) {
        MediaPlayer mediaPlayer;
        try {
            if (this.currentPlayingIndex == i2 && (mediaPlayer = this.mediaPlayer) != null && mediaPlayer.isPlaying()) {
                this.mediaPlayer.pause();
                imageView.setImageResource(R.drawable.ic_play_circle);
                this.currentPlayingIndex = -1;
                return;
            }
            stopCurrentAudio();
            this.currentPlayingIndex = i2;
            this.currentPlayingIcon = imageView;
            imageView.setImageResource(R.drawable.ic_pause_circle);
            MediaPlayer mediaPlayer2 = new MediaPlayer();
            this.mediaPlayer = mediaPlayer2;
            mediaPlayer2.setDataSource(str);
            this.mediaPlayer.setOnPreparedListener(new x0(3));
            this.mediaPlayer.setOnCompletionListener(new v(this, imageView, 1));
            this.mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.salatak.app.x
                @Override // android.media.MediaPlayer.OnErrorListener
                public final boolean onError(MediaPlayer mediaPlayer3, int i3, int i4) {
                    boolean lambda$playOnlineAudio$4;
                    lambda$playOnlineAudio$4 = AzkarDetailActivity.this.lambda$playOnlineAudio$4(imageView, mediaPlayer3, i3, i4);
                    return lambda$playOnlineAudio$4;
                }
            });
            this.mediaPlayer.prepareAsync();
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في تشغيل الصوت", 0).show();
        }
    }

    private void stopCurrentAudio() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    this.mediaPlayer.stop();
                }
                this.mediaPlayer.release();
            } catch (Exception unused) {
            }
            this.mediaPlayer = null;
        }
        ImageView imageView = this.currentPlayingIcon;
        if (imageView != null) {
            imageView.setImageResource(R.drawable.ic_play_circle);
        }
        this.currentPlayingIndex = -1;
        this.currentPlayingIcon = null;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            Window window = getWindow();
            window.setStatusBarColor(-15918294);
            window.setNavigationBarColor(-15918294);
            setContentView(R.layout.activity_azkar_detail);
            this.categoryId = getIntent().getStringExtra("category_id");
            this.categoryName = getIntent().getStringExtra("category_name");
            this.offlineMode = getIntent().getBooleanExtra("offline_mode", false);
            ImageView imageView = (ImageView) findViewById(R.id.btnBack);
            TextView textView = (TextView) findViewById(R.id.tvToolbarTitle);
            this.container = (LinearLayout) findViewById(R.id.azkarDetailContainer);
            textView.setText(this.categoryName);
            imageView.setOnClickListener(new y0(1, this));
            loadAzkar();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        stopCurrentAudio();
        super.onDestroy();
    }
}
