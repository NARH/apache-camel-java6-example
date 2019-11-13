package com.github.narh.test;

import java.io.StringReader;

import javax.xml.bind.JAXB;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JAXBProcessor<T> implements Processor {

  private Logger log = LoggerFactory.getLogger(getClass());

  private Class<T> clazz;

  public JAXBProcessor(Class<T> clazz) {
    this.clazz = clazz;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    String body = exchange.getIn().getBody(String.class);
    StringReader reader = new StringReader(body);
    T obj = JAXB.unmarshal(reader, clazz);
    exchange.getIn().setBody(obj);
    log.info("{}, unmarsharl obj is {} ", exchange.getFromRouteId(), exchange.getIn().getBody().toString());
  }

}
