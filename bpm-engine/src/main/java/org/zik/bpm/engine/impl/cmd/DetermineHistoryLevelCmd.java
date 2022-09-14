// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.HistoryLevelSetupCommand;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.List;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DetermineHistoryLevelCmd implements Command<HistoryLevel>
{
    private final List<HistoryLevel> historyLevels;
    
    public DetermineHistoryLevelCmd(final List<HistoryLevel> historyLevels) {
        this.historyLevels = historyLevels;
    }
    
    @Override
    public HistoryLevel execute(final CommandContext commandContext) {
        final Integer databaseHistoryLevel = HistoryLevelSetupCommand.databaseHistoryLevel(commandContext);
        HistoryLevel result = null;
        if (databaseHistoryLevel == null) {
            return null;
        }
        for (final HistoryLevel historyLevel : this.historyLevels) {
            if (historyLevel.getId() == databaseHistoryLevel) {
                result = historyLevel;
                break;
            }
        }
        if (result != null) {
            return result;
        }
        throw new ProcessEngineException(String.format("The configured history level with id='%s' is not registered in this config.", databaseHistoryLevel));
    }
}
