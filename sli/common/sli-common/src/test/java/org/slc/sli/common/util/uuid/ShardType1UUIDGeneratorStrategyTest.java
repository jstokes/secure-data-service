package org.slc.sli.common.util.uuid;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author wscott
 */
public class ShardType1UUIDGeneratorStrategyTest {
    @Test
    public void TestShardType1UUIDGenerator() {
        ShardType1UUIDGeneratorStrategy uuidGen = new ShardType1UUIDGeneratorStrategy();
        String uuid = uuidGen.randomUUID();
        assertNotNull(uuid);
        assertEquals('1', uuid.charAt(22)); //make sure we generated a type1 uuid
        assertEquals(43, uuid.length()); // 7 chars for 'yyyyrr-', 36 chars for type 1 uuid
    }
}
