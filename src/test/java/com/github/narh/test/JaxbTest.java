package com.github.narh.test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.bind.JAXB;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaxbTest {

  Logger log = LoggerFactory.getLogger(getClass());

  @Test
  public void test() throws Exception {
    RootBean root = new RootBean();
    root.setSamples(new ArrayList<SampleBean>());
    root.getSamples().add(new SampleBean());
    root.getSamples().add(new SampleBean());

    root.getSamples().get(0).setName("foo-1");
    root.getSamples().get(0).setValue(100);
    root.getSamples().get(1).setName("bar-2");
    root.getSamples().get(1).setValue(200);

    StringWriter writer = new StringWriter();
    JAXB.marshal(root, writer);
    log.info("{}", writer);

    StringReader reader = new StringReader(writer.toString());
    RootBean result = JAXB.unmarshal(reader, RootBean.class);
    assertThat(result.getSamples().size(), is(2));
    assertThat(result.getSamples().get(0).getName(), is("foo-1"));
    assertThat(result.getSamples().get(0).getValue(), is(100));
    assertThat(result.getSamples().get(1).getName(), is("bar-2"));
    assertThat(result.getSamples().get(1).getValue(), is(200));
  }
}
