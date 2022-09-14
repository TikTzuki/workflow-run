// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.configuration;

import org.zik.bpm.engine.impl.metrics.dmn.MetricsDecisionEvaluationListener;
import org.zik.bpm.engine.impl.history.parser.HistoryDecisionEvaluationListener;
import org.camunda.bpm.dmn.engine.impl.spi.transform.DmnTransformer;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationListener;
import org.camunda.bpm.dmn.engine.impl.spi.el.ElProvider;
import org.zik.bpm.engine.impl.dmn.el.ProcessEngineElProvider;
import org.zik.bpm.engine.impl.dmn.transformer.DecisionDefinitionHandler;
import org.camunda.bpm.model.dmn.instance.Decision;
import org.camunda.bpm.dmn.engine.impl.spi.transform.DmnElementTransformHandler;
import org.zik.bpm.engine.impl.dmn.transformer.DecisionRequirementsDefinitionTransformHandler;
import org.camunda.bpm.model.dmn.instance.Definitions;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.camunda.bpm.dmn.feel.impl.scala.function.FeelCustomFunctionProvider;
import java.util.List;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.dmn.engine.impl.spi.el.DmnScriptEngineResolver;
import org.zik.bpm.engine.impl.history.producer.DmnHistoryEventProducer;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;

public class DmnEngineConfigurationBuilder
{
    protected final DefaultDmnEngineConfiguration dmnEngineConfiguration;
    protected DmnHistoryEventProducer dmnHistoryEventProducer;
    protected DmnScriptEngineResolver scriptEngineResolver;
    protected ExpressionManager expressionManager;
    protected List<FeelCustomFunctionProvider> feelCustomFunctionProviders;
    
    public DmnEngineConfigurationBuilder(final DefaultDmnEngineConfiguration dmnEngineConfiguration) {
        EnsureUtil.ensureNotNull("dmnEngineConfiguration", dmnEngineConfiguration);
        this.dmnEngineConfiguration = dmnEngineConfiguration;
    }
    
    public DmnEngineConfigurationBuilder dmnHistoryEventProducer(final DmnHistoryEventProducer dmnHistoryEventProducer) {
        this.dmnHistoryEventProducer = dmnHistoryEventProducer;
        return this;
    }
    
    public DmnEngineConfigurationBuilder scriptEngineResolver(final DmnScriptEngineResolver scriptEngineResolver) {
        this.scriptEngineResolver = scriptEngineResolver;
        return this;
    }
    
    public DmnEngineConfigurationBuilder expressionManager(final ExpressionManager expressionManager) {
        this.expressionManager = expressionManager;
        return this;
    }
    
    public DmnEngineConfigurationBuilder feelCustomFunctionProviders(final List<FeelCustomFunctionProvider> feelCustomFunctionProviders) {
        this.feelCustomFunctionProviders = feelCustomFunctionProviders;
        return this;
    }
    
    public DefaultDmnEngineConfiguration build() {
        final List<DmnDecisionEvaluationListener> decisionEvaluationListeners = this.createCustomPostDecisionEvaluationListeners();
        this.dmnEngineConfiguration.setCustomPostDecisionEvaluationListeners((List)decisionEvaluationListeners);
        final DmnTransformer dmnTransformer = this.dmnEngineConfiguration.getTransformer();
        dmnTransformer.getElementTransformHandlerRegistry().addHandler((Class)Definitions.class, (DmnElementTransformHandler)new DecisionRequirementsDefinitionTransformHandler());
        dmnTransformer.getElementTransformHandlerRegistry().addHandler((Class)Decision.class, (DmnElementTransformHandler)new DecisionDefinitionHandler());
        if (this.dmnEngineConfiguration.getScriptEngineResolver() == null) {
            EnsureUtil.ensureNotNull("scriptEngineResolver", this.scriptEngineResolver);
            this.dmnEngineConfiguration.setScriptEngineResolver(this.scriptEngineResolver);
        }
        if (this.dmnEngineConfiguration.getElProvider() == null) {
            EnsureUtil.ensureNotNull("expressionManager", this.expressionManager);
            final ProcessEngineElProvider elProvider = new ProcessEngineElProvider(this.expressionManager);
            this.dmnEngineConfiguration.setElProvider((ElProvider)elProvider);
        }
        if (this.dmnEngineConfiguration.getFeelCustomFunctionProviders() == null) {
            this.dmnEngineConfiguration.setFeelCustomFunctionProviders((List)this.feelCustomFunctionProviders);
        }
        return this.dmnEngineConfiguration;
    }
    
    protected List<DmnDecisionEvaluationListener> createCustomPostDecisionEvaluationListeners() {
        EnsureUtil.ensureNotNull("dmnHistoryEventProducer", this.dmnHistoryEventProducer);
        final HistoryDecisionEvaluationListener historyDecisionEvaluationListener = new HistoryDecisionEvaluationListener(this.dmnHistoryEventProducer);
        final List<DmnDecisionEvaluationListener> customPostDecisionEvaluationListeners = (List<DmnDecisionEvaluationListener>)this.dmnEngineConfiguration.getCustomPostDecisionEvaluationListeners();
        customPostDecisionEvaluationListeners.add((DmnDecisionEvaluationListener)new MetricsDecisionEvaluationListener());
        customPostDecisionEvaluationListeners.add((DmnDecisionEvaluationListener)historyDecisionEvaluationListener);
        return customPostDecisionEvaluationListeners;
    }
    
    public DmnEngineConfigurationBuilder enableFeelLegacyBehavior(final boolean dmnFeelEnableLegacyBehavior) {
        this.dmnEngineConfiguration.enableFeelLegacyBehavior(dmnFeelEnableLegacyBehavior);
        return this;
    }
}
