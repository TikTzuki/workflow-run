// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.helper;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.tree.ActivityExecutionTuple;
import org.zik.bpm.engine.impl.tree.ReferenceWalker;
import org.zik.bpm.engine.impl.tree.OutputVariablesPropagator;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.tree.TreeVisitor;
import org.zik.bpm.engine.impl.tree.ActivityExecutionHierarchyWalker;
import org.zik.bpm.engine.impl.tree.ActivityExecutionMappingCollector;
import org.zik.bpm.engine.impl.bpmn.parser.EscalationEventDefinition;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.bpmn.behavior.BpmnBehaviorLogger;

public class EscalationHandler
{
    private static final BpmnBehaviorLogger LOG;
    
    public static void propagateEscalation(final ActivityExecution execution, final String escalationCode) {
        final EscalationEventDefinition escalationEventDefinition = executeEscalation(execution, escalationCode);
        if (escalationEventDefinition == null) {
            throw EscalationHandler.LOG.missingBoundaryCatchEventEscalation(execution.getActivity().getId(), escalationCode);
        }
    }
    
    public static EscalationEventDefinition executeEscalation(final ActivityExecution execution, final String escalationCode) {
        final PvmActivity currentActivity = execution.getActivity();
        final EscalationEventDefinitionFinder escalationEventDefinitionFinder = new EscalationEventDefinitionFinder(escalationCode, currentActivity);
        final ActivityExecutionMappingCollector activityExecutionMappingCollector = new ActivityExecutionMappingCollector(execution);
        final ActivityExecutionHierarchyWalker walker = new ActivityExecutionHierarchyWalker(execution);
        walker.addScopePreVisitor(escalationEventDefinitionFinder);
        walker.addExecutionPreVisitor(activityExecutionMappingCollector);
        walker.addExecutionPreVisitor(new OutputVariablesPropagator());
        walker.walkUntil(new ReferenceWalker.WalkCondition<ActivityExecutionTuple>() {
            @Override
            public boolean isFulfilled(final ActivityExecutionTuple element) {
                return escalationEventDefinitionFinder.getEscalationEventDefinition() != null || element == null;
            }
        });
        final EscalationEventDefinition escalationEventDefinition = escalationEventDefinitionFinder.getEscalationEventDefinition();
        if (escalationEventDefinition != null) {
            executeEscalationHandler(escalationEventDefinition, activityExecutionMappingCollector, escalationCode);
        }
        return escalationEventDefinition;
    }
    
    protected static void executeEscalationHandler(final EscalationEventDefinition escalationEventDefinition, final ActivityExecutionMappingCollector activityExecutionMappingCollector, final String escalationCode) {
        final PvmActivity escalationHandler = escalationEventDefinition.getEscalationHandler();
        final PvmScope escalationScope = getScopeForEscalation(escalationEventDefinition);
        final ActivityExecution escalationExecution = activityExecutionMappingCollector.getExecutionForScope(escalationScope);
        if (escalationEventDefinition.getEscalationCodeVariable() != null) {
            escalationExecution.setVariable(escalationEventDefinition.getEscalationCodeVariable(), escalationCode);
        }
        escalationExecution.executeActivity(escalationHandler);
    }
    
    protected static PvmScope getScopeForEscalation(final EscalationEventDefinition escalationEventDefinition) {
        final PvmActivity escalationHandler = escalationEventDefinition.getEscalationHandler();
        if (escalationEventDefinition.isCancelActivity()) {
            return escalationHandler.getEventScope();
        }
        return escalationHandler.getFlowScope();
    }
    
    static {
        LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;
    }
}
