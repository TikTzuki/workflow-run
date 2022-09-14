// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.zik.bpm.engine.runtime.ProcessElementInstance;
import java.util.Iterator;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.Collection;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import org.zik.bpm.engine.impl.ProcessEngineImpl;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.el.ExpressionManager;

public final class StringUtil
{
    public static int DB_MAX_STRING_LENGTH;
    
    public static boolean isExpression(String text) {
        text = text.trim();
        return text.startsWith("${") || text.startsWith("#{");
    }
    
    public static boolean isCompositeExpression(final String text, final ExpressionManager expressionManager) {
        return !expressionManager.createExpression(text).isLiteralText();
    }
    
    public static String[] split(final String text, final String regex) {
        if (text == null) {
            return null;
        }
        if (regex == null) {
            return new String[] { text };
        }
        final String[] result = text.split(regex);
        for (int i = 0; i < result.length; ++i) {
            result[i] = result[i].trim();
        }
        return result;
    }
    
    public static boolean hasAnySuffix(final String text, final String[] suffixes) {
        for (final String suffix : suffixes) {
            if (text.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
    
    public static String fromBytes(final byte[] bytes) {
        EnsureUtil.ensureActiveCommandContext("StringUtil.fromBytes");
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        return fromBytes(bytes, processEngineConfiguration.getProcessEngine());
    }
    
    public static String fromBytes(final byte[] bytes, final ProcessEngine processEngine) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = ((ProcessEngineImpl)processEngine).getProcessEngineConfiguration();
        final Charset charset = processEngineConfiguration.getDefaultCharset();
        return (bytes != null) ? new String(bytes, charset) : new String();
    }
    
    public static Reader readerFromBytes(final byte[] bytes) {
        EnsureUtil.ensureActiveCommandContext("StringUtil.readerFromBytes");
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        return new InputStreamReader(inputStream, processEngineConfiguration.getDefaultCharset());
    }
    
    public static Writer writerForStream(final OutputStream outStream) {
        EnsureUtil.ensureActiveCommandContext("StringUtil.readerFromBytes");
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        return new OutputStreamWriter(outStream, processEngineConfiguration.getDefaultCharset());
    }
    
    public static byte[] toByteArray(final String string) {
        EnsureUtil.ensureActiveCommandContext("StringUtil.toByteArray");
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        return toByteArray(string, processEngineConfiguration.getProcessEngine());
    }
    
    public static byte[] toByteArray(final String string, final ProcessEngine processEngine) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = ((ProcessEngineImpl)processEngine).getProcessEngineConfiguration();
        final Charset charset = processEngineConfiguration.getDefaultCharset();
        return string.getBytes(charset);
    }
    
    public static String trimToMaximumLengthAllowed(final String string) {
        if (string != null && string.length() > StringUtil.DB_MAX_STRING_LENGTH) {
            return string.substring(0, StringUtil.DB_MAX_STRING_LENGTH);
        }
        return string;
    }
    
    public static String joinDbEntityIds(final Collection<? extends DbEntity> dbEntities) {
        return join(new StringIterator<DbEntity>(dbEntities.iterator()) {
            @Override
            public String next() {
                return ((DbEntity)this.iterator.next()).getId();
            }
        });
    }
    
    public static String joinProcessElementInstanceIds(final Collection<? extends ProcessElementInstance> processElementInstances) {
        final Iterator<? extends ProcessElementInstance> iterator = processElementInstances.iterator();
        return join(new StringIterator<ProcessElementInstance>(iterator) {
            @Override
            public String next() {
                return ((ProcessElementInstance)this.iterator.next()).getId();
            }
        });
    }
    
    public static boolean hasText(final String string) {
        return string != null && !string.isEmpty();
    }
    
    public static String join(final Iterator<String> iterator) {
        final StringBuilder builder = new StringBuilder();
        while (iterator.hasNext()) {
            builder.append(iterator.next());
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }
    
    static {
        StringUtil.DB_MAX_STRING_LENGTH = 666;
    }
    
    public abstract static class StringIterator<T> implements Iterator<String>
    {
        protected Iterator<? extends T> iterator;
        
        public StringIterator(final Iterator<? extends T> iterator) {
            this.iterator = iterator;
        }
        
        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
        
        @Override
        public void remove() {
            this.iterator.remove();
        }
    }
}
