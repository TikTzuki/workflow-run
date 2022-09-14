// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.model;

import java.io.Serializable;

public class CmmnOnPartDeclaration implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected String standardEvent;
    protected CmmnActivity source;
    protected CmmnSentryDeclaration sentry;
    
    public String getStandardEvent() {
        return this.standardEvent;
    }
    
    public void setStandardEvent(final String standardEvent) {
        this.standardEvent = standardEvent;
    }
    
    public CmmnActivity getSource() {
        return this.source;
    }
    
    public void setSource(final CmmnActivity source) {
        this.source = source;
    }
    
    public CmmnSentryDeclaration getSentry() {
        return this.sentry;
    }
    
    public void setSentry(final CmmnSentryDeclaration sentry) {
        this.sentry = sentry;
    }
}
