package praktikum;
import java.math.BigInteger;
import java.util.*; 


/** 
 * Hauptklasse für Aufgabe 1, enthält die Main-Funktion. 
 * Zum Testen der Implementierung muß diese Klasse gestartet werden. 
 */ 
  
public class Aufgabe1 { 
 
    /* Fuer Internationalisierung */
    public static ResourceBundle messages;  


    /**
     * Diese Methode versucht, Schlüsseltextblöcke zu attackieren. 
     * Wenn ein zum Schlüsseltextblock passender Klartextblock gefunden wurde, 
     * gibt die Funktion den Klartextblock zurück, sonst <code>null</code>. 
     * @param targetBlk der zu attackierende Schlüsseltextblock 
     * @param serv Das Service-Objekt, in dem die Verschlüsselungsfunktionen des 
     *             Angegriffenen zur Verfügung gestellt werden. 
     * @return den ermittelten Klartextblock, oder <code>null</code>, falls keiner gefunden wurde. 
     */ 
            
    public BigInteger attackBlock(BigInteger targetBlk, Service serv){ 
    	BigInteger codeBlk, cipherBlk; 
		boolean hit = false; 
 
        /** Hinweis: 
         * Klartextlänge uns Schlüsseltextlänge sind im Objekt "serv" definiert. 
         * Es kann darauf über 
         * serv.RSA_PLAIN_LEN  bzw. 
         * serv.RSA_CIPHER_LEN 
         * zugegriffen werden. 
         */ 
	 	 
	    
		/* Ersten Testcodeblock mit Nullen initialisieren */
		codeBlk = BigInteger.ZERO;
 
		/* Erschöpfende Suche in dem letzten 8 Bit des Codeblocks durchführen */
		do{ 
			
			//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX 
	   	    /* ab hier implementieren                */ 
	       	//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX 
			
			/* Hinweise:
			 *  Verschlüsseln mit dem öffentlichen Schlüssel des Angegriffenen:
			 *  	BigInteger encrypted = serv.encrypt(BigInteger codeblock)
			 *  
			 *  targetBlk enthält den anzugreifenden Schlüsseltextblock
			 */
			cipherBlk = serv.encrypt(codeBlk);
			if (cipherBlk.compareTo(targetBlk) != 0){
				hit = false;
				codeBlk = codeBlk.add(BigInteger.ONE);
			}else{
				hit = true;
			}
			
	    }while(!hit && (codeBlk.compareTo(BigInteger.valueOf((long)127)) != 1)); 
	

		
		
	 
			//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX 
			/* bis hier implementieren               */ 
			//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX 	    	
	    	
	    	 
		/* Klartext zurückgeben, wenn einer gefunden wurde. */
		if (hit){ 
	    	return codeBlk; 
		}else{ 
	    	return null; 
		} 
    } 
 
 
    /** 
     * Diese Methode ist die Hauptfunktion für den Angriff. Es wird zuerst der Angegriffene 
     * initialisiert. Danach werden die Schlüsseltextblöcke der Reihe nach angegriffen. 
     * Außerdem wird eine Zeitmessung vorgenommen. 
     */ 
     
    public Aufgabe1(){ 
 
		/* Angegriffenen Initialisieren */
		Service serv = new Service(); 
 
		BigInteger[] targetPkt = serv.getCipher(); 
		int blocks = targetPkt.length; 
		int codePos = 0; 
		byte[] codePkt = new byte[targetPkt.length]; 
 
		/* === Beginn des  Angriffs === */
		System.out.println("==================================================================================\n"); 
		System.out.println("     "+messages.getString("Main4a1ov1")); 
		System.out.println("     "+messages.getString("Main4a1ov2")); 
		System.out.println("     "+messages.getString("Main4a1ov3") + "  " + Integer.toString(Service.RSA_CIPHER_LEN)); 
		System.out.println("     "+messages.getString("Main4a1ov4") + "  " + blocks); 
		System.out.println("     "+messages.getString("Main4a1ov5") + "  8" +"\n"); 
		System.out.println("     "+messages.getString("Main4a1ov6")); 
 
		/* für Zeitmessung */
		long[] zeit = new long[4]; 
		zeit[0] = System.currentTimeMillis(); 
 
		/* alle Blöcke durchsuchen */
		for(int i = 0; i < blocks; i++){ 
	    	System.out.print(messages.getString("Main4a1blk")+" " + Integer.toString(i) + "   ...   "); 
 
	 	   	/* Target-Paket bilden */
	    	BigInteger targetBlk; 
 
	    	zeit[1] = System.currentTimeMillis(); 
	    	targetBlk = targetPkt[i];
	    	BigInteger codeBlk = attackBlock(targetBlk, serv); 
	    	/* Abbruch, falls kein Klartextblock ermittelt wurde. */
	    	if (codeBlk == null){ 
				System.out.println(messages.getString("Main4a1par")); 
				System.exit(0); 
	    	} 
 
	    	/* Zeitdauer zum Suchen ausgeben */
	    	zeit[2] = System.currentTimeMillis(); 
	    	System.out.print(messages.getString("Main4a1time")+"  "+ Long.toString(zeit[2] - zeit[1]) +"ms )"); 
 
	    	/* Lege Code in codePkt ab */
	    	byte[] compCodeBlk = serv.compressBlocks(new BigInteger[]{codeBlk}); 
	    	System.out.println(messages.getString("Main4a1code")+" "+ new String(compCodeBlk).toString()); 
	    	for(int j = 0; j < compCodeBlk.length; j++){ 
				codePkt[codePos] = compCodeBlk[j]; 
				codePos++; 
	    	} 
		} 
 
		zeit[3] = System.currentTimeMillis(); 
 
		/* === Ende des Angriffs === */
		System.out.println(messages.getString("Main4a1erfolg")+" " 
			   +messages.getString("Main4a1dauer")+Long.toString(zeit[3]-zeit[0])+ "ms )\n"); 
		System.out.println(messages.getString("Main4a1plaintext")); 
        byte[] ba = new byte[1]; 
        for(int i = 0; i < codePos; i++){ 
            ba[0] = codePkt[i]; 
            System.out.print(new String(ba)); 
        } 
		System.out.println("\n============================================================================="); 
    } 
 
 
 
    /**
     * main-Funktion zum Starten des Programms aus der Kommandozeile. 
     * @param argv Kommandozeilenoptionen, werden nicht benötigt 
     */ 
     
    public static void main(String[] argv){ 
		/* The following part is used for the internationalization */
		Locale currentLocale; 
		currentLocale = Locale.getDefault(); 
		messages = ResourceBundle.getBundle("praktikum.localmessages",currentLocale); 
 
		/* rest of the main method */
		new Aufgabe1(); 
    } 
 
}
