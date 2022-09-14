// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.metrics.dmn;

import org.zik.bpm.engine.impl.metrics.MetricsRegistry;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationEvent;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationListener;

public class MetricsDecisionEvaluationListener implements DmnDecisionEvaluationListener
{
    public void notify(final DmnDecisionEvaluationEvent evaluationEvent) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        if (processEngineConfiguration != null && processEngineConfiguration.isMetricsEnabled()) {
            final MetricsRegistry metricsRegistry = processEngineConfiguration.getMetricsRegistry();
            metricsRegistry.markOccurrence("executed-decision-instances", evaluationEvent.getExecutedDecisionInstances());
            metricsRegistry.markOccurrence("executed-decision-elements", evaluationEvent.getExecutedDecisionElements());
        }
    }
}
