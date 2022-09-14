// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import java.net.InetAddress;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class SimpleIpBasedProvider implements HostnameProvider
{
    private static final ProcessEngineLogger LOG;
    
    @Override
    public String getHostname(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        String localIp = "";
        try {
            localIp = InetAddress.getLocalHost().getHostAddress();
        }
        catch (Exception e) {
            SimpleIpBasedProvider.LOG.couldNotDetermineIp(e);
        }
        return createId(localIp, processEngineConfiguration.getProcessEngineName());
    }
    
    public static final String createId(final String ip, final String engineName) {
        return ip + "$" + engineName;
    }
    
    static {
        LOG = ProcessEngineLogger.INSTANCE;
    }
}
