// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import java.util.Iterator;
import java.util.List;
import org.zik.bpm.container.impl.metadata.spi.ProcessEngineXml;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public abstract class AbstractStartProcessEnginesStep extends DeploymentOperationStep
{
    @Override
    public String getName() {
        return "Start process engines";
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final List<ProcessEngineXml> processEngines = this.getProcessEnginesXmls(operationContext);
        for (final ProcessEngineXml parsedProcessEngine : processEngines) {
            operationContext.addStep(this.createStartProcessEngineStep(parsedProcessEngine));
        }
    }
    
    protected StartProcessEngineStep createStartProcessEngineStep(final ProcessEngineXml parsedProcessEngine) {
        return new StartProcessEngineStep(parsedProcessEngine);
    }
    
    protected abstract List<ProcessEngineXml> getProcessEnginesXmls(final DeploymentOperation p0);
}
