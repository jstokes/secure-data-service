package org.slc.sli.api.config;

import org.apache.commons.lang3.StringUtils;

import org.slc.sli.api.service.AssociationService;
import org.slc.sli.api.service.BasicAssocService;
import org.slc.sli.api.service.Treatment;
import org.slc.sli.api.service.Validator;
import org.slc.sli.dal.repository.EntityRepository;

/**
 * Definition of an association resource
 * 
 * @author nbrown
 * 
 */
public final class AssociationDefinition extends EntityDefinition {

    private final EntityDefinition sourceEntity;
    private final EntityDefinition targetEntity;
    private final String relNameFromSource;
    private final String relNameFromTarget;
    private final String sourceLink;
    private final String targetLink;
    private final String sourceKey;
    private final String targetKey;
    private final String sourceHopLink;
    private final String targetHopLink;
    
    private AssociationDefinition(String type, String resourceName, String collectionName, AssociationService service,
            EntityInfo source, EntityInfo target) {
        super(type, resourceName, collectionName, service);
        this.sourceEntity = source.getDefn();
        this.targetEntity = target.getDefn();
        this.relNameFromSource = source.getLinkToAssociation();
        this.relNameFromTarget = target.getLinkToAssociation();
        this.sourceLink = source.getLinkName();
        this.targetLink = target.getLinkName();
        this.sourceHopLink = source.getHopLinkName();
        this.targetHopLink = target.getHopLinkName();
        this.sourceKey = source.getKey();
        this.targetKey = target.getKey();
    }
    
    /**
     * The source of the association
     * 
     * @return
     */
    public EntityDefinition getSourceEntity() {
        return sourceEntity;
    }
    
    /**
     * The target of the association
     * 
     * @return
     */
    public EntityDefinition getTargetEntity() {
        return targetEntity;
    }
    
    /**
     * Gets the name of the relationship as its called when coming from the source
     * 
     * @return
     */
    public String getRelNameFromSource() {
        return relNameFromSource;
    }
    
    /**
     * Gets the name of the relationship as its called when coming from the target
     * 
     * @return
     */
    public String getRelNameFromTarget() {
        return relNameFromTarget;
    }

    /**
     * The label for the link to the source
     * 
     * @return
     */
    public String getSourceLink() {
        return sourceLink;
    }

    /**
     * The label for the link from the target directly to the source
     *
     * @return 
     */
    public String getHoppedTargetLink() {
        return targetHopLink;
    }
    
    /**
     * The label for the link from the target directly to the source
     *
     * @return 
     */
    public String getHoppedSourceLink() {
        return sourceHopLink;
    }
    
    /**
     * The label for the link to the target
     * 
     * @return
     */
    public String getTargetLink() {
        return targetLink;
    }
    
    /**
     * The key for the target
     * 
     * @return
     */
    public String getSourceKey() {
        return sourceKey;
    }
    
    /**
     * The key for the source
     * 
     * @return
     */
    public String getTargetKey() {
        return targetKey;
    }

    @Override
    public AssociationService getService() {
        return (AssociationService) super.getService();
    }
    
    /**
     * Create a builder for an entity definition with the same collection and resource name
     * 
     * @param entityName
     *            the collection/resource name
     * @return the builder to create the entity definition
     */
    public static AssocBuilder makeAssoc(String entityName) {
        return new AssocBuilder(entityName);
    }
    
    /**
     * Create a builder for an entity definition
     * 
     * @param collectionName
     *            the name of the entity in the db
     * @param resourceName
     *            the name of the entity in the ReST uri
     * @return the builder to create the entity definition
     */
    public static AssocBuilder makeAssoc(String type, String collectionName, String resourceName) {
        return new AssocBuilder(type);
    }
    
    /**
     * Fluent builder for AssocBuilder.
     */
    public static class AssocBuilder extends EntityDefinition.Builder {
        private EntityInfo source;
        private EntityInfo target;
        private String relNameFromSource;
        private String relNameFromTarget;
        
        /**
         * Create a builder for an association definition
         * 
         * @param collectionName
         *            the name of the association in the db
         * @param resourceName
         *            the name of the association in the ReST uri
         */
        public AssocBuilder(String type) {
            super(type);
        }
        
        /**
         * Sets the source definition. The link will get name get{type} and the id will be {type}Id
         * 
         * @param source
         *            the source of the association
         * @return the builder
         */
        public AssocBuilder from(EntityDefinition source) {
            this.source = new EntityInfo(source, "get" + StringUtils.capitalize(source.getType()), source.getType()
                    + "Id");
            return this;
        }
        
        /**
         * Sets the target definition. The link will get name get{Type} and the id will be {type}Id
         * 
         * @param target
         *            the target definition
         * @return the builder
         */
        public AssocBuilder to(EntityDefinition target) {
            this.target = new EntityInfo(target, "get" + StringUtils.capitalize(target.getType()), target.getType()
                    + "Id");
            return this;
        }
        
