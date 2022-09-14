// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.handler;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.core.handler.HandlerContext;
import org.camunda.bpm.model.cmmn.Query;
import org.camunda.bpm.model.cmmn.instance.ExtensionElements;
import java.util.ArrayList;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.cmmn.VariableTransition;
import org.zik.bpm.engine.impl.cmmn.model.CmmnVariableOnPartDeclaration;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.zik.bpm.engine.impl.cmmn.model.CmmnIfPartDeclaration;
import org.camunda.bpm.model.cmmn.instance.ConditionExpression;
import org.camunda.bpm.model.cmmn.PlanItemTransition;
import org.camunda.bpm.model.cmmn.instance.PlanItem;
import org.zik.bpm.engine.impl.cmmn.model.CmmnOnPartDeclaration;
import org.camunda.bpm.model.cmmn.instance.CaseFileItemOnPart;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import java.util.Iterator;
import java.util.List;
import org.camunda.bpm.model.cmmn.instance.IfPart;
import java.util.Collection;
import org.camunda.bpm.model.cmmn.instance.PlanItemOnPart;
import org.camunda.bpm.model.cmmn.instance.OnPart;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;
import org.camunda.bpm.model.cmmn.instance.camunda.CamundaVariableOnPart;
import org.zik.bpm.engine.impl.cmmn.transformer.CmmnTransformerLogger;
import org.zik.bpm.engine.impl.cmmn.model.CmmnSentryDeclaration;
import org.camunda.bpm.model.cmmn.instance.Sentry;

public class SentryHandler extends CmmnElementHandler<Sentry, CmmnSentryDeclaration>
{
    protected static final CmmnTransformerLogger LOG;
    
    @Override
    public CmmnSentryDeclaration handleElement(final Sentry element, final CmmnHandlerContext context) {
        final String id = element.getId();
        final Collection<OnPart> onParts = (Collection<OnPart>)element.getOnParts();
        final IfPart ifPart = element.getIfPart();
        final List<CamundaVariableOnPart> variableOnParts = this.queryExtensionElementsByClass((CmmnElement)element, CamundaVariableOnPart.class);
        if ((ifPart == null || ifPart.getConditions().isEmpty()) && variableOnParts.isEmpty()) {
            if (onParts == null || onParts.isEmpty()) {
                SentryHandler.LOG.ignoredSentryWithMissingCondition(id);
                return null;
            }
            boolean atLeastOneOnPartsValid = false;
            for (final OnPart onPart : onParts) {
                if (onPart instanceof PlanItemOnPart) {
                    final PlanItemOnPart planItemOnPart = (PlanItemOnPart)onPart;
                    if (planItemOnPart.getSource() != null && planItemOnPart.getStandardEvent() != null) {
                        atLeastOneOnPartsValid = true;
                        break;
                    }
                    continue;
                }
            }
            if (!atLeastOneOnPartsValid) {
                SentryHandler.LOG.ignoredSentryWithInvalidParts(id);
                return null;
            }
        }
        final CmmnSentryDeclaration sentryDeclaration = new CmmnSentryDeclaration(id);
        this.initializeIfPart(ifPart, sentryDeclaration, context);
        this.initializeVariableOnParts((CmmnElement)element, sentryDeclaration, context, variableOnParts);
        final CmmnActivity parent = context.getParent();
        if (parent != null) {
            parent.addSentry(sentryDeclaration);
        }
        return sentryDeclaration;
    }
    
    public void initializeOnParts(final Sentry sentry, final CmmnHandlerContext context) {
        final Collection<OnPart> onParts = (Collection<OnPart>)sentry.getOnParts();
        for (final OnPart onPart : onParts) {
            if (onPart instanceof PlanItemOnPart) {
                this.initializeOnPart((PlanItemOnPart)onPart, sentry, context);
            }
            else {
                this.initializeOnPart((CaseFileItemOnPart)onPart, sentry, context);
            }
        }
    }
    
