package com.github.narh.tool.crypt;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Crypter {

  Logger log = LoggerFactory.getLogger(getClass());

  /** アルゴリズム */
  private final ALGORITHM algorithm;
  private final SecretKeySpec secretKey;
  private final IvParameterSpec iv;
  private byte[] xorKey;

  public enum ALGORITHM {
      AES128("AES", true, false)
    , AES256("AES/CBC/PKCS5Padding", false, true);

    public final String algorithm;
    public final boolean useXor;
    public final boolean useIv;

    private ALGORITHM(final String algorithm, boolean useXor, boolean useIv) {
      this.algorithm = algorithm;
      this.useXor = useXor;
      this.useIv = useIv;
    }
  }

  public static class Builder {
    private static final String MS932 = "MS932";
    private final ALGORITHM algorithm;
    private String secretKey;
    private String iv;
    private String xorKey;

    public Builder(ALGORITHM algorithm) {
      this.algorithm = algorithm;
    }

    public Crypter build() throws UnsupportedEncodingException {
      assert secretKey != null : "Secret Key is null.";
      assert 0 < secretKey.length() : "SecretKey length is zero.";

      if(algorithm.useXor) {
        assert xorKey != null : "XOR key is null.";
        assert 0 < xorKey.length() : "XOR key length is zero.";
      }

      if(algorithm.useIv) {
        assert iv != null : "IV Parameter is null.";
        assert 0 < iv.length() : "IV Parameter length is zero.";
      }
      return new Crypter(algorithm, new SecretKeySpec(secretKey.getBytes(MS932), "AES")
          , (algorithm.useXor) ? xorKey.getBytes(MS932): null
          , (algorithm.useIv)  ? new IvParameterSpec(iv.getBytes(MS932)) : null);
    }

    public String getSecretKey() {
      return secretKey;
    }

    public void setSecretKey(String secretKey) {
      this.secretKey = secretKey;
    }

    public String getIv() {
      return iv;
    }

    public void setIv(String iv) {
      this.iv = iv;
    }

    public String getXorKey() {
      return xorKey;
    }

    public void setXorKey(String xorKey) {
      this.xorKey = xorKey;
    }

  }

  protected Crypter(final ALGORITHM algorithm, SecretKeySpec secretkey
      , byte[] xorKey, IvParameterSpec iv ) {
    this.algorithm = algorithm;
    this.secretKey = secretkey;
    this.xorKey    = xorKey;
    this.iv        = iv;
  }

  public byte[] encrypt(final byte[] data)
      throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    return (algorithm.useXor) ? doXorCrypt(doAESCrypt(Cipher.ENCRYPT_MODE, data)) : doAESCrypt(Cipher.ENCRYPT_MODE, data);
  }

  public byte[] decrypt(final byte[] data)
      throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    return (algorithm.useXor) ? doAESCrypt(Cipher.DECRYPT_MODE, doXorCrypt(data)) : doAESCrypt(Cipher.DECRYPT_MODE, data);
  }

  private byte[] doAESCrypt(final int opmode, final byte[] data)
      throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
      , InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    Cipher cipher = Cipher.getInstance(algorithm.algorithm);
    if(algorithm.useIv)
      cipher.init(opmode, secretKey, iv);
    else
      cipher.init(opmode, secretKey);
    return cipher.doFinal(data);
  }

  private byte[] doXorCrypt(final byte[] data) {
    byte[] result = new byte[data.length];
    for(int i = 0; i < data.length; i++) {
      result[i] =(byte) (data[i] ^ xorKey[i%xorKey.length]);
    }
    return result;
  }
}
