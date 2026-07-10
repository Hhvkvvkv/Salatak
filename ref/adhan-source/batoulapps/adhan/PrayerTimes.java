package com.batoulapps.adhan;

import androidx.media3.extractor.ts.TsExtractor;
import com.batoulapps.adhan.CalculationParameters;
import com.batoulapps.adhan.data.CalendarUtil;
import com.batoulapps.adhan.data.DateComponents;
import com.batoulapps.adhan.data.TimeComponents;
import com.batoulapps.adhan.internal.SolarTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/* loaded from: classes.dex */
public class PrayerTimes {
    public final Date asr;
    final CalculationParameters calculationParameters;
    final Coordinates coordinates;
    final DateComponents dateComponents;
    public final Date dhuhr;
    public final Date fajr;
    public final Date isha;
    public final Date maghrib;
    public final Date sunrise;

    /* renamed from: com.batoulapps.adhan.PrayerTimes$1, reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$batoulapps$adhan$Prayer;

        static {
            int[] iArr = new int[Prayer.values().length];
            $SwitchMap$com$batoulapps$adhan$Prayer = iArr;
            try {
                iArr[Prayer.FAJR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$Prayer[Prayer.SUNRISE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$Prayer[Prayer.DHUHR.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$Prayer[Prayer.ASR.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$Prayer[Prayer.MAGHRIB.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$Prayer[Prayer.ISHA.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$Prayer[Prayer.NONE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public PrayerTimes(Coordinates coordinates, DateComponents dateComponents, CalculationParameters calculationParameters) {
        boolean z2;
        Date date;
        Date date2;
        Date date3;
        Date date4;
        Date date5;
        PrayerTimes prayerTimes;
        Date date6;
        Date date7;
        Date date8;
        Date date9;
        int i2;
        CalculationParameters.NightPortions nightPortions;
        Date add;
        this.coordinates = coordinates;
        this.dateComponents = dateComponents;
        this.calculationParameters = calculationParameters;
        Date resolveTime = CalendarUtil.resolveTime(dateComponents);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(resolveTime);
        int i3 = calendar.get(6);
        DateComponents fromUTC = DateComponents.fromUTC(CalendarUtil.add(resolveTime, 1, 5));
        SolarTime solarTime = new SolarTime(dateComponents, coordinates);
        TimeComponents fromDouble = TimeComponents.fromDouble(solarTime.transit);
        Date dateComponents2 = fromDouble == null ? null : fromDouble.dateComponents(dateComponents);
        TimeComponents fromDouble2 = TimeComponents.fromDouble(solarTime.sunrise);
        Date dateComponents3 = fromDouble2 == null ? null : fromDouble2.dateComponents(dateComponents);
        TimeComponents fromDouble3 = TimeComponents.fromDouble(solarTime.sunset);
        Date dateComponents4 = fromDouble3 == null ? null : fromDouble3.dateComponents(dateComponents);
        TimeComponents fromDouble4 = TimeComponents.fromDouble(new SolarTime(fromUTC, coordinates).sunrise);
        boolean z3 = dateComponents2 == null || dateComponents3 == null || dateComponents4 == null || fromDouble4 == null;
        if (z3) {
            z2 = z3;
            date = null;
            date2 = null;
            date3 = null;
            date4 = null;
            dateComponents4 = null;
            date5 = null;
        } else {
            TimeComponents fromDouble5 = TimeComponents.fromDouble(solarTime.afternoon(calculationParameters.madhab.getShadowLength()));
            Date dateComponents5 = fromDouble5 != null ? fromDouble5.dateComponents(dateComponents) : null;
            Date date10 = dateComponents2;
            long time = fromDouble4.dateComponents(fromUTC).getTime() - dateComponents4.getTime();
            TimeComponents fromDouble6 = TimeComponents.fromDouble(solarTime.hourAngle(-calculationParameters.fajrAngle, false));
            date2 = fromDouble6 != null ? fromDouble6.dateComponents(dateComponents) : null;
            CalculationMethod calculationMethod = calculationParameters.method;
            CalculationMethod calculationMethod2 = CalculationMethod.MOON_SIGHTING_COMMITTEE;
            if (calculationMethod == calculationMethod2) {
                date7 = dateComponents3;
                if (coordinates.latitude >= 55.0d) {
                    date2 = CalendarUtil.add(date7, ((int) (time / 7000)) * (-1), 13);
                }
            } else {
                date7 = dateComponents3;
            }
            CalculationParameters.NightPortions nightPortions2 = calculationParameters.nightPortions();
            if (calculationParameters.method == calculationMethod2) {
                date9 = dateComponents5;
                date8 = date10;
                i2 = i3;
                z2 = z3;
                nightPortions = nightPortions2;
                add = seasonAdjustedMorningTwilight(coordinates.latitude, i2, dateComponents.year, date7);
            } else {
                date8 = date10;
                date9 = dateComponents5;
                i2 = i3;
                nightPortions = nightPortions2;
                z2 = z3;
                add = CalendarUtil.add(date7, ((int) ((nightPortions2.fajr * time) / 1000.0d)) * (-1), 13);
            }
            date2 = (date2 == null || date2.before(add)) ? add : date2;
            int i4 = calculationParameters.ishaInterval;
            if (i4 > 0) {
                date = CalendarUtil.add(dateComponents4, i4 * 60, 13);
            } else {
                TimeComponents fromDouble7 = TimeComponents.fromDouble(solarTime.hourAngle(-calculationParameters.ishaAngle, true));
                Date dateComponents6 = fromDouble7 != null ? fromDouble7.dateComponents(dateComponents) : null;
                if (calculationParameters.method == calculationMethod2 && coordinates.latitude >= 55.0d) {
                    dateComponents6 = CalendarUtil.add(dateComponents4, (int) (time / 7000), 13);
                }
                date = calculationParameters.method == calculationMethod2 ? seasonAdjustedEveningTwilight(coordinates.latitude, i2, dateComponents.year, dateComponents4) : CalendarUtil.add(dateComponents4, (int) ((nightPortions.isha * time) / 1000.0d), 13);
                if (dateComponents6 != null && !dateComponents6.after(date)) {
                    date4 = date7;
                    date = dateComponents6;
                    date5 = date9;
                    date3 = date8;
                }
            }
            date4 = date7;
            date5 = date9;
            date3 = date8;
        }
        if (z2) {
            prayerTimes = this;
            date6 = null;
        } else {
            if (date5 != null) {
                this.fajr = CalendarUtil.roundedMinute(CalendarUtil.add(CalendarUtil.add(date2, calculationParameters.adjustments.fajr, 12), calculationParameters.methodAdjustments.fajr, 12));
                this.sunrise = CalendarUtil.roundedMinute(CalendarUtil.add(CalendarUtil.add(date4, calculationParameters.adjustments.sunrise, 12), calculationParameters.methodAdjustments.sunrise, 12));
                this.dhuhr = CalendarUtil.roundedMinute(CalendarUtil.add(CalendarUtil.add(date3, calculationParameters.adjustments.dhuhr, 12), calculationParameters.methodAdjustments.dhuhr, 12));
                this.asr = CalendarUtil.roundedMinute(CalendarUtil.add(CalendarUtil.add(date5, calculationParameters.adjustments.asr, 12), calculationParameters.methodAdjustments.asr, 12));
                this.maghrib = CalendarUtil.roundedMinute(CalendarUtil.add(CalendarUtil.add(dateComponents4, calculationParameters.adjustments.maghrib, 12), calculationParameters.methodAdjustments.maghrib, 12));
                this.isha = CalendarUtil.roundedMinute(CalendarUtil.add(CalendarUtil.add(date, calculationParameters.adjustments.isha, 12), calculationParameters.methodAdjustments.isha, 12));
                return;
            }
            date6 = null;
            prayerTimes = this;
        }
        prayerTimes.fajr = date6;
        prayerTimes.sunrise = date6;
        prayerTimes.dhuhr = date6;
        prayerTimes.asr = date6;
        prayerTimes.maghrib = date6;
        prayerTimes.isha = date6;
    }

    public static int daysSinceSolstice(int i2, int i3, double d) {
        boolean isLeapYear = CalendarUtil.isLeapYear(i3);
        int i4 = isLeapYear ? 173 : TsExtractor.TS_STREAM_TYPE_AC4;
        int i5 = isLeapYear ? 366 : 365;
        if (d >= 0.0d) {
            int i6 = i2 + 10;
            return i6 >= i5 ? i6 - i5 : i6;
        }
        int i7 = i2 - i4;
        return i7 < 0 ? i7 + i5 : i7;
    }

    private static Date seasonAdjustedEveningTwilight(double d, int i2, int i3, Date date) {
        double abs = (Math.abs(d) * 0.46545454545454545d) + 75.0d;
        double abs2 = (Math.abs(d) * 0.03727272727272727d) + 75.0d;
        double abs3 = 75.0d - (Math.abs(d) * 0.16745454545454547d);
        double abs4 = (Math.abs(d) * 0.11163636363636363d) + 75.0d;
        int daysSinceSolstice = daysSinceSolstice(i2, i3, d);
        return CalendarUtil.add(date, (int) Math.round((daysSinceSolstice < 91 ? (((abs2 - abs) / 91.0d) * daysSinceSolstice) + abs : daysSinceSolstice < 137 ? abs2 + (((abs3 - abs2) / 46.0d) * (daysSinceSolstice - 91)) : daysSinceSolstice < 183 ? (((abs4 - abs3) / 46.0d) * (daysSinceSolstice - 137)) + abs3 : daysSinceSolstice < 229 ? (((abs3 - abs4) / 46.0d) * (daysSinceSolstice - 183)) + abs4 : daysSinceSolstice < 275 ? (((abs2 - abs3) / 46.0d) * (daysSinceSolstice - 229)) + abs3 : abs2 + (((abs - abs2) / 91.0d) * (daysSinceSolstice - 275))) * 60.0d), 13);
    }

    private static Date seasonAdjustedMorningTwilight(double d, int i2, int i3, Date date) {
        double abs = (Math.abs(d) * 0.5209090909090909d) + 75.0d;
        double abs2 = (Math.abs(d) * 0.35345454545454547d) + 75.0d;
        double abs3 = (Math.abs(d) * 0.5952727272727273d) + 75.0d;
        double abs4 = (Math.abs(d) * 0.8745454545454546d) + 75.0d;
        int daysSinceSolstice = daysSinceSolstice(i2, i3, d);
        return CalendarUtil.add(date, -((int) Math.round((daysSinceSolstice < 91 ? (((abs2 - abs) / 91.0d) * daysSinceSolstice) + abs : daysSinceSolstice < 137 ? abs2 + (((abs3 - abs2) / 46.0d) * (daysSinceSolstice - 91)) : daysSinceSolstice < 183 ? (((abs4 - abs3) / 46.0d) * (daysSinceSolstice - 137)) + abs3 : daysSinceSolstice < 229 ? (((abs3 - abs4) / 46.0d) * (daysSinceSolstice - 183)) + abs4 : daysSinceSolstice < 275 ? (((abs2 - abs3) / 46.0d) * (daysSinceSolstice - 229)) + abs3 : abs2 + (((abs - abs2) / 91.0d) * (daysSinceSolstice - 275))) * 60.0d)), 13);
    }

    public Prayer currentPrayer() {
        return currentPrayer(new Date());
    }

    public Prayer nextPrayer() {
        return nextPrayer(new Date());
    }

    public Date timeForPrayer(Prayer prayer) {
        switch (AnonymousClass1.$SwitchMap$com$batoulapps$adhan$Prayer[prayer.ordinal()]) {
            case 1:
                return this.fajr;
            case 2:
                return this.sunrise;
            case 3:
                return this.dhuhr;
            case 4:
                return this.asr;
            case 5:
                return this.maghrib;
            case 6:
                return this.isha;
            default:
                return null;
        }
    }

    public Prayer currentPrayer(Date date) {
        long time = date.getTime();
        return this.isha.getTime() - time <= 0 ? Prayer.ISHA : this.maghrib.getTime() - time <= 0 ? Prayer.MAGHRIB : this.asr.getTime() - time <= 0 ? Prayer.ASR : this.dhuhr.getTime() - time <= 0 ? Prayer.DHUHR : this.sunrise.getTime() - time <= 0 ? Prayer.SUNRISE : this.fajr.getTime() - time <= 0 ? Prayer.FAJR : Prayer.NONE;
    }

    public Prayer nextPrayer(Date date) {
        long time = date.getTime();
        return this.isha.getTime() - time <= 0 ? Prayer.NONE : this.maghrib.getTime() - time <= 0 ? Prayer.ISHA : this.asr.getTime() - time <= 0 ? Prayer.MAGHRIB : this.dhuhr.getTime() - time <= 0 ? Prayer.ASR : this.sunrise.getTime() - time <= 0 ? Prayer.DHUHR : this.fajr.getTime() - time <= 0 ? Prayer.SUNRISE : Prayer.FAJR;
    }
}
