package com.lvbby.codema.app.mybatis;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by peng on 16/2/2.
 */
@Configuration
@MapperScan("${config.destPackage}")
@EnableTransactionManagement
//@PropertySource("application.properties")
public class DalConfig {

    @Value(value = "classpath:${config.mapperDir}/*.xml")
    Resource[] resources;
    @Value(value = "classpath:mybatis.xml")
    Resource mybatisConfig;


    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
        ssfb.setTypeAliasesPackage("${config.fromPackage}");
        ssfb.setDataSource(dataSource);
        ssfb.setMapperLocations(resources);
        ssfb.setConfigLocation(mybatisConfig);
        return ssfb;
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public DruidDataSource dataSource() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/lvbby?characterEncoding=UTF-8";
        String username = "root";
        String password = "";
        return createDataSource(url, username, password);
    }

    DruidDataSource createDataSource(String url, String username, String password) throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setMaxActive(60);
        druidDataSource.setInitialSize(1);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setMinIdle(1);
        druidDataSource.setTimeBetweenEvictionRunsMillis(3000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        druidDataSource.setValidationQuery("select 1");
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        Properties properties = new Properties();
        properties.put("config.decrypt", "true");
        druidDataSource.setConnectProperties(properties);

        StatFilter statFilter = new StatFilter();
        statFilter.setSlowSqlMillis(10000);
        statFilter.setMergeSql(true);
        statFilter.setLogSlowSql(true);

        List<Filter> re = new ArrayList<Filter>();
        re.add(statFilter);
        druidDataSource.setProxyFilters(re);

        return druidDataSource;
    }


}
