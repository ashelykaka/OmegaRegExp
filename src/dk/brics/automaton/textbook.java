package dk.brics.automaton;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

public class textbook  extends Automaton{ 
	private Set<State> initial;
	private Set<State> states;
	Automaton a;
	public textbook() {
		initial = new HashSet<State>();
		states = new HashSet<State>();
	}
	public textbook(Automaton a) {
		initial = new HashSet<State>();
		states = new HashSet<State>();
		//super(a);
		this.a = a;
	}
	public Set<State> getInitial() {
		return initial;
	}
	public void setInitial(Set<State> initial) {
		this.initial = initial;
	}
	public Set<State> getStates() {
		return this.states;
	}
	public void setStates(Set<State> states) {
		this.states = states;
	}


	public textbook nbaUnion(textbook a1, textbook a2) {
		textbook union = new textbook();
		Set<State> initialStates = new HashSet<State>();
		initialStates.addAll(a1.getInitial());
		initialStates.addAll(a2.getInitial());
		union.setInitial(initialStates);
		Set<State> allStates = new HashSet<State>();
		allStates.addAll(a1.getStates());
		allStates.addAll(a2.getStates());
		union.setStates(allStates);
		//how about final state.
		return union;
	}

//change the nfa into which has no incoming transitions and is not final state
	public static textbook condition(Automaton a) {
		textbook nfa = new textbook(a);
		
		if (isValid(a)) {
			return nfa;
		} else {
			State initialState = a.getInitialState();
			Set<Transition> transitions = initialState.getTransitions();
			
			
			State newInitState = new State();
			nfa.initial.add(newInitState);
			newInitState.number = -1;
			System.out.println("initial " + nfa.initial);
			for (Transition transition : transitions) {
				newInitState.addTransition(transition);
			}
			return nfa;
		}
		
	}
	
//generate w-operator for NFA
	public static textbook nbaOmega(Automaton a) {
		textbook nfa = new textbook(a);
		
		if (!isValid(a)) {
			nfa = condition(a);
		}
			Set<State> liveStates = a.getLiveStates();
			State finalState = null;
			State initialState = a.getInitialState();
			for (State state : liveStates) {
				System.out.println(state);
//				if(state.isAccept())
//				{
//					System.out.println("state is accepted  " +state);
//					finalState = state;
////					state.setAccept(false);
////					finalState.setAccept(true);
////					System.out.println("state is changed  " +state);
//					System.out.println("finalState   " +finalState);
//				}
				Set<Transition> transitions = state.getTransitions(); 
				for (Transition transition : transitions) {
					System.out.println(state + "we have transitions  " + transition.getDest());	
					if (transition.getDest().isAccept())
					{
						System.out.println("one step before final state  " +state);	
//						  state.addTransition(transition);

					}
			}
		
		}
		return nfa;

	}
// test whether initial state has incoming edge
	private static boolean isValid(Automaton a) {
		State initialState = a.getInitialState();
		Set<State> liveStates = a.getLiveStates();
		for (State state : liveStates) {
			Set<Transition> transitions = state.getTransitions(); 
			for (Transition transition : transitions) {
				if (transition.getDest() == initialState)
					return false;
			}
		}

		return true;
	}
	public textbook nbaConcat(Automaton r, textbook s) {
		textbook conc = new textbook();
		// ...
		return conc;
	}
	
	public static void main(String[] args) {
		RegExp epr = new RegExp("a*b");
		Automaton automaton = epr.toAutomaton();
//		System.out.println(automaton);
//		
//		System.out.println(isValid(automaton));
		
		textbook nfa  = textbook.nbaOmega(automaton);
		System.out.println(nfa);
	}
	@Override
	public String toString() {
		return "textbook [initial=" + initial + ", states=" + states + ", a="
				+ a + "]";
	}	


}