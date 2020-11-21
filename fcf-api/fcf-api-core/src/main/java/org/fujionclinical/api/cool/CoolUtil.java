package org.fujionclinical.api.cool;

import edu.utah.kmm.model.cool.mediator.datasource.DataSource;
import edu.utah.kmm.model.cool.mediator.datasource.DataSources;
import org.apache.commons.lang3.StringUtils;

public class CoolUtil {

    private static CoolUtil instance = new CoolUtil();

    private DataSource defaultDataSource;

    private static CoolUtil create(String defaultDataSourceId) {
        instance.defaultDataSource = StringUtils.isBlank(defaultDataSourceId) ? null : DataSources.get(defaultDataSourceId);
        return instance;
    }

    public static DataSource getDefaultDataSource() {
        return instance.defaultDataSource;
    }

    private CoolUtil() {
    }
}
