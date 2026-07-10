package com.salatak.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

/* loaded from: classes2.dex */
public class WelcomeActivity extends AppCompatActivity {
    private CardView btnNext;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            setContentView(R.layout.activity_welcome);
            CardView cardView = (CardView) findViewById(R.id.btnNext);
            this.btnNext = cardView;
            if (cardView != null) {
                cardView.setOnClickListener(new View.OnClickListener() { // from class: com.salatak.app.WelcomeActivity.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        try {
                            WelcomeActivity.this.startActivity(new Intent(WelcomeActivity.this, (Class<?>) LocationSelectionActivity.class));
                            WelcomeActivity.this.finish();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            Toast.makeText(WelcomeActivity.this, "خطأ في الانتقال للأذونات", 0).show();
                        }
                    }
                });
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "خطأ في تحميل الواجهة", 0).show();
        }
    }
}
