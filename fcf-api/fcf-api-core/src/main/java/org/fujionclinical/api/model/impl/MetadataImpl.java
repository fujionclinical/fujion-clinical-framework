package org.fujionclinical.api.model.impl;

import edu.utah.kmm.model.cool.core.datatype.Metadata;
import edu.utah.kmm.model.cool.terminology.ConceptReferenceSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MetadataImpl implements Metadata {

    private final List<ConceptReferenceSet> group = new ArrayList<>();

    private final List<String> keyword = new ArrayList<>();

    @Override
    public List<ConceptReferenceSet> getGroup() {
        return group;
    }

    @Override
    public void setGroup(List<ConceptReferenceSet> group) {
        this.group.clear();
        this.group.addAll(group);
    }

    @Override
    public boolean hasGroup() {
        return !group.isEmpty();
    }

    @Override
    public void addGroup(ConceptReferenceSet... group) {
        Collections.addAll(this.group, group);
    }

    @Override
    public List<String> getKeyword() {
        return keyword;
    }

    @Override
    public void setKeyword(List<String> keyword) {
        this.keyword.clear();
        this.keyword.addAll(keyword);
    }

    @Override
    public boolean hasKeyword() {
        return !keyword.isEmpty();
    }

    @Override
    public void addKeyword(String... keyword) {
        Collections.addAll(this.keyword, keyword);
    }

}
