// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.entitymanager.operation;

import org.zik.bpm.engine.impl.db.entitymanager.operation.comparator.DbBulkOperationComparator;
import org.zik.bpm.engine.impl.db.entitymanager.operation.comparator.DbEntityOperationComparator;
import org.zik.bpm.engine.impl.db.entitymanager.operation.comparator.EntityTypeComparatorForModifications;
import org.zik.bpm.engine.impl.db.entitymanager.operation.comparator.EntityTypeComparatorForInserts;
import java.util.Set;
import java.util.Iterator;
import java.util.Collection;
import org.zik.bpm.engine.impl.db.HasDbReferences;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.TreeMap;
import java.util.LinkedHashSet;
import java.util.SortedSet;
import java.util.SortedMap;
import java.util.Comparator;

public class DbOperationManager
{
    public static Comparator<Class<?>> INSERT_TYPE_COMPARATOR;
    public static Comparator<Class<?>> MODIFICATION_TYPE_COMPARATOR;
    public static Comparator<DbEntityOperation> INSERT_OPERATION_COMPARATOR;
    public static Comparator<DbEntityOperation> MODIFICATION_OPERATION_COMPARATOR;
    public static Comparator<DbBulkOperation> BULK_OPERATION_COMPARATOR;
    public SortedMap<Class<?>, SortedSet<DbEntityOperation>> inserts;
    public SortedMap<Class<?>, SortedSet<DbEntityOperation>> updates;
    public SortedMap<Class<?>, SortedSet<DbEntityOperation>> deletes;
    public SortedMap<Class<?>, SortedSet<DbBulkOperation>> bulkOperations;
    public LinkedHashSet<DbBulkOperation> bulkOperationsInsertionOrder;
    
    public DbOperationManager() {
        this.inserts = new TreeMap<Class<?>, SortedSet<DbEntityOperation>>(DbOperationManager.INSERT_TYPE_COMPARATOR);
        this.updates = new TreeMap<Class<?>, SortedSet<DbEntityOperation>>(DbOperationManager.MODIFICATION_TYPE_COMPARATOR);
        this.deletes = new TreeMap<Class<?>, SortedSet<DbEntityOperation>>(DbOperationManager.MODIFICATION_TYPE_COMPARATOR);
        this.bulkOperations = new TreeMap<Class<?>, SortedSet<DbBulkOperation>>(DbOperationManager.MODIFICATION_TYPE_COMPARATOR);
        this.bulkOperationsInsertionOrder = new LinkedHashSet<DbBulkOperation>();
    }
    
    public boolean addOperation(final DbEntityOperation newOperation) {
        if (newOperation.getOperationType() == DbOperationType.INSERT) {
            return this.getInsertsForType(newOperation.getEntityType(), true).add(newOperation);
        }
        if (newOperation.getOperationType() == DbOperationType.DELETE) {
            return this.getDeletesByType(newOperation.getEntityType(), true).add(newOperation);
        }
        return this.getUpdatesByType(newOperation.getEntityType(), true).add(newOperation);
    }
    
    protected SortedSet<DbEntityOperation> getDeletesByType(final Class<? extends DbEntity> type, final boolean create) {
        SortedSet<DbEntityOperation> deletesByType = this.deletes.get(type);
        if (deletesByType == null && create) {
            deletesByType = new TreeSet<DbEntityOperation>(DbOperationManager.MODIFICATION_OPERATION_COMPARATOR);
            this.deletes.put(type, deletesByType);
        }
        return deletesByType;
    }
    
    protected SortedSet<DbEntityOperation> getUpdatesByType(final Class<? extends DbEntity> type, final boolean create) {
        SortedSet<DbEntityOperation> updatesByType = this.updates.get(type);
        if (updatesByType == null && create) {
            updatesByType = new TreeSet<DbEntityOperation>(DbOperationManager.MODIFICATION_OPERATION_COMPARATOR);
            this.updates.put(type, updatesByType);
        }
        return updatesByType;
    }
    
    protected SortedSet<DbEntityOperation> getInsertsForType(final Class<? extends DbEntity> type, final boolean create) {
        SortedSet<DbEntityOperation> insertsByType = this.inserts.get(type);
        if (insertsByType == null && create) {
            insertsByType = new TreeSet<DbEntityOperation>(DbOperationManager.INSERT_OPERATION_COMPARATOR);
            this.inserts.put(type, insertsByType);
        }
        return insertsByType;
    }
    
    public boolean addOperation(final DbBulkOperation newOperation) {
        SortedSet<DbBulkOperation> bulksByType = this.bulkOperations.get(newOperation.getEntityType());
        if (bulksByType == null) {
            bulksByType = new TreeSet<DbBulkOperation>(DbOperationManager.BULK_OPERATION_COMPARATOR);
            this.bulkOperations.put(newOperation.getEntityType(), bulksByType);
        }
        return bulksByType.add(newOperation);
    }
    
