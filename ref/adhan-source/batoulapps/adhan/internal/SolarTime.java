package com.batoulapps.adhan.internal;

import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.data.DateComponents;

/* loaded from: classes.dex */
public class SolarTime {
    private double approximateTransit;
    private final SolarCoordinates nextSolar;
    private final Coordinates observer;
    private final SolarCoordinates prevSolar;
    private final SolarCoordinates solar;
    public final double sunrise;
    public final double sunset;
    public final double transit;

    public SolarTime(DateComponents dateComponents, Coordinates coordinates) {
        double julianDay = CalendricalHelper.julianDay(dateComponents.year, dateComponents.month, dateComponents.day);
        SolarCoordinates solarCoordinates = new SolarCoordinates(julianDay - 1.0d);
        this.prevSolar = solarCoordinates;
        SolarCoordinates solarCoordinates2 = new SolarCoordinates(julianDay);
        this.solar = solarCoordinates2;
        SolarCoordinates solarCoordinates3 = new SolarCoordinates(julianDay + 1.0d);
        this.nextSolar = solarCoordinates3;
        double approximateTransit = Astronomical.approximateTransit(coordinates.longitude, solarCoordinates2.apparentSiderealTime, solarCoordinates2.rightAscension);
        this.approximateTransit = approximateTransit;
        this.observer = coordinates;
        this.transit = Astronomical.correctedTransit(approximateTransit, coordinates.longitude, solarCoordinates2.apparentSiderealTime, solarCoordinates2.rightAscension, solarCoordinates.rightAscension, solarCoordinates3.rightAscension);
        this.sunrise = Astronomical.correctedHourAngle(this.approximateTransit, -0.8333333333333334d, coordinates, false, solarCoordinates2.apparentSiderealTime, solarCoordinates2.rightAscension, solarCoordinates.rightAscension, solarCoordinates3.rightAscension, solarCoordinates2.declination, solarCoordinates.declination, solarCoordinates3.declination);
        this.sunset = Astronomical.correctedHourAngle(this.approximateTransit, -0.8333333333333334d, coordinates, true, solarCoordinates2.apparentSiderealTime, solarCoordinates2.rightAscension, solarCoordinates.rightAscension, solarCoordinates3.rightAscension, solarCoordinates2.declination, solarCoordinates.declination, solarCoordinates3.declination);
    }

    public double afternoon(ShadowLength shadowLength) {
        double abs = Math.abs(this.observer.latitude - this.solar.declination);
        return hourAngle(Math.toDegrees(Math.atan(1.0d / (Math.tan(Math.toRadians(abs)) + shadowLength.getShadowLength()))), true);
    }

    public double hourAngle(double d, boolean z2) {
        double d2 = this.approximateTransit;
        Coordinates coordinates = this.observer;
        SolarCoordinates solarCoordinates = this.solar;
        double d3 = solarCoordinates.apparentSiderealTime;
        double d4 = solarCoordinates.rightAscension;
        SolarCoordinates solarCoordinates2 = this.prevSolar;
        double d5 = solarCoordinates2.rightAscension;
        SolarCoordinates solarCoordinates3 = this.nextSolar;
        return Astronomical.correctedHourAngle(d2, d, coordinates, z2, d3, d4, d5, solarCoordinates3.rightAscension, solarCoordinates.declination, solarCoordinates2.declination, solarCoordinates3.declination);
    }
}
