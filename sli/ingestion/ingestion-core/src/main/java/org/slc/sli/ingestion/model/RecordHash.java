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

package org.slc.sli.ingestion.model;

import java.util.Map;
import java.util.HashMap;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.DecoderException;
import org.slc.sli.ingestion.smooks.SliDeltaManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.index.Indexed;


/**
 *
 * @author unavani
 *
 * A RecordHash is calculated per entity, based on its fields in "neutral record" form,
 * early in the ingestion process -- prior to transformation -- to allow skipping of
 * processing records seen with the exact same content on the most recent upload.
 *
 */
public class RecordHash {

    // Logging
    private static final Logger LOG = LoggerFactory.getLogger(RecordHash.class);

	// These are the fields persisted in the MongoDB recordHash collection
	
    public String _id;			// Deterministic ID = function(natural key) = a (mostly) stable ID
    public String hash;			// Record hash = SHA-1(neutral record attributes + tenant ID)
    public long created;		// Unix time stamp of creation, never updated.
    public long updated;		// Unix time stamp of update, absent for first version
    public short version;			// Number of times updated after create (== zero-origin version number), absent for first version
    
    @Indexed
    public String tenantId;		// Tenant ID, for purge purposes, will be un-needed when record hash store is moved to tenant Db

    public RecordHash() {
        this._id = "";
        this.hash = "";
        this.created = 0;
        this.updated = 0;
        this.version = 0;
        this.tenantId = "";
    }
    
    /*
     * Conversions to/from compact key/value map, suitable for document-oriented databases, including
     * those DBMSes that benefit from very short field names.
     * 
     * Member      Map key  Always in Map?  Default if absent
     * ---------   -------  --------------  -----------------
     * _id         _id      Yes
     * hash        h        Yes
     * created     c        Yes
     * updated     u        No              <created>
     * version     v        No              0
     * tenantID    t        No              ""
     */
    
    // Construct from compact, document-oriented database key/value map
    public RecordHash(Map<String, Object> map) {
    	this();
    	if ( null == map )
    		return;
    	
    	this._id = Binary2Hex((byte[]) map.get("_id"));
    	this.hash = Binary2Hex((byte[]) map.get("h"));
    	this.created = ((Long) map.get("c")).longValue();
    	
    	Long updated = (Long) map.get("u");
    	if ( null == updated )
    		this.updated = this.created;
    	else
    		this.updated = updated.longValue();
    	
    	Integer version = (Integer) map.get("v");
    	if ( null == version )
    		this.version = 0;
    	else
    		this.version = version.shortValue();
    	
    	String tenantId = (String) map.get("t");
    	if ( null == tenantId )
    		this.tenantId = "";
    	else
    		this.tenantId = tenantId;
    }
    
    // Return key/value map, omitting defaults for some fields
    public Map<String, Object> toKVMap() {
    	Map<String, Object> m = new HashMap<String, Object>();
    	m.put("_id", Hex2Binary(this._id));
    	m.put("h", Hex2Binary(this.hash));
    	m.put("c", new Long(this.created));
    	if ( this.updated != this.created )
    		m.put("u", new Long(this.updated));
    	if ( this.version != 0 )
    		m.put("v", new Short(this.version));
    	if ( null != this.tenantId && this.tenantId.length() > 0 )
    		m.put("t", this.tenantId);
    	return m;
    }
    
    // Convert binary bytes to Hex string.  E.g. 20-bytes SHA hash to 40-hex-char string
    public static String Binary2Hex(byte[] bytes) {
    	return new String(new Hex().encode(bytes));
    }
    
    // Convert DiD or hash to 20-byte binary form
    public static byte[] Hex2Binary(String id) {
    	// Take first 40 hex digits of DiD, lopping off the trailing "_id"
    	try {
    		return new Hex().decode(id.substring(0, 40).getBytes());
    	}
    	catch( DecoderException e ) {
    		LOG.warn("Cannot convert hex hash or ID to binary: '" + id + "'");
    		LOG.warn(e.getMessage());
    		return null;
    	}
    }
}
