// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.telemetry.dto;

import org.zik.bpm.engine.impl.util.ParseUtil;
import org.zik.bpm.engine.telemetry.ApplicationServer;

public class ApplicationServerImpl implements ApplicationServer
{
    protected String vendor;
    protected String version;
    
    public ApplicationServerImpl(final String vendor, final String version) {
        this.vendor = vendor;
        this.version = version;
    }
    
    public ApplicationServerImpl(final String version) {
        this.vendor = ParseUtil.parseServerVendor(version);
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
