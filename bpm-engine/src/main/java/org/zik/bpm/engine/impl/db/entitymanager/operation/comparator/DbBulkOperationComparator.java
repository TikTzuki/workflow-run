// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.entitymanager.operation.comparator;

import org.zik.bpm.engine.impl.db.entitymanager.operation.DbBulkOperation;
import java.util.Comparator;

public class DbBulkOperationComparator implements Comparator<DbBulkOperation>
{
    @Override
    public int compare(final DbBulkOperation firstOperation, final DbBulkOperation secondOperation) {
        if (firstOperation.equals(secondOperation)) {
            return 0;
        }
        final int statementOrder = firstOperation.getStatement().compareTo(secondOperation.getStatement());
        if (statementOrder == 0) {
            return (firstOperation.hashCode() < secondOperation.hashCode()) ? -1 : 1;
        }
        return statementOrder;
    }
}
