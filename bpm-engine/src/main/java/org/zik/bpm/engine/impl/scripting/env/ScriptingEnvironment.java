// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting.env;

import java.util.ArrayList;
import org.zik.bpm.application.ProcessApplicationInterface;
import org.zik.bpm.application.ProcessApplicationUnavailableException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.application.AbstractProcessApplication;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Iterator;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import org.zik.bpm.engine.delegate.VariableScope;
import java.util.HashMap;
import org.zik.bpm.engine.impl.scripting.engine.ScriptingEngines;
import org.zik.bpm.engine.impl.scripting.ScriptFactory;
import org.zik.bpm.engine.impl.scripting.ExecutableScript;
import java.util.List;
import java.util.Map;

public class ScriptingEnvironment
{
    protected Map<String, List<ExecutableScript>> env;
    protected List<ScriptEnvResolver> envResolvers;
    protected ScriptFactory scriptFactory;
    protected ScriptingEngines scriptingEngines;
    
    public ScriptingEnvironment(final ScriptFactory scriptFactory, final List<ScriptEnvResolver> scriptEnvResolvers, final ScriptingEngines scriptingEngines) {
        this.env = new HashMap<String, List<ExecutableScript>>();
        this.scriptFactory = scriptFactory;
        this.envResolvers = scriptEnvResolvers;
        this.scriptingEngines = scriptingEngines;
    }
    
    public Object execute(final ExecutableScript script, final VariableScope scope) {
        final ScriptEngine scriptEngine = this.scriptingEngines.getScriptEngineForLanguage(script.getLanguage());
        final Bindings bindings = this.scriptingEngines.createBindings(scriptEngine, scope);
        return this.execute(script, scope, bindings, scriptEngine);
    }
    
    public Object execute(final ExecutableScript script, final VariableScope scope, final Bindings bindings, final ScriptEngine scriptEngine) {
        final List<ExecutableScript> envScripts = this.getEnvScripts(script, scriptEngine);
        for (final ExecutableScript envScript : envScripts) {
            envScript.execute(scriptEngine, scope, bindings);
        }
        return script.execute(scriptEngine, scope, bindings);
    }
    
    protected Map<String, List<ExecutableScript>> getEnv(final String language) {
        final ProcessEngineConfigurationImpl config = Context.getProcessEngineConfiguration();
        final ProcessApplicationReference processApplication = Context.getCurrentProcessApplication();
        Map<String, List<ExecutableScript>> result = null;
        if (config.isEnableFetchScriptEngineFromProcessApplication() && processApplication != null) {
            result = this.getPaEnvScripts(processApplication);
        }
        return (result != null) ? result : this.env;
    }
    
    protected Map<String, List<ExecutableScript>> getPaEnvScripts(final ProcessApplicationReference pa) {
        try {
            final ProcessApplicationInterface processApplication = pa.getProcessApplication();
            final ProcessApplicationInterface rawObject = processApplication.getRawObject();
            if (rawObject instanceof AbstractProcessApplication) {
                final AbstractProcessApplication abstractProcessApplication = (AbstractProcessApplication)rawObject;
                return abstractProcessApplication.getEnvironmentScripts();
            }
            return null;
        }
        catch (ProcessApplicationUnavailableException e) {
            throw new ProcessEngineException("Process Application is unavailable.", e);
        }
    }
    
    protected List<ExecutableScript> getEnvScripts(final ExecutableScript script, final ScriptEngine scriptEngine) {
        List<ExecutableScript> envScripts = this.getEnvScripts(script.getLanguage().toLowerCase());
        if (envScripts.isEmpty()) {
            envScripts = this.getEnvScripts(scriptEngine.getFactory().getLanguageName().toLowerCase());
        }
        return envScripts;
    }
    
    protected List<ExecutableScript> getEnvScripts(final String scriptLanguage) {
        final Map<String, List<ExecutableScript>> environment = this.getEnv(scriptLanguage);
        List<ExecutableScript> envScripts = environment.get(scriptLanguage);
        if (envScripts == null) {
            synchronized (this) {
                envScripts = environment.get(scriptLanguage);
                if (envScripts == null) {
                    envScripts = this.initEnvForLanguage(scriptLanguage);
                    environment.put(scriptLanguage, envScripts);
                }
            }
        }
        return envScripts;
    }
    
    protected List<ExecutableScript> initEnvForLanguage(final String language) {
        final List<ExecutableScript> scripts = new ArrayList<ExecutableScript>();
        for (final ScriptEnvResolver resolver : this.envResolvers) {
            final String[] resolvedScripts = resolver.resolve(language);
            if (resolvedScripts != null) {
                for (final String resolvedScript : resolvedScripts) {
                    scripts.add(this.scriptFactory.createScriptFromSource(language, resolvedScript));
                }
            }
        }
        return scripts;
    }
}
