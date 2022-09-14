// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.exception.DeploymentResourceNotFoundException;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.repository.CamundaFormDefinition;
import org.zik.bpm.engine.form.CamundaFormRef;
import org.zik.bpm.engine.form.FormData;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.InputStream;
import org.zik.bpm.engine.impl.interceptor.Command;

public abstract class AbstractGetDeployedFormCmd implements Command<InputStream>
{
    protected static String EMBEDDED_KEY;
    protected static String CAMUNDA_FORMS_KEY;
    protected static int EMBEDDED_KEY_LENGTH;
    protected static int CAMUNDA_FORMS_KEY_LENGTH;
    protected static String DEPLOYMENT_KEY;
    protected static int DEPLOYMENT_KEY_LENGTH;
    protected CommandContext commandContext;
    
    @Override
    public InputStream execute(final CommandContext commandContext) {
        this.commandContext = commandContext;
        this.checkAuthorization();
        final FormData formData = this.getFormData();
        final String formKey = formData.getFormKey();
        final CamundaFormRef camundaFormRef = formData.getCamundaFormRef();
        if (formKey != null) {
            return this.getResourceForFormKey(formData, formKey);
        }
        if (camundaFormRef != null && camundaFormRef.getKey() != null) {
            return this.getResourceForCamundaFormRef(camundaFormRef, formData.getDeploymentId());
        }
        throw new BadUserRequestException("One of the attributes 'formKey' and 'camunda:formRef' must be supplied but none were set.");
    }
    
    protected InputStream getResourceForFormKey(final FormData formData, final String formKey) {
        String resourceName = formKey;
        if (resourceName.startsWith(AbstractGetDeployedFormCmd.EMBEDDED_KEY)) {
            resourceName = resourceName.substring(AbstractGetDeployedFormCmd.EMBEDDED_KEY_LENGTH, resourceName.length());
        }
        else if (resourceName.startsWith(AbstractGetDeployedFormCmd.CAMUNDA_FORMS_KEY)) {
            resourceName = resourceName.substring(AbstractGetDeployedFormCmd.CAMUNDA_FORMS_KEY_LENGTH, resourceName.length());
        }
        if (!resourceName.startsWith(AbstractGetDeployedFormCmd.DEPLOYMENT_KEY)) {
            throw new BadUserRequestException("The form key '" + formKey + "' does not reference a deployed form.");
        }
        resourceName = resourceName.substring(AbstractGetDeployedFormCmd.DEPLOYMENT_KEY_LENGTH, resourceName.length());
        return this.getDeploymentResource(formData.getDeploymentId(), resourceName);
    }
    
    protected InputStream getResourceForCamundaFormRef(final CamundaFormRef camundaFormRef, final String deploymentId) {
        final CamundaFormDefinition definition = this.commandContext.runWithoutAuthorization((Command<CamundaFormDefinition>)new GetCamundaFormDefinitionCmd(camundaFormRef, deploymentId));
        if (definition == null) {
            throw new NotFoundException("No Camunda Form Definition was found for Camunda Form Ref: " + camundaFormRef);
        }
        return this.getDeploymentResource(definition.getDeploymentId(), definition.getResourceName());
    }
    
    protected InputStream getDeploymentResource(final String deploymentId, final String resourceName) {
        final GetDeploymentResourceCmd getDeploymentResourceCmd = new GetDeploymentResourceCmd(deploymentId, resourceName);
        try {
            return this.commandContext.runWithoutAuthorization((Command<InputStream>)getDeploymentResourceCmd);
        }
        catch (DeploymentResourceNotFoundException e) {
            throw new NotFoundException("The form with the resource name '" + resourceName + "' cannot be found in deployment with id " + deploymentId, e);
        }
    }
    
    protected abstract FormData getFormData();
    
    protected abstract void checkAuthorization();
    
    static {
        AbstractGetDeployedFormCmd.EMBEDDED_KEY = "embedded:";
        AbstractGetDeployedFormCmd.CAMUNDA_FORMS_KEY = "camunda-forms:";
        AbstractGetDeployedFormCmd.EMBEDDED_KEY_LENGTH = AbstractGetDeployedFormCmd.EMBEDDED_KEY.length();
        AbstractGetDeployedFormCmd.CAMUNDA_FORMS_KEY_LENGTH = AbstractGetDeployedFormCmd.CAMUNDA_FORMS_KEY.length();
        AbstractGetDeployedFormCmd.DEPLOYMENT_KEY = "deployment:";
        AbstractGetDeployedFormCmd.DEPLOYMENT_KEY_LENGTH = AbstractGetDeployedFormCmd.DEPLOYMENT_KEY.length();
    }
}
