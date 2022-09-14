// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.jmx;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import javax.management.QueryExp;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.concurrent.ConcurrentHashMap;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import java.util.Stack;
import org.zik.bpm.container.impl.spi.PlatformService;
import javax.management.ObjectName;
import java.util.Map;
import javax.management.MBeanServer;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;

public class MBeanServiceContainer implements PlatformServiceContainer
{
    private static final ContainerIntegrationLogger LOG;
    protected MBeanServer mBeanServer;
    protected Map<ObjectName, PlatformService<?>> servicesByName;
    protected ThreadLocal<Stack<DeploymentOperation>> activeDeploymentOperations;
    public static final String SERVICE_NAME_EXECUTOR = "executor-service";
    
    public MBeanServiceContainer() {
        this.servicesByName = new ConcurrentHashMap<ObjectName, PlatformService<?>>();
        this.activeDeploymentOperations = new ThreadLocal<Stack<DeploymentOperation>>();
    }
    
    @Override
    public synchronized <S> void startService(final ServiceType serviceType, final String localName, final PlatformService<S> service) {
        final String serviceName = composeLocalName(serviceType, localName);
        this.startService(serviceName, service);
    }
    
    @Override
    public synchronized <S> void startService(final String name, final PlatformService<S> service) {
        final ObjectName serviceName = getObjectName(name);
        if (this.getService(serviceName) != null) {
            throw new ProcessEngineException("Cannot register service " + serviceName + " with MBeans Container, service with same name already registered.");
        }
        final MBeanServer beanServer = this.getmBeanServer();
        service.start(this);
        try {
            beanServer.registerMBean(service, serviceName);
            this.servicesByName.put(serviceName, service);
            final Stack<DeploymentOperation> currentOperationContext = this.activeDeploymentOperations.get();
            if (currentOperationContext != null) {
                currentOperationContext.peek().serviceAdded(name);
            }
        }
        catch (Exception e) {
            throw MBeanServiceContainer.LOG.cannotRegisterService(serviceName, e);
        }
    }
    
    public static ObjectName getObjectName(final String serviceName) {
        try {
            return new ObjectName(serviceName);
        }
        catch (Exception e) {
            throw MBeanServiceContainer.LOG.cannotComposeNameFor(serviceName, e);
        }
    }
    
    public static String composeLocalName(final ServiceType type, final String localName) {
        return type.getTypeName() + ":type=" + localName;
    }
    
    @Override
    public synchronized void stopService(final ServiceType serviceType, final String localName) {
        final String globalName = composeLocalName(serviceType, localName);
        this.stopService(globalName);
    }
    
    @Override
    public synchronized void stopService(final String name) {
        final MBeanServer mBeanServer = this.getmBeanServer();
        final ObjectName serviceName = getObjectName(name);
        final PlatformService<Object> service = this.getService(serviceName);
        EnsureUtil.ensureNotNull("Cannot stop service " + serviceName + ": no such service registered", "service", service);
        try {
            service.stop(this);
        }
        finally {
            try {
                mBeanServer.unregisterMBean(serviceName);
                this.servicesByName.remove(serviceName);
            }
            catch (Throwable t) {
                throw MBeanServiceContainer.LOG.exceptionWhileUnregisteringService(serviceName.getCanonicalName(), t);
            }
        }
    }
    
    @Override
    public DeploymentOperation.DeploymentOperationBuilder createDeploymentOperation(final String name) {
        return new DeploymentOperation.DeploymentOperationBuilder(this, name);
    }
    
    @Override
    public DeploymentOperation.DeploymentOperationBuilder createUndeploymentOperation(final String name) {
        final DeploymentOperation.DeploymentOperationBuilder builder = new DeploymentOperation.DeploymentOperationBuilder(this, name);
        builder.setUndeploymentOperation();
        return builder;
    }
    
    @Override
    public void executeDeploymentOperation(final DeploymentOperation operation) {
        Stack<DeploymentOperation> currentOperationContext = this.activeDeploymentOperations.get();
        if (currentOperationContext == null) {
            currentOperationContext = new Stack<DeploymentOperation>();
            this.activeDeploymentOperations.set(currentOperationContext);
        }
        try {
            currentOperationContext.push(operation);
            operation.execute();
        }
        finally {
            currentOperationContext.pop();
            if (currentOperationContext.isEmpty()) {
                this.activeDeploymentOperations.remove();
            }
        }
    }
    
    @Override
    public <S> S getService(final ServiceType type, final String localName) {
        final String globalName = composeLocalName(type, localName);
        final ObjectName serviceName = getObjectName(globalName);
        return this.getService(serviceName);
    }
    
    public <S> S getService(final ObjectName name) {
        return (S)this.servicesByName.get(name);
    }
    
    public <S> S getServiceValue(final ObjectName name) {
        final PlatformService<S> service = this.getService(name);
        if (service != null) {
            return service.getValue();
        }
        return null;
    }
    
    @Override
    public <S> S getServiceValue(final ServiceType type, final String localName) {
        final String globalName = composeLocalName(type, localName);
        final ObjectName serviceName = getObjectName(globalName);
        return this.getServiceValue(serviceName);
    }
    
    @Override
    public <S> List<PlatformService<S>> getServicesByType(final ServiceType type) {
        final Set<String> serviceNames = this.getServiceNames(type);
        final List<PlatformService<S>> res = new ArrayList<PlatformService<S>>();
        for (final String serviceName : serviceNames) {
            res.add((PlatformService<S>)this.servicesByName.get(getObjectName(serviceName)));
        }
        return res;
    }
    
    @Override
    public Set<String> getServiceNames(final ServiceType type) {
        final String typeName = composeLocalName(type, "*");
        final ObjectName typeObjectName = getObjectName(typeName);
        final Set<ObjectName> resultNames = this.getmBeanServer().queryNames(typeObjectName, null);
        final Set<String> result = new HashSet<String>();
        for (final ObjectName objectName : resultNames) {
            result.add(objectName.toString());
        }
        return result;
    }
    
    @Override
    public <S> List<S> getServiceValuesByType(final ServiceType type) {
        final Set<String> serviceNames = this.getServiceNames(type);
        final List<S> res = new ArrayList<S>();
        for (final String serviceName : serviceNames) {
            final PlatformService<S> BpmPlatformService = (PlatformService<S>)this.servicesByName.get(getObjectName(serviceName));
            if (BpmPlatformService != null) {
                res.add(BpmPlatformService.getValue());
            }
        }
        return res;
    }
    
    public MBeanServer getmBeanServer() {
        if (this.mBeanServer == null) {
            synchronized (this) {
                if (this.mBeanServer == null) {
                    this.mBeanServer = this.createOrLookupMbeanServer();
                }
            }
        }
        return this.mBeanServer;
    }
    
    public void setmBeanServer(final MBeanServer mBeanServer) {
        this.mBeanServer = mBeanServer;
    }
    
    protected MBeanServer createOrLookupMbeanServer() {
        return ManagementFactory.getPlatformMBeanServer();
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
