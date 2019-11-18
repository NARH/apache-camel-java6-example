package com.github.narh.tool.crypt;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypter {

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
    private final ALGORITHM algorithm;
    private byte[] secretKey;
    private byte[] iv;
    private byte[] xorKey;

    public Builder(ALGORITHM algorithm) {
      this.algorithm = algorithm;
    }

    public Crypter build() {
      assert secretKey != null : "Secret Key is null.";

      if(algorithm.useXor) {
        assert xorKey != null : "XOR key is null.";
      }

      if(algorithm.useIv ) {
        assert iv != null : "IV Parameter is null.";
      }
      return new Crypter(algorithm, new SecretKeySpec(secretKey, algorithm.algorithm)
          , xorKey, new IvParameterSpec(iv));
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
