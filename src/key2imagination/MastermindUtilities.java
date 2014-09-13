/* 
 * Coded by key2imagination, for the evaluation of the 8th Light apprenticeship program.
 * 
 * MastermindUtilities gives {black, white} peg evaluation 
 * This function is shared for both gameHost and any solver because of the simple nature of its function
 * 	
 */

package key2imagination;

public class MastermindUtilities {
	
	private int countBlackPegs(int[] guessHolder, int[] userInputHolder) {
		int black = 0;
		for (int i = 0; i < 4; i++)
			if (guessHolder[i] == userInputHolder[i]) {
				guessHolder[i] = 0;
				userInputHolder[i] = 0;
				black++;
			}
		return black;
	}
	
	private int countWhitePegs(int[] guessHolder, int[] userInputHolder) {
		int white = 0;
		for (int i = 0; i < 4; i++)
			if (userInputHolder[i] != 0)
				for (int j = 0; j < 4; j++)
					if (guessHolder[j] == userInputHolder[i] && guessHolder[j]!=0) {
						guessHolder[j] = 0;
						userInputHolder[i] = 0;
						white++;
					}
		return white;
	}
	
	public int[] giveFeedback(int[] guess, int[] userInput){
		
		// returns a int[2] array for peg counts, {black, white}
		int[] guessHolder2 = new int[4];		// guessHolder used to calculate for a respHolderonse
		int[] userInputHolder = new int[4];		// help in determining the latest respHolderonse
		System.arraycopy(guess, 0, guessHolder2, 0, 4);
		System.arraycopy(userInput, 0, userInputHolder, 0, 4);
		
		return new int[] { countBlackPegs(guessHolder2, userInputHolder), countWhitePegs(guessHolder2, userInputHolder) };
	}
}
