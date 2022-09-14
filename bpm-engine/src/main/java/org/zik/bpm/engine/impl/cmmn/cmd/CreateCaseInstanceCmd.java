// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.cmd;

import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.cmmn.CaseDefinitionNotFoundException;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.repository.CaseDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.cmmn.CaseInstanceBuilderImpl;
import java.util.Map;
import java.io.Serializable;
import org.zik.bpm.engine.runtime.CaseInstance;
import org.zik.bpm.engine.impl.interceptor.Command;

public class CreateCaseInstanceCmd implements Command<CaseInstance>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String caseDefinitionKey;
    protected String caseDefinitionId;
    protected Map<String, Object> variables;
    protected String businessKey;
    protected String caseDefinitionTenantId;
    protected boolean isTenantIdSet;
    
    public CreateCaseInstanceCmd(final CaseInstanceBuilderImpl builder) {
        this.isTenantIdSet = false;
        this.caseDefinitionKey = builder.getCaseDefinitionKey();
        this.caseDefinitionId = builder.getCaseDefinitionId();
        this.businessKey = builder.getBusinessKey();
        this.variables = (Map<String, Object>)builder.getVariables();
        this.caseDefinitionTenantId = builder.getCaseDefinitionTenantId();
        this.isTenantIdSet = builder.isTenantIdSet();
    }
    
    @Override
    public CaseInstance execute(final CommandContext commandContext) {
        EnsureUtil.ensureAtLeastOneNotNull("caseDefinitionId and caseDefinitionKey are null", this.caseDefinitionId, this.caseDefinitionKey);
        final CaseDefinitionEntity caseDefinition = this.find(commandContext);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkCreateCaseInstance(caseDefinition);
        }
        final CaseExecutionEntity caseInstance = (CaseExecutionEntity)caseDefinition.createCaseInstance(this.businessKey);
        caseInstance.create(this.variables);
        return caseInstance;
    }
    
    protected CaseDefinitionEntity find(final CommandContext commandContext) {
        final DeploymentCache deploymentCache = commandContext.getProcessEngineConfiguration().getDeploymentCache();
        CaseDefinitionEntity caseDefinition = null;
        if (this.caseDefinitionId != null) {
            caseDefinition = this.findById(deploymentCache, this.caseDefinitionId);
            EnsureUtil.ensureNotNull(CaseDefinitionNotFoundException.class, "No case definition found for id = '" + this.caseDefinitionId + "'", "caseDefinition", caseDefinition);
        }
        else {
            caseDefinition = this.findByKey(deploymentCache, this.caseDefinitionKey);
            EnsureUtil.ensureNotNull(CaseDefinitionNotFoundException.class, "No case definition found for key '" + this.caseDefinitionKey + "'", "caseDefinition", caseDefinition);
        }
        return caseDefinition;
    }
    
    protected CaseDefinitionEntity findById(final DeploymentCache deploymentCache, final String caseDefinitionId) {
        return deploymentCache.findDeployedCaseDefinitionById(caseDefinitionId);
    }
    
    protected CaseDefinitionEntity findByKey(final DeploymentCache deploymentCache, final String caseDefinitionKey) {
        if (this.isTenantIdSet) {
            return deploymentCache.findDeployedLatestCaseDefinitionByKeyAndTenantId(caseDefinitionKey, this.caseDefinitionTenantId);
        }
        return deploymentCache.findDeployedLatestCaseDefinitionByKey(caseDefinitionKey);
    }
}
