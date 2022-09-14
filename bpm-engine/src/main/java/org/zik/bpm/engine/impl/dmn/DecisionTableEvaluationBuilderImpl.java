// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.exception.dmn.DecisionDefinitionNotFoundException;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.exception.NullValueException;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.dmn.cmd.EvaluateDecisionTableCmd;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.dmn.DecisionEvaluationBuilder;

public class DecisionTableEvaluationBuilderImpl implements DecisionEvaluationBuilder
{
    private static final DecisionLogger LOG;
    protected CommandExecutor commandExecutor;
    protected String decisionDefinitionKey;
    protected String decisionDefinitionId;
    protected Integer version;
    protected Map<String, Object> variables;
    protected String decisionDefinitionTenantId;
    protected boolean isTenantIdSet;
    
    public DecisionTableEvaluationBuilderImpl(final CommandExecutor commandExecutor) {
        this.isTenantIdSet = false;
        this.commandExecutor = commandExecutor;
    }
    
    @Override
    public DecisionEvaluationBuilder variables(final Map<String, Object> variables) {
        this.variables = variables;
        return this;
    }
    
    @Override
    public DecisionEvaluationBuilder version(final Integer version) {
        this.version = version;
        return this;
    }
    
    @Override
    public DecisionEvaluationBuilder decisionDefinitionTenantId(final String tenantId) {
        this.decisionDefinitionTenantId = tenantId;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public DecisionEvaluationBuilder decisionDefinitionWithoutTenantId() {
        this.decisionDefinitionTenantId = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public DmnDecisionTableResult evaluate() {
        EnsureUtil.ensureOnlyOneNotNull(NotValidException.class, "either decision definition id or key must be set", this.decisionDefinitionId, this.decisionDefinitionKey);
        if (this.isTenantIdSet && this.decisionDefinitionId != null) {
            throw DecisionTableEvaluationBuilderImpl.LOG.exceptionEvaluateDecisionDefinitionByIdAndTenantId();
        }
        try {
            return this.commandExecutor.execute((Command<DmnDecisionTableResult>)new EvaluateDecisionTableCmd(this));
        }
        catch (NullValueException e) {
            throw new NotValidException(e.getMessage(), e);
        }
        catch (DecisionDefinitionNotFoundException e2) {
            throw new NotFoundException(e2.getMessage(), e2);
        }
    }
    
    public static DecisionEvaluationBuilder evaluateDecisionTableByKey(final CommandExecutor commandExecutor, final String decisionDefinitionKey) {
        final DecisionTableEvaluationBuilderImpl builder = new DecisionTableEvaluationBuilderImpl(commandExecutor);
        builder.decisionDefinitionKey = decisionDefinitionKey;
        return builder;
    }
    
    public static DecisionEvaluationBuilder evaluateDecisionTableById(final CommandExecutor commandExecutor, final String decisionDefinitionId) {
        final DecisionTableEvaluationBuilderImpl builder = new DecisionTableEvaluationBuilderImpl(commandExecutor);
        builder.decisionDefinitionId = decisionDefinitionId;
        return builder;
    }
    
    public String getDecisionDefinitionKey() {
        return this.decisionDefinitionKey;
    }
    
    public String getDecisionDefinitionId() {
        return this.decisionDefinitionId;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public Map<String, Object> getVariables() {
        return this.variables;
    }
    
    public String getDecisionDefinitionTenantId() {
        return this.decisionDefinitionTenantId;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    static {
        LOG = ProcessEngineLogger.DECISION_LOGGER;
    }
}
