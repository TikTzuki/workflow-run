// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting.engine;

import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import javax.script.ScriptEngineFactory;
import java.util.HashMap;
import javax.script.ScriptEngine;
import java.util.Map;
import javax.script.ScriptEngineManager;

public class DefaultScriptEngineResolver implements ScriptEngineResolver
{
    protected final ScriptEngineManager scriptEngineManager;
    protected Map<String, ScriptEngine> cachedEngines;
    
    public DefaultScriptEngineResolver(final ScriptEngineManager scriptEngineManager) {
        this.cachedEngines = new HashMap<String, ScriptEngine>();
        this.scriptEngineManager = scriptEngineManager;
    }
    
    @Override
    public void addScriptEngineFactory(final ScriptEngineFactory scriptEngineFactory) {
        this.scriptEngineManager.registerEngineName(scriptEngineFactory.getEngineName(), scriptEngineFactory);
    }
    
    @Override
    public ScriptEngineManager getScriptEngineManager() {
        return this.scriptEngineManager;
    }
    
    @Override
    public ScriptEngine getScriptEngine(final String language, final boolean resolveFromCache) {
        ScriptEngine scriptEngine = null;
        if (resolveFromCache) {
            scriptEngine = this.cachedEngines.get(language);
            if (scriptEngine == null) {
                scriptEngine = this.getScriptEngine(language);
                if (scriptEngine != null && this.isCachable(scriptEngine)) {
                    this.cachedEngines.put(language, scriptEngine);
                }
            }
        }
        else {
            scriptEngine = this.getScriptEngine(language);
        }
        return scriptEngine;
    }
    
    protected ScriptEngine getScriptEngine(final String language) {
        ScriptEngine scriptEngine = null;
        if ("javascript".equalsIgnoreCase(language) || "ecmascript".equalsIgnoreCase(language)) {
            scriptEngine = this.getJavaScriptScriptEngine(language);
        }
        else {
            scriptEngine = this.scriptEngineManager.getEngineByName(language);
        }
        if (scriptEngine != null) {
            this.configureScriptEngines(language, scriptEngine);
        }
        return scriptEngine;
    }
    
    protected ScriptEngine getJavaScriptScriptEngine(final String language) {
        ScriptEngine scriptEngine = null;
        final ProcessEngineConfigurationImpl config = Context.getProcessEngineConfiguration();
        if (config != null && config.getScriptEngineNameJavaScript() != null) {
            scriptEngine = this.scriptEngineManager.getEngineByName(config.getScriptEngineNameJavaScript());
        }
        else {
            scriptEngine = this.scriptEngineManager.getEngineByName("Graal.js");
            if (scriptEngine == null) {
                scriptEngine = this.scriptEngineManager.getEngineByName(language);
            }
        }
        return scriptEngine;
    }
    
    protected boolean isCachable(final ScriptEngine scriptEngine) {
        final Object threadingParameter = scriptEngine.getFactory().getParameter("THREADING");
        return threadingParameter != null;
    }
    
    protected void configureScriptEngines(final String language, final ScriptEngine scriptEngine) {
        if ("groovy".equals(language)) {
            this.configureGroovyScriptEngine(scriptEngine);
        }
        if ("Graal.js".equals(scriptEngine.getFactory().getEngineName())) {
            this.configureGraalJsScriptEngine(scriptEngine);
        }
    }
    
    protected void configureGroovyScriptEngine(final ScriptEngine scriptEngine) {
        scriptEngine.getContext().setAttribute("#jsr223.groovy.engine.keep.globals", "weak", 100);
    }
    
    protected void configureGraalJsScriptEngine(final ScriptEngine scriptEngine) {
        final ProcessEngineConfigurationImpl config = Context.getProcessEngineConfiguration();
        if (config != null) {
            if (config.isConfigureScriptEngineHostAccess()) {
                scriptEngine.getContext().setAttribute("polyglot.js.allowHostAccess", true, 100);
                scriptEngine.getContext().setAttribute("polyglot.js.allowHostClassLookup", true, 100);
            }
            if (config.isEnableScriptEngineLoadExternalResources()) {
                scriptEngine.getContext().setAttribute("polyglot.js.allowIO", true, 100);
            }
            if (config.isEnableScriptEngineNashornCompatibility()) {
                scriptEngine.getContext().setAttribute("polyglot.js.nashorn-compat", true, 100);
            }
        }
    }
}
