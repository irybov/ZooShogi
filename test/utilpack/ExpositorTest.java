package utilpack;

import static org.junit.Assert.*;

import org.junit.Test;

import utilpack.Expositor;

public class ExpositorTest {

	@Test
	public void testColumnName() {
		assertEquals("A", Expositor.getColumnName(0));
		assertEquals("#", Expositor.getColumnName(3));
	}
	
	@Test
	public void testPieceName() {
		assertEquals("King", Expositor.getPieceName("K"));
		assertEquals("King", Expositor.getPieceName("k"));
		assertEquals("", Expositor.getPieceName(" "));
	}

	@Test(expected = NumberFormatException.class)
	public void testEmptyColumn() {
		assertNull(Expositor.getColumnName(new Integer(null)));
	}
	
	@Test(expected = NullPointerException.class)
	public void testEmptyPiece() {
		assertNull(Expositor.getPieceName(null));
	}
	
}
