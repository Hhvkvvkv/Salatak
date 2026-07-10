package com.salatak.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import java.util.Calendar;

/* loaded from: classes2.dex */
public class CalendarWidgetProvider extends AppWidgetProvider {
    private static int[] calculateHijri(Calendar calendar) {
        int i2 = calendar.get(5);
        int i3 = calendar.get(2);
        int i4 = i3 + 1;
        int i5 = calendar.get(1);
        if (i4 <= 2) {
            i5--;
            i4 = i3 + 13;
        }
        int i6 = i5 / 100;
        int i7 = (((((((int) ((i5 + 4716) * 365.25d)) + ((int) ((i4 + 1) * 30.6001d))) + i2) + ((i6 / 4) + (2 - i6))) - 1939332) - (((int) ((r9 - 1939333) / 10631.0d)) * 10631)) + 354;
        int i8 = ((i7 - (((int) ((30 - r2) / 15.0d)) * ((int) ((r2 * 17719) / 50.0d)))) - (((int) (((((int) (i7 / 5670.0d)) * ((int) ((i7 * 43) / 15238.0d))) + (((int) ((10985 - i7) / 5316.0d)) * ((int) ((i7 * 50) / 17719.0d)))) / 16.0d)) * ((int) ((r2 * 15238) / 43.0d)))) + 29;
        int i9 = (i8 * 24) / 709;
        return new int[]{i8 - ((i9 * 709) / 24), i9, ((r9 * 30) + r2) - 30};
    }

    private static String convertToArabicNumerals(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c < '0' || c > '9') {
                sb.append(c);
            } else {
                sb.append((char) (c + 1584));
            }
        }
        return sb.toString();
    }

    private static void updateWidget(Context context, AppWidgetManager appWidgetManager, int i2) {
        int i3;
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_calendar);
        remoteViews.setOnClickPendingIntent(R.id.widgetCalendarRoot, PendingIntent.getActivity(context, 0, new Intent(context, (Class<?>) MainActivity.class), AccessibilityEventCompat.TYPE_VIEW_TARGETED_BY_SCROLL));
        try {
            Calendar calendar = Calendar.getInstance();
            String[] strArr = {"محرم", "صفر", "ربيع الأول", "ربيع الثاني", "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان", "رمضان", "شوال", "ذو القعدة", "ذو الحجة"};
            int[] calculateHijri = calculateHijri(calendar);
            if (calculateHijri[0] > 0 && (i3 = calculateHijri[1]) >= 1 && i3 <= 12) {
                remoteViews.setTextViewText(R.id.tvCalWidgetHijri, convertToArabicNumerals(String.valueOf(calculateHijri[0])) + " " + strArr[calculateHijri[1] - 1] + " " + convertToArabicNumerals(String.valueOf(calculateHijri[2])) + " هـ");
            }
            remoteViews.setTextViewText(R.id.tvCalWidgetGregorian, convertToArabicNumerals(String.valueOf(calendar.get(5))) + " " + new String[]{"يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"}[calendar.get(2)] + " " + convertToArabicNumerals(String.valueOf(calendar.get(1))) + " م");
            remoteViews.setTextViewText(R.id.tvCalWidgetDayName, new String[]{"الأحد", "الإثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت"}[calendar.get(7) - 1]);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        appWidgetManager.updateAppWidget(i2, remoteViews);
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] iArr) {
        for (int i2 : iArr) {
            updateWidget(context, appWidgetManager, i2);
        }
    }
}
