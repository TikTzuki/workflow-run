// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.interceptor.Command;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import org.zik.bpm.engine.runtime.TransitionInstance;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import java.util.HashSet;
import java.util.Set;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.runtime.ActivityInstance;

public class ActivityCancellationCmd extends AbstractProcessInstanceModificationCommand
{
    protected String activityId;
    protected boolean cancelCurrentActiveActivityInstances;
    protected ActivityInstance activityInstanceTree;
    
    public ActivityCancellationCmd(final String activityId) {
        this(null, activityId);
    }
    
    public ActivityCancellationCmd(final String processInstanceId, final String activityId) {
        super(processInstanceId);
        this.activityId = activityId;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final ActivityInstance activityInstanceTree = this.getActivityInstanceTree(commandContext);
        final List<AbstractInstanceCancellationCmd> commands = this.createActivityInstanceCancellations(activityInstanceTree, commandContext);
        for (final AbstractInstanceCancellationCmd cmd : commands) {
            cmd.setSkipCustomListeners(this.skipCustomListeners);
            cmd.setSkipIoMappings(this.skipIoMappings);
            cmd.execute(commandContext);
        }
        return null;
    }
    
    protected Set<String> collectParentScopeIdsForActivity(final ProcessDefinitionImpl processDefinition, final String activityId) {
        final Set<String> parentScopeIds = new HashSet<String>();
        for (ScopeImpl scope = processDefinition.findActivity(activityId); scope != null; scope = scope.getFlowScope()) {
            parentScopeIds.add(scope.getId());
        }
        return parentScopeIds;
    }
    
    protected List<TransitionInstance> getTransitionInstancesForActivity(final ActivityInstance tree, final Set<String> parentScopeIds) {
        if (!parentScopeIds.contains(tree.getActivityId())) {
            return Collections.emptyList();
        }
        final List<TransitionInstance> instances = new ArrayList<TransitionInstance>();
        final TransitionInstance[] childTransitionInstances;
        final TransitionInstance[] transitionInstances = childTransitionInstances = tree.getChildTransitionInstances();
        for (final TransitionInstance transitionInstance : childTransitionInstances) {
            if (this.activityId.equals(transitionInstance.getActivityId())) {
                instances.add(transitionInstance);
            }
        }
        for (final ActivityInstance child : tree.getChildActivityInstances()) {
            instances.addAll(this.getTransitionInstancesForActivity(child, parentScopeIds));
        }
        return instances;
    }
    
    protected List<ActivityInstance> getActivityInstancesForActivity(final ActivityInstance tree, final Set<String> parentScopeIds) {
        if (!parentScopeIds.contains(tree.getActivityId())) {
            return Collections.emptyList();
        }
        final List<ActivityInstance> instances = new ArrayList<ActivityInstance>();
        if (this.activityId.equals(tree.getActivityId())) {
            instances.add(tree);
        }
        for (final ActivityInstance child : tree.getChildActivityInstances()) {
            instances.addAll(this.getActivityInstancesForActivity(child, parentScopeIds));
        }
        return instances;
    }
    
    public ActivityInstance getActivityInstanceTree(final CommandContext commandContext) {
        return commandContext.runWithoutAuthorization((Command<ActivityInstance>)new GetActivityInstanceCmd(this.processInstanceId));
    }
    
    public String getActivityId() {
        return this.activityId;
    }
    
    public void setActivityInstanceTreeToCancel(final ActivityInstance activityInstanceTreeToCancel) {
        this.activityInstanceTree = activityInstanceTreeToCancel;
    }
    
    @Override
    protected String describe() {
        return "Cancel all instances of activity '" + this.activityId + "'";
    }
    
    public List<AbstractInstanceCancellationCmd> createActivityInstanceCancellations(final ActivityInstance activityInstanceTree, final CommandContext commandContext) {
        final List<AbstractInstanceCancellationCmd> commands = new ArrayList<AbstractInstanceCancellationCmd>();
        final ExecutionEntity processInstance = commandContext.getExecutionManager().findExecutionById(this.processInstanceId);
        final ProcessDefinitionImpl processDefinition = processInstance.getProcessDefinition();
        final Set<String> parentScopeIds = this.collectParentScopeIdsForActivity(processDefinition, this.activityId);
        final List<ActivityInstance> childrenForActivity = this.getActivityInstancesForActivity(activityInstanceTree, parentScopeIds);
        for (final ActivityInstance instance : childrenForActivity) {
            commands.add(new ActivityInstanceCancellationCmd(this.processInstanceId, instance.getId()));
        }
        final List<TransitionInstance> transitionInstancesForActivity = this.getTransitionInstancesForActivity(activityInstanceTree, parentScopeIds);
        for (final TransitionInstance instance2 : transitionInstancesForActivity) {
            commands.add(new TransitionInstanceCancellationCmd(this.processInstanceId, instance2.getId()));
        }
        return commands;
    }
    
    public boolean isCancelCurrentActiveActivityInstances() {
        return this.cancelCurrentActiveActivityInstances;
    }
    
    public void setCancelCurrentActiveActivityInstances(final boolean cancelCurrentActiveActivityInstances) {
        this.cancelCurrentActiveActivityInstances = cancelCurrentActiveActivityInstances;
    }
}
