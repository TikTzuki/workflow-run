// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.scripting.engine.JuelScriptEngineFactory;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.scripting.ScriptFactory;
import org.zik.bpm.engine.impl.scripting.ExecutableScript;
import org.zik.bpm.engine.impl.el.ExpressionManager;

public final class ScriptUtil
{
    public static ExecutableScript getScript(final String language, final String source, final String resource, final ExpressionManager expressionManager) {
        return getScript(language, source, resource, expressionManager, getScriptFactory());
    }
    
    public static ExecutableScript getScript(final String language, final String source, final String resource, final ExpressionManager expressionManager, final ScriptFactory scriptFactory) {
        EnsureUtil.ensureNotEmpty(NotValidException.class, "Script language", language);
        EnsureUtil.ensureAtLeastOneNotNull(NotValidException.class, "No script source or resource was given", source, resource);
        if (resource != null && !resource.isEmpty()) {
            return getScriptFromResource(language, resource, expressionManager, scriptFactory);
        }
        return getScriptFormSource(language, source, expressionManager, scriptFactory);
    }
    
    public static ExecutableScript getScriptFormSource(final String language, final String source, final ExpressionManager expressionManager, final ScriptFactory scriptFactory) {
        EnsureUtil.ensureNotEmpty(NotValidException.class, "Script language", language);
        EnsureUtil.ensureNotNull(NotValidException.class, "Script source", (Object)source);
        if (isDynamicScriptExpression(language, source)) {
            final Expression sourceExpression = expressionManager.createExpression(source);
            return getScriptFromSourceExpression(language, sourceExpression, scriptFactory);
        }
        return getScriptFromSource(language, source, scriptFactory);
    }
    
    public static ExecutableScript getScriptFromSource(final String language, final String source, final ScriptFactory scriptFactory) {
        EnsureUtil.ensureNotEmpty(NotValidException.class, "Script language", language);
        EnsureUtil.ensureNotNull(NotValidException.class, "Script source", (Object)source);
        return scriptFactory.createScriptFromSource(language, source);
    }
    
    public static ExecutableScript getScriptFromSourceExpression(final String language, final Expression sourceExpression, final ScriptFactory scriptFactory) {
        EnsureUtil.ensureNotEmpty(NotValidException.class, "Script language", language);
        EnsureUtil.ensureNotNull(NotValidException.class, "Script source expression", sourceExpression);
        return scriptFactory.createScriptFromSource(language, sourceExpression);
    }
    
    public static ExecutableScript getScriptFromResource(final String language, final String resource, final ExpressionManager expressionManager, final ScriptFactory scriptFactory) {
        EnsureUtil.ensureNotEmpty(NotValidException.class, "Script language", language);
        EnsureUtil.ensureNotEmpty(NotValidException.class, "Script resource", resource);
        if (isDynamicScriptExpression(language, resource)) {
            final Expression resourceExpression = expressionManager.createExpression(resource);
            return getScriptFromResourceExpression(language, resourceExpression, scriptFactory);
        }
        return getScriptFromResource(language, resource, scriptFactory);
    }
    
    public static ExecutableScript getScriptFromResource(final String language, final String resource, final ScriptFactory scriptFactory) {
        EnsureUtil.ensureNotEmpty(NotValidException.class, "Script language", language);
        EnsureUtil.ensureNotEmpty(NotValidException.class, "Script resource", resource);
        return scriptFactory.createScriptFromResource(language, resource);
    }
    
    public static ExecutableScript getScriptFromResourceExpression(final String language, final Expression resourceExpression, final ScriptFactory scriptFactory) {
        EnsureUtil.ensureNotEmpty(NotValidException.class, "Script language", language);
        EnsureUtil.ensureNotNull(NotValidException.class, "Script resource expression", resourceExpression);
        return scriptFactory.createScriptFromResource(language, resourceExpression);
    }
    
    public static boolean isDynamicScriptExpression(final String language, final String value) {
        return StringUtil.isExpression(value) && language != null && !JuelScriptEngineFactory.names.contains(language.toLowerCase());
    }
    
    public static ScriptFactory getScriptFactory() {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        if (processEngineConfiguration != null) {
            return processEngineConfiguration.getScriptFactory();
        }
        return new ScriptFactory();
    }
}
