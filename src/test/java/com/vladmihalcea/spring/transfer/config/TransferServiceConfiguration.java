package com.vladmihalcea.spring.transfer.config;

import com.vladmihalcea.spring.util.DataSourceProvider;
import com.vladmihalcea.spring.util.Database;
import com.vladmihalcea.spring.util.logging.InlineQueryLogEntryCreator;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Properties;

/**
 *
 * @author Vlad Mihalcea
 */
@Configuration
@PropertySource("application.properties")
@ComponentScan(
    basePackages = {
        "com.vladmihalcea.spring.transfer",
    }
)
@EnableJpaRepositories("com.vladmihalcea.spring.transfer.repository")
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class TransferServiceConfiguration {

    public static final String DATA_SOURCE_PROXY_NAME = "DATA_SOURCE_PROXY";

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int maxConnections;

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public Database database() {
        return Database.POSTGRESQL;
    }

    @Bean
    public DataSourceProvider dataSourceProvider() {
        return database().dataSourceProvider();
    }

    @Bean(destroyMethod = "close")
    public HikariDataSource poolingDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setMaximumPoolSize(maxConnections);
        hikariConfig.setDataSource(dataSourceProvider().dataSource());
        hikariConfig.setAutoCommit(false);
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public DataSource dataSource(DataSource poolingDataSource) {
        SLF4JQueryLoggingListener loggingListener = new SLF4JQueryLoggingListener();
        loggingListener.setQueryLogEntryCreator(new InlineQueryLogEntryCreator());
        DataSource dataSource = ProxyDataSourceBuilder
                .create(poolingDataSource)
                .name(DATA_SOURCE_PROXY_NAME)
                .listener(loggingListener)
                .build();
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setPersistenceUnitName(getClass().getSimpleName());
        entityManagerFactoryBean.setPersistenceProvider(new HibernatePersistenceProvider());
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan(packagesToScan());

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactoryBean.setJpaProperties(additionalProperties());
        return entityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    public TransactionTemplate transactionTemplate(EntityManagerFactory entityManagerFactory) {
        return new TransactionTemplate(transactionManager(entityManagerFactory));
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    protected Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", dataSourceProvider().hibernateDialect());
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty(AvailableSettings.IN_CLAUSE_PARAMETER_PADDING, "true");
        return properties;
    }

    protected String[] packagesToScan() {
        return new String[]{
            "com.vladmihalcea.spring.transfer.domain"
        };
    }
}
