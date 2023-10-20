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
package org.fujionclinical.hibernate.h2;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.EnumUtils;
import org.fujionclinical.hibernate.core.HibernateDataSource;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;

/**
 * H2-based data source that also handles starting the database in the appropriate mode.
 */
public class H2DataSource extends HibernateDataSource {

    /**
     * Server modes.
     */
    public enum DBMode {
        EMBEDDED, // H2 embedded mode
        REMOTE, // H2 remote server
        LOCAL // H2 local server
    }

    private Server server;

    @Value("${org.fujionclinical.hibernate.h2.mode:embedded}")
    private String dbModeStr;

    private DBMode dbMode;

    /**
     * If running H2 in local mode, starts the server.
     *
     * @throws Exception Unspecified exception
     */
    @PostConstruct
    public void init() throws Exception {
        dbMode = EnumUtils.getEnumIgnoreCase(DBMode.class, dbModeStr);

        if (dbMode == DBMode.LOCAL) {
            String port = getPort();

            if (port.isEmpty()) {
                server = Server.createTcpServer();
            } else {
                server = Server.createTcpServer("-tcpPort", port);
            }

            server.start();
        }
    }

    @Override
    public void destroy() throws Exception {
        if (server != null) {
            server.stop();
        }

        super.destroy();
    }

    /**
     * Extract the TCP port from the connection URL.
     *
     * @return The TCP port, or empty string if none.
     */
    private String getPort() {
        String url = getUrl();
        int i = url.indexOf("://") + 3;
        int j = url.indexOf("/", i);
        String s = i == 2 || j == -1 ? "" : url.substring(i, j);
        i = s.indexOf(":");
        return i == -1 ? "" : s.substring(i + 1);
    }

    public DBMode getMode() {
        return dbMode;
    }

}
