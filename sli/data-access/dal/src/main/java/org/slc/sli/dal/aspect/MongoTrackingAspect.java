package org.slc.sli.dal.aspect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import com.mongodb.DBCollection;

import org.apache.commons.lang3.tuple.Pair;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 *
 * @author dkornishev
 *
 */
@Aspect
public class MongoTrackingAspect {

    private static final Logger LOG = LoggerFactory.getLogger(MongoTrackingAspect.class);

    private ConcurrentMap<String, Pair<AtomicLong, AtomicLong>> stats = new ConcurrentHashMap<String, Pair<AtomicLong, AtomicLong>>();
    private static final long SLOW_QUERY_THRESHOLD = 20;  // ms

    @Around("call(* org.springframework.data.mongodb.core.MongoTemplate.*(..)) && !this(MongoTrackingAspect) && !within(org..*Test)")
    public Object track(ProceedingJoinPoint pjp) throws Throwable {

        MongoTemplate mt = (MongoTemplate) pjp.getTarget();

        String collection = "UNKNOWN";
        Object[] args = pjp.getArgs();
        if (args.length > 0 && args[0] instanceof String) {
            collection = (String) args[0];
        } else if (args.length > 1 && args[1] instanceof String) {
            collection = (String) args[1];
        } else if (args.length > 2 && args[2] instanceof String) {
            collection = (String) args[2];
        }

        if (collection.lastIndexOf("_") > -1) {
            collection = collection.substring(0, collection.lastIndexOf("_"));
        }

        if (pjp.getSignature().getName().equals("executeCommand")) {
            LOG.info("~~{} {}", pjp.getSourceLocation().getFileName(), pjp.getSourceLocation().getLine());
            LOG.info("{}", pjp.getArgs()[0]);
            collection = "EXEC-UNKNOWN";
        }

        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long elapsed = System.currentTimeMillis() - start;

        this.upCounts(mt.getDb().getName(), pjp.getSignature().getName(), collection, elapsed);
        logSlowQuery(elapsed, mt.getDb().getName(), pjp.getSignature().getName(), collection, pjp);

        return result;
    }

    @Around("call(* com.mongodb.DBCollection.*(..)) && !this(MongoTrackingAspect) && !within(org..*Test)")
    public Object trackDBCollection(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long elapsed = System.currentTimeMillis() - start;

        DBCollection col = (DBCollection) pjp.getTarget();

        this.upCounts(col.getDB().getName(), pjp.getSignature().getName(), col.getName(), elapsed);
        logSlowQuery(elapsed, col.getDB().getName(), pjp.getSignature().getName(), col.getName(), pjp);

        return result;
    }

    public Map<String, Pair<AtomicLong, AtomicLong>> getStats() {
        return this.stats;
    }

    public void reset() {
        this.stats = new ConcurrentHashMap<String, Pair<AtomicLong, AtomicLong>>();
    }

    private void upCounts(String db, String function, String collection, long elapsed) {
        stats.putIfAbsent(String.format("%s#%s#%s", db, function, collection), Pair.of(new AtomicLong(0), new AtomicLong(0)));

        Pair<AtomicLong, AtomicLong> pair = stats.get(String.format("%s#%s#%s", db, function, collection));

        pair.getLeft().incrementAndGet();
        pair.getRight().addAndGet(elapsed);
    }

    private void logSlowQuery(long elapsed, String db, String function, String collection, ProceedingJoinPoint pjp) {
        if (elapsed > SLOW_QUERY_THRESHOLD) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Slow query: %s#%s#%s (%d ms)", db, function, collection, elapsed));

            for (Object obj : pjp.getArgs()) {
                if (obj instanceof Query) {
                    sb.append("\nQUERY:" + ((Query) obj).getQueryObject().toString());
                } else if (obj instanceof Update) {
                    sb.append("\nUPDATE:" + ((Update) obj).getUpdateObject().toString());
                } else {
                    sb.append("\nMISC:" + obj.toString());
                }
            }
            sb.append("\n-----------------------------------------------\n");
            LOG.debug(sb.toString());
        }
    }
}
