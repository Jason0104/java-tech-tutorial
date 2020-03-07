package com.java.tech.serviceTest;

import com.java.tech.AbstractSpringContextTest;
import com.java.tech.model.User;
import com.java.tech.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * created by Jason on 2020/3/5
 */
public class TransactionTemplateServiceTest extends AbstractSpringContextTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    public void testTransaction() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                String sql = "select * from cu_user where id=1";
                userService.queryUser(sql);
            }
        });

        transactionTemplate.execute(new TransactionCallback<List<User>>() {

            @Override
            public List<User> doInTransaction(TransactionStatus status) {
                String sql = "select * from cu_user";
                return userService.queryList(sql);
            }
        });
    }
}
