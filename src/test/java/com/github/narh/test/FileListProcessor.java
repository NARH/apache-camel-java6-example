package com.github.narh.test;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileListProcessor implements Processor {

  Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public void process(Exchange exchange) throws Exception {
    String body = exchange.getIn().getBody(String.class);
    log.info("---> body: {}", body);
    Map<String, Object> headers = exchange.getIn().getHeaders();
    log.info("---> fileName: {}", headers.get("CamelFileAbsolutePath"));
  }

}
