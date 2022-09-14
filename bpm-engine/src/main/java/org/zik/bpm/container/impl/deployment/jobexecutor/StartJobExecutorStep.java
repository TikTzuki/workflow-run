// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment.jobexecutor;

import org.zik.bpm.container.impl.metadata.spi.BpmPlatformXml;
import java.util.Iterator;
import org.zik.bpm.container.impl.metadata.spi.JobExecutorXml;
import org.zik.bpm.container.impl.metadata.spi.JobAcquisitionXml;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class StartJobExecutorStep extends DeploymentOperationStep
{
    @Override
    public String getName() {
        return "Starting the Managed Job Executor";
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final JobExecutorXml jobExecutorXml = this.getJobExecutorXml(operationContext);
        for (final JobAcquisitionXml jobAcquisitionXml : jobExecutorXml.getJobAcquisitions()) {
            operationContext.addStep(new StartJobAcquisitionStep(jobAcquisitionXml));
        }
    }
    
    private JobExecutorXml getJobExecutorXml(final DeploymentOperation operationContext) {
        final BpmPlatformXml bpmPlatformXml = operationContext.getAttachment("bpmPlatformXml");
        final JobExecutorXml jobExecutorXml = bpmPlatformXml.getJobExecutor();
        return jobExecutorXml;
    }
}
