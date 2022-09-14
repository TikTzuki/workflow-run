// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.annotation.Annotation;
import org.zik.bpm.container.impl.deployment.util.InjectionUtil;
import org.zik.bpm.application.PostDeploy;
import org.zik.bpm.application.AbstractProcessApplication;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class PostDeployInvocationStep extends DeploymentOperationStep
{
    private static final ContainerIntegrationLogger LOG;
    private static final String CALLBACK_NAME = "@PostDeploy";
    
    @Override
    public String getName() {
        return "Invoking @PostDeploy";
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final AbstractProcessApplication processApplication = operationContext.getAttachment("processApplication");
        final String paName = processApplication.getName();
        final Class<? extends AbstractProcessApplication> paClass = processApplication.getClass();
        final Method postDeployMethod = InjectionUtil.detectAnnotatedMethod(paClass, PostDeploy.class);
        if (postDeployMethod == null) {
            PostDeployInvocationStep.LOG.debugPaLifecycleMethodNotFound("@PostDeploy", paName);
            return;
        }
        PostDeployInvocationStep.LOG.debugFoundPaLifecycleCallbackMethod("@PostDeploy", paName);
        final Object[] injections = InjectionUtil.resolveInjections(operationContext, postDeployMethod);
        try {
            postDeployMethod.invoke(processApplication, injections);
        }
        catch (IllegalArgumentException e) {
            throw PostDeployInvocationStep.LOG.exceptionWhileInvokingPaLifecycleCallback("@PostDeploy", paName, e);
        }
        catch (IllegalAccessException e2) {
            throw PostDeployInvocationStep.LOG.exceptionWhileInvokingPaLifecycleCallback("@PostDeploy", paName, e2);
        }
        catch (InvocationTargetException e3) {
            final Throwable cause = e3.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException)cause;
            }
            throw PostDeployInvocationStep.LOG.exceptionWhileInvokingPaLifecycleCallback("@PostDeploy", paName, e3);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
