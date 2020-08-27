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
package org.fujionclinical.plugin.encounters;

import org.fujion.component.*;
import org.fujion.event.DblclickEvent;
import org.fujionclinical.api.event.IEventSubscriber;
import org.fujionclinical.api.model.core.IReference;
import org.fujionclinical.api.model.encounter.EncounterContext;
import org.fujionclinical.api.model.encounter.IEncounter;
import org.fujionclinical.api.model.location.ILocation;
import org.fujionclinical.api.model.person.IPersonName;
import org.fujionclinical.api.model.person.IPersonNameType;
import org.fujionclinical.sharedforms.controller.ResourceListView;
import org.fujionclinical.shell.elements.ElementPlugin;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller for patient encounters display.
 */
public class MainController extends ResourceListView<IEncounter, IEncounter> {

    private IEncounter lastEncounter;

    private final IEventSubscriber<IEncounter> encounterChangeListener = (eventName, encounter) -> setEncounter(encounter);

    @Override
    protected void setup() {
        setup(IEncounter.class, "Encounters", "Encounter Detail", "patient={{patient}}", 1, "", "Date", "EncounterStatus", "Location", "Providers");
        columns.getFirstChild(Column.class).setStyles("width: 1%; min-width: 40px");
    }

    @Override
    protected void populate(
            IEncounter encounter,
            List<Object> columns) {
        columns.add(" ");
        columns.add(encounter.getPeriod());
        columns.add(encounter.getStatus());
        columns.add(getLocations(encounter));
        columns.add(getParticipants(encounter));
    }

    private List<ILocation> getLocations(IEncounter encounter) {
        return encounter.getLocations().stream()
                .map(IReference::getReferenced)
                .collect(Collectors.toList());
    }

    private List<IPersonName> getParticipants(IEncounter encounter) {
        return encounter.getParticipants().stream()
                .map(IReference::getReferenced)
                .map(IPersonNameType::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    protected void initModel(List<IEncounter> entries) {
        model.addAll(entries);
    }

    @Override
    protected void renderRow(
            Row row,
            IEncounter encounter) {
        super.renderRow(row, encounter);

        row.addEventListener(DblclickEvent.class, event ->
                EncounterContext.changeEncounter(encounter));
    }

    @Override
    public void onLoad(ElementPlugin plugin) {
        super.onLoad(plugin);
        EncounterContext.getEncounterContext().addListener(encounterChangeListener);
        lastEncounter = EncounterContext.getActiveEncounter();
    }

    @Override
    public void onUnload() {
        super.onUnload();
        EncounterContext.getEncounterContext().removeListener(encounterChangeListener);
    }

    @Override
    protected void renderData() {
        super.renderData();
        updateRowStatus(lastEncounter, true);
    }

    private void setEncounter(IEncounter encounter) {
        updateRowStatus(lastEncounter, false);
        updateRowStatus(encounter, true);
        lastEncounter = encounter;
    }

    private void updateRowStatus(
            IEncounter encounter,
            boolean activeContext) {
        Row row = encounter == null ? null : (Row) rows.findChild(child -> encounter.isSame((IEncounter) child.getData()));

        if (row != null) {
            Rowcell cell = row.getFirstChild(Rowcell.class);
            BaseUIComponent flag;

            if (cell.hasChildren()) {
                flag = (BaseUIComponent) cell.getFirstChild();
            } else {
                cell.addChild(flag = new Div());
            }

            flag.setClasses(activeContext ? "fa fa-check" : "-fa -fa-check");
        }
    }

}
