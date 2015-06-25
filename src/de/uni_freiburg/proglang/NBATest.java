package de.uni_freiburg.proglang;

import hui.NBA;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExpHelper;

public class NBATest {

	public static void main(String[] args) {
			
		
//			testomegalinearfactor1();
//			testomegalinearfactor2();
		
			testregexp();
	}	

	private static void testregexp() {
		SimpleRegExp chara = SimpleRegExp.makeChar('a');				
		//char y
		SimpleRegExp charb = SimpleRegExp.makeChar('b');		
	
		SimpleRegExp  expleft =  SimpleRegExp.makeUnion(chara,charb);
		SimpleRegExp  expstar =  SimpleRegExp.makeStar(expleft);
		
		SimpleRegExp exptotal = SimpleRegExp.makeConcatenation(expstar, charb);	
		Automaton aut = RegExpHelper.SimpleRegExpToAutomaton(exptotal);
		System.out.println(aut.getAcceptStates());
		System.out.println(aut.toDot());
	}

	static void testlinearfactor() {		
		
		//char x
		SimpleRegExp charX = SimpleRegExp.makeChar('X');				
		//char y
		SimpleRegExp charY = SimpleRegExp.makeChar('Y');		
	
		SimpleRegExp  expleft =  SimpleRegExp.makeStar(charX);
		
		SimpleRegExp expXX = SimpleRegExp.makeConcatenation(charX, charX);		
		SimpleRegExp expystar = SimpleRegExp.makeStar(charY);
		
		SimpleRegExp expright = SimpleRegExp.makeUnion(expXX, expystar);
		
		SimpleRegExp expall = SimpleRegExp.makeConcatenation(expleft, expright);
		
		System.out.println(expall);
		
		System.out.println(LinearFactor.compute(expall));

		
	}
	private static void testomegalinearfactor1() {
		SimpleRegExp chara = SimpleRegExp.makeChar('a');				
		//char y
		SimpleRegExp charb = SimpleRegExp.makeChar('b');		
	
		SimpleRegExp  expleft =  SimpleRegExp.makeUnion(chara,charb);
		SimpleRegExp  expstar =  SimpleRegExp.makeStar(expleft);
		
		SimpleRegExp exptotal = SimpleRegExp.makeConcatenation(expstar, charb);	
		System.out.println(exptotal);
		NBA nba = new NBA(expstar, charb);
		System.out.println(nba);

	}
	private static void testomegalinearfactor2() {
		SimpleRegExp chara = SimpleRegExp.makeChar('a');				
		//char y
		SimpleRegExp charb = SimpleRegExp.makeChar('b');		
	
		SimpleRegExp  expleft =  SimpleRegExp.makeUnion(chara,charb);
		SimpleRegExp  expstar =  SimpleRegExp.makeStar(expleft);
		SimpleRegExp  charbstar =  SimpleRegExp.makeStar(charb);
		SimpleRegExp expright = SimpleRegExp.makeConcatenation(charb, charbstar);	
		
		SimpleRegExp exptotal = SimpleRegExp.makeConcatenation(expstar, expright);	
		System.out.println(exptotal);
		
		NBA nba = new NBA(expstar, expright);
		System.out.println(nba);
	}
}
