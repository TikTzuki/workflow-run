// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cfg.jta.JtaTransactionContextFactory;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import org.zik.bpm.engine.impl.cfg.standalone.StandaloneTransactionContextFactory;
import org.zik.bpm.engine.impl.interceptor.TxContextCommandContextFactory;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContextInterceptor;
import org.zik.bpm.engine.impl.interceptor.JtaTransactionInterceptor;
import org.zik.bpm.engine.impl.interceptor.ProcessApplicationContextInterceptor;
import org.zik.bpm.engine.impl.interceptor.CommandCounterInterceptor;
import org.zik.bpm.engine.impl.interceptor.LogInterceptor;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.interceptor.CommandInterceptor;
import java.util.Collection;
import org.zik.bpm.engine.impl.interceptor.CommandContextFactory;
import javax.transaction.TransactionManager;

public class JtaProcessEngineConfiguration extends ProcessEngineConfigurationImpl
{
    private static final ConfigurationLogger LOG;
    protected TransactionManager transactionManager;
    protected String transactionManagerJndiName;
    protected CommandContextFactory dbSchemaOperationsCommandContextFactory;
    
    public JtaProcessEngineConfiguration() {
        this.transactionsExternallyManaged = true;
    }
    
    @Override
    protected void init() {
        this.initTransactionManager();
        this.initDbSchemaOperationsCommandContextFactory();
        super.init();
    }
    
    @Override
    protected Collection<? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequired() {
        final List<CommandInterceptor> defaultCommandInterceptorsTxRequired = new ArrayList<CommandInterceptor>();
        if ("cockroachdb".equals(this.databaseType)) {
            defaultCommandInterceptorsTxRequired.add(this.getCrdbRetryInterceptor());
        }
        defaultCommandInterceptorsTxRequired.add(new LogInterceptor());
        defaultCommandInterceptorsTxRequired.add(new CommandCounterInterceptor(this));
        defaultCommandInterceptorsTxRequired.add(new ProcessApplicationContextInterceptor(this));
        defaultCommandInterceptorsTxRequired.add(new JtaTransactionInterceptor(this.transactionManager, false, this));
        defaultCommandInterceptorsTxRequired.add(new CommandContextInterceptor(this.commandContextFactory, this));
        return defaultCommandInterceptorsTxRequired;
    }
    
    @Override
    protected Collection<? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequiresNew() {
        final List<CommandInterceptor> defaultCommandInterceptorsTxRequiresNew = new ArrayList<CommandInterceptor>();
        if ("cockroachdb".equals(this.databaseType)) {
            defaultCommandInterceptorsTxRequiresNew.add(this.getCrdbRetryInterceptor());
        }
        defaultCommandInterceptorsTxRequiresNew.add(new LogInterceptor());
        defaultCommandInterceptorsTxRequiresNew.add(new CommandCounterInterceptor(this));
        defaultCommandInterceptorsTxRequiresNew.add(new ProcessApplicationContextInterceptor(this));
        defaultCommandInterceptorsTxRequiresNew.add(new JtaTransactionInterceptor(this.transactionManager, true, this));
        defaultCommandInterceptorsTxRequiresNew.add(new CommandContextInterceptor(this.commandContextFactory, this, true));
        return defaultCommandInterceptorsTxRequiresNew;
    }
    
    @Override
    protected void initCommandExecutorDbSchemaOperations() {
        if (this.commandExecutorSchemaOperations == null) {
            final List<CommandInterceptor> commandInterceptorsDbSchemaOperations = new ArrayList<CommandInterceptor>();
            commandInterceptorsDbSchemaOperations.add(new LogInterceptor());
            commandInterceptorsDbSchemaOperations.add(new CommandCounterInterceptor(this));
            commandInterceptorsDbSchemaOperations.add(new CommandContextInterceptor(this.dbSchemaOperationsCommandContextFactory, this));
            commandInterceptorsDbSchemaOperations.add(this.actualCommandExecutor);
            this.commandExecutorSchemaOperations = this.initInterceptorChain(commandInterceptorsDbSchemaOperations);
        }
    }
    
    protected void initDbSchemaOperationsCommandContextFactory() {
        if (this.dbSchemaOperationsCommandContextFactory == null) {
            final TxContextCommandContextFactory cmdContextFactory = new TxContextCommandContextFactory();
            cmdContextFactory.setProcessEngineConfiguration(this);
            cmdContextFactory.setTransactionContextFactory(new StandaloneTransactionContextFactory());
            this.dbSchemaOperationsCommandContextFactory = cmdContextFactory;
        }
    }
    
    protected void initTransactionManager() {
        if (this.transactionManager == null) {
            if (this.transactionManagerJndiName == null || this.transactionManagerJndiName.length() == 0) {
                throw JtaProcessEngineConfiguration.LOG.invalidConfigTransactionManagerIsNull();
            }
            try {
                this.transactionManager = (TransactionManager)new InitialContext().lookup(this.transactionManagerJndiName);
            }
            catch (NamingException e) {
                throw JtaProcessEngineConfiguration.LOG.invalidConfigCannotFindTransactionManger(this.transactionManagerJndiName + "'.", e);
            }
        }
    }
    
    @Override
    protected void initTransactionContextFactory() {
        if (this.transactionContextFactory == null) {
            this.transactionContextFactory = new JtaTransactionContextFactory(this.transactionManager);
        }
    }
    
    public TransactionManager getTransactionManager() {
        return this.transactionManager;
    }
    
    public void setTransactionManager(final TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    public String getTransactionManagerJndiName() {
        return this.transactionManagerJndiName;
    }
    
    public void setTransactionManagerJndiName(final String transactionManagerJndiName) {
        this.transactionManagerJndiName = transactionManagerJndiName;
    }
    
    public CommandContextFactory getDbSchemaOperationsCommandContextFactory() {
        return this.dbSchemaOperationsCommandContextFactory;
    }
    
    public void setDbSchemaOperationsCommandContextFactory(final CommandContextFactory dbSchemaOperationsCommandContextFactory) {
        this.dbSchemaOperationsCommandContextFactory = dbSchemaOperationsCommandContextFactory;
    }
    
    static {
        LOG = ProcessEngineLogger.CONFIG_LOGGER;
    }
}
