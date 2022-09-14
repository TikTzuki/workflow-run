// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.cmd.GetStaticCalledProcessDefinitionCmd;
import org.zik.bpm.engine.repository.CalledProcessDefinition;
import java.util.Collection;
import org.zik.bpm.engine.impl.dmn.cmd.GetDeploymentDecisionRequirementsDiagramCmd;
import org.zik.bpm.engine.impl.dmn.cmd.GetDeploymentDecisionDiagramCmd;
import org.zik.bpm.engine.impl.dmn.cmd.GetDeploymentDecisionRequirementsModelCmd;
import org.zik.bpm.engine.impl.dmn.cmd.GetDeploymentDecisionModelCmd;
import org.zik.bpm.engine.impl.dmn.cmd.GetDeploymentDecisionRequirementsDefinitionCmd;
import org.zik.bpm.engine.repository.DecisionRequirementsDefinition;
import org.zik.bpm.engine.exception.dmn.DecisionDefinitionNotFoundException;
import org.zik.bpm.engine.impl.dmn.cmd.GetDeploymentDecisionDefinitionCmd;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.impl.cmmn.cmd.GetDeploymentCaseModelCmd;
import org.zik.bpm.engine.exception.cmmn.CaseDefinitionNotFoundException;
import org.zik.bpm.engine.impl.cmmn.cmd.GetDeploymentCaseDefinitionCmd;
import org.zik.bpm.engine.repository.CaseDefinition;
import org.zik.bpm.engine.impl.cmd.GetIdentityLinksForProcessDefinitionCmd;
import org.zik.bpm.engine.task.IdentityLink;
import org.zik.bpm.engine.impl.cmd.DeleteIdentityLinkForProcessDefinitionCmd;
import org.zik.bpm.engine.impl.cmd.AddIdentityLinkForProcessDefinitionCmd;
import org.zik.bpm.engine.exception.dmn.DmnModelInstanceNotFoundException;
import org.zik.bpm.engine.impl.dmn.cmd.GetDeploymentDmnModelInstanceCmd;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.zik.bpm.engine.exception.DeploymentResourceNotFoundException;
import org.zik.bpm.engine.exception.cmmn.CmmnModelInstanceNotFoundException;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.exception.NullValueException;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.cmmn.cmd.GetDeploymentCmmnModelInstanceCmd;
import org.camunda.bpm.model.cmmn.CmmnModelInstance;
import org.zik.bpm.engine.impl.cmd.GetDeploymentBpmnModelInstanceCmd;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.zik.bpm.engine.impl.cmd.GetDeploymentProcessDiagramLayoutCmd;
import org.zik.bpm.engine.repository.DiagramLayout;
import org.zik.bpm.engine.impl.cmmn.cmd.GetDeploymentCaseDiagramCmd;
import org.zik.bpm.engine.impl.cmd.GetDeploymentProcessDiagramCmd;
import org.zik.bpm.engine.impl.cmd.GetDeploymentProcessModelCmd;
import org.zik.bpm.engine.impl.cmmn.cmd.UpdateCaseDefinitionHistoryTimeToLiveCmd;
import org.zik.bpm.engine.impl.cmd.UpdateDecisionDefinitionHistoryTimeToLiveCmd;
import org.zik.bpm.engine.impl.cmd.UpdateProcessDefinitionHistoryTimeToLiveCmd;
import org.zik.bpm.engine.impl.repository.UpdateProcessDefinitionSuspensionStateBuilderImpl;
import org.zik.bpm.engine.repository.UpdateProcessDefinitionSuspensionStateSelectBuilder;
import java.util.Date;
import org.zik.bpm.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.zik.bpm.engine.impl.cmd.GetDeployedProcessDefinitionCmd;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.repository.DeploymentQuery;
import org.zik.bpm.engine.impl.cmd.GetDeploymentResourceForIdCmd;
import org.zik.bpm.engine.impl.cmd.GetDeploymentResourceCmd;
import java.io.InputStream;
import org.zik.bpm.engine.impl.cmd.GetDeploymentResourcesCmd;
import org.zik.bpm.engine.repository.Resource;
import org.zik.bpm.engine.impl.cmd.GetDeploymentResourceNamesCmd;
import java.util.List;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionQueryImpl;
import org.zik.bpm.engine.repository.DecisionRequirementsDefinitionQuery;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionQueryImpl;
import org.zik.bpm.engine.repository.DecisionDefinitionQuery;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionQueryImpl;
import org.zik.bpm.engine.repository.CaseDefinitionQuery;
import org.zik.bpm.engine.repository.ProcessDefinitionQuery;
import org.zik.bpm.engine.impl.repository.DeleteProcessDefinitionsBuilderImpl;
import org.zik.bpm.engine.repository.DeleteProcessDefinitionsSelectBuilder;
import org.zik.bpm.engine.repository.DeleteProcessDefinitionsBuilder;
import org.zik.bpm.engine.impl.cmd.DeleteDeploymentCmd;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.DeployCmd;
import org.zik.bpm.engine.repository.DeploymentWithDefinitions;
import org.zik.bpm.engine.impl.repository.ProcessApplicationDeploymentBuilderImpl;
import org.zik.bpm.engine.repository.ProcessApplicationDeploymentBuilder;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.impl.repository.DeploymentBuilderImpl;
import org.zik.bpm.engine.repository.DeploymentBuilder;
import java.nio.charset.Charset;
import org.zik.bpm.engine.RepositoryService;

