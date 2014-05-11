package test;

import java.math.BigInteger;

import praktikum.Service;

public class TestService4a extends Service {
	
	private String sequence = null;
	private int startBlockPos = -1;
	private int pos = 0;
	boolean streamEnded = false;
	
	public TestService4a(String sequence, int startBlockPos) {
		super();
		this.sequence = sequence;
		this.startBlockPos = startBlockPos;
	}
	
	@Override
	public BigInteger getNextCipherBlock(){
		BigInteger result = null;
		if(pos < sequence.length()) {
			result = generateCipherBlock((byte)sequence.charAt(pos));
			pos++;
		} else {
			streamEnded = true;
		}
		return result;
	}
	
	@Override
	public boolean checkStartBlockPos(int pos){
		return (startBlockPos == pos);
    }
	
	public boolean hasStreamEnded() {
		return streamEnded;
	}

}
