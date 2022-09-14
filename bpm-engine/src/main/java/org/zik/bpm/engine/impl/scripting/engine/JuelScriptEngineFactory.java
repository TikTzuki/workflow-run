// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import javax.script.ScriptEngine;
import java.util.List;
import javax.script.ScriptEngineFactory;

public class JuelScriptEngineFactory implements ScriptEngineFactory
{
    public static List<String> names;
    private static List<String> extensions;
    private static List<String> mimeTypes;
    
    @Override
    public String getEngineName() {
        return "juel";
    }
    
    @Override
    public String getEngineVersion() {
        return "1.0";
    }
    
    @Override
    public List<String> getExtensions() {
        return JuelScriptEngineFactory.extensions;
    }
    
    @Override
    public String getLanguageName() {
        return "JSP 2.1 EL";
    }
    
    @Override
    public String getLanguageVersion() {
        return "2.1";
    }
    
    @Override
    public String getMethodCallSyntax(final String obj, final String method, final String... arguments) {
        throw new UnsupportedOperationException("Method getMethodCallSyntax is not supported");
    }
    
    @Override
    public List<String> getMimeTypes() {
        return JuelScriptEngineFactory.mimeTypes;
    }
    
    @Override
    public List<String> getNames() {
        return JuelScriptEngineFactory.names;
    }
    
    @Override
    public String getOutputStatement(final String toDisplay) {
        final StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("out:print(\"");
        for (int length = toDisplay.length(), i = 0; i < length; ++i) {
            final char c = toDisplay.charAt(i);
            switch (c) {
                case '\"': {
                    stringBuffer.append("\\\"");
                    break;
                }
                case '\\': {
                    stringBuffer.append("\\\\");
                    break;
                }
                default: {
                    stringBuffer.append(c);
                    break;
                }
            }
        }
        stringBuffer.append("\")");
        return stringBuffer.toString();
    }
    
    @Override
    public String getParameter(final String key) {
        if (key.equals("javax.script.name")) {
            return this.getLanguageName();
        }
        if (key.equals("javax.script.engine")) {
            return this.getEngineName();
        }
        if (key.equals("javax.script.engine_version")) {
            return this.getEngineVersion();
        }
        if (key.equals("javax.script.language")) {
            return this.getLanguageName();
        }
        if (key.equals("javax.script.language_version")) {
            return this.getLanguageVersion();
        }
        if (key.equals("THREADING")) {
            return "MULTITHREADED";
        }
        return null;
    }
    
    @Override
    public String getProgram(final String... statements) {
        final StringBuilder buf = new StringBuilder();
        if (statements.length != 0) {
            for (int i = 0; i < statements.length; ++i) {
                buf.append("${");
                buf.append(statements[i]);
                buf.append("} ");
            }
        }
        return buf.toString();
    }
    
    @Override
    public ScriptEngine getScriptEngine() {
        return new JuelScriptEngine(this);
    }
    
    static {
        JuelScriptEngineFactory.names = Collections.unmodifiableList((List<? extends String>)Arrays.asList("juel"));
        JuelScriptEngineFactory.extensions = JuelScriptEngineFactory.names;
        JuelScriptEngineFactory.mimeTypes = Collections.unmodifiableList((List<? extends String>)new ArrayList<String>(0));
    }
}
