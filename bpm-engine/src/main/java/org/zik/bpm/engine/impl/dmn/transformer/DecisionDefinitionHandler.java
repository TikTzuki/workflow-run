// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.transformer;

import org.zik.bpm.engine.impl.util.ParseUtil;
import org.camunda.bpm.model.dmn.instance.Decision;
import org.camunda.bpm.dmn.engine.impl.spi.transform.DmnElementTransformContext;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionEntity;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionImpl;
import org.camunda.bpm.dmn.engine.impl.transform.DmnDecisionTransformHandler;

public class DecisionDefinitionHandler extends DmnDecisionTransformHandler
{
    protected DmnDecisionImpl createDmnElement() {
        return new DecisionDefinitionEntity();
    }
    
    protected DmnDecisionImpl createFromDecision(final DmnElementTransformContext context, final Decision decision) {
        final DecisionDefinitionEntity decisionDefinition = (DecisionDefinitionEntity)super.createFromDecision(context, decision);
        final String category = context.getModelInstance().getDefinitions().getNamespace();
        decisionDefinition.setCategory(category);
        decisionDefinition.setHistoryTimeToLive(ParseUtil.parseHistoryTimeToLive(decision.getCamundaHistoryTimeToLiveString()));
        decisionDefinition.setVersionTag(decision.getVersionTag());
        return decisionDefinition;
    }
}