        /**
         * Sets the source definition
         * 
         * @param source
         *            the source of the association
         * @param sourceLink
         *            the name of the link
         * @param hopLink
         *            the name of the link from the target to the source
         * @return the builder
         */
        public AssocBuilder from(EntityDefinition source, String sourceLink, String hopLink) {
            this.source = new EntityInfo(source, sourceLink, hopLink, source.getType() + "Id");
            return this;
        }
        
        /**
         * Sets the source definition
         * 
         * @param source
         *            the source of the association
         * @param sourceLink
         *            the name of the link
         * @param hopLink
         *            the name of the link from the target to the source
         * @param sourceKey
         *            the key to look up the source with
         * @return the builder
         */
        public AssocBuilder from(EntityDefinition source, String sourceLink, String hopLink, String sourceKey) {
            this.source = new EntityInfo(source, sourceLink, hopLink, sourceKey);
            return this;
        }
        
        /**
         * Sets the target definition
         * 
         * @param target
         *            the target definition
         * @param targetLink
         *            the name of the link
         * @param hopLink
         *            the name of the link from the source to the target
         * @return the builder
         */
        public AssocBuilder to(EntityDefinition target, String targetLink, String hopLink) {
            this.target = new EntityInfo(target, targetLink, hopLink, target.getType() + "Id");
            return this;
        }
        
        /**
         * Sets the target definition
         * 
         * @param target
         *            the target definition
         * @param targetLink
         *            the name of the link
         * @param hopLink
         *            the name of the link from the source to the target
         * @param targetKey
         *            the key to look up the target with
         * @return the builder
         */
        public AssocBuilder to(EntityDefinition target, String targetLink, String hopLink, String targetKey) {
            this.target = new EntityInfo(target, targetLink, hopLink, targetKey);
            return this;
        }
        
        /**
         * The name of the link from either entity to the relationship
         * 
         * @param relName
         * @return
         */
        public AssocBuilder called(String relName) {
            this.relNameFromSource = relName;
            this.relNameFromTarget = relName;
            return this;
        }

        /**
         * The name of the link on the source entity to the relationship
         * 
         * @param relName
         * @return
         */
        public AssocBuilder calledFromSource(String relName) {
            this.relNameFromSource = relName;
            return this;
        }
        
        /**
         * The name of the link of the target entity of the relationship
         * 
         * @param relName
         * @return
         */
        public AssocBuilder calledFromTarget(String relName) {
            this.relNameFromTarget = relName;
            return this;
        }

        @Override
        public AssocBuilder withTreatments(Treatment... treatments) {
            super.withTreatments(treatments);
            return this;
        }
        
        @Override
        public AssocBuilder withValidators(Validator... validators) {
            super.withValidators(validators);
            return this;
        }
        
        @Override
        public AssocBuilder storeAs(String collectionName) {
            super.storeAs(collectionName);
            return this;
        }
        
        @Override
        public AssocBuilder storeIn(EntityRepository repo) {
            super.storeIn(repo);
            return this;
        }
        
        @Override
        public AssocBuilder exposeAs(String resourceName) {
            super.exposeAs(resourceName);
            return this;
        }
        
        @Override
        public AssociationDefinition build() {
            BasicAssocService service = new BasicAssocService(getCollectionName(), getTreatments(), getValidators(),
                    getRepo(), source, target);
            source.setLinkToAssociation(this.relNameFromSource);
            target.setLinkToAssociation(this.relNameFromTarget);
            AssociationDefinition associationDefinition = new AssociationDefinition(getType(), getResourceName(),
                    getCollectionName(), service, source, target);
            service.setDefn(associationDefinition);
            return associationDefinition;
        }
    }
    
    /**
     * Holder class for entity information
     * Mostly so checkstyle doesn't whine about private association definition methods having too
     * many parameters
     * 
     * @author nbrown
     * 
     */
    public static class EntityInfo {
        private final EntityDefinition defn;
        private final String linkName;
        private final String key;
        private final String hopLinkName;
        private String linkToAssociation;
        
        public EntityInfo(EntityDefinition defn, String linkName, String hopLinkName, String key) {
            super();
            this.defn = defn;
            this.linkName = linkName;
            this.hopLinkName = hopLinkName;
            this.key = key;
        }
        
        public EntityInfo(EntityDefinition defn, String linkName, String key) {
            super();
            this.defn = defn;
            this.linkName = linkName;
            this.hopLinkName = linkName;
            this.key = key;
        }
        
        public EntityDefinition getDefn() {
            return defn;
        }
        
        public String getLinkName() {
            return linkName;
        }

        public String getHopLinkName() {
            return hopLinkName;
        }
        
        public String getKey() {
            return key;
        }
        
        public void setLinkToAssociation(String linkToAssociation) {
            this.linkToAssociation = linkToAssociation;
        }

        public String getLinkToAssociation() {
            return linkToAssociation;
        }

    }
}
