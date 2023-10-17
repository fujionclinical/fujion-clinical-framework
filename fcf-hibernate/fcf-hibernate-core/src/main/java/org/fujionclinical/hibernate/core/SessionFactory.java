package org.fujionclinical.hibernate.core;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class SessionFactory extends LocalSessionFactoryBean {

    @Autowired
    @Qualifier("fcfHibernateDataSource")
    private AbstractDataSource dataSource;

    @Autowired
    @Qualifier("fcfHibernateConfigurator")
    private Configurator configurator;

    @PostConstruct
    private void init() {
        Properties props = new Properties();
        String debug = Boolean.toString(configurator.getDebug());
        props.setProperty("hibernate.dialect", dataSource.getDialect());
        props.setProperty("hibernate.current_session_context_class", "thread");
        props.setProperty("hibernate.hbm2ddl.auto", configurator.getHbm2ddlAuto());
        props.setProperty("hibernate.show_sql", debug);
        props.setProperty("hibernate.format_sql", debug);
        setHibernateProperties(props);
        setPackagesToScan("org.fujionclinical.hibernate");
        setDataSource(dataSource);
    }
}
