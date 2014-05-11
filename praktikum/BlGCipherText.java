package praktikum;

import java.math.BigInteger;

/**
 * Diese Klasse enthält den Schlüsseltext, der vom BlG-System erzeugt wird.
 * Er besteht aus der Bitfolge, durch (Klartext XOR Zufallszahl) berechnet wird,
 * und aus letzten Statuswert des BBS-Zufallsgenerators.
 */

public class BlGCipherText {
    /** Der Schlüsseltext */
    
    public BigInteger cipherText;
    /** Länge des Schlüsseltextes */
    
    public int cipherLen;
    /** Der Zustand des BBS-Generators nach dem Erzeugen der zufälligen Bitfolge */
    
    public BigInteger zustand;
}
