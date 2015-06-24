package hui;

import de.uni_freiburg.proglang.OmegaLinearFactor;
import dk.brics.automaton.State;

@SuppressWarnings("serial")
public class NbaState extends State {
	OmegaLinearFactor omegaLinearFactor;
	 @Override
	public String toString() {
		return "nbaState [omegaLinearFactor=" + omegaLinearFactor +  "]\n";
	}

	public NbaState(OmegaLinearFactor s2) {
			super();
			this.omegaLinearFactor = s2;
		}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((omegaLinearFactor == null) ? 0 : omegaLinearFactor
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		NbaState other = (NbaState) obj;
		if (omegaLinearFactor == null) {
			if (other.omegaLinearFactor != null)
				return false;
		} else if (!omegaLinearFactor.equals(other.omegaLinearFactor))
			return false;
		return true;
	}

 
}
