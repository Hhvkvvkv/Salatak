package com.salatak.app;

import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.PlaybackException;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.google.android.material.card.MaterialCardViewHelper;

/* loaded from: classes2.dex */
public class AzkarDisplayActivity extends AppCompatActivity {
    private static final long AUTO_DISMISS_DELAY = 30000;
    private static final String TAG = "AzkarDisplayActivity";
    private Handler autoCloseHandler;
    private Runnable autoCloseRunnable;
    private ImageView btnClose;
    private LinearLayout comboboxCard;
    private FrameLayout comboboxRoot;
    private boolean isClosing = false;
    private TextView tvAzkarText;

    private void animateEntrance(String str) {
        try {
            this.comboboxCard.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(500L).setInterpolator(new DecelerateInterpolator(1.5f)).withStartAction(new y(this, 1)).withEndAction(new a0(this, str, 1)).start();
            animateFlowers();
        } catch (Exception e2) {
            Log.e(TAG, "Error in animateEntrance: " + e2.getMessage(), e2);
            this.comboboxCard.setAlpha(1.0f);
            announceForScreenReader(str);
        }
    }

    private void animateFlowers() {
        try {
            int[] iArr = {R.id.flowerTopRight, R.id.flowerTopLeft, R.id.flowerBottomRight, R.id.flowerBottomLeft, R.id.flowerCenter1, R.id.flowerCenter2};
            for (int i2 = 0; i2 < 6; i2++) {
                ImageView imageView = (ImageView) findViewById(iArr[i2]);
                if (imageView != null) {
                    imageView.postDelayed(new z(this, imageView, 1), (i2 * ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION) + MaterialCardViewHelper.DEFAULT_FADE_ANIM_DURATION);
                }
            }
            int[] iArr2 = {R.id.flowerInnerLeft, R.id.flowerInnerRight};
            for (int i3 = 0; i3 < 2; i3++) {
                ImageView imageView2 = (ImageView) findViewById(iArr2[i3]);
                if (imageView2 != null) {
                    imageView2.postDelayed(new z(this, imageView2, 2), 800L);
                }
            }
        } catch (Exception e2) {
            Log.e(TAG, "Error in animateFlowers: " + e2.getMessage(), e2);
        }
    }

    private void announceForScreenReader(String str) {
        try {
            AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService("accessibility");
            if (accessibilityManager != null && accessibilityManager.isEnabled()) {
                this.tvAzkarText.postDelayed(new a0(this, "ذكر من أذكار المسلم: " + str, 0), 500L);
            }
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error checking accessibility: "), TAG);
        }
    }