    public boolean addOperationPreserveOrder(final DbBulkOperation newOperation) {
        return this.bulkOperationsInsertionOrder.add(newOperation);
    }
    
    public List<DbOperation> calculateFlush() {
        final List<DbOperation> flush = new ArrayList<DbOperation>();
        this.addSortedInserts(flush);
        this.addSortedModifications(flush);
        this.determineDependencies(flush);
        return flush;
    }
    
    protected void addSortedInserts(final List<DbOperation> flush) {
        for (final Map.Entry<Class<?>, SortedSet<DbEntityOperation>> operationsForType : this.inserts.entrySet()) {
            if (HasDbReferences.class.isAssignableFrom(operationsForType.getKey())) {
                flush.addAll(this.sortByReferences(operationsForType.getValue()));
            }
            else {
                flush.addAll(operationsForType.getValue());
            }
        }
    }
    
    protected void addSortedModifications(final List<DbOperation> flush) {
        final SortedSet<Class<?>> modifiedEntityTypes = new TreeSet<Class<?>>(DbOperationManager.MODIFICATION_TYPE_COMPARATOR);
        modifiedEntityTypes.addAll((Collection<?>)this.updates.keySet());
        modifiedEntityTypes.addAll((Collection<?>)this.deletes.keySet());
        modifiedEntityTypes.addAll((Collection<?>)this.bulkOperations.keySet());
        for (final Class<?> type : modifiedEntityTypes) {
            this.addSortedModificationsForType(type, this.updates.get(type), flush);
            this.addSortedModificationsForType(type, this.deletes.get(type), flush);
            final SortedSet<DbBulkOperation> bulkOperationsForType = this.bulkOperations.get(type);
            if (bulkOperationsForType != null) {
                flush.addAll(bulkOperationsForType);
            }
        }
        if (this.bulkOperationsInsertionOrder != null) {
            flush.addAll(this.bulkOperationsInsertionOrder);
        }
    }
    
    protected void addSortedModificationsForType(final Class<?> type, final SortedSet<DbEntityOperation> preSortedOperations, final List<DbOperation> flush) {
        if (preSortedOperations != null) {
            if (HasDbReferences.class.isAssignableFrom(type)) {
                flush.addAll(this.sortByReferences(preSortedOperations));
            }
            else {
                flush.addAll(preSortedOperations);
            }
        }
    }
    
    protected List<DbEntityOperation> sortByReferences(final SortedSet<DbEntityOperation> preSorted) {
        final List<DbEntityOperation> opList = new ArrayList<DbEntityOperation>(preSorted);
        for (int i = 0; i < opList.size(); ++i) {
            final DbEntityOperation currentOperation = opList.get(i);
            final DbEntity currentEntity = currentOperation.getEntity();
            final Set<String> currentReferences = currentOperation.getFlushRelevantEntityReferences();
            int moveTo = i;
            for (int k = i + 1; k < opList.size(); ++k) {
                final DbEntityOperation otherOperation = opList.get(k);
                final DbEntity otherEntity = otherOperation.getEntity();
                final Set<String> otherReferences = otherOperation.getFlushRelevantEntityReferences();
                if (currentOperation.getOperationType() == DbOperationType.INSERT) {
                    if (currentReferences != null && currentReferences.contains(otherEntity.getId())) {
                        moveTo = k;
                        break;
                    }
                }
                else if (otherReferences != null && otherReferences.contains(currentEntity.getId())) {
                    moveTo = k;
                }
            }
            if (moveTo > i) {
                opList.remove(i);
                opList.add(moveTo, currentOperation);
                --i;
            }
        }
        return opList;
    }
    
    protected void determineDependencies(final List<DbOperation> flush) {
        final TreeSet<DbEntityOperation> defaultValue = new TreeSet<DbEntityOperation>();
        for (final DbOperation operation : flush) {
            if (operation instanceof DbEntityOperation) {
                final DbEntity entity = ((DbEntityOperation)operation).getEntity();
                if (!(entity instanceof HasDbReferences)) {
                    continue;
                }
                final Map<String, Class> dependentEntities = ((HasDbReferences)entity).getDependentEntities();
                if (dependentEntities == null) {
                    continue;
                }
                final DbOperation dependency;
                dependentEntities.forEach((id, type) -> this.deletes.getOrDefault(type, defaultValue).forEach(o -> {
                    if (id.equals(o.getEntity().getId())) {
                        o.setDependency(dependency);
                    }
                }));
            }
        }
    }
    
    static {
        DbOperationManager.INSERT_TYPE_COMPARATOR = new EntityTypeComparatorForInserts();
        DbOperationManager.MODIFICATION_TYPE_COMPARATOR = new EntityTypeComparatorForModifications();
        DbOperationManager.INSERT_OPERATION_COMPARATOR = new DbEntityOperationComparator();
        DbOperationManager.MODIFICATION_OPERATION_COMPARATOR = new DbEntityOperationComparator();
        DbOperationManager.BULK_OPERATION_COMPARATOR = new DbBulkOperationComparator();
    }
}
