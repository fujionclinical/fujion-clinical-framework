package org.fujionclinical.api.query.expression;

/**
 * Represents a parameter descriptor for a specific parameter type.  A descriptor does the following:
 * <ul>
 *     <li>Validates expression fragments</li>
 *     <li>Normalizes operands</li>
 * </ul>
 *
 *
 * @param <PRM> The parameter type.
 * @param <OPD> The normalized operand type.
 */
public interface IParameterDescriptor<PRM, OPD> {

    /**
     * This is the parameter type handled by the transform.
     *
     * @return The parameter type handled by the transform.
     */
    Class<PRM> getParameterType();

    /**
     * Validates that a query expression fragment references a supported operator and has the correct number of operands.
     *
     * @param fragment A query expression fragment.
     * @throws IllegalArgumentException If the fragment fails validation.
     */
    void validateFragment(ExpressionFragment fragment);

    /**
     * Transforms an operand from its original form to the target type.
     *
     * @param parameterType The parameter type.
     * @param operand The raw operand as obtained from the query context.
     * @param previousOperand The previously transformed operand (null if this is the first operand).
     * @return The transformed operand.
     */
    OPD normalizeOperand(
            Class<PRM> parameterType,
            Object operand,
            OPD previousOperand);

}
