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
package org.fujionclinical.plugin.patientheader;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujion.annotation.EventHandler;
import org.fujion.annotation.WiredComponent;
import org.fujion.common.DateUtil;
import org.fujion.component.*;
import org.fujionclinical.api.event.IEventSubscriber;
import org.fujionclinical.api.model.IIdentifier;
import org.fujionclinical.api.model.person.IPersonName;
import org.fujionclinical.api.model.Identifier;
import org.fujionclinical.api.model.user.IUser;
import org.fujionclinical.api.patient.IPatient;
import org.fujionclinical.api.patient.PatientContext;
import org.fujionclinical.api.security.SecurityUtil;
import org.fujionclinical.shell.elements.ElementPlugin;
import org.fujionclinical.shell.plugins.PluginController;
import org.fujionclinical.ui.patientselection.common.PatientSelection;

import java.util.Date;

/**
 * Controller for patient header plugin.
 */
public class PatientHeader extends PluginController {

    private static final Log log = LogFactory.getLog(PatientHeader.class);

    @WiredComponent
    private Toolbar tbPatient;

    @WiredComponent
    private Button btnDetail;

    @WiredComponent
    private Popup popDetail;

    @WiredComponent(value = "popDetail.pnlDetail")
    private Paneview pnlDetail;

    @WiredComponent
    private Label lblName;

    @WiredComponent
    private Label lblGender;

    @WiredComponent
    private Label lblDOBLabel;

    @WiredComponent
    private Label lblDOB;

    @WiredComponent
    private Label lblDODLabel;

    @WiredComponent
    private Label lblDOD;

    @WiredComponent
    private Label lblUser;

    private String noSelection;

    private IPatient patient;

    private String patientName;

    private boolean needsDetail = true;
    private final IEventSubscriber<IPatient> patientChangeListener = (event, patient) -> setPatient(patient);
    private boolean showUser = true;

    @Override
    public void afterInitialized(BaseComponent comp) {
        super.afterInitialized(comp);
        noSelection = lblName.getLabel();
        IUser user = SecurityUtil.getAuthenticatedUser();
        lblUser.setVisible(showUser);

        if (user == null) {
            setLabel(lblUser, "Unknown User", null);
        } else {
            setLabel(lblUser, user.getFullName() + " @ " + user.getSecurityDomain().getName(), null);
        }

        setPatient(PatientContext.getActivePatient());
        PatientContext.getPatientContext().addListener(patientChangeListener);
    }

    @Override
    public void onLoad(ElementPlugin plugin) {
        super.onLoad(plugin);
        plugin.registerProperties(this, "showUser");
    }

    @Override
    public void onUnload() {
        PatientContext.getPatientContext().removeListener(patientChangeListener);
    }

    @EventHandler(value = "click", target = "lnkSelect")
    private void onClick$lnkSelect() {
        PatientSelection.show(true);
    }

    @EventHandler(value = "click", target = "@btnDetail")
    private void onClick$btnDetail() {
        buildDetail();
        popDetail.open(btnDetail, "left top", "right bottom");
    }

    private void setPatient(IPatient patient) {
        this.patient = patient;
        hideLabels();
        needsDetail = true;
        pnlDetail.destroyChildren();

        if (log.isDebugEnabled()) {
            log.debug("patient: " + patient);
        }

        if (patient == null) {
            lblName.setLabel(noSelection);
            btnDetail.setDisabled(true);
            return;
        }

        btnDetail.setDisabled(false);
        patientName = patient.getFullName();
        IIdentifier mrn = patient.getMRN();
        lblName.setLabel(patientName + (mrn == null ? "" : "  (" + mrn.getValue() + ")"));
        setLabel(lblDOB, formatDateAndAge(patient.getBirthDate()), lblDOBLabel);
        setLabel(lblDOD, DateUtil.formatDate(patient.getDeceasedDate()), lblDODLabel);
        setLabel(lblGender, patient.hasGender() ? patient.getGender().toString() : null, null);
    }

    private String formatDateAndAge(Date date) {
        return date == null ? null : DateUtil.formatDate(date) + " (" + DateUtil.formatAge(date) + ")";
    }

