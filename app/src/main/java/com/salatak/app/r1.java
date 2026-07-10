package com.salatak.app;

import java.io.File;
import java.io.FilenameFilter;

/* loaded from: classes2.dex */
public final /* synthetic */ class r1 implements FilenameFilter {

    /* renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f509a;

    public /* synthetic */ r1(int i2) {
        this.f509a = i2;
    }

    @Override // java.io.FilenameFilter
    public final boolean accept(File file, String str) {
        boolean lambda$showReciterSurahs$5;
        boolean lambda$loadDownloadedReciters$1;
        boolean lambda$loadDownloadedReciters$3;
        switch (this.f509a) {
            case 0:
                lambda$showReciterSurahs$5 = QuranDownloadsActivity.lambda$showReciterSurahs$5(file, str);
                return lambda$showReciterSurahs$5;
            case 1:
                lambda$loadDownloadedReciters$1 = QuranDownloadsActivity.lambda$loadDownloadedReciters$1(file, str);
                return lambda$loadDownloadedReciters$1;
            default:
                lambda$loadDownloadedReciters$3 = QuranDownloadsActivity.lambda$loadDownloadedReciters$3(file, str);
                return lambda$loadDownloadedReciters$3;
        }
    }
}
