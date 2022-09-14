// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.io.File;
import java.nio.charset.Charset;
import java.io.Closeable;
import java.io.BufferedInputStream;
import org.zik.bpm.engine.ProcessEngineException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class IoUtil
{
    private static final EngineUtilLogger LOG;
    
    public static byte[] readInputStream(final InputStream inputStream, final String inputStreamName) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] buffer = new byte[16384];
        try {
            for (int bytesRead = inputStream.read(buffer); bytesRead != -1; bytesRead = inputStream.read(buffer)) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        catch (Exception e) {
            throw IoUtil.LOG.exceptionWhileReadingStream(inputStreamName, e);
        }
        return outputStream.toByteArray();
    }
    
    public static String readClasspathResourceAsString(final String resourceName) {
        final InputStream resourceAsStream = IoUtil.class.getClassLoader().getResourceAsStream(resourceName);
        if (resourceAsStream == null) {
            throw new ProcessEngineException("resource " + resourceName + " not found");
        }
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        BufferedInputStream inputStream = null;
        byte[] result;
        try {
            inputStream = new BufferedInputStream(resourceAsStream);
            int next;
            while ((next = inputStream.read(buffer)) >= 0) {
                outStream.write(buffer, 0, next);
            }
            result = outStream.toByteArray();
        }
        catch (Exception e) {
            throw IoUtil.LOG.exceptionWhileReadingFile(resourceName, e);
        }
        finally {
            closeSilently(inputStream);
            closeSilently(outStream);
        }
        return new String(result, Charset.forName("UTF-8"));
    }
    
    public static File getFile(final String filePath) {
        final URL url = IoUtil.class.getClassLoader().getResource(filePath);
        try {
            return new File(url.toURI());
        }
        catch (Exception e) {
            throw IoUtil.LOG.exceptionWhileGettingFile(filePath, e);
        }
    }
    
    public static void writeStringToFile(final String content, final String filePath) {
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(getFile(filePath)));
            outputStream.write(content.getBytes());
            outputStream.flush();
        }
        catch (Exception e) {
            throw IoUtil.LOG.exceptionWhileWritingToFile(filePath, e);
        }
        finally {
            closeSilently(outputStream);
        }
    }
    
    public static void closeSilently(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        }
        catch (IOException ignore) {
            IoUtil.LOG.debugCloseException(ignore);
        }
    }
    
    public static void flushSilently(final Flushable flushable) {
        try {
            if (flushable != null) {
                flushable.flush();
            }
        }
        catch (IOException ignore) {
            IoUtil.LOG.debugCloseException(ignore);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.UTIL_LOGGER;
    }
}
