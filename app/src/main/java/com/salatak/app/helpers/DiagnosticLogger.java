package com.salatak.app.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/* loaded from: classes2.dex */
public class DiagnosticLogger {
    private static final SimpleDateFormat FMT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.US);
    private static final String LOG_FILE = "salatak_diagnostic.txt";
    private static final int MAX_LINES = 600;

    public static void clearLog(Context context) {
        try {
            getLogFile(context).delete();
        } catch (Exception unused) {
        }
    }

    public static void deviceInfo(Context context) {
        try {
            StringBuilder sb = new StringBuilder("═══════════════════════════════\nالجهاز : ");
            sb.append(Build.MANUFACTURER);
            sb.append(" ");
            sb.append(Build.MODEL);
            sb.append("\nAndroid: ");
            sb.append(Build.VERSION.RELEASE);
            sb.append(" (API ");
            sb.append(Build.VERSION.SDK_INT);
            sb.append(")\n");
            String prop = getProp("ro.miui.ui.version.name");
            if (!prop.isEmpty()) {
                sb.append("MIUI   : ");
                sb.append(prop);
                sb.append("\n");
            }
            String prop2 = getProp("ro.mi.os.version.name");
            if (!prop2.isEmpty()) {
                sb.append("HyperOS: ");
                sb.append(prop2);
                sb.append("\n");
            }
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                sb.append("التطبيق: v");
                sb.append(packageInfo.versionName);
                sb.append(" (");
                sb.append(packageInfo.versionCode);
                sb.append(")\n");
            } catch (Exception unused) {
            }
            sb.append("التوقيت: ");
            sb.append(TimeZone.getDefault().getID());
            sb.append("\n═══════════════════════════════");
            write(context, "I", "DEVICE", sb.toString(), null);
        } catch (Exception e2) {
            write(context, ExifInterface.LONGITUDE_EAST, "DEVICE", "فشل جمع معلومات الجهاز", e2);
        }
    }

    public static void error(Context context, String str, String str2, Throwable th) {
        write(context, ExifInterface.LONGITUDE_EAST, str, str2, th);
    }

    private static File getLogFile(Context context) {
        return new File(context.getFilesDir(), LOG_FILE);
    }

    private static String getProp(String str) {
        try {
            byte[] bArr = new byte[128];
            int read = Runtime.getRuntime().exec(new String[]{"getprop", str}).getInputStream().read(bArr);
            return read > 0 ? new String(bArr, 0, read).trim() : "";
        } catch (Exception unused) {
            return "";
        }
    }

    public static void log(Context context, String str, String str2) {
        write(context, "I", str, str2, null);
    }

    public static String readLog(Context context) {
        try {
            File logFile = getLogFile(context);
            if (!logFile.exists()) {
                return "لا يوجد سجل بعد.";
            }
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(logFile));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    bufferedReader.close();
                    return sb.toString();
                }
                sb.append(readLine);
                sb.append("\n");
            }
        } catch (Exception e2) {
            return "خطأ في قراءة السجل: " + e2.getMessage();
        }
    }

    public static void shareLog(Context context) {
        try {
            File logFile = getLogFile(context);
            if (logFile.exists()) {
                Uri uriForFile = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", logFile);
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                intent.putExtra("android.intent.extra.SUBJECT", "سجل تشخيص تطبيق صلاتك");
                intent.addFlags(1);
                context.startActivity(Intent.createChooser(intent, "مشاركة سجل التشخيص"));
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private static void trimIfNeeded(File file) {
        try {
            if (file.exists()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                ArrayList arrayList = new ArrayList();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    } else {
                        arrayList.add(readLine);
                    }
                }
                bufferedReader.close();
                if (arrayList.size() > MAX_LINES) {
                    FileWriter fileWriter = new FileWriter(file, false);
                    for (int size = arrayList.size() - MAX_LINES; size < arrayList.size(); size++) {
                        fileWriter.write(((String) arrayList.get(size)) + "\n");
                    }
                    fileWriter.close();
                }
            }
        } catch (Exception unused) {
        }
    }

    public static void warn(Context context, String str, String str2) {
        write(context, ExifInterface.LONGITUDE_WEST, str, str2, null);
    }

    private static void write(Context context, String str, String str2, String str3, Throwable th) {
        try {
            File logFile = getLogFile(context);
            trimIfNeeded(logFile);
            FileWriter fileWriter = new FileWriter(logFile, true);
            fileWriter.write(FMT.format(new Date()) + " " + str + "/" + str2 + ": " + str3 + "\n");
            if (th != null) {
                StringWriter stringWriter = new StringWriter();
                th.printStackTrace(new PrintWriter(stringWriter));
                fileWriter.write(stringWriter.toString() + "\n");
            }
            fileWriter.close();
        } catch (Exception unused) {
        }
    }
}
