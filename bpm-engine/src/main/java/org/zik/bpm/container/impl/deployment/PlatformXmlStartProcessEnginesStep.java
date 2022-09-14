// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import org.zik.bpm.container.impl.metadata.spi.BpmPlatformXml;
import org.zik.bpm.container.impl.metadata.spi.ProcessEngineXml;
import java.util.List;
import org.zik.bpm.container.impl.spi.DeploymentOperation;

public class PlatformXmlStartProcessEnginesStep extends AbstractStartProcessEnginesStep
{
    @Override
    protected List<ProcessEngineXml> getProcessEnginesXmls(final DeploymentOperation operationContext) {
        final BpmPlatformXml bpmPlatformXml = operationContext.getAttachment("bpmPlatformXml");
        return bpmPlatformXml.getProcessEngines();
    }
}
