// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.management;

import org.zik.bpm.engine.impl.persistence.deploy.cache.CachePurgeReport;

public class PurgeReport
{
    private DatabasePurgeReport databasePurgeReport;
    private CachePurgeReport cachePurgeReport;
    
    public DatabasePurgeReport getDatabasePurgeReport() {
        return this.databasePurgeReport;
    }
    
    public void setDatabasePurgeReport(final DatabasePurgeReport databasePurgeReport) {
        this.databasePurgeReport = databasePurgeReport;
    }
    
    public CachePurgeReport getCachePurgeReport() {
        return this.cachePurgeReport;
    }
    
    public void setCachePurgeReport(final CachePurgeReport cachePurgeReport) {
        this.cachePurgeReport = cachePurgeReport;
    }
    
    public boolean isEmpty() {
        return this.cachePurgeReport.isEmpty() && this.databasePurgeReport.isEmpty();
    }
}
