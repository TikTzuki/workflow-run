// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.calendar;

import java.util.TimeZone;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.DateTimeZone;

public class DateTimeUtil
{
    private static final DateTimeZone JVM_DEFAULT_DATE_TIME_ZONE;
    private static DateTimeFormatter DATE_TIME_FORMATER;
    
    private static DateTimeFormatter getDataTimeFormater() {
        if (DateTimeUtil.DATE_TIME_FORMATER == null) {
            DateTimeUtil.DATE_TIME_FORMATER = ISODateTimeFormat.dateTimeParser().withZone(DateTimeUtil.JVM_DEFAULT_DATE_TIME_ZONE);
        }
        return DateTimeUtil.DATE_TIME_FORMATER;
    }
    
    public static DateTime now() {
        return new DateTime(DateTimeUtil.JVM_DEFAULT_DATE_TIME_ZONE);
    }
    
    public static DateTime parseDateTime(final String date) {
        return getDataTimeFormater().parseDateTime(date);
    }
    
    public static Date parseDate(final String date) {
        return parseDateTime(date).toDate();
    }
    
    static {
        JVM_DEFAULT_DATE_TIME_ZONE = DateTimeZone.forTimeZone(TimeZone.getDefault());
    }
}
