package com.github.narh.test;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

public class SampleBean implements Serializable {

	private String name;

	private Integer value;

  @XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

  @XmlAttribute
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SampleBean) {
			return this.name.equals(((SampleBean) obj).getName())
					&& this.value.equals(((SampleBean) obj).getValue());
		}
		return false;
	}

	@Override
	public String toString() {
		return "name=".concat(name)
				.concat(", ")
				.concat("value=")
				.concat(value.toString());
	}
}
