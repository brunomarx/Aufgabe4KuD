package praktikum;


/**
 * <p>Titel: Aufgabe 2</p>
 * @author Sebastian Clauß
 * @version 1.0
 */

import java.math.BigInteger;
import java.util.*;

public class Aufgabe2 {
  
    /* For internationalization */
    public static ResourceBundle messages;  
 

    /**
     * In dieser Methode soll der in Aufgabe 2 beschriebene
     * Angriff programmiert werden. Ziel des Angriffs ist es, den Beginn
     * eines Codetextblockes zu finden. Als Beginn des Codetextblockes gilt
     * die Position des ersten Blockes aus dem Strom von Schlüsseltextblöcken,
     * der zum Codetextblock gehört. (Die Blöcke werden dabei von 0 beginnend
     * gezählt.)
     */
    
    public Aufgabe2(Service serv) {
	
    	//===============================================================
        /* Hier implementieren                                         */
        //===============================================================

        /* Hinweis:
           Im Objekt "serv" wird eine Folge von Schlüsseltextblöcken generiert.
           mit der Methode "BigInteger serv.getNextCipherBlock()" kann jeweils der
           nächste Schlüsseltextblock gelesen werden.
           Weiterhin stehen folgende Methoden aus dem Objekt "serv" zur Verfügung:

           BigInteger serv.generateCipherBlock(byte character)
             diese Methode generiert einen Schlüsseltextblock für das angegebene Zeichen.
             Zum Verschlüsseln wird der öffentliche Schlüssel des Angegriffenen benutzt.

           boolean serv.checkStartBlockPos(int pos)
             diese Methode ist zum Testen der Implementierung vorgesehen. Sie gibt
             "true" zurück, falls die eingegebene Zahl die Position des ersten Blockes
             aus dem CodeTextBlock war, anderenfalls "false". Diese Methode darf für den
             Angriff natürlich nicht verwendet werden.

        */
		     
        //=======================================================
    	//=======================================================
        //alle Ziffer, die wir suchen sollen. D M _ {0...9 , .}
        byte dParm = 'D';
        byte mParm = 'M';
        byte blankParm = ' ';
        byte[] zahlParm = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', ','};
       
        BigInteger dZeichen = serv.generateCipherBlock(dParm);
        BigInteger mZeichen = serv.generateCipherBlock(mParm);
        BigInteger blankZeichen = serv.generateCipherBlock(blankParm);
        BigInteger[] zahlZeichen = new BigInteger[12];
        for(int i = 0; i < 12; i++){
        	zahlZeichen[i] = serv.generateCipherBlock(zahlParm[i]);
        }
       
        Arrays.sort(zahlZeichen);
       
        boolean hit = false;
        boolean treff = false; //hilft dabei zu erkennen, ob DM_Z erkannt wurde. Vermeidet DDM.
        int pos = 1 ;//die aktuelle Position der Stelle
        int startBlockPos =-1;
        BigInteger cipherBlk = serv.getNextCipherBlock();
        
        
       
        do{ //Vergleich jeder Block mit dem verschlüsselten Zeichen in der Reihenfolge DM_Z, wobei _= Leerzeichen und Z=Ziffer
        	if(cipherBlk.compareTo(dZeichen) == 0){
        		System.out.println("Ein D wurde gefunden in der Position " + pos);
        		treff = true;     //Ein D wurde gefunden
        		startBlockPos=pos-1;
        		cipherBlk = serv.getNextCipherBlock();
        		pos = pos + 1;
        		     		
        		if(cipherBlk.compareTo(mZeichen) == 0){
       
        				cipherBlk = serv.getNextCipherBlock();
        				pos = pos + 1;
        				int i = 1;
        				System.out.println("Ein M wurde gefunden");
        				treff=false;
        				
        				if (cipherBlk.compareTo(blankZeichen) == 0){
        				
        					while(cipherBlk.compareTo(blankZeichen) == 0){ //i ist gleich der Anzahl von Leerzeichen
        						cipherBlk = serv.getNextCipherBlock();
        						pos = pos + 1;
        						i++;
        						System.out.println("Ein Leerzeichen wurde gefunden");
        					}
        				}
        				else{ 
        					treff = false;
        					System.out.println("Ops. Kein Leerzeichen");
        					startBlockPos = -1;
        				}
       
        				//um zu pruefen, ob es einen Beitrag in dem Formular gibt
        				if (Arrays.binarySearch(zahlZeichen, cipherBlk) == -1) { 
        					treff = false;
        					System.out.println("Ops. Kein Symbol");
        					startBlockPos = -1;
        					
        				}
        				else {
        					while((Arrays.binarySearch(zahlZeichen, cipherBlk) != -1) && (i < 6)){
        						System.out.println("Ein Symbol wurde gefunden");
        						cipherBlk = serv.getNextCipherBlock();
        						i++;
        						pos = pos + 1;
        						hit = true; //wir sind am Ende der Durchsuchung
        						
        					}
        				}
        				
        		}
        		else { 
        			
        			System.out.println("Ops...Kein M.");
        			startBlockPos = -1;
        		}
        	}
        	else{
        		
        		treff=false;
        		System.out.println("Ops...kein D");
        		startBlockPos = -1;
        	}
        	
        		
        	
        	if (!treff){
        		cipherBlk = serv.getNextCipherBlock(); //der Block enthält kein "D"
        		pos++; // Wo befindet sich die Suche
        	}
        	
        }while((!hit) && ((cipherBlk != null))); //Bedingung für den Abbruch 
     
        
     if (startBlockPos!=-1)
    	 System.out.println("Der gesuchte Block befindet sich in der Position : " + startBlockPos);
     else 
    	 System.out.println("Kein passender Block wurde gefunden");
     
    }
    


    /**
     * main-Funktion zum Starten des Programms aus der Komandozeile.
     * @param argv Kommandozeilenoptionen, werden nicht benötigt
     */
    
     public static void main(String[] argv){
	 	/* Der folgende Teil ist nötig für die Internationalisierung */
		Locale currentLocale; 
		currentLocale = Locale.getDefault();
		messages = ResourceBundle.getBundle("praktikum.localmessages",currentLocale); 
 
		/* Angegriffenen Initialisieren */
		Service serv = new Service();
		
		/* Rest der Main-Methode */
        new Aufgabe2(serv);
	}
}