    private void cancelNotification() {
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
            if (notificationManager != null) {
                notificationManager.cancel(PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED);
            }
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error canceling notification: "), TAG);
        }
    }

    private void closeWithAnimation() {
        Runnable runnable;
        if (this.isClosing) {
            return;
        }
        this.isClosing = true;
        try {
            Handler handler = this.autoCloseHandler;
            if (handler != null && (runnable = this.autoCloseRunnable) != null) {
                handler.removeCallbacks(runnable);
            }
            int[] iArr = {R.id.flowerTopRight, R.id.flowerTopLeft, R.id.flowerBottomRight, R.id.flowerBottomLeft, R.id.flowerCenter1, R.id.flowerCenter2};
            for (int i2 = 0; i2 < 6; i2++) {
                ImageView imageView = (ImageView) findViewById(iArr[i2]);
                if (imageView != null) {
                    imageView.clearAnimation();
                    imageView.animate().alpha(0.0f).setDuration(250L).start();
                }
            }
            this.comboboxCard.animate().alpha(0.0f).scaleX(0.8f).scaleY(0.8f).setDuration(350L).setInterpolator(new DecelerateInterpolator()).withEndAction(new y(this, 0)).start();
        } catch (Exception e2) {
            Log.e(TAG, "Error in closeWithAnimation: " + e2.getMessage(), e2);
            finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateEntrance$3() {
        this.comboboxCard.setScaleX(0.85f);
        this.comboboxCard.setScaleY(0.85f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateEntrance$4(String str) {
        this.comboboxCard.setAlpha(1.0f);
        announceForScreenReader(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateFlowers$5(ImageView imageView) {
        try {
            if (this.isClosing || isFinishing()) {
                return;
            }
            imageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.flower_float));
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error starting float: "), TAG);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateFlowers$6(ImageView imageView) {
        try {
            if (!this.isClosing && !isFinishing()) {
                imageView.setScaleX(0.3f);
                imageView.setScaleY(0.3f);
                imageView.setRotation(-30.0f);
                imageView.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).rotation(0.0f).setDuration(800L).setInterpolator(new DecelerateInterpolator()).withEndAction(new z(this, imageView, 0)).start();
            }
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error animating flower: "), TAG);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateFlowers$7(ImageView imageView) {
        try {
            if (this.isClosing || isFinishing()) {
                return;
            }
            imageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.flower_float));
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error starting inner float: "), TAG);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$announceForScreenReader$8(String str) {
        try {
            if (isFinishing()) {
                return;
            }
            this.tvAzkarText.setFocusable(true);
            this.tvAzkarText.setFocusableInTouchMode(true);
            this.tvAzkarText.setAccessibilityLiveRegion(2);
            this.tvAzkarText.requestFocus();
            this.tvAzkarText.sendAccessibilityEvent(8);
            this.tvAzkarText.announceForAccessibility(str);
            Log.d(TAG, "Screen reader announcement sent");
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error in accessibility: "), TAG);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$closeWithAnimation$9() {
        FrameLayout frameLayout = this.comboboxRoot;
        if (frameLayout != null) {
            frameLayout.animate().alpha(0.0f).setDuration(200L).withEndAction(new y(this, 3)).start();
        } else {
            finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        if (this.isClosing) {
            return;
        }
        closeWithAnimation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(String str) {
        this.comboboxRoot.animate().alpha(1.0f).setDuration(300L).start();
        animateEntrance(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$2() {
        if (this.isClosing) {
            return;
        }
        closeWithAnimation();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.isClosing) {
            return;
        }
        closeWithAnimation();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            if (Build.VERSION.SDK_INT >= 27) {
                setShowWhenLocked(true);
                setTurnScreenOn(true);
            } else {
                getWindow().addFlags(2621440);
            }
            getWindow().addFlags(128);
            setContentView(R.layout.overlay_azkar_combobox);
            this.comboboxRoot = (FrameLayout) findViewById(R.id.comboboxRoot);
            this.comboboxCard = (LinearLayout) findViewById(R.id.comboboxCard);
            this.tvAzkarText = (TextView) findViewById(R.id.tvComboboxAzkarText);
            this.btnClose = (ImageView) findViewById(R.id.btnCloseCombobox);
            String stringExtra = (getIntent() == null || !getIntent().hasExtra("azkar_text")) ? null : getIntent().getStringExtra("azkar_text");
            if (stringExtra == null || stringExtra.isEmpty()) {
                stringExtra = AzkarService.getRandomAzkar(this);
            }
            this.tvAzkarText.setText(stringExtra);
            this.tvAzkarText.setContentDescription(stringExtra);
            this.btnClose.setOnClickListener(new y0(2, this));
            this.comboboxCard.setAlpha(0.0f);
            this.comboboxRoot.setAlpha(0.0f);
            this.comboboxRoot.post(new a0(this, stringExtra, 2));
            Handler handler = new Handler(Looper.getMainLooper());
            this.autoCloseHandler = handler;
            y yVar = new y(this, 2);
            this.autoCloseRunnable = yVar;
            handler.postDelayed(yVar, 30000L);
            cancelNotification();
        } catch (Exception e2) {
            Log.e(TAG, "Error in onCreate: " + e2.getMessage(), e2);
            finish();
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        Runnable runnable;
        super.onDestroy();
        Handler handler = this.autoCloseHandler;
        if (handler == null || (runnable = this.autoCloseRunnable) == null) {
            return;
        }
        handler.removeCallbacks(runnable);
    }
}
