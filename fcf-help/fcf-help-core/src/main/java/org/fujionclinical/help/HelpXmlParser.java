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
package org.fujionclinical.help;

import org.fujionclinical.api.spring.BaseXmlParser;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

/**
 * Spring xml configuration file parser extension. Supports the definition of help modules within
 * the configuration file in a much more abbreviated fashion than would be required without the
 * extension.
 */
public class HelpXmlParser extends BaseXmlParser {
    
    @Override
    protected Class<?> getBeanClass(Element element) {
        return HelpModule.class;
    }
    
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        addProperties(element, builder);
    }
    
    /**
     * Parses a help definition from an xml string.
     * 
     * @param xml XML containing help definition.
     * @return A help definition instance.
     * @throws Exception Unspecified exception.
     */
    public static HelpModule fromXml(String xml) throws Exception {
        return (HelpModule) new HelpXmlParser().fromXml(xml, "help");
    }
    
}
