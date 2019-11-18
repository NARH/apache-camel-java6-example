package com.github.narh.test;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAggregateStrategy implements AggregationStrategy {
  Logger log = LoggerFactory.getLogger(getClass());
  @Override
  public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
    if (oldExchange == null) return newExchange;
    StringBuilder stb = new StringBuilder();
    stb.append(oldExchange.getIn().getBody(String.class));
    stb.append("\r\n");
    stb.append(newExchange.getIn().getBody(String.class));
    oldExchange.getIn().setBody(stb.toString());
    oldExchange.getIn().setHeader(Exchange.FILE_NAME, oldExchange.getIn().getHeader(Exchange.FILE_NAME));
    if(log.isDebugEnabled()) log.debug("集約 {}", oldExchange.getIn().getBody(String.class));
    return oldExchange;
  }

}
