// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db;

import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.ProcessEngines;
import org.zik.bpm.engine.impl.ProcessEngineImpl;

public class DbSchemaDrop
{
    public static void main(final String[] args) {
        final ProcessEngineImpl processEngine = (ProcessEngineImpl)ProcessEngines.getDefaultProcessEngine();
        final CommandExecutor commandExecutor = processEngine.getProcessEngineConfiguration().getCommandExecutorTxRequired();
        commandExecutor.execute((Command<Object>)new Command<Object>() {
            @Override
            public Object execute(final CommandContext commandContext) {
                commandContext.getSession(PersistenceSession.class).dbSchemaDrop();
                return null;
            }
        });
        processEngine.close();
    }
}
