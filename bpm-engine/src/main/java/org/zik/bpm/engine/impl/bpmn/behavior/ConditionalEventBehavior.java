// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.core.variable.event.VariableEvent;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.impl.bpmn.parser.ConditionalEventDefinition;

public interface ConditionalEventBehavior
{
    ConditionalEventDefinition getConditionalEventDefinition();
    
    void leaveOnSatisfiedCondition(final EventSubscriptionEntity p0, final VariableEvent p1);
}
