package com.java.tech.serviceTest;

import com.java.tech.AbstractSpringContextTest;
import com.java.tech.model.Account;
import com.java.tech.model.request.AccountRequest;
import com.java.tech.model.response.AccountResponse;
import com.java.tech.service.AccountService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * created by Jason on 2020/3/7
 */
public class AccountServiceTest extends AbstractSpringContextTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void testTransfer() {
        //转出方 张三转账500给李四
        Account transfer = Account.builder().sender("张三").receiver("李四").amount(500.00).build();
        //构建转账请求
        AccountRequest transferRequest = AccountRequest.builder().account(transfer).build();

        AccountResponse accountResponse = accountService.transfer(transferRequest);
        System.out.println(accountResponse);
    }

    @Test
    public void testCreateAccount() {
        Account newAccount = Account.builder().sender("mandy").amount(7500.00).build();
        AccountRequest accountRequest = AccountRequest.builder().account(newAccount).build();
        accountService.create(accountRequest);
    }
}
