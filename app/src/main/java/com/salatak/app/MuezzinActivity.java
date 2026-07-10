package com.salatak.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.datasource.cache.ContentMetadata;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.salatak.app.AdhanSoundsData;
import com.salatak.app.MuezzinActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MuezzinActivity extends AppCompatActivity {
    private static final String FILTER_ALL = "all";
    private static final String FILTER_DOWNLOADS = "downloads";
    private static final String FILTER_FAJR = "fajr";
    private static final String FILTER_GENERAL = "general";
    private MuezzinAdapter adapter;
    private String currentFilter = "all";
    private String currentSearch = "";
    private String currentlyPlayingId = "";
    private ActivityResultLauncher<String[]> customAdhanPicker;
    private EditText etSearch;
    private ListView lvMuezzins;
    private MediaPlayer mediaPlayer;
    private String prayerKey;
    private String prayerName;
    private SharedPreferences prefs;
    private TextView tvCount;
    private TextView tvFilterLabel;

    public static class DownloadedItem {
        String displayName;
        File file;
        String fileName;

        public DownloadedItem(String str, String str2, File file) {
            this.fileName = str;
            this.displayName = str2;
            this.file = file;
        }
    }

    public class DownloadsAdapter extends BaseAdapter {
        private static final int TYPE_ADD_BUTTON = 1;
        private static final int TYPE_ITEM = 0;
        private List<DownloadedItem> items;
        private String selectedId;

        public DownloadsAdapter(List<DownloadedItem> list, String str) {
            this.items = list;
            this.selectedId = str;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getView$0(View view) {
            MuezzinActivity.this.openFilePicker();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getView$1() {
            notifyDataSetChanged();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getView$2(MediaPlayer mediaPlayer) {
            MuezzinActivity.this.stopMediaPlayer();
            MuezzinActivity.this.currentlyPlayingId = "";
            MuezzinActivity.this.runOnUiThread(new u0(this, 1));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getView$3() {
            Toast.makeText(MuezzinActivity.this, "خطأ في تشغيل الأذان", 0).show();
            notifyDataSetChanged();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$getView$4(MediaPlayer mediaPlayer, int i2, int i3) {
            MuezzinActivity.this.stopMediaPlayer();
            MuezzinActivity.this.currentlyPlayingId = "";
            MuezzinActivity.this.runOnUiThread(new u0(this, 0));
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getView$6(String str, DownloadedItem downloadedItem, View view) {
            if ((MuezzinActivity.this.currentlyPlayingId.equals(str) || MuezzinActivity.this.currentlyPlayingId.equals(downloadedItem.fileName)) && MuezzinActivity.this.mediaPlayer != null && MuezzinActivity.this.mediaPlayer.isPlaying()) {
                MuezzinActivity.this.stopMediaPlayer();
                MuezzinActivity.this.currentlyPlayingId = "";
                notifyDataSetChanged();
                return;
            }
            MuezzinActivity.this.stopMediaPlayer();
            MuezzinActivity.this.currentlyPlayingId = str;
            notifyDataSetChanged();
            try {
                MuezzinActivity.this.mediaPlayer = new MediaPlayer();
                File file = downloadedItem.file;
                if (file == null || !file.exists()) {
                    AssetFileDescriptor openFd = MuezzinActivity.this.getAssets().openFd("adhan_sounds/" + str + ".mp3");
                    MuezzinActivity.this.mediaPlayer.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
                    openFd.close();
                } else {
                    MuezzinActivity.this.mediaPlayer.setDataSource(downloadedItem.file.getAbsolutePath());
                }
                MuezzinActivity.this.mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setUsage(1).setContentType(2).build());
                MuezzinActivity.this.mediaPlayer.setOnCompletionListener(new v0(0, this));
                MuezzinActivity.this.mediaPlayer.setOnErrorListener(new w0(0, this));
                MuezzinActivity.this.mediaPlayer.prepareAsync();
                MuezzinActivity.this.mediaPlayer.setOnPreparedListener(new x0(0));
            } catch (Exception e2) {
                e2.printStackTrace();
                MuezzinActivity.this.currentlyPlayingId = "";
                notifyDataSetChanged();
                Toast.makeText(MuezzinActivity.this, "خطأ في تشغيل الأذان", 0).show();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getView$7(String str, DownloadedItem downloadedItem, View view) {
            this.selectedId = str;
            SharedPreferences.Editor edit = MuezzinActivity.this.prefs.edit();
            edit.putString(MuezzinActivity.this.prayerKey + "_adhan_sound_id", str);
            edit.apply();
            try {
                PrayerReminderScheduler.scheduleAdhanAlarms(MuezzinActivity.this);
            } catch (Exception unused) {
            }
            Toast.makeText(MuezzinActivity.this, "تم تفعيل: " + downloadedItem.displayName, 0).show();
            notifyDataSetChanged();
            MuezzinActivity.this.setResult(-1);
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.items.size() + 1;
        }

        @Override // android.widget.Adapter
        public Object getItem(int i2) {
            if (i2 < this.items.size()) {
                return this.items.get(i2);
            }
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i2) {
            return i2;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getItemViewType(int i2) {
            return i2 < this.items.size() ? 0 : 1;
        }

        @Override // android.widget.Adapter
        public View getView(int i2, View view, ViewGroup viewGroup) {
            if (i2 >= this.items.size()) {
                if (view == null || view.getTag() == null || !view.getTag().equals("add_btn")) {
                    view = LayoutInflater.from(MuezzinActivity.this).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
                    view.setTag("add_btn");
                }
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText("إضافة من الجهاز");
                textView.setTextColor(-666250);
                textView.setTextSize(16.0f);
                textView.setGravity(17);
                textView.setPadding(20, 30, 20, 30);
                view.setBackgroundColor(-14476531);
                view.setOnClickListener(new y0(0, this));
                view.setContentDescription("إضافة مؤذن من الجهاز");
                return view;
            }
            if (view == null || (view.getTag() != null && view.getTag().equals("add_btn"))) {
                view = LayoutInflater.from(MuezzinActivity.this).inflate(R.layout.item_muezzin, viewGroup, false);
            }
            final DownloadedItem downloadedItem = this.items.get(i2);
            final String str = downloadedItem.fileName;
            if (str.endsWith(".mp3")) {
                str = str.substring(0, str.length() - 4);
            }
            TextView textView2 = (TextView) view.findViewById(R.id.tvMuezzinName);
            TextView textView3 = (TextView) view.findViewById(R.id.tvMuezzinType);
            TextView textView4 = (TextView) view.findViewById(R.id.tvMuezzinCountry);
            TextView textView5 = (TextView) view.findViewById(R.id.tvMuezzinStatus);
            ImageView imageView = (ImageView) view.findViewById(R.id.btnPlay);
            ImageView imageView2 = (ImageView) view.findViewById(R.id.btnDownload);
            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressDownload);
            ImageView imageView3 = (ImageView) view.findViewById(R.id.btnActivate);
            textView2.setText(downloadedItem.displayName);
            textView3.setVisibility(8);
            textView4.setText("محمّل");
            textView4.setVisibility(0);
            boolean z2 = str.equals(this.selectedId) || downloadedItem.fileName.equals(this.selectedId);
            if (z2) {
                textView5.setText("مفعّل");
                textView5.setTextColor(-666250);
                textView5.setVisibility(0);
                view.setBackgroundColor(858398474);
            } else {
                textView5.setVisibility(8);
                view.setBackgroundColor(-15068152);
            }
            imageView2.setVisibility(8);
            progressBar.setVisibility(8);
            imageView.setVisibility(0);
            if ((MuezzinActivity.this.currentlyPlayingId.equals(str) || MuezzinActivity.this.currentlyPlayingId.equals(downloadedItem.fileName)) && MuezzinActivity.this.mediaPlayer != null && MuezzinActivity.this.mediaPlayer.isPlaying()) {
                imageView.setImageResource(android.R.drawable.ic_media_pause);
                imageView.setContentDescription("إيقاف " + downloadedItem.displayName);
            } else {
                imageView.setImageResource(android.R.drawable.ic_media_play);
                imageView.setContentDescription("تشغيل " + downloadedItem.displayName);
            }
            final int i3 = 0;
            imageView.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.z0
                public final /* synthetic */ MuezzinActivity.DownloadsAdapter b;

                {
                    this.b = this;
                }

                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    switch (i3) {
                        case 0:
                            this.b.lambda$getView$6(str, downloadedItem, view2);
                            break;
                        default:
                            this.b.lambda$getView$7(str, downloadedItem, view2);
                            break;
                    }
                }
            });
            if (z2) {
                imageView3.setImageResource(android.R.drawable.checkbox_on_background);
            } else {
                imageView3.setImageResource(android.R.drawable.ic_input_add);
            }
            StringBuilder sb = z2 ? new StringBuilder("مفعّل - ") : new StringBuilder("تفعيل ");
            sb.append(downloadedItem.displayName);
            imageView3.setContentDescription(sb.toString());
            final int i4 = 1;
            imageView3.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.z0
                public final /* synthetic */ MuezzinActivity.DownloadsAdapter b;

                {
                    this.b = this;
                }

                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    switch (i4) {
                        case 0:
                            this.b.lambda$getView$6(str, downloadedItem, view2);
                            break;
                        default:
                            this.b.lambda$getView$7(str, downloadedItem, view2);
                            break;
                    }
                }
            });
            return view;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getViewTypeCount() {
            return 2;
        }
    }

    public class MuezzinAdapter extends BaseAdapter {
        private List<AdhanSoundsData.AdhanSound> items;
        private String selectedId;

        public MuezzinAdapter(List<AdhanSoundsData.AdhanSound> list, String str) {
            this.items = list;
            this.selectedId = str;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getView$0() {
            notifyDataSetChanged();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getView$1(MediaPlayer mediaPlayer) {
            MuezzinActivity.this.stopMediaPlayer();
            MuezzinActivity.this.currentlyPlayingId = "";
            MuezzinActivity.this.runOnUiThread(new a1(this, 1));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getView$2() {
            Toast.makeText(MuezzinActivity.this, "خطأ في تشغيل الأذان", 0).show();
            notifyDataSetChanged();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$getView$3(MediaPlayer mediaPlayer, int i2, int i3) {
            MuezzinActivity.this.stopMediaPlayer();
            MuezzinActivity.this.currentlyPlayingId = "";
            MuezzinActivity.this.runOnUiThread(new a1(this, 0));
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getView$5(AdhanSoundsData.AdhanSound adhanSound, boolean z2, boolean z3, View view) {
            if (MuezzinActivity.this.currentlyPlayingId.equals(adhanSound.id) && MuezzinActivity.this.mediaPlayer != null && MuezzinActivity.this.mediaPlayer.isPlaying()) {
                MuezzinActivity.this.stopMediaPlayer();
                MuezzinActivity.this.currentlyPlayingId = "";
                notifyDataSetChanged();
                return;
            }
            MuezzinActivity.this.stopMediaPlayer();
            MuezzinActivity.this.currentlyPlayingId = adhanSound.id;
            notifyDataSetChanged();
            try {
                MuezzinActivity.this.mediaPlayer = new MediaPlayer();
                if (z2) {
                    MuezzinActivity.this.mediaPlayer.setDataSource(new File(MuezzinActivity.this.getFilesDir(), "adhan_sounds/" + adhanSound.id + ".mp3").getAbsolutePath());
                } else if (z3) {
                    AssetFileDescriptor openFd = MuezzinActivity.this.getAssets().openFd("adhan_sounds/" + adhanSound.id + ".mp3");
                    MuezzinActivity.this.mediaPlayer.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
                    openFd.close();
                } else {
                    MuezzinActivity.this.mediaPlayer.setDataSource(adhanSound.getUrl());
                }
                MuezzinActivity.this.mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setUsage(1).setContentType(2).build());
                MuezzinActivity.this.mediaPlayer.setOnCompletionListener(new v0(1, this));
                MuezzinActivity.this.mediaPlayer.setOnErrorListener(new w0(1, this));
                MuezzinActivity.this.mediaPlayer.prepareAsync();
                MuezzinActivity.this.mediaPlayer.setOnPreparedListener(new x0(1));
            } catch (Exception e2) {
                e2.printStackTrace();
                MuezzinActivity.this.currentlyPlayingId = "";
                notifyDataSetChanged();
                Toast.makeText(MuezzinActivity.this, "خطأ في تشغيل الأذان", 0).show();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getView$6(boolean z2, AdhanSoundsData.AdhanSound adhanSound, ImageView imageView, ProgressBar progressBar, View view) {
            if (z2) {
                Toast.makeText(MuezzinActivity.this, "محمّل بالفعل", 0).show();
            } else {
                MuezzinActivity.this.downloadAdhan(adhanSound, imageView, progressBar);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getView$7(AdhanSoundsData.AdhanSound adhanSound, View view) {
            this.selectedId = adhanSound.id;
            SharedPreferences.Editor edit = MuezzinActivity.this.prefs.edit();
            edit.putString(MuezzinActivity.this.prayerKey + "_adhan_sound_id", adhanSound.id);
            edit.apply();
            try {
                PrayerReminderScheduler.scheduleAdhanAlarms(MuezzinActivity.this);
            } catch (Exception unused) {
            }
            Toast.makeText(MuezzinActivity.this, "تم تفعيل: " + adhanSound.name, 0).show();
            notifyDataSetChanged();
            MuezzinActivity.this.setResult(-1);
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.items.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i2) {
            return this.items.get(i2);
        }

        @Override // android.widget.Adapter
        public long getItemId(int i2) {
            return i2;
        }

        @Override // android.widget.Adapter
        public View getView(int i2, View view, ViewGroup viewGroup) {
            if (view == null || (view.getTag() != null && view.getTag().equals("add_btn"))) {
                view = LayoutInflater.from(MuezzinActivity.this).inflate(R.layout.item_muezzin, viewGroup, false);
            }
            final AdhanSoundsData.AdhanSound adhanSound = this.items.get(i2);
            TextView textView = (TextView) view.findViewById(R.id.tvMuezzinName);
            TextView textView2 = (TextView) view.findViewById(R.id.tvMuezzinType);
            TextView textView3 = (TextView) view.findViewById(R.id.tvMuezzinCountry);
            TextView textView4 = (TextView) view.findViewById(R.id.tvMuezzinStatus);
            ImageView imageView = (ImageView) view.findViewById(R.id.btnPlay);
            final ImageView imageView2 = (ImageView) view.findViewById(R.id.btnDownload);
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressDownload);
            ImageView imageView3 = (ImageView) view.findViewById(R.id.btnActivate);
            textView.setText(adhanSound.name);
            if ("none".equals(adhanSound.id)) {
                textView2.setVisibility(8);
                textView3.setVisibility(8);
                imageView.setVisibility(8);
                imageView2.setVisibility(8);
                progressBar.setVisibility(8);
            } else {
                imageView.setVisibility(0);
                imageView2.setVisibility(0);
                if (MuezzinActivity.FILTER_FAJR.equals(adhanSound.type)) {
                    textView2.setText("فجر");
                    textView2.setVisibility(0);
                } else {
                    textView2.setVisibility(8);
                }
                String str = adhanSound.country;
                if (str == null || str.isEmpty()) {
                    textView3.setVisibility(8);
                } else {
                    textView3.setText(adhanSound.country);
                    textView3.setVisibility(0);
                }
            }
            final boolean isFileDownloaded = MuezzinActivity.this.isFileDownloaded(adhanSound.id);
            final boolean isFileBundled = MuezzinActivity.this.isFileBundled(adhanSound.id);
            boolean z2 = isFileDownloaded || isFileBundled;
            boolean equals = adhanSound.id.equals(this.selectedId);
            if (equals) {
                textView4.setText("مفعّل");
                textView4.setTextColor(-666250);
                textView4.setVisibility(0);
                view.setBackgroundColor(858398474);
            } else if (z2) {
                textView4.setText("محمّل");
                textView4.setTextColor(-3888819);
                textView4.setVisibility(0);
                view.setBackgroundColor(-15068152);
            } else {
                textView4.setVisibility(8);
                view.setBackgroundColor(-15068152);
            }
            if (!"none".equals(adhanSound.id)) {
                if (MuezzinActivity.this.currentlyPlayingId.equals(adhanSound.id) && MuezzinActivity.this.mediaPlayer != null && MuezzinActivity.this.mediaPlayer.isPlaying()) {
                    imageView.setImageResource(android.R.drawable.ic_media_pause);
                    imageView.setContentDescription("إيقاف " + adhanSound.name);
                } else {
                    imageView.setImageResource(android.R.drawable.ic_media_play);
                    imageView.setContentDescription("تشغيل " + adhanSound.name);
                }
                imageView.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.b1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        MuezzinActivity.MuezzinAdapter.this.lambda$getView$5(adhanSound, isFileDownloaded, isFileBundled, view2);
                    }
                });
                if (z2) {
                    imageView2.setImageResource(android.R.drawable.ic_menu_save);
                    imageView2.setAlpha(0.3f);
                    imageView2.setContentDescription("محمّل بالفعل - " + adhanSound.name);
                } else {
                    imageView2.setImageResource(android.R.drawable.stat_sys_download);
                    imageView2.setAlpha(1.0f);
                    imageView2.setContentDescription("تنزيل " + adhanSound.name);
                }
                final boolean z3 = z2;
                imageView2.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.c1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        MuezzinActivity.MuezzinAdapter.this.lambda$getView$6(z3, adhanSound, imageView2, progressBar, view2);
                    }
                });
            }
            if (equals) {
                imageView3.setImageResource(android.R.drawable.checkbox_on_background);
                imageView3.setContentDescription("مفعّل - " + adhanSound.name);
            } else {
                imageView3.setImageResource(android.R.drawable.ic_input_add);
                imageView3.setContentDescription("تفعيل " + adhanSound.name);
            }
            imageView3.setOnClickListener(new j0(1, this, adhanSound));
            return view;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downloadAdhan(AdhanSoundsData.AdhanSound adhanSound, ImageView imageView, ProgressBar progressBar) {
        imageView.animate().scaleX(0.7f).scaleY(0.7f).setDuration(200L).start();
        progressBar.setVisibility(0);
        progressBar.setProgress(0);
        new Thread(new s0(this, adhanSound, progressBar, imageView)).start();
    }

    /*  JADX ERROR: NullPointerException in pass: LoopRegionVisitor
        java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.SSAVar.use(jadx.core.dex.instructions.args.RegisterArg)" because "ssaVar" is null
        	at jadx.core.dex.nodes.InsnNode.rebindArgs(InsnNode.java:493)
        	at jadx.core.dex.nodes.InsnNode.rebindArgs(InsnNode.java:496)
        */
    private java.util.List<com.salatak.app.MuezzinActivity.DownloadedItem> getDownloadedAdhans() {
        /*
            Method dump skipped, instructions count: 265
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.salatak.app.MuezzinActivity.getDownloadedAdhans():java.util.List");
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String getFileDisplayName(android.net.Uri r8) {
        /*
            r7 = this;
            java.lang.String r0 = "مؤذن مخصص"
            android.content.ContentResolver r1 = r7.getContentResolver()     // Catch: java.lang.Exception -> L3b
            r5 = 0
            r6 = 0
            r3 = 0
            r4 = 0
            r2 = r8
            android.database.Cursor r8 = r1.query(r2, r3, r4, r5, r6)     // Catch: java.lang.Exception -> L3b
            if (r8 == 0) goto L43
            java.lang.String r1 = "_display_name"
            int r1 = r8.getColumnIndex(r1)     // Catch: java.lang.Exception -> L3b
            boolean r2 = r8.moveToFirst()     // Catch: java.lang.Exception -> L3b
            if (r2 == 0) goto L3e
            if (r1 < 0) goto L3e
            java.lang.String r1 = r8.getString(r1)     // Catch: java.lang.Exception -> L3b
            if (r1 == 0) goto L3f
            java.lang.String r2 = "."
            boolean r2 = r1.contains(r2)     // Catch: java.lang.Exception -> L39
            if (r2 == 0) goto L3f
            r2 = 46
            int r2 = r1.lastIndexOf(r2)     // Catch: java.lang.Exception -> L39
            r3 = 0
            java.lang.String r1 = r1.substring(r3, r2)     // Catch: java.lang.Exception -> L39
            goto L3f
        L39:
            r8 = move-exception
            goto L45
        L3b:
            r8 = move-exception
            r1 = r0
            goto L45
        L3e:
            r1 = r0
        L3f:
            r8.close()     // Catch: java.lang.Exception -> L39
            goto L48
        L43:
            r1 = r0
            goto L48
        L45:
            r8.printStackTrace()
        L48:
            if (r1 == 0) goto L4b
            r0 = r1
        L4b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.salatak.app.MuezzinActivity.getFileDisplayName(android.net.Uri):java.lang.String");
    }

    private void handleCustomAdhanSelected(Uri uri) {
        try {
            String str = ContentMetadata.KEY_CUSTOM_PREFIX + System.currentTimeMillis();
            File file = new File(getFilesDir(), "adhan_sounds");
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(file, str);
            InputStream openInputStream = getContentResolver().openInputStream(uri);
            if (openInputStream == null) {
                Toast.makeText(this, "فشل قراءة الملف", 0).show();
                return;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            byte[] bArr = new byte[4096];
            while (true) {
                int read = openInputStream.read(bArr);
                if (read == -1) {
                    break;
                } else {
                    fileOutputStream.write(bArr, 0, read);
                }
            }
            fileOutputStream.close();
            openInputStream.close();
            String fileDisplayName = getFileDisplayName(uri);
            SharedPreferences.Editor edit = this.prefs.edit();
            edit.putString(this.prayerKey + "_adhan_sound_id", str);
            edit.putString("custom_adhan_name_" + str, fileDisplayName);
            edit.apply();
            try {
                PrayerReminderScheduler.scheduleAdhanAlarms(this);
            } catch (Exception unused) {
            }
            Toast.makeText(this, "تم إضافة وتفعيل: " + fileDisplayName, 0).show();
            setResult(-1);
            refreshList();
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "فشل استيراد الملف الصوتي", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isFileBundled(String str) {
        if ("none".equals(str)) {
            return false;
        }
        try {
            getAssets().openFd("adhan_sounds/" + str + ".mp3").close();
            return true;
        } catch (IOException unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isFileDownloaded(String str) {
        if ("none".equals(str)) {
            return false;
        }
        return new File(getFilesDir(), android.support.v4.media.l.h("adhan_sounds/", str, ".mp3")).exists();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$downloadAdhan$4(ProgressBar progressBar, ImageView imageView, int i2) {
        progressBar.setVisibility(8);
        imageView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200L).start();
        Toast.makeText(this, "الرابط لا يعمل - رمز الخطأ: " + i2, 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$downloadAdhan$6(ProgressBar progressBar, ImageView imageView, AdhanSoundsData.AdhanSound adhanSound) {
        progressBar.setVisibility(8);
        imageView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200L).start();
        imageView.setAlpha(0.3f);
        Toast.makeText(this, "تم تنزيل: " + adhanSound.name, 0).show();
        MuezzinAdapter muezzinAdapter = this.adapter;
        if (muezzinAdapter != null) {
            muezzinAdapter.notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$downloadAdhan$7(ProgressBar progressBar, ImageView imageView) {
        progressBar.setVisibility(8);
        imageView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200L).start();
        Toast.makeText(this, "فشل التنزيل", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$downloadAdhan$8(AdhanSoundsData.AdhanSound adhanSound, final ProgressBar progressBar, final ImageView imageView) {
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        File file;
        final int responseCode;
        FileOutputStream fileOutputStream;
        FileOutputStream fileOutputStream2 = null;
        try {
            File file2 = new File(getFilesDir(), "adhan_sounds");
            if (!file2.exists()) {
                file2.mkdirs();
            }
            file = new File(file2, adhanSound.id + ".mp3");
            httpURLConnection = (HttpURLConnection) new URL(adhanSound.getUrl()).openConnection();
            try {
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.connect();
                responseCode = httpURLConnection.getResponseCode();
            } catch (Exception e2) {
                e = e2;
                inputStream = null;
            } catch (Throwable th) {
                th = th;
                inputStream = null;
            }
        } catch (Exception e3) {
            e = e3;
            httpURLConnection = null;
            inputStream = null;
        } catch (Throwable th2) {
            th = th2;
            httpURLConnection = null;
            inputStream = null;
        }
        if (responseCode != 200) {
            runOnUiThread(new Runnable() { // from class: com.salatak.app.t0
                @Override // java.lang.Runnable
                public final void run() {
                    MuezzinActivity.this.lambda$downloadAdhan$4(progressBar, imageView, responseCode);
                }
            });
            httpURLConnection.disconnect();
            return;
        }
        int contentLength = httpURLConnection.getContentLength();
        inputStream = httpURLConnection.getInputStream();
        try {
            try {
                fileOutputStream = new FileOutputStream(file);
            } catch (Exception e4) {
                e = e4;
            }
        } catch (Throwable th3) {
            th = th3;
        }
        try {
            byte[] bArr = new byte[4096];
            long j2 = 0;
            while (true) {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
                j2 += read;
                if (contentLength > 0) {
                    runOnUiThread(new androidx.core.content.res.b((int) ((100 * j2) / contentLength), 2, progressBar));
                }
            }
            runOnUiThread(new s0(this, progressBar, imageView, adhanSound));
            try {
                fileOutputStream.close();
            } catch (Exception unused) {
            }
            try {
                inputStream.close();
            } catch (Exception unused2) {
            }
        } catch (Exception e5) {
            e = e5;
            fileOutputStream2 = fileOutputStream;
            e.printStackTrace();
            runOnUiThread(new b0(this, progressBar, imageView, 2));
            if (fileOutputStream2 != null) {
                try {
                    fileOutputStream2.close();
                } catch (Exception unused3) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception unused4) {
                }
            }
            if (httpURLConnection == null) {
                return;
            }
            httpURLConnection.disconnect();
        } catch (Throwable th4) {
            th = th4;
            fileOutputStream2 = fileOutputStream;
            if (fileOutputStream2 != null) {
                try {
                    fileOutputStream2.close();
                } catch (Exception unused5) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception unused6) {
                }
            }
            if (httpURLConnection == null) {
                throw th;
            }
            httpURLConnection.disconnect();
            throw th;
        }
        httpURLConnection.disconnect();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(Uri uri) {
        if (uri != null) {
            handleCustomAdhanSelected(uri);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$2(View view) {
        showFilterDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showFilterDialog$3(DialogInterface dialogInterface, int i2) {
        if (i2 == 0) {
            this.currentFilter = "all";
            updateFilterLabel("الكل");
        } else if (i2 == 1) {
            this.currentFilter = FILTER_FAJR;
            updateFilterLabel("أذان الفجر");
        } else if (i2 == 2) {
            this.currentFilter = FILTER_GENERAL;
            updateFilterLabel("أذان عام");
        } else if (i2 == 3) {
            this.currentFilter = FILTER_DOWNLOADS;
            updateFilterLabel("التنزيلات");
        }
        refreshList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openFilePicker() {
        try {
            this.customAdhanPicker.launch(new String[]{"audio/*"});
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "فشل فتح مستعرض الملفات", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshList() {
        String string = this.prefs.getString(this.prayerKey + "_adhan_sound_id", "");
        if (!FILTER_DOWNLOADS.equals(this.currentFilter)) {
            String str = this.currentFilter;
            String str2 = FILTER_FAJR;
            if (!FILTER_FAJR.equals(str)) {
                String str3 = this.currentFilter;
                str2 = FILTER_GENERAL;
                if (!FILTER_GENERAL.equals(str3)) {
                    str2 = "";
                }
            }
            List<AdhanSoundsData.AdhanSound> filter = AdhanSoundsData.filter(this.currentSearch, str2, "");
            MuezzinAdapter muezzinAdapter = new MuezzinAdapter(filter, string);
            this.adapter = muezzinAdapter;
            this.lvMuezzins.setAdapter((ListAdapter) muezzinAdapter);
            this.tvCount.setText(filter.size() + " مؤذن");
            return;
        }
        List<DownloadedItem> downloadedAdhans = getDownloadedAdhans();
        String str4 = this.currentSearch;
        if (str4 != null && !str4.isEmpty()) {
            ArrayList arrayList = new ArrayList();
            for (DownloadedItem downloadedItem : downloadedAdhans) {
                if (downloadedItem.displayName.contains(this.currentSearch)) {
                    arrayList.add(downloadedItem);
                }
            }
            downloadedAdhans = arrayList;
        }
        this.lvMuezzins.setAdapter((ListAdapter) new DownloadsAdapter(downloadedAdhans, string));
        this.tvCount.setText(downloadedAdhans.size() + " تنزيل");
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("فرز حسب");
        builder.setItems(new String[]{"الكل", "أذان الفجر", "أذان عام", "التنزيلات"}, new q(2, this));
        builder.setNegativeButton("إلغاء", (DialogInterface.OnClickListener) null);
        AlertDialog create = builder.create();
        create.show();
        styleDialogGold(create);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopMediaPlayer() {
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
    }

    private void styleDialogGold(AlertDialog alertDialog) {
        TextView textView;
        if (alertDialog == null || alertDialog.getWindow() == null) {
            return;
        }
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(-15068152));
        try {
            int identifier = getResources().getIdentifier("alertTitle", TtmlNode.ATTR_ID, "android");
            if (identifier > 0 && (textView = (TextView) alertDialog.findViewById(identifier)) != null) {
                textView.setTextColor(-666250);
            }
        } catch (Exception unused) {
        }
        try {
            Button button = alertDialog.getButton(-1);
            Button button2 = alertDialog.getButton(-2);
            if (button != null) {
                button.setTextColor(-666250);
            }
            if (button2 != null) {
                button2.setTextColor(-3888819);
            }
        } catch (Exception unused2) {
        }
        try {
            ListView listView = alertDialog.getListView();
            if (listView != null) {
                listView.setBackgroundColor(-15068152);
                listView.setDivider(new ColorDrawable(-12767728));
                listView.setDividerHeight(1);
                listView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() { // from class: com.salatak.app.MuezzinActivity.2
                    @Override // android.view.ViewGroup.OnHierarchyChangeListener
                    public void onChildViewAdded(View view, View view2) {
                        if (view2 instanceof TextView) {
                            ((TextView) view2).setTextColor(-666250);
                        }
                    }

                    @Override // android.view.ViewGroup.OnHierarchyChangeListener
                    public void onChildViewRemoved(View view, View view2) {
                    }
                });
            }
        } catch (Exception unused3) {
        }
    }

    private void updateFilterLabel(String str) {
        if (this.tvFilterLabel != null) {
            if (str.equals("الكل")) {
                this.tvFilterLabel.setVisibility(8);
            } else {
                this.tvFilterLabel.setText("الفرز: ".concat(str));
                this.tvFilterLabel.setVisibility(0);
            }
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_muezzin);
        getWindow().setStatusBarColor(-15068152);
        getWindow().setNavigationBarColor(-15068152);
        this.prayerKey = getIntent().getStringExtra("prayer_key");
        String stringExtra = getIntent().getStringExtra("prayer_name");
        this.prayerName = stringExtra;
        if (this.prayerKey == null) {
            this.prayerKey = "";
        }
        if (stringExtra == null) {
            this.prayerName = "";
        }
        this.prefs = getSharedPreferences("SalatakPrefs", 0);
        this.customAdhanPicker = registerForActivityResult(new ActivityResultContracts.OpenDocument(), new f2(this));
        ImageView imageView = (ImageView) findViewById(R.id.btnBack);
        TextView textView = (TextView) findViewById(R.id.tvTitle);
        this.etSearch = (EditText) findViewById(R.id.etSearch);
        ImageView imageView2 = (ImageView) findViewById(R.id.btnFilter);
        this.tvCount = (TextView) findViewById(R.id.tvCount);
        this.tvFilterLabel = (TextView) findViewById(R.id.tvFilterLabel);
        this.lvMuezzins = (ListView) findViewById(R.id.lvMuezzins);
        if (!this.prayerName.isEmpty()) {
            textView.setText("تحميل المؤذن - " + this.prayerName);
        }
        final int i2 = 0;
        imageView.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.r0
            public final /* synthetic */ MuezzinActivity b;

            {
                this.b = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                switch (i2) {
                    case 0:
                        this.b.lambda$onCreate$1(view);
                        break;
                    default:
                        this.b.lambda$onCreate$2(view);
                        break;
                }
            }
        });
        imageView.setContentDescription("رجوع");
        refreshList();
        this.etSearch.addTextChangedListener(new TextWatcher() { // from class: com.salatak.app.MuezzinActivity.1
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                MuezzinActivity.this.currentSearch = editable.toString().trim();
                MuezzinActivity.this.refreshList();
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i3, int i4, int i5) {
            }
        });
        final int i3 = 1;
        imageView2.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.r0
            public final /* synthetic */ MuezzinActivity b;

            {
                this.b = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                switch (i3) {
                    case 0:
                        this.b.lambda$onCreate$1(view);
                        break;
                    default:
                        this.b.lambda$onCreate$2(view);
                        break;
                }
            }
        });
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        stopMediaPlayer();
    }
}