public class RepositoryServiceImpl extends ServiceImpl implements RepositoryService
{
    protected Charset deploymentCharset;
    
    public Charset getDeploymentCharset() {
        return this.deploymentCharset;
    }
    
    public void setDeploymentCharset(final Charset deploymentCharset) {
        this.deploymentCharset = deploymentCharset;
    }
    
    @Override
    public DeploymentBuilder createDeployment() {
        return new DeploymentBuilderImpl(this);
    }
    
    @Override
    public ProcessApplicationDeploymentBuilder createDeployment(final ProcessApplicationReference processApplication) {
        return new ProcessApplicationDeploymentBuilderImpl(this, processApplication);
    }
    
    public DeploymentWithDefinitions deployWithResult(final DeploymentBuilderImpl deploymentBuilder) {
        return this.commandExecutor.execute((Command<DeploymentWithDefinitions>)new DeployCmd(deploymentBuilder));
    }
    
    @Override
    public void deleteDeployment(final String deploymentId) {
        this.commandExecutor.execute((Command<Object>)new DeleteDeploymentCmd(deploymentId, false, false, false));
    }
    
    @Override
    public void deleteDeploymentCascade(final String deploymentId) {
        this.commandExecutor.execute((Command<Object>)new DeleteDeploymentCmd(deploymentId, true, false, false));
    }
    
    @Override
    public void deleteDeployment(final String deploymentId, final boolean cascade) {
        this.commandExecutor.execute((Command<Object>)new DeleteDeploymentCmd(deploymentId, cascade, false, false));
    }
    
    @Override
    public void deleteDeployment(final String deploymentId, final boolean cascade, final boolean skipCustomListeners) {
        this.commandExecutor.execute((Command<Object>)new DeleteDeploymentCmd(deploymentId, cascade, skipCustomListeners, false));
    }
    
    @Override
    public void deleteDeployment(final String deploymentId, final boolean cascade, final boolean skipCustomListeners, final boolean skipIoMappings) {
        this.commandExecutor.execute((Command<Object>)new DeleteDeploymentCmd(deploymentId, cascade, skipCustomListeners, skipIoMappings));
    }
    
    @Override
    public void deleteProcessDefinition(final String processDefinitionId) {
        this.deleteProcessDefinition(processDefinitionId, false);
    }
    
    @Override
    public void deleteProcessDefinition(final String processDefinitionId, final boolean cascade) {
        this.deleteProcessDefinition(processDefinitionId, cascade, false);
    }
    
    @Override
    public void deleteProcessDefinition(final String processDefinitionId, final boolean cascade, final boolean skipCustomListeners) {
        this.deleteProcessDefinition(processDefinitionId, cascade, skipCustomListeners, false);
    }
    
    @Override
    public void deleteProcessDefinition(final String processDefinitionId, final boolean cascade, final boolean skipCustomListeners, final boolean skipIoMappings) {
        final DeleteProcessDefinitionsBuilder builder = this.deleteProcessDefinitions().byIds(processDefinitionId);
        if (cascade) {
            builder.cascade();
        }
        if (skipCustomListeners) {
            builder.skipCustomListeners();
        }
        if (skipIoMappings) {
            builder.skipIoMappings();
        }
        builder.delete();
    }
    
    @Override
    public DeleteProcessDefinitionsSelectBuilder deleteProcessDefinitions() {
        return new DeleteProcessDefinitionsBuilderImpl(this.commandExecutor);
    }
    
    @Override
    public ProcessDefinitionQuery createProcessDefinitionQuery() {
        return new ProcessDefinitionQueryImpl(this.commandExecutor);
    }
    
