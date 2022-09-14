package org.camunda.bpm.run;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.spring.boot.starter.configuration.impl.DefaultDeploymentConfiguration;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BpmRunDeploymentConfiguration extends DefaultDeploymentConfiguration {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BpmRunDeploymentConfiguration.class);
    public static final String CAMUNDA_DEPLOYMENT_DIR_PROPERTY = "camunda.deploymentDir";

    @Autowired
    private Environment env;

    public Set<Resource> getDeploymentResources() {
        String deploymentDir = this.env.getProperty("camunda.deploymentDir");
        log.info(deploymentDir);
        if (!StringUtils.isEmpty(deploymentDir)) {
            Path resourceDir = Paths.get(deploymentDir, new String[0]);
            try {
                Stream<Path> stream = Files.walk(resourceDir, new java.nio.file.FileVisitOption[0]);
                try {
                    Set<Resource> set = (Set) stream.filter(file -> !Files.isDirectory(file, new java.nio.file.LinkOption[0])).map(org.springframework.core.io.FileSystemResource::new).collect(Collectors.toSet());
                    if (stream != null)
                        stream.close();
                    return set;
                } catch (Throwable throwable) {
                    if (stream != null)
                        try {
                            stream.close();
                        } catch (Throwable throwable1) {
                            throwable.addSuppressed(throwable1);
                        }
                    throw throwable;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Collections.emptySet();
    }
}
