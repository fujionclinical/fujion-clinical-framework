package org.fujionclinical.api.model.core;

import org.apache.commons.collections.CollectionUtils;
import org.fujionclinical.api.model.person.IPerson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Annotation implements IAnnotation {

    private final List<IPerson> authors = new ArrayList<>();

    private Date recorded;

    private String text;

    public Annotation(
            String text,
            IPerson... authors) {
        this(text, new Date(), authors);
    }

    public Annotation(
            String text,
            Date recorded,
            IPerson... authors) {
        this.text = text;
        this.recorded = recorded;
        CollectionUtils.addAll(this.authors, authors);
    }

    @Override
    public List<IPerson> getAuthors() {
        return authors;
    }

    @Override
    public Date getRecorded() {
        return recorded;
    }

    @Override
    public void setRecorded(Date recorded) {
        this.recorded = recorded;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

}
