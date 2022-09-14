// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl;

import org.zik.bpm.engine.impl.scripting.engine.DefaultScriptEngineResolver;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import java.util.HashMap;
import org.zik.bpm.engine.impl.scripting.ExecutableScript;
import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.scripting.engine.ScriptEngineResolver;
import org.zik.bpm.application.ProcessApplicationInterface;

public class ProcessApplicationScriptEnvironment
{
    protected ProcessApplicationInterface processApplication;
    protected ScriptEngineResolver processApplicationScriptEngineResolver;
    protected Map<String, List<ExecutableScript>> environmentScripts;
    
    public ProcessApplicationScriptEnvironment(final ProcessApplicationInterface processApplication) {
        this.environmentScripts = new HashMap<String, List<ExecutableScript>>();
        this.processApplication = processApplication;
    }
    
    public ScriptEngine getScriptEngineForName(final String scriptEngineName, final boolean cache) {
        if (this.processApplicationScriptEngineResolver == null) {
            synchronized (this) {
                if (this.processApplicationScriptEngineResolver == null) {
                    this.processApplicationScriptEngineResolver = new DefaultScriptEngineResolver(new ScriptEngineManager(this.getProcessApplicationClassloader()));
                }
            }
        }
        return this.processApplicationScriptEngineResolver.getScriptEngine(scriptEngineName, cache);
    }
    
    public Map<String, List<ExecutableScript>> getEnvironmentScripts() {
        return this.environmentScripts;
    }
    
    protected ClassLoader getProcessApplicationClassloader() {
        return this.processApplication.getProcessApplicationClassloader();
    }
}
