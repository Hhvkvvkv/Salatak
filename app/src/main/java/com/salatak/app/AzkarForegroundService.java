package com.salatak.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.app.NotificationCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.media3.common.PlaybackException;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.google.android.material.card.MaterialCardViewHelper;

/* loaded from: classes2.dex */
public class AzkarForegroundService extends Service {
    private static final long AUTO_DISMISS_DELAY = 30000;
    private static final String CHANNEL_ID = "azkar_service_channel";
    private static final int NOTIFICATION_ID = 1001;
    private static final String TAG = "AzkarForegroundService";
    private View azkarView;
    private Handler dismissHandler;
    private Runnable dismissRunnable;
    private boolean isAnimatingOut = false;
    private WindowManager windowManager;

    private void animateEntrance(final LinearLayout linearLayout, final String str) {
        if (linearLayout != null) {
            try {
                if (this.azkarView == null) {
                    return;
                }
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.setInterpolator(new DecelerateInterpolator(1.5f));
                animationSet.setFillAfter(true);
                animationSet.setFillEnabled(true);
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.85f, 1.0f, 0.85f, 1.0f, 1, 0.5f, 1, 0.5f);
                scaleAnimation.setDuration(500L);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                alphaAnimation.setDuration(400L);
                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(alphaAnimation);
                animationSet.setAnimationListener(new Animation.AnimationListener() { // from class: com.salatak.app.AzkarForegroundService.1
                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationEnd(Animation animation) {
                        linearLayout.setAlpha(1.0f);
                        linearLayout.clearAnimation();
                        AzkarForegroundService.this.announceForScreenReader(str);
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationStart(Animation animation) {
                        linearLayout.setVisibility(0);
                    }
                });
                linearLayout.startAnimation(animationSet);
                animateFlowers();
            } catch (Exception e2) {
                Log.e(TAG, "Error animating entrance: " + e2.getMessage(), e2);
                linearLayout.setAlpha(1.0f);
                linearLayout.setVisibility(0);
                announceForScreenReader(str);
            }
        }
    }

