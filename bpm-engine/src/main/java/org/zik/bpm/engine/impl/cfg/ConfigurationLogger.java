// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg;

import javax.naming.NamingException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class ConfigurationLogger extends ProcessEngineLogger
{
    public ProcessEngineException invalidConfigTransactionManagerIsNull() {
        return new ProcessEngineException(this.exceptionMessage("001", "Property 'transactionManager' is null and 'transactionManagerJndiName' is not set. Please set either the 'transactionManager' property or the 'transactionManagerJndiName' property.", new Object[0]));
    }
    
    public ProcessEngineException invalidConfigCannotFindTransactionManger(final String jndiName, final NamingException e) {
        return new ProcessEngineException(this.exceptionMessage("002", "Cannot lookup instance of Jta Transaction manager in JNDI using name '{}'", new Object[] { jndiName }), e);
    }
    
    public void pluginActivated(final String pluginName, final String processEngineName) {
        this.logInfo("003", "Plugin '{}' activated on process engine '{}'", new Object[] { pluginName, processEngineName });
    }
    
    public void debugDatabaseproductName(final String databaseProductName) {
        this.logDebug("004", "Database product name {}", new Object[] { databaseProductName });
    }
    
    public void debugDatabaseType(final String databaseType) {
        this.logDebug("005", "Database type {}", new Object[] { databaseType });
    }
    
    public void usingDeprecatedHistoryLevelVariable() {
        this.logWarn("006", "Using deprecated history level 'variable'. This history level is deprecated and replaced by 'activity'. Consider using 'ACTIVITY' instead.", new Object[0]);
    }
    
    public ProcessEngineException invalidConfigDefaultUserPermissionNameForTask(final String defaultUserPermissionNameForTask, final String[] validPermissionNames) {
        return new ProcessEngineException(this.exceptionMessage("007", "Invalid value '{}' for configuration property 'defaultUserPermissionNameForTask'. Valid values are: '{}'", new Object[] { defaultUserPermissionNameForTask, validPermissionNames }));
    }
    
    public ProcessEngineException invalidPropertyValue(final String propertyName, final String propertyValue) {
        return new ProcessEngineException(this.exceptionMessage("008", "Invalid value '{}' for configuration property '{}'.", new Object[] { propertyValue, propertyName }));
    }
    
    public ProcessEngineException invalidPropertyValue(final String propertyName, final String propertyValue, final String reason) {
        return new ProcessEngineException(this.exceptionMessage("009", "Invalid value '{}' for configuration property '{}': {}.", new Object[] { propertyValue, propertyName, reason }));
    }
    
    public void invalidBatchOperation(final String operation, final String historyTimeToLive) {
        this.logWarn("010", "Invalid batch operation name '{}' with history time to live set to'{}'", new Object[] { operation, historyTimeToLive });
    }
    
    public ProcessEngineException invalidPropertyValue(final String propertyName, final String propertyValue, final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("011", "Invalid value '{}' for configuration property '{}'.", new Object[] { propertyValue, propertyName }), e);
    }
    
    public void databaseConnectionAccessException(final Exception cause) {
        this.logError("012", "Exception on accessing the database connection: {}", new Object[] { cause.getMessage() });
    }
    
    public void databaseConnectionCloseException(final Exception cause) {
        this.logError("013", "Exception on closing the database connection: {}", new Object[] { cause.getMessage() });
    }
    
    public void invalidBatchTypeForInvocationsPerBatchJob(final String batchType) {
        this.logWarn("014", "The configuration property 'invocationsPerJobByBatchType' contains an invalid batch type '{}' which is neither a custom nor a built-in batch type", new Object[] { batchType });
    }
    
    public void invalidPropertyValue(final Exception e) {
        this.logError("015", "Exception while reading configuration property: {}", new Object[] { e.getMessage() });
    }
}
