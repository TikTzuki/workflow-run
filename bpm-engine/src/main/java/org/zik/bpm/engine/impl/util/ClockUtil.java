// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.joda.time.DateTimeUtils;
import java.util.Date;

public class ClockUtil
{
    public static void setCurrentTime(final Date currentTime) {
        DateTimeUtils.setCurrentMillisFixed(currentTime.getTime());
    }
    
    public static void reset() {
        resetClock();
    }
    
    public static Date getCurrentTime() {
        return now();
    }
    
    public static Date now() {
        return new Date(DateTimeUtils.currentTimeMillis());
    }
    
    public static Date offset(final Long offsetInMillis) {
        DateTimeUtils.setCurrentMillisOffset((long)offsetInMillis);
        return new Date(DateTimeUtils.currentTimeMillis());
    }
    
    public static Date resetClock() {
        DateTimeUtils.setCurrentMillisSystem();
        return new Date(DateTimeUtils.currentTimeMillis());
    }
}
