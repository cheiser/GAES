import java.util.Random;

/**
 * Singleton
 * @author Mattis
 *
 */
public class Accessories {
	
	Random random;
	private static Accessories instance = null;
	
	private Accessories(){
		random = new Random();
	}
	
	public static Accessories getInstance(){
		if(instance == null) instance = new Accessories();
		return instance;
	}
	
	public Random getRandom(){
		return random;
	}
	
	

	public void printIndividual(Individual ind){
		boolean[] indNumbers = ind.getGeneticInformation()[0];
		for(int i = 0; i < indNumbers.length; i++){
			System.out.print("" + (indNumbers[i] == true ? "1" : "0"));
		}
	}
	
	
	public void printIndividualAsNumber(Individual ind){
		System.out.print(this.convertToNumber(ind.getGeneticInformation()[0]));
	}
	
	
	
	/**
	 * prints the given bit string
	 * @param bitString the bitstring we want printed as an boolean array
	 */
	public void printBitString(boolean[] bitString){
		for(int i = 0; i < bitString.length; i++){
			System.out.print("" + (bitString[i] == true ? "1" : "0"));
		}
	}
	
	/**
	 * WARNING: can overflow, may implement exception later
	 * @param bitStrang the bitString that should get converted to a decimal number
	 * @return the number that is the result of translating the "bit string" bitStrang as an long
	 */
	public static long convertToNumber(boolean[] bitStrang){
        int quantifier = 1;
        long nr = 0;
        for(int i = bitStrang.length-1; i >= 0; i--){
            nr += bitStrang[i]? quantifier : 0;
            quantifier *= 2;
        }
        return nr;
    }
	
	
	
	/**
	 * Converts the given number into an bitstring
	 * @param nr the number that we want to be converted
	 * @return an bitstring as an array of booleans that represents the number that we sent in but
	 * in an binary notation
	 */
	public boolean[] convertToBitString(int nr){
        boolean[] temp = new boolean[Integer.toBinaryString(nr).length()];
        String oneString = "1";
        String tempString = Integer.toBinaryString(nr);
        for(int i = 0; i < tempString.length(); i++){
            temp[i] = (tempString.codePointAt(i) == oneString.codePointAt(0))? true : false;
        }
        return temp;
    }
	
	
	/**
	 * Converts the given number into an bitstring with a given length, padds with 0's before the
	 * actual number begins, eg. convertToBitStringPadded(2, 4) ger 0010 istället för bara 10
	 * @param nr the number that we want to be converted
	 * @param length the desired length of the resulting bistring we want
	 * @return an bitstring as an array of booleans that represents the number that we sent in but
	 * in an binary notation and with the given length
	 */
	public boolean[] convertToBitStringPadded(int nr, int length){
        boolean[] temp = new boolean[length];
        boolean[] bitStringCompressed = convertToBitString(nr);
        String oneString = "1";
        int compIndex = 0;
        
        for(int i = 0; i < length - bitStringCompressed.length; i++){
            temp[i] = false;
        }
        
        for(int i = length - bitStringCompressed.length; i < temp.length; i++){
            temp[i] = bitStringCompressed[compIndex++];
        }
        return temp;
    }
	
	
	
}
