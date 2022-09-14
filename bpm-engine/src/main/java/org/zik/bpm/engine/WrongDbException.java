// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import org.zik.bpm.engine.ProcessEngineException;

public class WrongDbException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    String libraryVersion;
    String dbVersion;
    
    public WrongDbException(final String libraryVersion, final String dbVersion) {
        this("version mismatch: activiti library version is '" + libraryVersion + "', db version is " + dbVersion + " Hint: Set <property name=\"databaseSchemaUpdate\" to value=\"true\" or value=\"create-drop\" (use create-drop for testing only!) in bean processEngineConfiguration in camunda.cfg.xml for automatic schema creation", libraryVersion, dbVersion);
    }
    
    public WrongDbException(final String exceptionMessage, final String libraryVersion, final String dbVersion) {
        super(exceptionMessage);
        this.libraryVersion = libraryVersion;
        this.dbVersion = dbVersion;
    }
    
    public String getLibraryVersion() {
        return this.libraryVersion;
    }
    
    public String getDbVersion() {
        return this.dbVersion;
    }
}
