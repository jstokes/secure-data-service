package org.slc.sli.aspect;

import org.slc.sli.common.util.logging.SecurityEvent;

/**
 * Logging "super class" injected into all sli classes via aspectj
 * inter-type declaration.  Java doesn't have mixins :(
 * 
 * @author dkornishev
 *
 */
public interface LoggerCarrier {
    public void audit(SecurityEvent event);
}
