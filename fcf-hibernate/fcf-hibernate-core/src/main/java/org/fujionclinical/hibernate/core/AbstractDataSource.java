package org.fujionclinical.hibernate.core;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDataSource extends BasicDataSource {

    @Autowired
    private Configurator configurator;

    private final String dialect;

    protected AbstractDataSource(String driverClassName, String dialect) {
        setDriverClassName(driverClassName);
        this.dialect = dialect;
    }

    @PostConstruct
    public void init() throws Exception {
        setUrl(configurator.getUrl());
        setUsername(configurator.getUsername());
        setPassword(configurator.getPassword());

        if (StringUtils.isNotBlank(configurator.getConnectionProperties())) {
            setConnectionProperties(configurator.getConnectionProperties());
        }
    }

    @PreDestroy
    public void destroy() throws Exception {
        close();
    }

    public String getDialect() {
        return dialect;
    }
}
