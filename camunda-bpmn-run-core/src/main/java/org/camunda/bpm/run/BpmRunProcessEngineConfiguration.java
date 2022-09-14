package org.camunda.bpm.run;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class BpmRunProcessEngineConfiguration extends SpringProcessEngineConfiguration {
    private static final Logger log = LoggerFactory.getLogger(BpmRunProcessEngineConfiguration.class);
    @Inject
    private Environment env;

    public BpmRunProcessEngineConfiguration() {
        setDeployChangedOnly(true);
    }

    protected String getFileResourceName(Resource resource) {
        try {
            String deploymentDir = this.env.getProperty("camunda.deploymentDir");
            log.info(deploymentDir);
            if (File.separator.equals("\\"))
                deploymentDir = deploymentDir.replace("\\", "/");
            String resourceAbsolutePath = resource.getURI().toString();
            log.info(resourceAbsolutePath);
            int startIndex = resourceAbsolutePath.indexOf(deploymentDir) + deploymentDir.length();
            String fileResourceName =resourceAbsolutePath.substring(startIndex);
            log.info(fileResourceName);
            return fileResourceName;
        } catch (IOException e) {
            throw new ProcessEngineException("Failed to locate resource " + resource.getFilename(), e);
        }
    }

    protected void initTelemetryData() {
        super.initTelemetryData();
        Set<String> camundaIntegration = this.telemetryData.getProduct().getInternals().getCamundaIntegration();
        camundaIntegration.add("camunda-bpm-run");
    }
}
