package praktikum;

import java.math.BigInteger;

/**
 * Diese Klasse implementiert den s^2 mod n Generator. (oder auch BBS-System genannt)
 */

public class BBS{
    /** Der innere Zustand des Generators */
    
    private BigInteger zustand = null;
    
    /** Länge des Moduls n = p * q in Bit */
    
    public static final int BBSKeyLength = 1024;

    /** Konstruktor wird nicht benötigt */
    
    public BBS(){
    }

    /**
     * Diese Methode erzeugt eine Pseudozufällige Bitfolge, indem wiederholt 
     * s^2 mod n berechnet und das letzte Bit des jeweiligen Ergebnisses zur
     * Bitfolge hinzugefügt wird. 
     * @param pubKey der öffentliche Schlüssel eines BBS-Systems
     * @param aZustand Initialisierung des inneren Zustandes. Wenn diese null ist,
     * wird eine zufällige Initialisierung gewählt.
     * @param bitLen Länge der zu erzeugenden Pseudozufallsbitkette
     * @return die pseudozufällige Bitkette
     */
    
    public BigInteger Tick(BBSPublicKey pubKey, BigInteger aZustand, int bitLen){
	/* Startwert bilden, falls noch keiner vorhanden */
	
	if (aZustand == null && zustand == null){
	    zustand = BBSKeyGenerator.generateStartwert(pubKey);
	}else if (aZustand != null){
	    zustand = aZustand;
	}

	//System.out.println("s: "+zustand.toString());
	//System.out.println("ggt(s,n): "+pubKey.n.gcd(zustand).toString());
	//System.out.println("n: "+pubKey.n.toString());

	/* Ergebnis-Bitfolge initialisieren */
	
	BigInteger ergebnis = new BigInteger("0");
	/* Bits gewinnen und Quadrieren */
	
	for(int i = 0; i < bitLen; i++){
	    ergebnis = ergebnis.shiftLeft(1);
	    if(zustand.testBit(0)){
		ergebnis = ergebnis.setBit(0);
	    }else{
		ergebnis = ergebnis.clearBit(0);
	    }
	    zustand = zustand.modPow(new BigInteger("2"),pubKey.n);
	    //System.out.println("Zustand im "+Integer.toString(i)+". Schritt: "
	    //		       + zustand.toString()); 
	    //System.out.println("State in step "+Integer.toString(i)+": "
	    //		       + zustand.toString()); 
	
	}
	return ergebnis;
    }

    /**
     * Diese Methode gibt den inneren Zustand des BBS-Systems zurück.
     * @return den inneren Zustand des BBS-Systems
     */
    
    public BigInteger getZustand(){
	return zustand;
    }

    
    /**
     * Diese Methode erzeugt eine Pseudozufällige Bitfolge, indem wiederholt
     * mit Hilfe von p und q die Wurzel aus dem Zustand s gezogen wird.
     * Die Bitfolge wird dann mit der Methode Tick() mit Hilfe 
     * der zum Schluß ermittelten Wurzel als Startwert erzeugt.
     * @param privKey der geheime Schlüssel eines BBS-Systems
     * @param aZustand Initialisierung des inneren Zustandes. Wenn diese null ist,
     * wird eine zufällige Initialisierung gewählt.
     * @param bitLen Länge der zu erzeugenden Pseudozufallsbitkette
     * @return die pseudozufällige Bitkette
     */
    
    public BigInteger TickBack(BBSPrivateKey privKey, BigInteger aZustand, int bitLen){
	/* Öffentlichen Schlüssel bilden */
	
	BBSPublicKey pubKey = new BBSPublicKey();
	pubKey.n = privKey.p.multiply(privKey.q);
	/* Startwert bilden, falls noch keiner vorhanden */
	
	if (aZustand == null && zustand == null){
	    zustand = BBSKeyGenerator.generateStartwert(pubKey);
	}else if (aZustand != null){
	    zustand = aZustand;
	}

	/* System.out.println("zust:"+zustand.toString());*/
	
	//	
	//System.out.println("p: "+privKey.p.toString());
	//System.out.println("q: "+privKey.q.toString());

	/* Zum Ausgangszustand zurückrechnen
	  Zuerst bitLen Wurzeln aus zustand ziehen modulo p bzw. q */
	
	BigInteger zModP = zustand.mod(privKey.p);
	BigInteger zNeuP = zModP.modPow(((privKey.p.add(BigInteger.ONE))
					   .divide(new BigInteger("4")))
					  .pow(bitLen),
					  privKey.p);
	/* System.out.println("z mod p: "+zModP.toString() +
			   " z mod p nach "+Integer.toString(bitLen)+" Wurzelziehen: "
			   + zNeuP.toString());*/
	
	BigInteger zModQ = zustand.mod(privKey.q);
	BigInteger zNeuQ = zModQ.modPow(((privKey.q.add(BigInteger.ONE))
					   .divide(new BigInteger("4")))
					  .pow(bitLen),
					  privKey.q);
	/* System.out.println("z mod q: "+zModQ.toString() +
			   " z mod q nach "+Integer.toString(bitLen)+" Wurzelziehen: "
			   + zNeuQ.toString());*/
	

	/* Mit den CRA wieder zusammenfügen */
	
	BigInteger u = privKey.p.modInverse(privKey.q);
	BigInteger v = privKey.q.modInverse(privKey.p);
	zustand = (zNeuQ.multiply(privKey.p.multiply(u))
	    .add(zNeuP.multiply(privKey.q.multiply(v)))).mod(pubKey.n);
	// System.out.println("u = p^-1 mod q: "+u.toString()
	//		   + ", v = q^-1 mod p: "+v.toString()
	/*		   + ", Zustand mod n: "+zustand.toString());*/
	

	/* Mit der Methode Tick() die Bitfolge erstellen */
	
	return Tick(pubKey, null, bitLen);
    }

}
