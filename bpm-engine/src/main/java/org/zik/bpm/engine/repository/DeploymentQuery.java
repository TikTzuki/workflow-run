// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import java.util.Date;
import org.zik.bpm.engine.query.Query;

public interface DeploymentQuery extends Query<DeploymentQuery, Deployment>
{
    DeploymentQuery deploymentId(final String p0);
    
    DeploymentQuery deploymentName(final String p0);
    
    DeploymentQuery deploymentNameLike(final String p0);
    
    DeploymentQuery deploymentSource(final String p0);
    
    DeploymentQuery deploymentBefore(final Date p0);
    
    DeploymentQuery deploymentAfter(final Date p0);
    
    DeploymentQuery tenantIdIn(final String... p0);
    
    DeploymentQuery withoutTenantId();
    
    DeploymentQuery includeDeploymentsWithoutTenantId();
    
    DeploymentQuery orderByDeploymentId();
    
    DeploymentQuery orderByDeploymentName();
    
    @Deprecated
    DeploymentQuery orderByDeploymenTime();
    
    DeploymentQuery orderByDeploymentTime();
    
    DeploymentQuery orderByTenantId();
}
