// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.ant;

import java.io.InputStream;
import java.io.Closeable;
import org.zik.bpm.engine.impl.util.IoUtil;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.tools.ant.BuildException;
import java.io.File;
import org.apache.tools.ant.Task;

public class LaunchThread extends Thread
{
    Task task;
    String[] cmd;
    File dir;
    String msg;
    
    public LaunchThread(final Task task, final String[] cmd, final File dir, final String msg) {
        this.task = task;
        this.cmd = cmd;
        this.dir = dir;
        this.msg = msg;
    }
    
    public static void launch(final Task task, final String[] cmd, final File dir, final String launchCompleteText) {
        if (cmd == null) {
            throw new BuildException("cmd is null");
        }
        try {
            final LaunchThread launchThread = new LaunchThread(task, cmd, dir, launchCompleteText);
            launchThread.start();
            launchThread.join();
        }
        catch (Exception e) {
            throw new BuildException("couldn't launch cmd: " + cmdString(cmd), (Throwable)e);
        }
    }
    
    private static String cmdString(final String[] cmd) {
        final StringBuilder cmdText = new StringBuilder();
        for (final String cmdPart : cmd) {
            cmdText.append(cmdPart);
            cmdText.append(" ");
        }
        return cmdText.toString();
    }
    
    @Override
    public void run() {
        this.task.log("launching cmd '" + cmdString(this.cmd) + "' in dir '" + this.dir + "'");
        if (this.msg != null) {
            this.task.log("waiting for launch completion msg '" + this.msg + "'...");
        }
        else {
            this.task.log("not waiting for a launch completion msg.");
        }
        final ProcessBuilder processBuilder = new ProcessBuilder(this.cmd).redirectErrorStream(true).directory(this.dir);
        InputStream consoleStream = null;
        try {
            final Process process = processBuilder.start();
            consoleStream = process.getInputStream();
            final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(consoleStream));
            String consoleLine = "";
            while (consoleLine != null && (this.msg == null || consoleLine.indexOf(this.msg) == -1)) {
                consoleLine = consoleReader.readLine();
                if (consoleLine != null) {
                    this.task.log("  " + consoleLine);
                }
                else {
                    this.task.log("launched process completed");
                }
            }
        }
        catch (Exception e) {
            throw new BuildException("couldn't launch " + cmdString(this.cmd), (Throwable)e);
        }
        finally {
            IoUtil.closeSilently(consoleStream);
        }
    }
}
