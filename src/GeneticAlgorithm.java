import java.util.Random;

import javax.swing.DebugGraphics;

/**
 * An interesting note is that it helps more to have an large population than it does to have an
 * greater amount of tournaments, atleast with the current setup
 * @author Mattis
 *
 */
public class GeneticAlgorithm {
	
	private Individual[] individuals; // represents every individual solution where you access it
	// as, invididuals[specificIndividual][aSingleBitOfTheIndividualsInformation].
	// NOTE: the number of individuals must with the current implementation be even
	
	private int geneticInformationSize = 10; // the number of bits to be used in the "solution-
	// string"
	private int numberOfNumbers = 1; // the amount of numbers to be used, eg. if we need
	// an x, y and z value, this would be three as there is three individual numbers we want to
	// find(assuming it is integers we are after)
	
	private int tournamentSize = 2; // the number of individuals competing
	private int nrOfTournaments = 100; // the termination condition that specifies for how many
	// "runs" we will loop through to find the solution
	private double likelyHoodOfWeakWinner = 0.001; // the likely hood that the weaker of two
	// individuals should win the tournament
	
	private double mutationProb = 0.1; // the likely hood of an individual in the population
	// gets mutated
	private Random random;
	
	Accessories a;
	
	private FitnessValueI fitnessFunction;
	
	
	private int[] randomizedPopulation; // will contain the index to be used to get an individual
	// from the population when performing the tournament and thereby make sure that there is some
	// non-determinism when choosing two individuals to fight
	
//	private Individual bestIndividualSoFar;
	
	/**
	 * Returns the fitness value for the individual represented by the boolean array and/or at
	 * the specified index
	 * @param individual the individual represented as an boolean array
	 * @param individualIndex the index of the individual
	 * @return the fitness value of the individual as an int
	 */
	// protected int fitnessValue(Individual individual, int individualIndex);
	
	
	public GeneticAlgorithm(int populationSize, int geneticInformationSize, int tournamentSize,
			int numberOfTournaments, int nrOfNumbers, FitnessValueI fitnessFunction){
		this.individuals = new Individual[populationSize];
		this.geneticInformationSize = geneticInformationSize;
		this.tournamentSize = tournamentSize;
		this.nrOfTournaments = numberOfTournaments;
		this.fitnessFunction = fitnessFunction;
		this.a = Accessories.getInstance();
		this.random = a.getRandom();
		this.numberOfNumbers = nrOfNumbers;
		
		initiatePopulation();
	}
	
	/**
	 * USED PRIMARILY FOR TESTING
	 * @param populationSize
	 * @param geneticInformationSize
	 * @param tournamentSize
	 * @param numberOfTournaments
	 * @param fitnessFunction
	 */
	public GeneticAlgorithm(Individual[] pop, int geneticInformationSize, int tournamentSize,
			int numberOfTournaments){
		this.individuals = pop;
		
		
		this.geneticInformationSize = geneticInformationSize;
		this.tournamentSize = tournamentSize;
		this.nrOfTournaments = numberOfTournaments;
		this.a = Accessories.getInstance();
		this.random = a.getRandom();
		
	}
	
	/**
	 * initiates the population
	 * TODO: går förmodligen att optimera här så man har lättare att nå en optimal lösning
	 */
	private void initiatePopulation(){
		for(int i = 0; i < individuals.length; i++){
			boolean[][] tempString = new boolean[numberOfNumbers][geneticInformationSize];
			for(int x = 0; x < numberOfNumbers; x++){
				for(int j = 0; j < geneticInformationSize; j++){
					tempString[x][j] = random.nextBoolean();
				}
			}
			this.individuals[i] = new Individual(tempString, fitnessFunction);
		}
	}
	
	/**
	 * Finds an candidate for the problem
	 * @return the candidate represented as an boolean array
	 */
	public Individual findSolution(){
		Individual bestIndividualSoFar; // could have that this is an "global" variable that i update
		// as the tournaments play out but since i can go though the population array in O(n) it does
		// not matter much as far as run speed goes for an example as small as this
		
		for(int i = 0; i < nrOfTournaments; i++){
			individuals = getNextGeneration();
		}
		
		bestIndividualSoFar = getBestIndividual();
		
		/*
		 * Kör tournament det antal gånger som är angivet i variabeln nrOfTournaments
		 */
		return bestIndividualSoFar;
	}
	
