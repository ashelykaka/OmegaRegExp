package hui;
import java.util.HashSet;
import java.util.Set;

import de.uni_freiburg.proglang.HuiRegExp;
import de.uni_freiburg.proglang.OmegaLinearFactor;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.Transition;

public class NBA  {
	private Set<NbaState> initial;
	private Set<NbaState> allStates;
	private Set<NbaState> finalStates;
	HashSet<Character> identifiers = new HashSet<Character>();	  

	public NBA() {
		initial = new HashSet<NbaState>();
		allStates = new HashSet<NbaState>();
		finalStates = new HashSet<NbaState>();

    }
	
	public NBA(Automaton a) {
		// Just assume NFA must fulfill condition (1)
		// task 2
	}
	
	public NBA(Automaton a, NBA b) {
	    // task 4
	}

	@Override
	public String toString() {
		String s =  " NBA [ allStates=" + allStates+ "\n identifiers =" + identifiers ;

		for (NbaState nbaState : allStates) {
			s+= ("\nfrom  " + nbaState + " Transactions: to");
			for (Character c : identifiers) {
				Set<NbaState> transationStates = trans (nbaState, c);
				if(!transationStates.isEmpty())
					s+= (" nbaState: " + transationStates + " \n");
			}
		}

		s += ( "\n initial=" + initial + "\n finalStates=" + finalStates +"]");
		return s;
	}
	public Set<NbaState> getInitial() {
		return initial;
	}
	public void setInitial(Set<NbaState> set) {
		this.initial = set;
	}
	public Set<NbaState> getNbaStates() {
		return allStates;
	}
	public void setNbaStates(Set<NbaState> states) {
		this.allStates = states;
	}

	public NBA(HuiRegExp re, HuiRegExp re1) {
		this();
		//the nbaInitialStates are Omega_LF(r.s^w)
		Set<OmegaLinearFactor> nbaInitialStates = OmegaLinearFactor.compute(re, re1);
		Set<OmegaLinearFactor> nbaInitialStates2 = OmegaLinearFactor.compute(re, re1);
		for (OmegaLinearFactor omegaLinearFactor : nbaInitialStates) {
			initial.add(new NbaState(omegaLinearFactor));
		}
		for (OmegaLinearFactor omegaLinearFactor : nbaInitialStates2) {
			allStates.add(new NbaState(omegaLinearFactor));
		}
		//NbaStates are all states that appear in Omega_automata

		identifiers.addAll(re.getIdentifiers());
		identifiers.addAll(re1.getIdentifiers());


		//continues to get new states 
		Set<NbaState> newTransationStates = null;
		do {
			newTransationStates = new HashSet<NbaState>();
			for (NbaState state : allStates) {

				//					System.out.println(allStates);

				for (Character c : identifiers) {
					//get new transationStates
					Set<NbaState> transationStates = trans (state, c);
					for (NbaState nbaState : transationStates) {
						if (!allStates.contains(nbaState))
							newTransationStates.add(nbaState) ;							
					}
					//testing all the possible transitions 
					for (NbaState transationState : transationStates) {
						state.addTransition(new Transition(c, transationState));
					}
				}

			}
			allStates.addAll(newTransationStates);
		} while (!newTransationStates.isEmpty());

		for (NbaState State : allStates) {
			if(State.omegaLinearFactor.g == true)
				finalStates.add(State);

		}
	}


	//if the first char fits,then return all states of Omega_LF(r.s^w)	
	public Set<NbaState> trans(NbaState state, char id) {
		Set<NbaState> result = new HashSet<NbaState>();
		if (state.omegaLinearFactor.symbol==id ) {
			Set<OmegaLinearFactor> compute = OmegaLinearFactor.compute(state.omegaLinearFactor.r, state.omegaLinearFactor.s);
			for (OmegaLinearFactor omegaLinearFactor : compute) {
				result.add(new NbaState(omegaLinearFactor));
			}
		} 
		return result;

	}

}
