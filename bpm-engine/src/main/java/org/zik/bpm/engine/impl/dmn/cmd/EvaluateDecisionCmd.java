// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.cmd;

import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.DecisionEvaluationUtil;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import java.util.Iterator;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Map;
import org.camunda.bpm.engine.variable.Variables;
import org.zik.bpm.engine.impl.dmn.DecisionEvaluationBuilderImpl;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.zik.bpm.engine.impl.interceptor.Command;

public class EvaluateDecisionCmd implements Command<DmnDecisionResult>
{
    protected String decisionDefinitionKey;
    protected String decisionDefinitionId;
    protected Integer version;
    protected VariableMap variables;
    protected String decisionDefinitionTenantId;
    protected boolean isTenandIdSet;
    
    public EvaluateDecisionCmd(final DecisionEvaluationBuilderImpl builder) {
        this.decisionDefinitionKey = builder.getDecisionDefinitionKey();
        this.decisionDefinitionId = builder.getDecisionDefinitionId();
        this.version = builder.getVersion();
        this.variables = Variables.fromMap((Map)builder.getVariables());
        this.decisionDefinitionTenantId = builder.getDecisionDefinitionTenantId();
        this.isTenandIdSet = builder.isTenantIdSet();
    }
    
    @Override
    public DmnDecisionResult execute(final CommandContext commandContext) {
        EnsureUtil.ensureOnlyOneNotNull("either decision definition id or key must be set", this.decisionDefinitionId, this.decisionDefinitionKey);
        final DecisionDefinition decisionDefinition = this.getDecisionDefinition(commandContext);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkEvaluateDecision(decisionDefinition);
        }
        this.writeUserOperationLog(commandContext, decisionDefinition);
        return this.doEvaluateDecision(decisionDefinition, this.variables);
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final DecisionDefinition decisionDefinition) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("decisionDefinitionId", null, decisionDefinition.getId()));
        propertyChanges.add(new PropertyChange("decisionDefinitionKey", null, decisionDefinition.getKey()));
        commandContext.getOperationLogManager().logDecisionDefinitionOperation("Evaluate", propertyChanges);
    }
    
    protected DmnDecisionResult doEvaluateDecision(final DecisionDefinition decisionDefinition, final VariableMap variables) {
        try {
            return DecisionEvaluationUtil.evaluateDecision(decisionDefinition, variables);
        }
        catch (Exception e) {
            throw new ProcessEngineException("Exception while evaluating decision with key '" + this.decisionDefinitionKey + "'", e);
        }
    }
    
    protected DecisionDefinition getDecisionDefinition(final CommandContext commandContext) {
        final DeploymentCache deploymentCache = commandContext.getProcessEngineConfiguration().getDeploymentCache();
        if (this.decisionDefinitionId != null) {
            return this.findById(deploymentCache);
        }
        return this.findByKey(deploymentCache);
    }
    
    protected DecisionDefinition findById(final DeploymentCache deploymentCache) {
        return deploymentCache.findDeployedDecisionDefinitionById(this.decisionDefinitionId);
    }
    
    protected DecisionDefinition findByKey(final DeploymentCache deploymentCache) {
        DecisionDefinition decisionDefinition = null;
        if (this.version == null && !this.isTenandIdSet) {
            decisionDefinition = deploymentCache.findDeployedLatestDecisionDefinitionByKey(this.decisionDefinitionKey);
        }
        else if (this.version == null && this.isTenandIdSet) {
            decisionDefinition = deploymentCache.findDeployedLatestDecisionDefinitionByKeyAndTenantId(this.decisionDefinitionKey, this.decisionDefinitionTenantId);
        }
        else if (this.version != null && !this.isTenandIdSet) {
            decisionDefinition = deploymentCache.findDeployedDecisionDefinitionByKeyAndVersion(this.decisionDefinitionKey, this.version);
        }
        else if (this.version != null && this.isTenandIdSet) {
            decisionDefinition = deploymentCache.findDeployedDecisionDefinitionByKeyVersionAndTenantId(this.decisionDefinitionKey, this.version, this.decisionDefinitionTenantId);
        }
        return decisionDefinition;
    }
}
