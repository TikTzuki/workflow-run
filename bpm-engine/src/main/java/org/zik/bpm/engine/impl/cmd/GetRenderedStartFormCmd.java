// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.form.StartFormData;
import org.zik.bpm.engine.impl.form.handler.StartFormHandler;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.ScriptEvaluationException;
import org.zik.bpm.engine.impl.form.engine.FormEngine;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetRenderedStartFormCmd implements Command<Object>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String processDefinitionId;
    protected String formEngineName;
    private static CommandLogger LOG;
    
    public GetRenderedStartFormCmd(final String processDefinitionId, final String formEngineName) {
        this.processDefinitionId = processDefinitionId;
        this.formEngineName = formEngineName;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final DeploymentCache deploymentCache = processEngineConfiguration.getDeploymentCache();
        final ProcessDefinitionEntity processDefinition = deploymentCache.findDeployedProcessDefinitionById(this.processDefinitionId);
        EnsureUtil.ensureNotNull("Process Definition '" + this.processDefinitionId + "' not found", "processDefinition", processDefinition);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadProcessDefinition(processDefinition);
        }
        final StartFormHandler startFormHandler = processDefinition.getStartFormHandler();
        if (startFormHandler == null) {
            return null;
        }
        final FormEngine formEngine = Context.getProcessEngineConfiguration().getFormEngines().get(this.formEngineName);
        EnsureUtil.ensureNotNull("No formEngine '" + this.formEngineName + "' defined process engine configuration", "formEngine", formEngine);
        final StartFormData startForm = startFormHandler.createStartFormData(processDefinition);
        Object renderedStartForm = null;
        try {
            renderedStartForm = formEngine.renderStartForm(startForm);
        }
        catch (ScriptEvaluationException e) {
            GetRenderedStartFormCmd.LOG.exceptionWhenStartFormScriptEvaluation(this.processDefinitionId, e);
        }
        return renderedStartForm;
    }
    
    static {
        GetRenderedStartFormCmd.LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
