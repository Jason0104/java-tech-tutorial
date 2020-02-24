package com.java.tech.template;

import com.java.tech.strategy.request.BaseRequest;
import com.java.tech.strategy.response.BaseResponse;

/**
 * created by Jason on 2020/2/24
 *
 * 定义模版方法类
 */
public class ServiceTemplate {

    private ServiceTemplate(){

    }

    public static <T extends BaseRequest,R extends BaseResponse> R execute(T request, ServiceCallBack<T,R> serviceCallBack){
        R response = null;
        try{
            //1.check parameter
            serviceCallBack.checkParameter(request);

            //2.process business
            response = serviceCallBack.process(request);
        }catch (Exception e){
            //3.fillFailedResult
            response = serviceCallBack.fillFailedResult(request,e.getMessage());
        }finally {
            //4.afterProcess
            serviceCallBack.afterProcess();
        }
        return response;

    }
}
