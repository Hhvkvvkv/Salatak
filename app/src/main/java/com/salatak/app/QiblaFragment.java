package com.salatak.app;

import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.Qibla;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class QiblaFragment extends Fragment implements SensorEventListener {
    private Sensor accelerometer;
    private float currentAzimuth;
    private float[] geomagnetic;
    private float[] gravity;
    private ImageView ivCompassDial;
    private ImageView ivQiblaArrow;
    private Sensor magnetometer;
    private SensorManager sensorManager;
    private TextView tvLocation;
    private TextView tvQiblaAngle;
    private TextView tvQiblaDirection;
    private double userLatitude = 21.4225d;
    private double userLongitude = 39.8262d;
    private boolean hasSpokenQiblaCorrect = false;

    private float calculateQiblaDirection() {
        return (float) new Qibla(new Coordinates(this.userLatitude, this.userLongitude)).direction;
    }

    private String convertToArabicNumerals(String str) {
        if (str == null) {
            return str;
        }
        char[] cArr = {1632, 1633, 1634, 1635, 1636, 1637, 1638, 1639, 1640, 1641};
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c < '0' || c > '9') {
                sb.append(c);
            } else {
                sb.append(cArr[c - '0']);
            }
        }
        return sb.toString();
    }

    private String getLocationName(Address address) {
        StringBuilder sb = new StringBuilder();
        if (address.getLocality() != null) {
            sb.append(address.getLocality());
        } else if (address.getSubAdminArea() != null) {
            sb.append(address.getSubAdminArea());
        } else if (address.getAdminArea() != null) {
            sb.append(address.getAdminArea());
        }
        if (address.getCountryName() != null) {
            if (sb.length() > 0) {
                sb.append("، ");
            }
            sb.append(address.getCountryName());
        }
        if (sb.length() == 0) {
            sb.append("موقعك الحالي");
        }
        return sb.toString();
    }

    private boolean isScreenReaderEnabled() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getContext().getSystemService("accessibility");
        return accessibilityManager != null && accessibilityManager.isEnabled() && accessibilityManager.isTouchExplorationEnabled();
    }

    private void loadUserLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), "android.permission.ACCESS_FINE_LOCATION") != 0) {
            this.tvLocation.setText("مكة المكرمة، السعودية");
            return;
        }
        try {
            LocationManager locationManager = (LocationManager) getContext().getSystemService("location");
            Location lastKnownLocation = locationManager.getLastKnownLocation("gps");
            if (lastKnownLocation == null) {
                lastKnownLocation = locationManager.getLastKnownLocation("network");
            }
            if (lastKnownLocation == null) {
                this.tvLocation.setText("مكة المكرمة، السعودية");
                return;
            }
            this.userLatitude = lastKnownLocation.getLatitude();
            this.userLongitude = lastKnownLocation.getLongitude();
            try {
                List<Address> fromLocation = new Geocoder(getContext(), new Locale("ar")).getFromLocation(this.userLatitude, this.userLongitude, 1);
                if (fromLocation == null || fromLocation.isEmpty()) {
                    this.tvLocation.setText("موقعك الحالي");
                } else {
                    this.tvLocation.setText(getLocationName(fromLocation.get(0)));
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                this.tvLocation.setText("موقعك الحالي");
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            this.tvLocation.setText("مكة المكرمة، السعودية");
        }
    }

    private float[] lowPassFilter(float[] fArr, float[] fArr2) {
        if (fArr2 == null) {
            return fArr;
        }
        for (int i2 = 0; i2 < fArr.length; i2++) {
            float f2 = fArr2[i2];
            fArr2[i2] = android.support.v4.media.l.a(fArr[i2], f2, 0.25f, f2);
        }
        return fArr2;
    }

    private void updateCompass(float f2) {
        float calculateQiblaDirection = calculateQiblaDirection();
        RotateAnimation rotateAnimation = new RotateAnimation(-this.currentAzimuth, -f2, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setDuration(500L);
        rotateAnimation.setFillAfter(true);
        float f3 = calculateQiblaDirection - f2;
        RotateAnimation rotateAnimation2 = new RotateAnimation(calculateQiblaDirection - this.currentAzimuth, f3, 1, 0.5f, 1, 0.5f);
        rotateAnimation2.setDuration(500L);
        rotateAnimation2.setFillAfter(true);
        this.ivCompassDial.startAnimation(rotateAnimation);
        this.ivQiblaArrow.startAnimation(rotateAnimation2);
        this.currentAzimuth = f2;
        this.tvQiblaAngle.setText(convertToArabicNumerals(String.format("%.0f°", Float.valueOf(calculateQiblaDirection))));
        float abs = Math.abs(f3);
        if (abs > 180.0f) {
            abs = 360.0f - abs;
        }
        if (abs < 10.0f) {
            this.ivQiblaArrow.setColorFilter(-16711936, PorterDuff.Mode.SRC_ATOP);
            this.tvQiblaDirection.setText("اتجاه القبلة صحيح ✓");
            this.tvQiblaDirection.setTextColor(-16711936);
            if (this.hasSpokenQiblaCorrect || !isScreenReaderEnabled()) {
                return;
            }
            this.tvQiblaDirection.announceForAccessibility("اتجاه القبلة صحيح");
            this.hasSpokenQiblaCorrect = true;
            return;
        }
        if (abs < 30.0f) {
            this.ivQiblaArrow.clearColorFilter();
            this.tvQiblaDirection.setText("قريب من اتجاه القبلة");
            this.tvQiblaDirection.setTextColor(-10496);
            this.hasSpokenQiblaCorrect = false;
            return;
        }
        this.ivQiblaArrow.clearColorFilter();
        this.tvQiblaDirection.setText("وجّه الجهاز نحو القبلة");
        this.tvQiblaDirection.setTextColor(-3355444);
        this.hasSpokenQiblaCorrect = false;
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i2) {
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_qibla, viewGroup, false);
        this.ivCompassDial = (ImageView) inflate.findViewById(R.id.ivCompassDial);
        this.ivQiblaArrow = (ImageView) inflate.findViewById(R.id.ivQiblaArrow);
        this.tvQiblaAngle = (TextView) inflate.findViewById(R.id.tvQiblaAngle);
        this.tvQiblaDirection = (TextView) inflate.findViewById(R.id.tvQiblaDirection);
        this.tvLocation = (TextView) inflate.findViewById(R.id.tvLocation);
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService("sensor");
        this.sensorManager = sensorManager;
        this.accelerometer = sensorManager.getDefaultSensor(1);
        this.magnetometer = this.sensorManager.getDefaultSensor(2);
        loadUserLocation();
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        this.sensorManager.unregisterListener(this);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        Sensor sensor = this.accelerometer;
        if (sensor != null) {
            this.sensorManager.registerListener(this, sensor, 1);
        }
        Sensor sensor2 = this.magnetometer;
        if (sensor2 != null) {
            this.sensorManager.registerListener(this, sensor2, 1);
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] fArr;
        if (sensorEvent.sensor.getType() == 1) {
            this.gravity = lowPassFilter((float[]) sensorEvent.values.clone(), this.gravity);
        }
        if (sensorEvent.sensor.getType() == 2) {
            this.geomagnetic = lowPassFilter((float[]) sensorEvent.values.clone(), this.geomagnetic);
        }
        float[] fArr2 = this.gravity;
        if (fArr2 == null || (fArr = this.geomagnetic) == null) {
            return;
        }
        float[] fArr3 = new float[9];
        if (SensorManager.getRotationMatrix(fArr3, new float[9], fArr2, fArr)) {
            SensorManager.getOrientation(fArr3, new float[3]);
            updateCompass((((float) Math.toDegrees(r4[0])) + 360.0f) % 360.0f);
        }
    }
}
