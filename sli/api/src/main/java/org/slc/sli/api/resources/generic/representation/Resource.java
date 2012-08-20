package org.slc.sli.api.resources.generic.representation;

/**
 * Created with IntelliJ IDEA.
 * User: srupasinghe
 * Date: 8/20/12
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class Resource {

    private String namespace;
    private String resourceType;

    public Resource(String namespace,String resourceType) {
        this.namespace = namespace;
        this.resourceType = resourceType;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getResourceType() {
        return resourceType;
    }
}
