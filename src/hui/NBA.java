package hui;
import java.util.HashSet;
import java.util.Set;

import de.uni_freiburg.proglang.HuiRegExp;
import de.uni_freiburg.proglang.OmegaLinearFactor;
import dk.brics.automaton.Transition;

public class NBA  {
	private Set<nbaState> initial;
	private Set<nbaState> allStates;
	private Set<nbaState> finalStates;
	HashSet<Character> identifiers = new HashSet<Character>();	  

	public NBA() {
		initial = new HashSet<nbaState>();
		allStates = new HashSet<nbaState>();
		finalStates = new HashSet<nbaState>();

	}

	@Override
	public String toString() {
		String s =  " NBA [ allStates=" + allStates+ "\n identifiers =" + identifiers ;

		for (nbaState nbaState : allStates) {
			s+= ("\nfrom  " + nbaState + " Transactions: to");
			for (Character c : identifiers) {
				Set<nbaState> transationStates = trans (nbaState, c);
				if(!transationStates.isEmpty())
					s+= (" nbaState: " + transationStates + " \n");
			}
		}

		s += ( "\n initial=" + initial + "\n finalStates=" + finalStates +"]");
		return s;
	}
	public Set<nbaState> getInitial() {
		return initial;
	}
	public void setInitial(Set<nbaState> set) {
		this.initial = set;
	}
	public Set<nbaState> getNbaStates() {
		return allStates;
	}
	public void setNbaStates(Set<nbaState> states) {
		this.allStates = states;
	}

	public NBA(HuiRegExp re, HuiRegExp re1) {
		this();
		//the nbaInitialStates are Omega_LF(r.s^w)
		Set<OmegaLinearFactor> nbaInitialStates = OmegaLinearFactor.compute(re, re1);
		Set<OmegaLinearFactor> nbaInitialStates2 = OmegaLinearFactor.compute(re, re1);
		for (OmegaLinearFactor omegaLinearFactor : nbaInitialStates) {
			initial.add(new nbaState(omegaLinearFactor));
		}
		for (OmegaLinearFactor omegaLinearFactor : nbaInitialStates2) {
			allStates.add(new nbaState(omegaLinearFactor));
		}
		//NbaStates are all states that appear in Omega_automata

		identifiers.addAll(re.getIdentifiers());
		identifiers.addAll(re1.getIdentifiers());


		//continues to get new states 
		Set<nbaState> newTransationStates = null;
		do {
			newTransationStates = new HashSet<nbaState>();
			for (nbaState state : allStates) {

				//					System.out.println(allStates);

				for (Character c : identifiers) {
					//get new transationStates
					Set<nbaState> transationStates = trans (state, c);
					for (nbaState nbaState : transationStates) {
						if (!allStates.contains(nbaState))
							newTransationStates.add(nbaState) ;							
					}
					//testing all the possible transitions 
					for (nbaState transationState : transationStates) {
						state.addTransition(new Transition(c, transationState));
					}
				}

			}
			allStates.addAll(newTransationStates);
		} while (!newTransationStates.isEmpty());

		for (nbaState State : allStates) {
			if(State.omegaLinearFactor.g == true)
				finalStates.add(State);

		}
	}


	//if the first char fits,then return all states of Omega_LF(r.s^w)	
	public Set<nbaState> trans(nbaState state, char id) {
		Set<nbaState> result = new HashSet<nbaState>();
		if (state.omegaLinearFactor.symbol==id ) {
			Set<OmegaLinearFactor> compute = OmegaLinearFactor.compute(state.omegaLinearFactor.r, state.omegaLinearFactor.s);
			for (OmegaLinearFactor omegaLinearFactor : compute) {
				result.add(new nbaState(omegaLinearFactor));
			}
		} 
		return result;

	}

}
