package com.batoulapps.adhan;

import com.batoulapps.adhan.internal.ShadowLength;

/* loaded from: classes.dex */
public enum Madhab {
    SHAFI,
    HANAFI;

    /* renamed from: com.batoulapps.adhan.Madhab$1, reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$batoulapps$adhan$Madhab;

        static {
            int[] iArr = new int[Madhab.values().length];
            $SwitchMap$com$batoulapps$adhan$Madhab = iArr;
            try {
                iArr[Madhab.SHAFI.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$batoulapps$adhan$Madhab[Madhab.HANAFI.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public ShadowLength getShadowLength() {
        int i2 = AnonymousClass1.$SwitchMap$com$batoulapps$adhan$Madhab[ordinal()];
        if (i2 == 1) {
            return ShadowLength.SINGLE;
        }
        if (i2 == 2) {
            return ShadowLength.DOUBLE;
        }
        throw new IllegalArgumentException("Invalid Madhab");
    }
}
