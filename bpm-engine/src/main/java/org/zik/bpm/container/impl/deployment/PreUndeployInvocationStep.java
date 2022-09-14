// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.annotation.Annotation;
import org.zik.bpm.container.impl.deployment.util.InjectionUtil;
import org.zik.bpm.application.PreUndeploy;
import org.zik.bpm.application.AbstractProcessApplication;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class PreUndeployInvocationStep extends DeploymentOperationStep
{
    private static final String CALLBACK_NAME = "@PreUndeploy";
    private static final ContainerIntegrationLogger LOG;
    
    @Override
    public String getName() {
        return "Invoking @PreUndeploy";
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final AbstractProcessApplication processApplication = operationContext.getAttachment("processApplication");
        final String paName = processApplication.getName();
        final Class<? extends AbstractProcessApplication> paClass = processApplication.getClass();
        final Method preUndeployMethod = InjectionUtil.detectAnnotatedMethod(paClass, PreUndeploy.class);
        if (preUndeployMethod == null) {
            PreUndeployInvocationStep.LOG.debugPaLifecycleMethodNotFound("@PreUndeploy", paName);
            return;
        }
        PreUndeployInvocationStep.LOG.debugFoundPaLifecycleCallbackMethod("@PreUndeploy", paName);
        final Object[] injections = InjectionUtil.resolveInjections(operationContext, preUndeployMethod);
        try {
            preUndeployMethod.invoke(processApplication, injections);
        }
        catch (IllegalArgumentException e) {
            throw PreUndeployInvocationStep.LOG.exceptionWhileInvokingPaLifecycleCallback("@PreUndeploy", paName, e);
        }
        catch (IllegalAccessException e2) {
            throw PreUndeployInvocationStep.LOG.exceptionWhileInvokingPaLifecycleCallback("@PreUndeploy", paName, e2);
        }
        catch (InvocationTargetException e3) {
            final Throwable cause = e3.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException)cause;
            }
            throw PreUndeployInvocationStep.LOG.exceptionWhileInvokingPaLifecycleCallback("@PreUndeploy", paName, e3);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
