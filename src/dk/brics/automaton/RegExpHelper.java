package dk.brics.automaton;

import de.uni_freiburg.proglang.HuiRegExp;

public class RegExpHelper {

	public static RegExp createRegExpFromHuiRegExp(HuiRegExp r) {
		switch (r.kind) {
		case REGEXP_CHAR:
			return RegExp.makeChar(r.c);
		case REGEXP_CONCATENATION:
			RegExp r1 = createRegExpFromHuiRegExp(r.exp1);
			RegExp r2 = createRegExpFromHuiRegExp(r.exp2);
			return RegExp.makeConcatenation(r1,r2);
		case REGEXP_EMPTY:
			//return RegExp.makeEmpty();
			throw new IllegalArgumentException("Not supported");
		case REGEXP_EMPTYSTRING:
			return RegExp.makeEmpty();
		case REGEXP_STAR:
			r1 = createRegExpFromHuiRegExp(r.exp1);
			return RegExp.makeRepeat(r1);
		case REGEXP_UNION:
			r1 = createRegExpFromHuiRegExp(r.exp1);
			r2 = createRegExpFromHuiRegExp(r.exp2);
			return RegExp.makeUnion(r1,r2);
		default:
			throw new IllegalArgumentException("Not supported");
		}
	}
	
	public static Automaton HuiRegExpToAutomaton(HuiRegExp r) {
		return createRegExpFromHuiRegExp(r).toAutomaton();
	}
	
	public static void enforceCondition(Automaton a) {
		State q0 = a.getInitialState();
		// task 1
	}
}
