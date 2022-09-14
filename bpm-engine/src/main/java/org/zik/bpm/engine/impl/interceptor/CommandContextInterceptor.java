// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

import org.zik.bpm.engine.impl.context.ProcessEngineContextImpl;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.cmd.CommandLogger;

public class CommandContextInterceptor extends CommandInterceptor
{
    private static final CommandLogger LOG;
    protected CommandContextFactory commandContextFactory;
    protected ProcessEngineConfigurationImpl processEngineConfiguration;
    protected boolean alwaysOpenNew;
    
    public CommandContextInterceptor() {
    }
    
    public CommandContextInterceptor(final CommandContextFactory commandContextFactory, final ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.commandContextFactory = commandContextFactory;
        this.processEngineConfiguration = processEngineConfiguration;
    }
    
    public CommandContextInterceptor(final CommandContextFactory commandContextFactory, final ProcessEngineConfigurationImpl processEngineConfiguration, final boolean alwaysOpenNew) {
        this(commandContextFactory, processEngineConfiguration);
        this.alwaysOpenNew = alwaysOpenNew;
    }
    
    @Override
    public <T> T execute(final Command<T> command) {
        CommandContext context = null;
        if (!this.alwaysOpenNew) {
            final CommandContext existingCommandContext = Context.getCommandContext();
            if (existingCommandContext != null && this.isFromSameEngine(existingCommandContext)) {
                context = existingCommandContext;
            }
        }
        final boolean isNew = ProcessEngineContextImpl.consume();
        final boolean openNew = context == null || isNew;
        final CommandInvocationContext commandInvocationContext = new CommandInvocationContext(command, this.processEngineConfiguration);
        Context.setCommandInvocationContext(commandInvocationContext);
        try {
            if (openNew) {
                CommandContextInterceptor.LOG.debugOpeningNewCommandContext();
                context = this.commandContextFactory.createCommandContext();
            }
            else {
                CommandContextInterceptor.LOG.debugReusingExistingCommandContext();
            }
            Context.setCommandContext(context);
            Context.setProcessEngineConfiguration(this.processEngineConfiguration);
            return this.next.execute(command);
        }
        catch (Throwable t) {
            commandInvocationContext.trySetThrowable(t);
            try {
                if (openNew) {
                    CommandContextInterceptor.LOG.closingCommandContext();
                    context.close(commandInvocationContext);
                }
                else {
                    commandInvocationContext.rethrow();
                }
            }
            finally {
                Context.removeCommandInvocationContext();
                Context.removeCommandContext();
                Context.removeProcessEngineConfiguration();
                ProcessEngineContextImpl.set(isNew);
            }
        }
        finally {
            try {
                if (openNew) {
                    CommandContextInterceptor.LOG.closingCommandContext();
                    context.close(commandInvocationContext);
                }
                else {
                    commandInvocationContext.rethrow();
                }
            }
            finally {
                Context.removeCommandInvocationContext();
                Context.removeCommandContext();
                Context.removeProcessEngineConfiguration();
                ProcessEngineContextImpl.set(isNew);
            }
        }
        return null;
    }
    
    protected boolean isFromSameEngine(final CommandContext existingCommandContext) {
        return this.processEngineConfiguration == existingCommandContext.getProcessEngineConfiguration();
    }
    
    public CommandContextFactory getCommandContextFactory() {
        return this.commandContextFactory;
    }
    
    public void setCommandContextFactory(final CommandContextFactory commandContextFactory) {
        this.commandContextFactory = commandContextFactory;
    }
    
    public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
        return this.processEngineConfiguration;
    }
    
    public void setProcessEngineContext(final ProcessEngineConfigurationImpl processEngineContext) {
        this.processEngineConfiguration = processEngineContext;
    }
    
    static {
        LOG = CommandLogger.CMD_LOGGER;
    }
}
