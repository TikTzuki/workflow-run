// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.delegate;

import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import java.util.Map;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.pvm.process.TransitionImpl;
import org.zik.bpm.engine.impl.pvm.PvmTransition;
import java.util.List;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnCaseInstance;
import org.zik.bpm.engine.impl.cmmn.model.CmmnCaseDefinition;
import org.zik.bpm.engine.impl.pvm.PvmProcessInstance;
import org.zik.bpm.engine.impl.pvm.PvmProcessDefinition;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.delegate.DelegateExecution;

public interface ActivityExecution extends DelegateExecution
{
    PvmActivity getActivity();
    
    void enterActivityInstance();
    
    void leaveActivityInstance();
    
    void setActivityInstanceId(final String p0);
    
    String getActivityInstanceId();
    
    String getParentActivityInstanceId();
    
    ActivityExecution createExecution();
    
    PvmProcessInstance createSubProcessInstance(final PvmProcessDefinition p0);
    
    PvmProcessInstance createSubProcessInstance(final PvmProcessDefinition p0, final String p1);
    
    PvmProcessInstance createSubProcessInstance(final PvmProcessDefinition p0, final String p1, final String p2);
    
    CmmnCaseInstance createSubCaseInstance(final CmmnCaseDefinition p0);
    
    CmmnCaseInstance createSubCaseInstance(final CmmnCaseDefinition p0, final String p1);
    
    ActivityExecution getParent();
    
    List<? extends ActivityExecution> getExecutions();
    
    List<? extends ActivityExecution> getNonEventScopeExecutions();
    
    boolean hasChildren();
    
    void end(final boolean p0);
    
    void endCompensation();
    
    void setActive(final boolean p0);
    
    boolean isActive();
    
    boolean isEnded();
    
    void setConcurrent(final boolean p0);
    
    boolean isConcurrent();
    
    boolean isProcessInstanceExecution();
    
    void inactivate();
    
    boolean isScope();
    
    void setScope(final boolean p0);
    
    boolean isCompleteScope();
    
    List<ActivityExecution> findInactiveConcurrentExecutions(final PvmActivity p0);
    
    List<ActivityExecution> findInactiveChildExecutions(final PvmActivity p0);
    
    void leaveActivityViaTransitions(final List<PvmTransition> p0, final List<? extends ActivityExecution> p1);
    
    void leaveActivityViaTransition(final PvmTransition p0);
    
    void executeActivity(final PvmActivity p0);
    
    void interrupt(final String p0);
    
    PvmActivity getNextActivity();
    
    void remove();
    
    void destroy();
    
    void signal(final String p0, final Object p1);
    
    void setActivity(final PvmActivity p0);
    
    boolean tryPruneLastConcurrentChild();
    
    void forceUpdate();
    
    TransitionImpl getTransition();
    
    ActivityExecution findExecutionForFlowScope(final PvmScope p0);
    
    Map<ScopeImpl, PvmExecutionImpl> createActivityExecutionMapping();
    
    void setEnded(final boolean p0);
    
    void setIgnoreAsync(final boolean p0);
}
