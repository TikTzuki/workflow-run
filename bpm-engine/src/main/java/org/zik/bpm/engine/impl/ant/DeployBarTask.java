// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.ant;

import org.zik.bpm.engine.RepositoryService;
import org.zik.bpm.engine.ProcessEngineInfo;
import org.zik.bpm.engine.ProcessEngine;
import org.apache.tools.ant.DirectoryScanner;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import java.io.Closeable;
import org.zik.bpm.engine.impl.util.IoUtil;
import java.io.InputStream;
import java.util.zip.ZipInputStream;
import java.io.FileInputStream;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.ProcessEngines;
import org.zik.bpm.engine.impl.util.LogUtil;
import java.util.Arrays;
import java.util.ArrayList;
import org.apache.tools.ant.types.FileSet;
import java.util.List;
import java.io.File;
import org.apache.tools.ant.Task;

public class DeployBarTask extends Task
{
    String processEngineName;
    File file;
    List<FileSet> fileSets;
    
    public DeployBarTask() {
        this.processEngineName = "default";
    }
    
    public void execute() throws BuildException {
        final List<File> files = new ArrayList<File>();
        if (this.file != null) {
            files.add(this.file);
        }
        if (this.fileSets != null) {
            for (final FileSet fileSet : this.fileSets) {
                final DirectoryScanner directoryScanner = fileSet.getDirectoryScanner(this.getProject());
                final File baseDir = directoryScanner.getBasedir();
                final String[] includedFiles = directoryScanner.getIncludedFiles();
                final String[] excludedFiles = directoryScanner.getExcludedFiles();
                final List<String> excludedFilesList = Arrays.asList(excludedFiles);
                for (final String includedFile : includedFiles) {
                    if (!excludedFilesList.contains(includedFile)) {
                        files.add(new File(baseDir, includedFile));
                    }
                }
            }
        }
        final Thread currentThread = Thread.currentThread();
        final ClassLoader originalClassLoader = currentThread.getContextClassLoader();
        currentThread.setContextClassLoader(DeployBarTask.class.getClassLoader());
        LogUtil.readJavaUtilLoggingConfigFromClasspath();
        try {
            this.log("Initializing process engine " + this.processEngineName);
            ProcessEngines.init();
            final ProcessEngine processEngine = ProcessEngines.getProcessEngine(this.processEngineName);
            if (processEngine == null) {
                final List<ProcessEngineInfo> processEngineInfos = ProcessEngines.getProcessEngineInfos();
                if (processEngineInfos != null && processEngineInfos.size() > 0) {
                    final String message = this.getErrorMessage(processEngineInfos, this.processEngineName);
                    throw new ProcessEngineException(message);
                }
                throw new ProcessEngineException("Could not find a process engine with name '" + this.processEngineName + "', no engines found. Make sure an engine configuration is present on the classpath");
            }
            else {
                final RepositoryService repositoryService = processEngine.getRepositoryService();
                this.log("Starting to deploy " + files.size() + " files");
                for (final File file : files) {
                    final String path = file.getAbsolutePath();
                    this.log("Handling file " + path);
                    try {
                        final FileInputStream inputStream = new FileInputStream(file);
                        try {
                            this.log("deploying bar " + path);
                            repositoryService.createDeployment().name(file.getName()).addZipInputStream(new ZipInputStream(inputStream)).deploy();
                        }
                        finally {
                            IoUtil.closeSilently(inputStream);
                        }
                    }
                    catch (Exception e) {
                        throw new BuildException("couldn't deploy bar " + path + ": " + e.getMessage(), (Throwable)e);
                    }
                }
            }
        }
        finally {
            currentThread.setContextClassLoader(originalClassLoader);
        }
    }
    
    private String getErrorMessage(final List<ProcessEngineInfo> processEngineInfos, final String name) {
        final StringBuilder builder = new StringBuilder("Could not find a process engine with name ");
        builder.append(name).append(", engines loaded:\n");
        for (final ProcessEngineInfo engineInfo : processEngineInfos) {
            final String engineName = (engineInfo.getName() != null) ? engineInfo.getName() : "unknown";
            builder.append("Process engine name: ").append(engineName);
            builder.append(" - resource: ").append(engineInfo.getResourceUrl());
            builder.append(" - status: ");
            if (engineInfo.getException() != null) {
                builder.append("Error while initializing engine. ");
                if (engineInfo.getException().indexOf("driver on UnpooledDataSource") != -1) {
                    builder.append("Exception while initializing process engine! Database or database driver might not have been configured correctly.").append("Please consult the user guide for supported database environments or build.properties. Stacktrace: ").append(engineInfo.getException());
                }
                else {
                    builder.append("Stacktrace: ").append(engineInfo.getException());
                }
            }
            else {
                builder.append("Initialised");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
    
    public String getProcessEngineName() {
        return this.processEngineName;
    }
    
    public void setProcessEngineName(final String processEngineName) {
        this.processEngineName = processEngineName;
    }
    
    public File getFile() {
        return this.file;
    }
    
    public void setFile(final File file) {
        this.file = file;
    }
    
    public List<FileSet> getFileSets() {
        return this.fileSets;
    }
    
    public void setFileSets(final List<FileSet> fileSets) {
        this.fileSets = fileSets;
    }
}
