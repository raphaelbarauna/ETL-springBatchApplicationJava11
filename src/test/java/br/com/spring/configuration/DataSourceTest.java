package br.com.spring.configuration;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceTest {

    @Bean
    public DataSource springDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.h2.Driver");
        dataSourceBuilder.url("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
        dataSourceBuilder.username("sa");
        dataSourceBuilder.password("sa");
        return dataSourceBuilder.build();
    }

}
