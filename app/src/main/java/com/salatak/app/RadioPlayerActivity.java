package com.salatak.app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.salatak.app.RadioService;

/* loaded from: classes2.dex */
public class RadioPlayerActivity extends AppCompatActivity {
    private ImageView btnPlayPause;
    private ProgressBar progressPlayer;
    private RadioService radioService;
    private boolean serviceBound = false;
    private final ServiceConnection serviceConnection = new AnonymousClass1();
    private String stationTitle;
    private String streamUrl;
    private TextView tvPlayerStatus;
    private TextView tvStationName;

    /* renamed from: com.salatak.app.RadioPlayerActivity$1, reason: invalid class name */
    public class AnonymousClass1 implements ServiceConnection {
        public AnonymousClass1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceConnected$0(int i2, boolean z2, boolean z3) {
            RadioPlayerActivity.this.updateUI(i2, z2, z3);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceConnected$1(int i2, boolean z2, boolean z3) {
            RadioPlayerActivity.this.runOnUiThread(new z1(this, i2, z2, z3, 1));
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            RadioPlayerActivity.this.radioService = ((RadioService.RadioBinder) iBinder).getService();
            RadioPlayerActivity.this.serviceBound = true;
            RadioPlayerActivity.this.radioService.setCallback(new f2(this));
            String currentUrl = RadioPlayerActivity.this.radioService.getCurrentUrl();
            if (currentUrl == null || !currentUrl.equals(RadioPlayerActivity.this.streamUrl)) {
                RadioPlayerActivity.this.radioService.playStream(RadioPlayerActivity.this.streamUrl, RadioPlayerActivity.this.stationTitle);
            } else {
                RadioPlayerActivity radioPlayerActivity = RadioPlayerActivity.this;
                radioPlayerActivity.updateUI(radioPlayerActivity.radioService.getPlayerState(), RadioPlayerActivity.this.radioService.isPlaying(), RadioPlayerActivity.this.radioService.hasError());
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            RadioPlayerActivity.this.serviceBound = false;
            RadioPlayerActivity.this.radioService = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        RadioService radioService;
        if (!this.serviceBound || (radioService = this.radioService) == null) {
            return;
        }
        radioService.togglePlayPause();
    }

    private void startAndBindService() {
        Intent intent = new Intent(this, (Class<?>) RadioService.class);
        intent.putExtra(RadioService.EXTRA_STREAM_URL, this.streamUrl);
        intent.putExtra(RadioService.EXTRA_STATION_TITLE, this.stationTitle);
        startService(intent);
        bindService(intent, this.serviceConnection, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUI(int i2, boolean z2, boolean z3) {
        if (isFinishing()) {
            return;
        }
        if (z3) {
            RadioService radioService = this.radioService;
            if (radioService == null || !radioService.isRetrying()) {
                this.tvPlayerStatus.setText("اضغط لإعادة المحاولة");
                this.tvPlayerStatus.setTextColor(-38037);
                this.progressPlayer.setVisibility(8);
                this.btnPlayPause.setImageResource(R.drawable.ic_play_circle);
                return;
            }
            this.tvPlayerStatus.setText("جاري إعادة الاتصال...");
            this.tvPlayerStatus.setTextColor(-2052805);
            this.progressPlayer.setVisibility(0);
            this.btnPlayPause.setImageResource(R.drawable.ic_pause_circle);
            return;
        }
        if (i2 == 1) {
            this.tvPlayerStatus.setText("جاري الاتصال...");
            this.tvPlayerStatus.setTextColor(-7824982);
            this.progressPlayer.setVisibility(0);
            this.btnPlayPause.setImageResource(R.drawable.ic_pause_circle);
            return;
        }
        if (i2 == 2) {
            this.tvPlayerStatus.setText("جاري التحميل...");
            this.tvPlayerStatus.setTextColor(-2052805);
            this.progressPlayer.setVisibility(0);
            this.btnPlayPause.setImageResource(R.drawable.ic_pause_circle);
            return;
        }
        if (i2 != 3) {
            if (i2 != 4) {
                return;
            }
            this.tvPlayerStatus.setText("انتهى البث");
            this.tvPlayerStatus.setTextColor(-7824982);
            this.progressPlayer.setVisibility(8);
            this.btnPlayPause.setImageResource(R.drawable.ic_play_circle);
            return;
        }
        if (z2) {
            this.tvPlayerStatus.setText("يعمل الآن");
            this.tvPlayerStatus.setTextColor(-11751600);
            this.progressPlayer.setVisibility(8);
            this.btnPlayPause.setImageResource(R.drawable.ic_pause_circle);
            return;
        }
        this.tvPlayerStatus.setText("متوقف مؤقتاً");
        this.tvPlayerStatus.setTextColor(-2052805);
        this.progressPlayer.setVisibility(8);
        this.btnPlayPause.setImageResource(R.drawable.ic_play_circle);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            Window window = getWindow();
            window.setStatusBarColor(-15918294);
            window.setNavigationBarColor(-15655637);
            setContentView(R.layout.activity_radio_player);
            this.streamUrl = getIntent().getStringExtra(RadioService.EXTRA_STREAM_URL);
            String stringExtra = getIntent().getStringExtra(RadioService.EXTRA_STATION_TITLE);
            this.stationTitle = stringExtra;
            if (this.streamUrl != null && stringExtra != null) {
                this.tvStationName = (TextView) findViewById(R.id.tvStationName);
                this.tvPlayerStatus = (TextView) findViewById(R.id.tvPlayerStatus);
                this.progressPlayer = (ProgressBar) findViewById(R.id.progressPlayer);
                this.btnPlayPause = (ImageView) findViewById(R.id.btnPlayPause);
                ImageView imageView = (ImageView) findViewById(R.id.btnBack);
                TextView textView = (TextView) findViewById(R.id.tvToolbarTitle);
                this.tvStationName.setText(this.stationTitle);
                textView.setText(this.stationTitle);
                this.tvPlayerStatus.setText("جاري الاتصال...");
                this.progressPlayer.setVisibility(0);
                this.btnPlayPause.setImageResource(R.drawable.ic_pause_circle);
                final int i2 = 0;
                imageView.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.e2
                    public final /* synthetic */ RadioPlayerActivity b;

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
                this.btnPlayPause.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.e2
                    public final /* synthetic */ RadioPlayerActivity b;

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
                startAndBindService();
                return;
            }
            Toast.makeText(this, "خطأ في بيانات الإذاعة", 0).show();
            finish();
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في فتح المشغل", 0).show();
            finish();
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        if (this.serviceBound) {
            RadioService radioService = this.radioService;
            if (radioService != null) {
                radioService.setCallback(null);
            }
            unbindService(this.serviceConnection);
            this.serviceBound = false;
        }
        super.onDestroy();
    }
}
