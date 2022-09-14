// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import org.zik.bpm.engine.impl.util.ReflectUtil;
import java.io.ObjectStreamClass;
import java.io.IOException;
import java.io.Serializable;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.Closeable;
import org.zik.bpm.engine.impl.util.IoUtil;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import org.camunda.bpm.engine.variable.Variables;

public class JavaObjectSerializer extends AbstractObjectValueSerializer
{
    public static final String NAME = "serializable";
    
    public JavaObjectSerializer() {
        super(Variables.SerializationDataFormats.JAVA.getName());
    }
    
    @Override
    public String getName() {
        return "serializable";
    }
    
    @Override
    protected boolean isSerializationTextBased() {
        return false;
    }
    
    @Override
    protected Object deserializeFromByteArray(final byte[] bytes, final String objectTypeName) throws Exception {
        final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = null;
        try {
            ois = new ClassloaderAwareObjectInputStream(bais);
            return ois.readObject();
        }
        finally {
            IoUtil.closeSilently(ois);
            IoUtil.closeSilently(bais);
        }
    }
    
    @Override
    protected byte[] serializeToByteArray(final Object deserializedObject) throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream ois = null;
        try {
            ois = new ObjectOutputStream(baos);
            ois.writeObject(deserializedObject);
            return baos.toByteArray();
        }
        finally {
            IoUtil.closeSilently(ois);
            IoUtil.closeSilently(baos);
        }
    }
    
    @Override
    protected String getTypeNameForDeserialized(final Object deserializedObject) {
        return deserializedObject.getClass().getName();
    }
    
    @Override
    protected boolean canSerializeValue(final Object value) {
        return value instanceof Serializable;
    }
    
    protected static class ClassloaderAwareObjectInputStream extends ObjectInputStream
    {
        public ClassloaderAwareObjectInputStream(final InputStream in) throws IOException {
            super(in);
        }
        
        @Override
        protected Class<?> resolveClass(final ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            return ReflectUtil.loadClass(desc.getName());
        }
    }
}
