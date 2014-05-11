package praktikum;

/** Diese Klasse implementiert einen Schlüsselgenerator für das BlG-System */

public class BlGKeyGenerator{
    BlGPrivateKey privKey;
    BlGPublicKey pubKey;

    /**
     * Schlüsselgenerator erstellen und Schlüssel generieren.
     * Es wird der Schlüsselgenerator des BBS-Zufallsgenerators aufgerufen.
     * Die dort erzeugten Schlüssel werden dann für das BlG-System verwendet.
     * <p>Der geheime Schlüssel besteht aus den Primzahlen p und q und einem 
     * Startwert s.
     * Der öffentliche Schlüssel enthält n = p * q
     * Die Primzahlen werden aus Zufallszahlen generiert.
     */
    
    
    public BlGKeyGenerator(){
	BBSKeyGenerator kGen = new BBSKeyGenerator();
	privKey = new BlGPrivateKey(kGen.getPrivateKey());
	pubKey = new BlGPublicKey(kGen.getPublicKey());
    }
	
    /**
     * Diese Methode gibt den geheimen Schlüssel zurück.
     * @return den geheimen Schlüssel
     */
    
    public BlGPrivateKey getPrivateKey(){
	return privKey;
    }
    
    /**
     * Diese Methode gibt den öffentlichen Schlüssel zurück.
     * @return den öffentlichen Schlüssel
     */
    
    public BlGPublicKey getPublicKey(){
	return pubKey;
    }


}
