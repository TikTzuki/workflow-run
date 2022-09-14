// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.el;

import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.ReflectUtil;
import java.util.HashMap;
import java.lang.reflect.Method;
import java.util.Map;
import org.zik.bpm.engine.impl.javax.el.FunctionMapper;

public class CommandContextFunctionMapper extends FunctionMapper
{
    public static Map<String, Method> COMMAND_CONTEXT_FUNCTION_MAP;
    
    @Override
    public Method resolveFunction(final String prefix, final String localName) {
        this.ensureContextFunctionMapInitialized();
        return CommandContextFunctionMapper.COMMAND_CONTEXT_FUNCTION_MAP.get(localName);
    }
    
    protected void ensureContextFunctionMapInitialized() {
        if (CommandContextFunctionMapper.COMMAND_CONTEXT_FUNCTION_MAP == null) {
            synchronized (CommandContextFunctionMapper.class) {
                if (CommandContextFunctionMapper.COMMAND_CONTEXT_FUNCTION_MAP == null) {
                    CommandContextFunctionMapper.COMMAND_CONTEXT_FUNCTION_MAP = new HashMap<String, Method>();
                    this.createMethodBindings();
                }
            }
        }
    }
    
    protected void createMethodBindings() {
        final Class<?> mapperClass = this.getClass();
        CommandContextFunctionMapper.COMMAND_CONTEXT_FUNCTION_MAP.put("currentUser", ReflectUtil.getMethod(mapperClass, "currentUser", (Class<?>[])new Class[0]));
        CommandContextFunctionMapper.COMMAND_CONTEXT_FUNCTION_MAP.put("currentUserGroups", ReflectUtil.getMethod(mapperClass, "currentUserGroups", (Class<?>[])new Class[0]));
    }
    
    public static String currentUser() {
        final CommandContext commandContext = Context.getCommandContext();
        if (commandContext != null) {
            return commandContext.getAuthenticatedUserId();
        }
        return null;
    }
    
    public static List<String> currentUserGroups() {
        final CommandContext commandContext = Context.getCommandContext();
        if (commandContext != null) {
            return commandContext.getAuthenticatedGroupIds();
        }
        return null;
    }
    
    static {
        CommandContextFunctionMapper.COMMAND_CONTEXT_FUNCTION_MAP = null;
    }
}
