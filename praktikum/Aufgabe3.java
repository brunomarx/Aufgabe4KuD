package praktikum;

import java.util.*;
import java.math.BigInteger;

/**
 * Testprogramm für das BlG-System. Um die implementierten Funktionen zu testen, muß
 * diese Klasse gestartet werden. Sie führt als erstes die Schlüsselgenerierung
 * aus. Danach wird ein Klartext zuerst verschlüsselt und danach der entstandene
 * Schlüsseltext wieder entschlüsselt.
 */

public class Aufgabe3 {
   
    
    /* Fuer Internationalisierung */
    public static ResourceBundle messages; 
    
    /**
     * Main-Funktion. Es wird ein BlG-Schlüssel generiert, und dann wird ein
     * Klartext zuerst verschlüsselt und dann wieder entschlüsselt.
     */
    
    public static void main(String[] argv){


	/* Der folgende Teil ist nötig für die Internationalisierung  */
	Locale currentLocale; 
	currentLocale = Locale.getDefault(); 
	messages = ResourceBundle.getBundle("praktikum.localmessages",currentLocale); 
 

	/* Schlüssel generieren */

	System.out.println(messages.getString("Main4bGenKey"));
	BlGKeyGenerator keyGen = new BlGKeyGenerator();
	/* Klartext setzen */

	String plainString = "Hello World!";

	System.out.println(messages.getString("Main4bPlTxt")+plainString);

	/* Klartext in BigInteger umwandeln */

	BigInteger plainText = new BigInteger(plainString.getBytes());
	System.out.println(messages.getString("Main4bPlTxtD")+plainText.toString()+"\n");


	/* Verschlüsseln */

	BlG blg = new BlG();
	BlGCipherText ciph = blg.encryptStream(keyGen.getPublicKey(),
					       plainText,
					       (plainString.getBytes().length +1)* 8);

	System.out.println(messages.getString("Main4bKTxt")
			   +ciph.cipherText.toString());
			   System.out.println(messages.getString("Main4bBBS")
			   + ciph.zustand.toString()+"\n");

	/* wieder entschlüsseln */

	BigInteger newPlainText = blg.decryptStream(keyGen.getPrivateKey(),
						    ciph);
	System.out.println(messages.getString("Main4bPlTxtD2")+newPlainText.toString());

	String newPlainString = new String(newPlainText.toByteArray());

	System.out.println(messages.getString("Main4bPlTxt2")+ newPlainString+"\n");
    }
}