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
		assertEquals('c',mpr.peak());
		mpr.reset();
		assertEquals('b',mpr.peak());
		mpr.reset();
		assertEquals('a',mpr.peak());
		mpr.close();
	}

}
