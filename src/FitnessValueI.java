
public interface FitnessValueI {
	
	/**
	 * Returns the fitnessValue for the specific individual
	 * @param geneticInformation the genetic information for the individual to get the fitness value
	 * for
	 * @return the fitness value as an double
	 */
	public double fitnessValue(boolean[][] geneticInformation);
}
