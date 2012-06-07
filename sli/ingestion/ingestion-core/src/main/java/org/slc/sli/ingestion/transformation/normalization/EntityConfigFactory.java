package org.slc.sli.ingestion.transformation.normalization;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import org.slc.sli.ingestion.transformation.EdFi2SLITransformer;

/**
 * Factory for entity configurations
 *
 * @author okrook
 *
 */
public class EntityConfigFactory implements ResourceLoaderAware {
    private static final String CONFIG_EXT = ".json";
    private static final EntityConfig NOT_FOUND = null;
    private static final Logger LOG = LoggerFactory.getLogger(EdFi2SLITransformer.class);


    private String searchPath;
    private ResourceLoader resourceLoader;

    private Map<String, EntityConfig> entityConfigurations = new HashMap<String, EntityConfig>();

    public synchronized EntityConfig getEntityConfiguration(String entityType) {
        if (!entityConfigurations.containsKey(entityType)) {
            InputStream configIs = null;
            try {
                Resource config = resourceLoader.getResource(searchPath + entityType + CONFIG_EXT);

                if (config.exists()) {
                    configIs = config.getInputStream();
                    entityConfigurations.put(entityType, EntityConfig.parse(configIs));
                } else {
                    LOG.warn("no config found for entity type {}", entityType);
                }
            } catch (IOException e) {
                LOG.error("Error loading entity type " + entityType, e);
            } finally {
                IOUtils.closeQuietly(configIs);
            }
        }

        return entityConfigurations.get(entityType);
    }

    /**
     * @return the searchPath
     */
    public String getSearchPath() {
        return searchPath;
    }

    /**
     * @param searchPath the searchPath to set
     */
    public void setSearchPath(String searchPath) {
        this.searchPath = searchPath;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
