package com.github.narh.tool.crypt;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class CryptConverter {

  @Autowired @Qualifier("srcCrypter")
  private Crypter srcCrypter;

  @Autowired @Qualifier("destCrypter")
  private Crypter destCrypter;

  public void setSrcCrypter(Crypter srcCrypter) {
    this.srcCrypter = srcCrypter;
  }

  public void setDestCrypter(Crypter destCrypter) {
    this.destCrypter = destCrypter;
  }

  public byte[] convert(final byte[] src) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    return destCrypter.encrypt(srcCrypter.decrypt(src));
  }
}
