// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.cmd.CommandLogger;
import org.zik.bpm.engine.history.HistoricVariableInstance;
import org.zik.bpm.engine.history.NativeHistoricVariableInstanceQuery;

public class NativeHistoricVariableInstanceQueryImpl extends AbstractNativeQuery<NativeHistoricVariableInstanceQuery, HistoricVariableInstance> implements NativeHistoricVariableInstanceQuery
{
    private static final CommandLogger LOG;
    private static final long serialVersionUID = 1L;
    protected boolean isCustomObjectDeserializationEnabled;
    
    public NativeHistoricVariableInstanceQueryImpl(final CommandContext commandContext) {
        super(commandContext);
        this.isCustomObjectDeserializationEnabled = true;
    }
    
    public NativeHistoricVariableInstanceQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.isCustomObjectDeserializationEnabled = true;
    }
    
    @Override
    public NativeHistoricVariableInstanceQuery disableCustomObjectDeserialization() {
        this.isCustomObjectDeserializationEnabled = false;
        return this;
    }
    
    @Override
    public List<HistoricVariableInstance> executeList(final CommandContext commandContext, final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        final List<HistoricVariableInstance> historicVariableInstances = commandContext.getHistoricVariableInstanceManager().findHistoricVariableInstancesByNativeQuery(parameterMap, firstResult, maxResults);
        if (historicVariableInstances != null) {
            for (final HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
                final HistoricVariableInstanceEntity variableInstanceEntity = (HistoricVariableInstanceEntity)historicVariableInstance;
                try {
                    variableInstanceEntity.getTypedValue(this.isCustomObjectDeserializationEnabled);
                }
                catch (Exception t) {
                    NativeHistoricVariableInstanceQueryImpl.LOG.exceptionWhileGettingValueForVariable(t);
                }
            }
        }
        return historicVariableInstances;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext, final Map<String, Object> parameterMap) {
        return commandContext.getHistoricVariableInstanceManager().findHistoricVariableInstanceCountByNativeQuery(parameterMap);
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
