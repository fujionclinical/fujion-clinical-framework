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
package org.fujionclinical.ui.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.fujion.common.MiscUtil;
import org.fujion.component.*;
import org.fujion.core.BeanUtil;

import java.util.List;

public class FCFUtil {

    /**
     * Possible match modes for hierarchical tree search.
     */
    public enum MatchMode {
        AUTO, // Autodetect index vs label
        INDEX, // By node index.
        CASE_SENSITIVE, // Case sensitive by node label.
        CASE_INSENSITIVE // Case insensitive by node label.
    }

    /**
     * Returns a component of a type suitable for displaying the specified text. For text that is a
     * URL, returns a hyperlink. For text that begins with &lt;html&gt;, returns an HTML component.
     * All other text returns a cell.
     *
     * @param text Text to be displayed.
     * @return BaseComponent of the appropriate type.
     */
    public static BaseComponent getTextComponent(String text) {
        String frag = text == null ? "" : StringUtils.substring(text, 0, 20).toLowerCase();

        if (frag.contains("<html>")) {
            return new Html(text);
        }

        if (frag.matches("^https?://.+$")) {
            Hyperlink link = new Hyperlink();
            link.setHref(text);
            link.setTarget("_blank");
            return link;
        }

        return new Cell(text);
    }

    /**
     * Sets focus to first input element under the parent that is capable of receiving focus.
     *
     * @param parent Parent component.
     * @param select If true, select contents after setting focus.
     * @return The input element that received focus, or null if focus was not set.
     */
    public static BaseInputboxComponent<?> focusFirst(BaseComponent parent, boolean select) {
        for (BaseComponent child : parent.getChildren()) {
            BaseInputboxComponent<?> ele;

            if (child instanceof BaseInputboxComponent) {
                ele = (BaseInputboxComponent<?>) child;

                if (ele.isVisible() && !ele.isDisabled() && !ele.isReadonly()) {
                    ele.focus();

                    if (select) {
                        ele.selectAll();
                    }

                    return ele;
                }
            } else if ((ele = focusFirst(child, select)) != null) {
                return ele;
            }
        }

        return null;
    }

    /**
     * Returns the node associated with the specified \-delimited path.
     *
     * @param <NODE>    Class of the node component.
     * @param root      Root component of hierarchy.
     * @param nodeClass Class of the node component.
     * @param path      \-delimited path to search.
     * @param create    If true, nodes are created as needed.
     * @param matchMode The match mode.
     * @return The node corresponding to the specified path, or null if not found.
     */
    @SuppressWarnings("unchecked")
    public static <NODE extends BaseComponent> NODE findNode(BaseComponent root, Class<NODE> nodeClass, String path,
                                                             boolean create, MatchMode matchMode) {
        String[] pcs = path.split("\\\\");
        BaseComponent node = null;

        try {
            for (String pc : pcs) {
                if (pc.isEmpty()) {
                    continue;
                }

                BaseComponent parent = node == null ? root : node;
                node = null;
                int index = matchMode == MatchMode.INDEX || matchMode == MatchMode.AUTO ? NumberUtils.toInt(pc, -1) : -1;
                MatchMode mode = matchMode != MatchMode.AUTO ? matchMode
                        : index >= 0 ? MatchMode.INDEX : MatchMode.CASE_INSENSITIVE;
                List<BaseComponent> children = parent.getChildren();
                int size = children.size();

                if (mode == MatchMode.INDEX) {

                    if (index < 0) {
                        index = size;
                    }

                    int deficit = index - size;

                    if (!create && deficit >= 0) {
                        return null;
                    }

                    while (deficit-- >= 0) {
                        parent.addChild(nodeClass.newInstance());
                    }
                    node = children.get(index);

                } else {
                    for (BaseComponent child : children) {
                        String label = BeanUtil.getPropertyValue(child, "label", String.class);

                        if (mode == MatchMode.CASE_SENSITIVE ? pc.equals(label) : pc.equalsIgnoreCase(label)) {
                            node = child;
                            break;
                        }
                    }

                    if (node == null) {
                        if (!create) {
                            return null;
                        }
                        node = nodeClass.newInstance();
                        parent.addChild(node);
                        BeanUtil.setPropertyValue(node, "label", pc);
                    }
                }

                if (node == null) {
                    break;
                }
            }
        } catch (Exception e) {
            throw MiscUtil.toUnchecked(e);
        }

        return (NODE) node;
    }

    private FCFUtil() {
    }
}
