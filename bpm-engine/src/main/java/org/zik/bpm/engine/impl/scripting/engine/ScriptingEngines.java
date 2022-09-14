// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting.engine;

import javax.script.Bindings;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.application.ProcessApplicationInterface;
import org.zik.bpm.application.ProcessApplicationUnavailableException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.application.AbstractProcessApplication;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.impl.context.Context;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import org.camunda.bpm.dmn.engine.impl.spi.el.DmnScriptEngineResolver;

public class ScriptingEngines implements DmnScriptEngineResolver
{
    public static final String DEFAULT_SCRIPTING_LANGUAGE = "juel";
    public static final String GROOVY_SCRIPTING_LANGUAGE = "groovy";
    public static final String JAVASCRIPT_SCRIPTING_LANGUAGE = "javascript";
    public static final String ECMASCRIPT_SCRIPTING_LANGUAGE = "ecmascript";
    public static final String GRAAL_JS_SCRIPT_ENGINE_NAME = "Graal.js";
    public static final String DEFAULT_JS_SCRIPTING_LANGUAGE = "Graal.js";
    protected ScriptEngineResolver scriptEngineResolver;
    protected ScriptBindingsFactory scriptBindingsFactory;
    protected boolean enableScriptEngineCaching;
    
    public ScriptingEngines(final ScriptBindingsFactory scriptBindingsFactory, final ScriptEngineResolver scriptEngineResolver) {
        this(scriptEngineResolver);
        this.scriptBindingsFactory = scriptBindingsFactory;
    }
    
    public ScriptingEngines(final ScriptEngineResolver scriptEngineResolver) {
        this.enableScriptEngineCaching = true;
        this.scriptEngineResolver = scriptEngineResolver;
    }
    
    public boolean isEnableScriptEngineCaching() {
        return this.enableScriptEngineCaching;
    }
    
    public void setEnableScriptEngineCaching(final boolean enableScriptEngineCaching) {
        this.enableScriptEngineCaching = enableScriptEngineCaching;
    }
    
    public ScriptEngineManager getScriptEngineManager() {
        return this.scriptEngineResolver.getScriptEngineManager();
    }
    
    public ScriptingEngines addScriptEngineFactory(final ScriptEngineFactory scriptEngineFactory) {
        this.scriptEngineResolver.addScriptEngineFactory(scriptEngineFactory);
        return this;
    }
    
    public ScriptEngine getScriptEngineForLanguage(String language) {
        if (language != null) {
            language = language.toLowerCase();
        }
        final ProcessApplicationReference pa = Context.getCurrentProcessApplication();
        final ProcessEngineConfigurationImpl config = Context.getProcessEngineConfiguration();
        ScriptEngine engine = null;
        if (config.isEnableFetchScriptEngineFromProcessApplication() && pa != null) {
            engine = this.getPaScriptEngine(language, pa);
        }
        if (engine == null) {
            engine = this.getGlobalScriptEngine(language);
        }
        return engine;
    }
    
    protected ScriptEngine getPaScriptEngine(final String language, final ProcessApplicationReference pa) {
        try {
            final ProcessApplicationInterface processApplication = pa.getProcessApplication();
            final ProcessApplicationInterface rawObject = processApplication.getRawObject();
            if (rawObject instanceof AbstractProcessApplication) {
                final AbstractProcessApplication abstractProcessApplication = (AbstractProcessApplication)rawObject;
                return abstractProcessApplication.getScriptEngineForName(language, this.enableScriptEngineCaching);
            }
            return null;
        }
        catch (ProcessApplicationUnavailableException e) {
            throw new ProcessEngineException("Process Application is unavailable.", e);
        }
    }
    
    protected ScriptEngine getGlobalScriptEngine(final String language) {
        final ScriptEngine scriptEngine = this.scriptEngineResolver.getScriptEngine(language, this.enableScriptEngineCaching);
        EnsureUtil.ensureNotNull("Can't find scripting engine for '" + language + "'", "scriptEngine", scriptEngine);
        return scriptEngine;
    }
    
    public Bindings createBindings(final ScriptEngine scriptEngine, final VariableScope variableScope) {
        return this.scriptBindingsFactory.createBindings(variableScope, scriptEngine.createBindings());
    }
    
    public ScriptBindingsFactory getScriptBindingsFactory() {
        return this.scriptBindingsFactory;
    }
    
    public void setScriptBindingsFactory(final ScriptBindingsFactory scriptBindingsFactory) {
        this.scriptBindingsFactory = scriptBindingsFactory;
    }
    
    public void setScriptEngineResolver(final ScriptEngineResolver scriptEngineResolver) {
        this.scriptEngineResolver = scriptEngineResolver;
    }
}
