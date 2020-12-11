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
package org.fujionclinical.api.model.person;

import edu.utah.kmm.model.cool.foundation.entity.Person;
import edu.utah.kmm.model.cool.foundation.entity.PersonImpl;
import edu.utah.kmm.model.cool.util.CoolUtils;
import edu.utah.kmm.model.cool.util.PersonNameParsers;
import edu.utah.kmm.model.cool.util.PersonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fujionclinical.api.context.ContextItems;
import org.fujionclinical.api.context.ContextManager;
import org.fujionclinical.api.context.IContextSubscriber;
import org.fujionclinical.api.context.ManagedContext;

/**
 * Base context for shared person contexts.
 */
public class AbstractPersonContext extends ManagedContext<Person> {

    private static final Log log = LogFactory.getLog(AbstractPersonContext.class);

    private static final String CCOW_ID = "Id";

    private static final String CCOW_MRN = CCOW_ID + ".MRN";

    // protected static final String CCOW_MPI = CCOW_ID + ".MPI";

    private static final String CCOW_GENDER = "Co.Sex";

    private static final String CCOW_DOB = "Co.DateTimeOfBirth";

    private static final String CCOW_NAM = "Co.PatientName"; // TODO: what about practitioner name?

    /**
     * Returns the managed practitioner context.
     *
     * @param contextClass The context class.
     * @param <T> The context class.
     * @return Practitioner context.
     */
    @SuppressWarnings("unchecked")
    protected static <T extends AbstractPersonContext> T getPersonContext(Class<T> contextClass) {
        return (T) ContextManager.getInstance().getSharedContext(contextClass.getName());
    }

    /**
     * Returns the person in the current context.
     *
     * @param contextClass The context class.
     * @param <T> The context class.
     * @return Person object (may be null).
     */
    protected static <T extends AbstractPersonContext> Person getActivePerson(Class<T> contextClass) {
        return getPersonContext(contextClass).getContextObject(false);
    }

    /**
     * Change the person in the shared context.
     *
     * @param person       The person.
     * @param contextClass The context class.
     * @param <T>          The context class.
     */
    protected static <T extends AbstractPersonContext> void changePerson(
            Person person,
            Class<T> contextClass) {
        T personContext = getPersonContext(contextClass);

        try {
            personContext.requestContextChange(person);
        } catch (Exception e) {
            log.error("Error during " + personContext.getContextName() + " context change.", e);
        }
    }

    /**
     * Create a shared person context with a specified initial state.
     *
     * @param contextName The unique context name.
     * @param subscriberType The interface for context subscriptions.
     * @param person The initial state.
     */
    protected AbstractPersonContext(
            String contextName,
            Class<? extends IContextSubscriber> subscriberType,
            Person person) {
        super(contextName, subscriberType, person);
    }

    /**
     * Create a shared person context with an initial null state.
     *
     * @param contextName The unique context name.
     * @param subscriberType The interface for context subscriptions.
     */
    protected AbstractPersonContext(
            String contextName,
            Class<? extends IContextSubscriber> subscriberType) {
        super(contextName, subscriberType, null);
    }

    /**
     * Creates a CCOW context from the specified person object.
     */
    @Override
    public ContextItems toCCOWContext(Person person) {
        setItem(CCOW_MRN, PersonUtils.hasMRN(person) ? PersonUtils.getMRN(person).getId() : null, "MRN");
        setItem(CCOW_NAM, person.hasName() ? PersonNameParsers.get().toString(person.getName().get(0)) : null);
        setItem(CCOW_GENDER, person.hasGender() ? person.getGender().getFirstConcept().getCode() : null);
        setItem(CCOW_DOB, person.getBirthDate());
        return contextItems;
    }

    /**
     * Returns a person object based on the specified CCOW context.
     */
    @Override
    public Person fromCCOWContext(ContextItems contextItems) {
        Person person = new PersonImpl();
        String id = contextItems.getItem(CCOW_ID);

        if (id != null) {
            person.setDefaultId(id);
        }

        person.setBirthDate(contextItems.getDate(CCOW_DOB));
        person.setGender(PersonUtils.genderAsConceptReferenceSet(getItem(CCOW_GENDER)));
        person.addName(PersonNameParsers.get().fromString(getItem(CCOW_NAM)));
        String mrn = getItem(CCOW_MRN, "MRN");
        PersonUtils.setMRN(person, mrn == null ? null : PersonUtils.createMRN(null, mrn));
        return person;
    }

    /**
     * Get a context item.
     *
     * @param itemName The item name.
     * @return The item value.
     */
    protected String getItem(String itemName) {
        return contextItems.getItem(qualifyItemName(itemName));
    }

    /**
     * Get a context item.
     *
     * @param itemName  The item name.
     * @param qualifier The item qualifier.
     * @return The item value.
     */
    protected String getItem(
            String itemName,
            String qualifier) {
        return contextItems.getItem(qualifyItemName(itemName), qualifier);
    }

    /**
     * Set a context item.
     *
     * @param itemName The item name.
     * @param value    The item value.
     */
    protected void setItem(
            String itemName,
            Object value) {
        contextItems.setItem(qualifyItemName(itemName), value);
    }

    /**
     * Set a context item.
     *
     * @param itemName  The item name.
     * @param value     The item value.
     * @param qualifier The item qualifier.
     */
    protected void setItem(
            String itemName,
            String value,
            String qualifier) {
        contextItems.setItem(qualifyItemName(itemName), value, qualifier);
    }

    /**
     * Prepends the subject name (i.e., the context name) to the item name.
     *
     * @param itemName The item name.
     * @return The qualified item name.
     */
    private String qualifyItemName(String itemName) {
        return getContextName() + "." + itemName;
    }

    /**
     * Returns a priority value of 10.
     *
     * @return Priority value for context manager.
     */
    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    protected boolean isSameContext(
            Person person1,
            Person person2) {
        return CoolUtils.areSame(person1, person2);
    }

}
