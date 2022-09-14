// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.telemetry.dto;

import org.zik.bpm.engine.telemetry.Database;

public class DatabaseImpl implements Database
{
    protected String vendor;
    protected String version;
    
    public DatabaseImpl(final String vendor, final String version) {
        this.vendor = vendor;
        this.version = version;
    }
    
    @Override
    public String getVendor() {
        return this.vendor;
    }
    
    public void setVendor(final String vendor) {
        this.vendor = vendor;
    }
    
    @Override
    public String getVersion() {
        return this.version;
    }
    
    public void setVersion(final String version) {
        this.version = version;
    }
}
