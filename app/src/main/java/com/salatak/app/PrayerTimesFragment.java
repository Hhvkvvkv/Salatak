package com.salatak.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.batoulapps.adhan.Prayer;
import com.salatak.app.PrayerTimesFragment;
import com.salatak.app.helpers.DiagnosticLogger;
import com.salatak.app.helpers.PrayerTimesCalculator;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes2.dex */
public class PrayerTimesFragment extends Fragment {
    private static final int LOCATION_PERMISSION_REQUEST = 1001;
    private View cardGregorianDate;
    private View cardHijriDate;
    private View frameCountdown;
    private Handler gpsTimeoutHandler;
    private Runnable gpsTimeoutRunnable;
    private LinearLayout llNextPrayerBox;
    private LinearLayout llPrayerTimes;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private PrayerTimesCalculator.PrayerTimesResult prayerTimesResult;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvGregorianDate;
    private TextView tvHijriDate;
    private TextView tvLocationBanner;
    private TextView tvNextPrayer;
    private TextView tvTimeRemaining;
    private Handler updateHandler;
    private Runnable updateRunnable;
    private double currentLatitude = 21.4225d;
    private double currentLongitude = 39.8262d;
    private boolean usingFallbackLocation = true;
    private boolean gpsUpdateReceived = false;
    private boolean initialLoadDone = false;
    private final AtomicBoolean calculationRunning = new AtomicBoolean(false);

