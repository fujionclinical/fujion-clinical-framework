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
package org.fujionclinical.api.model.core;

import org.fujion.common.CollectionUtil;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public interface IAttachmentType {

    /**
     * Returns all attachments-.
     *
     * @return A list of all attachments (never null)
     */
    default List<IAttachment> getAttachments() {
        return Collections.emptyList();
    }

    default void setAttachments(List<IAttachment> attachments) {
        CollectionUtil.replaceList(getAttachments(), attachments);
    }

    default List<IAttachment> getAttachments(Predicate<IAttachment> criteria) {
        return CollectionUtil.findMatches(getAttachments(), criteria);
    }

    /**
     * Returns the person's attachment from one of the specified titles.  Titles are
     * searched in order until a match is found.
     *
     * @param titles Only attachments belonging to one of these titles will be returned.
     * @return The person's attachment, or null if not found.
     */
    default IAttachment getAttachment(String... titles) {
        return CollectionUtil.findMatch(getAttachments(), (attachment, title) ->
                title.equalsIgnoreCase(attachment.getTitle()), titles);
    }

    default void addAttachments(IAttachment... attachments) {
        Collections.addAll(getAttachments(), attachments);
    }

    default boolean hasAttachment() {
        return CollectionUtil.notEmpty(getAttachments());
    }

}
