// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg;

public class StandaloneInMemProcessEngineConfiguration extends StandaloneProcessEngineConfiguration
{
    public StandaloneInMemProcessEngineConfiguration() {
        this.databaseSchemaUpdate = "create-drop";
        this.jdbcUrl = "jdbc:h2:mem:camunda";
    }
}
