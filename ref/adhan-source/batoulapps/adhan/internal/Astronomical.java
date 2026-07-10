package com.batoulapps.adhan.internal;

import com.batoulapps.adhan.Coordinates;

/* loaded from: classes.dex */
class Astronomical {
    public static double altitudeOfCelestialBody(double d, double d2, double d3) {
        return Math.toDegrees(Math.asin((Math.cos(Math.toRadians(d3)) * Math.cos(Math.toRadians(d2)) * Math.cos(Math.toRadians(d))) + (Math.sin(Math.toRadians(d2)) * Math.sin(Math.toRadians(d)))));
    }

    public static double apparentObliquityOfTheEcliptic(double d, double d2) {
        return (Math.cos(Math.toRadians(125.04d - (d * 1934.136d))) * 0.00256d) + d2;
    }

    public static double apparentSolarLongitude(double d, double d2) {
        return DoubleUtil.unwindAngle(((d2 + solarEquationOfTheCenter(d, meanSolarAnomaly(d))) - 0.00569d) - (Math.sin(Math.toRadians(125.04d - (d * 1934.136d))) * 0.00478d));
    }

    public static double approximateTransit(double d, double d2, double d3) {
        return DoubleUtil.normalizeWithBound((((d * (-1.0d)) + d3) - d2) / 360.0d, 1.0d);
    }

    public static double ascendingLunarNodeLongitude(double d) {
        double pow = Math.pow(d, 2.0d) * 0.0020708d;
        return DoubleUtil.unwindAngle((125.04452d - (1934.136261d * d)) + pow + (Math.pow(d, 3.0d) / 450000.0d));
    }

    public static double correctedHourAngle(double d, double d2, Coordinates coordinates, boolean z2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        double d10 = coordinates.longitude * (-1.0d);
        double degrees = Math.toDegrees(Math.acos((Math.sin(Math.toRadians(d2)) - (Math.sin(Math.toRadians(d7)) * Math.sin(Math.toRadians(coordinates.latitude)))) / (Math.cos(Math.toRadians(d7)) * Math.cos(Math.toRadians(coordinates.latitude))))) / 360.0d;
        double d11 = z2 ? degrees + d : d - degrees;
        double unwindAngle = DoubleUtil.unwindAngle((360.985647d * d11) + d3);
        double unwindAngle2 = DoubleUtil.unwindAngle(interpolateAngles(d4, d5, d6, d11));
        double interpolate = interpolate(d7, d8, d9, d11);
        double d12 = (unwindAngle - d10) - unwindAngle2;
        return (((altitudeOfCelestialBody(coordinates.latitude, interpolate, d12) - d2) / (Math.sin(Math.toRadians(d12)) * (Math.cos(Math.toRadians(coordinates.latitude)) * (Math.cos(Math.toRadians(interpolate)) * 360.0d)))) + d11) * 24.0d;
    }

    public static double correctedTransit(double d, double d2, double d3, double d4, double d5, double d6) {
        return ((DoubleUtil.closestAngle((DoubleUtil.unwindAngle((360.985647d * d) + d3) - (d2 * (-1.0d))) - DoubleUtil.unwindAngle(interpolateAngles(d4, d5, d6, d))) / (-360.0d)) + d) * 24.0d;
    }

    public static double interpolate(double d, double d2, double d3, double d4) {
        double d5 = d - d2;
        double d6 = d3 - d;
        return (((d4 * (d6 - d5)) + d5 + d6) * (d4 / 2.0d)) + d;
    }

    public static double interpolateAngles(double d, double d2, double d3, double d4) {
        double unwindAngle = DoubleUtil.unwindAngle(d - d2);
        double unwindAngle2 = DoubleUtil.unwindAngle(d3 - d);
        return (((d4 * (unwindAngle2 - unwindAngle)) + unwindAngle + unwindAngle2) * (d4 / 2.0d)) + d;
    }

    public static double meanLunarLongitude(double d) {
        return DoubleUtil.unwindAngle((d * 481267.8813d) + 218.3165d);
    }

    public static double meanObliquityOfTheEcliptic(double d) {
        double pow = Math.pow(d, 2.0d) * 1.639E-7d;
        return ((23.439291d - (0.013004167d * d)) - pow) + (Math.pow(d, 3.0d) * 5.036E-7d);
    }

    public static double meanSiderealTime(double d) {
        double pow = Math.pow(d, 2.0d) * 3.87933E-4d;
        return DoubleUtil.unwindAngle(((((((36525.0d * d) + 2451545.0d) - 2451545.0d) * 360.98564736629d) + 280.46061837d) + pow) - (Math.pow(d, 3.0d) / 3.871E7d));
    }

    public static double meanSolarAnomaly(double d) {
        return DoubleUtil.unwindAngle(((35999.05029d * d) + 357.52911d) - (Math.pow(d, 2.0d) * 1.537E-4d));
    }

    public static double meanSolarLongitude(double d) {
        return DoubleUtil.unwindAngle((36000.76983d * d) + 280.4664567d + (Math.pow(d, 2.0d) * 3.032E-4d));
    }

    public static double nutationInLongitude(double d, double d2, double d3, double d4) {
        double sin = Math.sin(Math.toRadians(d4)) * (-0.0047777777777777775d);
        double sin2 = Math.sin(Math.toRadians(d2) * 2.0d) * 3.6666666666666667E-4d;
        return ((sin - sin2) - (Math.sin(Math.toRadians(d3) * 2.0d) * 6.38888888888889E-5d)) + (Math.sin(Math.toRadians(d4) * 2.0d) * 5.833333333333333E-5d);
    }

    public static double nutationInObliquity(double d, double d2, double d3, double d4) {
        double cos = Math.cos(Math.toRadians(d4)) * 0.0025555555555555553d;
        double cos2 = Math.cos(Math.toRadians(d2) * 2.0d) * 1.5833333333333332E-4d;
        return ((cos + cos2) + (Math.cos(Math.toRadians(d3) * 2.0d) * 2.777777777777778E-5d)) - (Math.cos(Math.toRadians(d4) * 2.0d) * 2.4999999999999998E-5d);
    }

    public static double solarEquationOfTheCenter(double d, double d2) {
        double radians = Math.toRadians(d2);
        return (Math.sin(radians) * ((1.914602d - (0.004817d * d)) - (Math.pow(d, 2.0d) * 1.4E-5d))) + (Math.sin(2.0d * radians) * (0.019993d - (d * 1.01E-4d))) + (Math.sin(radians * 3.0d) * 2.89E-4d);
    }
}
