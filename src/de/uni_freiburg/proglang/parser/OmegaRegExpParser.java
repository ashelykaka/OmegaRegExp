package de.uni_freiburg.proglang.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import de.uni_freiburg.proglang.HuiRegExp;
import de.uni_freiburg.proglang.aux.MarkPushbackReader;
import de.uni_freiburg.proglang.aux.Pair;

/**
 * Parses a string representing an omega regular expression.
 * 
 * Omega regular expressions obey the following grammar:
 * 
 * a,b = 0 | (a + b) | r.a | s^w
 * 
 * where r and s are standard regular expressions. We ignore the empty language
 * 0.
 * 
 * r = 0 | 1 | c | (r.s) | (r + s) | r*
 * 
 * where c is a lower case letter from the English alphabet.
 * 
 * @author jakobro
 */
public class OmegaRegExpParser {

	public static HuiRegExp parse(final String s) throws IOException {
		return parseRegExp(new MarkPushbackReader(new StringReader(s)));
	}

	public static List<Pair<HuiRegExp, HuiRegExp>> parseOmegaRegExp(
			final MarkPushbackReader reader) throws IOException {
		ArrayList<Pair<HuiRegExp, HuiRegExp>> res = new ArrayList<Pair<HuiRegExp, HuiRegExp>>(
				20);

		boolean done = false;

		do {

			Pair<HuiRegExp, HuiRegExp> pair = parseSingleOmegaRegExp(reader);
			if (pair == null) {
				return null;
			} else {
				res.add(pair);
			}

			parseWhiteSpaces(reader);

			if (!reader.atEnd() && parseIgnoreChar(reader, '+')) {
				parseWhiteSpaces(reader);
				done = false;
			} else {
				done = true;
			}

		} while (!done);

		return res;
	}

	public static Pair<HuiRegExp, HuiRegExp> parseSingleOmegaRegExp(
			final MarkPushbackReader reader) throws IOException {
		HuiRegExp r1 = parseRegExp(reader);
		if (r1 != null) {
			parseWhiteSpaces(reader);

			if (parseIgnoreChar(reader, '.')) {
				parseWhiteSpaces(reader);
				HuiRegExp r2 = parseRegExp(reader);
				if (r2 != null && parseIgnoreChar(reader, '^')
						&& parseIgnoreChar(reader, 'w')) {
					return new Pair<HuiRegExp, HuiRegExp>(r1, r2);
				}
			}
		}

		return null;
	}

	public static HuiRegExp parseRegExp(final MarkPushbackReader reader)
			throws IOException {

		HuiRegExp r;

		if ((r = parseStar(reader)) != null) {
			return r;
		} else if ((r = parseRegExpNoStar(reader)) != null) {
			return r;
		} else {
			return null;
		}
	}

	public static HuiRegExp parseRegExpNoStar(final MarkPushbackReader reader)
			throws IOException {
		HuiRegExp r;

		if ((r = parseConcat(reader)) != null) {
			return r;
		} else if ((r = parseUnion(reader)) != null) {
			return r;
		} else if ((r = parseEpsilon(reader)) != null) {
			return r;
		} else if ((r = parseChar(reader)) != null) {
			return r;
		} else if ((r = parseEmptyLanguage(reader)) != null) {
			return r;
		} else {
			return null;
		}
	}

	private static int readOne(final MarkPushbackReader reader, String exp)
			throws IOException {
		int c = reader.read();
		if (c == -1) {
			throw new IllegalStateException(
					"Reached end of stream but expected " + exp);
		}

		return c;
	}

	public static void parseWhiteSpaces(final MarkPushbackReader reader)
			throws IOException {
		boolean isWhitespace = true;
		while (isWhitespace) {
			int c = reader.read();
			if (c == -1) {
				return;
			}
			isWhitespace = Character.isWhitespace((char) c);

			if (!isWhitespace) {
				reader.unread(c);
			}
		}
	}

	public static boolean parseIgnoreChar(final MarkPushbackReader reader,
			char d) throws IOException {
		int i = readOne(reader, d+"");
		char c = (char) i;
		if (c == d) {
			return true;
		} else {
			reader.unread(i);
			return false;
		}
	}

	public static HuiRegExp parseChar(final MarkPushbackReader reader)
			throws IOException {
		int c = readOne(reader, "a symbol between 'a' and 'z'.");

		if ('a' <= c && c <= 'z') {
			return HuiRegExp.makechar((char) c);
		} else {
			reader.unread(c);
			return null;
		}
	}

	public static HuiRegExp parseEmptyLanguage(final MarkPushbackReader reader)
			throws IOException {
		if (parseIgnoreChar(reader, '0')) {
			return HuiRegExp.makeEmpty();
		} else {
			return null;
		}
	}

	public static HuiRegExp parseEpsilon(final MarkPushbackReader reader)
			throws IOException {
		if (parseIgnoreChar(reader, '1')) {
			return HuiRegExp.makeEpsilon();
		} else {
			return null;
		}
	}

	public static HuiRegExp parseStar(final MarkPushbackReader reader)
			throws IOException {
		reader.mark();
		HuiRegExp r;
		if ((r = parseRegExpNoStar(reader)) != null) {

			// Now we read all * at the end and stack things up.
			boolean found = false;

			while (!reader.atEnd() && parseIgnoreChar(reader, '*')) {
				found = true;
				r = HuiRegExp.star(r);
			}

			if (found) {
				reader.killMark();
				return r;
			}

		}

		reader.reset();
		return null;
	}

	public static HuiRegExp parseConcat(final MarkPushbackReader reader)
			throws IOException {

		reader.mark();

		if (parseIgnoreChar(reader, '(')) {
			parseWhiteSpaces(reader);

			HuiRegExp r = parseRegExp(reader);
			if (r != null) {
				parseWhiteSpaces(reader);
				if (parseIgnoreChar(reader, '.')) {
					parseWhiteSpaces(reader);

					HuiRegExp s = parseRegExp(reader);
					if (s != null) {
						parseWhiteSpaces(reader);

						if (parseIgnoreChar(reader, ')')) {
							reader.killMark();
							return HuiRegExp.concatenate(r, s);
						}
					}
				}
			}
		}

		reader.reset();

		return null;
	}

	public static HuiRegExp parseUnion(final MarkPushbackReader reader)
			throws IOException {

		reader.mark();

		if (parseIgnoreChar(reader, '(')) {
			parseWhiteSpaces(reader);

			HuiRegExp r = parseRegExp(reader);
			if (r != null) {
				parseWhiteSpaces(reader);
				if (parseIgnoreChar(reader, '+')) {
					parseWhiteSpaces(reader);

					HuiRegExp s = parseRegExp(reader);
					if (s != null) {
						parseWhiteSpaces(reader);

						if (parseIgnoreChar(reader, ')')) {
							reader.killMark();
							return HuiRegExp.union(r, s);
						}
					}
				}
			}
		}

		reader.reset();

		return null;
	}
}
