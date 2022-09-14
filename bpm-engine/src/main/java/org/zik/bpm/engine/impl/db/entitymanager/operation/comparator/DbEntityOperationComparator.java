// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.entitymanager.operation.comparator;

import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbEntityOperation;
import java.util.Comparator;

public class DbEntityOperationComparator implements Comparator<DbEntityOperation>
{
    @Override
    public int compare(final DbEntityOperation firstOperation, final DbEntityOperation secondOperation) {
        if (firstOperation.equals(secondOperation)) {
            return 0;
        }
        final DbEntity firstEntity = firstOperation.getEntity();
        final DbEntity secondEntity = secondOperation.getEntity();
        return firstEntity.getId().compareTo(secondEntity.getId());
    }
}
