package praktikum;

/** Diese Klasse stellt den Datentyp für einen geheimen BlG-Schlüssel dar. */

public class BlGPrivateKey extends BBSPrivateKey {
    
    /**
     * Konstruktor. Erstellt einen BlG-Schlüssel aus einem
     * BBS-Schlüssel.
     * @param aKey der BBS-Schlüssel
     */
    
    public BlGPrivateKey(BBSPrivateKey aKey){
	p = aKey.p;
	q = aKey.q;
	s = aKey.s;
    }	
}