    @Override
    public CaseDefinitionQuery createCaseDefinitionQuery() {
        return new CaseDefinitionQueryImpl(this.commandExecutor);
    }
    
    @Override
    public DecisionDefinitionQuery createDecisionDefinitionQuery() {
        return new DecisionDefinitionQueryImpl(this.commandExecutor);
    }
    
    @Override
    public DecisionRequirementsDefinitionQuery createDecisionRequirementsDefinitionQuery() {
        return new DecisionRequirementsDefinitionQueryImpl(this.commandExecutor);
    }
    
    @Override
    public List<String> getDeploymentResourceNames(final String deploymentId) {
        return this.commandExecutor.execute((Command<List<String>>)new GetDeploymentResourceNamesCmd(deploymentId));
    }
    
    @Override
    public List<Resource> getDeploymentResources(final String deploymentId) {
        return this.commandExecutor.execute((Command<List<Resource>>)new GetDeploymentResourcesCmd(deploymentId));
    }
    
    @Override
    public InputStream getResourceAsStream(final String deploymentId, final String resourceName) {
        return this.commandExecutor.execute((Command<InputStream>)new GetDeploymentResourceCmd(deploymentId, resourceName));
    }
    
    @Override
    public InputStream getResourceAsStreamById(final String deploymentId, final String resourceId) {
        return this.commandExecutor.execute((Command<InputStream>)new GetDeploymentResourceForIdCmd(deploymentId, resourceId));
    }
    
    @Override
    public DeploymentQuery createDeploymentQuery() {
        return new DeploymentQueryImpl(this.commandExecutor);
    }
    
    @Override
    public ProcessDefinition getProcessDefinition(final String processDefinitionId) {
        return this.commandExecutor.execute((Command<ProcessDefinition>)new GetDeployedProcessDefinitionCmd(processDefinitionId, true));
    }
    
    public ReadOnlyProcessDefinition getDeployedProcessDefinition(final String processDefinitionId) {
        return this.commandExecutor.execute((Command<ReadOnlyProcessDefinition>)new GetDeployedProcessDefinitionCmd(processDefinitionId, true));
    }
    
    @Override
    public void suspendProcessDefinitionById(final String processDefinitionId) {
        this.updateProcessDefinitionSuspensionState().byProcessDefinitionId(processDefinitionId).suspend();
    }
    
    @Override
    public void suspendProcessDefinitionById(final String processDefinitionId, final boolean suspendProcessInstances, final Date suspensionDate) {
        this.updateProcessDefinitionSuspensionState().byProcessDefinitionId(processDefinitionId).includeProcessInstances(suspendProcessInstances).executionDate(suspensionDate).suspend();
    }
    
    @Override
    public void suspendProcessDefinitionByKey(final String processDefinitionKey) {
        this.updateProcessDefinitionSuspensionState().byProcessDefinitionKey(processDefinitionKey).suspend();
    }
    
    @Override
    public void suspendProcessDefinitionByKey(final String processDefinitionKey, final boolean suspendProcessInstances, final Date suspensionDate) {
        this.updateProcessDefinitionSuspensionState().byProcessDefinitionKey(processDefinitionKey).includeProcessInstances(suspendProcessInstances).executionDate(suspensionDate).suspend();
    }
    
    @Override
    public void activateProcessDefinitionById(final String processDefinitionId) {
        this.updateProcessDefinitionSuspensionState().byProcessDefinitionId(processDefinitionId).activate();
    }
    
    @Override
    public void activateProcessDefinitionById(final String processDefinitionId, final boolean activateProcessInstances, final Date activationDate) {
        this.updateProcessDefinitionSuspensionState().byProcessDefinitionId(processDefinitionId).includeProcessInstances(activateProcessInstances).executionDate(activationDate).activate();
    }
    
    @Override
    public void activateProcessDefinitionByKey(final String processDefinitionKey) {
        this.updateProcessDefinitionSuspensionState().byProcessDefinitionKey(processDefinitionKey).activate();
    }
    
    @Override
    public void activateProcessDefinitionByKey(final String processDefinitionKey, final boolean activateProcessInstances, final Date activationDate) {
        this.updateProcessDefinitionSuspensionState().byProcessDefinitionKey(processDefinitionKey).includeProcessInstances(activateProcessInstances).executionDate(activationDate).activate();
    }
    
