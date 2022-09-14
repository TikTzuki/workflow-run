// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.entitymanager;

import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import org.zik.bpm.engine.impl.db.DbEntity;

public interface OptimisticLockingListener
{
    Class<? extends DbEntity> getEntityType();
    
    OptimisticLockingResult failedOperation(final DbOperation p0);
}
