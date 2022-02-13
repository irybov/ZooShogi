package utilpack;

import static org.junit.Assert.*;

import org.junit.Test;

import utilpack.Message;

public class MessageTest {

	@Test
	public void testColumnName() {
		assertEquals("A", Message.getColumnName(0));
		assertEquals("#", Message.getColumnName(3));
	}
	
	@Test
	public void testPieceName() {
		assertEquals("King", Message.getPieceName("K"));
		assertEquals("King", Message.getPieceName("k"));
		assertEquals("", Message.getPieceName(" "));
	}

	@Test(expected = NumberFormatException.class)
	public void testEmptyColumn() {
		assertNull(Message.getColumnName(new Integer(null)));
	}
	
	@Test(expected = NullPointerException.class)
	public void testEmptyPiece() {
		assertNull(Message.getPieceName(null));
	}
	
}
