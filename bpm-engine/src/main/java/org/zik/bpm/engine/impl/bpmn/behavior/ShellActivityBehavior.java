// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.VariableScope;
import java.io.IOException;
import java.io.Writer;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.InputStream;
import java.util.Map;
import java.util.List;
import java.io.File;
import java.util.ArrayList;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.delegate.Expression;

public class ShellActivityBehavior extends AbstractBpmnActivityBehavior
{
    protected static final BpmnBehaviorLogger LOG;
    protected Expression command;
    protected Expression wait;
    protected Expression arg1;
    protected Expression arg2;
    protected Expression arg3;
    protected Expression arg4;
    protected Expression arg5;
    protected Expression outputVariable;
    protected Expression errorCodeVariable;
    protected Expression redirectError;
    protected Expression cleanEnv;
    protected Expression directory;
    String commandStr;
    String arg1Str;
    String arg2Str;
    String arg3Str;
    String arg4Str;
    String arg5Str;
    String waitStr;
    String resultVariableStr;
    String errorCodeVariableStr;
    Boolean waitFlag;
    Boolean redirectErrorFlag;
    Boolean cleanEnvBoolan;
    String directoryStr;
    
    private void readFields(final ActivityExecution execution) {
        this.commandStr = this.getStringFromField(this.command, execution);
        this.arg1Str = this.getStringFromField(this.arg1, execution);
        this.arg2Str = this.getStringFromField(this.arg2, execution);
        this.arg3Str = this.getStringFromField(this.arg3, execution);
        this.arg4Str = this.getStringFromField(this.arg4, execution);
        this.arg5Str = this.getStringFromField(this.arg5, execution);
        this.waitStr = this.getStringFromField(this.wait, execution);
        this.resultVariableStr = this.getStringFromField(this.outputVariable, execution);
        this.errorCodeVariableStr = this.getStringFromField(this.errorCodeVariable, execution);
        final String redirectErrorStr = this.getStringFromField(this.redirectError, execution);
        final String cleanEnvStr = this.getStringFromField(this.cleanEnv, execution);
        this.waitFlag = (this.waitStr == null || this.waitStr.equals("true"));
        this.redirectErrorFlag = (redirectErrorStr != null && redirectErrorStr.equals("true"));
        this.cleanEnvBoolan = (cleanEnvStr != null && cleanEnvStr.equals("true"));
        this.directoryStr = this.getStringFromField(this.directory, execution);
    }
    
    @Override
    public void execute(final ActivityExecution execution) {
        this.readFields(execution);
        final List<String> argList = new ArrayList<String>();
        argList.add(this.commandStr);
        if (this.arg1Str != null) {
            argList.add(this.arg1Str);
        }
        if (this.arg2Str != null) {
            argList.add(this.arg2Str);
        }
        if (this.arg3Str != null) {
            argList.add(this.arg3Str);
        }
        if (this.arg4Str != null) {
            argList.add(this.arg4Str);
        }
        if (this.arg5Str != null) {
            argList.add(this.arg5Str);
        }
        final ProcessBuilder processBuilder = new ProcessBuilder(argList);
        try {
            processBuilder.redirectErrorStream(this.redirectErrorFlag);
            if (this.cleanEnvBoolan) {
                final Map<String, String> env = processBuilder.environment();
                env.clear();
            }
            if (this.directoryStr != null && this.directoryStr.length() > 0) {
                processBuilder.directory(new File(this.directoryStr));
            }
            final Process process = processBuilder.start();
            if (this.waitFlag) {
                final int errorCode = process.waitFor();
                if (this.resultVariableStr != null) {
                    final String result = convertStreamToStr(process.getInputStream());
                    execution.setVariable(this.resultVariableStr, result);
                }
                if (this.errorCodeVariableStr != null) {
                    execution.setVariable(this.errorCodeVariableStr, Integer.toString(errorCode));
                }
            }
        }
        catch (Exception e) {
            throw ShellActivityBehavior.LOG.shellExecutionException(e);
        }
        this.leave(execution);
    }
    
    public static String convertStreamToStr(final InputStream is) throws IOException {
        if (is != null) {
            final Writer writer = new StringWriter();
            final char[] buffer = new char[1024];
            try {
                final Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            }
            finally {
                is.close();
            }
            return writer.toString();
        }
        return "";
    }
    
    protected String getStringFromField(final Expression expression, final DelegateExecution execution) {
        if (expression != null) {
            final Object value = expression.getValue(execution);
            if (value != null) {
                return value.toString();
            }
        }
        return null;
    }
    
    static {
        LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;
    }
}
