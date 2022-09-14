// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import org.zik.bpm.application.impl.metadata.spi.ProcessesXml;
import java.net.URL;
import java.util.Map;
import org.zik.bpm.container.impl.metadata.spi.ProcessEngineXml;
import java.util.List;
import org.zik.bpm.container.impl.spi.DeploymentOperation;

public class ProcessesXmlStartProcessEnginesStep extends AbstractStartProcessEnginesStep
{
    @Override
    protected List<ProcessEngineXml> getProcessEnginesXmls(final DeploymentOperation operationContext) {
        final Map<URL, ProcessesXml> processesXmls = operationContext.getAttachment("processesXmlList");
        final List<ProcessEngineXml> processEngines = new ArrayList<ProcessEngineXml>();
        for (final ProcessesXml processesXml : processesXmls.values()) {
            processEngines.addAll(processesXml.getProcessEngines());
        }
        return processEngines;
    }
}