    protected void initializeOnPart(final PlanItemOnPart onPart, final Sentry sentry, final CmmnHandlerContext context) {
        final CmmnActivity parent = context.getParent();
        final String sentryId = sentry.getId();
        final CmmnSentryDeclaration sentryDeclaration = parent.getSentry(sentryId);
        final PlanItem source = onPart.getSource();
        final PlanItemTransition standardEvent = onPart.getStandardEvent();
        if (source != null && standardEvent != null) {
            final CmmnOnPartDeclaration onPartDeclaration = new CmmnOnPartDeclaration();
            final String standardEventName = standardEvent.name();
            onPartDeclaration.setStandardEvent(standardEventName);
            final String sourceId = source.getId();
            final CmmnActivity sourceActivity = parent.findActivity(sourceId);
            if (sourceActivity != null) {
                onPartDeclaration.setSource(sourceActivity);
            }
            final Sentry sentryRef = onPart.getSentry();
            if (sentryRef != null) {
                final String sentryRefId = sentryRef.getId();
                final CmmnSentryDeclaration sentryRefDeclaration = parent.getSentry(sentryRefId);
                onPartDeclaration.setSentry(sentryRefDeclaration);
            }
            sentryDeclaration.addOnPart(onPartDeclaration);
        }
    }
    
    protected void initializeOnPart(final CaseFileItemOnPart onPart, final Sentry sentry, final CmmnHandlerContext context) {
        final String id = sentry.getId();
        SentryHandler.LOG.ignoredUnsupportedAttribute("onPart", "CaseFileItem", id);
    }
    
    protected void initializeIfPart(final IfPart ifPart, final CmmnSentryDeclaration sentryDeclaration, final CmmnHandlerContext context) {
        if (ifPart == null) {
            return;
        }
        final Collection<ConditionExpression> conditions = (Collection<ConditionExpression>)ifPart.getConditions();
        if (conditions.size() > 1) {
            final String id = sentryDeclaration.getId();
            SentryHandler.LOG.multipleIgnoredConditions(id);
        }
        final ExpressionManager expressionManager = context.getExpressionManager();
        final ConditionExpression condition = conditions.iterator().next();
        final Expression conditionExpression = expressionManager.createExpression(condition.getText());
        final CmmnIfPartDeclaration ifPartDeclaration = new CmmnIfPartDeclaration();
        ifPartDeclaration.setCondition(conditionExpression);
        sentryDeclaration.setIfPart(ifPartDeclaration);
    }
    
    protected void initializeVariableOnParts(final CmmnElement element, final CmmnSentryDeclaration sentryDeclaration, final CmmnHandlerContext context, final List<CamundaVariableOnPart> variableOnParts) {
        for (final CamundaVariableOnPart variableOnPart : variableOnParts) {
            this.initializeVariableOnPart(variableOnPart, sentryDeclaration, context);
        }
    }
    
    protected void initializeVariableOnPart(final CamundaVariableOnPart variableOnPart, final CmmnSentryDeclaration sentryDeclaration, final CmmnHandlerContext context) {
        VariableTransition variableTransition;
        try {
            variableTransition = variableOnPart.getVariableEvent();
        }
        catch (IllegalArgumentException illegalArgumentexception) {
            throw SentryHandler.LOG.nonMatchingVariableEvents(sentryDeclaration.getId());
        }
        catch (NullPointerException nullPointerException) {
            throw SentryHandler.LOG.nonMatchingVariableEvents(sentryDeclaration.getId());
        }
        final String variableName = variableOnPart.getVariableName();
        final String variableEventName = variableTransition.name();
        if (variableName != null) {
            if (!sentryDeclaration.hasVariableOnPart(variableEventName, variableName)) {
                final CmmnVariableOnPartDeclaration variableOnPartDeclaration = new CmmnVariableOnPartDeclaration();
                variableOnPartDeclaration.setVariableEvent(variableEventName);
                variableOnPartDeclaration.setVariableName(variableName);
                sentryDeclaration.addVariableOnParts(variableOnPartDeclaration);
            }
            return;
        }
        throw SentryHandler.LOG.emptyVariableName(sentryDeclaration.getId());
    }
    
    protected <V extends ModelElementInstance> List<V> queryExtensionElementsByClass(final CmmnElement element, final Class<V> cls) {
        final ExtensionElements extensionElements = element.getExtensionElements();
        if (extensionElements != null) {
            final Query<ModelElementInstance> query = (Query<ModelElementInstance>)extensionElements.getElementsQuery();
            return (List<V>)query.filterByType((Class)cls).list();
        }
        return new ArrayList<V>();
    }
    
    static {
        LOG = ProcessEngineLogger.CMMN_TRANSFORMER_LOGGER;
    }
}
