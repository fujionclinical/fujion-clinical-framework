package org.fujionclinical.hibernate.core;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class Configurator {

    @Value("${org.fujionclinical.hibernate.url}")
    private String url;

    @Value("${org.fujionclinical.hibernate.password:}")
    private String username;

    @Value("${org.fujionclinical.hibernate.username:}")
    private String password;

    @Value("${org.fujionclinical.hibernate.connectionproperties:}")
    private String connectionProperties;

    @Value("${org.fujionclinical.hibernate.debug:false}")
    private boolean debug;

    @Value("${org.fujionclinical.hibernate.hbm2ddl.auto:update}")
    private String hbm2ddl_auto;

    public Configurator() {
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConnectionProperties() {
        return connectionProperties;
    }

    public String getHbm2ddlAuto() {
        return hbm2ddl_auto;
    }

    public boolean getDebug() {
        return debug;
    }

    @Bean("fcfHibernateSessionFactory")
    protected LocalSessionFactoryBean sessionFactory(
            @Qualifier("fcfHibernateDataSource") AbstractDataSource dataSource) {
        LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
        Properties props = new Properties();
        String debug = Boolean.toString(getDebug());
        props.setProperty("hibernate.dialect", dataSource.getDialect());
        props.setProperty("hibernate.hbm2ddl.auto", getHbm2ddlAuto());
        props.setProperty("hibernate.show_sql", debug);
        props.setProperty("hibernate.format_sql", debug);
        bean.setHibernateProperties(props);
        bean.setPackagesToScan("org.fujionclinical.hibernate");
        bean.setDataSource(dataSource);
        return bean;
    }

    @Bean("fcfHibernateTransactionManager")
    protected HibernateTransactionManager hibernateTransactionManager(
            @Qualifier("fcfHibernateSessionFactory") LocalSessionFactoryBean sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory.getObject());
        return transactionManager;
    }

    @Bean("fcfHibernatePersistenceExceptionTranslationPostProcessor")
    protected PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

}
