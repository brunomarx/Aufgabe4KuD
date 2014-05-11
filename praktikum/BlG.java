package praktikum;


import java.math.BigInteger;
/**
 * Diese Klasse implementiert das BlG-System. Es gibt eine Funktion zum Verschlüsseln
 * mit dem öffentlichen Schlüssel und eine zum Entschlüsseln mit dem geheimen
 * Schlüssel. Der Schlüseltext wird in einer Datenstruktur abgelegt, die in der
 * Klasse BlGCipherText definiert ist. Der Klartext wird als BigInteger erwartet.
 */

public class BlG {

    public int BlGKeyLength;

    /** Konstruktor wird nicht benötigt */
    
    public BlG(){
	BlGKeyLength = BBS.BBSKeyLength;
    }

    /**
     * gibt die Blockgröße eines Klartextblockes zurück.
     * Da das BlG-System eine Stromchiffre ist, wird immer 1 zurückgegeben.
     * @return 1
     */
    
    public int getPlainBlockSize(){
	return 1;
    }

    /**
     * Die probabilistische Verschlüsselung erfolgt durch XOR Verknüfpung
     * des Klartextes plainstream mit einer ebensolangen mit dem BBS-Generator
     * erzeugten zufälligen Bytefolge. Der zum Schluß erreichte Zustand des
     * dafür verwendeten BBS-Generators wird in BlgCipherText.status herausgegeben
     * und ist Bestandteil der Chiffre.
     * @param pubKey der öffentliche BlG-Schlüssel
     * @param plainStream Der Klartext
     * @param len Länge des Klartextes
     * @return den Schlüsseltext
     */
    
    public BlGCipherText encryptStream(BlGPublicKey pubKey, BigInteger plainStream, int len){
	/* Klartextlänge auf Korrektheit testen */
	
	if(len <= 0){
	    System.out.println(Aufgabe3.messages.getString("BIGlen"));
	    System.exit(0);
	}

	/* Erzeugen des BBS-Generators, der die Methoden Tick() und Tickback enthält */
	
        BBS bbs = new BBS();

	/* Erzeugen eines Schlüsseltext-Objektes */
	
        BlGCipherText ciph = new BlGCipherText();

        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        // Hier implementieren
        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

	/* Die Parameter des ciph-Objektes (cipherText, cipherLen, zustand) erzeugen und in das ciph-Objekt schreiben. */
	


        
        
        

        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        return ciph;
    }

    /**
     * Die Entschlüsselung erfolgt, indem zuerst der Zustand des
     * BBS-Generators auf den im Schlüsseltext enthaltenen  gesetzt wird.
     * Anschließend wird dann durch Rückwärtsbetreiben des BBS-Generators
     * die zum Verschlüsseln verwendete Zufallsfolge regeneriert und
     * schließlich mit der Chiffre cipherstream XOR-verknüpft.
     */
    
    public BigInteger decryptStream(BlGPrivateKey privKey, BlGCipherText ciph){
	BBS bbs = new BBS();

	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	/* Hier implementieren (Entschlüsselung)                              */
	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX


	
	

	/* Klartext zurückgeben */
	
        return null;
        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX


    }
}
