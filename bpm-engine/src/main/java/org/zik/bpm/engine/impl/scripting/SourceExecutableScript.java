// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.ScriptCompilationException;
import javax.script.Compilable;
import javax.script.CompiledScript;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import javax.script.ScriptException;
import org.zik.bpm.engine.ScriptEvaluationException;
import org.zik.bpm.engine.delegate.BpmnError;
import javax.script.Bindings;
import org.zik.bpm.engine.delegate.VariableScope;
import javax.script.ScriptEngine;

public class SourceExecutableScript extends CompiledExecutableScript
{
    private static final ScriptLogger LOG;
    protected String scriptSource;
    protected boolean shouldBeCompiled;
    
    public SourceExecutableScript(final String language, final String source) {
        super(language);
        this.shouldBeCompiled = true;
        this.scriptSource = source;
    }
    
    @Override
    public Object evaluate(final ScriptEngine engine, final VariableScope variableScope, final Bindings bindings) {
        if (this.shouldBeCompiled) {
            this.compileScript(engine);
        }
        if (this.getCompiledScript() != null) {
            return super.evaluate(engine, variableScope, bindings);
        }
        try {
            return this.evaluateScript(engine, bindings);
        }
        catch (ScriptException e) {
            if (e.getCause() instanceof BpmnError) {
                throw (BpmnError)e.getCause();
            }
            final String activityIdMessage = this.getActivityIdExceptionMessage(variableScope);
            throw new ScriptEvaluationException("Unable to evaluate script" + activityIdMessage + ":" + e.getMessage(), e);
        }
    }
    
    protected void compileScript(final ScriptEngine engine) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        if (processEngineConfiguration.isEnableScriptEngineCaching() && processEngineConfiguration.isEnableScriptCompilation()) {
            if (this.getCompiledScript() == null && this.shouldBeCompiled) {
                synchronized (this) {
                    if (this.getCompiledScript() == null && this.shouldBeCompiled) {
                        this.compiledScript = this.compile(engine, this.language, this.scriptSource);
                        this.shouldBeCompiled = false;
                    }
                }
            }
        }
        else {
            this.shouldBeCompiled = false;
        }
    }
    
    public CompiledScript compile(final ScriptEngine scriptEngine, final String language, final String src) {
        if (scriptEngine instanceof Compilable && !scriptEngine.getFactory().getLanguageName().equalsIgnoreCase("ecmascript")) {
            final Compilable compilingEngine = (Compilable)scriptEngine;
            try {
                final CompiledScript compiledScript = compilingEngine.compile(src);
                SourceExecutableScript.LOG.debugCompiledScriptUsing(language);
                return compiledScript;
            }
            catch (ScriptException e) {
                throw new ScriptCompilationException("Unable to compile script: " + e.getMessage(), e);
            }
        }
        return null;
    }
    
    protected Object evaluateScript(final ScriptEngine engine, final Bindings bindings) throws ScriptException {
        SourceExecutableScript.LOG.debugEvaluatingNonCompiledScript(this.scriptSource);
        return engine.eval(this.scriptSource, bindings);
    }
    
    public String getScriptSource() {
        return this.scriptSource;
    }
    
    public void setScriptSource(final String scriptSource) {
        this.compiledScript = null;
        this.shouldBeCompiled = true;
        this.scriptSource = scriptSource;
    }
    
    public boolean isShouldBeCompiled() {
        return this.shouldBeCompiled;
    }
    
    static {
        LOG = ProcessEngineLogger.SCRIPT_LOGGER;
    }
}
