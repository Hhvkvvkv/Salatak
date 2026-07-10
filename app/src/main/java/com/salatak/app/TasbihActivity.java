package com.salatak.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

/* loaded from: classes2.dex */
public class TasbihActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "TasbihPrefs";
    private CardView btnTap;
    private SharedPreferences prefs;
    private TextView tvCount;
    private TextView tvDhikr;
    private TextView tvTarget;
    private TextView tvTotal;
    private Vibrator vibrator;
    private final String[] dhikrList = {"سُبحانَ الله", "الحمدُ لله", "اللهُ أكبر", "لا إله إلا الله", "سُبحانَ الله وبحمده"};
    private int currentDhikrIndex = 0;
    private int count = 0;
    private int totalCount = 0;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        this.count++;
        this.totalCount++;
        this.prefs.edit().putInt("count_" + this.currentDhikrIndex, this.count).putInt("total_count", this.totalCount).apply();
        updateDisplay();
        vibrate();
        int i2 = this.count;
        if (i2 == 33 || i2 == 100) {
            Toast.makeText(this, "أحسنت! " + this.count + " مرة", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        this.count = 0;
        this.prefs.edit().putInt("count_" + this.currentDhikrIndex, 0).apply();
        updateDisplay();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$2(View view) {
        this.count = 0;
        this.totalCount = 0;
        SharedPreferences.Editor edit = this.prefs.edit();
        for (int i2 = 0; i2 < this.dhikrList.length; i2++) {
            edit.putInt("count_" + i2, 0);
        }
        edit.putInt("total_count", 0).apply();
        updateDisplay();
        Toast.makeText(this, "تم إعادة ضبط جميع العدادات", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$3(int i2, View[] viewArr, View view) {
        saveCurrentCount();
        this.currentDhikrIndex = i2;
        this.count = this.prefs.getInt("count_" + i2, 0);
        this.prefs.edit().putInt("dhikr_index", i2).apply();
        updateDisplay();
        updateDhikrButtonStyles(viewArr);
    }

    private void saveCurrentCount() {
        this.prefs.edit().putInt("count_" + this.currentDhikrIndex, this.count).apply();
    }

    private void updateDhikrButtonStyles(View[] viewArr) {
        int i2 = 0;
        while (i2 < viewArr.length) {
            View view = viewArr[i2];
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(i2 == this.currentDhikrIndex ? -10496 : -7824982);
                viewArr[i2].setAlpha(i2 == this.currentDhikrIndex ? 1.0f : 0.6f);
            }
            i2++;
        }
    }

    private void updateDisplay() {
        this.tvDhikr.setText(this.dhikrList[this.currentDhikrIndex]);
        this.tvCount.setText(String.valueOf(this.count));
        this.tvTotal.setText("المجموع: " + this.totalCount);
        int i2 = this.count;
        int max = Math.max(0, 33 - ((i2 % 33 != 0 || i2 <= 0) ? i2 % 33 : 33));
        this.tvTarget.setText("المتبقي: " + max + " من 33");
    }

    private void vibrate() {
        VibrationEffect createOneShot;
        try {
            Vibrator vibrator = this.vibrator;
            if (vibrator == null || !vibrator.hasVibrator()) {
                return;
            }
            Vibrator vibrator2 = this.vibrator;
            createOneShot = VibrationEffect.createOneShot(30L, -1);
            vibrator2.vibrate(createOneShot);
        } catch (Exception unused) {
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_tasbih);
        this.prefs = getSharedPreferences(PREFS_NAME, 0);
        this.vibrator = (Vibrator) getSystemService("vibrator");
        this.tvDhikr = (TextView) findViewById(R.id.tvDhikr);
        this.tvCount = (TextView) findViewById(R.id.tvCount);
        this.tvTotal = (TextView) findViewById(R.id.tvTotal);
        this.tvTarget = (TextView) findViewById(R.id.tvTarget);
        this.btnTap = (CardView) findViewById(R.id.btnTap);
        this.currentDhikrIndex = this.prefs.getInt("dhikr_index", 0);
        this.count = this.prefs.getInt("count_" + this.currentDhikrIndex, 0);
        this.totalCount = this.prefs.getInt("total_count", 0);
        updateDisplay();
        final int i2 = 0;
        this.btnTap.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.u2
            public final /* synthetic */ TasbihActivity b;

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
                    default:
                        this.b.lambda$onCreate$2(view);
                        break;
                }
            }
        });
        final int i3 = 1;
        findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.u2
            public final /* synthetic */ TasbihActivity b;

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
                    default:
                        this.b.lambda$onCreate$2(view);
                        break;
                }
            }
        });
        final int i4 = 2;
        findViewById(R.id.btnResetAll).setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.u2
            public final /* synthetic */ TasbihActivity b;

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
                    default:
                        this.b.lambda$onCreate$2(view);
                        break;
                }
            }
        });
        final View[] viewArr = {findViewById(R.id.btnDhikr0), findViewById(R.id.btnDhikr1), findViewById(R.id.btnDhikr2), findViewById(R.id.btnDhikr3), findViewById(R.id.btnDhikr4)};
        for (final int i5 = 0; i5 < 5; i5++) {
            View view = viewArr[i5];
            if (view != null) {
                view.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.v2
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        TasbihActivity.this.lambda$onCreate$3(i5, viewArr, view2);
                    }
                });
            }
        }
        updateDhikrButtonStyles(viewArr);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        saveCurrentCount();
    }
}
