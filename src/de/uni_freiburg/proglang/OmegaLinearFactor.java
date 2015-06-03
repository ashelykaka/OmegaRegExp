package de.uni_freiburg.proglang;

import java.util.HashSet;
import java.util.Set;

import de.uni_freiburg.proglang.HuiRegExp.Kind;

public class OmegaLinearFactor {
	@Override
	public String toString() {
		if (r.kind == Kind.REGEXP_EMPTYSTRING)
			return "<" + symbol + "," + s + "^ω" + ","+ g+  ">";
		else if (s.kind == Kind.REGEXP_EMPTYSTRING)
			return "<" + symbol + "," + r + "^ω" + ","+ g+  ">";
		
		return "<" + symbol + "," + r  +  s + "^ω" + ","+ g+  ">";

	}
	public char symbol;
	public HuiRegExp r;
	public HuiRegExp s;
	public boolean g;
	public OmegaLinearFactor(char symbol, HuiRegExp r, HuiRegExp s, boolean g) {
		super();
		this.symbol = symbol;
		this.r = r;
		this.s = s;
		this.g = g;
	}
	
	public static Set<OmegaLinearFactor> compute(HuiRegExp r, HuiRegExp s) {
		HashSet<OmegaLinearFactor> result = new HashSet<OmegaLinearFactor>();
		for (LinearFactor lf : LinearFactor.compute(r)) {
			result.add(new OmegaLinearFactor(lf.symbol, lf.exp, s, false));
		}
		if (r.canBeEmptyString()) {
			for (LinearFactor lf : LinearFactor.compute(s)) {
				result.add(new OmegaLinearFactor(lf.symbol, lf.exp, s, true));
			}
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (g ? 1231 : 1237);
		result = prime * result + ((r == null) ? 0 : r.hashCode());
		result = prime * result + ((s == null) ? 0 : s.hashCode());
		result = prime * result + symbol;
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
		OmegaLinearFactor other = (OmegaLinearFactor) obj;
		if (g != other.g)
			return false;
		if (r == null) {
			if (other.r != null)
				return false;
		} else if (!r.equals(other.r))
			return false;
		if (s == null) {
			if (other.s != null)
				return false;
		} else if (!s.equals(other.s))
			return false;
		if (symbol != other.symbol)
			return false;
		return true;
	}

}
