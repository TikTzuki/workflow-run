// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.handler;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.core.handler.HandlerContext;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.cmmn.instance.Definitions;
import org.camunda.bpm.model.cmmn.CmmnModelInstance;
import org.zik.bpm.engine.repository.Deployment;
import org.zik.bpm.engine.impl.util.ParseUtil;
import java.util.Map;
import org.zik.bpm.engine.impl.task.TaskDefinition;
import java.util.HashMap;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;
import org.zik.bpm.engine.impl.cmmn.transformer.CmmnTransformerLogger;
import org.zik.bpm.engine.impl.cmmn.model.CmmnCaseDefinition;
import org.camunda.bpm.model.cmmn.instance.Case;

public class CaseHandler extends CmmnElementHandler<Case, CmmnCaseDefinition>
{
    protected static final CmmnTransformerLogger LOG;
    
    @Override
    public CmmnCaseDefinition handleElement(final Case element, final CmmnHandlerContext context) {
        final CaseDefinitionEntity definition = this.createActivity((CmmnElement)element, context);
        this.initializeActivity(element, definition, context);
        return definition;
    }
    
    protected void initializeActivity(final Case element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final CaseDefinitionEntity definition = (CaseDefinitionEntity)activity;
        final Deployment deployment = context.getDeployment();
        definition.setKey(element.getId());
        definition.setName(element.getName());
        definition.setDeploymentId(deployment.getId());
        definition.setTaskDefinitions(new HashMap<String, TaskDefinition>());
        definition.setHistoryTimeToLive(ParseUtil.parseHistoryTimeToLive(element.getCamundaHistoryTimeToLiveString()));
        final CmmnModelInstance model = context.getModel();
        final Definitions definitions = model.getDefinitions();
        final String category = definitions.getTargetNamespace();
        definition.setCategory(category);
    }
    
    protected CaseDefinitionEntity createActivity(final CmmnElement element, final CmmnHandlerContext context) {
        final CaseDefinitionEntity definition = new CaseDefinitionEntity();
        definition.setCmmnElement(element);
        return definition;
    }
    
    static {
        LOG = ProcessEngineLogger.CMMN_TRANSFORMER_LOGGER;
    }
}
