package de.uni_freiburg.proglang.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;

import de.uni_freiburg.proglang.HuiRegExp;
import de.uni_freiburg.proglang.HuiRegExp.Kind;
import de.uni_freiburg.proglang.aux.MarkPushbackReader;
import de.uni_freiburg.proglang.aux.Pair;

public class OmegaRegExpParserTest {

	private static MarkPushbackReader mkReader(String s) {
		return new MarkPushbackReader(new StringReader(s));
	}
	
	@Test
	public void testParseSingleOmegaRegExp() throws IOException {
		String s = "((a.b*)+c*).(a+b)*^w";
		MarkPushbackReader reader = mkReader(s);
		Pair<HuiRegExp, HuiRegExp> omega = OmegaRegExpParser.parseSingleOmegaRegExp(reader);
		assertEquals("<((a.b*)+c*),(a+b)*>", omega.toString());
	}
	
	@Test
	public void testParseOmegaRegExp() throws IOException {
		String s = "((a.b*)+c*).(a+b)*^w + a.b^w";
		MarkPushbackReader reader = mkReader(s);
		List<Pair<HuiRegExp, HuiRegExp>> lst = OmegaRegExpParser.parseOmegaRegExp(reader);
		assertEquals("[<((a.b*)+c*),(a+b)*>, <a,b>]", lst.toString()); // Yes I know that is bad.
	}
	
	@Test
	public void testParseEmptyLanguage() throws IOException {

		MarkPushbackReader reader = mkReader("0");

		HuiRegExp r = OmegaRegExpParser.parseEmptyLanguage(reader);
		assertEmptyLanguageRegExp(r);

		reader.unread('1');
		r = OmegaRegExpParser.parseEmptyLanguage(reader);
		assertNull(r);

		try {
			OmegaRegExpParser.parseEmptyLanguage(mkReader(""));
			fail("Expected IllegalStateException");
		} catch (IllegalStateException e) {
			// Everything fine
		}
	}

	@Test
	public void testParseEpsilon() throws IOException {

		MarkPushbackReader reader = mkReader("1");

		HuiRegExp r = OmegaRegExpParser.parseEpsilon(reader);
		assertEpsilonRegExp(r);

		reader.unread('0');
		r = OmegaRegExpParser.parseEpsilon(reader);
		assertNull(r);

		try {
			OmegaRegExpParser.parseEmptyLanguage(mkReader(""));
			fail("Expected IllegalStateException");
		} catch (IllegalStateException e) {
			// Everything fine
		}
	}

	@Test
	public void testParseChar() throws IOException {
		MarkPushbackReader reader = mkReader("a");

		HuiRegExp r = OmegaRegExpParser.parseChar(reader);
		assertEquals('a', r.c);

		reader.unread('A');
		r = OmegaRegExpParser.parseChar(reader);
		assertNull(r);

		try {
			OmegaRegExpParser.parseChar(mkReader(""));
			fail("Expected IllegalStateException");
		} catch (IllegalStateException e) {
			// Everything fine
		}
	}

	@Test
	public void testParseWhiteSpaces() throws IOException {
		MarkPushbackReader reader = mkReader("         a");

		OmegaRegExpParser.parseWhiteSpaces(reader);
		HuiRegExp r = OmegaRegExpParser.parseChar(reader);
		assertCharRegExp('a', r);

		reader = mkReader("a    c");
		OmegaRegExpParser.parseWhiteSpaces(reader);
		r = OmegaRegExpParser.parseChar(reader);
		assertCharRegExp('a', r);

		OmegaRegExpParser.parseWhiteSpaces(mkReader(""));
	}

	@Test
	public void testParseIgnoreChar() throws IOException {
		MarkPushbackReader reader = mkReader("(a");

		assertTrue(OmegaRegExpParser.parseIgnoreChar(reader, '('));
		HuiRegExp r = OmegaRegExpParser.parseChar(reader);
		assertCharRegExp('a', r);

		reader = mkReader(")");
		assertFalse(OmegaRegExpParser.parseIgnoreChar(reader, '('));
		assertEquals(')', reader.read());

		try {
			OmegaRegExpParser.parseIgnoreChar(mkReader(""), '(');
			fail("Expected IllegalStateException");
		} catch (IllegalStateException e) {
			// Everything fine
		}
	}

	@Test
	public void testParseRegExp() throws IOException {
		HuiRegExp r = OmegaRegExpParser.parse("a");
		assertCharRegExp('a', r);

		r = OmegaRegExpParser.parse("0");
		assertEmptyLanguageRegExp(r);

		r = OmegaRegExpParser.parse("1");
		assertEpsilonRegExp(r);

		r = OmegaRegExpParser.parse("y");
		assertCharRegExp('y', r);

		String s = "(a*.b*)*";
		r = OmegaRegExpParser.parse(s);
		assertEquals(s,r.toString());
		
		s = "(a*+b)";
		r = OmegaRegExpParser.parse(s);
		assertEquals(s,r.toString());
		
		s = "(a+b*)";
		r = OmegaRegExpParser.parse(s);
		assertEquals(s,r.toString());
		
		s = "((a**.b**)**+(c.d))";
		r = OmegaRegExpParser.parse(s);
		assertEquals(s,r.toString());
	}

