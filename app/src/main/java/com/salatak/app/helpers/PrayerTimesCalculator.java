package com.salatak.app.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import com.batoulapps.adhan.CalculationMethod;
import com.batoulapps.adhan.CalculationParameters;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.HighLatitudeRule;
import com.batoulapps.adhan.Madhab;
import com.batoulapps.adhan.Prayer;
import com.batoulapps.adhan.PrayerAdjustments;
import com.batoulapps.adhan.PrayerTimes;
import com.batoulapps.adhan.data.DateComponents;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/* loaded from: classes2.dex */
public class PrayerTimesCalculator {

    /* renamed from: com.salatak.app.helpers.PrayerTimesCalculator$1, reason: invalid class name */
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

    public static class PrayerTimesResult {
        public String asr;
        public String asr24h;
        public Prayer currentPrayer;
        public String dhuhr;
        public String dhuhr24h;
        public String fajr;
        public String fajr24h;
        public String isha;
        public String isha24h;
        public String maghrib;
        public String maghrib24h;
        public Prayer nextPrayer;
        public long nextPrayerTime;
        public String sunrise;
        public String sunrise24h;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static PrayerTimesResult calculatePrayerTimes(Context context, double d, double d2) {
        char c;
        SharedPreferences sharedPreferences = context.getSharedPreferences("SalatakPrefs", 0);
        String string = sharedPreferences.getString("calculation_method", "MuslimWorldLeague");
        String string2 = sharedPreferences.getString("madhab", "Shafi");
        int i2 = sharedPreferences.getInt("fajr_adjustment", 0);
        int i3 = sharedPreferences.getInt("sunrise_adjustment", 0);
        int i4 = sharedPreferences.getInt("dhuhr_adjustment", 0);
        int i5 = sharedPreferences.getInt("asr_adjustment", 0);
        int i6 = sharedPreferences.getInt("maghrib_adjustment", 0);
        int i7 = sharedPreferences.getInt("isha_adjustment", 0);
        Coordinates coordinates = new Coordinates(d, d2);
        TimeZone timeZone = TimeZone.getDefault();
        if (sharedPreferences.getBoolean("summer_time_enabled", false) && !timeZone.inDaylightTime(new Date())) {
            i2 += 60;
            i3 += 60;
            i4 += 60;
            i5 += 60;
            i6 += 60;
            i7 += 60;
        }
        Calendar calendar = Calendar.getInstance(timeZone);
        DateComponents dateComponents = new DateComponents(calendar.get(1), calendar.get(2) + 1, calendar.get(5));
        CalculationParameters calculationMethod = getCalculationMethod(string);
        calculationMethod.madhab = string2.equals("Hanafi") ? Madhab.HANAFI : Madhab.SHAFI;
        String string3 = sharedPreferences.getString("high_latitude_rule", "none");
        string3.getClass();
        switch (string3.hashCode()) {
            case -787824461:
                if (string3.equals("MiddleOfTheNight")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -632855549:
                if (string3.equals("TwilightAngle")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -584697057:
                if (string3.equals("SeventhOfTheNight")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                calculationMethod.highLatitudeRule = HighLatitudeRule.MIDDLE_OF_THE_NIGHT;
                break;
            case 1:
                calculationMethod.highLatitudeRule = HighLatitudeRule.TWILIGHT_ANGLE;
                break;
            case 2:
                calculationMethod.highLatitudeRule = HighLatitudeRule.SEVENTH_OF_THE_NIGHT;
                break;
        }
        PrayerAdjustments prayerAdjustments = calculationMethod.adjustments;
        prayerAdjustments.fajr = i2;
        prayerAdjustments.sunrise = i3;
        prayerAdjustments.dhuhr = i4;
        prayerAdjustments.asr = i5;
        prayerAdjustments.maghrib = i6;
        prayerAdjustments.isha = i7;
        PrayerTimes prayerTimes = new PrayerTimes(coordinates, dateComponents, calculationMethod);
        SimpleDateFormat simpleDateFormat = sharedPreferences.getString("time_format", "12").equals("24") ? new SimpleDateFormat("HH:mm", Locale.US) : new SimpleDateFormat("hh:mm a", Locale.US);
        simpleDateFormat.setTimeZone(timeZone);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm", Locale.US);
        simpleDateFormat2.setTimeZone(timeZone);
        PrayerTimesResult prayerTimesResult = new PrayerTimesResult();
        prayerTimesResult.fajr = simpleDateFormat.format(prayerTimes.fajr);
        prayerTimesResult.sunrise = simpleDateFormat.format(prayerTimes.sunrise);
        prayerTimesResult.dhuhr = simpleDateFormat.format(prayerTimes.dhuhr);
        prayerTimesResult.asr = simpleDateFormat.format(prayerTimes.asr);
        prayerTimesResult.maghrib = simpleDateFormat.format(prayerTimes.maghrib);
        prayerTimesResult.isha = simpleDateFormat.format(prayerTimes.isha);
        prayerTimesResult.fajr24h = simpleDateFormat2.format(prayerTimes.fajr);
        prayerTimesResult.sunrise24h = simpleDateFormat2.format(prayerTimes.sunrise);
        prayerTimesResult.dhuhr24h = simpleDateFormat2.format(prayerTimes.dhuhr);
        prayerTimesResult.asr24h = simpleDateFormat2.format(prayerTimes.asr);
        prayerTimesResult.maghrib24h = simpleDateFormat2.format(prayerTimes.maghrib);
        prayerTimesResult.isha24h = simpleDateFormat2.format(prayerTimes.isha);
        Date date = new Date();
        prayerTimesResult.currentPrayer = prayerTimes.currentPrayer(date);
        Prayer nextPrayer = prayerTimes.nextPrayer(date);
        prayerTimesResult.nextPrayer = nextPrayer;
        if (nextPrayer == null || nextPrayer == Prayer.NONE) {
            calendar.add(5, 1);
            PrayerTimes prayerTimes2 = new PrayerTimes(coordinates, new DateComponents(calendar.get(1), calendar.get(2) + 1, calendar.get(5)), calculationMethod);
            prayerTimesResult.nextPrayer = Prayer.FAJR;
            prayerTimesResult.nextPrayerTime = prayerTimes2.fajr.getTime();
        } else {
            prayerTimesResult.nextPrayerTime = prayerTimes.timeForPrayer(nextPrayer).getTime();
        }
        return prayerTimesResult;
    }

    public static String formatTime(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(date);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static CalculationParameters getCalculationMethod(String str) {
        char c;
        switch (str.hashCode()) {
            case -2070403900:
                if (str.equals("Jordan")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case -2036087297:
                if (str.equals("Kuwait")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -1835785125:
                if (str.equals("Russia")) {
                    c = '#';
                    break;
                }
                c = 65535;
                break;
            case -1793632056:
                if (str.equals("Tehran")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -1778564402:
                if (str.equals("Turkey")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case -1398106084:
                if (str.equals("MuslimWorldLeague")) {
                    c = '\'';
                    break;
                }
                c = 65535;
                break;
            case -1390138320:
                if (str.equals("Morocco")) {
                    c = 19;
                    break;
                }
                c = 65535;
                break;
            case -1246099243:
                if (str.equals("Palestine")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case -884569212:
                if (str.equals("Afghanistan")) {
                    c = 31;
                    break;
                }
                c = 65535;
                break;
            case -770596381:
                if (str.equals("Bangladesh")) {
                    c = 26;
                    break;
                }
                c = 65535;
                break;
            case -684851599:
                if (str.equals("Nigeria")) {
                    c = 28;
                    break;
                }
                c = 65535;
                break;
            case -365109388:
                if (str.equals("Somalia")) {
                    c = 30;
                    break;
                }
                c = 65535;
                break;
            case -107812217:
                if (str.equals("SouthAfrica")) {
                    c = '%';
                    break;
                }
                c = 65535;
                break;
            case 2287417:
                if (str.equals("Iraq")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case 2461355:
                if (str.equals("Oman")) {
                    c = 22;
                    break;
                }
                c = 65535;
                break;
            case 66382265:
                if (str.equals("Dubai")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 70793495:
                if (str.equals("India")) {
                    c = 27;
                    break;
                }
                c = 65535;
                break;
            case 73413677:
                if (str.equals("Libya")) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            case 77809525:
                if (str.equals("Qatar")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 80237007:
                if (str.equals("Sudan")) {
                    c = 20;
                    break;
                }
                c = 65535;
                break;
            case 80369860:
                if (str.equals("Syria")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 85310250:
                if (str.equals("Yemen")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case 131201883:
                if (str.equals("Malaysia")) {
                    c = 23;
                    break;
                }
                c = 65535;
                break;
            case 133498567:
                if (str.equals("Maldives")) {
                    c = '$';
                    break;
                }
                c = 65535;
                break;
            case 489548859:
                if (str.equals("Egyptian")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 499614468:
                if (str.equals("Singapore")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 614217844:
                if (str.equals("Uzbekistan")) {
                    c = ' ';
                    break;
                }
                c = 65535;
                break;
            case 695337775:
                if (str.equals("Tunisia")) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case 728596575:
                if (str.equals("Karachi")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 748389889:
                if (str.equals("Algeria")) {
                    c = 18;
                    break;
                }
                c = 65535;
                break;
            case 1085587817:
                if (str.equals("NorthAmerica")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 1322267389:
                if (str.equals("Bahrain")) {
                    c = 21;
                    break;
                }
                c = 65535;
                break;
            case 1474019620:
                if (str.equals("Indonesia")) {
                    c = 24;
                    break;
                }
                c = 65535;
                break;
            case 1715851317:
                if (str.equals("Lebanon")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case 1781677121:
                if (str.equals("Mauritania")) {
                    c = 29;
                    break;
                }
                c = 65535;
                break;
            case 1889776435:
                if (str.equals("UmmAlQura")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1935241404:
                if (str.equals("SaudiGeneral")) {
                    c = '&';
                    break;
                }
                c = 65535;
                break;
            case 1995569824:
                if (str.equals("Bosnia")) {
                    c = '!';
                    break;
                }
                c = 65535;
                break;
            case 1998399853:
                if (str.equals("Brunei")) {
                    c = 25;
                    break;
                }
                c = 65535;
                break;
            case 2112320571:
                if (str.equals("France")) {
                    c = '\"';
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return CalculationMethod.EGYPTIAN.getParameters();
            case 1:
                return CalculationMethod.KARACHI.getParameters();
            case 2:
                return CalculationMethod.UMM_AL_QURA.getParameters();
            case 3:
                return CalculationMethod.DUBAI.getParameters();
            case 4:
                return CalculationMethod.QATAR.getParameters();
            case 5:
                return CalculationMethod.KUWAIT.getParameters();
            case 6:
                return CalculationMethod.SINGAPORE.getParameters();
            case 7:
                CalculationParameters parameters = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters.fajrAngle = 18.0d;
                parameters.ishaAngle = 17.0d;
                return parameters;
            case '\b':
                CalculationParameters parameters2 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters2.fajrAngle = 17.7d;
                parameters2.ishaAngle = 14.0d;
                return parameters2;
            case '\t':
                return CalculationMethod.NORTH_AMERICA.getParameters();
            case '\n':
                CalculationParameters parameters3 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters3.fajrAngle = 18.0d;
                parameters3.ishaAngle = 18.0d;
                return parameters3;
            case 11:
                CalculationParameters parameters4 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters4.fajrAngle = 19.5d;
                parameters4.ishaAngle = 17.5d;
                return parameters4;
            case '\f':
                CalculationParameters parameters5 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters5.fajrAngle = 19.5d;
                parameters5.ishaAngle = 17.5d;
                return parameters5;
            case '\r':
                CalculationParameters parameters6 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters6.fajrAngle = 19.5d;
                parameters6.ishaAngle = 17.5d;
                return parameters6;
            case 14:
                CalculationParameters parameters7 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters7.fajrAngle = 18.0d;
                parameters7.ishaAngle = 17.0d;
                return parameters7;
            case 15:
                CalculationParameters parameters8 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters8.fajrAngle = 18.0d;
                parameters8.ishaAngle = 17.0d;
                return parameters8;
            case 16:
                CalculationParameters parameters9 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters9.fajrAngle = 19.5d;
                parameters9.ishaAngle = 17.5d;
                return parameters9;
            case 17:
                CalculationParameters parameters10 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters10.fajrAngle = 18.0d;
                parameters10.ishaAngle = 18.0d;
                return parameters10;
            case 18:
                CalculationParameters parameters11 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters11.fajrAngle = 18.0d;
                parameters11.ishaAngle = 17.0d;
                return parameters11;
            case 19:
                CalculationParameters parameters12 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters12.fajrAngle = 19.0d;
                parameters12.ishaAngle = 17.0d;
                return parameters12;
            case 20:
                CalculationParameters parameters13 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters13.fajrAngle = 18.0d;
                parameters13.ishaAngle = 17.0d;
                return parameters13;
            case 21:
                CalculationParameters parameters14 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters14.fajrAngle = 18.0d;
                parameters14.ishaAngle = 17.0d;
                return parameters14;
            case 22:
                CalculationParameters parameters15 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters15.fajrAngle = 18.0d;
                parameters15.ishaAngle = 18.0d;
                return parameters15;
            case 23:
                CalculationParameters parameters16 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters16.fajrAngle = 20.0d;
                parameters16.ishaAngle = 18.0d;
                return parameters16;
            case 24:
                CalculationParameters parameters17 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters17.fajrAngle = 20.0d;
                parameters17.ishaAngle = 18.0d;
                return parameters17;
            case 25:
                CalculationParameters parameters18 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters18.fajrAngle = 20.0d;
                parameters18.ishaAngle = 18.0d;
                return parameters18;
            case 26:
                CalculationParameters parameters19 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters19.fajrAngle = 18.0d;
                parameters19.ishaAngle = 18.0d;
                return parameters19;
            case 27:
                CalculationParameters parameters20 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters20.fajrAngle = 18.0d;
                parameters20.ishaAngle = 18.0d;
                return parameters20;
            case 28:
                CalculationParameters parameters21 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters21.fajrAngle = 18.0d;
                parameters21.ishaAngle = 17.0d;
                return parameters21;
            case 29:
                CalculationParameters parameters22 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters22.fajrAngle = 18.0d;
                parameters22.ishaAngle = 17.0d;
                return parameters22;
            case 30:
                CalculationParameters parameters23 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters23.fajrAngle = 18.0d;
                parameters23.ishaAngle = 17.0d;
                return parameters23;
            case 31:
                CalculationParameters parameters24 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters24.fajrAngle = 18.0d;
                parameters24.ishaAngle = 18.0d;
                return parameters24;
            case ' ':
                CalculationParameters parameters25 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters25.fajrAngle = 18.0d;
                parameters25.ishaAngle = 18.0d;
                return parameters25;
            case '!':
                CalculationParameters parameters26 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters26.fajrAngle = 18.0d;
                parameters26.ishaAngle = 17.0d;
                return parameters26;
            case '\"':
                CalculationParameters parameters27 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters27.fajrAngle = 12.0d;
                parameters27.ishaAngle = 12.0d;
                return parameters27;
            case '#':
                CalculationParameters parameters28 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters28.fajrAngle = 16.0d;
                parameters28.ishaAngle = 15.0d;
                return parameters28;
            case '$':
                CalculationParameters parameters29 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters29.fajrAngle = 18.0d;
                parameters29.ishaAngle = 17.0d;
                return parameters29;
            case '%':
                CalculationParameters parameters30 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters30.fajrAngle = 18.0d;
                parameters30.ishaAngle = 17.0d;
                return parameters30;
            case '&':
                CalculationParameters parameters31 = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
                parameters31.fajrAngle = 18.5d;
                parameters31.ishaAngle = 17.0d;
                return parameters31;
            default:
                return CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
        }
    }

    public static String getPrayerNameArabic(Prayer prayer) {
        if (prayer == null) {
            return "";
        }
        switch (AnonymousClass1.$SwitchMap$com$batoulapps$adhan$Prayer[prayer.ordinal()]) {
        }
        return "";
    }
}
