// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.telemetry.dto;

import org.zik.bpm.engine.telemetry.Jdk;

public class JdkImpl implements Jdk
{
    protected String version;
    protected String vendor;
    
    public JdkImpl(final String version, final String vendor) {
        this.version = version;
        this.vendor = vendor;
    }
    
    @Override
    public String getVersion() {
        return this.version;
    }
    
    public void setVersion(final String version) {
        this.version = version;
    }
    
    @Override
    public String getVendor() {
        return this.vendor;
    }
    
    public void setVendor(final String vendor) {
        this.vendor = vendor;
    }
}
