// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import java.io.InputStream;
import java.util.Map;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.zik.bpm.engine.ProcessEngineConfiguration;
import org.springframework.core.io.Resource;

public class BeansConfigurationHelper
{
    public static ProcessEngineConfiguration parseProcessEngineConfiguration(final Resource springResource, final String beanName) {
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        final XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry)beanFactory);
        xmlBeanDefinitionReader.setValidationMode(3);
        xmlBeanDefinitionReader.loadBeanDefinitions(springResource);
        final ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl)beanFactory.getBean(beanName);
        if (processEngineConfiguration.getBeans() == null) {
            processEngineConfiguration.setBeans(new SpringBeanFactoryProxyMap((BeanFactory)beanFactory));
        }
        return processEngineConfiguration;
    }
    
    public static ProcessEngineConfiguration parseProcessEngineConfigurationFromInputStream(final InputStream inputStream, final String beanName) {
        final Resource springResource = (Resource)new InputStreamResource(inputStream);
        return parseProcessEngineConfiguration(springResource, beanName);
    }
    
    public static ProcessEngineConfiguration parseProcessEngineConfigurationFromResource(final String resource, final String beanName) {
        final Resource springResource = (Resource)new ClassPathResource(resource);
        return parseProcessEngineConfiguration(springResource, beanName);
    }
}
