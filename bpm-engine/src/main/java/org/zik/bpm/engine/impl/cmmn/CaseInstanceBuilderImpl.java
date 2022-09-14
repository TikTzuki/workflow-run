// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.exception.cmmn.CaseIllegalStateTransitionException;
import org.zik.bpm.engine.exception.NotAllowedException;
import org.zik.bpm.engine.exception.NullValueException;
import org.zik.bpm.engine.exception.cmmn.CaseDefinitionNotFoundException;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmmn.cmd.CreateCaseInstanceCmd;
import org.zik.bpm.engine.runtime.CaseInstance;
import java.util.Map;
import org.camunda.bpm.engine.variable.Variables;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cmmn.operation.CmmnOperationLogger;
import org.zik.bpm.engine.runtime.CaseInstanceBuilder;

public class CaseInstanceBuilderImpl implements CaseInstanceBuilder
{
    private static final CmmnOperationLogger LOG;
    protected CommandExecutor commandExecutor;
    protected CommandContext commandContext;
    protected String caseDefinitionKey;
    protected String caseDefinitionId;
    protected String businessKey;
    protected VariableMap variables;
    protected String caseDefinitionTenantId;
    protected boolean isTenantIdSet;
    
    public CaseInstanceBuilderImpl(final CommandExecutor commandExecutor, final String caseDefinitionKey, final String caseDefinitionId) {
        this(caseDefinitionKey, caseDefinitionId);
        EnsureUtil.ensureNotNull("commandExecutor", commandExecutor);
        this.commandExecutor = commandExecutor;
    }
    
    public CaseInstanceBuilderImpl(final CommandContext commandContext, final String caseDefinitionKey, final String caseDefinitionId) {
        this(caseDefinitionKey, caseDefinitionId);
        EnsureUtil.ensureNotNull("commandContext", commandContext);
        this.commandContext = commandContext;
    }
    
    private CaseInstanceBuilderImpl(final String caseDefinitionKey, final String caseDefinitionId) {
        this.isTenantIdSet = false;
        this.caseDefinitionKey = caseDefinitionKey;
        this.caseDefinitionId = caseDefinitionId;
    }
    
    @Override
    public CaseInstanceBuilder businessKey(final String businessKey) {
        this.businessKey = businessKey;
        return this;
    }
    
    @Override
    public CaseInstanceBuilder caseDefinitionTenantId(final String tenantId) {
        this.caseDefinitionTenantId = tenantId;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public CaseInstanceBuilder caseDefinitionWithoutTenantId() {
        this.caseDefinitionTenantId = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public CaseInstanceBuilder setVariable(final String variableName, final Object variableValue) {
        EnsureUtil.ensureNotNull(NotValidException.class, "variableName", (Object)variableName);
        if (this.variables == null) {
            this.variables = Variables.createVariables();
        }
        this.variables.putValue(variableName, variableValue);
        return this;
    }
    
    @Override
    public CaseInstanceBuilder setVariables(final Map<String, Object> variables) {
        if (variables != null) {
            if (this.variables == null) {
                this.variables = Variables.fromMap((Map)variables);
            }
            else {
                this.variables.putAll((Map)variables);
            }
        }
        return this;
    }
    
    @Override
    public CaseInstance create() {
        if (this.isTenantIdSet && this.caseDefinitionId != null) {
            throw CaseInstanceBuilderImpl.LOG.exceptionCreateCaseInstanceByIdAndTenantId();
        }
        try {
            final CreateCaseInstanceCmd command = new CreateCaseInstanceCmd(this);
            if (this.commandExecutor != null) {
                return this.commandExecutor.execute((Command<CaseInstance>)command);
            }
            return command.execute(this.commandContext);
        }
        catch (CaseDefinitionNotFoundException e) {
            throw new NotFoundException(e.getMessage(), e);
        }
        catch (NullValueException e2) {
            throw new NotValidException(e2.getMessage(), e2);
        }
        catch (CaseIllegalStateTransitionException e3) {
            throw new NotAllowedException(e3.getMessage(), e3);
        }
    }
    
    public String getCaseDefinitionKey() {
        return this.caseDefinitionKey;
    }
    
    public String getCaseDefinitionId() {
        return this.caseDefinitionId;
    }
    
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    public VariableMap getVariables() {
        return this.variables;
    }
    
    public String getCaseDefinitionTenantId() {
        return this.caseDefinitionTenantId;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    static {
        LOG = ProcessEngineLogger.CMMN_OPERATION_LOGGER;
    }
}
