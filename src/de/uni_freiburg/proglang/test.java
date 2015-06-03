package de.uni_freiburg.proglang;

import hui.NBA;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExpHelper;

public class test {

	public static void main(String[] args) {
			
		
//			testomegalinearfactor1();
//			testomegalinearfactor2();
		
			testregexp();
	}	

	private static void testregexp() {
		HuiRegExp charX = HuiRegExp.makechar('X');
		Automaton aut = RegExpHelper.HuiRegExpToAutomaton(charX);
		System.out.println(aut.toDot());
	}

	static void testlinearfactor() {		
		
		//char x
		HuiRegExp charX = HuiRegExp.makechar('X');				
		//char y
		HuiRegExp charY = HuiRegExp.makechar('Y');		
	
		HuiRegExp  expleft =  HuiRegExp.star(charX);
		
		HuiRegExp expXX = HuiRegExp.concatenate(charX, charX);		
		HuiRegExp expystar = HuiRegExp.star(charY);
		
		HuiRegExp expright = HuiRegExp.union(expXX, expystar);
		
		HuiRegExp expall = HuiRegExp.concatenate(expleft, expright);
		
		System.out.println(expall);
		
		System.out.println(LinearFactor.compute(expall));

		
	}
	private static void testomegalinearfactor1() {
		HuiRegExp chara = HuiRegExp.makechar('a');				
		//char y
		HuiRegExp charb = HuiRegExp.makechar('b');		
	
		HuiRegExp  expleft =  HuiRegExp.union(chara,charb);
		HuiRegExp  expstar =  HuiRegExp.star(expleft);
		
		HuiRegExp exptotal = HuiRegExp.concatenate(expstar, charb);	
		System.out.println(exptotal);
		NBA nba = new NBA(expstar, charb);
		System.out.println(nba);

	}
	private static void testomegalinearfactor2() {
		HuiRegExp chara = HuiRegExp.makechar('a');				
		//char y
		HuiRegExp charb = HuiRegExp.makechar('b');		
	
		HuiRegExp  expleft =  HuiRegExp.union(chara,charb);
		HuiRegExp  expstar =  HuiRegExp.star(expleft);
		HuiRegExp  charbstar =  HuiRegExp.star(charb);
		HuiRegExp expright = HuiRegExp.concatenate(charb, charbstar);	
		
		HuiRegExp exptotal = HuiRegExp.concatenate(expstar, expright);	
		System.out.println(exptotal);
		
		NBA nba = new NBA(expstar, expright);
		System.out.println(nba);
	}
}
