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
package org.fujionclinical.shell.layout;

import org.fujion.common.CollectionUtil;
import org.fujionclinical.api.property.IPropertyProvider;
import org.fujionclinical.shell.plugins.PluginDefinition;
import org.w3c.dom.Element;

import java.util.*;

/**
 * Base class for all node types within a layout.
 */
public abstract class LayoutNode implements IPropertyProvider {

    protected static final String NULL_VALUE = "\\null\\";

    private final Map<String, String> attributes = new HashMap<>();

    private final String tagName;

    private final LayoutNode parent;

    private final List<LayoutNode> children = new ArrayList<>();

    private final PluginDefinition pluginDefinition;

    protected LayoutNode(
            String tagName,
            LayoutNode parent,
            PluginDefinition pluginDefinition) {
        this.tagName = tagName;
        this.parent = parent;
        this.pluginDefinition = pluginDefinition;

        if (parent != null) {
            parent.getChildren().add(this);
        }
    }

    /**
     * Returns the tag name used when serializing this node.
     *
     * @return The tag name.
     */
    protected String getTagName() {
        return tagName;
    }

    protected PluginDefinition getDefinition() {
        return pluginDefinition;
    }

    protected List<LayoutNode> getChildren() {
        return children;
    }

    protected <T extends LayoutNode> T getChild(Class<T> clazz) {
        Iterator<T> iter = CollectionUtil.iteratorForType(children, clazz);
        return iter.hasNext() ? iter.next() : null;
    }

    protected LayoutNode getParent() {
        return parent;
    }

    protected LayoutNode getNextSibling() {
        int i = parent == null ? 0 : parent.children.indexOf(this) + 1;
        return i == 0 || i >= parent.children.size() ? null : parent.children.get(i);
    }

    /**
     * Returns the attribute map for the node.
     *
     * @return The attribute map.
     */
    protected Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * @see org.fujionclinical.api.property.IPropertyProvider#getProperty(java.lang.String)
     */
    @Override
    public String getProperty(String key) {
        String value = attributes.get(key);
        return NULL_VALUE.equals(value) ? null : value;
    }

    /**
     * @see org.fujionclinical.api.property.IPropertyProvider#hasProperty(java.lang.String)
     */
    @Override
    public boolean hasProperty(String key) {
        return attributes.containsKey(key);
    }

    /**
     * Creates a DOM node from this layout node.
     *
     * @param parent Parent DOM node.
     * @return The newly created DOM node.
     */
    public Element createDOMNode(Element parent) {
        Element domNode = parent.getOwnerDocument().createElement(tagName);
        LayoutUtil.copyAttributes(attributes, domNode);
        parent.appendChild(domNode);
        return domNode;
    }

}
