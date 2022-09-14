// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.engine.variable.value.builder.FileValueBuilder;
import java.util.Arrays;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.UntypedValueImpl;
import org.camunda.bpm.engine.variable.impl.value.FileValueImpl;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.value.FileValue;

public class FileValueSerializer extends AbstractTypedValueSerializer<FileValue>
{
    protected static final int NR_OF_VALUES_IN_TEXTFIELD2 = 2;
    protected static final String MIMETYPE_ENCODING_SEPARATOR = "#";
    
    public FileValueSerializer() {
        super((ValueType)ValueType.FILE);
    }
    
    @Override
    public void writeValue(final FileValue value, final ValueFields valueFields) {
        final byte[] data = ((FileValueImpl)value).getByteArray();
        valueFields.setByteArrayValue(data);
        valueFields.setTextValue(value.getFilename());
        if (value.getMimeType() == null && value.getEncoding() != null) {
            valueFields.setTextValue2("#" + value.getEncoding());
        }
        else if (value.getMimeType() != null && value.getEncoding() == null) {
            valueFields.setTextValue2(value.getMimeType() + "#");
        }
        else if (value.getMimeType() != null && value.getEncoding() != null) {
            valueFields.setTextValue2(value.getMimeType() + "#" + value.getEncoding());
        }
    }
    
    @Override
    public FileValue convertToTypedValue(final UntypedValueImpl untypedValue) {
        throw new UnsupportedOperationException("Currently no automatic conversation from UntypedValue to FileValue");
    }
    
    @Override
    public FileValue readValue(final ValueFields valueFields, final boolean deserializeValue, final boolean asTransientValue) {
        String fileName = valueFields.getTextValue();
        if (fileName == null) {
            fileName = "";
        }
        final FileValueBuilder builder = Variables.fileValue(fileName);
        if (valueFields.getByteArrayValue() != null) {
            builder.file(valueFields.getByteArrayValue());
        }
        if (valueFields.getTextValue2() != null) {
            final String[] split = Arrays.copyOf(valueFields.getTextValue2().split("#", 2), 2);
            final String mimeType = this.returnNullIfEmptyString(split[0]);
            final String encoding = this.returnNullIfEmptyString(split[1]);
            builder.mimeType(mimeType);
            builder.encoding(encoding);
        }
        builder.setTransient(asTransientValue);
        return (FileValue)builder.create();
    }
    
    protected String returnNullIfEmptyString(final String s) {
        if (s.isEmpty()) {
            return null;
        }
        return s;
    }
    
    @Override
    public String getName() {
        return this.valueType.getName();
    }
    
    @Override
    protected boolean canWriteValue(final TypedValue value) {
        return value != null && value.getType() != null && value.getType().getName().equals(this.getName());
    }
}
