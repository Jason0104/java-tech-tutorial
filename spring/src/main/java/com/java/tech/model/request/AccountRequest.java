package com.java.tech.model.request;


import com.java.tech.model.Account;
import lombok.Builder;
import lombok.Data;

/**
 * created by Jason on 2020/3/7
 */
@Data
@Builder
public class AccountRequest extends BaseRequest {

    private Account account;
}
