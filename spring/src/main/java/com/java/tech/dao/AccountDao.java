package com.java.tech.dao;

/**
 * created by Jason on 2020/3/7
 */
public interface AccountDao {

    /**
     * 转出
     *
     * @param sender 转出账户
     * @param money  转出金额
     */
    void cashOut(String sender, Double money);

    void cashIn(String receiver, Double money);

    /**
     * 开户
     *
     * @param account 户名
     * @param money   存款
     */
    void addAccount(String account, Double money);

}
