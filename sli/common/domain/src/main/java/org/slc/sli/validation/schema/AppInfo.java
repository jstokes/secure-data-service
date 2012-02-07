package org.slc.sli.validation.schema;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.ws.commons.schema.constants.Constants.BlockConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.slc.sli.domain.enums.Right;

/**
 * SLI appinfo annotation support. This class holds SLI-specific appinfo elements as
 * defined in the SLI XSD files.
 *
 * @author asaarela
 *
 */
public class AppInfo extends Annotation {

    protected static final String SLI_XSD_NAMESPACE = "http://slc-sli/ed-org/0.1";
    protected static final String PII_ELEMENT_NAME = "PersonallyIdentifiableInfo";
    protected static final String READ_ENFORCEMENT_ELEMENT_NAME = "ReadEnforcement";
    protected static final String WRITE_ENFORCEMENT_ELEMENT_NAME = "WriteEnforcement";
    protected static final String SECURITY_SPHERE = "SecuritySphere";

    private final Map<String, String> values = new LinkedHashMap<String, String>();

    /**
     * Construct an AppInfo instance from a list of AppInfo DOM nodes.
     *
     * @param nodes
     */
    public AppInfo(NodeList nodes) {

        if (nodes == null) {
            return;
        }

        for (int appInfoNodeIdx = 0; appInfoNodeIdx < nodes.getLength(); ++appInfoNodeIdx) {
            Node node = nodes.item(appInfoNodeIdx);
            if (node instanceof Element) {
                Element e = (Element) node;

                // ignore nodes not in the sli namespace
                if (!e.getNamespaceURI().equals(SLI_XSD_NAMESPACE)) {
                    continue;
                }

                String key = node.getLocalName().trim();
                String value = node.getFirstChild().getNodeValue().trim();

                values.put(key, value);
            }
        }
    }

    public void put(String key, String value) {
        values.put(key, value);
    }

    @Override
    public Annotation.AnnotationType getType() {
        return Annotation.AnnotationType.APPINFO;
    }


    public Map<String, String> getValues() {
        return values;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        int idx = 0;
        int len = values.size();

        for (Map.Entry<String, String> entry : values.entrySet()) {
            builder.append("\"" + entry.getKey() + "\":");
            builder.append("\"" + entry.getValue() + "\"");

            if (++idx < len) {
                builder.append(", ");
            }

        }

        return builder.toString();
    }

    /**
     * Helper functions
     */

    public boolean isPersonallyIdentifiableInfo() {

        boolean rval = false;
        if (values.containsKey(PII_ELEMENT_NAME)) {
            rval = Boolean.parseBoolean(values.get(PII_ELEMENT_NAME));
        }

        return rval;
    }

    public Right getReadAuthority() {
        Right rval = Right.READ_GENERAL;

        if (values.containsKey(READ_ENFORCEMENT_ELEMENT_NAME)) {
            return Right.valueOf(values.get(READ_ENFORCEMENT_ELEMENT_NAME));
        }

        return rval;
    }

    public Right getWriteAuthority() {
        Right rval = Right.WRITE_GENERAL;

        if (values.containsKey(WRITE_ENFORCEMENT_ELEMENT_NAME)) {
            return Right.valueOf(values.get(WRITE_ENFORCEMENT_ELEMENT_NAME));
        }

        return rval;
    }

    public String getSecuritySphere() {
        String rval = "CDM";

        if (values.containsKey(SECURITY_SPHERE)) {
            rval = values.get(SECURITY_SPHERE);
        }

        return rval;
    }

    public String getValue(String key) {
        return values.get(key);

    }

    /**
     * Inherit more restrictive annotations from a parent container.
     *
     * @param parentInfo
     */
    public void inherit(AppInfo parentInfo) {
        if (parentInfo.isPersonallyIdentifiableInfo()) {
            values.put(PII_ELEMENT_NAME, "true");
        }

        switch (parentInfo.getReadAuthority()) {
            case FULL_ACCESS:
                values.put(READ_ENFORCEMENT_ELEMENT_NAME, Right.FULL_ACCESS.toString());
                break;
            case ADMIN_ACCESS:
                if (getReadAuthority() != Right.FULL_ACCESS) {
                    values.put(READ_ENFORCEMENT_ELEMENT_NAME, Right.ADMIN_ACCESS.toString());
                }
                break;
            case READ_RESTRICTED:
                if (getReadAuthority() != Right.FULL_ACCESS && getReadAuthority() != Right.ADMIN_ACCESS) {
                    values.put(READ_ENFORCEMENT_ELEMENT_NAME, Right.READ_RESTRICTED.toString());
                }
            case READ_GENERAL:
                if (getReadAuthority() == Right.ANONYMOUS_ACCESS) {
                    values.put(READ_ENFORCEMENT_ELEMENT_NAME, Right.READ_GENERAL.toString());
                }
                break;
        }

        switch (parentInfo.getWriteAuthority()) {
            case FULL_ACCESS:
                values.put(WRITE_ENFORCEMENT_ELEMENT_NAME, Right.FULL_ACCESS.toString());
                break;
            case ADMIN_ACCESS:
                if (getWriteAuthority() != Right.FULL_ACCESS) {
                    values.put(WRITE_ENFORCEMENT_ELEMENT_NAME, Right.ADMIN_ACCESS.toString());
                }
                break;
            case WRITE_RESTRICTED:
                if (getWriteAuthority() != Right.FULL_ACCESS && getWriteAuthority() != Right.ADMIN_ACCESS) {
                    values.put(WRITE_ENFORCEMENT_ELEMENT_NAME, Right.WRITE_RESTRICTED.toString());
                }
                break;
            case WRITE_GENERAL:
                if (getWriteAuthority() == Right.ANONYMOUS_ACCESS) {
                    values.put(WRITE_ENFORCEMENT_ELEMENT_NAME, Right.WRITE_GENERAL.toString());
                }
                break;
        }

        if (parentInfo.getSecuritySphere() != null) {
            values.put(SECURITY_SPHERE, parentInfo.getSecuritySphere());
        }
    }

    public boolean isRequired() {
        String tmp = values.get(BlockConstants.REQUIRED);
        if (tmp != null) {
            return Boolean.parseBoolean(tmp);
        }
        return false;
    }

}
