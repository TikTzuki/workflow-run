package org.camunda.bpm.run;

import org.camunda.bpm.engine.impl.cfg.CompositeProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.plugin.AdministratorAuthorizationPlugin;
import org.camunda.bpm.identity.impl.ldap.plugin.LdapIdentityProviderPlugin;
import org.camunda.bpm.run.property.BpmRunProcessEnginePluginProperty;
import org.camunda.bpm.run.property.BpmRunProperties;
import org.camunda.bpm.run.utils.BpmRunProcessEnginePluginHelper;
import org.camunda.bpm.spring.boot.starter.CamundaBpmAutoConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaDeploymentConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@EnableConfigurationProperties({BpmRunProperties.class})
@Configuration
@AutoConfigureAfter({CamundaBpmAutoConfiguration.class})
public class BpmRunConfiguration {
    @Autowired
    BpmRunProperties camundaBpmRunProperties;

    @Bean
    @ConditionalOnProperty(name = {"enabled"}, havingValue = "true", prefix = "camunda.bpm.run.ldap")
    public LdapIdentityProviderPlugin ldapIdentityProviderPlugin() {
        return (LdapIdentityProviderPlugin) this.camundaBpmRunProperties.getLdap();
    }

    @Bean
    @ConditionalOnProperty(name = {"enabled"}, havingValue = "true", prefix = "camunda.bpm.run.admin-auth")
    public AdministratorAuthorizationPlugin administratorAuthorizationPlugin() {
        return (AdministratorAuthorizationPlugin) this.camundaBpmRunProperties.getAdminAuth();
    }

    @Bean
    public ProcessEngineConfigurationImpl processEngineConfigurationImpl(List<ProcessEnginePlugin> processEnginePlugins) {
        BpmRunProcessEngineConfiguration camundaBpmRunProcessEngineConfiguration = new BpmRunProcessEngineConfiguration();
        List<BpmRunProcessEnginePluginProperty> yamlPluginsInfo = this.camundaBpmRunProperties.getProcessEnginePlugins();
        BpmRunProcessEnginePluginHelper.registerYamlPlugins(processEnginePlugins, yamlPluginsInfo);
        camundaBpmRunProcessEngineConfiguration.getProcessEnginePlugins().add(new CompositeProcessEnginePlugin(processEnginePlugins));
        return (ProcessEngineConfigurationImpl) camundaBpmRunProcessEngineConfiguration;
    }

    @Bean
    public static CamundaDeploymentConfiguration camundaDeploymentConfiguration() {
        return (CamundaDeploymentConfiguration) new BpmRunDeploymentConfiguration();
    }
}
