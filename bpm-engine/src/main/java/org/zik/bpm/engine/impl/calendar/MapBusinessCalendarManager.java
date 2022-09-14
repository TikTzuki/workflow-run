// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.calendar;

import java.util.HashMap;
import java.util.Map;

public class MapBusinessCalendarManager implements BusinessCalendarManager
{
    private Map<String, BusinessCalendar> businessCalendars;
    
    public MapBusinessCalendarManager() {
        this.businessCalendars = new HashMap<String, BusinessCalendar>();
    }
    
    @Override
    public BusinessCalendar getBusinessCalendar(final String businessCalendarRef) {
        return this.businessCalendars.get(businessCalendarRef);
    }
    
    public BusinessCalendarManager addBusinessCalendar(final String businessCalendarRef, final BusinessCalendar businessCalendar) {
        this.businessCalendars.put(businessCalendarRef, businessCalendar);
        return this;
    }
}
