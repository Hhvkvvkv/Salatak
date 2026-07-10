package com.batoulapps.adhan.internal;

/* loaded from: classes.dex */
class DoubleUtil {
    public static double closestAngle(double d) {
        return (d < -180.0d || d > 180.0d) ? d - (Math.round(d / 360.0d) * 360) : d;
    }

    public static double normalizeWithBound(double d, double d2) {
        return d - (Math.floor(d / d2) * d2);
    }

    public static double unwindAngle(double d) {
        return normalizeWithBound(d, 360.0d);
    }
}
