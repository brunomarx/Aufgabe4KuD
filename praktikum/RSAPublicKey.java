package praktikum;


import java.math.BigInteger;

/**
 * Diese Klasse stellt die Datenstruktur eines öffentlichen RSA-Schlüssels dar.
 * Dieser besteht aus dem Modulus n und dem öffentlichen Exponenten e 
 */

public class RSAPublicKey {
    /** Der Modulus n */
    public BigInteger n;
    /** Der öffentliche Exponent e */
    public BigInteger e;
}
