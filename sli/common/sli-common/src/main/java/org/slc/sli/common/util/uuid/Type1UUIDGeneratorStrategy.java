package org.slc.sli.common.util.uuid;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Generates Type 1 (time-based) UUIDs.
 *
 * @author smelody
 *
 */
@Component
@Qualifier("type1UUIDGeneratorStrategy")
public class Type1UUIDGeneratorStrategy implements UUIDGeneratorStrategy {

    private TimeBasedGenerator generator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());

    /**
     * Generate a type 1 random UUID.
     */
    @Override
    public String randomUUID() {
        return generator.generate().toString();
    }
}
