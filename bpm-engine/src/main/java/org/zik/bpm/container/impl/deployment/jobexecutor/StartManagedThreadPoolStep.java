// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment.jobexecutor;

import org.zik.bpm.container.impl.metadata.spi.BpmPlatformXml;
import java.util.concurrent.BlockingQueue;
import org.zik.bpm.container.impl.metadata.spi.JobExecutorXml;
import org.zik.bpm.container.impl.spi.PlatformService;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.spi.ServiceTypes;
import org.zik.bpm.container.impl.jmx.services.JmxManagedThreadPool;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ArrayBlockingQueue;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class StartManagedThreadPoolStep extends DeploymentOperationStep
{
    private static final int DEFAULT_CORE_POOL_SIZE = 3;
    private static final int DEFAULT_MAX_POOL_SIZE = 10;
    private static final long DEFAULT_KEEP_ALIVE_TIME_MS = 0L;
    private static final int DEFAULT_QUEUE_SIZE = 3;
    
    @Override
    public String getName() {
        return "Deploy Job Executor Thread Pool";
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        final JobExecutorXml jobExecutorXml = this.getJobExecutorXml(operationContext);
        final int queueSize = this.getQueueSize(jobExecutorXml);
        final int corePoolSize = this.getCorePoolSize(jobExecutorXml);
        final int maxPoolSize = this.getMaxPoolSize(jobExecutorXml);
        final long keepAliveTime = this.getKeepAliveTime(jobExecutorXml);
        final BlockingQueue<Runnable> threadPoolQueue = new ArrayBlockingQueue<Runnable>(queueSize);
        final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, threadPoolQueue);
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        final JmxManagedThreadPool managedThreadPool = new JmxManagedThreadPool(threadPoolQueue, threadPoolExecutor);
        serviceContainer.startService(ServiceTypes.BPM_PLATFORM, "executor-service", managedThreadPool);
    }
    
    private JobExecutorXml getJobExecutorXml(final DeploymentOperation operationContext) {
        final BpmPlatformXml bpmPlatformXml = operationContext.getAttachment("bpmPlatformXml");
        final JobExecutorXml jobExecutorXml = bpmPlatformXml.getJobExecutor();
        return jobExecutorXml;
    }
    
    private int getQueueSize(final JobExecutorXml jobExecutorXml) {
        final String queueSize = jobExecutorXml.getProperties().get("queueSize");
        if (queueSize == null) {
            return 3;
        }
        return Integer.parseInt(queueSize);
    }
    
    private long getKeepAliveTime(final JobExecutorXml jobExecutorXml) {
        final String keepAliveTime = jobExecutorXml.getProperties().get("keepAliveTime");
        if (keepAliveTime == null) {
            return 0L;
        }
        return Long.parseLong(keepAliveTime);
    }
    
    private int getMaxPoolSize(final JobExecutorXml jobExecutorXml) {
        final String maxPoolSize = jobExecutorXml.getProperties().get("maxPoolSize");
        if (maxPoolSize == null) {
            return 10;
        }
        return Integer.parseInt(maxPoolSize);
    }
    
    private int getCorePoolSize(final JobExecutorXml jobExecutorXml) {
        final String corePoolSize = jobExecutorXml.getProperties().get("corePoolSize");
        if (corePoolSize == null) {
            return 3;
        }
        return Integer.parseInt(corePoolSize);
    }
}
