// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.ant;

import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;
import org.apache.tools.ant.BuildException;
import java.io.File;
import org.apache.tools.ant.Task;

public class LaunchTask extends Task
{
    private static final String FILESEPARATOR;
    File dir;
    String script;
    String msg;
    String args;
    
    public void execute() throws BuildException {
        if (this.dir == null) {
            throw new BuildException("dir attribute is required with the launch task");
        }
        if (this.script == null) {
            throw new BuildException("script attribute is required with the launch task");
        }
        String[] cmd = null;
        final String executable = this.getExecutable();
        if (this.args != null) {
            final List<String> pieces = new ArrayList<String>();
            pieces.add(executable);
            final StringTokenizer tokenizer = new StringTokenizer("args", " ");
            while (tokenizer.hasMoreTokens()) {
                pieces.add(tokenizer.nextToken());
            }
            cmd = pieces.toArray(new String[pieces.size()]);
        }
        else {
            cmd = new String[] { executable };
        }
        LaunchThread.launch(this, cmd, this.dir, this.msg);
    }
    
    public String getExecutable() {
        final String os = System.getProperty("os.name").toLowerCase();
        final String dirPath = this.dir.getAbsolutePath();
        final String base = dirPath + LaunchTask.FILESEPARATOR + this.script;
        if (this.exists(base)) {
            return base;
        }
        if (os.indexOf("windows") != -1) {
            if (this.exists(base + ".exe")) {
                return base + ".exe";
            }
            if (this.exists(base + ".bat")) {
                return base + ".bat";
            }
        }
        if ((os.indexOf("linux") != -1 || os.indexOf("mac") != -1) && this.exists(base + ".sh")) {
            return base + ".sh";
        }
        throw new BuildException("couldn't find executable for script " + base);
    }
    
    public boolean exists(final String path) {
        final File file = new File(path);
        return file.exists();
    }
    
    public void setDir(final File dir) {
        this.dir = dir;
    }
    
    public void setScript(final String script) {
        this.script = script;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    public void setArgs(final String args) {
        this.args = args;
    }
    
    static {
        FILESEPARATOR = System.getProperty("file.separator");
    }
}
