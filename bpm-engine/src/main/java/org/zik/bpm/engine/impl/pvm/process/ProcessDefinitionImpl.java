// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.process;

import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.core.delegate.CoreActivityBehavior;
import java.util.Iterator;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.pvm.runtime.ExecutionImpl;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.PvmProcessInstance;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.pvm.PvmProcessDefinition;

public class ProcessDefinitionImpl extends ScopeImpl implements PvmProcessDefinition
{
    private static final long serialVersionUID = 1L;
    protected String name;
    protected String description;
    protected ActivityImpl initial;
    protected Map<ActivityImpl, List<ActivityImpl>> initialActivityStacks;
    protected List<LaneSet> laneSets;
    protected ParticipantProcess participantProcess;
    
    public ProcessDefinitionImpl(final String id) {
        super(id, null);
        this.initialActivityStacks = new HashMap<ActivityImpl, List<ActivityImpl>>();
        this.processDefinition = this;
        this.isSubProcessScope = true;
    }
    
    protected void ensureDefaultInitialExists() {
        EnsureUtil.ensureNotNull("Process '" + this.name + "' has no default start activity (e.g. none start event), hence you cannot use 'startProcessInstanceBy...' but have to start it using one of the modeled start events (e.g. message start events)", "initial", this.initial);
    }
    
    @Override
    public PvmProcessInstance createProcessInstance() {
        this.ensureDefaultInitialExists();
        return this.createProcessInstance(null, null, this.initial);
    }
    
    @Override
    public PvmProcessInstance createProcessInstance(final String businessKey) {
        this.ensureDefaultInitialExists();
        return this.createProcessInstance(businessKey, null, this.initial);
    }
    
    @Override
    public PvmProcessInstance createProcessInstance(final String businessKey, final String caseInstanceId) {
        this.ensureDefaultInitialExists();
        return this.createProcessInstance(businessKey, caseInstanceId, this.initial);
    }
    
    public PvmProcessInstance createProcessInstance(final String businessKey, final ActivityImpl initial) {
        return this.createProcessInstance(businessKey, null, initial);
    }
    
    public PvmProcessInstance createProcessInstance(final String businessKey, final String caseInstanceId, final ActivityImpl initial) {
        final PvmExecutionImpl processInstance = (PvmExecutionImpl)this.createProcessInstanceForInitial(initial);
        processInstance.setBusinessKey(businessKey);
        processInstance.setCaseInstanceId(caseInstanceId);
        return processInstance;
    }
    
    public PvmProcessInstance createProcessInstanceForInitial(final ActivityImpl initial) {
        EnsureUtil.ensureNotNull("Cannot start process instance, initial activity where the process instance should start is null", "initial", initial);
        final PvmExecutionImpl processInstance = this.newProcessInstance();
        processInstance.setStarting(true);
        processInstance.setProcessDefinition(this);
        processInstance.setProcessInstance(processInstance);
        processInstance.setActivity(initial);
        return processInstance;
    }
    
    protected PvmExecutionImpl newProcessInstance() {
        return new ExecutionImpl();
    }
    
    public List<ActivityImpl> getInitialActivityStack() {
        return this.getInitialActivityStack(this.initial);
    }
    
    public synchronized List<ActivityImpl> getInitialActivityStack(final ActivityImpl startActivity) {
        List<ActivityImpl> initialActivityStack = this.initialActivityStacks.get(startActivity);
        if (initialActivityStack == null) {
            initialActivityStack = new ArrayList<ActivityImpl>();
            for (ActivityImpl activity = startActivity; activity != null; activity = activity.getParentFlowScopeActivity()) {
                initialActivityStack.add(0, activity);
            }
            this.initialActivityStacks.put(startActivity, initialActivityStack);
        }
        return initialActivityStack;
    }
    
    @Override
    public String getDiagramResourceName() {
        return null;
    }
    
    @Override
    public String getDeploymentId() {
        return null;
    }
    
    public void addLaneSet(final LaneSet newLaneSet) {
        this.getLaneSets().add(newLaneSet);
    }
    
    public Lane getLaneForId(final String id) {
        if (this.laneSets != null && this.laneSets.size() > 0) {
            for (final LaneSet set : this.laneSets) {
                final Lane lane = set.getLaneForId(id);
                if (lane != null) {
                    return lane;
                }
            }
        }
        return null;
    }
    
    @Override
    public CoreActivityBehavior<? extends BaseDelegateExecution> getActivityBehavior() {
        return null;
    }
    
    @Override
    public ActivityImpl getInitial() {
        return this.initial;
    }
    
    public void setInitial(final ActivityImpl initial) {
        this.initial = initial;
    }
    
    @Override
    public String toString() {
        return "ProcessDefinition(" + this.id + ")";
    }
    
    @Override
    public String getDescription() {
        return (String)this.getProperty("documentation");
    }
    
    public List<LaneSet> getLaneSets() {
        if (this.laneSets == null) {
            this.laneSets = new ArrayList<LaneSet>();
        }
        return this.laneSets;
    }
    
    public void setParticipantProcess(final ParticipantProcess participantProcess) {
        this.participantProcess = participantProcess;
    }
    
    public ParticipantProcess getParticipantProcess() {
        return this.participantProcess;
    }
    
    @Override
    public boolean isScope() {
        return true;
    }
    
    @Override
    public PvmScope getEventScope() {
        return null;
    }
    
    @Override
    public ScopeImpl getFlowScope() {
        return null;
    }
    
    @Override
    public PvmScope getLevelOfSubprocessScope() {
        return null;
    }
    
    @Override
    public boolean isSubProcessScope() {
        return true;
    }
}
