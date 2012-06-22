/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.slc.sli.api.security.context.traversal.graph;


import org.slc.sli.api.security.context.resolver.EntityContextResolver;

import java.util.List;
import java.util.ArrayList;

/**
 * Basic wrapper class for fields that a connection has
 *
 * @author rlatta
 */
public class SecurityNodeConnection {
    private String fieldName = "";
    private String connectionTo = "";
    private String associationNode = "";
    private boolean isReferenceInSelf = false;
    private boolean isResolver = false;
    private EntityContextResolver resolver = null;
    private List<NodeFilter> filter;

    public NodeAggregator getAggregator() {
        return aggregator;
    }

    private NodeAggregator aggregator = null;

    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @param fieldName the fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * @return the connectionTo
     */
    public String getConnectionTo() {
        return connectionTo;
    }

    /**
     * @param connectionTo the connectionTo to set
     */
    public void setConnectionTo(String connectionTo) {
        this.connectionTo = connectionTo;
    }

    /**
     * @return the associationNode
     */
    public String getAssociationNode() {
        return associationNode;
    }


    public List<NodeFilter> getFilter() {
        return this.filter;
    }

    public boolean isReferenceInSelf() {
        return isReferenceInSelf;
    }

    public boolean isResolver() {
        return isResolver;
    }

    public void setResolver(boolean resolver) {
        isResolver = resolver;
    }

    public EntityContextResolver getResolver() {
        return resolver;
    }

    public void setResolver(EntityContextResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * @param associationNode the associationNode to set
     */
    public void setAssociationNode(String associationNode) {
        this.associationNode = associationNode;
    }

    public SecurityNodeConnection(String toEntity, String withField, String associationNode, NodeFilter filter) {
        this.connectionTo = toEntity;
        this.fieldName = withField;
        this.associationNode = associationNode;
        this.filter = new ArrayList<NodeFilter>();
        this.filter.add(filter);
    }
    public SecurityNodeConnection(String toEntity, String withField, String associationNode, NodeFilter filter, NodeAggregator nodeAggregator) {
        this.connectionTo = toEntity;
        this.fieldName = withField;
        this.associationNode = associationNode;
        this.filter = new ArrayList<NodeFilter>();
        this.filter.add(filter);
        this.aggregator = nodeAggregator;
    }
    public SecurityNodeConnection(String toEntity, String withField, String associationNode, List<NodeFilter> filter) {
        this.connectionTo = toEntity;
        this.fieldName = withField;
        this.associationNode = associationNode;
        this.filter = filter;
    }

    public SecurityNodeConnection(String connectionTo, String fieldName, String associationNode) {
        this.connectionTo = connectionTo;
        this.fieldName = fieldName;
        this.associationNode = associationNode;
        this.filter = null;
    }

    public SecurityNodeConnection(String connectionTo, String fieldName) {
        this.connectionTo = connectionTo;
        this.fieldName = fieldName;
    }

    public SecurityNodeConnection(String connectionTo, String fieldName, boolean isReferenceInSelf) {
        this.connectionTo = connectionTo;
        this.fieldName = fieldName;
        this.isReferenceInSelf = isReferenceInSelf;
    }

    public SecurityNodeConnection(String toEntity, EntityContextResolver resolver) {
        isResolver = true;
        this.resolver = resolver;
        connectionTo = toEntity;
    }

}