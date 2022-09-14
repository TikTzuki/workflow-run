// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.entitymanager.operation.comparator;

public class EntityTypeComparatorForInserts extends EntityTypeComparatorForModifications
{
    @Override
    public int compare(final Class<?> firstEntityType, final Class<?> secondEntityType) {
        return super.compare(firstEntityType, secondEntityType) * -1;
    }
}
