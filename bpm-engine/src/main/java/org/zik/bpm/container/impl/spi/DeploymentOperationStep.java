// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.spi;

public abstract class DeploymentOperationStep
{
    public abstract String getName();
    
    public abstract void performOperationStep(final DeploymentOperation p0);
    
    public void cancelOperationStep(final DeploymentOperation operationContext) {
    }
}
