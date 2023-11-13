package com.example.config;


import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Configuration
// 开启Spring事务
@EnableTransactionManagement
public class AppConfig {
    /**
     * 事务定义
     * 这里都使用默认的，而且是新创建出来的！当然也可以使用已经存在的
     *
     * @return
     */

    @Bean
    public TransactionDefinition transactionDefinition() {
        return new DefaultTransactionDefinition();
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource);
        return platformTransactionManager;
    }

}
