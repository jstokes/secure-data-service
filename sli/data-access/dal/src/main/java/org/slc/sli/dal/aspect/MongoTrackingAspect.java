package org.slc.sli.dal.aspect;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.tuple.Pair;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 
 * @author dkornishev
 * 
 */
@Aspect
public class MongoTrackingAspect {
    
    private Map<String, Pair<AtomicLong, AtomicLong>> stats = new HashMap<String, Pair<AtomicLong, AtomicLong>>();
    
    @Around("call(* org.springframework.data.mongodb.core.MongoTemplate.*(..))")
    public Object track(ProceedingJoinPoint pjp) throws Throwable {
        
        if (stats.get(pjp.getSignature().getName()) == null) {
            stats.put(pjp.getSignature().getName(), Pair.of(new AtomicLong(0), new AtomicLong(0)));
        }
        
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long elapsed = System.currentTimeMillis() - start;
        
        Pair<AtomicLong, AtomicLong> pair = stats.get(pjp.getSignature().getName());
        pair.getLeft().incrementAndGet();
        pair.getRight().addAndGet(elapsed);
        
        return result;
    }
    
    public Map<String, Pair<AtomicLong, AtomicLong>> getStats() {
        return this.stats;
    }
    
}
