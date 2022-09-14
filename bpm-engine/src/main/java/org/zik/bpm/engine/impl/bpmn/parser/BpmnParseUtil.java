// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import java.util.HashMap;
import java.util.Map;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.ScriptUtil;
import org.zik.bpm.engine.impl.scripting.ExecutableScript;
import org.zik.bpm.engine.impl.core.variable.mapping.value.NullValueProvider;
import org.zik.bpm.engine.impl.scripting.ScriptValueProvider;
import org.zik.bpm.engine.impl.core.variable.mapping.value.MapValueProvider;
import org.zik.bpm.engine.impl.el.ElValueProvider;
import java.util.TreeMap;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ListValueProvider;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.core.variable.mapping.OutputParameter;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;
import org.zik.bpm.engine.impl.core.variable.mapping.InputParameter;
import org.zik.bpm.engine.BpmnParseException;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.core.variable.mapping.IoMapping;
import org.zik.bpm.engine.impl.util.xml.Element;

public final class BpmnParseUtil
{
    public static Element findCamundaExtensionElement(final Element element, final String extensionElementName) {
        final Element extensionElements = element.element("extensionElements");
        if (extensionElements != null) {
            return extensionElements.elementNS(BpmnParse.CAMUNDA_BPMN_EXTENSIONS_NS, extensionElementName);
        }
        return null;
    }
    
    public static IoMapping parseInputOutput(final Element element) {
        final Element inputOutputElement = element.elementNS(BpmnParse.CAMUNDA_BPMN_EXTENSIONS_NS, "inputOutput");
        if (inputOutputElement != null) {
            final IoMapping ioMapping = new IoMapping();
            parseCamundaInputParameters(inputOutputElement, ioMapping);
            parseCamundaOutputParameters(inputOutputElement, ioMapping);
            return ioMapping;
        }
        return null;
    }
    
    public static void parseCamundaInputParameters(final Element inputOutputElement, final IoMapping ioMapping) {
        final List<Element> inputParameters = inputOutputElement.elementsNS(BpmnParse.CAMUNDA_BPMN_EXTENSIONS_NS, "inputParameter");
        for (final Element inputParameterElement : inputParameters) {
            parseInputParameterElement(inputParameterElement, ioMapping);
        }
    }
    
    public static void parseCamundaOutputParameters(final Element inputOutputElement, final IoMapping ioMapping) {
        final List<Element> outputParameters = inputOutputElement.elementsNS(BpmnParse.CAMUNDA_BPMN_EXTENSIONS_NS, "outputParameter");
        for (final Element outputParameterElement : outputParameters) {
            parseOutputParameterElement(outputParameterElement, ioMapping);
        }
    }
    
    public static void parseInputParameterElement(final Element inputParameterElement, final IoMapping ioMapping) {
        final String nameAttribute = inputParameterElement.attribute("name");
        if (nameAttribute == null || nameAttribute.isEmpty()) {
            throw new BpmnParseException("Missing attribute 'name' for inputParameter", inputParameterElement);
        }
        final ParameterValueProvider valueProvider = parseNestedParamValueProvider(inputParameterElement);
        ioMapping.addInputParameter(new InputParameter(nameAttribute, valueProvider));
    }
    
    public static void parseOutputParameterElement(final Element outputParameterElement, final IoMapping ioMapping) {
        final String nameAttribute = outputParameterElement.attribute("name");
        if (nameAttribute == null || nameAttribute.isEmpty()) {
            throw new BpmnParseException("Missing attribute 'name' for outputParameter", outputParameterElement);
        }
        final ParameterValueProvider valueProvider = parseNestedParamValueProvider(outputParameterElement);
        ioMapping.addOutputParameter(new OutputParameter(nameAttribute, valueProvider));
    }
    
    protected static ParameterValueProvider parseNestedParamValueProvider(final Element element) {
        if (element.elements().size() == 0) {
            return parseParamValueProvider(element);
        }
        if (element.elements().size() == 1) {
            return parseParamValueProvider(element.elements().get(0));
        }
        throw new BpmnParseException("Nested parameter can at most have one child element", element);
    }
    
    protected static ParameterValueProvider parseParamValueProvider(final Element parameterElement) {
        if ("list".equals(parameterElement.getTagName())) {
            final List<ParameterValueProvider> providerList = new ArrayList<ParameterValueProvider>();
            for (final Element element : parameterElement.elements()) {
                providerList.add(parseParamValueProvider(element));
            }
            return new ListValueProvider(providerList);
        }
        if ("map".equals(parameterElement.getTagName())) {
            final TreeMap<ParameterValueProvider, ParameterValueProvider> providerMap = new TreeMap<ParameterValueProvider, ParameterValueProvider>();
            for (final Element entryElement : parameterElement.elements("entry")) {
                final String keyAttribute = entryElement.attribute("key");
                if (keyAttribute == null || keyAttribute.isEmpty()) {
                    throw new BpmnParseException("Missing attribute 'key' for 'entry' element", entryElement);
                }
                providerMap.put(new ElValueProvider(getExpressionManager().createExpression(keyAttribute)), parseNestedParamValueProvider(entryElement));
            }
            return new MapValueProvider(providerMap);
        }
        if ("script".equals(parameterElement.getTagName())) {
            final ExecutableScript executableScript = parseCamundaScript(parameterElement);
            if (executableScript != null) {
                return new ScriptValueProvider(executableScript);
            }
            return new NullValueProvider();
        }
        else {
            final String textContent = parameterElement.getText().trim();
            if (!textContent.isEmpty()) {
                return new ElValueProvider(getExpressionManager().createExpression(textContent));
            }
            return new NullValueProvider();
        }
    }
    
    public static ExecutableScript parseCamundaScript(final Element scriptElement) {
        final String scriptLanguage = scriptElement.attribute("scriptFormat");
        if (scriptLanguage == null || scriptLanguage.isEmpty()) {
            throw new BpmnParseException("Missing attribute 'scriptFormat' for 'script' element", scriptElement);
        }
        final String scriptResource = scriptElement.attribute("resource");
        final String scriptSource = scriptElement.getText();
        try {
            return ScriptUtil.getScript(scriptLanguage, scriptSource, scriptResource, getExpressionManager());
        }
        catch (ProcessEngineException e) {
            throw new BpmnParseException("Unable to process script", scriptElement, e);
        }
    }
    
    public static Map<String, String> parseCamundaExtensionProperties(final Element element) {
        final Element propertiesElement = findCamundaExtensionElement(element, "properties");
        if (propertiesElement != null) {
            final List<Element> properties = propertiesElement.elementsNS(BpmnParse.CAMUNDA_BPMN_EXTENSIONS_NS, "property");
            final Map<String, String> propertiesMap = new HashMap<String, String>();
            for (final Element property : properties) {
                propertiesMap.put(property.attribute("name"), property.attribute("value"));
            }
            return propertiesMap;
        }
        return null;
    }
    
    protected static ExpressionManager getExpressionManager() {
        return Context.getProcessEngineConfiguration().getExpressionManager();
    }
}