    @Override
    public UpdateProcessDefinitionSuspensionStateSelectBuilder updateProcessDefinitionSuspensionState() {
        return new UpdateProcessDefinitionSuspensionStateBuilderImpl(this.commandExecutor);
    }
    
    @Override
    public void updateProcessDefinitionHistoryTimeToLive(final String processDefinitionId, final Integer historyTimeToLive) {
        this.commandExecutor.execute((Command<Object>)new UpdateProcessDefinitionHistoryTimeToLiveCmd(processDefinitionId, historyTimeToLive));
    }
    
    @Override
    public void updateDecisionDefinitionHistoryTimeToLive(final String decisionDefinitionId, final Integer historyTimeToLive) {
        this.commandExecutor.execute((Command<Object>)new UpdateDecisionDefinitionHistoryTimeToLiveCmd(decisionDefinitionId, historyTimeToLive));
    }
    
    @Override
    public void updateCaseDefinitionHistoryTimeToLive(final String caseDefinitionId, final Integer historyTimeToLive) {
        this.commandExecutor.execute((Command<Object>)new UpdateCaseDefinitionHistoryTimeToLiveCmd(caseDefinitionId, historyTimeToLive));
    }
    
    @Override
    public InputStream getProcessModel(final String processDefinitionId) {
        return this.commandExecutor.execute((Command<InputStream>)new GetDeploymentProcessModelCmd(processDefinitionId));
    }
    
    @Override
    public InputStream getProcessDiagram(final String processDefinitionId) {
        return this.commandExecutor.execute((Command<InputStream>)new GetDeploymentProcessDiagramCmd(processDefinitionId));
    }
    
    @Override
    public InputStream getCaseDiagram(final String caseDefinitionId) {
        return this.commandExecutor.execute((Command<InputStream>)new GetDeploymentCaseDiagramCmd(caseDefinitionId));
    }
    
    @Override
    public DiagramLayout getProcessDiagramLayout(final String processDefinitionId) {
        return this.commandExecutor.execute((Command<DiagramLayout>)new GetDeploymentProcessDiagramLayoutCmd(processDefinitionId));
    }
    
    @Override
    public BpmnModelInstance getBpmnModelInstance(final String processDefinitionId) {
        return this.commandExecutor.execute((Command<BpmnModelInstance>)new GetDeploymentBpmnModelInstanceCmd(processDefinitionId));
    }
    
    @Override
    public CmmnModelInstance getCmmnModelInstance(final String caseDefinitionId) {
        try {
            return this.commandExecutor.execute((Command<CmmnModelInstance>)new GetDeploymentCmmnModelInstanceCmd(caseDefinitionId));
        }
        catch (NullValueException e) {
            throw new NotValidException(e.getMessage(), e);
        }
        catch (CmmnModelInstanceNotFoundException e2) {
            throw new NotFoundException(e2.getMessage(), e2);
        }
        catch (DeploymentResourceNotFoundException e3) {
            throw new NotFoundException(e3.getMessage(), e3);
        }
    }
    
    @Override
    public DmnModelInstance getDmnModelInstance(final String decisionDefinitionId) {
        try {
            return this.commandExecutor.execute((Command<DmnModelInstance>)new GetDeploymentDmnModelInstanceCmd(decisionDefinitionId));
        }
        catch (NullValueException e) {
            throw new NotValidException(e.getMessage(), e);
        }
        catch (DmnModelInstanceNotFoundException e2) {
            throw new NotFoundException(e2.getMessage(), e2);
        }
        catch (DeploymentResourceNotFoundException e3) {
            throw new NotFoundException(e3.getMessage(), e3);
        }
    }
    
    @Override
    public void addCandidateStarterUser(final String processDefinitionId, final String userId) {
        this.commandExecutor.execute((Command<Object>)new AddIdentityLinkForProcessDefinitionCmd(processDefinitionId, userId, null));
    }
    
    @Override
    public void addCandidateStarterGroup(final String processDefinitionId, final String groupId) {
        this.commandExecutor.execute((Command<Object>)new AddIdentityLinkForProcessDefinitionCmd(processDefinitionId, null, groupId));
    }
    
    @Override
    public void deleteCandidateStarterGroup(final String processDefinitionId, final String groupId) {
        this.commandExecutor.execute((Command<Object>)new DeleteIdentityLinkForProcessDefinitionCmd(processDefinitionId, null, groupId));
    }
    
    @Override
    public void deleteCandidateStarterUser(final String processDefinitionId, final String userId) {
        this.commandExecutor.execute((Command<Object>)new DeleteIdentityLinkForProcessDefinitionCmd(processDefinitionId, userId, null));
    }
    
