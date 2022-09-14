// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import java.util.HashMap;
import java.util.Map;

public class ConnectUtil
{
    public static final String PARAM_NAME_REQUEST_URL = "url";
    public static final String PARAM_NAME_REQUEST_METHOD = "method";
    public static final String PARAM_NAME_REQUEST_PAYLOAD = "payload";
    public static final String PARAM_NAME_REQUEST_CONFIG = "request-config";
    public static final String METHOD_NAME_POST = "POST";
    public static final String CONFIG_NAME_CONNECTION_TIMEOUT = "connection-timeout";
    public static final String CONFIG_NAME_SOCKET_TIMEOUT = "socket-timeout";
    public static final String PARAM_NAME_RESPONSE_STATUS_CODE = "statusCode";
    public static final String PARAM_NAME_RESPONSE = "response";
    public static final String PARAM_NAME_HEADERS = "headers";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    
    public static Map<String, Object> assembleRequestParameters(final String methodName, final String url, final String contentType, final String payload) {
        final Map<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("Content-Type", contentType);
        final Map<String, Object> requestParams = new HashMap<String, Object>();
        requestParams.put("method", methodName);
        requestParams.put("url", url);
        requestParams.put("headers", requestHeaders);
        requestParams.put("payload", payload);
        return requestParams;
    }
    
    public static Map<String, Object> addRequestTimeoutConfiguration(final Map<String, Object> requestParams, final int timeout) {
        final Map<String, Object> config = new HashMap<String, Object>();
        config.put("connection-timeout", timeout);
        config.put("socket-timeout", timeout);
        requestParams.put("request-config", config);
        return requestParams;
    }
}
