package com.java.tech.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * created by Jason on 2020/3/4
 */
@Service
public class LogService {

    private Logger log = LoggerFactory.getLogger(LogService.class);
    public Random random = new Random(System.currentTimeMillis());

    public int log(String param) {
        log.info("LogService: log invoked,param:{}", param);
        return random.nextInt();
    }

    public void getExceptionMethod() throws Exception {
        log.info("LogService: getExceptionMethod invoked");
        throw new Exception("method execute error");
    }
}
