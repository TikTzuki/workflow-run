// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.metrics.reporter.DbMetricsReporter;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class ReportDbMetricsCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final ProcessEngineConfigurationImpl engineConfiguration = Context.getProcessEngineConfiguration();
        if (!engineConfiguration.isMetricsEnabled()) {
            throw new ProcessEngineException("Metrics reporting is disabled");
        }
        if (!engineConfiguration.isDbMetricsReporterActivate()) {
            throw new ProcessEngineException("Metrics reporting to database is disabled");
        }
        final DbMetricsReporter dbMetricsReporter = engineConfiguration.getDbMetricsReporter();
        dbMetricsReporter.reportNow();
        return null;
    }
}
