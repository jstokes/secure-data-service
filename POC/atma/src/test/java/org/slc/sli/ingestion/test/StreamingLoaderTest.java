package org.slc.sli.ingestion.test;

import java.io.FileReader;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slc.sli.dal.repository.MongoEntityRepository;
import org.slc.sli.domain.Entity;
import org.slc.sli.ingestion.streaming.StreamingLoader;
import org.slc.sli.validation.EntityValidationException;
import org.slc.sli.validation.EntityValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/ctx.xml", "/spring/db.xml" })
public class StreamingLoaderTest {

	public static final Logger log = LoggerFactory.getLogger(StreamingLoaderTest.class);
	
	@Resource(name = "mongoEntityRepository")
	private MongoEntityRepository repo;

	@Resource
	private StreamingLoader sp;

	@Before
	public void init() {
		repo.setValidator(new EntityValidator() {

			public boolean validate(Entity entity) throws EntityValidationException {
				return true;
			}

			public void setReferenceCheck(String referenceCheck) {
			}

			@Override
			public boolean validatePresent(Entity entity) throws EntityValidationException {
				return false;
			}
		});
	}

	@Test
	public void parseSmallData() throws Exception {
		// Warm up
		sp.process(new FileReader("src/test/resources/xml/small/InterchangeSection.xml"));
		StopWatch sw = new StopWatch();
		sw.start();
		sp.process(new FileReader("src/test/resources/xml/small/InterchangeSectionBig.xml"));
		sw.stop();
		log.info("Total time: "+sw.getTotalTimeMillis());
	}

	//@Test
	public void testParsing() throws Throwable {
		StopWatch sw = new StopWatch();
		sw.start();
		sp.process(new FileReader("src/test/resources/xml/interchange.xml"));
		sw.stop();

		log.info(""+sw.getTotalTimeMillis());

		sw = new StopWatch();
		sw.start();
		sp.process(new FileReader("src/test/resources/xml/interchange.xml"));
		sw.stop();

		log.info(""+sw.getTotalTimeMillis());

		sw = new StopWatch();
		sw.start();
		sp.process(new FileReader("src/test/resources/xml/interchange.xml"));
		sw.stop();

		log.info(""+sw.getTotalTimeMillis());
	}
}
