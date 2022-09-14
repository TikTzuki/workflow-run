// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.form.entity.CamundaFormDefinitionManager;
import org.zik.bpm.engine.impl.persistence.entity.CamundaFormDefinitionEntity;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.form.handler.DefaultFormHandler;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.form.CamundaFormRef;
import org.zik.bpm.engine.repository.CamundaFormDefinition;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetCamundaFormDefinitionCmd implements Command<CamundaFormDefinition>
{
    protected CamundaFormRef camundaFormRef;
    protected String deploymentId;
    
    public GetCamundaFormDefinitionCmd(final CamundaFormRef camundaFormRef, final String deploymentId) {
        this.camundaFormRef = camundaFormRef;
        this.deploymentId = deploymentId;
    }
    
    @Override
    public CamundaFormDefinition execute(final CommandContext commandContext) {
        final String binding = this.camundaFormRef.getBinding();
        final String key = this.camundaFormRef.getKey();
        CamundaFormDefinitionEntity definition = null;
        final CamundaFormDefinitionManager manager = commandContext.getCamundaFormDefinitionManager();
        if (binding.equals("deployment")) {
            definition = manager.findDefinitionByDeploymentAndKey(this.deploymentId, key);
        }
        else if (binding.equals("latest")) {
            definition = manager.findLatestDefinitionByKey(key);
        }
        else {
            if (!binding.equals("version")) {
                throw new BadUserRequestException("Unsupported binding type for camundaFormRef. Expected to be one of " + DefaultFormHandler.ALLOWED_FORM_REF_BINDINGS + " but was:" + binding);
            }
            definition = manager.findDefinitionByKeyVersionAndTenantId(key, this.camundaFormRef.getVersion(), null);
        }
        return definition;
    }
}
