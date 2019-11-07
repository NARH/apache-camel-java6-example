package com.github.narh.test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.ServiceStatus;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JsonTest {


	@Autowired
	private CamelContext camelContext;

	@EndpointInject(uri = "mock:result")
	private MockEndpoint resultEndpoint;

	@Produce(uri = "direct:in")
    protected ProducerTemplate template;

	@Test
	public void test起動() throws Exception {
      assertThat("ステータス", camelContext.getStatus(), is(ServiceStatus.Started));
      SampleBean bean = new SampleBean();
      bean.setName("David");
      bean.setValue(100);

//      resultEndpoint.expectedBodiesReceived("{\"name\":\"David\",\"value\":100}");
      resultEndpoint.expectedBodiesReceived(bean);
      template.sendBody(bean);
      MockEndpoint.assertIsSatisfied(camelContext);
	}
}