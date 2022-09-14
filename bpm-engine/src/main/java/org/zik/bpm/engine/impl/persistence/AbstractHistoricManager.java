// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;

public class AbstractHistoricManager extends AbstractManager
{
    protected static final EnginePersistenceLogger LOG;
    protected HistoryLevel historyLevel;
    protected boolean isHistoryEnabled;
    protected boolean isHistoryLevelFullEnabled;
    
    public AbstractHistoricManager() {
        this.historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
        this.isHistoryEnabled = !this.historyLevel.equals(HistoryLevel.HISTORY_LEVEL_NONE);
        this.isHistoryLevelFullEnabled = this.historyLevel.equals(HistoryLevel.HISTORY_LEVEL_FULL);
    }
    
    protected void checkHistoryEnabled() {
        if (!this.isHistoryEnabled) {
            throw AbstractHistoricManager.LOG.disabledHistoryException();
        }
    }
    
    public boolean isHistoryEnabled() {
        return this.isHistoryEnabled;
    }
    
    public boolean isHistoryLevelFullEnabled() {
        return this.isHistoryLevelFullEnabled;
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
