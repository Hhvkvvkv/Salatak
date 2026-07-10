package com.batoulapps.adhan;

import com.batoulapps.adhan.internal.QiblaUtil;

/* loaded from: classes.dex */
public class Qibla {
    private static final Coordinates MAKKAH = new Coordinates(21.4225241d, 39.8261818d);
    public final double direction;

    public Qibla(Coordinates coordinates) {
        this.direction = QiblaUtil.calculateQiblaDirection(coordinates);
    }
}
