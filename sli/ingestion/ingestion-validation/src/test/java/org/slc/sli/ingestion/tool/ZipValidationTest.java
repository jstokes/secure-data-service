package org.slc.sli.ingestion.tool;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slc.sli.ingestion.BatchJob;
import org.slc.sli.ingestion.FaultsReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/validatorContext.xml" })
public class ZipValidationTest {

    @Autowired
    private ZipValidation zipValidation;
    @Mock
    BatchJob job;
    @Mock
    FaultsReport fr;

    @Test
    public void testValidate() throws IOException {
        Resource zipFileResource = new ClassPathResource("Session1.zip");
        File zipFile = zipFileResource.getFile();
        Mockito.when(job.getErrorReport()).thenReturn(fr);
        File ctlFile = zipValidation.validate(zipFile, job);
        Assert.assertNotNull(ctlFile);
    }

}
