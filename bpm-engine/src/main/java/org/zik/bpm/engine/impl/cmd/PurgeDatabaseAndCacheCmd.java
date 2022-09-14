// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Arrays;
import java.util.Map;
import java.util.Iterator;
import org.zik.bpm.engine.impl.db.entitymanager.DbEntityManager;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbBulkOperation;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperationType;
import java.util.HashMap;
import org.zik.bpm.engine.impl.persistence.deploy.cache.CachePurgeReport;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.management.DatabasePurgeReport;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.List;
import java.io.Serializable;
import org.zik.bpm.engine.impl.management.PurgeReport;
import org.zik.bpm.engine.impl.interceptor.Command;

public class PurgeDatabaseAndCacheCmd implements Command<PurgeReport>, Serializable
{
    protected static final String DELETE_TABLE_DATA = "deleteTableData";
    protected static final String SELECT_TABLE_COUNT = "selectTableCount";
    protected static final String TABLE_NAME = "tableName";
    protected static final String EMPTY_STRING = "";
    public static final List<String> TABLENAMES_EXCLUDED_FROM_DB_CLEAN_CHECK;
    
    @Override
    public PurgeReport execute(final CommandContext commandContext) {
        final PurgeReport purgeReport = new PurgeReport();
        final DatabasePurgeReport databasePurgeReport = this.purgeDatabase(commandContext);
        purgeReport.setDatabasePurgeReport(databasePurgeReport);
        final DeploymentCache deploymentCache = commandContext.getProcessEngineConfiguration().getDeploymentCache();
        final CachePurgeReport cachePurgeReport = deploymentCache.purgeCache();
        purgeReport.setCachePurgeReport(cachePurgeReport);
        return purgeReport;
    }
    
    private DatabasePurgeReport purgeDatabase(final CommandContext commandContext) {
        final DbEntityManager dbEntityManager = commandContext.getDbEntityManager();
        dbEntityManager.setIgnoreForeignKeysForNextFlush(true);
        final List<String> tablesNames = dbEntityManager.getTableNamesPresentInDatabase();
        final String databaseTablePrefix = commandContext.getProcessEngineConfiguration().getDatabaseTablePrefix().trim();
        final DatabasePurgeReport databasePurgeReport = new DatabasePurgeReport();
        for (final String tableName : tablesNames) {
            final String tableNameWithoutPrefix = tableName.replace(databaseTablePrefix, "");
            if (!PurgeDatabaseAndCacheCmd.TABLENAMES_EXCLUDED_FROM_DB_CLEAN_CHECK.contains(tableNameWithoutPrefix)) {
                final Map<String, String> param = new HashMap<String, String>();
                param.put("tableName", tableName);
                final Long count = (Long)dbEntityManager.selectOne("selectTableCount", param);
                if (count <= 0L) {
                    continue;
                }
                if (tableNameWithoutPrefix.equals("ACT_GE_BYTEARRAY") && commandContext.getResourceManager().findLicenseKeyResource() != null) {
                    if (count != 1L) {
                        final DbBulkOperation purgeByteArrayPreserveLicenseKeyBulkOp = new DbBulkOperation(DbOperationType.DELETE_BULK, ByteArrayEntity.class, "purgeTablePreserveLicenseKey", "camunda-license-key-id");
                        databasePurgeReport.addPurgeInformation(tableName, Long.valueOf(count - 1L));
                        dbEntityManager.getDbOperationManager().addOperation(purgeByteArrayPreserveLicenseKeyBulkOp);
                    }
                    databasePurgeReport.setDbContainsLicenseKey(true);
                }
                else {
                    databasePurgeReport.addPurgeInformation(tableName, count);
                    final List<Class<? extends DbEntity>> entities = commandContext.getTableDataManager().getEntities(tableName);
                    if (entities.isEmpty()) {
                        throw new ProcessEngineException("No mapped implementation of " + DbEntity.class.getName() + " was found for: " + tableName);
                    }
                    final Class<? extends DbEntity> entity = entities.get(0);
                    final DbBulkOperation deleteBulkOp = new DbBulkOperation(DbOperationType.DELETE_BULK, entity, "deleteTableData", param);
                    dbEntityManager.getDbOperationManager().addOperation(deleteBulkOp);
                }
            }
        }
        return databasePurgeReport;
    }
    
    static {
        TABLENAMES_EXCLUDED_FROM_DB_CLEAN_CHECK = Arrays.asList("ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG");
    }
}