    private void setLabel(
            Label label,
            String value,
            BaseUIComponent associatedComponent) {
        label.setLabel(value);
        label.setVisible(value != null && !value.isEmpty());

        if (associatedComponent != null) {
            associatedComponent.setVisible(label.isVisible());
        }
    }

    private void hideLabels() {
        for (Label child : tbPatient.getChildren(Label.class)) {
            if (child != lblName) {
                child.setVisible(false);
            }
        }
    }

    private boolean buildDetail() {
        if (!needsDetail) {
            return false;
        }

        needsDetail = false;

        Pane header = null;

        // Names

        for (IPersonName name : patient.getNames()) {

            String nm = name.toString();

            if (patientName.equals(nm)) {
                continue;
            }

            if (header == null) {
                header = addHeader("Other Names");
            }

            addDetail(header, nm, null);
        }

        // Identifiers

        header = null;

        if (patient.getIdentifiers() != null) {
            for (IIdentifier id : patient.getIdentifiers()) {
                if (header == null) {
                    header = addHeader("Identifiers");
                }

                Identifier.IdentifierCategory categoryId = id.getCategory();
                String category = categoryId == null ? null : categoryId.name().toLowerCase();
                String system = StringUtils.defaultString(id.getSystem());
                String value = StringUtils.defaultString(id.getValue());

                if (!StringUtils.isEmpty(system)) {
                    value += " (" + system + ")";
                }

                addDetail(header, value, category);
            }
        }

        // Communication

        header = null;

        /* TODO:
        for (IPatient.Communication comm : patient.getCommunication()) {
            if (header == null) {
                header = addHeader("Communication");
            }

            String language = FhirUtil.getDisplayValueForType(comm.getLanguage());

            if (comm.getPreferred()) {
                language += " (preferred)";
            }

            addDetail(header, language, null);
        }
        // Telecom info

        header = null;

        List<ContactPointDt> telecoms = new ArrayList<>(patient.getTelecom());
        Collections.sort(telecoms, (cp1, cp2) -> cp1.getRank() - cp2.getRank());

        for (ContactPointDt telecom : telecoms) {
            if (header == null) {
                header = addHeader("Contact Details");
            }

            String type = !telecom.getSystemElement().isEmpty() ? telecom.getSystem() : "";
            String use = telecom.getUseElement().isEmpty() ? telecom.getUse() : "";

            if (!StringUtils.isEmpty(use)) {
                type += " (" + use + ")";
            }

            addDetail(header, telecom.getValue(), type);
        }

        // Address(es)
        header = null;

        for (AddressDt address : patient.getAddress()) {
            if (header == null) {
                header = addHeader("Addresses");
            }

            String type = !address.getTypeElement().isEmpty() ? address.getType() : "";
            String use = !address.getUseElement().isEmpty() ? address.getUse() : "";

            if (!StringUtils.isEmpty(type)) {
                use += " (" + type + ")";
            }

            addDetail(header, " ", use);

            for (StringDt line : address.getLine()) {
                addDetail(header, line.getValue(), null);
            }

            StringBuilder line = new StringBuilder();
            line.append(address.getCity()).append(", ");
            line.append(address.getState()).append("  ");
            line.append(address.getPostalCode());
            addDetail(header, line.toString(), null);
        }

        if (pnlDetail.getFirstChild() == null) {
            addHeader(StrUtil.getLabel("fcfpatientheader.nodetail.label"));
        }
        */
        return true;
    }

    private Pane addHeader(String text) {
        Pane header = new Pane();
        header.setTitle(text);
        pnlDetail.addChild(header);
        return header;
    }

    private void addDetail(
            Pane header,
            String text,
            String label) {
        if (StringUtils.isEmpty(text)) {
            return;
        }

        if (label != null) {
            Label lbl = new Label(label);
            lbl.addClass("fcf-patientheader-label");
            header.addChild(lbl);
        }

        header.addChild(new Label(text));
    }

    public boolean getShowUser() {
        return showUser;
    }

    public void setShowUser(boolean showUser) {
        this.showUser = showUser;

        if (lblUser != null) {
            lblUser.setVisible(showUser);
        }
    }

}
