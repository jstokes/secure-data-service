package org.slc.sli.modeling.tools;

import javax.xml.namespace.QName;

/**
 * Constants used in the SLI MongoDB W3C XML Schema.
 */
public final class SliMongoConstants {

    private SliMongoConstants() {

    }

    public static final String NAMESPACE_SLI = "http://slc-sli/ed-org/0.1";

    public static final QName SLI_PII = new QName(NAMESPACE_SLI, "PersonallyIdentifiableInfo");
    public static final QName SLI_READ_ENFORCEMENT = new QName(NAMESPACE_SLI, "ReadEnforcement");
    public static final QName SLI_REFERENCE_TYPE = new QName(NAMESPACE_SLI, "ReferenceType");
    public static final QName SLI_WRITE_ENFORCEMENT = new QName(NAMESPACE_SLI, "WriteEnforcement");
    public static final QName SLI_WHITELIST = new QName(NAMESPACE_SLI, "Whitelist")
}
