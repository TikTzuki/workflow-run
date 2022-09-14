// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting.engine;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngineFactory;

public interface ScriptEngineResolver
{
    void addScriptEngineFactory(final ScriptEngineFactory p0);
    
    ScriptEngineManager getScriptEngineManager();
    
    ScriptEngine getScriptEngine(final String p0, final boolean p1);
}
