package praktikum;
/* Klasse mit Service-Funktionen für die Aufgaben 1 und 2 */
import java.math.BigInteger;
import java.util.*;

/**
 * Diese Klasse stellt Funktionen zur Verschlüsselung und Entschlüsselung zur Verfügung.
 * Der Konstuktor initialisiert einen Angegriffenen, indem dessen Schlüsselpaar generiert
 * wird, und damit ein Schlüsseltext erzeugt wird.
 */

public class Service {

    /* Fuer Internationalisierung */
    public static ResourceBundle messages;

    /**
     * Länge des RSA-Modulus, bzw des Ciphertextes in Bit
     */
    
    public static final int RSA_CIPHER_LEN = 256;


    /**
     * Länge des RSA-Klartextes in Bit. (wird erst bei der Schlüsselgenerierung ermittelt.)
     */
    
    public int RSA_PLAIN_LEN;


    /**
     * Der öffentliche Schlüssel des Angegriffenen.
     */
    
    RSAPublicKey pubRSAKey;


    /**
     * Der Schlüsseltext
     */
    
    BigInteger cipherText[];


    /**
     * Die Verschlüsselungsfunktion (wird beim Erstellen des Serviceobjektes mit dem
     * öffentlichen Schlüssel des angegriffenen vorinitialisiert)
     */
    
     RSA ciph;


    /** Anschalten von Debugausgaben */
    
    static boolean debug = false;

    /* For internationalization */
    public int ch;


    /**
     * Konstruktor, erzeugt das RSA-Schlüsselpaar des Angegriffenen und bildet einen
     * Schlüsseltext, der dann in Aufgabe 1 anzugreifen ist.
     */
    
    public Service(){

		/* Der folgende Teil ist nötig für die Internationalisierung */
		Locale currentLocale;
		currentLocale = Locale.getDefault();
		messages = ResourceBundle.getBundle("praktikum.localmessages",currentLocale);

		try{
	    	/* RSA-Schlüsselpaar generieren */
	    	RSAKeyGenerator kpGen = new RSAKeyGenerator(RSA_CIPHER_LEN);
	    	pubRSAKey = kpGen.getPublicKey();

	    	/* der Klartext (Eurocheque-Inhalt aus Versuchsaufgabe)*/
	    	String clearText = new String("DM  800  POSTGIROAMT HANNOVER  "
						  +"BLZ 250 100 30  KTO 62 95 25 304  HILDESHEIM  31 12 91 "
						  +"JOHANNA SONNENSCHEIN  LISTER MEILE 23  3000 HANNOVER");
	    	byte[] clearTextBytes = clearText.getBytes();

	    	/* Verschlüsseln des Klartextes mit RSA */
	    	ciph = new RSA(pubRSAKey);
	    	System.out.println(messages.getString("Service4aplainblk")+" "+ Integer.toString(ciph.getPlaintextSize()));
	    	RSA_PLAIN_LEN = ciph.getPlaintextSize();
	    	/* Pro Klartextblock nur ein  byte verwenden, um Angriff zu erleichtern */
	    	BigInteger[] expClearText = expandBlocks(clearTextBytes);
	    	System.out.println(messages.getString("Service4acipherblk")+" "+ Integer.toString(ciph.getCiphertextSize()));
	    	cipherText = ciph.encryptECB(expClearText);
		}catch(Exception e){
	    	System.out.println(messages.getString("Service4aErr1")+e);
	    	e.printStackTrace();
	    	System.exit(0);
		}
    }

    /**
     * Diese Methode gibt den öffentlichen RSA-Schlüssel aus.
     * @return den öffentlichen RSA-Schlüssel des Angegriffenen
     */
    
    public RSAPublicKey getPublicKey(){
		return pubRSAKey;
    }

    /**
     * Diese Methode gibt den Schlüsseltext zurück, der in Aufgabe 1 angegriffen werden soll.
     * @return den Schlüsseltext
     */
    
    public BigInteger[] getCipher(){
		return cipherText;
    }

