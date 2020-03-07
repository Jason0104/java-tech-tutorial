package com.java.tech.impl;

import com.java.tech.dao.AccountDao;
import com.java.tech.dao.UserDao;
import com.java.tech.model.User;
import com.java.tech.model.request.AccountRequest;
import com.java.tech.model.response.AccountResponse;
import com.java.tech.service.AccountService;
import com.java.tech.service.ServiceCallBack;
import com.java.tech.service.ServiceTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * created by Jason on 2020/3/6
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public AccountResponse transfer(AccountRequest account) {
        return ServiceTemplate.execute(account, new ServiceCallBack<AccountRequest, AccountResponse>() {
            @Override
            public void checkParameter(AccountRequest request) {
                System.out.println("开始进入账户校验");
            }

            @Override
            public AccountResponse process(AccountRequest request) {
                System.out.println("开始进入转账流程");

                //使用了事务管理
                //将转账操作放入到transactionTemplate中执行
                transactionTemplate.execute(new TransactionCallback<AccountResponse>() {
                    @Override
                    public AccountResponse doInTransaction(TransactionStatus status) {
                        //转出
                        accountDao.cashOut(account.getAccount().getSender(), account.getAccount().getAmount());

                        //故意制造异常,转出成功 转入失败 就会回滚 转出金额不变
                        int i = 1 / 0;
                        //转入
                        accountDao.cashIn(account.getAccount().getReceiver(), account.getAccount().getAmount());
                        return AccountResponse.builder().success(true).build();
                    }
                });
                return null;


                /**没有使用事务管理,会出现账户数据不对**/
                /**
                 *                 accountDao.cashOut(account.getAccount().getSender(), account.getAccount().getAmount());
                 *                 //故意制造异常,会导致金额已经转出 但是没有转入到收款方
                 *                 int i = 1 / 0;
                 *                 //转入
                 *                 accountDao.cashIn(account.getAccount().getReceiver(), account.getAccount().getAmount());
                 *
                 *                 //如果返回true代表成功
                 *                 return AccountResponse.builder().success(true).build();
                 *
                 */
            }

            @Override
            public AccountResponse fillFailedResult(AccountRequest request, String message) {
                //转账失败的业务处理 回滚操作
                System.out.println("转账失败 进入回滚,异常信息为:" + message);
                return null;
            }

            @Override
            public void afterProcess() {

            }
        });

    }

    @Override
    public AccountResponse create(AccountRequest addRequest) {
        return ServiceTemplate.execute(addRequest, new ServiceCallBack<AccountRequest, AccountResponse>() {
            @Override
            public void checkParameter(AccountRequest request) {
                System.out.println("开始进入创建账户校验");
            }

            @Override
            public AccountResponse process(AccountRequest request) throws Exception {
                transactionTemplate.execute(new TransactionCallback<AccountResponse>() {

                    @Override
                    public AccountResponse doInTransaction(TransactionStatus status) {
                        //每次创建账户的时候 往用户表中插入一条记录
                        accountDao.addAccount(request.getAccount().getSender(), request.getAccount().getAmount());
                        //故意制造异常
                        int i = 2 / 0;
                        userDao.addUser(buildUserRequest(request));
                        return AccountResponse.builder().success(true).build();
                    }
                });
                return null;
            }

            private User buildUserRequest(AccountRequest request) {
                return User.builder()
                        .username(request.getAccount().getSender()).mobile("18701795780").address("beijing").isDeleted(0).build();

            }

            @Override
            public AccountResponse fillFailedResult(AccountRequest request, String message) {
                return null;
            }

            @Override
            public void afterProcess() {

            }
        });
    }
}
