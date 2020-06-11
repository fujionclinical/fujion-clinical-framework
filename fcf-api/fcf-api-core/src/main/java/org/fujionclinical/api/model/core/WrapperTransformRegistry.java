package org.fujionclinical.api.model.core;

import org.fujion.common.Logger;
import org.fujionclinical.api.core.CoreUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Indexes all wrapper transforms by their native and model types.
 */
public class WrapperTransformRegistry implements BeanPostProcessor {

    private static class TransformEntry {

        final IWrapperTransform transform;

        final Class<?> toClass;

        final Class<?> fromClass;

        TransformEntry(IWrapperTransform transform) {
            this.transform = transform;
            Class<?>[] genericTypes = CoreUtil.getGenericParameters(transform.getClass(), IWrapperTransform.class);
            Assert.isTrue(genericTypes.length == 2, () ->
                    "Class '" + transform.getClass() + "' has incorrect number of generic parameters (" + genericTypes.length + ").");
            this.toClass = genericTypes[0];
            this.fromClass = genericTypes[1];
        }

        boolean matches(
                Class<?> fromClass,
                Class<?> toClass) {
            return fromClass.isAssignableFrom(this.fromClass) && toClass.isAssignableFrom(this.toClass);
        }

    }

    private static final Logger log = Logger.create(WrapperTransformRegistry.class);

    private static final WrapperTransformRegistry instance = new WrapperTransformRegistry();

    private final List<TransformEntry> transforms = new ArrayList<>();

    public static WrapperTransformRegistry getInstance() {
        return instance;
    }

    private WrapperTransformRegistry() {
    }

    public IWrapperTransform get(
            Class<?> toClass,
            Class<?> fromClass) {
        List<IWrapperTransform> candidates = transforms.stream()
                .filter(entry -> entry.matches(fromClass, toClass))
                .map(entry -> entry.transform)
                .collect(Collectors.toList());

        Assert.isTrue(candidates.size() == 1,
                () -> (candidates.isEmpty() ? "No wrapper transform " : "Multiple wrapper transforms")
                        + " found for converting '" + fromClass + "' to '" + toClass + "'.");
        return candidates.get(0);
    }

    @Override
    public Object postProcessAfterInitialization(
            Object bean,
            String beanName) throws BeansException {
        if (bean instanceof IWrapperTransform) {
            transforms.add(new TransformEntry((IWrapperTransform) bean));
            log.info(() -> "Registered wrapper transform '" + bean.getClass() + "'.");
        }
        return bean;
    }

}
