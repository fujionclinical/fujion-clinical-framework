package org.fujionclinical.api.query;

import org.apache.commons.lang.ArrayUtils;

/**
 * Valid query operators.  NOTE: The order is important.  Operators with the longest alias name(s)
 * should appear first for proper parsing to occur.
 */
public enum QueryOperator {
    NE("<>", "!="), LE("<=", "!>"), GE(">=", "!<"), LT("<"), GT(">"), EQ("="), SW("~");

    private final String[] aliases;

    QueryOperator() {
        this(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    QueryOperator(String... aliases) {
        this.aliases = aliases;
    }

    public String[] getAliases() {
        return aliases;
    }
}
