package com.salatak.app;

import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.Comparator;

/* loaded from: classes2.dex */
public final /* synthetic */ class s1 implements Comparator {
    public final /* synthetic */ int b;
    public final /* synthetic */ AppCompatActivity c;

    public /* synthetic */ s1(AppCompatActivity appCompatActivity, int i2) {
        this.b = i2;
        this.c = appCompatActivity;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$showReciterSurahs$6;
        int lambda$buildTrackList$9;
        int lambda$displaySurahs$1;
        switch (this.b) {
            case 0:
                lambda$showReciterSurahs$6 = ((QuranDownloadsActivity) this.c).lambda$showReciterSurahs$6((File) obj, (File) obj2);
                return lambda$showReciterSurahs$6;
            case 1:
                lambda$buildTrackList$9 = ((QuranPlayerActivity) this.c).lambda$buildTrackList$9((String[]) obj, (String[]) obj2);
                return lambda$buildTrackList$9;
            default:
                lambda$displaySurahs$1 = ((QuranSurahListActivity) this.c).lambda$displaySurahs$1((String[]) obj, (String[]) obj2);
                return lambda$displaySurahs$1;
        }
    }
}
