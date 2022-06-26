package utilpack;

import static org.junit.Assert.*;

import org.junit.Test;

import utilpack.Expositor;

public class ExpositorTest {

	@Test
	public void column_name() {
		assertEquals("A", Expositor.getColumnName(0));
		assertEquals("#", Expositor.getColumnName(3));
	}
	
	@Test
	public void piece_name() {
		assertEquals("King", Expositor.getPieceName("K"));
		assertEquals("King", Expositor.getPieceName("k"));
		assertEquals("", Expositor.getPieceName(" "));
	}

	@Test(expected = NumberFormatException.class)
	public void empty_column() {
		assertNull(Expositor.getColumnName(new Integer(null)));
	}
	
	@Test(expected = NullPointerException.class)
	public void empty_piece() {
		assertNull(Expositor.getPieceName(null));
	}
	
}
