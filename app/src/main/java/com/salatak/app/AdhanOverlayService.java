package com.salatak.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import androidx.core.app.NotificationCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.media3.common.PlaybackException;
import androidx.media3.session.MediaController;

/* loaded from: classes2.dex */
public class AdhanOverlayService extends Service {
    private static final String CHANNEL_ID = "adhan_overlay_channel";
    public static final String EXTRA_MESSAGE = "overlay_message";
    public static final String EXTRA_PRAYER_NAME = "overlay_prayer_name";
    public static final String EXTRA_TITLE = "overlay_title";
    public static final String EXTRA_TYPE = "overlay_type";
    private static final int NOTIFICATION_ID = 3001;
    private static final String TAG = "AdhanOverlayService";
    public static final String TYPE_ADHAN = "adhan";
    public static final String TYPE_AZKAR = "azkar";
    public static final String TYPE_REMINDER = "reminder";
    private String currentType = TYPE_ADHAN;
    private boolean isDismissing = false;
    private View overlayView;
    private WindowManager windowManager;

    private Notification createNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("صلاتك").setContentText("جاري عرض التنبيه...").setSmallIcon(R.mipmap.ic_launcher).setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, (Class<?>) MainActivity.class), AccessibilityEventCompat.TYPE_VIEW_TARGETED_BY_SCROLL)).setPriority(-1).setOngoing(false).build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel h2 = a.h();
            h2.setDescription("إشعارات خدمة تنبيهات الصلاة");
            h2.setShowBadge(false);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(h2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissOverlay() {
        View view;
        View findViewById;
        if (this.isDismissing) {
            return;
        }
        this.isDismissing = true;
        if (TYPE_AZKAR.equals(this.currentType) && (view = this.overlayView) != null && (findViewById = view.findViewById(R.id.azkarBannerContainer)) != null) {
            findViewById.animate().translationY(-800.0f).setDuration(400L).setInterpolator(new AccelerateInterpolator(1.5f)).withEndAction(new g(this, 1)).start();
        } else {
            removeOverlayView();
            stopSelf();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissOverlay$4() {
        removeOverlayView();
        stopSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showOverlay$0(View view) {
        dismissOverlay();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showOverlay$1(View view, TextView textView, String str) {
        try {
            if (this.overlayView == null || view == null) {
                return;
            }
            view.requestFocus();
            view.sendAccessibilityEvent(8);
            textView.announceForAccessibility(str);
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error in accessibility focus: "), TAG);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showOverlay$2(View view) {
        dismissOverlay();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showOverlay$3(String str, View view) {
        if ("reminder".equals(str)) {
            PrayerReminderReceiver.stopCurrentSound();
        } else {
            AdhanAlarmReceiver.stopCurrentSound();
        }
        dismissOverlay();
    }

    private void removeOverlayView() {
        WindowManager windowManager;
        try {
            View view = this.overlayView;
            if (view == null || (windowManager = this.windowManager) == null) {
                return;
            }
            windowManager.removeView(view);
            this.overlayView = null;
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error removing overlay view: "), TAG);
        }
    }

    public static void showAdhanOverlay(Context context, String str) {
        try {
            if (AzkarService.canDrawOverlays(context)) {
                Intent intent = new Intent(context, (Class<?>) AdhanOverlayService.class);
                intent.putExtra(EXTRA_TITLE, "🕌 حان وقت الأذان");
                intent.putExtra(EXTRA_PRAYER_NAME, "صلاة " + str);
                intent.putExtra(EXTRA_MESSAGE, "حان الآن موعد أذان صلاة " + str);
                intent.putExtra(EXTRA_TYPE, TYPE_ADHAN);
                if (Build.VERSION.SDK_INT >= 26) {
                    context.startForegroundService(intent);
                } else {
                    context.startService(intent);
                }
            }
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error starting adhan overlay: "), TAG);
        }
    }

    private static void showAzkarNotificationFallback(Context context, String str) {
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                androidx.core.graphics.a.w();
                NotificationChannel q2 = a.q();
                q2.setDescription("إشعارات الأذكار الإسلامية");
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(q2);
                }
            }
            NotificationCompat.Builder timeoutAfter = new NotificationCompat.Builder(context, "azkar_notifications").setSmallIcon(R.mipmap.ic_launcher).setContentTitle("ذكر من أذكار المسلم").setContentText(str).setStyle(new NotificationCompat.BigTextStyle().bigText(str)).setPriority(1).setAutoCancel(true).setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, (Class<?>) MainActivity.class), 201326592)).setVisibility(1).setTimeoutAfter(MediaController.RELEASE_UNBIND_TIMEOUT_MS);
            NotificationManager notificationManager2 = (NotificationManager) context.getSystemService("notification");
            if (notificationManager2 != null) {
                notificationManager2.notify(PlaybackException.ERROR_CODE_DECODER_INIT_FAILED, timeoutAfter.build());
            }
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error showing fallback notification: "), TAG);
        }
    }

    public static void showAzkarOverlay(Context context, String str) {
        try {
            if (!AzkarService.canDrawOverlays(context)) {
                Log.w(TAG, "No overlay permission, falling back to notification");
                showAzkarNotificationFallback(context, str);
                return;
            }
            Intent intent = new Intent(context, (Class<?>) AdhanOverlayService.class);
            intent.putExtra(EXTRA_TITLE, "🕌 ذكر من أذكار المسلم");
            intent.putExtra(EXTRA_PRAYER_NAME, "");
            intent.putExtra(EXTRA_MESSAGE, str);
            intent.putExtra(EXTRA_TYPE, TYPE_AZKAR);
            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error starting azkar overlay: "), TAG);
            showAzkarNotificationFallback(context, str);
        }
    }

    private void showOverlay(String str, String str2, String str3, String str4) {
        try {
            if (!AzkarService.canDrawOverlays(this)) {
                Log.e(TAG, "No overlay permission");
                stopSelf();
                return;
            }
            removeOverlayView();
            this.windowManager = (WindowManager) getSystemService("window");
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService("layout_inflater");
            int i2 = Build.VERSION.SDK_INT >= 26 ? 2038 : PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT;
            this.currentType = str4;
            if (TYPE_AZKAR.equals(str4)) {
                View inflate = layoutInflater.inflate(R.layout.overlay_azkar_banner, (ViewGroup) null);
                this.overlayView = inflate;
                TextView textView = (TextView) inflate.findViewById(R.id.tvBannerTitle);
                TextView textView2 = (TextView) this.overlayView.findViewById(R.id.tvBannerAzkarText);
                TextView textView3 = (TextView) this.overlayView.findViewById(R.id.btnCloseBanner);
                View findViewById = this.overlayView.findViewById(R.id.azkarBannerContainer);
                String str5 = str + ". " + str3 + ". صلّي على محمد";
                findViewById.setContentDescription(str5);
                textView.setText(str);
                textView2.setText(str3);
                final int i3 = 0;
                textView3.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.h
                    public final /* synthetic */ AdhanOverlayService b;

                    {
                        this.b = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        switch (i3) {
                            case 0:
                                this.b.lambda$showOverlay$0(view);
                                break;
                            default:
                                this.b.lambda$showOverlay$2(view);
                                break;
                        }
                    }
                });
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -2, i2, 288, -3);
                layoutParams.gravity = 49;
                this.windowManager.addView(this.overlayView, layoutParams);
                findViewById.setTranslationY(-800.0f);
                findViewById.animate().translationY(0.0f).setDuration(500L).setInterpolator(new OvershootInterpolator(0.6f)).withEndAction(new androidx.media3.exoplayer.source.h(this, findViewById, textView2, str5, 1)).start();
            } else {
                View inflate2 = layoutInflater.inflate(R.layout.overlay_adhan_alert, (ViewGroup) null);
                this.overlayView = inflate2;
                TextView textView4 = (TextView) inflate2.findViewById(R.id.tvOverlayTitle);
                TextView textView5 = (TextView) this.overlayView.findViewById(R.id.tvOverlayPrayerName);
                TextView textView6 = (TextView) this.overlayView.findViewById(R.id.tvOverlayMessage);
                TextView textView7 = (TextView) this.overlayView.findViewById(R.id.btnDismissOverlay);
                TextView textView8 = (TextView) this.overlayView.findViewById(R.id.btnStopSound);
                textView4.setText(str);
                textView5.setText(str2);
                textView6.setText(str3);
                final int i4 = 1;
                textView7.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.h
                    public final /* synthetic */ AdhanOverlayService b;

                    {
                        this.b = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        switch (i4) {
                            case 0:
                                this.b.lambda$showOverlay$0(view);
                                break;
                            default:
                                this.b.lambda$showOverlay$2(view);
                                break;
                        }
                    }
                });
                textView8.setOnClickListener(new j0(3, this, str4));
                WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams(-1, -1, i2, 2621728, -3);
                layoutParams2.gravity = 17;
                this.windowManager.addView(this.overlayView, layoutParams2);
            }
            this.overlayView.postDelayed(new g(this, 0), 30000);
            Log.d(TAG, "Overlay shown: type=" + str4 + ", title=" + str);
        } catch (Exception e2) {
            Log.e(TAG, "Error showing overlay: " + e2.getMessage());
            stopSelf();
        }
    }

    public static void showReminderOverlay(Context context, String str, int i2) {
        try {
            if (AzkarService.canDrawOverlays(context)) {
                Intent intent = new Intent(context, (Class<?>) AdhanOverlayService.class);
                intent.putExtra(EXTRA_TITLE, "🕌 تنبيه صلاة");
                intent.putExtra(EXTRA_PRAYER_NAME, "صلاة " + str);
                intent.putExtra(EXTRA_MESSAGE, "باقي " + i2 + " دقيقة على أذان " + str);
                intent.putExtra(EXTRA_TYPE, "reminder");
                if (Build.VERSION.SDK_INT >= 26) {
                    context.startForegroundService(intent);
                } else {
                    context.startService(intent);
                }
            }
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error starting reminder overlay: "), TAG);
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
    }

    @Override // android.app.Service
    public void onDestroy() {
        WindowManager windowManager;
        super.onDestroy();
        try {
            View view = this.overlayView;
            if (view == null || (windowManager = this.windowManager) == null) {
                return;
            }
            windowManager.removeView(view);
            this.overlayView = null;
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error in onDestroy: "), TAG);
        }
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i2, int i3) {
        String str;
        try {
            if (Build.VERSION.SDK_INT >= 34) {
                startForeground(PlaybackException.ERROR_CODE_PARSING_CONTAINER_MALFORMED, createNotification(), 1073741824);
            } else {
                startForeground(3001, createNotification());
            }
            String str2 = TYPE_ADHAN;
            String str3 = "حان وقت الأذان";
            String str4 = "";
            if (intent != null) {
                String stringExtra = intent.getStringExtra(EXTRA_TITLE);
                String stringExtra2 = intent.getStringExtra(EXTRA_PRAYER_NAME);
                String stringExtra3 = intent.getStringExtra(EXTRA_MESSAGE);
                String stringExtra4 = intent.getStringExtra(EXTRA_TYPE);
                if (stringExtra != null) {
                    str3 = stringExtra;
                }
                if (stringExtra2 == null) {
                    stringExtra2 = "";
                }
                if (stringExtra3 != null) {
                    str4 = stringExtra3;
                }
                if (stringExtra4 != null) {
                    str2 = stringExtra4;
                }
                str = str4;
                str4 = stringExtra2;
            } else {
                str = "";
            }
            showOverlay(str3, str4, str, str2);
            return 2;
        } catch (Exception e2) {
            android.support.v4.media.l.p(e2, new StringBuilder("Error in onStartCommand: "), TAG);
            return 2;
        }
    }
}
