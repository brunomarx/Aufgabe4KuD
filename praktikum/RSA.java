package praktikum;

import java.math.BigInteger;

/**
 * Diese Klasse implementiert das Konzellationssystem RSA im ECB-Modus.
 */
 
public class RSA{

	/** kann ENCRYPT oder DECRYPT sein */
	 
	int mode;
	/** Konstante für Verschlüsselungsmodus */
	
	public static final int ENCRYPT = 0;
	/** Konstante für Entschlüsselungsmodus */
	
	public static final int DECRYPT = 1;
    /** öffentlicher Schlüssel */
	
	RSAPublicKey pubKey;
	/** geheimer Schlüssel */
	
	RSAPrivateKey privKey;

	/** 
	 * Initialisiert RSA im Verschlüsselungsmodus.
	 * @param pubKey der öffentliche Schlüssel, der zum Verschlüsseln benutzt werden soll
	 */
	
	public RSA(RSAPublicKey pubKey){
		mode = ENCRYPT;
		this.pubKey = pubKey;
	}
	
	/**
	 * Initialisiert RSA im Entschlüsselungsmodus.
	 * @param privKey der geheime Schlüssel, der zum Entschlüsseln benutzt werden soll
	 */
	
	public RSA(RSAPrivateKey privKey){
		mode = DECRYPT;
		this.privKey = privKey;
	}

    /**
     * Diese Methode implementiert die Entschlüsselungsfunktion von RSA
     * im ECB-Modus.
     * @param blocks die verschlüsselte Nachricht
     * @return die Klartextnachricht
     * @throws Exception wenn RSA nicht für den Entschlüsselungsmodus initialisiert wurde.
     */
	
    public BigInteger[] decryptECB(BigInteger[] blocks)throws Exception{
		if(mode != DECRYPT) throw new Exception("RSA is not initialized for decryption.");
		
		BigInteger[] result = new BigInteger[blocks.length];
		
		for(int i = 0; i < blocks.length; i++){

			// Beschleunigte Entschlüsselungsfunktion
			BigInteger mP = blocks[i].modPow(privKey.dp, privKey.p);     // m^(e^-1 mod p-1) mod p
			BigInteger mQ = blocks[i].modPow(privKey.dq, privKey.q);     // das gleiche mit q

			BigInteger reste[] = {mP,mQ};                        // sigP und sigQ mit dem CRA
			BigInteger mods[] = {privKey.p,privKey.q};           // zusammenfassen
			result[i] = CRA(reste, mods);                  // Entschlüsselte Nachricht
		}
		return result;
    }

    /**
     * Diese Methode implementiert die Verschlüsselungsfunktion von RSA im ECB-Modus.
     * @param blocks der Klartext
     * @return die verschlüsselte Nachricht
     * @throws Exception wenn RSA nicht für den Verschlüsselungsmodus initialisiert wurde.
     */
	
    public BigInteger[] encryptECB(BigInteger[] blocks)throws Exception{
		if(mode != ENCRYPT) throw new Exception("RSA is not initialized for encryption.");
		
		BigInteger[] result = new BigInteger[blocks.length]; 
		
		for(int i = 0; i < blocks.length; i++){
    		result[i] = blocks[i].modPow(pubKey.e, pubKey.n);
		}
    	return result;
    }
    
    /**
     * @return die Länge eines Klartext-Blocks in byte.
     */
    
    public int getPlaintextSize(){
    	BigInteger n = null;
    	if(pubKey != null) n = pubKey.n;
    	if(privKey != null) n = privKey.n;
    	return n.bitLength() - 1;
    }
    
    /**
     * @return die Länge eines Schlüsseltext-Blocks in byte.
     */
    
    public int getCiphertextSize(){
		BigInteger n = null;
		if(pubKey != null) n = pubKey.n;
		if(privKey != null) n= privKey.n;
		return n.bitLength();
    }
    
    private BigInteger CRA(BigInteger[] r, BigInteger[] m){
    	BigInteger M = m[0];               
    	BigInteger x = r[0];
	    
    	for(int i = 1; i < r.length; i++){
    		
    		// Euklid
    		BigInteger a0 = m[i];                                  // Initialisierungen
    		BigInteger a1 = M;
    		BigInteger s0 = BigInteger.valueOf(1);
    		BigInteger s1 = BigInteger.valueOf(0);
    			
    		while ((a1.compareTo(BigInteger.valueOf(0))!= 0)){   
    		    BigInteger q = a0.divide(a1);                   
    		    BigInteger buffer = a0;                         
    		    a0 = a1;
    		    a1 = buffer.subtract(a1.multiply(q));
    		    buffer = s0;                                    
    		    s0 = s1;
    		    s1 = buffer.subtract(s1.multiply(q));     
    		}
    		// a0 ist das Ergebnis (das Inverse)
    		BigInteger inverse = null;
    	    if(a0.compareTo(BigInteger.valueOf(1)) == 0){
    	    	inverse = s0;                   
    	    } else {
    	    	System.out.println("Teiler von n gefunden...???");
    	    	System.exit(0);
    	    }
    	    BigInteger h = inverse.multiply(r[i].subtract(x)).mod(m[i]);
    	    x = x.add(h.multiply(M));                  
    	    M = M.multiply(m[i]);                  
    	}
    	x = x.mod(M);
    	return x;
	}	
    
    
    /**
     * Testmethode fürs debugging.
     * @param argv nicht benutzt
     */
    
    public static void main(String argv[]){
    	try{
	    	RSAKeyGenerator rkg = new RSAKeyGenerator(400);
    		// encrypt
    		RSA rsaE = new RSA(rkg.getPublicKey());
    		BigInteger[] input = new BigInteger[4];
    		input[0] = new BigInteger("0123456");
    		input[1] = new BigInteger("7891011");
    		input[2] = new BigInteger("1213141");
    		input[3] = new BigInteger("5161718");
    		BigInteger ciphertext[] = rsaE.encryptECB(input);
    		// decrypt
    		RSA rsaD = new RSA(rkg.getPrivateKey());
    		BigInteger plaintext[] = rsaD.decryptECB(ciphertext);
    		System.out.print("Original text :"); 
    		for(int i = 0; i < 4; i++) {
    			System.out.print(input[i]);
    		}
    		System.out.print("\n");
    		System.out.print("Decrypted text:");
    		for(int i = 0; i < 4; i++) {
    			System.out.print(plaintext[i]);
    		}
    		System.out.print("\n");
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
}
