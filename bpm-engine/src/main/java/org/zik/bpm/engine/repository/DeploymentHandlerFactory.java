// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import org.zik.bpm.engine.ProcessEngine;

public interface DeploymentHandlerFactory
{
    DeploymentHandler buildDeploymentHandler(final ProcessEngine p0);
}
