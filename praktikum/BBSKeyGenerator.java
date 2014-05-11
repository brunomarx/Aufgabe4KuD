package praktikum;

import java.math.BigInteger;
import java.util.Random;

/** Diese Klasse implementiert einen Schlüsselgenerator für den BBS */

public class BBSKeyGenerator{
    BBSPrivateKey privKey;
    BBSPublicKey pubKey;

    /**
     * Schlüsselgenerator erstellen und Schlüssel generieren.
     * Der geheime Schlüssel besteht aus den Primzahlen p und q und einem 
     * Startwert s.
     * Der öffentliche Schlüssel enthält n = p * q
     * Die Primzahlen werden aus Zufallszahlen generiert.
     * p und q sind jeweils 512 Bit lang. 
     */
    
    public BBSKeyGenerator(){
	/* p generieren: p ist Primzahl und  =3 mod 4 */
	
	BigInteger p = null;
	do{
	    p = new BigInteger(BBS.BBSKeyLength / 2, 100, new Random()
                                 /** müßte eigentlich sicherer Zufallsgenerator sein*/
                                 );
	}while (p.compareTo(BigInteger.ZERO) <= 0
		|| p.mod(new BigInteger("4")).compareTo(new BigInteger("3")) != 0);
	//System.out.println("p: "+p.toString());
	//System.out.println("p mod 4: "+p.mod(new BigInteger("4")).toString());
	/* q generieren: q ist Primzahl und = 3 mod 4 und != p */
	
	BigInteger q = null;
	do{
	    q = new BigInteger(BBS.BBSKeyLength / 2, 100, new Random()
                                /** müßte eigentlich sicherer Zufallsgenerator sein*/
                                );
	}while (q.compareTo(BigInteger.ZERO) <= 0
		|| q.mod(new BigInteger("4")).compareTo(new BigInteger("3")) != 0
		|| q.compareTo(p) == 0);
       	//System.out.println("q: "+q.toString());
	//System.out.println("q mod 4: "+q.mod(new BigInteger("4")).toString());
	/* öffentlichen Modulus n erzeugen */
	
	BigInteger n = null;
	n = p.multiply(q);
       	//System.out.println("n: "+n.toString());
	//System.out.println("ggt(n,s):"+n.gcd(s).toString());
	/* Werte in die Schlüsseldatenstrukturen eintragen */
	
	privKey = new BBSPrivateKey();
	privKey.p = p;
	privKey.q = q;
	pubKey = new BBSPublicKey();
	pubKey.n = n;
	/* Startwert s generieren */
	
	privKey.s = generateStartwert(pubKey);
       	//System.out.println("s: "+s.toString());
    }
	
    /**
     * Diese Methode gibt den geheimen Schlüssel zurück.
     * @return den geheimen Schlüssel
     */
    
    public BBSPrivateKey getPrivateKey(){
	return privKey;
    }
    
    /**
     * Diese Methode gibt den öffentlichen Schlüssel zurück.
     * @return den öffentlichen Schlüssel
     */
    
    public BBSPublicKey getPublicKey(){
	return pubKey;
    }

    /**
     * Erzeugen eines Startwertes s für einen öffentlichen Schlüssel.
     * @param aKey der öffentliche Schlüssel
     * @return der Startwert
     */
    
    public static BigInteger generateStartwert(BBSPublicKey aKey){
	/* Startwert s erzeugen */
	
	BigInteger s = null;
	do {
	    s = new BigInteger(BBS.BBSKeyLength, new Random());
	}while (s.compareTo(BigInteger.ZERO) <= 0 
		|| s.compareTo(aKey.n) >= 0
		|| (s.gcd(aKey.n)).compareTo(BigInteger.ONE) != 0);
	/* Startwert muß ein quadratischer Rest sein, deshalb quadrieren */
	
	return s.modPow(new BigInteger("2"),aKey.n);
    }

}
