package org.slc.sli.ingestion.dal;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * An implementation of PathMatchingResourcePatternResolver used to resolve the directory structure that holds index information
 * @author ablum
 *
 */

public class IndexResourcePatternResolver extends PathMatchingResourcePatternResolver {

    private static final Logger LOG = LoggerFactory.getLogger(IndexResourcePatternResolver.class);

    private static final String INDEX_FILES = "/**/*.json";

    public List<MongoIndexConfig> findAllIndexes(String rootDir) {

        List<MongoIndexConfig> indexConfigs = new ArrayList<MongoIndexConfig>();

        try {
            Resource[] collectionDirectories = findPathMatchingResources("classpath:" + rootDir + INDEX_FILES);

            for (Resource collectionDirectory : collectionDirectories) {
                InputStream inputStream = collectionDirectory.getInputStream();
                indexConfigs.add(MongoIndexConfig.parse(inputStream));
                inputStream.close();
            }

        } catch (IOException e) {
            LOG.error("Path to index directory does not exist: " + rootDir);
        }

        return indexConfigs;
    }
}
