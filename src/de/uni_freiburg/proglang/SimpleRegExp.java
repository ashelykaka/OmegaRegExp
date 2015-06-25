package de.uni_freiburg.proglang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dk.brics.automaton.Automaton;


public class SimpleRegExp {
	private SimpleRegExp r;

	public enum Kind {
		REGEXP_UNION,
		REGEXP_CONCATENATION,
		REGEXP_STAR,
		REGEXP_CHAR,
		REGEXP_EMPTY,
		REGEXP_EMPTYSTRING,
	}
	
	public Kind kind;
	public SimpleRegExp exp1, exp2;
	public char c;
	
	/** 
	 * Constructs string from parsed regular expression. 
	 */
	@Override
	public String toString() {
		return toStringBuilder(new StringBuilder()).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + c;
		result = prime * result + ((exp1 == null) ? 0 : exp1.hashCode());
		result = prime * result + ((exp2 == null) ? 0 : exp2.hashCode());
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleRegExp other = (SimpleRegExp) obj;
		if (c != other.c)
			return false;
		if (exp1 == null) {
			if (other.exp1 != null)
				return false;
		} else if (!exp1.equals(other.exp1))
			return false;
		if (exp2 == null) {
			if (other.exp2 != null)
				return false;
		} else if (!exp2.equals(other.exp2))
			return false;
		if (kind != other.kind)
			return false;
		return true;
	}

	StringBuilder toStringBuilder(StringBuilder b) {
		switch (kind) {
		case REGEXP_UNION:
			b.append("(");
			exp1.toStringBuilder(b);
			b.append("+");
			exp2.toStringBuilder(b);
			b.append(")");
			break;
		case REGEXP_CONCATENATION:
			if (exp1.kind == Kind.REGEXP_EMPTYSTRING)
				exp2.toStringBuilder(b);
			else if (exp2.kind == Kind.REGEXP_EMPTYSTRING)
				exp1.toStringBuilder(b);
			else {			
			b.append("(");
			exp1.toStringBuilder(b);
			b.append(".");
			exp2.toStringBuilder(b);
			b.append(")");
			}
			break;
		case REGEXP_STAR:
//			b.append("(");
			exp1.toStringBuilder(b);
//			b.append(")");
			b.append("*");
			break;
		case REGEXP_CHAR:
			b.append(c);
			break;
		case REGEXP_EMPTY:
			b.append("{}");
			break;
		case REGEXP_EMPTYSTRING:
			b.append("ε");
			break;			
		}
		return b;
	}
	public boolean canBeEmptyString() {
		
		if(kind == Kind.REGEXP_STAR || kind == Kind.REGEXP_EMPTYSTRING)
			return true;
		else if(kind == Kind.REGEXP_CHAR ){
			return false;
		}
		else{
//			System.out.println("Kind: " + kind +  " exp1: " + exp1 + " exp2:"+ exp2);
			if (exp1.canBeEmptyString() && exp2.canBeEmptyString())
				return true;
			else
				return false;
		}
		
	}

	public static SimpleRegExp makeUnion(SimpleRegExp left, SimpleRegExp right) {
		SimpleRegExp expunion = new SimpleRegExp();
		expunion.kind = SimpleRegExp.Kind.REGEXP_UNION;
		expunion.exp1= left;
		expunion.exp2= right;
		return expunion;
	}
	static public SimpleRegExp makeConcatenation(SimpleRegExp left, SimpleRegExp right) {
		SimpleRegExp hui = new SimpleRegExp();
		hui.exp1 = left;
		hui.exp2 = right;
		hui.kind = Kind.REGEXP_CONCATENATION;
		return hui;
	}
	public static SimpleRegExp makeStar(SimpleRegExp left) {
		SimpleRegExp expystar = new SimpleRegExp();		
		expystar.kind = SimpleRegExp.Kind.REGEXP_STAR;
		expystar.exp1= left;		
		return expystar;
	}

	public static SimpleRegExp makeChar(char d) {
		SimpleRegExp charone = new SimpleRegExp();
		charone.c = d;
		charone.kind = SimpleRegExp.Kind.REGEXP_CHAR;		
		return charone;
	}
	
	public Set<Character> getIdentifiers() {
		HashSet<Character> res = new HashSet<Character>();
		switch (this.kind) {
		case REGEXP_CHAR:
			res.add(c);
			break;
		case REGEXP_CONCATENATION:
		case REGEXP_UNION:
			res.addAll(exp1.getIdentifiers());
			res.addAll(exp2.getIdentifiers());
			break;
		case REGEXP_EMPTY:
			break;
		case REGEXP_EMPTYSTRING:
			break;
		case REGEXP_STAR:
			res.addAll(exp1.getIdentifiers());
			break;
		default:
			throw new IllegalArgumentException("Unknown kind");
		}
		
		return res;
	}

	/**
	 * @return Creates a regular expression representing the empty language.
	 */
	public static SimpleRegExp makeEmpty() {
		SimpleRegExp r = new SimpleRegExp();
		r.kind = Kind.REGEXP_EMPTY;
		return r;
	}
	
	/**
	 * @return Creates a regular expression representing the empty string.
	 */
	public static SimpleRegExp makeEpsilon() {
		SimpleRegExp r = new SimpleRegExp();
		r.kind = Kind.REGEXP_EMPTYSTRING;
		return r;
	}


}
