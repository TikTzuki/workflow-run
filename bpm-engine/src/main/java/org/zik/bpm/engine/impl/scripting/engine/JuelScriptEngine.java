// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting.engine;

import javax.script.ScriptEngine;
import javax.script.CompiledScript;
import java.lang.reflect.Modifier;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.ReflectUtil;
import java.lang.reflect.Method;
import org.zik.bpm.engine.impl.javax.el.FunctionMapper;
import org.zik.bpm.engine.impl.javax.el.VariableMapper;
import org.zik.bpm.engine.impl.javax.el.ELContext;
import java.io.IOException;
import org.zik.bpm.engine.impl.juel.SimpleResolver;
import org.zik.bpm.engine.impl.javax.el.BeanELResolver;
import org.zik.bpm.engine.impl.javax.el.ResourceBundleELResolver;
import org.zik.bpm.engine.impl.javax.el.MapELResolver;
import org.zik.bpm.engine.impl.javax.el.ListELResolver;
import org.zik.bpm.engine.impl.javax.el.ArrayELResolver;
import org.zik.bpm.engine.impl.javax.el.CompositeELResolver;
import org.zik.bpm.engine.impl.javax.el.ELResolver;
import org.zik.bpm.engine.impl.javax.el.ELException;
import javax.script.SimpleBindings;
import javax.script.Bindings;
import java.io.Reader;
import javax.script.ScriptException;
import org.zik.bpm.engine.impl.javax.el.ValueExpression;
import javax.script.ScriptContext;
import org.zik.bpm.engine.impl.el.ExpressionFactoryResolver;
import org.zik.bpm.engine.impl.javax.el.ExpressionFactory;
import javax.script.ScriptEngineFactory;
import javax.script.AbstractScriptEngine;

public class JuelScriptEngine extends AbstractScriptEngine
{
    private ScriptEngineFactory scriptEngineFactory;
    private ExpressionFactory expressionFactory;
    
    public JuelScriptEngine(final ScriptEngineFactory scriptEngineFactory) {
        this.scriptEngineFactory = scriptEngineFactory;
        this.expressionFactory = ExpressionFactoryResolver.resolveExpressionFactory();
    }
    
    public JuelScriptEngine() {
        this((ScriptEngineFactory)null);
    }
    
    @Override
    public Object eval(final String script, final ScriptContext scriptContext) throws ScriptException {
        final ValueExpression expr = this.parse(script, scriptContext);
        return this.evaluateExpression(expr, scriptContext);
    }
    
    @Override
    public Object eval(final Reader reader, final ScriptContext scriptContext) throws ScriptException {
        return this.eval(this.readFully(reader), scriptContext);
    }
    
