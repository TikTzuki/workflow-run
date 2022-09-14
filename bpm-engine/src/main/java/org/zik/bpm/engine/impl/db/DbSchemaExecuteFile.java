// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.ProcessEngineConfiguration;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.util.EnsureUtil;

public class DbSchemaExecuteFile
{
    protected static final EnginePersistenceLogger LOG;
    
    public static void main(final String[] args) {
        if (args.length != 2) {
            throw DbSchemaExecuteFile.LOG.invokeSchemaResourceToolException(args.length);
        }
        final String configurationFileResourceName = args[0];
        final String schemaFileResourceName = args[1];
        EnsureUtil.ensureNotNull("Process engine configuration file name cannot be null", "configurationFileResourceName", configurationFileResourceName);
        EnsureUtil.ensureNotNull("Schema resource file name cannot be null", "schemaFileResourceName", schemaFileResourceName);
        final ProcessEngineConfigurationImpl configuration = (ProcessEngineConfigurationImpl)ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(configurationFileResourceName);
        final ProcessEngine processEngine = configuration.buildProcessEngine();
        configuration.getCommandExecutorTxRequired().execute(new Command<Void>() {
            @Override
            public Void execute(final CommandContext commandContext) {
                commandContext.getDbSqlSession().executeSchemaResource(schemaFileResourceName);
                return null;
            }
        });
        processEngine.close();
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
