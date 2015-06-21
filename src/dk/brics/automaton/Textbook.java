package dk.brics.automaton;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class Textbook {

	private Automaton a;

	public Textbook(Automaton a) {
		this.a = a;
	}

	public Textbook nbaUnion(Textbook a2) {
		//		textbook union = new textbook();
		//		Set<State> initialStates = new HashSet<State>();
		//		initialStates.addAll(a1.getInitial());
		//		initialStates.addAll(a2.getInitial());
		//		union.setInitial(initialStates);
		//		Set<State> allStates = new HashSet<State>();
		//		allStates.addAll(a1.getStates());
		//		allStates.addAll(a2.getStates());
		//		union.setStates(allStates);
		//		// how about final state.
		//		return union;

		return new Textbook(this.a.union(a2.a));
	}

	// change the nfa into which has no incoming transitions and is not final
	// state
	public void fixCondition() {
		if (!this.isValid()) {
			State initialState = this.a.getInitialState();
			Set<Transition> transitions = initialState.getTransitions();

			State newInitState = new State();
			newInitState.number = -1;
			this.a.setInitialState(newInitState);
			for (Transition transition : transitions) {
				newInitState.addTransition(transition);
			}
		}

	}

	// generate w-operator for NFA
	public void nbaOmega() {
		fixCondition();

		Set<State> liveStates = a.getLiveStates();
		State initialState = a.getInitialState();

		HashMap<State, Transition> newTrans = new HashMap<State, Transition>();

		for (State state : liveStates) {
			// System.out.println(state);

			Set<Transition> transitions = state.getTransitions();
			for (Transition transition : transitions) {
				// System.out.println(state + "we have transitions  "
				// + transition.getDest());
				if (transition.getDest().isAccept()) {
					System.out.println("one step before final state  " + state
							+ " with final state " + transition.getDest()
							+ " and symbol " + transition.getMin() + " "
							+ transition.getMax());
					Transition t1 = new Transition(transition.getMin(),
							transition.getMax(), initialState);
					newTrans.put(state, t1);
				}
			}
		}

		for (State state : liveStates) {
			// change final state
			if (state.isAccept())
				state.setAccept(false);
		}
		initialState.setAccept(true);
		for (Entry<State, Transition> entry : newTrans.entrySet()) {
			State from = entry.getKey();
			Transition t = entry.getValue();

			from.addTransition(t);
		}
	}

	// test whether initial state has incoming edge
	private boolean isValid() {
		State initialState = this.a.getInitialState();
		Set<State> liveStates = this.a.getLiveStates();
		for (State state : liveStates) {
			Set<Transition> transitions = state.getTransitions();
			for (Transition transition : transitions) {
				if (transition.getDest() == initialState)
					return false;
			}
		}

		return true;
	}

	public static Textbook nbaConcat(Automaton r, Textbook s) {

		Set<State> liveStates = r.getLiveStates();

		HashMap<State, Transition> newTrans = new HashMap<State, Transition>();

		for (State state : liveStates) {
			// System.out.println(state);

			Set<Transition> transitions = state.getTransitions();
			for (Transition transition : transitions) {
				// System.out.println(state + "we have transitions  "
				// + transition.getDest());
				if (transition.getDest().isAccept()) {
					System.out.println("one step before final state  " + state
							+ " with final state " + transition.getDest()
							+ " and symbol " + transition.getMin() + " "
							+ transition.getMax());
					Transition t1 = new Transition(transition.getMin(),
							transition.getMax(), s.a.getInitialState());
					newTrans.put(state, t1);
				}
			}
		}
		for (State state : liveStates) {
			// change final state
			if (state.isAccept())
				state.setAccept(false);
		}
		for (Entry<State, Transition> entry : newTrans.entrySet()) {
			State from = entry.getKey();
			Transition t = entry.getValue();

			from.addTransition(t);
		}
			s.a.setInitialState(r.getInitialState());

			State initialState = s.a.getInitialState();
			System.out.println("initialState "+ initialState);

//			System.out.println("s "+ s);
			return s;
			//		return null;
		}

		public static void main(String[] args) {
			RegExp epr = new RegExp("(a|b)*b");
			RegExp epr2 = new RegExp("a*");
			// epr = new RegExp("a*(c|d|e)");
			RegExp epr3 = new RegExp("(ab)*");

			Automaton automaton = epr.toAutomaton();
//			Textbook nba = new Textbook(automaton);
			Textbook nba2 = new Textbook(epr2.toAutomaton());
//			nba.nbaOmega();
			nba2.nbaOmega();
			System.out.println("NFA "+ automaton.toDot());
//			System.out.println("(ab)* "+ epr3.toAutomaton().toDot());
//			System.out.println("(ab)w "+ nba2.toDot());

			System.out.println(Textbook.nbaConcat(automaton, nba2).toDot());

			//		Textbook newnba = nba.nbaUnion(nba2);
			//		System.out.println(nba.toDot());
					System.out.println(nba2.toDot());
		}

		@Override
		public String toString() {
			return "textbook [initial=" + this.a.getInitialState() + ", states="
					+ this.a.getStates() + "]";
		}

		public String toDot() {
			return a.toDot();
		}

	}