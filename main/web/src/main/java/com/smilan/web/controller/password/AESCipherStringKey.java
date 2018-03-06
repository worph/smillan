/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.web.controller.password;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/**
 *
 * @author pierr
 */
public class AESCipherStringKey {

    private BufferedBlockCipher cipher;
    private CipherParameters params;

    public AESCipherStringKey(String key16Characters) {
        try {
            // hash password with SHA-256 and crop the output to 128-bit for key
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(key16Characters.getBytes("UTF-8"));
            byte[] keyBytes = new byte[32];
            System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
            
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            
             // setup cipher parameters with key and IV
            byte[] keyb = key.getEncoded();
            KeyParameter keyParam = new KeyParameter(keyb);
            byte[] ivData = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
            CipherParameters params = new ParametersWithIV(keyParam, ivData);

            // setup AES cipher in CBC mode with PKCS7 padding
            BlockCipherPadding padding = new PKCS7Padding();
            cipher = new PaddedBufferedBlockCipher(
                    new CBCBlockCipher(new AESEngine()), padding);
            cipher.init(true, params);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AESCipherStringKey.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AESCipherStringKey.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    public String encrypt(String plainText) {
        try {
            final byte[] dataIn = plainText.getBytes("UTF-8");
            byte[] buf = new byte[cipher.getOutputSize(dataIn.length)];
            int len = cipher.processBytes(dataIn, 0, dataIn.length, buf, 0);
            len += cipher.doFinal(buf, len);
            byte[] encrypted = new byte[len];
            System.arraycopy(buf, 0, encrypted, 0, len);
            String encryptedText = new String(Base64.getEncoder().encode(encrypted), "UTF-8");
            
            return encryptedText;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AESCipherStringKey.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataLengthException ex) {
            Logger.getLogger(AESCipherStringKey.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(AESCipherStringKey.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidCipherTextException ex) {
            Logger.getLogger(AESCipherStringKey.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}