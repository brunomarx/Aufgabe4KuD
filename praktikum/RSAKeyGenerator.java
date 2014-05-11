package praktikum;

import java.math.BigInteger;
import java.util.Random;

/** 
 * Diese Klasse implementiert einen Schlüsselgenerator für RSA 
 */

public class RSAKeyGenerator{
    RSAPrivateKey privKey;
    RSAPublicKey pubKey;

    /**
     * Erstellt den Schlüsselgenerator und generiert ein RSA-Schlüsselpaar.
     * @param k Länge des Schlüssels in Bit
     */
	
    public RSAKeyGenerator(int k){
		/* e = 2^16+1 */
		BigInteger e = BigInteger.valueOf(2).pow(16).add(BigInteger.valueOf(1));
		/* Primzahl p mit Länge k/2 */	
		BigInteger p = new BigInteger(k / 2, 200, new Random());
		/* Primzahl q mit länge k/2 */
		BigInteger q = null;
		do {
	    	q = new BigInteger(k / 2, 200, new Random());
		}while(q.compareTo(p) == 0);
		/* invpq = p^-1 mod q */
		BigInteger invpq = p.modInverse(q);
		/* n = p * q */
		BigInteger n = p.multiply(q);
		e = e.mod(n);
		/* dp = e^-1 mod p-1 = d mod p-1*/
		BigInteger dp = e.modInverse(p.subtract(BigInteger.valueOf(1)));
		/* dq = e^-1 mod q-1 = d mod q-1*/
		BigInteger dq = e.modInverse(q.subtract(BigInteger.valueOf(1)));
	
		privKey = new RSAPrivateKey();
		privKey.n = n;
		privKey.p = p;
		privKey.q = q;
		privKey.invpq = invpq;
		privKey.dp = dp;
		privKey.dq = dq;

		pubKey = new RSAPublicKey();
		pubKey.n = n;
		pubKey.e = e;
    }

    /**
     * Diese Methode gibt den geheimen Schlüssel zurück.
     * @return den geheimen Schlüssel
     */
    
    public RSAPrivateKey getPrivateKey(){
		return privKey;
    }
    
    /**
     * Diese Methode gibt den öffentlichen Schlüssel zurück.
     * @return den öffentlichen Schlüssel
     */
    
    public RSAPublicKey getPublicKey(){
		return pubKey;
    }
}
