package com.batoulapps.adhan;

/* loaded from: classes.dex */
public enum CalculationMethod {
    MUSLIM_WORLD_LEAGUE,
    EGYPTIAN,
    KARACHI,
    UMM_AL_QURA,
    DUBAI,
    MOON_SIGHTING_COMMITTEE,
    NORTH_AMERICA,
    KUWAIT,
    QATAR,
    SINGAPORE,
    OTHER;

    /* renamed from: com.batoulapps.adhan.CalculationMethod$1, reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$batoulapps$adhan$CalculationMethod;

        static {
            int[] iArr = new int[CalculationMethod.values().length];
            $SwitchMap$com$batoulapps$adhan$CalculationMethod = iArr;
            try {
                iArr[CalculationMethod.MUSLIM_WORLD_LEAGUE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$CalculationMethod[CalculationMethod.EGYPTIAN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$CalculationMethod[CalculationMethod.KARACHI.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$CalculationMethod[CalculationMethod.UMM_AL_QURA.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$CalculationMethod[CalculationMethod.DUBAI.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$CalculationMethod[CalculationMethod.MOON_SIGHTING_COMMITTEE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$CalculationMethod[CalculationMethod.NORTH_AMERICA.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$CalculationMethod[CalculationMethod.KUWAIT.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$CalculationMethod[CalculationMethod.QATAR.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$CalculationMethod[CalculationMethod.SINGAPORE.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$CalculationMethod[CalculationMethod.OTHER.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
        }
    }

    public CalculationParameters getParameters() {
        switch (AnonymousClass1.$SwitchMap$com$batoulapps$adhan$CalculationMethod[ordinal()]) {
            case 1:
                return new CalculationParameters(18.0d, 17.0d, this).withMethodAdjustments(new PrayerAdjustments(0, 0, 1, 0, 0, 0));
            case 2:
                return new CalculationParameters(19.5d, 17.5d, this).withMethodAdjustments(new PrayerAdjustments(0, 0, 1, 0, 0, 0));
            case 3:
                return new CalculationParameters(18.0d, 18.0d, this).withMethodAdjustments(new PrayerAdjustments(0, 0, 1, 0, 0, 0));
            case 4:
                return new CalculationParameters(18.5d, 90, this);
            case 5:
                return new CalculationParameters(18.2d, 18.2d, this).withMethodAdjustments(new PrayerAdjustments(0, -3, 3, 3, 3, 0));
            case 6:
                return new CalculationParameters(18.0d, 18.0d, this).withMethodAdjustments(new PrayerAdjustments(0, 0, 5, 0, 3, 0));
            case 7:
                return new CalculationParameters(15.0d, 15.0d, this).withMethodAdjustments(new PrayerAdjustments(0, 0, 1, 0, 0, 0));
            case 8:
                return new CalculationParameters(18.0d, 17.5d, this);
            case 9:
                return new CalculationParameters(18.0d, 90, this);
            case 10:
                return new CalculationParameters(20.0d, 18.0d, this).withMethodAdjustments(new PrayerAdjustments(0, 0, 1, 0, 0, 0));
            case 11:
                return new CalculationParameters(0.0d, 0.0d, this);
            default:
                throw new IllegalArgumentException("Invalid CalculationMethod");
        }
    }
}
