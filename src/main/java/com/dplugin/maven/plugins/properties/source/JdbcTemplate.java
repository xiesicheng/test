package com.dplugin.maven.plugins.properties.source;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 数据库表操作类 2016-07-23 22:45:46
 * @author nayuan
 */
public class JdbcTemplate {

    private DataSource dataSource;

    public JdbcTemplate(com.dplugin.maven.plugins.properties.model.DataSource source) throws Exception {
        Properties properties = new Properties();
        properties.put("url", source.getUrl());
        properties.put("username", source.getUsername());
        properties.put("password", source.getPassword());
        this.dataSource = DruidDataSourceFactory.createDataSource(properties);
    }

    public String getPackId(String sql, Object... args) throws SQLException {
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Connection connection = dataSource.getConnection();
        try{
            preparedStatement = connection.prepareStatement(sql);
            if(args != null && args.length > 0) {
                for(int i = 0; i < args.length; i++) {
                    preparedStatement.setObject(i+1, args[i]);
                }
            }

            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getString("id");
            }else{
                return null;
            }
        }finally {
            if(resultSet != null) {
                resultSet.close();
            }
            if(preparedStatement != null) {
                preparedStatement.close();
            }
            if(connection != null) {
                connection.close();
            }
        }
    }

    public List<Entry> queryForList(String sql, Object... args) throws SQLException {
        List<Entry> result = new ArrayList();

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Connection connection = dataSource.getConnection();
        try{
            preparedStatement = connection.prepareStatement(sql);
            if(args != null && args.length > 0) {
                for(int i = 0; i < args.length; i++) {
                    preparedStatement.setObject(i+1, args[i]);
                }
            }

            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                result.add(new Entry(
                        resultSet.getObject("id"),
                        resultSet.getString("title"),
                        resultSet.getString("key"),
                        resultSet.getString("value")
                ));
            }
        }finally {
            if(resultSet != null) {
                resultSet.close();
            }
            if(preparedStatement != null) {
                preparedStatement.close();
            }
            if(connection != null) {
                connection.close();
            }
        }
        return result;

    }


}
