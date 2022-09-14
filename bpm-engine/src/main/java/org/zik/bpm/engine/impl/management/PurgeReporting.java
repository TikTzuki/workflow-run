// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.management;

import java.util.Map;

public interface PurgeReporting<T>
{
    void addPurgeInformation(final String p0, final T p1);
    
    Map<String, T> getPurgeReport();
    
    String getPurgeReportAsString();
    
    T getReportValue(final String p0);
    
    boolean containsReport(final String p0);
    
    boolean isEmpty();
}
