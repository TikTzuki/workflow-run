// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.execution;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.pvm.PvmProcessInstance;
import org.camunda.bpm.model.cmmn.CmmnModelInstance;
import org.zik.bpm.engine.delegate.CmmnModelExecutionContext;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.engine.delegate.ProcessEngineServicesAware;
import org.zik.bpm.engine.ProcessEngineServices;
import java.util.Collections;
import org.zik.bpm.engine.impl.core.variable.scope.VariableInstanceLifecycleListener;
import org.zik.bpm.engine.impl.core.variable.scope.VariableInstanceFactory;
import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import org.zik.bpm.engine.impl.cmmn.model.CmmnCaseDefinition;
import org.zik.bpm.engine.impl.pvm.PvmProcessDefinition;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import java.util.Collection;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.core.variable.scope.SimpleVariableInstance;
import org.zik.bpm.engine.impl.core.variable.scope.VariableStore;
import org.zik.bpm.engine.impl.pvm.runtime.ExecutionImpl;
import java.util.List;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnBehaviorLogger;
import java.io.Serializable;

public class CaseExecutionImpl extends CmmnExecution implements Serializable
{
    protected static final CmmnBehaviorLogger LOG;
    private static final long serialVersionUID = 1L;
    protected List<CaseExecutionImpl> caseExecutions;
    protected List<CaseSentryPartImpl> caseSentryParts;
    protected CaseExecutionImpl caseInstance;
    protected CaseExecutionImpl parent;
    protected ExecutionImpl subProcessInstance;
    protected ExecutionImpl superExecution;
    protected CaseExecutionImpl subCaseInstance;
    protected CaseExecutionImpl superCaseExecution;
    protected VariableStore<SimpleVariableInstance> variableStore;
    
    public CaseExecutionImpl() {
        this.variableStore = new VariableStore<SimpleVariableInstance>();
    }
    
    @Override
    public String getCaseDefinitionId() {
        return this.getCaseDefinition().getId();
    }
    
    @Override
    public CaseExecutionImpl getParent() {
        return this.parent;
    }
    
    @Override
    public void setParent(final CmmnExecution parent) {
        this.parent = (CaseExecutionImpl)parent;
    }
    
    @Override
    public String getParentId() {
        return this.getParent().getId();
    }
    
    @Override
    public String getActivityId() {
        return this.getActivity().getId();
    }
    
    @Override
    public String getActivityName() {
        return this.getActivity().getName();
    }
    
    @Override
    public List<CaseExecutionImpl> getCaseExecutions() {
        return new ArrayList<CaseExecutionImpl>(this.getCaseExecutionsInternal());
    }
    
    @Override
    protected List<CaseExecutionImpl> getCaseExecutionsInternal() {
        if (this.caseExecutions == null) {
            this.caseExecutions = new ArrayList<CaseExecutionImpl>();
        }
        return this.caseExecutions;
    }
    
    @Override
    public CaseExecutionImpl getCaseInstance() {
        return this.caseInstance;
    }
    
    @Override
    public void setCaseInstance(final CmmnExecution caseInstance) {
        this.caseInstance = (CaseExecutionImpl)caseInstance;
    }
    
    @Override
    public ExecutionImpl getSuperExecution() {
        return this.superExecution;
    }
    
    @Override
    public void setSuperExecution(final PvmExecutionImpl superExecution) {
        this.superExecution = (ExecutionImpl)superExecution;
    }
    
    @Override
    public ExecutionImpl getSubProcessInstance() {
        return this.subProcessInstance;
    }
    
    @Override
    public void setSubProcessInstance(final PvmExecutionImpl subProcessInstance) {
        this.subProcessInstance = (ExecutionImpl)subProcessInstance;
    }
    
    @Override
    public PvmExecutionImpl createSubProcessInstance(final PvmProcessDefinition processDefinition) {
        return this.createSubProcessInstance(processDefinition, null);
    }
    
    @Override
    public PvmExecutionImpl createSubProcessInstance(final PvmProcessDefinition processDefinition, final String businessKey) {
        return this.createSubProcessInstance(processDefinition, businessKey, this.getCaseInstanceId());
    }
    
    @Override
    public PvmExecutionImpl createSubProcessInstance(final PvmProcessDefinition processDefinition, final String businessKey, final String caseInstanceId) {
        final ExecutionImpl subProcessInstance = (ExecutionImpl)processDefinition.createProcessInstance(businessKey, caseInstanceId);
        subProcessInstance.setSuperCaseExecution(this);
        this.setSubProcessInstance(subProcessInstance);
        return subProcessInstance;
    }
    
    @Override
    public CaseExecutionImpl getSubCaseInstance() {
        return this.subCaseInstance;
    }
    
    @Override
    public void setSubCaseInstance(final CmmnExecution subCaseInstance) {
        this.subCaseInstance = (CaseExecutionImpl)subCaseInstance;
    }
    
