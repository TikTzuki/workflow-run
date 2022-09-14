// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg;

import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContextInterceptor;
import org.zik.bpm.engine.impl.interceptor.ProcessApplicationContextInterceptor;
import org.zik.bpm.engine.impl.interceptor.CommandCounterInterceptor;
import org.zik.bpm.engine.impl.interceptor.LogInterceptor;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.interceptor.CommandInterceptor;
import java.util.Collection;

public class StandaloneProcessEngineConfiguration extends ProcessEngineConfigurationImpl
{
    @Override
    protected Collection<? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequired() {
        final List<CommandInterceptor> defaultCommandInterceptorsTxRequired = new ArrayList<CommandInterceptor>();
        if ("cockroachdb".equals(this.databaseType)) {
            defaultCommandInterceptorsTxRequired.add(this.getCrdbRetryInterceptor());
        }
        defaultCommandInterceptorsTxRequired.add(new LogInterceptor());
        defaultCommandInterceptorsTxRequired.add(new CommandCounterInterceptor(this));
        defaultCommandInterceptorsTxRequired.add(new ProcessApplicationContextInterceptor(this));
        defaultCommandInterceptorsTxRequired.add(new CommandContextInterceptor(this.commandContextFactory, this));
        return defaultCommandInterceptorsTxRequired;
    }
    
    @Override
    protected Collection<? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequiresNew() {
        final List<CommandInterceptor> defaultCommandInterceptorsTxRequired = new ArrayList<CommandInterceptor>();
        if ("cockroachdb".equals(this.databaseType)) {
            defaultCommandInterceptorsTxRequired.add(this.getCrdbRetryInterceptor());
        }
        defaultCommandInterceptorsTxRequired.add(new LogInterceptor());
        defaultCommandInterceptorsTxRequired.add(new CommandCounterInterceptor(this));
        defaultCommandInterceptorsTxRequired.add(new ProcessApplicationContextInterceptor(this));
        defaultCommandInterceptorsTxRequired.add(new CommandContextInterceptor(this.commandContextFactory, this, true));
        return defaultCommandInterceptorsTxRequired;
    }
}
