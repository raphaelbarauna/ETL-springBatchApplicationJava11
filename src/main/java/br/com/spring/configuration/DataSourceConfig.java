package br.com.spring.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


@Configuration
public class DataSourceConfig {

    @Value ("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value ("${spring.datasource.url}")
    private String url;

    @Value ("${spring.datasource.username}")
    private String username;

    @Value ("${spring.datasource.password}")
    private String password;

    @Bean(name = "springDataSource")
    public DataSource springDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(driverClassName);
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }

}
