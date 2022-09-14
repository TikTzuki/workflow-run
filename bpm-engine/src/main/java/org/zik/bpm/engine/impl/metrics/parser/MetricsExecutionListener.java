// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.metrics.parser;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.DelegateExecution;
import java.util.function.Function;
import org.zik.bpm.engine.delegate.ExecutionListener;

public class MetricsExecutionListener implements ExecutionListener
{
    protected String metricsName;
    protected Function<DelegateExecution, Boolean> condition;
    
    public MetricsExecutionListener(final String metricsName) {
        this(metricsName, delegateExecution -> true);
    }
    
    public MetricsExecutionListener(final String metricsName, final Function<DelegateExecution, Boolean> condition) {
        this.metricsName = metricsName;
        this.condition = condition;
    }
    
    @Override
    public void notify(final DelegateExecution execution) throws Exception {
        if (this.condition.apply(execution)) {
            Context.getProcessEngineConfiguration().getMetricsRegistry().markOccurrence(this.metricsName);
        }
    }
}
