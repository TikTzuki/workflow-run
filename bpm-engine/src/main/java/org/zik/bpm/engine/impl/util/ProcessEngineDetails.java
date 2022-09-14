// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

public class ProcessEngineDetails
{
    public static final String EDITION_ENTERPRISE = "enterprise";
    public static final String EDITION_COMMUNITY = "community";
    protected String version;
    protected String edition;
    
    public ProcessEngineDetails(final String version, final String edition) {
        this.version = version;
        this.edition = edition;
    }
    
    public String getVersion() {
        return this.version;
    }
    
    public void setVersion(final String version) {
        this.version = version;
    }
    
    public String getEdition() {
        return this.edition;
    }
    
    public void setEdition(final String edition) {
        this.edition = edition;
    }
}