    @Override
    public ScriptEngineFactory getFactory() {
        synchronized (this) {
            if (this.scriptEngineFactory == null) {
                this.scriptEngineFactory = new JuelScriptEngineFactory();
            }
        }
        return this.scriptEngineFactory;
    }
    
    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }
    
    private Object evaluateExpression(final ValueExpression expr, final ScriptContext ctx) throws ScriptException {
        try {
            return expr.getValue(this.createElContext(ctx));
        }
        catch (ELException elexp) {
            throw new ScriptException(elexp);
        }
    }
    
    private ELResolver createElResolver() {
        final CompositeELResolver compositeResolver = new CompositeELResolver();
        compositeResolver.add(new ArrayELResolver());
        compositeResolver.add(new ListELResolver());
        compositeResolver.add(new MapELResolver());
        compositeResolver.add(new ResourceBundleELResolver());
        compositeResolver.add(new BeanELResolver());
        return new SimpleResolver(compositeResolver);
    }
    
    private String readFully(final Reader reader) throws ScriptException {
        final char[] array = new char[8192];
        final StringBuilder strBuffer = new StringBuilder();
        try {
            int count;
            while ((count = reader.read(array, 0, array.length)) > 0) {
                strBuffer.append(array, 0, count);
            }
        }
        catch (IOException exp) {
            throw new ScriptException(exp);
        }
        return strBuffer.toString();
    }
    
    private ValueExpression parse(final String script, final ScriptContext scriptContext) throws ScriptException {
        try {
            return this.expressionFactory.createValueExpression(this.createElContext(scriptContext), script, Object.class);
        }
        catch (ELException ele) {
            throw new ScriptException(ele);
        }
    }
    
    private ELContext createElContext(final ScriptContext scriptCtx) {
        final Object existingELCtx = scriptCtx.getAttribute("elcontext");
        if (existingELCtx instanceof ELContext) {
            return (ELContext)existingELCtx;
        }
        scriptCtx.setAttribute("context", scriptCtx, 100);
        scriptCtx.setAttribute("out:print", getPrintMethod(), 100);
        final SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            scriptCtx.setAttribute("lang:import", getImportMethod(), 100);
        }
        final ELContext elContext = new ELContext() {
            ELResolver resolver = JuelScriptEngine.this.createElResolver();
            VariableMapper varMapper = new ScriptContextVariableMapper(scriptCtx);
            FunctionMapper funcMapper = new ScriptContextFunctionMapper(scriptCtx);
            
            @Override
            public ELResolver getELResolver() {
                return this.resolver;
            }
            
            @Override
            public VariableMapper getVariableMapper() {
                return this.varMapper;
            }
            
            @Override
            public FunctionMapper getFunctionMapper() {
                return this.funcMapper;
            }
        };
        scriptCtx.setAttribute("elcontext", elContext, 100);
        return elContext;
    }
    
    private static Method getPrintMethod() {
        try {
            return JuelScriptEngine.class.getMethod("print", Object.class);
        }
        catch (Exception exp) {
            return null;
        }
    }
    
    private static Method getImportMethod() {
        try {
            return JuelScriptEngine.class.getMethod("importFunctions", ScriptContext.class, String.class, Object.class);
        }
        catch (Exception exp) {
            return null;
        }
    }
    
    public static void importFunctions(final ScriptContext ctx, final String namespace, final Object obj) {
        Class<?> clazz = null;
        Label_0057: {
            if (!(obj instanceof Class)) {
                if (obj instanceof String) {
                    try {
                        clazz = ReflectUtil.loadClass((String)obj);
                        break Label_0057;
                    }
                    catch (ProcessEngineException ae) {
                        throw new ELException(ae);
                    }
                }
                throw new ELException("Class or class name is missing");
            }
            clazz = (Class<?>)obj;
        }
        final Method[] methods2;
        final Method[] methods = methods2 = clazz.getMethods();
        for (final Method m : methods2) {
            final int mod = m.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isPublic(mod)) {
                final String name = namespace + ":" + m.getName();
                ctx.setAttribute(name, m, 100);
            }
        }
    }
    
    private class JuelCompiledScript extends CompiledScript
    {
        private ValueExpression valueExpression;
        
        JuelCompiledScript(final ValueExpression valueExpression) {
            this.valueExpression = valueExpression;
        }
        
        @Override
        public ScriptEngine getEngine() {
            return JuelScriptEngine.this;
        }
        
        @Override
        public Object eval(final ScriptContext ctx) throws ScriptException {
            return JuelScriptEngine.this.evaluateExpression(this.valueExpression, ctx);
        }
    }
    
    private class ScriptContextVariableMapper extends VariableMapper
    {
        private ScriptContext scriptContext;
        
        ScriptContextVariableMapper(final ScriptContext scriptCtx) {
            this.scriptContext = scriptCtx;
        }
        
        @Override
        public ValueExpression resolveVariable(final String variableName) {
            final int scope = this.scriptContext.getAttributesScope(variableName);
            if (scope == -1) {
                return null;
            }
            final Object value = this.scriptContext.getAttribute(variableName, scope);
            if (value instanceof ValueExpression) {
                return (ValueExpression)value;
            }
            return JuelScriptEngine.this.expressionFactory.createValueExpression(value, Object.class);
        }
        
        @Override
        public ValueExpression setVariable(final String name, final ValueExpression value) {
            final ValueExpression previousValue = this.resolveVariable(name);
            this.scriptContext.setAttribute(name, value, 100);
            return previousValue;
        }
    }
    
    private class ScriptContextFunctionMapper extends FunctionMapper
    {
        private ScriptContext scriptContext;
        
        ScriptContextFunctionMapper(final ScriptContext ctx) {
            this.scriptContext = ctx;
        }
        
        private String getFullFunctionName(final String prefix, final String localName) {
            return prefix + ":" + localName;
        }
        
        @Override
        public Method resolveFunction(final String prefix, final String localName) {
            final String functionName = this.getFullFunctionName(prefix, localName);
            final int scope = this.scriptContext.getAttributesScope(functionName);
            if (scope != -1) {
                final Object attributeValue = this.scriptContext.getAttribute(functionName);
                return (attributeValue instanceof Method) ? ((Method)attributeValue) : null;
            }
            return null;
        }
    }
}
