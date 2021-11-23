package util;

import static org.junit.Assert.*;

import org.junit.Test;

import utilpack.Message;

public class MessageTest {

	@Test
	public void testColName() {
		assertEquals("A", Message.colName(0));
		assertEquals("4", Message.colName(3));
		assertEquals("", Message.colName(9));
	}
	
	@Test
	public void testPieceName() {
		assertEquals("King", Message.pieceName("K"));
		assertEquals("King", Message.pieceName("k"));
		assertEquals("", Message.pieceName(" "));
	}

	@Test(expected = NumberFormatException.class)
	public void testEmptyCol() {
		assertNull(Message.colName(new Integer(null)));
	}
	
	@Test(expected = NullPointerException.class)
	public void testEmptyPiece() {
		assertNull(Message.pieceName(null));
	}
	
}
