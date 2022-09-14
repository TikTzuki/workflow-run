// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.type;

import java.util.Iterator;
import java.util.LinkedHashMap;
import org.zik.bpm.engine.impl.bpmn.parser.BpmnParse;
import org.zik.bpm.engine.impl.util.xml.Element;
import java.util.HashMap;
import java.util.Map;

public class FormTypes
{
    protected Map<String, AbstractFormFieldType> formTypes;
    
    public FormTypes() {
        this.formTypes = new HashMap<String, AbstractFormFieldType>();
    }
    
    public void addFormType(final AbstractFormFieldType formType) {
        this.formTypes.put(formType.getName(), formType);
    }
    
    public AbstractFormFieldType parseFormPropertyType(final Element formFieldElement, final BpmnParse bpmnParse) {
        AbstractFormFieldType formType = null;
        final String typeText = formFieldElement.attribute("type");
        final String datePatternText = formFieldElement.attribute("datePattern");
        if (typeText == null && "formField".equals(formFieldElement.getTagName())) {
            bpmnParse.addError("form field must have a 'type' attribute", formFieldElement);
        }
        if ("date".equals(typeText) && datePatternText != null) {
            formType = new DateFormType(datePatternText);
        }
        else if ("enum".equals(typeText)) {
            final Map<String, String> values = new LinkedHashMap<String, String>();
            for (final Element valueElement : formFieldElement.elementsNS(BpmnParse.CAMUNDA_BPMN_EXTENSIONS_NS, "value")) {
                final String valueId = valueElement.attribute("id");
                final String valueName = valueElement.attribute("name");
                values.put(valueId, valueName);
            }
            formType = new EnumFormType(values);
        }
        else if (typeText != null) {
            formType = this.formTypes.get(typeText);
            if (formType == null) {
                bpmnParse.addError("unknown type '" + typeText + "'", formFieldElement);
            }
        }
        return formType;
    }
    
    public AbstractFormFieldType getFormType(final String name) {
        return this.formTypes.get(name);
    }
}
