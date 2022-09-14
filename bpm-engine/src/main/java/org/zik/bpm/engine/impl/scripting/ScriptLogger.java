// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting;

import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class ScriptLogger extends ProcessEngineLogger
{
    public void debugEvaluatingCompiledScript(final String language) {
        this.logDebug("001", "Evaluating compiled script {} in language", new Object[] { language });
    }
    
    public void debugCompiledScriptUsing(final String language) {
        this.logDebug("002", "Compiled script using {} script language", new Object[] { language });
    }
    
    public void debugEvaluatingNonCompiledScript(final String scriptSource) {
        this.logDebug("001", "Evaluating non-compiled script {}", new Object[] { scriptSource });
    }
}
