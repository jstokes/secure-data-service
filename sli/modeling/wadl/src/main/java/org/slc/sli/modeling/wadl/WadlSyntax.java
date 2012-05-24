package org.slc.sli.modeling.wadl;

import java.util.List;

import org.slc.sli.modeling.rest.ParamStyle;

/**
 * Utilities used to encode and decode WADL.
 */
public final class WadlSyntax {

    public static final String NAMESPACE = "http://wadl.dev.java.net/2009/02";
    public static final String QUERY_TYPE_APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

    public static final ParamStyle decodeParamStyle(final String value) {
        if (value != null) {
            if ("header".equals(value)) {
                return ParamStyle.HEADER;
            } else if ("matrix".equals(value)) {
                return ParamStyle.MATRIX;
            } else if ("plain".equals(value)) {
                return ParamStyle.PLAIN;
            } else if ("query".equals(value)) {
                return ParamStyle.QUERY;
            } else if ("template".equals(value)) {
                return ParamStyle.TEMPLATE;
            } else {
                throw new IllegalArgumentException(value);
            }
        } else {
            return null;
        }
    }

    public static final String encodeParamStyle(final ParamStyle style) {
        if (style != null) {
            switch (style) {
                case HEADER: {
                    return "header";
                }
                case MATRIX: {
                    return "matrix";
                }
                case PLAIN: {
                    return "plain";
                }
                case QUERY: {
                    return "query";
                }
                case TEMPLATE: {
                    return "template";
                }
                default: {
                    throw new AssertionError(style);
                }
            }
        } else {
            return null;
        }
    }

    public static final String encodeStringList(final List<String> strings) {
        if (strings != null) {
            final int size = strings.size();
            if (size == 0) {
                return "";
            } else if (size == 1) {
                return strings.get(0);
            } else {
                final StringBuilder sb = new StringBuilder();
                boolean first = true;
                for (final String s : strings) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(' ');
                    }
                    sb.append(s);
                }
                return sb.toString();
            }
        } else {
            return null;
        }
    }

    private WadlSyntax() {

    }
}
