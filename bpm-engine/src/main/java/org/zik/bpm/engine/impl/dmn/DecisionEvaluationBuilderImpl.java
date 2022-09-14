// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.exception.dmn.DecisionDefinitionNotFoundException;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.exception.NullValueException;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.dmn.cmd.EvaluateDecisionCmd;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.dmn.DecisionsEvaluationBuilder;

public class DecisionEvaluationBuilderImpl implements DecisionsEvaluationBuilder
{
    private static final DecisionLogger LOG;
    protected CommandExecutor commandExecutor;
    protected String decisionDefinitionKey;
    protected String decisionDefinitionId;
    protected Integer version;
    protected Map<String, Object> variables;
    protected String decisionDefinitionTenantId;
    protected boolean isTenantIdSet;
    
    public DecisionEvaluationBuilderImpl(final CommandExecutor commandExecutor) {
        this.isTenantIdSet = false;
        this.commandExecutor = commandExecutor;
    }
    
    @Override
    public DecisionsEvaluationBuilder variables(final Map<String, Object> variables) {
        this.variables = variables;
        return this;
    }
    
    @Override
    public DecisionsEvaluationBuilder version(final Integer version) {
        this.version = version;
        return this;
    }
    
    @Override
    public DecisionsEvaluationBuilder decisionDefinitionTenantId(final String tenantId) {
        this.decisionDefinitionTenantId = tenantId;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public DecisionsEvaluationBuilder decisionDefinitionWithoutTenantId() {
        this.decisionDefinitionTenantId = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public DmnDecisionResult evaluate() {
        EnsureUtil.ensureOnlyOneNotNull(NotValidException.class, "either decision definition id or key must be set", this.decisionDefinitionId, this.decisionDefinitionKey);
        if (this.isTenantIdSet && this.decisionDefinitionId != null) {
            throw DecisionEvaluationBuilderImpl.LOG.exceptionEvaluateDecisionDefinitionByIdAndTenantId();
        }
        try {
            return this.commandExecutor.execute((Command<DmnDecisionResult>)new EvaluateDecisionCmd(this));
        }
        catch (NullValueException e) {
            throw new NotValidException(e.getMessage(), e);
        }
        catch (DecisionDefinitionNotFoundException e2) {
            throw new NotFoundException(e2.getMessage(), e2);
        }
    }
    
    public static DecisionsEvaluationBuilder evaluateDecisionByKey(final CommandExecutor commandExecutor, final String decisionDefinitionKey) {
        final DecisionEvaluationBuilderImpl builder = new DecisionEvaluationBuilderImpl(commandExecutor);
        builder.decisionDefinitionKey = decisionDefinitionKey;
        return builder;
    }
    
    public static DecisionsEvaluationBuilder evaluateDecisionById(final CommandExecutor commandExecutor, final String decisionDefinitionId) {
        final DecisionEvaluationBuilderImpl builder = new DecisionEvaluationBuilderImpl(commandExecutor);
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
