package org.slc.sli.api.resources.generic.util;

/**
 * @author jstokes
 */
public enum ResourceTemplate {
    ONE_PART("/generic/{version}/{resource}"),
    ONE_PART_FULL("/generic/{resource}"),
    TWO_PART("/generic/{version}/{resource}/{id}"),
    THREE_PART("/generic/{version}/{endpoint}/{id}/{resource}"),
    FOUR_PART("/generic/{version}/{endpoint}/{id}/{association}/{resource}"),
    CUSTOM("/generic/{version}/{resource}/{id}/custom");

    private final String template;

    private ResourceTemplate(final String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
