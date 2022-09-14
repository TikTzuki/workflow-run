// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.management;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public class DatabasePurgeReport implements PurgeReporting<Long>
{
    Map<String, Long> deletedEntities;
    boolean dbContainsLicenseKey;
    
    public DatabasePurgeReport() {
        this.deletedEntities = new HashMap<String, Long>();
    }
    
    @Override
    public void addPurgeInformation(final String key, final Long value) {
        this.deletedEntities.put(key, value);
    }
    
    @Override
    public Map<String, Long> getPurgeReport() {
        return this.deletedEntities;
    }
    
    @Override
    public String getPurgeReportAsString() {
        final StringBuilder builder = new StringBuilder();
        for (final String key : this.deletedEntities.keySet()) {
            builder.append("Table: ").append(key).append(" contains: ").append(this.getReportValue(key)).append(" rows\n");
        }
        return builder.toString();
    }
    
    @Override
    public Long getReportValue(final String key) {
        return this.deletedEntities.get(key);
    }
    
    @Override
    public boolean containsReport(final String key) {
        return this.deletedEntities.containsKey(key);
    }
    
    @Override
    public boolean isEmpty() {
        return this.deletedEntities.isEmpty();
    }
    
    public boolean isDbContainsLicenseKey() {
        return this.dbContainsLicenseKey;
    }
    
    public void setDbContainsLicenseKey(final boolean dbContainsLicenseKey) {
        this.dbContainsLicenseKey = dbContainsLicenseKey;
    }
}
