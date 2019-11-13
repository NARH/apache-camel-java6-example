package com.github.narh.test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ServiceStatus;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SampleXMLTest {

  Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private CamelContext camelContext;

  @EndpointInject(uri = "mock:result")
  private MockEndpoint resultEndpoint;

  @Test
  public void XMLファイルの読み込み() throws Exception {
    assertThat("ステータス", camelContext.getStatus(), is(ServiceStatus.Started));
    Thread.sleep(5000);
    MockEndpoint.assertIsSatisfied(camelContext);
    log.info("=== END ===");
  }
}
