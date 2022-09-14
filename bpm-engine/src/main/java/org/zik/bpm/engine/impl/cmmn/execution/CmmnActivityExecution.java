// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.execution;

import org.zik.bpm.engine.impl.cmmn.model.CmmnCaseDefinition;
import org.zik.bpm.engine.impl.pvm.PvmProcessInstance;
import org.zik.bpm.engine.impl.pvm.PvmProcessDefinition;
import org.zik.bpm.engine.task.Task;
import org.zik.bpm.engine.impl.task.TaskDecorator;
import java.util.List;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.zik.bpm.engine.delegate.DelegateCaseExecution;

public interface CmmnActivityExecution extends DelegateCaseExecution
{
    CmmnActivityExecution getParent();
    
    boolean isCaseInstanceExecution();
    
    CaseExecutionState getCurrentState();
    
    void setCurrentState(final CaseExecutionState p0);
    
    CaseExecutionState getPreviousState();
    
    boolean isNew();
    
    boolean isTerminating();
    
    boolean isSuspending();
    
    CmmnActivity getActivity();
    
    List<CmmnExecution> createChildExecutions(final List<CmmnActivity> p0);
    
    void triggerChildExecutionsLifecycle(final List<CmmnExecution> p0);
    
    void enable();
    
    void disable();
    
    void reenable();
    
    void manualStart();
    
    void start();
    
    void complete();
    
    void manualComplete();
    
    void occur();
    
    void terminate();
    
    void performTerminate();
    
    void parentTerminate();
    
    void performParentTerminate();
    
    void exit();
    
    void parentComplete();
    
    void performExit();
    
    void suspend();
    
    void performSuspension();
    
    void parentSuspend();
    
    void performParentSuspension();
    
    void resume();
    
    void parentResume();
    
    void reactivate();
    
    void close();
    
    boolean isRequired();
    
    void setRequired(final boolean p0);
    
    void remove();
    
    List<? extends CmmnExecution> getCaseExecutions();
    
    Task createTask(final TaskDecorator p0);
    
    PvmProcessInstance createSubProcessInstance(final PvmProcessDefinition p0);
    
    PvmProcessInstance createSubProcessInstance(final PvmProcessDefinition p0, final String p1);
    
    PvmProcessInstance createSubProcessInstance(final PvmProcessDefinition p0, final String p1, final String p2);
    
    CmmnCaseInstance createSubCaseInstance(final CmmnCaseDefinition p0);
    
    CmmnCaseInstance createSubCaseInstance(final CmmnCaseDefinition p0, final String p1);
    
    void createSentryParts();
    
    boolean isSentrySatisfied(final String p0);
    
    boolean isEntryCriterionSatisfied();
    
    void fireIfOnlySentryParts();
}
