package org.fujionclinical.api.test;

import edu.utah.kmm.model.cool.mediator.common.AbstractModelDescriptor;

public class MockModelDescriptor extends AbstractModelDescriptor {

    public MockModelDescriptor() {
        super("mock", "Mock data source", null, null);
    }

    @Override
    protected void initialize() {
    }

}
