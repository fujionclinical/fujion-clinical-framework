package org.fujionclinical.hibernate.core;

import jakarta.annotation.PreDestroy;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Overrides close method to prevent anything other than the container from closing the data source.
 */
public class HibernateDataSource extends BasicDataSource {

    @PreDestroy
    public void destroy() throws Exception {
        super.close();
    }

    /**
     * Only IOC allowed to close a datasource.
     */
    @Override
    public void close() {
        // NOP
    }
}
