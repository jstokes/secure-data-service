package org.slc.sli.ingestion.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slc.sli.ingestion.BatchJob;
import org.slc.sli.ingestion.Job;
import org.slc.sli.ingestion.handler.ReferenceResolutionHandler;
import org.slc.sli.ingestion.landingzone.IngestionFileEntry;
import org.slc.sli.ingestion.queues.MessageType;
import org.slc.sli.ingestion.util.BatchJobUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Processes a XML file
 * 
 * @author ablum
 * 
 */
@Component
public class XmlFileProcessor implements Processor {
    private Logger log = LoggerFactory.getLogger(XmlFileProcessor.class);
    
    @Autowired
    private ReferenceResolutionHandler referenceResolutionHandler;
    
    @Override
    public void process(Exchange exchange) throws Exception {
        Job batchJob = BatchJobUtils.getBatchJobUsingStateManager(exchange);
        
        try {
            for (IngestionFileEntry fe : batchJob.getFiles()) {
                
                referenceResolutionHandler.handle(fe, fe.getErrorReport());
                batchJob.getFaultsReport().append(fe.getFaultsReport());
                
            }
            exchange.getIn().setHeader("hasErrors", batchJob.getFaultsReport().hasErrors());
            exchange.getIn().setBody(batchJob, BatchJob.class);
            exchange.getIn().setHeader("IngestionMessageType", MessageType.XML_FILE_PROCESSED.name());
            
        } catch (Exception exception) {
            exchange.getIn().setHeader("ErrorMessage", exception.toString());
            exchange.getIn().setHeader("IngestionMessageType", MessageType.ERROR.name());
            log.error("Exception:", exception);
        }
    }
    
    public ReferenceResolutionHandler getReferenceResolutionHandler() {
        return referenceResolutionHandler;
    }
    
    public void setReferenceResolutionHandler(ReferenceResolutionHandler referenceResolutionHandler) {
        this.referenceResolutionHandler = referenceResolutionHandler;
    }
    
}
