package org.slc.sli.bulk.extract.lea;

import java.util.Iterator;
import java.util.Set;

import org.slc.sli.bulk.extract.extractor.EntityExtractor;
import org.slc.sli.bulk.extract.util.EdOrgExtractHelper;
import org.slc.sli.common.constants.EntityNames;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;

public class StudentSchoolAssociationExtractor implements EntityExtract {
    private ExtractFileMap map;
    private EntityExtractor extractor;
    private EntityToEdOrgCache cache;
    private EntityToEdOrgCache graduationPlanCache;
    private Repository<Entity> repo;
    private EdOrgExtractHelper helper;
    
    public StudentSchoolAssociationExtractor(EntityExtractor extractor, ExtractFileMap map, Repository<Entity> repo,
                                             EntityToEdOrgCache cache, EdOrgExtractHelper helper) {
        this.extractor = extractor;
        this.map = map;
        this.cache = cache;
        this.graduationPlanCache = new EntityToEdOrgCache();
        this.repo = repo;
        this.helper = helper;
    }

	
	@Override
    public void extractEntities(EntityToEdOrgCache entityCache) {
        helper.logSecurityEvent(map.getEdOrgs(), EntityNames.STUDENT_SCHOOL_ASSOCIATION, this.getClass().getName());
        Iterator<Entity> cursor = repo.findEach("studentSchoolAssociation", new NeutralQuery());
        while(cursor.hasNext()) {
        	Entity ssa = cursor.next();
            String graduationPlanId = (String) ssa.getBody().get("graduationPlanId");
        	Set<String> edOrgs = cache.getEntriesById((String) ssa.getBody().get("studentId"));
        	for (String edOrg : edOrgs) {
        		extractor.extractEntity(ssa, map.getExtractFileForEdOrg(edOrg), "studentSchoolAssociation");

                graduationPlanCache.addEntry(graduationPlanId, edOrg);

            }
        }
	}

    public EntityToEdOrgCache getGraduationPlanCache() {
        return graduationPlanCache;
    }

}
