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
package org.fujionclinical.plugin.conditions;

import org.fujionclinical.api.model.condition.ICondition;
import org.fujionclinical.sharedforms.controller.ResourceListView;

import java.util.List;

/**
 * Controller for patient conditions display.
 */
public class MainController extends ResourceListView<ICondition, ICondition> {
    
    @Override
    protected void setup() {
        setup(ICondition.class, "Conditions", "Condition Detail", "patient={{patient}}", 1, "Condition", "Onset", "EncounterStatus",
                "Notes");
    }
    
    @Override
    protected void populate(ICondition condition, List<Object> columns) {
        columns.add(condition.getCondition());
        columns.add(condition.getOnset());
        columns.add(condition.getClinicalStatus());
        columns.add(condition.getAnnotations());
    }
    
    @Override
    protected void initModel(List<ICondition> entries) {
        model.addAll(entries);
    }
    
}