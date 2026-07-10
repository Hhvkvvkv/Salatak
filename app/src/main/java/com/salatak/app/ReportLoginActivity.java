package com.salatak.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.extractor.text.ttml.TtmlNode;
import java.util.concurrent.Executors;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class ReportLoginActivity extends AppCompatActivity {
    private Button btnRegister;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etUsername;
    private ProgressBar progressLogin;
    private TextView tvError;

    private void doRegister() {
        String trim = this.etUsername.getText().toString().trim();
        String trim2 = this.etEmail.getText().toString().trim();
        String trim3 = this.etPassword.getText().toString().trim();
        if (trim.isEmpty() || trim2.isEmpty() || trim3.isEmpty()) {
            this.tvError.setText("يرجى ملء جميع الحقول");
            this.tvError.setVisibility(0);
        } else {
            this.btnRegister.setEnabled(false);
            this.progressLogin.setVisibility(0);
            this.tvError.setVisibility(8);
            Executors.newSingleThreadExecutor().execute(new g2(this, trim, trim2, trim3, 0));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doRegister$2(String str, String str2, String str3) {
        getSharedPreferences("SalatakPrefs", 0).edit().putString("report_user_id", str).putString("report_username", str2).putString("report_email", str3).apply();
        Toast.makeText(this, "تم التسجيل بنجاح", 0).show();
        startActivity(new Intent(this, (Class<?>) ChatActivity.class));
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doRegister$3(Exception exc) {
        this.progressLogin.setVisibility(8);
        this.btnRegister.setEnabled(true);
        this.tvError.setText(exc.getMessage() != null ? exc.getMessage() : "خطأ في التسجيل");
        this.tvError.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doRegister$4(String str, String str2, String str3) {
        try {
            JSONObject registerUser = GitHubApi.registerUser(str, str2, str3);
            runOnUiThread(new g2(this, registerUser.getString(TtmlNode.ATTR_ID), registerUser.getString("username"), str2, 1));
        } catch (Exception e2) {
            runOnUiThread(new e0(8, this, e2));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        doRegister();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.setStatusBarColor(-15918294);
        window.setNavigationBarColor(-15918294);
        if (!getSharedPreferences("SalatakPrefs", 0).getString("report_user_id", "").isEmpty()) {
            startActivity(new Intent(this, (Class<?>) ChatActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_report_login);
        this.etUsername = (EditText) findViewById(R.id.etUsername);
        this.etEmail = (EditText) findViewById(R.id.etEmail);
        this.etPassword = (EditText) findViewById(R.id.etPassword);
        this.btnRegister = (Button) findViewById(R.id.btnRegister);
        this.progressLogin = (ProgressBar) findViewById(R.id.progressLogin);
        this.tvError = (TextView) findViewById(R.id.tvError);
        final int i2 = 0;
        ((ImageView) findViewById(R.id.btnBack)).setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.h2
            public final /* synthetic */ ReportLoginActivity b;

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
        this.btnRegister.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.h2
            public final /* synthetic */ ReportLoginActivity b;

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
    }
}
