package com.java.tech.strategy.request;

import java.io.Serializable;

/**
 * created by Jason on 2020/2/24
 */
public class BaseRequest implements Serializable {

    private String requestId;
    private String requestBody;
    private String channelType;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }
}
