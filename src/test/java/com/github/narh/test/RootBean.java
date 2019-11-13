package com.github.narh.test;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.util.StringUtils;

@XmlRootElement(name="root")
public class RootBean implements Serializable {

  private List<SampleBean> samples;

  @XmlElementWrapper(name="samples")
  public List<SampleBean> getSamples() {
    return samples;
  }

  public void setSamples(List<SampleBean> samples) {
    this.samples = samples;
  }

  @Override
  public String toString() {
    return (null != samples) ? StringUtils.arrayToCommaDelimitedString(samples.toArray()) : "";
  }
}
