package org.fujionclinical.hibernate.core;

import edu.utah.kmm.common.utils.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

import static org.fujionclinical.api.spring.Constants.PROFILE_ROOT;

@Configuration
@EnableTransactionManagement
public class Configurator {

    @Value("${org.fujionclinical.hibernate.url}")
    private String url;

    @Value("${org.fujionclinical.hibernate.username:}")
    private String username;

    @Value("${org.fujionclinical.hibernate.password:}")
    private String password;

    @Value("${org.fujionclinical.hibernate.connectionproperties:}")
    private String connectionProperties;

    @Value("${org.fujionclinical.hibernate.debug:false}")
    private boolean debug;

    @Value("${org.fujionclinical.hibernate.hbm2ddl.auto:update}")
    private String hbm2ddl_auto;

    @Value("${org.fujionclinical.hibernate.datasource:}")
    private String dataSourceClass;

    @Value("${org.fujionclinical.hibernate.driver}")
    private String driverClass;

    @Value("${org.fujionclinical.hibernate.dialect:#{null}}")
    private String dialect;

    @Bean("fcfHibernateSessionFactory")
    @Profile(PROFILE_ROOT)
    protected LocalSessionFactoryBean sessionFactory(
            @Autowired @Qualifier("fcfHibernateDataSource") HibernateDataSource dataSource) {
        LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
        Properties props = new Properties();
        String debugStr = Boolean.toString(debug);
        props.setProperty("hibernate.dialect", dialect);
        props.setProperty("hibernate.hbm2ddl.auto", hbm2ddl_auto);
        props.setProperty("hibernate.show_sql", debugStr);
        props.setProperty("hibernate.format_sql", debugStr);
        props.setProperty("hibernate.globally_quoted_identifiers", "true");
        bean.setHibernateProperties(props);
        bean.setPackagesToScan("org.fujionclinical.hibernate");
        bean.setDataSource(dataSource);
        return bean;
    }

    @Bean("fcfHibernateTransactionManager")
    @Profile(PROFILE_ROOT)
    protected HibernateTransactionManager hibernateTransactionManager(
            @Qualifier("fcfHibernateSessionFactory") LocalSessionFactoryBean sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory.getObject());
        return transactionManager;
    }

    @Bean("fcfHibernatePersistenceExceptionTranslationPostProcessor")
    @Profile(PROFILE_ROOT)
    protected PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean("fcfHibernateDataSource")
    @Profile(PROFILE_ROOT)
    protected HibernateDataSource dataSource() throws Exception {
        HibernateDataSource dataSource;

        if (StringUtils.isBlank(dataSourceClass)) {
            dataSource = new HibernateDataSource();
        } else {
            dataSource = (HibernateDataSource) ClassUtils.newInstance(Class.forName(dataSourceClass));
        }

        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        if (StringUtils.isNotBlank(connectionProperties)) {
            dataSource.setConnectionProperties(connectionProperties);
        }

        return dataSource;
    }

}
