// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class PropertyManager extends AbstractManager
{
    public PropertyEntity findPropertyById(final String propertyId) {
        return this.getDbEntityManager().selectById(PropertyEntity.class, propertyId);
    }
    
    public void acquireExclusiveLock() {
        this.getDbEntityManager().lock("lockDeploymentLockProperty");
    }
    
    public void acquireExclusiveLockForHistoryCleanupJob() {
        this.getDbEntityManager().lock("lockHistoryCleanupJobLockProperty");
    }
    
    public void acquireExclusiveLockForStartup() {
        this.getDbEntityManager().lock("lockStartupLockProperty");
    }
    
    public void acquireExclusiveLockForTelemetry() {
        this.getDbEntityManager().lock("lockTelemetryLockProperty");
    }
    
    public void acquireExclusiveLockForInstallationId() {
        this.getDbEntityManager().lock("lockInstallationIdLockProperty");
    }
}
