package kz.optimabank.optima24.utility.crypt;

import android.util.Log;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import kz.optimabank.optima24.model.manager.GeneralManager;

/**
  Created by Timur on 13.01.2017.
 */

public final class DiffieHellman {
    private final static int G_VALUE = 5;
    private final static int BIT_LENGTH = 256;
    private final static int RADIX = 36;
    private final static Random rnd = new SecureRandom();
    private BigInteger a;
    private BigInteger p;

    public String generatePublicKey() {
        p = BigInteger.probablePrime(BIT_LENGTH, rnd);
        a = BigInteger.probablePrime(BIT_LENGTH, rnd);
        BigInteger g = new BigInteger(Integer.toString(G_VALUE));

        BigInteger A = g.modPow(a, p);
        String pubKey = String.format("%s|%s|%s", p.toString(RADIX),
                g.toString(RADIX), A.toString(RADIX));
        Log.d("TAG", "a = " + a + " p = " + p);
        GeneralManager.getInstance().setA(a);
        GeneralManager.getInstance().setP(p);
        return pubKey;
    }

    public byte[] retrievePrivateKey(String hostKey, String tk) {
        if(hostKey!=null&&tk!=null) {
            BigInteger B = new BigInteger(hostKey, RADIX);
            Log.d("TAG", "B = " + B);
            Log.d("TAG", "a = " + GeneralManager.getInstance().getA() + " p = " + GeneralManager.getInstance().getP());
            a = GeneralManager.getInstance().getA();
            p = GeneralManager.getInstance().getP();
            BigInteger pkInt = B.modPow(a, p);
            byte[] pk = pkInt.toByteArray();
            if (pk[0] == 0) {
                byte[] tmp = new byte[pk.length - 1];
                System.arraycopy(pk, 1, tmp, 0, tmp.length);
                pk = tmp;
            }
            return Blowfish.getInstance().deCrypt(Base64Coder.getInstance().decode(tk), pk);
        }
        return null;
    }
}
