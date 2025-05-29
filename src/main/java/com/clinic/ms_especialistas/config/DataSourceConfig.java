package com.clinic.ms_especialistas.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Value("${db.mssql.host}")
    private String MSSQL_HOST;
    @Value("${db.mssql.database}")
    private String MSSQL_DATABASE;
    @Value("${db.mssql.user}")
    private String MSSQL_USER;
    @Value("${db.mssql.password}")
    private String MSSQL_PASSWORD;

    @Bean
    public DataSource dataSource() throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl(String.format("jdbc:sqlserver://%s;databaseName=%s;trustServerCertificate=true", MSSQL_HOST , MSSQL_DATABASE));
        dataSource.setUser(MSSQL_USER);
        dataSource.setPassword(MSSQL_PASSWORD);
        dataSource.setDriverClass("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setMinPoolSize(5);
        dataSource.setInitialPoolSize(5);
        dataSource.setMaxIdleTime(30);
        dataSource.setMaxIdleTimeExcessConnections(20);
        dataSource.setMaxPoolSize(100);
        dataSource.setAcquireIncrement(5);
        dataSource.setLoginTimeout(180);
        return dataSource;
    }
}
