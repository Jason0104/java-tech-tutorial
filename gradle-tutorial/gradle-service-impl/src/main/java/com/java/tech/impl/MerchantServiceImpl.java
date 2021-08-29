package com.java.tech.impl;

import com.java.tech.service.MerchantQueryFacade;
import org.springframework.stereotype.Service;

/**
 * created by Jason on 2021/8/29
 */
@Service
public class MerchantServiceImpl implements MerchantQueryFacade {
    @Override
    public String queryMerchant(String merchantId) {
        return "Query Merchant Info,merchant id:" + merchantId;
    }
}
