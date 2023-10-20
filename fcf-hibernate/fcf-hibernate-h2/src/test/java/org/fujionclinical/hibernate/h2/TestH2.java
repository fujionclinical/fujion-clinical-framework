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

import org.apache.commons.io.file.PathUtils;
import org.fujionclinical.api.test.TestUtil;
import org.fujionclinical.hibernate.core.Configurator;
import org.fujionclinical.hibernate.h2.H2DataSource.DBMode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TestH2 {

    private static Path databaseFile;

    private static String database;

    private static final Configurator config = new Configurator();

    private static Map<String, Object> savedConfigState;

    @BeforeClass
    public static void before() throws Exception {
        databaseFile = Files.createTempDirectory("h2-test");
        database = databaseFile.toFile().getAbsolutePath();
        TestUtil.initValueAnnotatedFields(config);
        savedConfigState = TestUtil.getFieldValues(config);
    }

    @AfterClass
    public static void after() throws Exception {
        PathUtils.deleteDirectory(databaseFile);
    }

    @Test
    public void test() throws Exception {
        Map<String, Object> params = new HashMap<>();

        params.put("url", "jdbc:h2:" + database);
        testDB(params, DBMode.EMBEDDED, null);

        params.put("url", "jdbc:h2:tcp://localhost/" + database);
        testDB(params, DBMode.LOCAL, null);
        testDB(params, DBMode.REMOTE, "Connection refused");

        params.put("url", "jdbc:h2:tcp://localhost:1234/" + database);
        testDB(params, DBMode.REMOTE, "Connection refused");
        params.put("url", "jdbc:h2:tcp://localhost/" + database);
        params.put("username", "username");
        params.put("password", "password");
        testDB(params, DBMode.LOCAL, "Wrong user name or password");
    }

    private void testDB(Map<String, Object> params, DBMode dbMode, String expectedException) throws Exception {
        TestUtil.setFields(config, savedConfigState);
        TestUtil.setFields(config, params);
        H2DataSource h2 = new H2DataSource();
        ReflectionTestUtils.setField(h2, "dbModeStr", dbMode.toString());
        h2.init();
        String error = null;

        try (H2DataSource ds = h2;
             Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT X FROM SYSTEM_RANGE(1, 9);");
             ResultSet rs = ps.executeQuery()) {
            int i = 0;

            while (rs.next()) {
                i++;
                assertEquals(i, rs.getInt("X"));
            }

            assertEquals(i, 9);
            assertEquals(dbMode, ds.getMode());
        } catch (AssertionError e) {
            throw e;
        } catch (Exception e) {
            error = e.getMessage();
        }

        h2.destroy();

        if (expectedException == null) {
            assertNull(error);
        } else {
            assertNotNull(error);
            assertTrue(error, error.contains(expectedException));
        }
    }
}
