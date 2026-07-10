package com.batoulapps.adhan.internal;

/* loaded from: classes.dex */
public enum ShadowLength {
    SINGLE(1.0d),
    DOUBLE(2.0d);

    private final double shadowLength;

    ShadowLength(double d) {
        this.shadowLength = d;
    }

    public double getShadowLength() {
        return this.shadowLength;
    }
}
