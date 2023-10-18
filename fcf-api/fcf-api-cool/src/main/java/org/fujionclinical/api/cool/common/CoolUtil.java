/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2023 fujionclinical.org
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This Source Code Form is also subject to the terms of the Health-Related
 * Additional Disclaimer of Warranty and Limitation of Liability available at
 *
 *      http://www.fujionclinical.org/licensing/disclaimer
 *
 * #L%
 */
package org.fujionclinical.api.cool.common;

import org.apache.commons.lang3.StringUtils;
import org.coolmodel.core.complex.Attachment;
import org.coolmodel.core.complex.Period;
import org.coolmodel.core.complex.PeriodImpl;
import org.coolmodel.mediator.datasource.DataSource;
import org.coolmodel.mediator.datasource.DataSources;
import org.fujion.ancillary.MimeContent;
import org.fujion.common.DateRange;
import org.fujion.common.DateUtil;

public class CoolUtil {

    private static final CoolUtil instance = new CoolUtil();

    private volatile DataSource defaultDataSource;

    private String defaultDataSourceId;

    @SuppressWarnings("unused")
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

    public static MimeContent toMimeContent(Attachment attachment) {
        if (attachment == null) {
            return null;
        }

        MimeContent mimeContent =
                new MimeContent(attachment.hasContentType() ? attachment.getContentType().getCode() : null,
                        attachment.hasUrl() ? attachment.getUrl().toString() : null);
        mimeContent.setEncodedData(attachment.getContent());
        return mimeContent;
    }

    public static DateRange periodToDateRange(Period period) {
        return new DateRange(DateUtil.toDate(period.getStart()), DateUtil.toDate(period.getEnd()));
    }

    public static Period dateRangeToPeriod(DateRange dateRange) {
        return new PeriodImpl(DateUtil.toOffsetDateTime(dateRange.getStartDate()), DateUtil.toOffsetDateTime(dateRange.getEndDate()));
    }

    private CoolUtil() {
    }

}