    @Override
    public CaseExecutionImpl createSubCaseInstance(final CmmnCaseDefinition caseDefinition) {
        return this.createSubCaseInstance(caseDefinition, null);
    }
    
    @Override
    public CaseExecutionImpl createSubCaseInstance(final CmmnCaseDefinition caseDefinition, final String businessKey) {
        final CaseExecutionImpl caseInstance = (CaseExecutionImpl)caseDefinition.createCaseInstance(businessKey);
        this.subCaseInstance.setSuperCaseExecution(this);
        this.setSubCaseInstance(this.subCaseInstance);
        return caseInstance;
    }
    
    @Override
    public CaseExecutionImpl getSuperCaseExecution() {
        return this.superCaseExecution;
    }
    
    @Override
    public void setSuperCaseExecution(final CmmnExecution superCaseExecution) {
        this.superCaseExecution = (CaseExecutionImpl)superCaseExecution;
    }
    
    @Override
    public List<CaseSentryPartImpl> getCaseSentryParts() {
        if (this.caseSentryParts == null) {
            this.caseSentryParts = new ArrayList<CaseSentryPartImpl>();
        }
        return this.caseSentryParts;
    }
    
    @Override
    protected Map<String, List<CmmnSentryPart>> getSentries() {
        final Map<String, List<CmmnSentryPart>> sentries = new HashMap<String, List<CmmnSentryPart>>();
        for (final CaseSentryPartImpl sentryPart : this.getCaseSentryParts()) {
            final String sentryId = sentryPart.getSentryId();
            List<CmmnSentryPart> parts = sentries.get(sentryId);
            if (parts == null) {
                parts = new ArrayList<CmmnSentryPart>();
                sentries.put(sentryId, parts);
            }
            parts.add(sentryPart);
        }
        return sentries;
    }
    
    @Override
    protected List<CaseSentryPartImpl> findSentry(final String sentryId) {
        final List<CaseSentryPartImpl> result = new ArrayList<CaseSentryPartImpl>();
        for (final CaseSentryPartImpl sentryPart : this.getCaseSentryParts()) {
            if (sentryPart.getSentryId().equals(sentryId)) {
                result.add(sentryPart);
            }
        }
        return result;
    }
    
    @Override
    protected void addSentryPart(final CmmnSentryPart sentryPart) {
        this.getCaseSentryParts().add((CaseSentryPartImpl)sentryPart);
    }
    
    @Override
    protected CmmnSentryPart newSentryPart() {
        return new CaseSentryPartImpl();
    }
    
    @Override
    protected CaseExecutionImpl createCaseExecution(final CmmnActivity activity) {
        final CaseExecutionImpl child = this.newCaseExecution();
        child.setActivity(activity);
        child.setParent(this);
        this.getCaseExecutionsInternal().add(child);
        child.setCaseInstance(this.getCaseInstance());
        child.setCaseDefinition(this.getCaseDefinition());
        return child;
    }
    
    @Override
    protected CaseExecutionImpl newCaseExecution() {
        return new CaseExecutionImpl();
    }
    
    @Override
    protected VariableStore<CoreVariableInstance> getVariableStore() {
        return (VariableStore<CoreVariableInstance>)this.variableStore;
    }
    
    @Override
    protected VariableInstanceFactory<CoreVariableInstance> getVariableInstanceFactory() {
        return (VariableInstanceFactory<CoreVariableInstance>)SimpleVariableInstance.SimpleVariableInstanceFactory.INSTANCE;
    }
    
    @Override
    protected List<VariableInstanceLifecycleListener<CoreVariableInstance>> getVariableInstanceLifecycleListeners() {
        return Collections.emptyList();
    }
    
    @Override
    public String toString() {
        if (this.isCaseInstanceExecution()) {
            return "CaseInstance[" + this.getToStringIdentity() + "]";
        }
        return "CmmnExecution[" + this.getToStringIdentity() + "]";
    }
    
    @Override
    protected String getToStringIdentity() {
        return Integer.toString(System.identityHashCode(this));
    }
    
    @Override
    public String getId() {
        return String.valueOf(System.identityHashCode(this));
    }
    
    @Override
    public ProcessEngineServices getProcessEngineServices() {
        throw CaseExecutionImpl.LOG.unsupportedTransientOperationException(ProcessEngineServicesAware.class.getName());
    }
    
    @Override
    public ProcessEngine getProcessEngine() {
        throw CaseExecutionImpl.LOG.unsupportedTransientOperationException(ProcessEngineServicesAware.class.getName());
    }
    
    @Override
    public CmmnElement getCmmnModelElementInstance() {
        throw CaseExecutionImpl.LOG.unsupportedTransientOperationException(CmmnModelExecutionContext.class.getName());
    }
    
    @Override
    public CmmnModelInstance getCmmnModelInstance() {
        throw CaseExecutionImpl.LOG.unsupportedTransientOperationException(CmmnModelExecutionContext.class.getName());
    }
    
    static {
        LOG = ProcessEngineLogger.CMNN_BEHAVIOR_LOGGER;
    }
}
