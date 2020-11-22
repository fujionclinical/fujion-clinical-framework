package org.fujionclinical.api.cool;

import edu.utah.kmm.model.cool.mediator.datasource.DataSource;
import edu.utah.kmm.model.cool.mediator.datasource.DataSources;
import org.apache.commons.lang3.StringUtils;

public class CoolUtil {

    private static CoolUtil instance = new CoolUtil();

    private volatile DataSource defaultDataSource;

    private String defaultDataSourceId;

    private static CoolUtil create(String defaultDataSourceId) {
        instance.defaultDataSourceId = defaultDataSourceId;
        return instance;
    }

    public static DataSource getDefaultDataSource() {
        return instance._getDefaultDataSource();
    }

    public DataSource _getDefaultDataSource() {
        return defaultDataSource == null ? _initDataSource() : defaultDataSource;
    }

    private synchronized DataSource _initDataSource() {
        if (defaultDataSource == null) {
            defaultDataSource = StringUtils.isBlank(defaultDataSourceId) ? null : DataSources.get(defaultDataSourceId);
        }

        return defaultDataSource;
    }

    private CoolUtil() {
    }
}
