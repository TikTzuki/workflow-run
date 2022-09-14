// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import java.util.Iterator;
import org.zik.bpm.application.impl.metadata.spi.ProcessArchiveXml;
import org.zik.bpm.application.impl.metadata.spi.ProcessesXml;
import java.net.URL;
import java.util.Map;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class DeployProcessArchivesStep extends DeploymentOperationStep
{
    @Override
    public String getName() {
        return "Deploy process archvies";
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final Map<URL, ProcessesXml> processesXmls = operationContext.getAttachment("processesXmlList");
        for (final Map.Entry<URL, ProcessesXml> processesXml : processesXmls.entrySet()) {
            for (final ProcessArchiveXml processArchive : processesXml.getValue().getProcessArchives()) {
                operationContext.addStep(this.createDeployProcessArchiveStep(processArchive, processesXml.getKey()));
            }
        }
    }
    
    protected DeployProcessArchiveStep createDeployProcessArchiveStep(final ProcessArchiveXml parsedProcessArchive, final URL url) {
        return new DeployProcessArchiveStep(parsedProcessArchive, url);
    }
}
