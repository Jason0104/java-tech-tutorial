package com.java.tech.strategy;

/**
 * created by Jason on 2020/2/24
 */
public enum ChannelType {
    SMS(new SmsSendServiceImpl()),
    EMAIL(new EmailSendServiceImpl());

    private CommonSendService commonSendService;

    ChannelType(CommonSendService commonSendService) {
        this.commonSendService = commonSendService;
    }

    public CommonSendService getCommonSendService(){
        return this.commonSendService;
    }
}
