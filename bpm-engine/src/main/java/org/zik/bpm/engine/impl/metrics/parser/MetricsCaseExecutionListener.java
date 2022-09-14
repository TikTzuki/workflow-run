// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.metrics.parser;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.DelegateCaseExecution;
import org.zik.bpm.engine.delegate.CaseExecutionListener;

public class MetricsCaseExecutionListener implements CaseExecutionListener
{
    @Override
    public void notify(final DelegateCaseExecution caseExecution) throws Exception {
        Context.getProcessEngineConfiguration().getMetricsRegistry().markOccurrence("activity-instance-start");
    }
}
