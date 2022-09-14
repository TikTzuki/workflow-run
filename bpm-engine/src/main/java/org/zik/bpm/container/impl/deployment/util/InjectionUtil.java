// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment.util;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Iterator;
import org.zik.bpm.application.ProcessApplicationDeploymentInfo;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.spi.ServiceTypes;
import org.zik.bpm.container.impl.jmx.services.JmxManagedProcessApplication;
import org.zik.bpm.application.AbstractProcessApplication;
import java.util.List;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import org.zik.bpm.application.ProcessApplicationInfo;
import org.zik.bpm.engine.ProcessEngine;
import java.util.ArrayList;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;

public class InjectionUtil
{
    private static final ContainerIntegrationLogger LOG;
    
    public static Method detectAnnotatedMethod(final Class<?> clazz, final Class<? extends Annotation> annotationType) {
        final Method[] methods2;
        final Method[] methods = methods2 = clazz.getMethods();
        for (final Method method : methods2) {
            for (final Annotation annotaiton : method.getAnnotations()) {
                if (annotationType.equals(annotaiton.annotationType())) {
                    return method;
                }
            }
        }
        return null;
    }
    
    public static Object[] resolveInjections(final DeploymentOperation operationContext, final Method lifecycleMethod) {
        final Type[] parameterTypes = lifecycleMethod.getGenericParameterTypes();
        final List<Object> parameters = new ArrayList<Object>();
        for (final Type parameterType : parameterTypes) {
            boolean injectionResolved = false;
            if (parameterType instanceof Class) {
                final Class<?> parameterClass = (Class<?>)parameterType;
                if (ProcessEngine.class.isAssignableFrom(parameterClass)) {
                    parameters.add(getDefaultProcessEngine(operationContext));
                    injectionResolved = true;
                }
                else if (ProcessApplicationInfo.class.isAssignableFrom(parameterClass)) {
                    parameters.add(getProcessApplicationInfo(operationContext));
                    injectionResolved = true;
                }
            }
            else if (parameterType instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType)parameterType;
                final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length == 1 && ProcessEngine.class.isAssignableFrom((Class<?>)actualTypeArguments[0])) {
                    parameters.add(getProcessEngines(operationContext));
                    injectionResolved = true;
                }
            }
            if (!injectionResolved) {
                throw InjectionUtil.LOG.unsuppoertedParameterType(parameterType);
            }
        }
        return parameters.toArray();
    }
    
    public static ProcessApplicationInfo getProcessApplicationInfo(final DeploymentOperation operationContext) {
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        final AbstractProcessApplication processApplication = operationContext.getAttachment("processApplication");
        final JmxManagedProcessApplication managedPa = serviceContainer.getServiceValue(ServiceTypes.PROCESS_APPLICATION, processApplication.getName());
        return managedPa.getProcessApplicationInfo();
    }
    
    public static List<ProcessEngine> getProcessEngines(final DeploymentOperation operationContext) {
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        final ProcessApplicationInfo processApplicationInfo = getProcessApplicationInfo(operationContext);
        final List<ProcessEngine> processEngines = new ArrayList<ProcessEngine>();
        for (final ProcessApplicationDeploymentInfo deploymentInfo : processApplicationInfo.getDeploymentInfo()) {
            final String processEngineName = deploymentInfo.getProcessEngineName();
            processEngines.add(serviceContainer.getServiceValue(ServiceTypes.PROCESS_ENGINE, processEngineName));
        }
        return processEngines;
    }
    
    public static ProcessEngine getDefaultProcessEngine(final DeploymentOperation operationContext) {
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        return serviceContainer.getServiceValue(ServiceTypes.PROCESS_ENGINE, "default");
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
