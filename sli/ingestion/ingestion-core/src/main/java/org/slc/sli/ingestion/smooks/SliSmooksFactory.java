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

package org.slc.sli.ingestion.smooks;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.milyn.Smooks;
import org.milyn.delivery.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.xml.sax.SAXException;

import org.slc.sli.common.util.uuid.DeterministicUUIDGeneratorStrategy;
import org.slc.sli.ingestion.FileType;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.ResourceWriter;
import org.slc.sli.ingestion.dal.NeutralRecordMongoAccess;
import org.slc.sli.ingestion.landingzone.IngestionFileEntry;
import org.slc.sli.ingestion.model.da.BatchJobDAO;
import org.slc.sli.ingestion.validation.ErrorReport;

import scala.actors.threadpool.Arrays;

/**
 * Factory class for Smooks
 * 
 * @author dduran
 * 
 */
public class SliSmooksFactory {
    
    private Map<FileType, SliSmooksConfig> sliSmooksConfigMap;
    private String beanId;
    private NeutralRecordMongoAccess nrMongoStagingWriter;

    @Autowired
    public BatchJobDAO batchJobDAO;

    @Value("${sli.ingestion.recordLevelDeltaEntities}")
    private String recordLevelDeltaEnabledEntityNames;
    public Smooks createInstance(IngestionFileEntry ingestionFileEntry, ErrorReport errorReport, String tenantId,
            DeterministicUUIDGeneratorStrategy deterministicUUIDGeneratorStrategy) throws IOException, SAXException {
        
        FileType fileType = ingestionFileEntry.getFileType();
        SliSmooksConfig sliSmooksConfig = sliSmooksConfigMap.get(fileType);
        if (sliSmooksConfig != null) {
            
            return createSmooksFromConfig(sliSmooksConfig, errorReport, ingestionFileEntry.getBatchJobId(),
                    ingestionFileEntry, tenantId, deterministicUUIDGeneratorStrategy);
            
        } else {
            errorReport.fatal("File type not supported : " + fileType, SliSmooksFactory.class);
            throw new IllegalArgumentException("File type not supported : " + fileType);
        }
    }
    
    private Smooks createSmooksFromConfig(SliSmooksConfig sliSmooksConfig, ErrorReport errorReport, String batchJobId,
            IngestionFileEntry fe, String tenantId,
            DeterministicUUIDGeneratorStrategy deterministicUUIDGeneratorStrategy) throws IOException, SAXException {
        
        Smooks smooks = new Smooks(sliSmooksConfig.getConfigFileName());
        
        // based on target selectors for this file type, add visitors
        List<String> targetSelectorList = sliSmooksConfig.getTargetSelectors();
        if (targetSelectorList != null) {
            
            // just one visitor instance that can be added with multiple target selectors
            Visitor smooksEdFiVisitor = SmooksEdFiVisitor.createInstance(beanId, batchJobId, errorReport, fe, tenantId,
                    deterministicUUIDGeneratorStrategy);
            
            ((SmooksEdFiVisitor) smooksEdFiVisitor).setNrMongoStagingWriter(nrMongoStagingWriter);
            ((SmooksEdFiVisitor) smooksEdFiVisitor).setBatchJobDAO(batchJobDAO);

            HashSet<String> recordLevelDeltaEnabledEntities = new HashSet<String>();
            recordLevelDeltaEnabledEntities.addAll(Arrays.asList(recordLevelDeltaEnabledEntityNames.split(",")));
            ((SmooksEdFiVisitor) smooksEdFiVisitor).setRecordLevelDeltaEnabledEntities(recordLevelDeltaEnabledEntities);

            ((SmooksEdFiVisitor) smooksEdFiVisitor).setBatchJobDAO(batchJobDAO);

            HashSet<String> recordLevelDeltaEnabledEntities = new HashSet<String>();
            recordLevelDeltaEnabledEntities.addAll(Arrays.asList(recordLevelDeltaEnabledEntityNames.split(",")));
            ((SmooksEdFiVisitor) smooksEdFiVisitor).setRecordLevelDeltaEnabledEntities(recordLevelDeltaEnabledEntities);

            for (String targetSelector : targetSelectorList) {
                smooks.addVisitor(smooksEdFiVisitor, targetSelector);
            }
        }
        return smooks;
    }
    
    public void setSliSmooksConfigMap(Map<FileType, SliSmooksConfig> sliSmooksConfigMap) {
        this.sliSmooksConfigMap = sliSmooksConfigMap;
    }
    
    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }
    
    public void setNrMongoStagingWriter(ResourceWriter<NeutralRecord> nrMongoStagingWriter) {
        this.nrMongoStagingWriter = (NeutralRecordMongoAccess) nrMongoStagingWriter;
    }

    public void setBatchJobDAO(BatchJobDAO batchJobDAO) {
        this.batchJobDAO = batchJobDAO;
    }
}
