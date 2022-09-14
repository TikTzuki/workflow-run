// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting;

import org.zik.bpm.engine.delegate.Expression;

public class ScriptFactory
{
    public ExecutableScript createScriptFromResource(final String language, final String resource) {
        return new ResourceExecutableScript(language, resource);
    }
    
    public ExecutableScript createScriptFromResource(final String language, final Expression resourceExpression) {
        return new DynamicResourceExecutableScript(language, resourceExpression);
    }
    
    public ExecutableScript createScriptFromSource(final String language, final String source) {
        return new SourceExecutableScript(language, source);
    }
    
    public ExecutableScript createScriptFromSource(final String language, final Expression sourceExpression) {
        return new DynamicSourceExecutableScript(language, sourceExpression);
    }
}