	/**
	 * Gets the single best individual in the current population
	 * @return the best Individual as the class "Individual"
	 */
	private Individual getBestIndividual(){
		Individual theBestIndividual = individuals[0];
		
		for(int i = 0; i < individuals.length; i++){
			if(individuals[i].fitnessValue() >= theBestIndividual.fitnessValue()){
				theBestIndividual = individuals[i];
			}
		}
		
		return theBestIndividual;
	}
	
	
	/**
	 * TODO: make private, public only during testing, also maybe make it so mating is somewhat random
	 * @return a single new generation based on the old one, that is we run tournament,
	 * crossover and mutation on the old population and returns the result as a new generation
	 * here
	 */
	public Individual[] getNextGeneration(){
		// VIKTIGT: i här när man kör crossover så gör det åt båda håll, alltså
		// både crossover(individual[i], individual[i+1]) och 
		// crossover(individual[i+1], individual[i])
		
		Individual[] newGeneration = new Individual[individuals.length];
		Individual[] tournamentWinners = performTournament();
		
		int newGenerationIndex = 0;
		
		for(int i = 0; i < tournamentWinners.length; i++){
			newGeneration[newGenerationIndex++] = tournamentWinners[i];
		}
		
		for(int i = 0; i < tournamentWinners.length; i += 2){
			Individual[] offSpring = tournamentWinners[i].mateWith(tournamentWinners[i+1]);
			newGeneration[newGenerationIndex++] = offSpring[0];
			newGeneration[newGenerationIndex++] = offSpring[1];
		}
		
		
		
		// TODO: check here mutate population
		while(random.nextDouble() < mutationProb){
			// mutate
			System.out.println("mutating");
			int individualToMutateIndex = random.nextInt(newGeneration.length);
			mutate(newGeneration[individualToMutateIndex]);
		}
		
		
		
		return newGeneration;
		/*
		 * Ta dem individer som returneras från tournament funktionen som startindividerna för den nya
		 * populationen och kör sedan att alla dem ska "paras" med varandra och utöver detta kör
		 * mutation med en viss sannolikhet på den population som vi har för tillfället
		 */
		
	}
	
	
	
	/**
	 * Takes an individual of the population and mutates it and returns it, leaves the original
	 * individual intact
	 * @param individual the individual from the population that's gonna get mutated
	 * @return a mutated version of the individual that has been sent as an argument(assuming 
	 * that the mutation does not actually mutate into an identical individual)
	 */
	private void mutate(Individual individual){
		
		individual.mutateIndividual();
		
//		
//		boolean[][] ind = individual.getGeneticInformation(); // TODO: INTE BRA, utnyttjar
//		// Individuals underliggande struktur, så gör även crossover
//		
//		int mutateNr = random.nextInt(ind.length);
//		int mutateBit = random.nextInt(ind[mutateNr].length);
//		
//		ind[mutateNr][mutateBit] = !ind[mutateNr][mutateBit];
//		return new Individual(ind, individual.getOwnFitnessFunction());
	}
	
	/**
	 * TODO: make private again, only used for testing
	 * Executes the tournament and returns the resulting winners(will occasionally return a "loser" to
	 * mantain good genetic variation).
	 * @return the winners as an array
	 */
	public Individual[] performTournament(){
		Individual[] winners = new Individual[individuals.length / tournamentSize]; // TODO: tS
		
		int[] randInd = getRandomizedPopulation();
		
		int indIndex = 0;
		for(int winnerIndex = 0; winnerIndex < winners.length; winnerIndex++){
			if(indIndex >= individuals.length){
				System.out.println("The input have been incorrect as indIndex >= individual.length");
				System.exit(1);
			}
			Individual tempInd1 = individuals[randInd[indIndex++]];// individuals[indIndex++];
			Individual tempInd2 = individuals[randInd[indIndex++]];// individuals[indIndex++];
			
			if(random.nextDouble() <= likelyHoodOfWeakWinner){
				System.out.println("choosing weak winner");
				winners[winnerIndex] = (tempInd1.fitnessValue() < tempInd2.fitnessValue()) ?
						tempInd1: tempInd2;
			} else{
				winners[winnerIndex] = (tempInd1.fitnessValue() < tempInd2.fitnessValue()) ?
						tempInd2 : tempInd1;
			}
		}
		
		return winners;
		
		
		/*
		 * Skulle eventuellt kunna ha att med en väldigt liten sannolikhet kan en "svag"
		 * individ väljas här istället för en stark och därmed eventuellt kunna få större 
		 * genetisk variation
		 * 
		 * Hur viktigt är det att inte välja så att inte alla individer ligger brevid varandra?
		 * 
		 */
	}
	
	
	/**
	 * TODO: make private again after testing
	 * returns indexes to be used to access the current population to make sure that we retrieve
	 * the individuals non-deterministic
	 * @return randomized indexes to be used to get the Individuals
	 */
	public int[] getRandomizedPopulation(){
		int[] randomizedPop = new int[individuals.length];
		
		// instantiate the list in the order the individuals are in at the moment
		for(int i = 0; i < randomizedPop.length; i++){
			randomizedPop[i] = i;
		}
		
		
		// TODO: can make this more effecient which will really show if the population is big
		for(int i = 0; i < randomizedPop.length; i++){
			int temp = 0, changeIndex = random.nextInt(randomizedPop.length);
			temp = randomizedPop[i];
			randomizedPop[i] = randomizedPop[changeIndex];
			randomizedPop[changeIndex] = temp;
		}
		
		return randomizedPop;		
	}
}
