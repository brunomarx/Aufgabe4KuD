package praktikum;

/** Diese Klasse stellt den Datentyp für einen öffentlichen Schlüssel des BlG-Systems dar */
/** This class represents the data structure for the public key of the BIG system */
public class BlGPublicKey extends BBSPublicKey{

    /**
     * Konstruktor. Erstellt einen BlG-Schlüssel aus
     * einem BBS-Schlüssel.
     * @param aKey der BBS-Schlüssel
     */
    /**
     * Constructor. Creates a BIG key out of a BBS key.
     * @param aKey the BBS key
     */
    public BlGPublicKey(BBSPublicKey aKey){
	n = aKey.n;
    }
}
