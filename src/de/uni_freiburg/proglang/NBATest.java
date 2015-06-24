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
		RegExp chara = RegExp.makeChar('a');				
		//char y
		RegExp charb = RegExp.makeChar('b');		
	
		RegExp  expleft =  RegExp.makeUnion(chara,charb);
		RegExp  expstar =  RegExp.makeStar(expleft);
		
		RegExp exptotal = RegExp.makeConcatenation(expstar, charb);	
		Automaton aut = RegExpHelper.HuiRegExpToAutomaton(exptotal);
		System.out.println(aut.getAcceptStates());
		System.out.println(aut.toDot());
	}

	static void testlinearfactor() {		
		
		//char x
		RegExp charX = RegExp.makeChar('X');				
		//char y
		RegExp charY = RegExp.makeChar('Y');		
	
		RegExp  expleft =  RegExp.makeStar(charX);
		
		RegExp expXX = RegExp.makeConcatenation(charX, charX);		
		RegExp expystar = RegExp.makeStar(charY);
		
		RegExp expright = RegExp.makeUnion(expXX, expystar);
		
		RegExp expall = RegExp.makeConcatenation(expleft, expright);
		
		System.out.println(expall);
		
		System.out.println(LinearFactor.compute(expall));

		
	}
	private static void testomegalinearfactor1() {
		RegExp chara = RegExp.makeChar('a');				
		//char y
		RegExp charb = RegExp.makeChar('b');		
	
		RegExp  expleft =  RegExp.makeUnion(chara,charb);
		RegExp  expstar =  RegExp.makeStar(expleft);
		
		RegExp exptotal = RegExp.makeConcatenation(expstar, charb);	
		System.out.println(exptotal);
		NBA nba = new NBA(expstar, charb);
		System.out.println(nba);

	}
	private static void testomegalinearfactor2() {
		RegExp chara = RegExp.makeChar('a');				
		//char y
		RegExp charb = RegExp.makeChar('b');		
	
		RegExp  expleft =  RegExp.makeUnion(chara,charb);
		RegExp  expstar =  RegExp.makeStar(expleft);
		RegExp  charbstar =  RegExp.makeStar(charb);
		RegExp expright = RegExp.makeConcatenation(charb, charbstar);	
		
		RegExp exptotal = RegExp.makeConcatenation(expstar, expright);	
		System.out.println(exptotal);
		
		NBA nba = new NBA(expstar, expright);
		System.out.println(nba);
	}
}
