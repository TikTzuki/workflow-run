// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.metrics.reporter;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.metrics.Meter;
import org.zik.bpm.engine.impl.persistence.entity.MeterLogEntity;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.metrics.MetricsRegistry;
import org.zik.bpm.engine.impl.metrics.MetricsLogger;
import java.util.TimerTask;

public class MetricsCollectionTask extends TimerTask
{
    private static final MetricsLogger LOG;
    protected MetricsRegistry metricsRegistry;
    protected CommandExecutor commandExecutor;
    protected String reporterId;
    
    public MetricsCollectionTask(final MetricsRegistry metricsRegistry, final CommandExecutor commandExecutor) {
        this.reporterId = null;
        this.metricsRegistry = metricsRegistry;
        this.commandExecutor = commandExecutor;
    }
    
    @Override
    public void run() {
        try {
            this.collectMetrics();
        }
        catch (Exception e) {
            try {
                MetricsCollectionTask.LOG.couldNotCollectAndLogMetrics(e);
            }
            catch (Exception ex) {}
        }
    }
    
    protected void collectMetrics() {
        final List<MeterLogEntity> logs = new ArrayList<MeterLogEntity>();
        for (final Meter meter : this.metricsRegistry.getDbMeters().values()) {
            logs.add(new MeterLogEntity(meter.getName(), this.reporterId, meter.getAndClear(), ClockUtil.getCurrentTime()));
        }
        this.commandExecutor.execute((Command<Object>)new MetricsCollectionCmd(logs));
    }
    
    public String getReporter() {
        return this.reporterId;
    }
    
    public void setReporter(final String reporterId) {
        this.reporterId = reporterId;
    }
    
    static {
        LOG = ProcessEngineLogger.METRICS_LOGGER;
    }
    
    protected class MetricsCollectionCmd implements Command<Void>
    {
        protected List<MeterLogEntity> logs;
        
        public MetricsCollectionCmd(final List<MeterLogEntity> logs) {
            this.logs = logs;
        }
        
        @Override
        public Void execute(final CommandContext commandContext) {
            for (final MeterLogEntity meterLogEntity : this.logs) {
                commandContext.getMeterLogManager().insert(meterLogEntity);
            }
            return null;
        }
    }
}
