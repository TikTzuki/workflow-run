// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting;

import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.zik.bpm.engine.impl.util.ResourceUtil;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.delegate.Expression;

public class DynamicResourceExecutableScript extends DynamicExecutableScript
{
    public DynamicResourceExecutableScript(final String language, final Expression scriptResourceExpression) {
        super(scriptResourceExpression, language);
    }
    
    @Override
    public String getScriptSource(final VariableScope variableScope) {
        final String scriptPath = this.evaluateExpression(variableScope);
        return ResourceUtil.loadResourceContent(scriptPath, this.getDeployment());
    }
    
    protected DeploymentEntity getDeployment() {
        return Context.getBpmnExecutionContext().getDeployment();
    }
}
