package com.github.narh.test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.ArrayList;

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
public class SampleXMLTest2 {

  Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private CamelContext camelContext;

  @EndpointInject(uri = "mock:result")
  private MockEndpoint resultEndpoint;

  @Test
  public void XMLファイルの読み込み() throws Exception {
    assertThat("ステータス", camelContext.getStatus(), is(ServiceStatus.Started));
    resultEndpoint.expectedMessageCount(1);
    resultEndpoint.assertIsSatisfied();

    RootBean root = new RootBean();
    root.setSamples(new ArrayList<SampleBean>());
    root.getSamples().add(new SampleBean());
    root.getSamples().add(new SampleBean());

    root.getSamples().get(0).setName("foo");
    root.getSamples().get(0).setValue(100);
    root.getSamples().get(1).setName("bar");
    root.getSamples().get(1).setValue(200);

    resultEndpoint.expectedBodiesReceived(root);
    log.info("=== END ===");
  }
}
