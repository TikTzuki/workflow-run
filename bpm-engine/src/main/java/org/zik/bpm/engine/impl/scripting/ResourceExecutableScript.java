// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting;

import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.zik.bpm.engine.impl.util.ResourceUtil;
import org.zik.bpm.engine.impl.context.Context;
import javax.script.Bindings;
import org.zik.bpm.engine.delegate.VariableScope;
import javax.script.ScriptEngine;

public class ResourceExecutableScript extends SourceExecutableScript
{
    protected String scriptResource;
    
    public ResourceExecutableScript(final String language, final String scriptResource) {
        super(language, (String)null);
        this.scriptResource = scriptResource;
    }
    
    @Override
    public Object evaluate(final ScriptEngine engine, final VariableScope variableScope, final Bindings bindings) {
        if (this.scriptSource == null) {
            this.loadScriptSource();
        }
        return super.evaluate(engine, variableScope, bindings);
    }
    
    protected synchronized void loadScriptSource() {
        if (this.getScriptSource() == null) {
            final DeploymentEntity deployment = Context.getCoreExecutionContext().getDeployment();
            final String source = ResourceUtil.loadResourceContent(this.scriptResource, deployment);
            this.setScriptSource(source);
        }
    }
}
