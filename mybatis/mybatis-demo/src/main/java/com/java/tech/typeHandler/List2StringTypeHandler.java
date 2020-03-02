package com.java.tech.typeHandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * created by Jason on 2020/2/29
 */
public class List2StringTypeHandler implements TypeHandler<List<String>> {
    private Logger log = LoggerFactory.getLogger(List2StringTypeHandler.class);

    @Override
    public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        log.info("使用自定义TypeHandler setParameter");
        StringBuffer sb = new StringBuffer();
        for (String param : parameter) {
            sb.append(param).append(",");
        }
        ps.setString(i, sb.toString().substring(0, sb.toString().length() - 1));
    }

    @Override
    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
        String[] arrs = rs.getString(columnName).split(",");
        return Arrays.asList(arrs);
    }

    @Override
    public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        String[] arrs = rs.getString(columnIndex).split(",");
        return Arrays.asList(arrs);
    }

    @Override
    public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String[] arrs = cs.getString(columnIndex).split(",");
        return Arrays.asList(arrs);
    }
}
