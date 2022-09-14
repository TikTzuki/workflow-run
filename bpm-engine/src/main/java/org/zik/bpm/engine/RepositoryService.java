// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import org.zik.bpm.engine.repository.CalledProcessDefinition;
import java.util.Collection;
import org.zik.bpm.engine.repository.DecisionRequirementsDefinition;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.repository.CaseDefinition;
import org.zik.bpm.engine.task.IdentityLink;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.camunda.bpm.model.cmmn.CmmnModelInstance;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.zik.bpm.engine.repository.DiagramLayout;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.repository.UpdateProcessDefinitionSuspensionStateSelectBuilder;
import java.util.Date;
import org.zik.bpm.engine.repository.DeploymentQuery;
import org.zik.bpm.engine.repository.DecisionRequirementsDefinitionQuery;
import org.zik.bpm.engine.repository.DecisionDefinitionQuery;
import org.zik.bpm.engine.repository.CaseDefinitionQuery;
import org.zik.bpm.engine.repository.ProcessDefinitionQuery;
import java.io.InputStream;
import org.zik.bpm.engine.repository.Resource;
import java.util.List;
import org.zik.bpm.engine.repository.DeleteProcessDefinitionsSelectBuilder;
import org.zik.bpm.engine.repository.ProcessApplicationDeploymentBuilder;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.repository.DeploymentBuilder;

public interface RepositoryService
{
    DeploymentBuilder createDeployment();
    
    ProcessApplicationDeploymentBuilder createDeployment(final ProcessApplicationReference p0);
    
    void deleteDeployment(final String p0);
    
    @Deprecated
    void deleteDeploymentCascade(final String p0);
    
    void deleteDeployment(final String p0, final boolean p1);
    
    void deleteDeployment(final String p0, final boolean p1, final boolean p2);
    
    void deleteDeployment(final String p0, final boolean p1, final boolean p2, final boolean p3);
    
    void deleteProcessDefinition(final String p0);
    
    void deleteProcessDefinition(final String p0, final boolean p1);
    
    void deleteProcessDefinition(final String p0, final boolean p1, final boolean p2);
    
    void deleteProcessDefinition(final String p0, final boolean p1, final boolean p2, final boolean p3);
    
    DeleteProcessDefinitionsSelectBuilder deleteProcessDefinitions();
    
    List<String> getDeploymentResourceNames(final String p0);
    
    List<Resource> getDeploymentResources(final String p0);
    
    InputStream getResourceAsStream(final String p0, final String p1);
    
    InputStream getResourceAsStreamById(final String p0, final String p1);
    
    ProcessDefinitionQuery createProcessDefinitionQuery();
    
    CaseDefinitionQuery createCaseDefinitionQuery();
    
    DecisionDefinitionQuery createDecisionDefinitionQuery();
    
    DecisionRequirementsDefinitionQuery createDecisionRequirementsDefinitionQuery();
    
    DeploymentQuery createDeploymentQuery();
    
    void suspendProcessDefinitionById(final String p0);
    
    void suspendProcessDefinitionById(final String p0, final boolean p1, final Date p2);
    
    void suspendProcessDefinitionByKey(final String p0);
    
    void suspendProcessDefinitionByKey(final String p0, final boolean p1, final Date p2);
    
    void activateProcessDefinitionById(final String p0);
    
    void activateProcessDefinitionById(final String p0, final boolean p1, final Date p2);
    
    void activateProcessDefinitionByKey(final String p0);
    
    void activateProcessDefinitionByKey(final String p0, final boolean p1, final Date p2);
    
    UpdateProcessDefinitionSuspensionStateSelectBuilder updateProcessDefinitionSuspensionState();
    
    void updateProcessDefinitionHistoryTimeToLive(final String p0, final Integer p1);
    
    void updateDecisionDefinitionHistoryTimeToLive(final String p0, final Integer p1);
    
    void updateCaseDefinitionHistoryTimeToLive(final String p0, final Integer p1);
    
    InputStream getProcessModel(final String p0);
    
    InputStream getProcessDiagram(final String p0);
    
    ProcessDefinition getProcessDefinition(final String p0);
    
    DiagramLayout getProcessDiagramLayout(final String p0);
    
    BpmnModelInstance getBpmnModelInstance(final String p0);
    
    CmmnModelInstance getCmmnModelInstance(final String p0);
    
    DmnModelInstance getDmnModelInstance(final String p0);
    
    @Deprecated
    void addCandidateStarterUser(final String p0, final String p1);
    
    @Deprecated
    void addCandidateStarterGroup(final String p0, final String p1);
    
    @Deprecated
    void deleteCandidateStarterUser(final String p0, final String p1);
    
    @Deprecated
    void deleteCandidateStarterGroup(final String p0, final String p1);
    
    @Deprecated
    List<IdentityLink> getIdentityLinksForProcessDefinition(final String p0);
    
    CaseDefinition getCaseDefinition(final String p0);
    
    InputStream getCaseModel(final String p0);
    
    InputStream getCaseDiagram(final String p0);
    
    DecisionDefinition getDecisionDefinition(final String p0);
    
    DecisionRequirementsDefinition getDecisionRequirementsDefinition(final String p0);
    
    InputStream getDecisionModel(final String p0);
    
    InputStream getDecisionRequirementsModel(final String p0);
    
    InputStream getDecisionDiagram(final String p0);
    
    InputStream getDecisionRequirementsDiagram(final String p0);
    
    Collection<CalledProcessDefinition> getStaticCalledProcessDefinitions(final String p0);
}
