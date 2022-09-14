// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.repository;

import org.zik.bpm.engine.repository.DeploymentHandler;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.engine.repository.DeploymentHandlerFactory;

public class DefaultDeploymentHandlerFactory implements DeploymentHandlerFactory
{
    @Override
    public DeploymentHandler buildDeploymentHandler(final ProcessEngine processEngine) {
        return new DefaultDeploymentHandler(processEngine);
    }
}
