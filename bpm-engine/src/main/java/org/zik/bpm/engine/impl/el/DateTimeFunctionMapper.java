// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.el;

import org.joda.time.DateTime;
import org.zik.bpm.engine.impl.util.ClockUtil;
import java.util.Date;
import org.zik.bpm.engine.impl.util.ReflectUtil;
import java.util.HashMap;
import java.lang.reflect.Method;
import java.util.Map;
import org.zik.bpm.engine.impl.javax.el.FunctionMapper;

public class DateTimeFunctionMapper extends FunctionMapper
{
    public static Map<String, Method> DATE_TIME_FUNCTION_MAP;
    
    @Override
    public Method resolveFunction(final String prefix, final String localName) {
        this.ensureContextFunctionMapInitialized();
        return DateTimeFunctionMapper.DATE_TIME_FUNCTION_MAP.get(localName);
    }
    
    protected void ensureContextFunctionMapInitialized() {
        if (DateTimeFunctionMapper.DATE_TIME_FUNCTION_MAP == null) {
            synchronized (CommandContextFunctionMapper.class) {
                if (DateTimeFunctionMapper.DATE_TIME_FUNCTION_MAP == null) {
                    DateTimeFunctionMapper.DATE_TIME_FUNCTION_MAP = new HashMap<String, Method>();
                    this.createMethodBindings();
                }
            }
        }
    }
    
    protected void createMethodBindings() {
        final Class<?> mapperClass = this.getClass();
        DateTimeFunctionMapper.DATE_TIME_FUNCTION_MAP.put("now", ReflectUtil.getMethod(mapperClass, "now", (Class<?>[])new Class[0]));
        DateTimeFunctionMapper.DATE_TIME_FUNCTION_MAP.put("dateTime", ReflectUtil.getMethod(mapperClass, "dateTime", (Class<?>[])new Class[0]));
    }
    
    public static Date now() {
        return ClockUtil.getCurrentTime();
    }
    
    public static DateTime dateTime() {
        return new DateTime((Object)now());
    }
    
    static {
        DateTimeFunctionMapper.DATE_TIME_FUNCTION_MAP = null;
    }
}
