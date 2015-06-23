package de.uni_freiburg.proglang.aux;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.Stack;

/**
 * A pushback reader that supports arbitrary many marks, depending
 * on the buffer size of the pushback reader created.
 * 
 * In general the buffer for the marks is only heap-limited.
 * The only read function that is implemented is int read(), the others are unimplemented
 * until now. 
 * 
 * @author Robert Jakob
 *
 */
public class MarkPushbackReader extends PushbackReader {

	private Stack<Integer> marks;
	private Stack<Integer> buffer;

	/**
	 * Create a new MarkPushbackReader on top of an existing one.
	 * @param in The reader to decorate.
	 */
	public MarkPushbackReader(Reader in) {
		this(in, 10000);
	}
	
	/**
	 * Create a new MarkPushbackReader on top of an existing one.
	 * @param in The reader to decorate.
	 * @param size The size of the internal buffer for the pushback to be used.
	 */
	public MarkPushbackReader(Reader in, int size) {
		super(in, size);
		marks = new Stack<Integer>();
		buffer = new Stack<Integer>();
	}

	/**
	 * Adds a mark for the current position on top of the mark stack. 
	 */
	public void mark() {
		marks.push(buffer.size());
	}

	/**
	 * Removes the topmost mark without jumping back.
	 */
	public void killMark() {
		marks.pop();
		if (marks.isEmpty()) {
			buffer.clear();
		}
	}

	/**
	 * Reads a symbol from the decorated reader and
	 * buffers it if marks are set.
	 */
	@Override
	public int read() throws IOException {
		int c = super.read();
		if (c != -1 && !marks.isEmpty()) {
			buffer.push(c);
		}
		return c;
	}

	/**
	 * Puts back a symbol onto the internal reader.
	 * This might invalidate a mark.
	 */
	@Override
	public void unread(int c) throws IOException {
		super.unread(c);
		if (!buffer.empty()) {
			buffer.pop();
		}
	}

	/**
	 * Not supported.
	 */
	@Override
	public int read(char[] cbuf) throws IOException {
		throw new IllegalStateException("Not implemented");
	}

	/**
	 * Not supported.
	 */
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		throw new IllegalStateException("Not implemented");
	}

	/**
	 * Not supported.
	 */
	@Override
	public int read(CharBuffer target) throws IOException {
		throw new IllegalStateException("Not implemented");
	}

	/**
	 * Rewinds back to the topmost mark. If unread has been executed in between
	 * then the symbol is not rolled back again.
	 */
	@Override
	public void reset() throws IOException {
		while (buffer.size() > marks.peek()) {
			super.unread(buffer.pop());
		}
		
		killMark();
	}

	/**
	 * Checks if there are any marks.
	 * @return True if it has.
	 */
	public boolean hasMark() {
		return !marks.empty();
	}

	/**
	 * Checks if there is a next symbol.
	 * @return True if there is none.
	 * @throws IOException
	 */
	public boolean atEnd() throws IOException {
		int c = super.read();
		if (c == -1) {
			return true;
		} else {
			super.unread(c);
			return false;
		}
	}

	/**
	 * Gets a nice string representation of the first 20 symbols
	 * of the string and the internal buffer an marks.
	 * (Only good for debugging).
	 */
	public String toString() {
		Stack<Integer> buf = new Stack<Integer>();
		try {
			StringBuilder b = new StringBuilder();

			for (int i = 0; i <= 20; i++) {
				if (!atEnd()) {
					int c = super.read();
					buf.push(c);
					b.append((char) c);
				} else {
					break;
				}
			}
			
			while (!buf.empty()) {
				super.unread(buf.pop());
			}
			
			return b.toString() + "... B:" + buffer.toString() + " M:" + marks.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
			return "*** Unknown";
		}
	}
}
