// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.Date;
import java.util.logging.LogRecord;
import java.util.logging.Formatter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.io.InputStream;
import java.io.Closeable;
import org.zik.bpm.engine.impl.pvm.PvmException;
import java.util.logging.LogManager;
import java.text.Format;
import java.util.Map;

@Deprecated
public class LogUtil
{
    private static final String LINE_SEPARATOR;
    private static Map<Integer, String> threadIndents;
    private static ThreadLogMode threadLogMode;
    private static Format dateFormat;
    
    public static ThreadLogMode getThreadLogMode() {
        return LogUtil.threadLogMode;
    }
    
    public static ThreadLogMode setThreadLogMode(final ThreadLogMode threadLogMode) {
        final ThreadLogMode old = LogUtil.threadLogMode;
        LogUtil.threadLogMode = threadLogMode;
        return old;
    }
    
    public static void readJavaUtilLoggingConfigFromClasspath() {
        final InputStream inputStream = ReflectUtil.getResourceAsStream("logging.properties");
        try {
            if (inputStream != null) {
                LogManager.getLogManager().readConfiguration(inputStream);
                final String redirectCommons = LogManager.getLogManager().getProperty("redirect.commons.logging");
                if (redirectCommons != null && !redirectCommons.equalsIgnoreCase("false")) {
                    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
                }
            }
        }
        catch (Exception e) {
            throw new PvmException("couldn't initialize logging properly", e);
        }
        finally {
            IoUtil.closeSilently(inputStream);
        }
    }
    
    public static void resetThreadIndents() {
        LogUtil.threadIndents = new HashMap<Integer, String>();
    }
    
    static {
        LINE_SEPARATOR = System.getProperty("line.separator");
        LogUtil.threadIndents = new HashMap<Integer, String>();
        LogUtil.threadLogMode = ThreadLogMode.NONE;
        LogUtil.dateFormat = new SimpleDateFormat("HH:mm:ss,SSS");
    }
    
    public enum ThreadLogMode
    {
        NONE, 
        INDENT, 
        PRINT_ID;
    }
    
    public static class LogFormatter extends Formatter
    {
        @Override
        public String format(final LogRecord record) {
            final StringBuilder line = new StringBuilder();
            line.append(LogUtil.dateFormat.format(new Date()));
            if (Level.FINE.equals(record.getLevel())) {
                line.append(" FIN ");
            }
            else if (Level.FINEST.equals(record.getLevel())) {
                line.append(" FST ");
            }
            else if (Level.INFO.equals(record.getLevel())) {
                line.append(" INF ");
            }
            else if (Level.SEVERE.equals(record.getLevel())) {
                line.append(" SEV ");
            }
            else if (Level.WARNING.equals(record.getLevel())) {
                line.append(" WRN ");
            }
            else if (Level.FINER.equals(record.getLevel())) {
                line.append(" FNR ");
            }
            else if (Level.CONFIG.equals(record.getLevel())) {
                line.append(" CFG ");
            }
            final int threadId = record.getThreadID();
            final String threadIndent = getThreadIndent(threadId);
            line.append(threadIndent);
            line.append(" | ");
            line.append(record.getMessage());
            if (record.getThrown() != null) {
                line.append(LogUtil.LINE_SEPARATOR);
                final StringWriter stringWriter = new StringWriter();
                final PrintWriter printWriter = new PrintWriter(stringWriter);
                record.getThrown().printStackTrace(printWriter);
                line.append(stringWriter.toString());
            }
            line.append("  [");
            line.append(record.getLoggerName());
            line.append("]");
            line.append(LogUtil.LINE_SEPARATOR);
            return line.toString();
        }
        
        protected static String getThreadIndent(final int threadId) {
            final Integer threadIdInteger = threadId;
            if (LogUtil.threadLogMode == ThreadLogMode.NONE) {
                return "";
            }
            if (LogUtil.threadLogMode == ThreadLogMode.PRINT_ID) {
                return "" + threadId;
            }
            String threadIndent = LogUtil.threadIndents.get(threadIdInteger);
            if (threadIndent == null) {
                final StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < LogUtil.threadIndents.size(); ++i) {
                    stringBuilder.append("  ");
                }
                threadIndent = stringBuilder.toString();
                LogUtil.threadIndents.put(threadIdInteger, threadIndent);
            }
            return threadIndent;
        }
    }
}