    /* renamed from: com.salatak.app.PrayerTimesFragment$1, reason: invalid class name */
    public class AnonymousClass1 implements LocationListener {
        public AnonymousClass1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLocationChanged$0() {
            PrayerTimesFragment.this.calculateAndDisplayPrayerTimes();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLocationChanged$1() {
            PrayerTimesFragment.this.updateLocationBanner();
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(@NonNull Location location) {
            if (PrayerTimesFragment.this.gpsUpdateReceived) {
                return;
            }
            PrayerTimesFragment.this.gpsUpdateReceived = true;
            PrayerTimesFragment.this.stopLocationUpdates();
            DiagnosticLogger.log(PrayerTimesFragment.this.getContext() != null ? PrayerTimesFragment.this.getContext() : PrayerTimesFragment.this.requireActivity(), "GPS", "تم استقبال الموقع: " + location.getLatitude() + ", " + location.getLongitude() + " دقة=" + location.getAccuracy() + " مزود=" + location.getProvider());
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double abs = Math.abs(latitude - PrayerTimesFragment.this.currentLatitude);
            double abs2 = Math.abs(longitude - PrayerTimesFragment.this.currentLongitude);
            PrayerTimesFragment.this.currentLatitude = latitude;
            PrayerTimesFragment.this.currentLongitude = longitude;
            PrayerTimesFragment.this.usingFallbackLocation = false;
            PrayerTimesFragment.this.saveCoordinates(latitude, longitude);
            if (abs > 0.005d || abs2 > 0.005d) {
                final int i2 = 0;
                new Handler(Looper.getMainLooper()).post(new Runnable(this) { // from class: com.salatak.app.o1
                    public final /* synthetic */ PrayerTimesFragment.AnonymousClass1 c;

                    {
                        this.c = this;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        switch (i2) {
                            case 0:
                                this.c.lambda$onLocationChanged$0();
                                break;
                            default:
                                this.c.lambda$onLocationChanged$1();
                                break;
                        }
                    }
                });
            } else {
                final int i3 = 1;
                new Handler(Looper.getMainLooper()).post(new Runnable(this) { // from class: com.salatak.app.o1
                    public final /* synthetic */ PrayerTimesFragment.AnonymousClass1 c;

                    {
                        this.c = this;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        switch (i3) {
                            case 0:
                                this.c.lambda$onLocationChanged$0();
                                break;
                            default:
                                this.c.lambda$onLocationChanged$1();
                                break;
                        }
                    }
                });
            }
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(@NonNull String str) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(@NonNull String str) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i2, Bundle bundle) {
        }
    }

    private View buildFallbackView(String str, String str2, boolean z2, boolean z3, String str3) {
        if (!isAdded() || getContext() == null) {
            return null;
        }
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(0, 0, 0, (int) (getResources().getDisplayMetrics().density * 6.0f));
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setPadding(40, 36, 40, 36);
        linearLayout.setGravity(16);
        linearLayout.setLayoutDirection(1);
        try {
            linearLayout.setBackgroundResource(z2 ? R.drawable.prayer_card_touch_active : R.drawable.prayer_card_touch);
        } catch (Exception unused) {
            linearLayout.setBackgroundColor(z2 ? -14799539 : -15063228);
        }
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
        textView.setText(str);
        textView.setTextSize(18.0f);
        textView.setTextColor(z2 ? -10496 : -1);
        Typeface typeface = Typeface.DEFAULT_BOLD;
        textView.setTypeface(typeface);
        TextView textView2 = new TextView(getContext());
        textView2.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        textView2.setText(convertToArabicNumerals(str2));
        textView2.setTextSize(18.0f);
        textView2.setTextColor(z2 ? -10496 : -1);
        textView2.setTypeface(typeface);
        linearLayout.addView(textView);
        linearLayout.addView(textView2);
        if (!z3) {
            linearLayout.setClickable(true);
            linearLayout.setFocusable(true);
            linearLayout.setOnClickListener(new m1(this, str3, str, 1));
        }
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void calculateAndDisplayPrayerTimes() {
        if (!isAdded() || getContext() == null) {
            return;
        }
        if (this.calculationRunning.getAndSet(true)) {
            DiagnosticLogger.log(getContext(), "PRAYER", "حساب قيد التشغيل — تجاهل الطلب المتكرر");
            return;
        }
        DiagnosticLogger.log(getContext(), "PRAYER", "حساب المواقيت (خلفية): lat=" + this.currentLatitude + " lon=" + this.currentLongitude + " fallback=" + this.usingFallbackLocation);
        final double d = this.currentLatitude;
        final double d2 = this.currentLongitude;
        final Context applicationContext = getContext().getApplicationContext();
        new Thread(new Runnable() { // from class: com.salatak.app.n1
            @Override // java.lang.Runnable
            public final void run() {
                PrayerTimesFragment.this.lambda$calculateAndDisplayPrayerTimes$3(applicationContext, d, d2);
            }
        }).start();
    }

    private String calculateHijriDate(Calendar calendar) {
        int i2;
        int i3;
        int i4;
        try {
            int i5 = calendar.get(5);
            int i6 = calendar.get(2);
            int i7 = i6 + 1;
            int i8 = calendar.get(1);
            if (i7 <= 2) {
                i8--;
                i7 = i6 + 13;
            }
            int i9 = i8 / 100;
            int i10 = (((((int) ((i8 + 4716) * 365.25d)) + ((int) ((i7 + 1) * 30.6001d))) + i5) + ((2 - i9) + (i9 / 4))) - 1939332;
            int i11 = (int) ((r0 - 1939333) / 10631.0d);
            int i12 = (i10 - (i11 * 10631)) + 354;
            int i13 = (((int) (i12 / 5670.0d)) * ((int) ((i12 * 43) / 15238.0d))) + (((int) ((10985 - i12) / 5316.0d)) * ((int) ((i12 * 50) / 17719.0d)));
            i2 = ((i12 - (((int) ((30 - i13) / 15.0d)) * ((int) ((i13 * 17719) / 50.0d)))) - (((int) (i13 / 16.0d)) * ((int) ((i13 * 15238) / 43.0d)))) + 29;
            i3 = (i2 * 24) / 709;
            i4 = ((i11 * 30) + i13) - 30;
        } catch (Exception unused) {
        }
        try {
            return convertToArabicNumerals((i2 - ((i3 * 709) / 24)) + " " + ((i3 < 1 || i3 > 12) ? "" : new String[]{"محرم", "صفر", "ربيع الأول", "ربيع الثاني", "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان", "رمضان", "شوال", "ذو القعدة", "ذو الحجة"}[i3 - 1]) + " " + i4 + " هـ");
        } catch (Exception unused2) {
            return "";
        }
    }

    private String convertToArabicNumerals(String str) {
        if (str == null) {
            return "";
        }
        char[] cArr = {1632, 1633, 1634, 1635, 1636, 1637, 1638, 1639, 1640, 1641};
        StringBuilder sb = new StringBuilder();
        for (char c : str.replace("AM", "ص").replace("PM", "م").toCharArray()) {
            if (c >= '0' && c <= '9') {
                c = cArr[c - '0'];
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x011a A[Catch: Exception -> 0x0108, TryCatch #2 {Exception -> 0x0108, blocks: (B:33:0x0102, B:35:0x011a, B:37:0x0127, B:40:0x0148, B:43:0x015f, B:45:0x016f, B:46:0x0178, B:55:0x0140, B:57:0x0145), top: B:32:0x0102 }] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x015a  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x016f A[Catch: Exception -> 0x0108, TryCatch #2 {Exception -> 0x0108, blocks: (B:33:0x0102, B:35:0x011a, B:37:0x0127, B:40:0x0148, B:43:0x015f, B:45:0x016f, B:46:0x0178, B:55:0x0140, B:57:0x0145), top: B:32:0x0102 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x015d  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0137 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x012e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Unreachable blocks removed: 1, instructions: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void displayPrayerTimes() {
        /*
            Method dump skipped, instructions count: 534
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.salatak.app.PrayerTimesFragment.displayPrayerTimes():void");
    }

    private void doFullLoad() {
        loadSavedLocation();
        calculateAndDisplayPrayerTimes();
        requestLiveLocationUpdate();
    }

    private int getPrayerIcon(String str) {
        str.getClass();
        switch (str) {
            case "sunrise":
                return R.drawable.ic_sunrise;
            case "asr":
                return R.drawable.ic_sun_decorative;
            case "fajr":
                return R.drawable.ic_moon_crescent;
            case "isha":
                return R.drawable.ic_moon_crescent;
            case "dhuhr":
                return R.drawable.ic_sun;
            case "maghrib":
                return R.drawable.ic_sunrise;
            default:
                return R.drawable.ic_sun;
        }
    }

    private boolean hasLocationPermission() {
        if (!isAdded() || getContext() == null) {
            return false;
        }
        return ContextCompat.checkSelfPermission(getContext(), "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(getContext(), "android.permission.ACCESS_COARSE_LOCATION") == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buildFallbackView$5(String str, String str2, View view) {
        try {
            Intent intent = new Intent(getContext(), (Class<?>) PrayerSettingsActivity.class);
            intent.putExtra("prayer_key", str);
            intent.putExtra("prayer_name", str2);
            startActivity(intent);
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$calculateAndDisplayPrayerTimes$2(PrayerTimesCalculator.PrayerTimesResult prayerTimesResult) {
        this.calculationRunning.set(false);
        if (!isAdded() || getContext() == null) {
            return;
        }
        this.prayerTimesResult = prayerTimesResult;
        try {
            displayPrayerTimes();
        } catch (Exception e2) {
            DiagnosticLogger.error(getContext(), "PRAYER", "فشل displayPrayerTimes", e2);
            showPrayerTimesError();
        }
        LinearLayout linearLayout = this.llPrayerTimes;
        if (linearLayout != null && linearLayout.getChildCount() == 0 && this.prayerTimesResult != null) {
            DiagnosticLogger.warn(getContext(), "PRAYER", "كشف تلقائي: لا صفوف — تشغيل العرض الاحتياطي");
            showFallbackPrayerRows();
        }
        try {
            updateNextPrayerInfo();
        } catch (Exception unused) {
        }
        try {
            updateLocationBanner();
        } catch (Exception unused2) {
        }
        try {
            PrayerReminderScheduler.scheduleAllAlarms(getContext());
        } catch (Exception unused3) {
        }
        try {
            SwipeRefreshLayout swipeRefreshLayout = this.swipeRefresh;
            if (swipeRefreshLayout == null || !swipeRefreshLayout.isRefreshing()) {
                return;
            }
            this.swipeRefresh.setRefreshing(false);
        } catch (Exception unused4) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$calculateAndDisplayPrayerTimes$3(Context context, double d, double d2) {
        PrayerTimesCalculator.PrayerTimesResult prayerTimesResult;
        try {
            prayerTimesResult = PrayerTimesCalculator.calculatePrayerTimes(context, d, d2);
        } catch (Exception e2) {
            e = e2;
            prayerTimesResult = null;
        }
        try {
            DiagnosticLogger.log(context, "PRAYER", "الحساب نجح في الخلفية: فجر=" + prayerTimesResult.fajr);
        } catch (Exception e3) {
            e = e3;
            DiagnosticLogger.error(context, "PRAYER", "فشل الحساب الأساسي — تجربة مكة", e);
            try {
                prayerTimesResult = PrayerTimesCalculator.calculatePrayerTimes(context, 21.4225d, 39.8262d);
                DiagnosticLogger.warn(context, "PRAYER", "تم الحساب بمكة كاحتياطي");
            } catch (Exception e4) {
                DiagnosticLogger.error(context, "PRAYER", "فشل حساب مكة أيضاً", e4);
            }
            new Handler(Looper.getMainLooper()).post(new e0(6, this, prayerTimesResult));
        }
        new Handler(Looper.getMainLooper()).post(new e0(6, this, prayerTimesResult));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPrayerTimes$4(String str, String str2, View view) {
        try {
            Intent intent = new Intent(getContext(), (Class<?>) PrayerSettingsActivity.class);
            intent.putExtra("prayer_key", str);
            intent.putExtra("prayer_name", str2);
            startActivity(intent);
        } catch (Exception unused) {
            Toast.makeText(getContext(), "خطأ في فتح إعدادات الصلاة", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupSwipeRefresh$0() {
        this.gpsUpdateReceived = false;
        doFullLoad();
        updateCalendarDates();
        Toast.makeText(getContext(), "جاري تحديث مواقيت الصلاة...", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateLocationBanner$1(View view) {
        try {
            startActivity(new Intent(getContext(), (Class<?>) SettingsActivity.class));
        } catch (Exception unused) {
        }
    }

    private void loadSavedLocation() {
        try {
            if (isAdded() && getContext() != null) {
                Context context = getContext();
                getContext();
                SharedPreferences sharedPreferences = context.getSharedPreferences("SalatakPrefs", 0);
                if (sharedPreferences.getBoolean("manual_location_enabled", false)) {
                    float f2 = sharedPreferences.getFloat("manual_location_lat", 0.0f);
                    float f3 = sharedPreferences.getFloat("manual_location_lon", 0.0f);
                    if (f2 != 0.0f && f3 != 0.0f) {
                        this.currentLatitude = f2;
                        this.currentLongitude = f3;
                        this.usingFallbackLocation = false;
                        DiagnosticLogger.log(getContext(), "LOCATION", "موقع يدوي: " + f2 + ", " + f3);
                        return;
                    }
                }
                float f4 = sharedPreferences.getFloat("latitude", 0.0f);
                float f5 = sharedPreferences.getFloat("longitude", 0.0f);
                if (f4 != 0.0f && f5 != 0.0f) {
                    this.currentLatitude = f4;
                    this.currentLongitude = f5;
                    this.usingFallbackLocation = false;
                    DiagnosticLogger.log(getContext(), "LOCATION", "موقع محفوظ: " + f4 + ", " + f5);
                    return;
                }
                if (hasLocationPermission()) {
                    try {
                        Context context2 = getContext();
                        getContext();
                        LocationManager locationManager = (LocationManager) context2.getSystemService("location");
                        String[] strArr = {"gps", "network", "passive"};
                        Location location = null;
                        for (int i2 = 0; i2 < 3; i2++) {
                            try {
                                Location lastKnownLocation = locationManager.getLastKnownLocation(strArr[i2]);
                                if (lastKnownLocation != null && (location == null || lastKnownLocation.getAccuracy() < location.getAccuracy())) {
                                    location = lastKnownLocation;
                                }
                            } catch (Exception unused) {
                            }
                        }
                        if (location != null) {
                            this.currentLatitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            this.currentLongitude = longitude;
                            this.usingFallbackLocation = false;
                            saveCoordinates(this.currentLatitude, longitude);
                            DiagnosticLogger.log(getContext(), "LOCATION", "آخر موقع معروف: " + location.getLatitude() + ", " + location.getLongitude() + " دقة=" + location.getAccuracy());
                            return;
                        }
                    } catch (Exception e2) {
                        DiagnosticLogger.error(getContext(), "LOCATION", "خطأ في getLastKnownLocation", e2);
                    }
                } else {
                    DiagnosticLogger.warn(getContext(), "LOCATION", "لا توجد صلاحية موقع");
                }
                this.currentLatitude = 21.4225d;
                this.currentLongitude = 39.8262d;
                this.usingFallbackLocation = true;
                DiagnosticLogger.warn(getContext(), "LOCATION", "تم استخدام احتياطي مكة المكرمة — لا يوجد موقع حقيقي");
            }
        } catch (Exception e3) {
            DiagnosticLogger.error(getContext(), "LOCATION", "استثناء في loadSavedLocation", e3);
        }
    }

    private void requestLiveLocationUpdate() {
        if (!isAdded() || getContext() == null) {
            return;
        }
        if (!hasLocationPermission()) {
            DiagnosticLogger.warn(getContext(), "GPS", "لا توجد صلاحية — تم تجاوز طلب الموقع الحي");
            return;
        }
        Context context = getContext();
        getContext();
        if (context.getSharedPreferences("SalatakPrefs", 0).getBoolean("manual_location_enabled", false)) {
            DiagnosticLogger.log(getContext(), "GPS", "الموقع اليدوي مفعّل — تم تجاوز GPS");
            return;
        }
        try {
            stopLocationUpdates();
            Context context2 = getContext();
            getContext();
            LocationManager locationManager = (LocationManager) context2.getSystemService("location");
            this.locationManager = locationManager;
            this.gpsUpdateReceived = false;
            boolean isProviderEnabled = locationManager.isProviderEnabled("gps");
            boolean isProviderEnabled2 = this.locationManager.isProviderEnabled("network");
            DiagnosticLogger.log(getContext(), "GPS", "GPS=" + isProviderEnabled + " Network=" + isProviderEnabled2);
            AnonymousClass1 anonymousClass1 = new AnonymousClass1();
            this.locationListener = anonymousClass1;
            if (isProviderEnabled) {
                this.locationManager.requestLocationUpdates("gps", 0L, 0.0f, anonymousClass1, Looper.getMainLooper());
            }
            if (isProviderEnabled2) {
                this.locationManager.requestLocationUpdates("network", 0L, 0.0f, this.locationListener, Looper.getMainLooper());
            }
            Handler handler = new Handler(Looper.getMainLooper());
            this.gpsTimeoutHandler = handler;
            b2 b2Var = new b2(3, this);
            this.gpsTimeoutRunnable = b2Var;
            handler.postDelayed(b2Var, 20000L);
            DiagnosticLogger.log(getContext(), "GPS", "تم تسجيل طلب الموقع بنجاح");
        } catch (Exception e2) {
            DiagnosticLogger.error(getContext(), "GPS", "فشل requestLocationUpdates", e2);
        }
    }

    private void requestLocationPermissionIfNeeded() {
        if (!isAdded() || getContext() == null || hasLocationPermission()) {
            return;
        }
        requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}, 1001);
    }

    private String safe(String str) {
        return str != null ? str : "--:--";
    }

    private void safelyApplyOrnamentBackground(View view) {
        try {
            Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.corner_ornament);
            if (drawable != null) {
                drawable.setBounds(0, 0, 1, 1);
                Bitmap createBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
                drawable.draw(new Canvas(createBitmap));
                createBitmap.recycle();
                DiagnosticLogger.log(requireContext(), "UI", "corner_ornament مدعوم على هذا الجهاز");
            }
        } catch (Exception unused) {
            DiagnosticLogger.warn(requireContext(), "UI", "corner_ornament غير مدعوم — تجاوز التدرج الدائري");
            try {
                View findViewWithTag = view.findViewWithTag("corner_ornament");
                if (findViewWithTag != null) {
                    findViewWithTag.setBackgroundColor(0);
                }
            } catch (Exception unused2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveCoordinates(double d, double d2) {
        try {
            if (isAdded() && getContext() != null) {
                Context context = getContext();
                getContext();
                context.getSharedPreferences("SalatakPrefs", 0).edit().putFloat("latitude", (float) d).putFloat("longitude", (float) d2).apply();
            }
        } catch (Exception unused) {
        }
    }

    private void setupSwipeRefresh() {
        SwipeRefreshLayout swipeRefreshLayout = this.swipeRefresh;
        if (swipeRefreshLayout == null) {
            return;
        }
        swipeRefreshLayout.setColorSchemeColors(-3825624, -10496, -2052805);
        this.swipeRefresh.setProgressBackgroundColorSchemeColor(-15918294);
        this.swipeRefresh.setOnRefreshListener(new f2(this));
    }

    private void setupUpdateTimer() {
        this.updateHandler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() { // from class: com.salatak.app.PrayerTimesFragment.2
            @Override // java.lang.Runnable
            public void run() {
                if (PrayerTimesFragment.this.isAdded()) {
                    PrayerTimesFragment.this.updateNextPrayerInfo();
                }
                PrayerTimesFragment.this.updateHandler.postDelayed(this, 1000L);
            }
        };
        this.updateRunnable = runnable;
        this.updateHandler.post(runnable);
    }

    private void showFallbackPrayerRows() {
        if (this.llPrayerTimes == null || this.prayerTimesResult == null || !isAdded() || getContext() == null) {
            return;
        }
        try {
            this.llPrayerTimes.removeAllViews();
            String[] strArr = {"الفجر", "الشروق", Calendar.getInstance().get(7) == 6 ? "الجمعة" : "الظهر", "العصر", "المغرب", "العشاء"};
            String[] strArr2 = {"fajr", "sunrise", "dhuhr", "asr", "maghrib", "isha"};
            String[] strArr3 = {safe(this.prayerTimesResult.fajr), safe(this.prayerTimesResult.sunrise), safe(this.prayerTimesResult.dhuhr), safe(this.prayerTimesResult.asr), safe(this.prayerTimesResult.maghrib), safe(this.prayerTimesResult.isha)};
            Prayer prayer = this.prayerTimesResult.nextPrayer;
            Prayer[] prayerArr = {Prayer.FAJR, Prayer.SUNRISE, Prayer.DHUHR, Prayer.ASR, Prayer.MAGHRIB, Prayer.ISHA};
            int i2 = 0;
            while (i2 < 6) {
                int i3 = i2;
                View buildFallbackView = buildFallbackView(strArr[i2], strArr3[i2], prayer != null && prayer == prayerArr[i2], strArr2[i2].equals("sunrise"), strArr2[i2]);
                if (buildFallbackView != null) {
                    this.llPrayerTimes.addView(buildFallbackView);
                }
                i2 = i3 + 1;
            }
            DiagnosticLogger.warn(getContext(), "DISPLAY", "عرض احتياطي نجح — صفوف=" + this.llPrayerTimes.getChildCount());
        } catch (Exception e2) {
            DiagnosticLogger.error(getContext(), "DISPLAY", "فشل العرض الاحتياطي الطارئ", e2);
        }
    }

    private void showPrayerTimesError() {
        try {
            if (this.llPrayerTimes != null && isAdded() && getContext() != null) {
                this.llPrayerTimes.removeAllViews();
                TextView textView = new TextView(getContext());
                textView.setText("تعذّر تحميل مواقيت الصلاة — اسحب لأعلى لإعادة المحاولة");
                textView.setTextColor(-2130706433);
                textView.setTextSize(14.0f);
                textView.setGravity(17);
                textView.setPadding(32, 48, 32, 48);
                this.llPrayerTimes.addView(textView);
            }
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopLocationUpdates() {
        Runnable runnable;
        LocationListener locationListener;
        try {
            LocationManager locationManager = this.locationManager;
            if (locationManager != null && (locationListener = this.locationListener) != null) {
                locationManager.removeUpdates(locationListener);
            }
        } catch (Exception unused) {
        }
        try {
            Handler handler = this.gpsTimeoutHandler;
            if (handler == null || (runnable = this.gpsTimeoutRunnable) == null) {
                return;
            }
            handler.removeCallbacks(runnable);
        } catch (Exception unused2) {
        }
    }

    private void updateCalendarDates() {
        if (!isAdded() || getContext() == null) {
            return;
        }
        try {
            Calendar calendar = Calendar.getInstance();
            if (this.tvGregorianDate != null) {
                String[] strArr = {"يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"};
                int i2 = calendar.get(5);
                int i3 = calendar.get(2);
                int i4 = calendar.get(1);
                this.tvGregorianDate.setText(convertToArabicNumerals(i2 + " " + strArr[i3] + " " + i4 + " م"));
                View view = this.cardGregorianDate;
                if (view != null) {
                    view.setContentDescription("التقويم الميلادي: " + i2 + " " + strArr[i3] + " " + i4);
                    this.cardGregorianDate.setImportantForAccessibility(1);
                }
            }
            if (this.tvHijriDate != null) {
                String calculateHijriDate = calculateHijriDate(calendar);
                this.tvHijriDate.setText(calculateHijriDate);
                View view2 = this.cardHijriDate;
                if (view2 != null) {
                    view2.setContentDescription("التقويم الهجري: " + calculateHijriDate);
                    this.cardHijriDate.setImportantForAccessibility(1);
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLocationBanner() {
        if (this.tvLocationBanner == null || !isAdded()) {
            return;
        }
        if (!this.usingFallbackLocation) {
            this.tvLocationBanner.setVisibility(8);
            return;
        }
        this.tvLocationBanner.setVisibility(0);
        this.tvLocationBanner.setText("⚠ يتم عرض مواقيت مكة المكرمة مؤقتاً — فعّل GPS أو حدد موقعك يدوياً");
        this.tvLocationBanner.setOnClickListener(new y0(4, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNextPrayerInfo() {
        PrayerTimesCalculator.PrayerTimesResult prayerTimesResult;
        TextView textView;
        if (!isAdded() || getContext() == null || (prayerTimesResult = this.prayerTimesResult) == null || (textView = this.tvNextPrayer) == null || this.tvTimeRemaining == null) {
            return;
        }
        try {
            Prayer prayer = prayerTimesResult.nextPrayer;
            if (prayer != null && prayer != Prayer.NONE) {
                textView.setText("متبقي على صلاة " + PrayerTimesCalculator.getPrayerNameArabic(prayer));
                long j2 = this.prayerTimesResult.nextPrayerTime;
                if (j2 > 0) {
                    long currentTimeMillis = j2 - System.currentTimeMillis();
                    if (currentTimeMillis <= 0) {
                        this.tvTimeRemaining.setText(convertToArabicNumerals("00:00:00"));
                        return;
                    }
                    long j3 = currentTimeMillis / 3600000;
                    long j4 = (currentTimeMillis % 3600000) / 60000;
                    this.tvTimeRemaining.setText(convertToArabicNumerals(String.format("%02d:%02d:%02d", Long.valueOf(j3), Long.valueOf(j4), Long.valueOf((currentTimeMillis % 60000) / 1000))));
                    View view = this.frameCountdown;
                    if (view != null) {
                        view.setContentDescription("متبقي على صلاة " + PrayerTimesCalculator.getPrayerNameArabic(prayer) + " " + j3 + " ساعة " + j4 + " دقيقة");
                        return;
                    }
                    return;
                }
                return;
            }
            textView.setText("متبقي على صلاة الفجر");
            this.tvTimeRemaining.setText(convertToArabicNumerals("00:00:00"));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_prayer_times, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        Runnable runnable;
        super.onDestroyView();
        stopLocationUpdates();
        Handler handler = this.updateHandler;
        if (handler != null && (runnable = this.updateRunnable) != null) {
            handler.removeCallbacks(runnable);
        }
        this.llPrayerTimes = null;
        this.tvNextPrayer = null;
        this.tvTimeRemaining = null;
        this.tvHijriDate = null;
        this.tvGregorianDate = null;
        this.llNextPrayerBox = null;
        this.frameCountdown = null;
        this.cardHijriDate = null;
        this.cardGregorianDate = null;
        this.swipeRefresh = null;
        this.tvLocationBanner = null;
        this.initialLoadDone = false;
    }

    @Override // androidx.fragment.app.Fragment
    public void onRequestPermissionsResult(int i2, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i2, strArr, iArr);
        if (i2 == 1001 && iArr.length > 0 && iArr[0] == 0) {
            this.gpsUpdateReceived = false;
            doFullLoad();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (isAdded()) {
            if (!this.initialLoadDone) {
                this.initialLoadDone = true;
                DiagnosticLogger.log(requireContext(), "FRAGMENT", "onResume: تم تجاهله (أول مرة)");
            } else {
                DiagnosticLogger.log(requireContext(), "FRAGMENT", "onResume: إعادة تحميل بعد العودة");
                this.gpsUpdateReceived = false;
                doFullLoad();
                updateCalendarDates();
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        DiagnosticLogger.deviceInfo(requireContext());
        DiagnosticLogger.log(requireContext(), "FRAGMENT", "onViewCreated بدأ");
        this.tvNextPrayer = (TextView) view.findViewById(R.id.tvNextPrayer);
        this.tvTimeRemaining = (TextView) view.findViewById(R.id.tvTimeRemaining);
        this.tvHijriDate = (TextView) view.findViewById(R.id.tvHijriDate);
        this.tvGregorianDate = (TextView) view.findViewById(R.id.tvGregorianDate);
        this.llPrayerTimes = (LinearLayout) view.findViewById(R.id.llPrayerTimes);
        this.llNextPrayerBox = (LinearLayout) view.findViewById(R.id.llNextPrayerBox);
        this.frameCountdown = view.findViewById(R.id.frameCountdown);
        this.cardHijriDate = view.findViewById(R.id.cardHijriDate);
        this.cardGregorianDate = view.findViewById(R.id.cardGregorianDate);
        this.swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        this.tvLocationBanner = (TextView) view.findViewById(R.id.tvLocationBanner);
        Context requireContext = requireContext();
        StringBuilder sb = new StringBuilder("Views: llPrayerTimes=");
        sb.append(this.llPrayerTimes != null ? "OK" : "NULL");
        sb.append(" tvNextPrayer=");
        sb.append(this.tvNextPrayer != null ? "OK" : "NULL");
        sb.append(" swipeRefresh=");
        sb.append(this.swipeRefresh != null ? "OK" : "NULL");
        DiagnosticLogger.log(requireContext, "FRAGMENT", sb.toString());
        safelyApplyOrnamentBackground(view);
        setupSwipeRefresh();
        updateCalendarDates();
        setupUpdateTimer();
        doFullLoad();
        DiagnosticLogger.log(requireContext(), "FRAGMENT", "onViewCreated انتهى — initialLoadDone=" + this.initialLoadDone);
        requestLocationPermissionIfNeeded();
    }
}
