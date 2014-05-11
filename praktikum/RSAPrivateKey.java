package praktikum;


import java.math.BigInteger;

/**
 * Diese Klasse stellt die Datenstruktur eines geheimen RSA-Schlüssels dar.
 * Dieser besteht aus: dem Modulus n,
 *                     den Primzahlen p und q mit p * q = n,
 *                     der Zahl invpq = p^-1 mod q,
 *                     dp = e^-1 mod p-1,
 *                     dq = e^-1 mod q-1
 */

public class RSAPrivateKey {
	/** Der Modulus n */
    public BigInteger n;
	/** Die Primzahl p */
    public BigInteger p;
	/** Die Primzahl q */
    public BigInteger q;
    /** invpq = p^-1 mod q */
    public BigInteger invpq;
    /** dp = e^-1 mod p-1 */
    public BigInteger dp;
    /** dq = e^-1 mod q-1 */
    public BigInteger dq;
}
