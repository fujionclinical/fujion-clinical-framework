/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2020 fujionclinical.org
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
package org.fujionclinical.api;

import org.fujionclinical.api.alias.AliasTypes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AliasRegistryTest {
    
    @Test
    public void test() {
        AliasTypes reg = AliasTypes.getInstance();
        reg.get("AUTHORITY").register("auth1", "auth.alias1");
        reg.get("AUTHORITY").register("auth2", "auth.alias2");
        AliasTypes.register("AUTHORITY", "auth3", "auth.alias3");
        reg.get("AUTHORITY").register("authx*", "auth.aliasx*");
        reg.get("AUTHORITY").register("authy.*.abc.*", "authz.*.xyz.*");
        reg.get("AUTHORITY").register("authy.?.def.*", "authz.?.xyz.*");
        
        reg.get("PROPERTY").register("prop1", "prop.alias1");
        reg.get("PROPERTY").register("prop2", "prop.alias2");
        AliasTypes.register("PROPERTY", "prop3", "prop.alias3");
        reg.get("PROPERTY").register("propx*", "prop.aliasx*");
        reg.get("PROPERTY").register("propy.*.abc.*", "propz.*.xyz.*");
        reg.get("PROPERTY").register("propy.?.def.*", "propz.?.xyz.*");
        
        assertEquals("auth.alias1", reg.get("AUTHORITY").get("auth1"));
        assertEquals("auth.alias2", reg.get("AUTHORITY").get("auth2"));
        assertEquals("auth.alias3", reg.get("AUTHORITY").get("auth3"));
        assertEquals("auth.aliasx.test", reg.get("AUTHORITY").get("authx.test"));
        assertEquals("authz.123.xyz.456", reg.get("AUTHORITY").get("authy.123.abc.456"));
        assertEquals("authz.9.xyz.789", reg.get("AUTHORITY").get("authy.9.def.789"));
        assertNull(reg.get("AUTHORITY").get("authz.5.ghi.987"));
        
        assertEquals("prop.alias1", reg.get("PROPERTY").get("prop1"));
        assertEquals("prop.alias2", reg.get("PROPERTY").get("prop2"));
        assertEquals("prop.alias3", reg.get("PROPERTY").get("prop3"));
        assertEquals("prop.aliasx.test", reg.get("PROPERTY").get("propx.test"));
        assertEquals("propz.123.xyz.456", reg.get("PROPERTY").get("propy.123.abc.456"));
        assertEquals("propz.9.xyz.789", reg.get("PROPERTY").get("propy.9.def.789"));
        assertNull(reg.get("PROPERTY").get("prop.test.property"));
        
        reg.get("AUTHORITY").register("auth1", "auth.new.alias1");
    }
    
}