    /**
     * Diese Methode wandelt jedes Byte der eingegebenen Bytefolge in einen
     * BigInteger um.
     * @param packet das Paket, das expandiert werden soll.
     * @return eine Folge BigInteger-Blöcke
     */
    
    public BigInteger[] expandBlocks(byte[] packet){
		try{
	    	BigInteger[] destPkt = new BigInteger[packet.length];

	    	for (int i = 0; i < packet.length; i++){
	    		destPkt[i] = BigInteger.valueOf(packet[i]);
	    	}
	    	return destPkt;
		}catch(Exception e){
	    	System.out.println(messages.getString("Service4aexpblks")+e);
	    	e.printStackTrace();
	    	System.exit(0);
		}
		return null;
    }

    /**
     * Gegenteil der Methode expandBlocks.
     * @param packet Eine Folge expandierter Blöcke
     * @return die wiederhergestellte Bytefolge.
     */
    
    public byte[] compressBlocks(BigInteger[] packet){
		try{
			byte[] result = new byte[packet.length]; 
	    	for(int i = 0; i < packet.length; i++){
	    		result[i] = packet[i].byteValue();
	    	}
	    	return result;
		}catch(Exception e){
	    	System.out.println(messages.getString("Service4acpblks")+e);
	    	System.exit(0);
		}
		return null;
    }

    /**
     * Diese Methode verschlüsselt einen Klartextblock mit dem öffentlichen Schlüssel des
     * Angegriffenen. Dieser Block darf nicht größer als die Klartextlänge RSA_PLAIN_LEN sein.
     * @param codeBlk der Klartextblock.
     * @return der Schlüsseltextblock
     */
    
    public BigInteger encrypt(BigInteger codeBlk){
		try{
	    	return ciph.encryptECB(new BigInteger[]{codeBlk})[0];
		}catch(Exception e){
	    	System.out.println(messages.getString("Service4aencrypt")+e);
	    	System.exit(0);
		}
		return null;
    }


    /* -------------------------- Aufgabe 2 ---------------------------------*/
    

    /** Interner Status des Generators des Streams von Schlüsseltextblöcken */
    
    int state = 0;


    /** Zufallsgenerator für Generierung des Schlüsseltextstreams */
    
    Random random = new Random(System.currentTimeMillis());


    /** Nummer des Formular-Startblocks im Schlüsseltextstream */
    
    int startBlockPos = -1;


    /** Blockzähler */
    
    int count = -1;


    /** Anzahl der Stellen des Geldbetrages (1 <= x <=4) */
    
    int digitCount = random.nextInt(4) + 1;


    /**
     * Gibt den jeweils nächsten Block des Schlüsseltextes für den Angriff in Aufgabe
     * 2 heraus. Der Beginn der
     * zum Formular gehörigen Codeblöcke wird jeweils zufällig gewählt.
     * @return einen Codeblock, oder <code>null</code>, falls das Ende des
     *         Datenstroms erreicht wurde.
     */
    
