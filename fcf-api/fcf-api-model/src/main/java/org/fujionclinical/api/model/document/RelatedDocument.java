package org.fujionclinical.api.model.document;

import org.fujionclinical.api.model.core.IReference;

public class RelatedDocument implements IDocument.IRelatedDocument {

    private IDocument.DocumentRelationship relationship;

    private IReference<IDocument> document;

    @Override
    public IDocument.DocumentRelationship getRelationship() {
        return relationship;
    }

    @Override
    public void setRelationship(IDocument.DocumentRelationship relationship) {
        this.relationship = relationship;
    }

    @Override
    public IReference<IDocument> getDocument() {
        return document;
    }

    @Override
    public void setDocument(IReference<IDocument> value) {
        this.document = document;
    }

}
