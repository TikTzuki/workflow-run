// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.metrics;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.management.MetricIntervalValue;
import java.util.List;
import org.zik.bpm.engine.impl.metrics.util.MetricsUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Date;
import org.zik.bpm.engine.management.MetricsQuery;
import org.zik.bpm.engine.impl.interceptor.Command;
import java.io.Serializable;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;

public class MetricsQueryImpl extends ListQueryParameterObject implements Serializable, Command<Object>, MetricsQuery
{
    public static final int DEFAULT_LIMIT_SELECT_INTERVAL = 200;
    public static final long DEFAULT_SELECT_INTERVAL = 900L;
    private static final long serialVersionUID = 1L;
    protected String name;
    protected String reporter;
    protected Date startDate;
    protected Date endDate;
    protected Long startDateMilliseconds;
    protected Long endDateMilliseconds;
    protected Long interval;
    protected Boolean aggregateByReporter;
    protected transient CommandExecutor commandExecutor;
    protected Command<Object> callback;
    
    public MetricsQueryImpl(final CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
        this.maxResults = 200;
        this.interval = 900L;
    }
    
    @Override
    public MetricsQueryImpl name(final String name) {
        this.name = MetricsUtil.resolveInternalName(name);
        return this;
    }
    
    @Override
    public MetricsQuery reporter(final String reporter) {
        this.reporter = reporter;
        return this;
    }
    
    @Override
    public MetricsQueryImpl startDate(final Date startDate) {
        this.startDate = startDate;
        this.startDateMilliseconds = startDate.getTime();
        return this;
    }
    
    @Override
    public MetricsQueryImpl endDate(final Date endDate) {
        this.endDate = endDate;
        this.endDateMilliseconds = endDate.getTime();
        return this;
    }
    
    @Override
    public List<MetricIntervalValue> interval() {
        this.callback = new MetricsQueryIntervalCmd(this);
        return this.commandExecutor.execute((Command<List<MetricIntervalValue>>)this);
    }
    
    @Override
    public List<MetricIntervalValue> interval(final long interval) {
        this.interval = interval;
        return this.interval();
    }
    
    @Override
    public long sum() {
        this.callback = new MetricsQuerySumCmd(this);
        return this.commandExecutor.execute((Command<Long>)this);
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        if (this.callback != null) {
            return this.callback.execute(commandContext);
        }
        throw new ProcessEngineException("Query can't be executed. Use either sum or interval to query the metrics.");
    }
    
    @Override
    public MetricsQuery offset(final int offset) {
        this.setFirstResult(offset);
        return this;
    }
    
    @Override
    public MetricsQuery limit(final int maxResults) {
        this.setMaxResults(maxResults);
        return this;
    }
    
    @Override
    public MetricsQuery aggregateByReporter() {
        this.aggregateByReporter = true;
        return this;
    }
    
    @Override
    public void setMaxResults(final int maxResults) {
        if (maxResults > 200) {
            throw new ProcessEngineException("Metrics interval query row limit can't be set larger than 200.");
        }
        this.maxResults = maxResults;
    }
    
    public Date getStartDate() {
        return this.startDate;
    }
    
    public Date getEndDate() {
        return this.endDate;
    }
    
    public Long getStartDateMilliseconds() {
        return this.startDateMilliseconds;
    }
    
    public Long getEndDateMilliseconds() {
        return this.endDateMilliseconds;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getReporter() {
        return this.reporter;
    }
    
    public Long getInterval() {
        if (this.interval == null) {
            return 900L;
        }
        return this.interval;
    }
    
    @Override
    public int getMaxResults() {
        if (this.maxResults > 200) {
            return 200;
        }
        return super.getMaxResults();
    }
    
    protected class MetricsQueryIntervalCmd implements Command<Object>
    {
        protected MetricsQueryImpl metricsQuery;
        
        public MetricsQueryIntervalCmd(final MetricsQueryImpl metricsQuery) {
            this.metricsQuery = metricsQuery;
        }
        
        @Override
        public Object execute(final CommandContext commandContext) {
            return commandContext.getMeterLogManager().executeSelectInterval(this.metricsQuery);
        }
    }
    
    protected class MetricsQuerySumCmd implements Command<Object>
    {
        protected MetricsQueryImpl metricsQuery;
        
        public MetricsQuerySumCmd(final MetricsQueryImpl metricsQuery) {
            this.metricsQuery = metricsQuery;
        }
        
        @Override
        public Object execute(final CommandContext commandContext) {
            return commandContext.getMeterLogManager().executeSelectSum(this.metricsQuery);
        }
    }
}
