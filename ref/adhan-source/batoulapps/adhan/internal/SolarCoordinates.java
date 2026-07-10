package com.batoulapps.adhan.internal;

/* loaded from: classes.dex */
class SolarCoordinates {
    final double apparentSiderealTime;
    final double declination;
    final double rightAscension;

    public SolarCoordinates(double d) {
        double julianCentury = CalendricalHelper.julianCentury(d);
        double meanSolarLongitude = Astronomical.meanSolarLongitude(julianCentury);
        double meanLunarLongitude = Astronomical.meanLunarLongitude(julianCentury);
        double ascendingLunarNodeLongitude = Astronomical.ascendingLunarNodeLongitude(julianCentury);
        double radians = Math.toRadians(Astronomical.apparentSolarLongitude(julianCentury, meanSolarLongitude));
        double meanSiderealTime = Astronomical.meanSiderealTime(julianCentury);
        double nutationInLongitude = Astronomical.nutationInLongitude(julianCentury, meanSolarLongitude, meanLunarLongitude, ascendingLunarNodeLongitude);
        double nutationInObliquity = Astronomical.nutationInObliquity(julianCentury, meanSolarLongitude, meanLunarLongitude, ascendingLunarNodeLongitude);
        double meanObliquityOfTheEcliptic = Astronomical.meanObliquityOfTheEcliptic(julianCentury);
        double radians2 = Math.toRadians(Astronomical.apparentObliquityOfTheEcliptic(julianCentury, meanObliquityOfTheEcliptic));
        this.declination = Math.toDegrees(Math.asin(Math.sin(radians) * Math.sin(radians2)));
        this.rightAscension = DoubleUtil.unwindAngle(Math.toDegrees(Math.atan2(Math.sin(radians) * Math.cos(radians2), Math.cos(radians))));
        this.apparentSiderealTime = ((Math.cos(Math.toRadians(meanObliquityOfTheEcliptic + nutationInObliquity)) * (nutationInLongitude * 3600.0d)) / 3600.0d) + meanSiderealTime;
    }
}