    @Override
    public List<IdentityLink> getIdentityLinksForProcessDefinition(final String processDefinitionId) {
        return this.commandExecutor.execute((Command<List<IdentityLink>>)new GetIdentityLinksForProcessDefinitionCmd(processDefinitionId));
    }
    
    @Override
    public CaseDefinition getCaseDefinition(final String caseDefinitionId) {
        try {
            return this.commandExecutor.execute((Command<CaseDefinition>)new GetDeploymentCaseDefinitionCmd(caseDefinitionId));
        }
        catch (NullValueException e) {
            throw new NotValidException(e.getMessage(), e);
        }
        catch (CaseDefinitionNotFoundException e2) {
            throw new NotFoundException(e2.getMessage(), e2);
        }
    }
    
    @Override
    public InputStream getCaseModel(final String caseDefinitionId) {
        try {
            return this.commandExecutor.execute((Command<InputStream>)new GetDeploymentCaseModelCmd(caseDefinitionId));
        }
        catch (NullValueException e) {
            throw new NotValidException(e.getMessage(), e);
        }
        catch (CaseDefinitionNotFoundException e2) {
            throw new NotFoundException(e2.getMessage(), e2);
        }
        catch (DeploymentResourceNotFoundException e3) {
            throw new NotFoundException(e3.getMessage(), e3);
        }
    }
    
    @Override
    public DecisionDefinition getDecisionDefinition(final String decisionDefinitionId) {
        try {
            return this.commandExecutor.execute((Command<DecisionDefinition>)new GetDeploymentDecisionDefinitionCmd(decisionDefinitionId));
        }
        catch (NullValueException e) {
            throw new NotValidException(e.getMessage(), e);
        }
        catch (DecisionDefinitionNotFoundException e2) {
            throw new NotFoundException(e2.getMessage(), e2);
        }
    }
    
    @Override
    public DecisionRequirementsDefinition getDecisionRequirementsDefinition(final String decisionRequirementsDefinitionId) {
        try {
            return this.commandExecutor.execute((Command<DecisionRequirementsDefinition>)new GetDeploymentDecisionRequirementsDefinitionCmd(decisionRequirementsDefinitionId));
        }
        catch (NullValueException e) {
            throw new NotValidException(e.getMessage(), e);
        }
        catch (DecisionDefinitionNotFoundException e2) {
            throw new NotFoundException(e2.getMessage(), e2);
        }
    }
    
    @Override
    public InputStream getDecisionModel(final String decisionDefinitionId) {
        try {
            return this.commandExecutor.execute((Command<InputStream>)new GetDeploymentDecisionModelCmd(decisionDefinitionId));
        }
        catch (NullValueException e) {
            throw new NotValidException(e.getMessage(), e);
        }
        catch (DecisionDefinitionNotFoundException e2) {
            throw new NotFoundException(e2.getMessage(), e2);
        }
        catch (DeploymentResourceNotFoundException e3) {
            throw new NotFoundException(e3.getMessage(), e3);
        }
    }
    
    @Override
    public InputStream getDecisionRequirementsModel(final String decisionRequirementsDefinitionId) {
        try {
            return this.commandExecutor.execute((Command<InputStream>)new GetDeploymentDecisionRequirementsModelCmd(decisionRequirementsDefinitionId));
        }
        catch (NullValueException e) {
            throw new NotValidException(e.getMessage(), e);
        }
        catch (DecisionDefinitionNotFoundException e2) {
            throw new NotFoundException(e2.getMessage(), e2);
        }
        catch (DeploymentResourceNotFoundException e3) {
            throw new NotFoundException(e3.getMessage(), e3);
        }
    }
    
    @Override
    public InputStream getDecisionDiagram(final String decisionDefinitionId) {
        return this.commandExecutor.execute((Command<InputStream>)new GetDeploymentDecisionDiagramCmd(decisionDefinitionId));
    }
    
    @Override
    public InputStream getDecisionRequirementsDiagram(final String decisionRequirementsDefinitionId) {
        return this.commandExecutor.execute((Command<InputStream>)new GetDeploymentDecisionRequirementsDiagramCmd(decisionRequirementsDefinitionId));
    }
    
    @Override
    public Collection<CalledProcessDefinition> getStaticCalledProcessDefinitions(final String processDefinitionId) {
        return this.commandExecutor.execute((Command<Collection<CalledProcessDefinition>>)new GetStaticCalledProcessDefinitionCmd(processDefinitionId));
    }
}
