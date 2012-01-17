package org.slc.sli.ingestion.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.slc.sli.ingestion.BatchJob;

/**
 *
 * @author jsa
 *
 */
public class IngestionRouteBuilderTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-test.xml");
        return context;
    }

    @Test
    public void testBatchJobWithFaultsIsNotProcessed() throws Exception {

        // create a mock endpoint against which we can set expectations
        // it will be swapped in for the seda:acceptedJobs queue later
        final MockEndpoint mockAcceptedJobsEndpoint = getMockEndpoint("mock:acceptedJobs");

        final MockEndpoint mockStopEndpoint = getMockEndpoint("mock:stop");

        // look up a specific route by id and get a reference to it
        RouteDefinition route = context.getRouteDefinition("jobDispatch");

        // use adviceWith to override the route config, intercepting messages
        // to the acceptedJobs queue and instead diverting them to our mock
        // endpoint
        route.adviceWith(context, new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint("seda:acceptedJobs").skipSendToOriginalEndpoint().to(mockAcceptedJobsEndpoint);
                interceptSendToEndpoint("direct:stop").skipSendToOriginalEndpoint().to(mockStopEndpoint);
            }

        });

        // we expect the message to hit the mockStopEndpoint and not mockAccepted
        mockAcceptedJobsEndpoint.expectedMessageCount(0);
        mockStopEndpoint.expectedMessageCount(1);

        // send job to aseembled jobs with hasErrors header
        BatchJob job = BatchJob.createDefault();
        template.sendBodyAndHeader("seda:assembledJobs", job, "hasErrors", true);

        // we are sending to SEDA which is a different thread so there is a race condition when we
        // check for our expectation
        // sleep so that we know parallel thread executes before we check
        Thread.sleep(500);

        // check it did NOT make it downstream to acceptedJobs q but did make it to stop q
        mockAcceptedJobsEndpoint.assertIsSatisfied();
        mockStopEndpoint.assertIsSatisfied();
    }

    @Test
    public void testBatchJobWithoutFaultsIsProcessed() throws Exception {

        // TODO boilerplate code - looks simple enough to factor out, but
        // so far all attempts have resulted in obscure camel/spring config
        // problems. needs further investigation.

        // create a mock endpoint against which we can set expectations
        // it will be swapped in for the seda:acceptedJobs queue later
        final MockEndpoint mockAcceptedJobsEndpoint = getMockEndpoint("mock:acceptedJobs");

        final MockEndpoint mockStopEndpoint = getMockEndpoint("mock:stop");

        // look up a specific route by id and get a reference to it
        RouteDefinition route = context.getRouteDefinition("jobDispatch");

        // use adviceWith to override the route config, intercepting messages
        // to the acceptedJobs queue and instead diverting them to our mock
        // endpoint
        route.adviceWith(context, new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint("seda:acceptedJobs").skipSendToOriginalEndpoint().to(mockAcceptedJobsEndpoint);
                interceptSendToEndpoint("direct:stop").skipSendToOriginalEndpoint().to(mockStopEndpoint);
            }

        });

        // expect 1 message (BatchJob) to pass to the acceptedJobs q and 0 to stop q
        mockAcceptedJobsEndpoint.expectedMessageCount(1);
        mockStopEndpoint.expectedMessageCount(0);

        // send job to aseembled jobs with hasErrors header
        BatchJob job = BatchJob.createDefault();
        template.sendBodyAndHeader("seda:assembledJobs", job, "hasErrors", false);

        // we are sending to SEDA which is a different thread so there is a race condition when we
        // check for our expectation
        // sleep so that we know parallel thread executes before we check
        Thread.sleep(500);

        // check the job was passed along to the acceptedJobs queue
        mockAcceptedJobsEndpoint.assertIsSatisfied();
        mockStopEndpoint.assertIsSatisfied();
    }

}
