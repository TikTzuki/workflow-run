// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg;

public class MailServerInfo
{
    protected String mailServerDefaultFrom;
    protected String mailServerHost;
    protected int mailServerPort;
    protected String mailServerUsername;
    protected String mailServerPassword;
    
    public String getMailServerDefaultFrom() {
        return this.mailServerDefaultFrom;
    }
    
    public void setMailServerDefaultFrom(final String mailServerDefaultFrom) {
        this.mailServerDefaultFrom = mailServerDefaultFrom;
    }
    
    public String getMailServerHost() {
        return this.mailServerHost;
    }
    
    public void setMailServerHost(final String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }
    
    public int getMailServerPort() {
        return this.mailServerPort;
    }
    
    public void setMailServerPort(final int mailServerPort) {
        this.mailServerPort = mailServerPort;
    }
    
    public String getMailServerUsername() {
        return this.mailServerUsername;
    }
    
    public void setMailServerUsername(final String mailServerUsername) {
        this.mailServerUsername = mailServerUsername;
    }
    
    public String getMailServerPassword() {
        return this.mailServerPassword;
    }
    
    public void setMailServerPassword(final String mailServerPassword) {
        this.mailServerPassword = mailServerPassword;
    }
}
