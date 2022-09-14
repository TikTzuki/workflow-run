// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db;

import org.zik.bpm.engine.ProcessEngineConfiguration;

public class DbSchemaCreate
{
    public static void main(final String[] args) {
        ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault().setDatabaseSchemaUpdate("create").buildProcessEngine();
    }
}
