// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.telemetry.dto.JdkImpl;
import org.zik.bpm.engine.impl.el.Expression;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import java.util.Arrays;
import org.zik.bpm.engine.impl.calendar.DurationHelper;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.bpmn.parser.FailedJobRetryConfiguration;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.regex.Matcher;
import org.zik.bpm.engine.exception.NotValidException;
import java.util.regex.Pattern;

public class ParseUtil
{
    private static final EngineUtilLogger LOG;
    protected static final Pattern REGEX_TTL_ISO;
    
    public static Integer parseHistoryTimeToLive(String historyTimeToLive) {
        Integer timeToLive = null;
        if (historyTimeToLive != null && !historyTimeToLive.isEmpty()) {
            final Matcher matISO = ParseUtil.REGEX_TTL_ISO.matcher(historyTimeToLive);
            if (matISO.find()) {
                historyTimeToLive = matISO.group(1);
            }
            timeToLive = parseIntegerAttribute("historyTimeToLive", historyTimeToLive);
        }
        if (timeToLive != null && timeToLive < 0) {
            throw new NotValidException("Cannot parse historyTimeToLive: negative value is not allowed");
        }
        return timeToLive;
    }
    
    protected static Integer parseIntegerAttribute(final String attributeName, final String text) {
        Integer result = null;
        if (text != null && !text.isEmpty()) {
            try {
                result = Integer.parseInt(text);
            }
            catch (NumberFormatException e) {
                throw new ProcessEngineException("Cannot parse " + attributeName + ": " + e.getMessage());
            }
        }
        return result;
    }
    
    public static FailedJobRetryConfiguration parseRetryIntervals(final String retryIntervals) {
        if (retryIntervals == null || retryIntervals.isEmpty()) {
            return null;
        }
        if (StringUtil.isExpression(retryIntervals)) {
            final ExpressionManager expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
            final Expression expression = expressionManager.createExpression(retryIntervals);
            return new FailedJobRetryConfiguration(expression);
        }
        final String[] intervals = StringUtil.split(retryIntervals, ",");
        int retries = intervals.length + 1;
        if (intervals.length == 1) {
            try {
                final DurationHelper durationHelper = new DurationHelper(intervals[0]);
                if (durationHelper.isRepeat()) {
                    retries = durationHelper.getTimes();
                }
            }
            catch (Exception e) {
                ParseUtil.LOG.logParsingRetryIntervals(intervals[0], e);
                return null;
            }
        }
        return new FailedJobRetryConfiguration(retries, Arrays.asList(intervals));
    }
    
    public static ProcessEngineDetails parseProcessEngineVersion(final boolean trimSuffixEE) {
        final String version = ProductPropertiesUtil.getProductVersion();
        return parseProcessEngineVersion(version, trimSuffixEE);
    }
    
    public static ProcessEngineDetails parseProcessEngineVersion(String version, final boolean trimSuffixEE) {
        String edition = "community";
        if (version.contains("-ee")) {
            edition = "enterprise";
            if (trimSuffixEE) {
                version = version.replace("-ee", "");
            }
        }
        return new ProcessEngineDetails(version, edition);
    }
    
    public static String parseServerVendor(final String applicationServerInfo) {
        String serverVendor = "";
        final Pattern pattern = Pattern.compile("[\\sA-Za-z]+");
        final Matcher matcher = pattern.matcher(applicationServerInfo);
        if (matcher.find()) {
            try {
                serverVendor = matcher.group();
            }
            catch (IllegalStateException ex) {}
            serverVendor = serverVendor.trim();
            if (serverVendor.contains("WildFly")) {
                return "WildFly";
            }
        }
        return serverVendor;
    }
    
    public static JdkImpl parseJdkDetails() {
        String jdkVendor = System.getProperty("java.vm.vendor");
        if (jdkVendor != null && jdkVendor.contains("Oracle") && System.getProperty("java.vm.name").contains("OpenJDK")) {
            jdkVendor = "OpenJDK";
        }
        final String jdkVersion = System.getProperty("java.version");
        final JdkImpl jdk = new JdkImpl(jdkVersion, jdkVendor);
        return jdk;
    }
    
    static {
        LOG = ProcessEngineLogger.UTIL_LOGGER;
        REGEX_TTL_ISO = Pattern.compile("^P(\\d+)D$");
    }
}
