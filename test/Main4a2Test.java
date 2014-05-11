package test;

import praktikum.Aufgabe2;
import junit.framework.TestCase;

public class Main4a2Test extends TestCase {
	
	// Test with usual sequence
	public void testFindSequence1() {
		TestService4a s = new TestService4a(
			"EEEEEEEDM 250XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", 7);
		new Aufgabe2(s);
		assertFalse(s.streamEnded);
	}

	// Test with no sequence
	public void testFindSequence2() {
		TestService4a s = new TestService4a(
			"EEEEEEE54823950743jfkleö8234ßXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", -1);
		new Aufgabe2(s);
		assertTrue(s.streamEnded);
	}

	// Test with DDM sequence
	public void testFindSequence3() {
		TestService4a s = new TestService4a(
			"EEEEEEDDM 250XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", 7);
		new Aufgabe2(s);
		assertFalse(s.streamEnded);
	}

	// Test with DM, but no number behind
	public void testFindSequence4() {
		TestService4a s = new TestService4a(
			"EEEEEEDM        XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", -1);
		new Aufgabe2(s);
		assertTrue(s.streamEnded);
	}
}
