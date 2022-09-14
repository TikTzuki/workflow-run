// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

import org.zik.bpm.engine.impl.telemetry.TelemetryRegistry;
import org.zik.bpm.engine.impl.util.ClassNameUtil;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;

public class CommandCounterInterceptor extends CommandInterceptor
{
    protected ProcessEngineConfigurationImpl processEngineConfiguration;
    
    public CommandCounterInterceptor(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
    
    @Override
    public <T> T execute(final Command<T> command) {
        try {
            return this.next.execute(command);
        }
        finally {
            final TelemetryRegistry telemetryRegistry = this.processEngineConfiguration.getTelemetryRegistry();
            if (telemetryRegistry != null) {
                String className = ClassNameUtil.getClassNameWithoutPackage(command);
                if (!command.getClass().isAnonymousClass() && !className.contains("$$Lambda$")) {
                    className = this.parseLocalClassName(className);
                    telemetryRegistry.markOccurrence(className);
                }
            }
        }
    }
    
    protected String parseLocalClassName(final String className) {
        return className.replace("$", "_");
    }
}
