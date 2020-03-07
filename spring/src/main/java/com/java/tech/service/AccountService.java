package com.java.tech.service;

import com.java.tech.model.request.AccountRequest;
import com.java.tech.model.response.AccountResponse;

/**
 * created by Jason on 2020/3/6
 */
public interface AccountService {

    AccountResponse transfer(AccountRequest request);

    AccountResponse create(AccountRequest addRequest);
}
