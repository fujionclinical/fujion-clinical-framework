package org.fujionclinical.hibernate.core;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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

    @Bean
    protected HibernateTransactionManager hibernateTransactionManager(@Qualifier("fcfHibernateSessionFactory") SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory.getObject());
        return transactionManager;
    }

    @Bean
    protected PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

}
