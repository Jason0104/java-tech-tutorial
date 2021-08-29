package com.java.tech;

import com.java.tech.service.MerchantQueryFacade;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * created by Jason on 2021/8/29
 */
public class MerchantQueryFacadeTest extends AbstractSpringTest{

    @Autowired
    private MerchantQueryFacade merchantQueryFacade;

    @Test
    public void testQueryMerchant() {
        String merchant = merchantQueryFacade.queryMerchant("20210829120200090000");
        System.out.println(merchant);
    }
}
