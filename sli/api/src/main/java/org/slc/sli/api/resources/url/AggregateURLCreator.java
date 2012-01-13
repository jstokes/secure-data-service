package org.slc.sli.api.resources.url;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriInfo;

import org.slc.sli.api.representation.EmbeddedLink;
import org.slc.sli.api.resources.util.ResourceUtil;
import org.slc.sli.domain.Entity;

import static org.slc.sli.api.resources.util.ResourceConstants.ENTITY_EXPOSE_TYPE_AGGREGATIONS;
import static org.slc.sli.api.resources.util.ResourceConstants.ENTITY_TYPE_AGGREGATION;
import static org.slc.sli.api.resources.util.ResourceConstants.ENTITY_BODY_GROUPBY;

/**
 * Creates URL sets for aggregations.
 * @author srupasinghe
 *
 */
public class AggregateURLCreator extends URLCreator {

	/**
	 * Returns a list of aggregate links that matches the parameters passed in 
	 */
	@Override
	public List<EmbeddedLink> getUrls(final UriInfo uriInfo, Map<String, String> params) {
		List<EmbeddedLink> results = new ArrayList<EmbeddedLink>();
		        
		//TODO - testing only
		/*Map<String, String> p = new java.util.HashMap<String, String>();
		p.put("sex", "Male");
		
        //get the aggregations that match the query params
        Iterable<Entity> aggregations = repo.findByFields("student", p);
      		
        //iterate through the aggregations and build the embedded links list
        for(Entity e : aggregations) {
        	results.add(new EmbeddedLink(ResourceUtil.LINKS, e.getType(), ResourceUtil.getURI(uriInfo, "students", e.getEntityId()).toString()));        			
        }*/
		
        //get the aggregations that match the query params
        Iterable<Entity> aggregations = repo.findByFields(ENTITY_TYPE_AGGREGATION, convertParamsToEnitiyFormat(params));	
        
        //iterate through the aggregations and build the embedded links list
        for(Entity e : aggregations) {
        	results.add(new EmbeddedLink(ResourceUtil.LINKS, e.getType(), ResourceUtil.getURI(uriInfo, ENTITY_EXPOSE_TYPE_AGGREGATIONS, e.getEntityId()).toString()));        			
        }

        
		return results;
	}
	
	/**
	 * Returns a map of modified keys that matches the Entity requirements
	 * @param params
	 * @return
	 */
	protected Map<String, String> convertParamsToEnitiyFormat(Map<String, String> params) {
		Map<String, String> results = new HashMap<String, String>();
		
		for(Map.Entry<String, String> e : params.entrySet()) {
			results.put(ENTITY_BODY_GROUPBY + "." + e.getKey(), e.getValue());
		}
		
		return results;
	}
	
}
