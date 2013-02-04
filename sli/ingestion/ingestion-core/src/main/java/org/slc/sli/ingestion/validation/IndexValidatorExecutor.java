package org.slc.sli.ingestion.validation;

import javax.annotation.PostConstruct;


import org.slc.sli.ingestion.reporting.ReportStats;
import org.slc.sli.ingestion.reporting.Source;
import org.slc.sli.ingestion.reporting.impl.JobSource;
import org.slc.sli.ingestion.reporting.impl.LoggingMessageReport;
import org.slc.sli.ingestion.reporting.impl.SimpleReportStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class IndexValidatorExecutor {
	
    private static final Logger LOG = LoggerFactory.getLogger(IndexValidatorExecutor.class);
    
    @Autowired
    private Validator<?> systemValidator;
    
    @Autowired
    private LoggingMessageReport loggingMessageReport;
    
	@PostConstruct
	public void init() {

		loggingMessageReport.setLogger(LOG);
		Source source = new JobSource(null);
		ReportStats reportStats = new SimpleReportStats();

		boolean indexValidated = systemValidator.isValid(null,
				loggingMessageReport, reportStats, source);
		indexValidated = false;
		if (!indexValidated) {
			throw new RuntimeException(
					"Indexes could not be verified, check the index file configurations are set");
		}

	}
}
