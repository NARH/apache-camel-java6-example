package com.github.narh.tool.crypt;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.util.ExchangeHelper;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class CryptDataFormat implements DataFormat {
  Logger log = LoggerFactory.getLogger(getClass());

  private Crypter srcCrypter;
  private Crypter destCrypter;

  @Autowired
  @Qualifier("srcCrypter")
  public void setSrcCrypter(Crypter srcCrypter) {
    this.srcCrypter = srcCrypter;
  }

  @Autowired
  @Qualifier("destCrypter")
  public void setDestCrypter(Crypter destCrypter) {
    this.destCrypter = destCrypter;
  }

  @Override
  public void marshal(Exchange exchange, Object graph, OutputStream stream) throws Exception {
    String text = ExchangeHelper.convertToType(exchange, String.class, graph);

    if (StringUtils.isNotEmpty(text)) {
      if (log.isDebugEnabled()) {
        log.debug("AES128で暗号 {} -> {}", text,
            new String(Hex.encodeHex(srcCrypter.encrypt(text.getBytes("MS932")))));
      }
      String decryptStr = new String(srcCrypter.decrypt(Hex.decodeHex(text)));
      if (log.isDebugEnabled()) log.debug("複合 {} -> {}", text, decryptStr);
      String encryptStr = new String(Hex.encodeHex(destCrypter.encrypt(decryptStr.getBytes("MS932"))));
      if (log.isDebugEnabled()) log.debug("暗号 {} -> {}", decryptStr, encryptStr);
      if (StringUtils.isNotEmpty(encryptStr)) {
        stream.write(encryptStr.getBytes("MS932"));
        if(null == exchange.getProperty("CamelSplitComplete")
            || !exchange.getProperty("CamelSplitComplete", Boolean.class)) stream.write("\r\n".getBytes("MS932"));
      }
    }
  }

  @Override
  public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
    return null;
  }

}