    private void animateFlowers() {
        try {
            if (this.azkarView == null) {
                return;
            }
            int[] iArr = {R.id.flowerTopRight, R.id.flowerTopLeft, R.id.flowerBottomRight, R.id.flowerBottomLeft, R.id.flowerCenter1, R.id.flowerCenter2};
            for (int i2 = 0; i2 < 6; i2++) {
                ImageView imageView = (ImageView) this.azkarView.findViewById(iArr[i2]);
                if (imageView != null) {
                    imageView.postDelayed(new d0(this, imageView, 1), (i2 * ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION) + MaterialCardViewHelper.DEFAULT_FADE_ANIM_DURATION);
                }
            }
            int[] iArr2 = {R.id.flowerInnerLeft, R.id.flowerInnerRight};
            for (int i3 = 0; i3 < 2; i3++) {
                ImageView imageView2 = (ImageView) this.azkarView.findViewById(iArr2[i3]);
                if (imageView2 != null) {
                    imageView2.postDelayed(new d0(this, imageView2, 2), 800L);
                }
            }
        } catch (Exception e2) {
            Log.e(TAG, "Error in animateFlowers: " + e2.getMessage(), e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void announceForScreenReader(String str) {
        try {
            AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService("accessibility");
            if (accessibilityManager != null && accessibilityManager.isEnabled()) {
                if (this.azkarView == null) {
                    return;
                }
                this.azkarView.postDelayed(new e0(0, this, "ذكر من أذكار المسلم: " + str), 500L);
                return;
            }
            Log.d(TAG, "Accessibility not enabled, skipping announcement");
        } catch (Exception e2) {
            Log.e(TAG, "Error checking accessibility: " + e2.getMessage(), e2);
        }
    }

    private Notification createNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("صلاتك").setContentText("جاري عرض الذكر...").setSmallIcon(R.mipmap.ic_launcher).setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, (Class<?>) MainActivity.class), AccessibilityEventCompat.TYPE_VIEW_TARGETED_BY_SCROLL)).setPriority(-1).setOngoing(false).build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel s = a.s();
            s.setDescription("إشعارات خدمة الأذكار");
            s.setShowBadge(false);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(s);
            }
        }
    }

    private void dismissWithAnimation() {
        Runnable runnable;
        if (this.isAnimatingOut) {
            return;
        }
        this.isAnimatingOut = true;
        try {
            Handler handler = this.dismissHandler;
            if (handler != null && (runnable = this.dismissRunnable) != null) {
                handler.removeCallbacks(runnable);
            }
            View view = this.azkarView;
            if (view == null) {
                stopSelf();
                return;
            }
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.comboboxCard);
            int[] iArr = {R.id.flowerTopRight, R.id.flowerTopLeft, R.id.flowerBottomRight, R.id.flowerBottomLeft, R.id.flowerCenter1, R.id.flowerCenter2};
            for (int i2 = 0; i2 < 6; i2++) {
                ImageView imageView = (ImageView) this.azkarView.findViewById(iArr[i2]);
                if (imageView != null) {
                    imageView.clearAnimation();
                    imageView.animate().alpha(0.0f).setDuration(250L).start();
                }
            }
            if (linearLayout != null) {
                linearLayout.animate().alpha(0.0f).scaleX(0.8f).scaleY(0.8f).setDuration(350L).setInterpolator(new DecelerateInterpolator()).withEndAction(new c0(this, 1)).start();
            } else {
                removeOverlayView();
                stopSelf();
            }
        } catch (Exception e2) {
            Log.e(TAG, "Error in dismissWithAnimation: " + e2.getMessage(), e2);
            removeOverlayView();
            stopSelf();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$animateFlowers$3(ImageView imageView) {
        imageView.setScaleX(0.3f);
        imageView.setScaleY(0.3f);
        imageView.setRotation(-30.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateFlowers$4(ImageView imageView) {
        try {
            if (this.azkarView == null || this.isAnimatingOut) {
                return;
            }
            imageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.flower_float));
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error starting float anim: "), TAG);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateFlowers$5(ImageView imageView) {
        try {
            if (this.azkarView != null && !this.isAnimatingOut) {
                imageView.setAlpha(0.0f);
                imageView.setVisibility(0);
                imageView.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).rotation(0.0f).setDuration(800L).setInterpolator(new DecelerateInterpolator()).withStartAction(new b2(2, imageView)).withEndAction(new d0(this, imageView, 0)).start();
            }
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error animating flower: "), TAG);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateFlowers$6(ImageView imageView) {
        try {
            if (this.azkarView == null || this.isAnimatingOut) {
                return;
            }
            imageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.flower_float));
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error starting inner float: "), TAG);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$announceForScreenReader$7(String str) {
        try {
            View view = this.azkarView;
            if (view == null) {
                return;
            }
            TextView textView = (TextView) view.findViewById(R.id.tvComboboxAzkarText);
            if (textView != null) {
                textView.setFocusable(true);
                textView.setFocusableInTouchMode(true);
                textView.setAccessibilityLiveRegion(2);
                textView.requestFocus();
                textView.sendAccessibilityEvent(8);
            }
            this.azkarView.announceForAccessibility(str);
            Log.d(TAG, "Screen reader announcement sent: " + str);
        } catch (Exception e2) {
            Log.e(TAG, "Error sending accessibility event: " + e2.getMessage(), e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissWithAnimation$8() {
        removeOverlayView();
        stopSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissWithAnimation$9() {
        try {
            View view = this.azkarView;
            FrameLayout frameLayout = view != null ? (FrameLayout) view.findViewById(R.id.comboboxRoot) : null;
            if (frameLayout != null) {
                frameLayout.animate().alpha(0.0f).setDuration(200L).withEndAction(new c0(this, 2)).start();
            } else {
                removeOverlayView();
                stopSelf();
            }
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error in exit end: "), TAG);
            removeOverlayView();
            stopSelf();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showComboBoxOverlay$0(View view) {
        if (this.isAnimatingOut) {
            return;
        }
        dismissWithAnimation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showComboBoxOverlay$1(LinearLayout linearLayout, String str) {
        try {
            animateEntrance(linearLayout, str);
        } catch (Exception e2) {
            Log.e(TAG, "Error in post animation: " + e2.getMessage(), e2);
            if (linearLayout != null) {
                linearLayout.setAlpha(1.0f);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showComboBoxOverlay$2() {
        if (this.isAnimatingOut) {
            return;
        }
        dismissWithAnimation();
    }

    private void removeOverlayView() {
        WindowManager windowManager;
        try {
            View view = this.azkarView;
            if (view != null && (windowManager = this.windowManager) != null) {
                windowManager.removeView(view);
            }
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error removing overlay view: "), TAG);
        }
        this.azkarView = null;
    }

    private void showComboBoxOverlay(String str) {
        try {
            if (!AzkarService.canDrawOverlays(this)) {
                Log.e(TAG, "No overlay permission - cannot show combobox");
                stopSelf();
                return;
            }
            removeOverlayView();
            this.isAnimatingOut = false;
            WindowManager windowManager = (WindowManager) getSystemService("window");
            this.windowManager = windowManager;
            if (windowManager == null) {
                Log.e(TAG, "WindowManager is null");
                stopSelf();
                return;
            }
            View inflate = LayoutInflater.from(this).inflate(R.layout.overlay_azkar_combobox, (ViewGroup) null);
            this.azkarView = inflate;
            if (inflate == null) {
                Log.e(TAG, "Failed to inflate overlay layout");
                stopSelf();
                return;
            }
            TextView textView = (TextView) inflate.findViewById(R.id.tvComboboxAzkarText);
            ImageView imageView = (ImageView) this.azkarView.findViewById(R.id.btnCloseCombobox);
            LinearLayout linearLayout = (LinearLayout) this.azkarView.findViewById(R.id.comboboxCard);
            FrameLayout frameLayout = (FrameLayout) this.azkarView.findViewById(R.id.comboboxRoot);
            if (textView != null) {
                textView.setText(str);
                textView.setContentDescription(str);
            }
            if (linearLayout != null) {
                linearLayout.setAlpha(0.0f);
                linearLayout.setVisibility(0);
            }
            if (imageView != null) {
                imageView.setOnClickListener(new y0(3, this));
            }
            if (frameLayout != null) {
                frameLayout.setAlpha(0.0f);
            }
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, Build.VERSION.SDK_INT >= 26 ? 2038 : PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT, 2621728, -3);
            layoutParams.gravity = 17;
            this.windowManager.addView(this.azkarView, layoutParams);
            if (frameLayout != null) {
                frameLayout.animate().alpha(1.0f).setDuration(300L).setListener(null).start();
            }
            this.azkarView.post(new b0(this, linearLayout, str, 0));
            c0 c0Var = new c0(this, 0);
            this.dismissRunnable = c0Var;
            this.dismissHandler.postDelayed(c0Var, 30000L);
            Log.d(TAG, "Combobox overlay added to window: " + str);
        } catch (Exception e2) {
            Log.e(TAG, "Error showing combobox overlay: " + e2.getMessage(), e2);
            stopSelf();
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        this.dismissHandler = new Handler(Looper.getMainLooper());
    }

    @Override // android.app.Service
    public void onDestroy() {
        Runnable runnable;
        super.onDestroy();
        Handler handler = this.dismissHandler;
        if (handler != null && (runnable = this.dismissRunnable) != null) {
            handler.removeCallbacks(runnable);
        }
        removeOverlayView();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i2, int i3) {
        try {
            if (Build.VERSION.SDK_INT >= 34) {
                startForeground(1001, createNotification(), 1073741824);
            } else {
                startForeground(1001, createNotification());
            }
            String stringExtra = (intent == null || !intent.hasExtra("azkar_text")) ? null : intent.getStringExtra("azkar_text");
            if (stringExtra == null || stringExtra.isEmpty()) {
                stringExtra = AzkarService.getRandomAzkar(this);
            }
            showComboBoxOverlay(stringExtra);
            return 2;
        } catch (Exception e2) {
            Log.e(TAG, "Error in onStartCommand: " + e2.getMessage(), e2);
            stopSelf();
            return 2;
        }
    }
}
