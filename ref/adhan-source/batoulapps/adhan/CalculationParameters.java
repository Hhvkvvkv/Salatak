package com.batoulapps.adhan;

/* loaded from: classes.dex */
public class CalculationParameters {
    public PrayerAdjustments adjustments;
    public double fajrAngle;
    public HighLatitudeRule highLatitudeRule;
    public double ishaAngle;
    public int ishaInterval;
    public Madhab madhab;
    public CalculationMethod method;
    public PrayerAdjustments methodAdjustments;

    /* renamed from: com.batoulapps.adhan.CalculationParameters$1, reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$batoulapps$adhan$HighLatitudeRule;

        static {
            int[] iArr = new int[HighLatitudeRule.values().length];
            $SwitchMap$com$batoulapps$adhan$HighLatitudeRule = iArr;
            try {
                iArr[HighLatitudeRule.MIDDLE_OF_THE_NIGHT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$HighLatitudeRule[HighLatitudeRule.SEVENTH_OF_THE_NIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$HighLatitudeRule[HighLatitudeRule.TWILIGHT_ANGLE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public static class NightPortions {
        final double fajr;
        final double isha;

        public /* synthetic */ NightPortions(double d, double d2, AnonymousClass1 anonymousClass1) {
            this(d, d2);
        }

        private NightPortions(double d, double d2) {
            this.fajr = d;
            this.isha = d2;
        }
    }

    public CalculationParameters(double d, double d2) {
        this.method = CalculationMethod.OTHER;
        this.madhab = Madhab.SHAFI;
        this.highLatitudeRule = HighLatitudeRule.MIDDLE_OF_THE_NIGHT;
        this.adjustments = new PrayerAdjustments();
        this.methodAdjustments = new PrayerAdjustments();
        this.fajrAngle = d;
        this.ishaAngle = d2;
    }

    public NightPortions nightPortions() {
        int i2 = AnonymousClass1.$SwitchMap$com$batoulapps$adhan$HighLatitudeRule[this.highLatitudeRule.ordinal()];
        if (i2 == 1) {
            return new NightPortions(0.5d, 0.5d, null);
        }
        if (i2 == 2) {
            return new NightPortions(0.14285714285714285d, 0.14285714285714285d, null);
        }
        if (i2 == 3) {
            return new NightPortions(this.fajrAngle / 60.0d, this.ishaAngle / 60.0d, null);
        }
        throw new IllegalArgumentException("Invalid high latitude rule");
    }

    public CalculationParameters withMethodAdjustments(PrayerAdjustments prayerAdjustments) {
        this.methodAdjustments = prayerAdjustments;
        return this;
    }

    public CalculationParameters(double d, int i2) {
        this(d, 0.0d);
        this.ishaInterval = i2;
    }

    public CalculationParameters(double d, double d2, CalculationMethod calculationMethod) {
        this(d, d2);
        this.method = calculationMethod;
    }

    public CalculationParameters(double d, int i2, CalculationMethod calculationMethod) {
        this(d, i2);
        this.method = calculationMethod;
    }
}
