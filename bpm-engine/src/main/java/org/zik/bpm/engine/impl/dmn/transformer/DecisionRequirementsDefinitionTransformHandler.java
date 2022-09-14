// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.transformer;

import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionEntity;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionRequirementsGraphImpl;
import org.camunda.bpm.model.dmn.instance.Definitions;
import org.camunda.bpm.dmn.engine.impl.spi.transform.DmnElementTransformContext;
import org.camunda.bpm.dmn.engine.impl.transform.DmnDecisionRequirementsGraphTransformHandler;

public class DecisionRequirementsDefinitionTransformHandler extends DmnDecisionRequirementsGraphTransformHandler
{
    protected DmnDecisionRequirementsGraphImpl createFromDefinitions(final DmnElementTransformContext context, final Definitions definitions) {
        final DecisionRequirementsDefinitionEntity entity = (DecisionRequirementsDefinitionEntity)super.createFromDefinitions(context, definitions);
        entity.setCategory(definitions.getNamespace());
        return entity;
    }
    
    protected DmnDecisionRequirementsGraphImpl createDmnElement() {
        return new DecisionRequirementsDefinitionEntity();
    }
}
