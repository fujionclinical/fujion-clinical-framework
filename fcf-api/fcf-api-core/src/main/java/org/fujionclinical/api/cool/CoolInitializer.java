package org.fujionclinical.api.cool;

import edu.utah.kmm.model.cool.mediator.dao.DAOFactories;
import edu.utah.kmm.model.cool.mediator.dao.DAOFactory;
import edu.utah.kmm.model.cool.mediator.datasource.DataSource;
import edu.utah.kmm.model.cool.mediator.datasource.DataSources;
import edu.utah.kmm.model.cool.mediator.expression.parameter.ParameterDescriptor;
import edu.utah.kmm.model.cool.mediator.expression.parameter.ParameterDescriptors;
import edu.utah.kmm.model.cool.mediator.transform.ModelTransform;
import edu.utah.kmm.model.cool.mediator.transform.ModelTransforms;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Adds automatic bean registration for COOL registries.
 */
public class CoolInitializer implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(
            Object bean,
            String beanName) throws BeansException {
        if (bean instanceof DataSource) {
            DataSources.register((DataSource) bean);
        } else if (bean instanceof ModelTransform) {
            ModelTransforms.register((ModelTransform) bean);
        } else if (bean instanceof DAOFactory) {
            DAOFactories.register((DAOFactory) bean);
        } else if (bean instanceof ParameterDescriptor) {
            ParameterDescriptors.register((ParameterDescriptor) bean);
        }

        return bean;
    }

}
