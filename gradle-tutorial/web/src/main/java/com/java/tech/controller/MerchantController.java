package com.java.tech.controller;

import com.java.tech.service.MerchantQueryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * created by Jason on 2021/8/29
 */
@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    private MerchantQueryFacade merchantQueryFacade;

    @RequestMapping(value = "/{merchantId}",method = RequestMethod.GET)
    public String getMerchant(@PathVariable("merchantId") String merchantId) {
        String merchantInfo = merchantQueryFacade.queryMerchant(merchantId);
        return merchantInfo;
    }
}
