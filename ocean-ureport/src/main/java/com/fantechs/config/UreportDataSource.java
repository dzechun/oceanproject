package com.fantechs.config;

import com.bstek.ureport.definition.datasource.BuildinDatasource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by lfz on 2020/9/28.
 */
@Component
public class UreportDataSource implements BuildinDatasource {

    private static final String NAME = "MyDataSource";
    private Logger log = LoggerFactory.getLogger(UreportDataSource.class);

    @Autowired
    private DataSource dataSource;

    /**
     * 数据源名称
     **/
    @Override
    public String name() {
        return NAME;
    }

    /**
     * 获取连接
     **/
    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error("Ureport 数据源 获取连接失败！");
            e.printStackTrace();
        }
        return null;
    }
}
