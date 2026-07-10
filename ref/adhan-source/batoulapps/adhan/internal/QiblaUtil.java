package com.batoulapps.adhan.internal;

import com.batoulapps.adhan.Coordinates;

/* loaded from: classes.dex */
public class QiblaUtil {
    private static final Coordinates MAKKAH = new Coordinates(21.4225241d, 39.8261818d);

    public static double calculateQiblaDirection(Coordinates coordinates) {
        Coordinates coordinates2 = MAKKAH;
        double radians = Math.toRadians(coordinates2.longitude) - Math.toRadians(coordinates.longitude);
        double radians2 = Math.toRadians(coordinates.latitude);
        return DoubleUtil.unwindAngle(Math.toDegrees(Math.atan2(Math.sin(radians), (Math.tan(Math.toRadians(coordinates2.latitude)) * Math.cos(radians2)) - (Math.cos(radians) * Math.sin(radians2)))));
    }
}
