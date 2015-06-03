package de.uni_freiburg.proglang;

import java.util.HashSet;
import java.util.Set;

public class LinearFactor {
	@Override
	public String toString() {
		return "<" + symbol + "," + exp + ">";
	}

	char symbol;
	HuiRegExp exp;
	public LinearFactor(char symbol, HuiRegExp exp) {
		super();
		this.symbol = symbol;
		this.exp = exp;
	}
	
	public static Set<LinearFactor> compute(HuiRegExp inp) {
		Set<LinearFactor> r = new HashSet<LinearFactor>();
		switch(inp.kind) {
		case REGEXP_EMPTY:
			return r;
		case REGEXP_EMPTYSTRING:
			return r;
		case REGEXP_CHAR:
			HuiRegExp huiRegExp = new HuiRegExp();
			huiRegExp.kind = HuiRegExp.Kind.REGEXP_EMPTYSTRING;
			r.add(new LinearFactor(inp.c, huiRegExp));
			break;
		case REGEXP_UNION:
			r.addAll(compute(inp.exp1));
			r.addAll(compute(inp.exp2));
			break;
		case REGEXP_STAR:
			Set<LinearFactor> LFs = compute(inp.exp1);
			for (LinearFactor lf : LFs) {
				r.add(new LinearFactor(lf.symbol, HuiRegExp.concatenate(lf.exp, inp)));
			}
			break;
		case REGEXP_CONCATENATION:
			Set<LinearFactor> left = compute(inp.exp1);
			for (LinearFactor lf : left) {
				r.add(new LinearFactor(lf.symbol, HuiRegExp.concatenate(lf.exp, inp.exp2)));
			}
			if (inp.exp1.canBeEmptyString()) {
				r.addAll(compute(inp.exp2));
			}

			break;
		default:
			throw new IllegalArgumentException("unknown case for regexp");
			
		}
		return r;
	}

	private static HuiRegExp createConcat(HuiRegExp exp2, HuiRegExp exp22) {
		return HuiRegExp.concatenate(exp2, exp22);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exp == null) ? 0 : exp.hashCode());
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
		LinearFactor other = (LinearFactor) obj;
		if (exp == null) {
			if (other.exp != null)
				return false;
		} else if (!exp.equals(other.exp))
			return false;
		if (symbol != other.symbol)
			return false;
		return true;
	}
	
	
}
