/*
 * #%L
 * Fujion Clinical Framework
 * %%
 * Copyright (C) 2018 fujionclinical.org
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
package org.fujionclinical.shell.designer;

import org.fujionclinical.shell.property.PropertyInfo;
import org.fujion.component.Doublebox;

/**
 * Editor for double values.
 */
public class PropertyEditorDouble extends PropertyEditorBase<Doublebox> {

    public PropertyEditorDouble() {
        super(new Doublebox());
    }

    @Override
    protected void init(Object target, PropertyInfo propInfo, PropertyGrid propGrid) {
        super.init(target, propInfo, propGrid);
        editor.setMinValue(propInfo.getConfigValueDouble("min", null));
        editor.setMaxValue(propInfo.getConfigValueDouble("max", null));
    }

    @Override
    protected String getValue() {
        return Double.toString(editor.getValue());
    }

    @Override
    protected void setValue(Object value) {
        editor.setValue((Double) value);
        updateValue();
    }

}
