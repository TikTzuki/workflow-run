// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.metrics.reporter;

import org.zik.bpm.engine.impl.persistence.entity.MeterLogEntity;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;
import java.util.TimerTask;
import java.util.Timer;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.metrics.MetricsRegistry;

public class DbMetricsReporter
{
    protected MetricsRegistry metricsRegistry;
    protected CommandExecutor commandExecutor;
    protected String reporterId;
    protected long reportingIntervalInSeconds;
    protected MetricsCollectionTask metricsCollectionTask;
    private Timer timer;
    
    public DbMetricsReporter(final MetricsRegistry metricsRegistry, final CommandExecutor commandExecutor) {
        this.reportingIntervalInSeconds = 900L;
        this.metricsRegistry = metricsRegistry;
        this.commandExecutor = commandExecutor;
        this.initMetricsCollectionTask();
    }
    
    protected void initMetricsCollectionTask() {
        this.metricsCollectionTask = new MetricsCollectionTask(this.metricsRegistry, this.commandExecutor);
    }
    
    public void start() {
        this.timer = new Timer("Camunda Metrics Reporter", true);
        final long reportingIntervalInMillis = this.reportingIntervalInSeconds * 1000L;
        this.timer.scheduleAtFixedRate(this.metricsCollectionTask, reportingIntervalInMillis, reportingIntervalInMillis);
    }
    
    public void stop() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
            this.reportNow();
        }
    }
    
    public void reportNow() {
        if (this.metricsCollectionTask != null) {
            this.metricsCollectionTask.run();
        }
    }
    
    public void reportValueAtOnce(final String name, final long value) {
        this.commandExecutor.execute(new ReportDbMetricsValueCmd(name, value));
    }
    
    public long getReportingIntervalInSeconds() {
        return this.reportingIntervalInSeconds;
    }
    
    public void setReportingIntervalInSeconds(final long reportingIntervalInSeconds) {
        this.reportingIntervalInSeconds = reportingIntervalInSeconds;
    }
    
    public MetricsRegistry getMetricsRegistry() {
        return this.metricsRegistry;
    }
    
    public CommandExecutor getCommandExecutor() {
        return this.commandExecutor;
    }
    
    public MetricsCollectionTask getMetricsCollectionTask() {
        return this.metricsCollectionTask;
    }
    
    public void setMetricsCollectionTask(final MetricsCollectionTask metricsCollectionTask) {
        this.metricsCollectionTask = metricsCollectionTask;
    }
    
    public void setReporterId(final String reporterId) {
        this.reporterId = reporterId;
        if (this.metricsCollectionTask != null) {
            this.metricsCollectionTask.setReporter(reporterId);
        }
    }
    
    protected class ReportDbMetricsValueCmd implements Command<Void>
    {
        protected String name;
        protected long value;
        
        public ReportDbMetricsValueCmd(final String name, final long value) {
            this.name = name;
            this.value = value;
        }
        
        @Override
        public Void execute(final CommandContext commandContext) {
            commandContext.getMeterLogManager().insert(new MeterLogEntity(this.name, DbMetricsReporter.this.reporterId, this.value, ClockUtil.getCurrentTime()));
            return null;
        }
    }
}
