package de.uni_freiburg.proglang.aux;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

import org.junit.Test;

public class MarkPushbackReaderTest {

	@Test
	public void testMark() throws IOException {
		MarkPushbackReader mpr = new MarkPushbackReader(new StringReader("abc"));
		mpr.mark();
		mpr.read();
		mpr.mark();
		mpr.read();
		mpr.mark();
		mpr.read();
		assertTrue(mpr.atEnd());
		mpr.reset();
		assertEquals("c",mpr.toString());
		mpr.reset();
		assertEquals("bc",mpr.toString());
		mpr.reset();
		assertEquals("abc",mpr.toString());
	}

}
