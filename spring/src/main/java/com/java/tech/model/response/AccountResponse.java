package com.java.tech.model.response;

import lombok.Builder;
import lombok.Data;

/**
 * created by Jason on 2020/3/7
 */
@Data
@Builder
public class AccountResponse extends BaseResult {

    private boolean success;
}
