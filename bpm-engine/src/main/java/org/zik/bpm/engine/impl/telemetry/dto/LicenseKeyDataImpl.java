// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.telemetry.dto;

import java.util.Map;
import com.google.gson.annotations.SerializedName;
import org.zik.bpm.engine.telemetry.LicenseKeyData;

public class LicenseKeyDataImpl implements LicenseKeyData
{
    public static final String SERIALIZED_VALID_UNTIL = "valid-until";
    public static final String SERIALIZED_IS_UNLIMITED = "unlimited";
    protected String customer;
    protected String type;
    @SerializedName("valid-until")
    protected String validUntil;
    @SerializedName("unlimited")
    protected Boolean isUnlimited;
    protected Map<String, String> features;
    protected String raw;

    public LicenseKeyDataImpl(final String customer, final String type, final String validUntil, final Boolean isUnlimited, final Map<String, String> features, final String raw) {
        this.customer = customer;
        this.type = type;
        this.validUntil = validUntil;
        this.isUnlimited = isUnlimited;
        this.features = features;
        this.raw = raw;
    }

    public static LicenseKeyDataImpl fromRawString(final String rawLicense) {
        final String licenseKeyRawString = rawLicense.contains(";") ? rawLicense.split(";", 2)[1] : rawLicense;
        return new LicenseKeyDataImpl(null, null, null, null, null, licenseKeyRawString);
    }

    @Override
    public String getCustomer() {
        return this.customer;
    }

    public void setCustomer(final String customer) {
        this.customer = customer;
    }

    @Override
    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public String getValidUntil() {
        return this.validUntil;
    }

    public void setValidUntil(final String validUntil) {
        this.validUntil = validUntil;
    }

    @Override
    public Boolean isUnlimited() {
        return this.isUnlimited;
    }

    public void setUnlimited(final Boolean isUnlimited) {
        this.isUnlimited = isUnlimited;
    }

    @Override
    public Map<String, String> getFeatures() {
        return this.features;
    }

    public void setFeatures(final Map<String, String> features) {
        this.features = features;
    }

    @Override
    public String getRaw() {
        return this.raw;
    }

    public void setRaw(final String raw) {
        this.raw = raw;
    }
}
