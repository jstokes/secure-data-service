package org.slc.sli.modeling.rest;public enum ParamStyle {    /**     * The parameter is represented as a string encoding of the parameter value and is substituted     * into the value of the resource element.     */    TEMPLATE,    /**     * query     */    QUERY,    /**     * Specifies a matrix URI component.     */    MATRIX,    /**     * Specifies a component of the representation formatted as a string encoding of the parameter     * value according to the rules of the media type.     */    PLAIN,    /**     * Specifies a HTTP header that pertains to the FTTP request (resource or request) or HTTP     * response (response).     */    HEADER;}