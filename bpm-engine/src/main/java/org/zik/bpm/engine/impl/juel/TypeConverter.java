// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELException;
import java.io.Serializable;

public interface TypeConverter extends Serializable
{
    public static final TypeConverter DEFAULT = new TypeConverterImpl();
    
     <T> T convert(final Object p0, final Class<T> p1) throws ELException;
}
