// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.handler;

import org.zik.bpm.engine.impl.cmmn.model.CmmnSentryDeclaration;
import java.util.Iterator;
import java.util.Collection;
import org.camunda.bpm.model.cmmn.instance.Sentry;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.model.cmmn.instance.CasePlanModel;
import java.util.List;
import org.camunda.bpm.model.cmmn.instance.PlanItemDefinition;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;

public class CasePlanModelHandler extends StageItemHandler
{
    @Override
    protected PlanItemDefinition getDefinition(final CmmnElement element) {
        return (PlanItemDefinition)element;
    }
    
    @Override
    protected List<String> getStandardEvents(final CmmnElement element) {
        return CasePlanModelHandler.CASE_PLAN_MODEL_EVENTS;
    }
    
    public void initializeExitCriterias(final CasePlanModel casePlanModel, final CmmnActivity activity, final CmmnHandlerContext context) {
        final Collection<Sentry> exitCriterias = (Collection<Sentry>)casePlanModel.getExitCriteria();
        for (final Sentry sentry : exitCriterias) {
            final String sentryId = sentry.getId();
            final CmmnSentryDeclaration sentryDeclaration = activity.getSentry(sentryId);
            activity.addExitCriteria(sentryDeclaration);
        }
    }
}
