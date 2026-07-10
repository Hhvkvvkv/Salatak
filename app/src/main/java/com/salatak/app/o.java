package com.salatak.app;

import java.io.File;
import java.io.FileFilter;

/* loaded from: classes2.dex */
public final /* synthetic */ class o implements FileFilter {
    @Override // java.io.FileFilter
    public final boolean accept(File file) {
        return file.isDirectory();
    }
}
