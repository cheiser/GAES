import java.util.Random;


public class Main {

	public static void main(String[] args) {
		// TODO: populationsSize must with the current setup be a number which rest after division with
		// 2 be an even number
		Accessories a = Accessories.getInstance();
		
		GeneticAlgorithm ga = new GeneticAlgorithm(1000, 10, 2, 100, 5, new FitnessValueI() {
			
			
			
			@Override
			public double fitnessValue(boolean[][] geneticInformation) {
				long number = Accessories.convertToNumber(geneticInformation[0]);
				int returnValue = 0;
				
				if(number-2 > 7 && number+2 < 10) returnValue = 1;
				else returnValue = 0;
				
				return returnValue;
			}
			
			
//			@Override
//			public double fitnessValue(boolean[][] geneticInformation) {
//				long number = Main.convertToNumber(geneticInformation[0]);
//				return 1 - (Math.pow(number, 2) + (4 * number) + 2); // OVERFLOW!!!
//			}
			
//			@Override
//			public double fitnessValue(boolean[][] geneticInformation) {
//				return (double)Main.convertToNumber(geneticInformation[0]); // OVERFLOW!!!
//			}
		});
		
		Individual theBest = ga.findSolution();
		
		System.out.println("The best solution: ");
		a.printIndividual(theBest);
		System.out.println("\n as a number: " + a.convertToNumber(theBest.getGeneticInformation()[0]));
		
		boolean[] convNumber = {true, false, false, true};
		System.out.println("\n as a number: " + a.convertToNumber(convNumber));
		
		
		System.out.println("bitString: ");
		a.printBitString(a.convertToBitString(9));
		System.out.println();
		System.out.println(a.convertToNumber(a.convertToBitString(10)));
		
		
		
		FitnessValueI testFitnessF = new FitnessValueI() {
			
//			@Override
//			public double fitnessValue(boolean[][] geneticInformation) {
//				long giNr = Accessories.convertToNumber(geneticInformation[0]);
//				return 1 - Math.pow(giNr, 2) + (giNr * 4);
//			}
			
			
			@Override
			public double fitnessValue(boolean[][] geneticInformation) {
				long giNr = Accessories.convertToNumber(geneticInformation[0]);
				return 1 - Math.pow(giNr, 2) + (giNr * 4);
			}
			
			
		};
		
		System.out.print("padded: ");
		a.printBitString(a.convertToBitStringPadded(0, 4));
		
		Individual[] testPopulation = new Individual[8];
		
		for(int i = 0; i < testPopulation.length; i++){
			boolean[][] newIndInformation = new boolean[1][4];
			newIndInformation[0] = a.convertToBitStringPadded(i+1, 4);
			testPopulation[i] = new Individual(newIndInformation, testFitnessF);
		}
		
		GeneticAlgorithm testGA = new GeneticAlgorithm(testPopulation, 4, 2, 100);
		
		Individual[] winners = testGA.performTournament();
		System.out.println("\nthe individual winners are:");
		for(int i = 0; i < winners.length; i++){
			a.printIndividualAsNumber(winners[i]);
			System.out.println();
			
		}
		
		
		System.out.println("2 as binary");
		a.printBitString(a.convertToBitString(2));
		System.out.println();
		
		Individual[] nextGen = testGA.getNextGeneration();
		System.out.println("the next generation looks like:");
		for(int i = 0; i < nextGen.length; i++){
			a.printIndividualAsNumber(nextGen[i]);
			System.out.println();
		}
		
		System.out.println("the best solution TEST: ");
		a.printIndividual(testGA.findSolution());
		
		System.out.println("\nrandomized population: ");
		int[] randPop = testGA.getRandomizedPopulation();
		for(int i = 0; i < randPop.length; i++){
			System.out.print(randPop[i] + " ");
		}
		
		System.out.println("\nthe result of mating 8 and 7");
		Individual[] mateResult = testPopulation[7].mateWith(testPopulation[6]);
		a.printIndividual(mateResult[0]);
		System.out.println();
		a.printIndividual(mateResult[1]);
		
	}
	

}
