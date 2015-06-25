package dk.brics.automaton;

import de.uni_freiburg.proglang.SimpleRegExp;

public class RegExpHelper {

	public static SimpleRegExp createRegExpFromHuiRegExp(SimpleRegExp r) {
		switch (r.kind) {
		case REGEXP_CHAR:
			return SimpleRegExp.makeChar(r.c);
		case REGEXP_CONCATENATION:
			SimpleRegExp r1 = createRegExpFromHuiRegExp(r.exp1);
			SimpleRegExp r2 = createRegExpFromHuiRegExp(r.exp2);
			return SimpleRegExp.makeConcatenation(r1,r2);
		case REGEXP_EMPTY:
			//return RegExp.makeEmpty();
			throw new IllegalArgumentException("Not supported");
		case REGEXP_EMPTYSTRING:
			return SimpleRegExp.makeEmpty();
		case REGEXP_STAR:
			r1 = createRegExpFromHuiRegExp(r.exp1);
			return SimpleRegExp.makeStar(r1);
		case REGEXP_UNION:
			r1 = createRegExpFromHuiRegExp(r.exp1);
			r2 = createRegExpFromHuiRegExp(r.exp2);
			return SimpleRegExp.makeUnion(r1,r2);
		default:
			throw new IllegalArgumentException("Not supported");
		}
	}
	
	public static Automaton HuiRegExpToAutomaton(SimpleRegExp r) {
		
		return createRegExpFromHuiRegExp(r).toAutomaton();
	}
	
	public static void enforceCondition(Automaton a) {
		State q0 = a.getInitialState();
		// task 1
	}
}
