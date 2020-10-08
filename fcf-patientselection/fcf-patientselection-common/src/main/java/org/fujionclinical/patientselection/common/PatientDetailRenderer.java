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
package org.fujionclinical.patientselection.common;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.fujion.ancillary.MimeContent;
import org.fujion.common.DateUtil;
import org.fujion.common.StrUtil;
import org.fujion.component.BaseUIComponent;
import org.fujion.component.Div;
import org.fujion.component.Image;
import org.fujion.component.Label;
import org.fujionclinical.api.model.core.IAttachment;
import org.fujionclinical.api.model.core.IContactPoint;
import org.fujionclinical.api.model.core.IContactPointType;
import org.fujionclinical.api.model.core.IPostalAddress;
import org.fujionclinical.api.model.patient.IPatient;
import org.fujionclinical.ui.util.Formatters;

/**
 * Default class for rendering detail view of patient in patient selection dialog. This class may be
 * overridden to provide an alternate detail view.
 */
public class PatientDetailRenderer implements IPatientDetailRenderer {

    /**
     * Render detail view for the specified patient.
     *
     * @param patient Patient whose detail view is to be rendered.
     */
    @Override
    public BaseUIComponent render(IPatient patient) {
        BaseUIComponent root = new Div();
        root.addClass("fujion-layout-vertical text-center");
        root.addStyle("align-items", "center");

        if (confirmAccess(patient, root)) {
            renderDemographics(patient, root);
        }

        return root;
    }

    protected void renderDemographics(
            IPatient patient,
            BaseUIComponent root) {
        root.addChild(new Div());
        Image photo = new Image();
        photo.setStyles("max-height:300px;max-width:300px;padding-bottom:10px");
        IAttachment pix = patient.getPhoto();
        MimeContent content = pix == null ? null : pix.getContent();

        if (content == null) {
            photo.setSrc(Constants.IMAGE_SILHOUETTE);
        } else {
            photo.setContent(content);
        }

        root.addChild(photo);
        addDemographic(root, null, patient.getFullName(), "font-weight: bold");
        addDemographic(root, "mrn", patient.getMRN());
        addDemographic(root, "gender", patient.getGender());
        addDemographic(root, "race", patient.getRace());
        addDemographic(root, "age", DateUtil.formatAge(patient.hasBirthDate() ? patient.getBirthDate() : null));
        addDemographic(root, "dob", patient.getBirthDate());
        addDemographic(root, "dod", patient.getDeceasedDate());
        addDemographic(root, "marital_status", patient.getMaritalStatus());
        addDemographic(root, "language", patient.hasLanguage() ? patient.getLanguages() : null);
        addContactPoint(root, "home_phone", patient);
        addContactPoint(root, "home_email", patient);
        addContactPoint(root, "home_fax", patient);
        addContactPoint(root, "work_phone", patient);
        addContactPoint(root, "work_email", patient);
        addContactPoint(root, "work_fax", patient);

        IPostalAddress address = patient.getAddress();

        if (address != null) {
            root.addChild(new Div());

            if (address.hasLines()) {
                for (String line : address.getLines()) {
                    addDemographic(root, null, line);
                }
            }

            String city = StringUtils.defaultString(address.getCity());
            String state = StringUtils.defaultString(address.getState());
            String zip = StringUtils.defaultString(address.getPostalCode());
            String sep = city.isEmpty() || state.isEmpty() ? "" : ", ";
            addDemographic(root, null, city + sep + state + "  " + zip);
        }

    }

    /**
     * Confirm access to patient.
     *
     * @param patient The patient to check.
     * @param root    The root component.
     * @return True if access confirmed.
     */
    private boolean confirmAccess(
            IPatient patient,
            BaseUIComponent root) {
        boolean allowed = confirmAccess(patient);

        if (!allowed) {
            addDemographic(root, null, getDemographicLabel("restricted"), "font-weight: bold");
        }

        return allowed;
    }

    /**
     * Override to restrict access to certain patients.
     *
     * @param patient The patient to check.
     * @return True if access confirmed.
     */
    protected boolean confirmAccess(IPatient patient) {
        return patient != null; //!patient.isRestricted();
    }

    /**
     * Adds a contact point to the demographic panel. Uses default styling.
     *
     * @param root          Root component.
     * @param type          Type of contact point desired (e.g., "home_phone").
     * @param contactPoints List of contact points from which to select.
     */
    protected void addContactPoint(
            BaseUIComponent root,
            String type,
            IContactPointType contactPoints) {
        String[] types = type.split("_", 2);
        IContactPoint.ContactPointUse use = EnumUtils.getEnumIgnoreCase(IContactPoint.ContactPointUse.class, types[0]);
        IContactPoint.ContactPointSystem system = EnumUtils.getEnumIgnoreCase(IContactPoint.ContactPointSystem.class, types[1]);
        IContactPoint contactPoint = contactPoints.getContactPoint(use, system);

        if (contactPoint != null) {
            addDemographic(root, type, contactPoint.getValue());
        }
    }

    /**
     * Adds a demographic element to the demographic panel. Uses default styling.
     *
     * @param root    Root component.
     * @param labelId The id of the label to use.
     * @param object  The element to be added.
     */
    protected void addDemographic(
            BaseUIComponent root,
            String labelId,
            Object object) {
        addDemographic(root, labelId, object, null);
    }

    /**
     * Adds a demographic element to the demographic panel.
     *
     * @param root    Root component.
     * @param labelId The id of the label to use.
     * @param object  The element to be added.
     * @param style   CSS styling to apply to element (may be null).
     */
    protected void addDemographic(
            BaseUIComponent root,
            String labelId,
            Object object,
            String style) {
        String value = Formatters.format(object);

        if (!StringUtils.isEmpty(value)) {
            String label = getDemographicLabel(labelId);
            Label lbl = new Label((label == null ? "" : label + ": ") + value);
            root.addChild(lbl);

            if (style != null) {
                lbl.addStyles(style);
            }
        }

    }

    /**
     * Returns the text for the specified label id.
     *
     * @param labelId The id of the label value to locate. If no prefix is present, the id is
     *                prefixed with "patient.selection.demographic.label." to find the associated value.
     * @return Label text.
     */
    protected String getDemographicLabel(String labelId) {
        return labelId == null ? null : StrUtil.getLabel(labelId.contains(".") ? labelId : "patientselection.demographic.label." + labelId);
    }

}
