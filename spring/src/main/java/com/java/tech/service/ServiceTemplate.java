package com.java.tech.service;


import com.java.tech.model.request.BaseRequest;
import com.java.tech.model.response.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * created by Jason on 2020/3/5
 */
public class ServiceTemplate {

    private static final Logger serviceLog = LoggerFactory.getLogger(ServiceTemplate.class);

    public static <T extends BaseRequest, R extends BaseResult> R execute(T request, ServiceCallBack<T, R> callBack) {
        R response = null;

        try {
            //1.check parameter
            callBack.checkParameter(request);

            //2.process
            response = callBack.process(request);

        } catch (Exception e) {
            //3.fillFailedResult
            callBack.fillFailedResult(request, e.getMessage());
        } finally {
            //4.afterProcess
            callBack.afterProcess();

            buildServiceLog(response, request);
        }
        return response;
    }

    private static <R extends BaseResult, T extends BaseRequest> void buildServiceLog(R response, T request) {

    }


}