    public BigInteger getNextCipherBlock(){
        /* Format des Streams von Schlüsseltextblöcken:
	       1. zufällige Anzahl Zufall
               2. 'DM'
               3. 0-3x ' '
               4. 1-4xZiffer
               5. zufällige Anzahl Zufall */
		
        count ++;
        byte[] character = new byte[1];
        if(state == 0){
            if(random.nextInt(30) == 1){
                /* Beginn des Formulars mit 'D' */
                state = 1;
                startBlockPos = count;
                character[0] = (byte)'D';
				if(debug) System.out.println(messages.getString("Service4aciphstream")
					+new String(character)+"'");
                return generateCipherBlock(character[0]);
            }else{
                /* Zufällige Zeichen zu Beginn des Streams */
                character[0] = generateRandomByte();
				if(debug) System.out.println(messages.getString("Service4aciphstream")
					+new String(character)+"'");
                return generateCipherBlock(character[0]);
            }
        }
        else if(state == 1){
            /* Zweiter Formularbuchstabe 'M' */
            state = 2;
            character[0] = (byte)'M';
	    	if(debug) System.out.println(messages.getString("Service4aciphstream")
	    		+new String(character)+"'");
            return generateCipherBlock(character[0]);
        }
        else if(state == 2){
            if(count - startBlockPos >= 6){
                /* Erstes zufälliges Byte nach Ende der Betragszahl */
				state = 3;
                character[0] = generateRandomByte();
				if(debug) System.out.println(messages.getString("Service4aciphstream")
					+new String(character)+"'");
                return generateCipherBlock(character[0]);
            }
            else if(count - startBlockPos == 2 + (4 - digitCount)){
                /* Erstes Zeichen der Betragszahl */
                character[0] = (byte)(((byte)'1')+random.nextInt(9));
				if(debug) System.out.println(messages.getString("Service4aciphstream")
					+new String(character)+"'");
                return generateCipherBlock(character[0]);
            }
            else if(count - startBlockPos > 2 + (4 - digitCount)){
                /* Nicht-Erstes Zeichen der Betragszahl */
                character[0] = (byte)(((byte)'0')+random.nextInt(10));
				if(debug) System.out.println(messages.getString("Service4aciphstream")
					+new String(character)+"'");
                return generateCipherBlock(character[0]);
            }else{
                /* Leerzeichen zwischen 'DM' und Betragszahl */
                character[0] = (byte)' ';
				if(debug) System.out.println(messages.getString("Service4aciphstream")
					+new String(character)+"'");
				return generateCipherBlock(character[0]);
            }
        }
        else if(state == 3){
            if(random.nextInt(50) == 1){
                /* Ende des Schlüsseltextstromes */
                state = 4;
                return null;
            }else{
                /* Weitere zufällige Bytes nach Ende der Betragszahl */
                character[0] = generateRandomByte();
				if(debug) System.out.println(messages.getString("Service4aciphstream")
					+new String(character)+"'");
                return generateCipherBlock(character[0]);
            }
        }
        count--;
        return null;
    }


    /**
     * Generiert ein Zufallsbyte.
     * @return das Zufallsbyte
     */
    
    byte generateRandomByte(){
        byte[] b = new byte[1];
        do{
            random.nextBytes(b);
        }while((b[0] == 0) || (b[0] == 1));
        return b[0];
    }


    /**
     * Setzt den Generator für den Schlüsseltextstrom für Aufgabe 2 zurück,
     * so daß ein neuer Strom generiert werden kann.
     */
    
    public void resetStream(){
        state = 0;
        random.setSeed(System.currentTimeMillis());
        digitCount = random.nextInt(4) + 1;
    }


    /**
     * Diese Methode generiert einen Schlüsseltextblock für das angegebene Zeichen.
     * Zum Verschlüsseln wird der öffentliche Schlüssel des Angegriffenen benutzt.
     * (Wird in Aufgabe 2 benötigt.)
     * @param character das zu verschlüsselnde Zeichen
     */
    
    public BigInteger generateCipherBlock(byte character){
        try{
            byte b[] = new byte[1];
            b[0] = character;
            /* Pro Klartextblock nur ein (das letzte) Byte verwenden */
            
	    BigInteger[] expClearTextBytes = expandBlocks(b);
            return ciph.encryptECB(expClearTextBytes)[0];
        }catch(Exception e){
	    System.out.println(messages.getString("Service4agencphblk")+e.getMessage());
        }
        return null;
    }


    /**
     * Diese Methode ist zum Testen der Implementierung vorgesehen. Sie testet, ob die
     * eingegebene Startposition für den Codeblock korrekt ist. (Wird in Aufgabe 2
     * benötigt.)
     * @param pos die zu prüfende Position des Blockes im Strom von
     *               Schlüsseltextblöcken.
     * @return <code>true</code>, falls die eingegebene Zahl der Position des ersten Blockes
     *          aus dem CodeTextBlock entspricht, anderenfalls <code>false</code>
     */
    
    public boolean checkStartBlockPos(int pos){
        if((startBlockPos > -1) && (startBlockPos == pos)){
            return true;
        }
       return false;
    }
}