	@Test
	public void testParseConcat() throws IOException {
		HuiRegExp r = OmegaRegExpParser.parseConcat(mkReader("(a.b)"));
		assertEquals(HuiRegExp.Kind.REGEXP_CONCATENATION, r.kind);
		assertCharRegExp('a', r.exp1);
		assertCharRegExp('b', r.exp2);

		r = OmegaRegExpParser.parseConcat(mkReader("(  a .   b  )"));
		assertEquals(HuiRegExp.Kind.REGEXP_CONCATENATION, r.kind);
		assertCharRegExp('a', r.exp1);
		assertCharRegExp('b', r.exp2);

		MarkPushbackReader reader = mkReader("(aa)");
		r = OmegaRegExpParser.parseConcat(reader);
		assertNull(r);
		assertFalse(reader.hasMark());
		assertEquals('(', reader.read());
		assertEquals('a', reader.read());
		assertEquals('a', reader.read());
		assertEquals(')', reader.read());

		reader = mkReader("(a.(a))");
		r = OmegaRegExpParser.parseRegExp(reader);
		assertNull(r);
		assertFalse(reader.hasMark());
		assertEquals('(', reader.read());
		assertEquals('a', reader.read());
		assertEquals('.', reader.read());
		assertEquals('(', reader.read());
		assertEquals('a', reader.read());
		assertEquals(')', reader.read());
		assertEquals(')', reader.read());
		
		reader = mkReader("(a + a)");
		r = OmegaRegExpParser.parseConcat(reader);
		assertNull(r);
	}

	@Test
	public void testParseUnion() throws IOException {
		HuiRegExp r = OmegaRegExpParser.parseUnion(mkReader("(a+b)"));
		assertEquals(HuiRegExp.Kind.REGEXP_UNION, r.kind);
		assertCharRegExp('a', r.exp1);
		assertCharRegExp('b', r.exp2);

		r = OmegaRegExpParser.parseUnion(mkReader("(  a +   b  )"));
		assertEquals(HuiRegExp.Kind.REGEXP_UNION, r.kind);
		assertCharRegExp('a', r.exp1);
		assertCharRegExp('b', r.exp2);

		MarkPushbackReader reader = mkReader("(aa)");
		r = OmegaRegExpParser.parseUnion(reader);
		assertNull(r);
		assertFalse(reader.hasMark());
		assertEquals('(', reader.read());
		assertEquals('a', reader.read());
		assertEquals('a', reader.read());
		assertEquals(')', reader.read());

		reader = mkReader("(a+(a))");
		r = OmegaRegExpParser.parseUnion(reader);
		assertNull(r);
		assertFalse(reader.hasMark());
		assertEquals('(', reader.read());
		assertEquals('a', reader.read());
		assertEquals('+', reader.read());
		assertEquals('(', reader.read());
		assertEquals('a', reader.read());
		assertEquals(')', reader.read());
		assertEquals(')', reader.read());
		
		reader = mkReader("(a . a)");
		r = OmegaRegExpParser.parseUnion(reader);
		assertNull(r);
	}
	
	@Test
	public void testParseStar() throws IOException {
		HuiRegExp r = OmegaRegExpParser.parseStar(mkReader("a*"));
		assertEquals(HuiRegExp.Kind.REGEXP_STAR, r.kind);
		assertCharRegExp('a', r.exp1);
		
		r = OmegaRegExpParser.parseStar(mkReader("1*"));
		assertEquals(HuiRegExp.Kind.REGEXP_STAR, r.kind);
		assertEpsilonRegExp(r.exp1);
		
		r = OmegaRegExpParser.parseStar(mkReader("(a.b)*"));
		assertEquals(HuiRegExp.Kind.REGEXP_STAR, r.kind);
		assertEquals(Kind.REGEXP_CONCATENATION, r.exp1.kind);
		assertCharRegExp('a', r.exp1.exp1);
		assertCharRegExp('b', r.exp1.exp2);
		
		r = OmegaRegExpParser.parseStar(mkReader("a**"));
		assertEquals(HuiRegExp.Kind.REGEXP_STAR, r.kind);
		assertEquals(Kind.REGEXP_STAR, r.exp1.kind);
		assertCharRegExp('a', r.exp1.exp1);
		
		r = OmegaRegExpParser.parseStar(mkReader("(a.b)**"));
		assertEquals(HuiRegExp.Kind.REGEXP_STAR, r.kind);
		assertEquals(Kind.REGEXP_CONCATENATION, r.exp1.exp1.kind);
		assertCharRegExp('a', r.exp1.exp1.exp1);
		assertCharRegExp('b', r.exp1.exp1.exp2);
	}

	private static void assertCharRegExp(char exp, HuiRegExp actual) {
		assertEquals(Kind.REGEXP_CHAR, actual.kind);
		assertEquals(exp, actual.c);
	}

	private static void assertEmptyLanguageRegExp(HuiRegExp actual) {
		assertEquals(Kind.REGEXP_EMPTY, actual.kind);
	}

	private static void assertEpsilonRegExp(HuiRegExp actual) {
		assertEquals(Kind.REGEXP_EMPTYSTRING, actual.kind);
	}
}
