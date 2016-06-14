import java.awt.RadialGradientPaint;
import java.util.Random;

/**
 * should probably be abstract....
 * @author Mattis
 *
 */
public class Individual {
	private boolean[][] bitString; // the "genetic information" where the first part represents
	// the number of "numbers" one wants and the second part are the bits that represents that
	// specific number, if using decimal numbers one could just let every single "decimal part"
	// be number by their own and then just "assemble" them when running the fitness function
	
	private FitnessValueI ownFitnessFunction; // TODO: ugly and inefficient! this individuals
	// fitness function
	private Accessories a;
	Random random;
	
	
	public Individual(boolean[][] information){
		this.bitString = information;
		this.a = Accessories.getInstance();
		this.random = a.getRandom();
	}
	
	public Individual(boolean[][] information, FitnessValueI fitnessFunction){
		this.bitString = information;
		this.ownFitnessFunction = fitnessFunction;
		this.a = Accessories.getInstance();
		this.random = a.getRandom();
	}
	
	/**
	 * Makes a "deep copy" of the individual entered, in other words it will not reference the
	 * same array but an identical one
	 * @param individualToCopy
	 */
	public Individual(Individual individualToCopy){
		boolean[][] copyBooleanArray = individualToCopy.bitString;
		this.bitString = new boolean[copyBooleanArray.length][copyBooleanArray[0].length];
		this.ownFitnessFunction = individualToCopy.ownFitnessFunction;
		this.a = Accessories.getInstance();
		this.random = a.getRandom();
		
		for(int i = 0; i < copyBooleanArray.length; i++){
			for(int j = 0; j < copyBooleanArray[i].length; j++){
				this.bitString[i][j] = copyBooleanArray[i][j];
			}
		}
	}
	
	/**
	 * @return the FitnessValueI used by this individual for fitness scoring
	 */
	public FitnessValueI getOwnFitnessFunction() {
		return ownFitnessFunction;
	}
	
	/**
	 * @return the fitness value for this individual
	 */
	public double fitnessValue(){
		return ownFitnessFunction.fitnessValue(this.bitString);
	}
	
	public boolean[][] getGeneticInformation(){
		return this.bitString;
	}
	
	/**
	 * Mutates the individual, with the current implementation it just changes a single(randomly
	 * selected) bit in a single number(randomly selected)
	 */
	public void mutateIndividual(){
		int mutateNr = random.nextInt(bitString.length);
		int mutateBit = random.nextInt(bitString[mutateNr].length);
		
		this.bitString[mutateNr][mutateBit] = !this.bitString[mutateNr][mutateBit];
	}
	
	
	
	public Individual[] mateWith(Individual individual) throws java.lang.IllegalArgumentException{
		Individual[] offSpring = new Individual[2];
		
		// TODO: vill förmodligen inte alltid ha samma crossover punkt här....
		
		int crossNumber = random.nextInt(this.bitString.length);
		int crossBit = this.bitString[0].length/2; // random.nextInt(this.bitString[0].length);
		
		
		
		
		offSpring[0] = crossover(this, individual, crossNumber, crossBit);
		offSpring[1] = crossover(individual, this, crossNumber, crossBit);
		
		
		return offSpring;
	}
	
	
	
	
	/**
	 * Takes two individuals and mates them and returns one of the offsprings.
	 * 
	 * TODO: Går nog att finna en mer effektiv lösning än den nuvarande som fungerar som så att man inprincip bara splittar på
	 * hälten på ett visst nummer där man sedan tar alla nummer efter det nummret från nummer1 och den andra hälten från nummer2 och
	 * vice versa
	 * 
	 * @requires ind1.bitString[0].length == ind2.bitString[0].length
	 * @param individual1 the first "parent" to be used in the creation of the new
	 * individual
	 * @param individual2 the second "parent" to be used in the creation of the new
	 * individual
	 * @return a new individual created using the two individuals genetic information
	 * 
	 * 
	 * 
	 * Code to test the function with:
	 * boolean[][] gI1 = new boolean[1][10];
		boolean[][] gI2 = new boolean[1][10];
		Random rand = new Random();
		for(int i = 0; i < gI1[0].length; i++){
			boolean bit = rand.nextBoolean();
			gI1[0][i] = true;// bit;
			gI2[0][i] = false;// !bit;
		}
		
		Individual ind1 = new Individual(gI1);
		Individual ind2 = new Individual(gI2);
		System.out.println("\nindividual 1: ");
		printIndividual(ind1);
		System.out.println("\nindividual 2: ");
		printIndividual(ind2);
		System.out.println("\nThe result of crossing them at bit 5");
		printIndividual(ga.crossover(ind1, ind2, 0, 5));
		System.out.println("\nAnd the reverse");
		printIndividual(ga.crossover(ind2, ind1, 0, 5));
	 */
	private Individual crossover(Individual individual1, Individual individual2, int number,
			int bitNr) throws java.lang.IllegalArgumentException{
		boolean[][] ind1 = individual1.getGeneticInformation();
		boolean[][] ind2 = individual2.getGeneticInformation();
		if(ind1[0].length != ind2[0].length || ind1.length != ind2.length){
			throw new java.lang.IllegalArgumentException("individual1 and individual2 contains " + 
					"genetic information of different lengths");
		}
		boolean[][] newInformation = new boolean[ind1.length][ind1[0].length];
		
		
		for(int i = 0; i < ind1.length; i++){
			for(int j = 0; j < ind1[i].length; j++){
				if(i < number){
					newInformation[i][j] = ind1[i][j];
				} else{
					if(j < bitNr){
						newInformation[i][j] = ind1[i][j];
					} else{
						newInformation[i][j] = ind2[i][j];
					}
				}
			}
		}
		
		
		
		return new Individual(newInformation, individual1.getOwnFitnessFunction());
	}
	
	
	
	
	
	
	
	
	
	
}
