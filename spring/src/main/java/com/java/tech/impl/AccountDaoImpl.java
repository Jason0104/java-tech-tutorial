package com.java.tech.impl;

import com.java.tech.dao.AccountDao;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * created by Jason on 2020/3/7
 */
@Service
public class AccountDaoImpl implements AccountDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void cashOut(String sender, Double money) {
        String cashOut = "update cu_account set money=money-? where name=?";
        this.jdbcTemplate.update(cashOut, money, sender);
    }

    @Override
    public void cashIn(String receiver, Double money) {
        String cashIn = "update cu_account set money=money+? where name=?";
        this.jdbcTemplate.update(cashIn, money, receiver);
    }

    @Override
    public void addAccount(String account, Double money) {
        String insertSql = "insert into cu_account(name,money) values(?,?)";
        this.jdbcTemplate.update(insertSql, account, money);
    }
}
