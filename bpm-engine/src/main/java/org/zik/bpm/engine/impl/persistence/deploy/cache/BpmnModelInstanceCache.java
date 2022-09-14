// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.deploy.cache;

import org.zik.bpm.engine.impl.AbstractQuery;
import org.camunda.bpm.model.xml.ModelInstance;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.ProcessDefinitionQueryImpl;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.repository.ProcessDefinition;
import java.util.List;
import org.camunda.bpm.model.bpmn.Bpmn;
import java.io.InputStream;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

public class BpmnModelInstanceCache extends ModelInstanceCache<BpmnModelInstance, ProcessDefinitionEntity>
{
    public BpmnModelInstanceCache(final CacheFactory factory, final int cacheCapacity, final ResourceDefinitionCache<ProcessDefinitionEntity> definitionCache) {
        super(factory, cacheCapacity, definitionCache);
    }
    
    @Override
    protected void throwLoadModelException(final String definitionId, final Exception e) {
        throw BpmnModelInstanceCache.LOG.loadModelException("BPMN", "process", definitionId, e);
    }
    
    @Override
    protected BpmnModelInstance readModelFromStream(final InputStream bpmnResourceInputStream) {
        return Bpmn.readModelFromStream(bpmnResourceInputStream);
    }
    
    @Override
    protected void logRemoveEntryFromDeploymentCacheFailure(final String definitionId, final Exception e) {
        BpmnModelInstanceCache.LOG.removeEntryFromDeploymentCacheFailure("process", definitionId, e);
    }
    
    @Override
    protected List<ProcessDefinition> getAllDefinitionsForDeployment(final String deploymentId) {
        final CommandContext commandContext = Context.getCommandContext();
        final List<ProcessDefinition> allDefinitionsForDeployment = commandContext.runWithoutAuthorization((Callable<List<ProcessDefinition>>)new Callable<List<ProcessDefinition>>() {
            @Override
            public List<ProcessDefinition> call() throws Exception {
                return ((AbstractQuery<T, ProcessDefinition>)new ProcessDefinitionQueryImpl().deploymentId(deploymentId)).list();
            }
        });
        return allDefinitionsForDeployment;
    }
}
