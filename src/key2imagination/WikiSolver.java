/*
 * Coded by key2imagination, for the evaluation of the 8th Light apprenticeship program.
 * 
 * This solver adopts the following algorithm excerpted from Wikipedia.org, 
 * According to the page, the original formulation could be attributed to Donald Knuth.
 * 
 * The excerpt of the algorithm is as follows:
 * 
 * Five-guess algorithm 
 * 
 * In 1977, Donald Knuth demonstrated that the codebreaker can solve the pattern in five moves or fewer, 
 * using an algorithm that progressively reduced the number of possible patterns. 
 * The algorithm works as follows:
 * 1. Create the set S of 1296 possible codes, 1111,1112,.., 6666.
 * 2. Start with initial guess 1122 (Knuth gives examples showing that some other first guesses such as 1123, 
 *     1234 do not win in five tries on every code).
 * 3. Play the guess to get a response of colored and white pegs. 
 * 4. If the response is four colored pegs the game is won, the algorithm terminates. 
 * 5. Otherwise, remove from S any code that would not give the same response if it (the guess) were the code.
 * 6. Apply minimax technique to find a next guess as follows: For each possible guess, that is, 
 *     any unused code of the 1296 not just those in S, 
 *     calculate how many possibilities in S would be eliminated for each possible colored/white peg score. 
 *     The score of a guess is the minimum number of possibilities it might eliminate from S. 
 *     From the set of guesses with the maximum score select one as the next guess, 
 *     choosing a member of S whenever possible. 
 *     (Knuth follows the convention of choosing the guess with the least numeric value, e.g. 2345 is lower than 3456. 
 *     Knuth also gives an example showing that in some cases no member of S will be among the highest scoring 
 *     guesses and thus the guess cannot win on the next turn yet will be necessary to assure a win in five.) 
 * 7. Repeat from step 3.
 * 
 */


package key2imagination;

import java.util.Arrays;
import java.util.LinkedList;

public class WikiSolver implements Solver {
	
	/*
	 * s records all the possible guesses of the game; reset only through resetSolver();
	 * - each s[i] holds 6 integers, they stands for: 
	 * 		- s[i][0]: if this guess has been ruled out, 0 as ruled out, 1 as still being considered
	 * 		- s[i][1]: the score of this guess, used in step 6 of the algorithm
	 * 		- s[i][2] ~ s[i][5]: the 4 integers each represent a color, range from 1 ~ 6
	 * responsePool records all possible peg situation; reset only through resetSolver();
	 * - each responsePool[i] holds 2 integers, they stands for:
	 * 		- responsePool[i][0]: number of black pegs
	 * 		- responsePool[i][1]: number of white pegs
	 * - The pool counts 14 entries 
	 * - The black-white convention follows from the game.
	 */
	
	private int[][] allPossibleGuessses;
	private LinkedList<int[]> responsePool = new LinkedList<int[]>();
	private int[] latestGuess;
	private MastermindUtilities solverUtil;
	
	private int totalPositions;
	private int typesOfPegs;
	private int countOfPossibilities = 1;
	private int countOfResponses = 0;
	
	private static final int GUESS_START_POSITION = 2;
	private static final int ELIMINATED_GUESS = 0;
	private static final int UNELIMINATED_GUESS = 1;
	private static final int INITIAL_SCORE = 0;

	public WikiSolver(int pegs, int positions) {
		// the constructor generate all elements required for 'allPossibleGuesses' and 'responsePool'
		typesOfPegs = pegs;
		totalPositions = positions;
		for (int i = 1; i <= positions; i++) countOfPossibilities *= pegs; // because there is no way to do powers..
		for (int i = 1; i <= positions; i++) countOfResponses += i;
		
		initiateAllPossibleGuesses();
		initiateResponsePool(totalPositions);
		latestGuess = new int[] {1,1,2,2};
		
		solverUtil = new MastermindUtilities();
	}

	// **************************** Solver common functions ****************************
	
	public int[] Solve(int[] resp) {
		int[] tempGuess = new int[] { 1, 1, 2, 2};
		int maxScoreLoc = 0;
		
		// this calls for initial guess
		if (Arrays.equals(resp, new int[] {-1,-1})) return tempGuess;

		// remove combo in S that does not give the same pegs as do the last guess
		removeUnrelatedCombos(latestGuess, resp);

		// remove the guess with maximum score, copy its content to latestGuess
		maxScoreLoc = getGuessWithMaxScore(tempGuess);
		removeGuess(maxScoreLoc);
		System.arraycopy(allPossibleGuessses[maxScoreLoc], 2, latestGuess, 0, 4);
		return latestGuess;
	}

	
	// ****************** algorithm specific utility functions ***************************

	private void initiateAllPossibleGuesses(){

		allPossibleGuessses = new int[countOfPossibilities][totalPositions + 2];  // first two are signifier-s
		
		// prepare 'allPossibleGuesses' for filling
		for (int i = 0; i < countOfPossibilities; i++) {
			allPossibleGuessses[i][0] = UNELIMINATED_GUESS;
			allPossibleGuessses[i][1] = INITIAL_SCORE;
			for (int j = 2; j < totalPositions + 2; j++) allPossibleGuessses[i][j] = 1;
		};

		// actually filling the array
		for (int i = 1; i < countOfPossibilities; i++){
			System.arraycopy(allPossibleGuessses[i-1], 0, allPossibleGuessses[i], 0, totalPositions+2);
			for (int j = totalPositions + 1; j >= 2; j--)
				if (allPossibleGuessses[i][j] + 1 <= typesOfPegs) {
					allPossibleGuessses[i][j]++;
					break;
				} else allPossibleGuessses[i][j] = 1;
		}
	}
	
	private void initiateResponsePool(int positions) {
		// set up responsePool; (0,0) ~ (4,0), 
		for (int i = 0; i <= positions; i++)
			for (int j = 0; j <= i; j++)
				responsePool.add( new int[] { j, i - j });
	}
	
	public void removeGuess(int index) {
		allPossibleGuessses[index][0] = ELIMINATED_GUESS;
	}
	
	private void removeUnrelatedCombos(int[] guess, int[] resp) {
		
		// removes combos from S that does not give the same feedback as would the real answer
		int[] tempSCode = new int[totalPositions];
		for (int i = 0; i < countOfPossibilities; i++){
			System.arraycopy(allPossibleGuessses[i], GUESS_START_POSITION, tempSCode, 0, totalPositions);
			if ( ! Arrays.equals(solverUtil.giveFeedback(guess, tempSCode), resp)) 
				removeGuess(i);
		}
	}

	private int CountRemovables(int[] guess, int[] resp) {
		
		// counts how many guesses could be removed from S using 'guess' and 'resp'
		int count = 0;
		int[] comboAtSi = new int[totalPositions];
		for (int i = 0; i < countOfPossibilities; i++)
			if (allPossibleGuessses[i][0] == UNELIMINATED_GUESS) {
				System.arraycopy(allPossibleGuessses[i], GUESS_START_POSITION, comboAtSi, 0, totalPositions);
				if (!Arrays.equals(solverUtil.giveFeedback(guess, comboAtSi), resp))
					count++;
			}
		return count;
	}

	private int getGuessWithMaxScore(int[] tempGuessHolder) {
		// Calculate the max scores for all removed or remaining combos in S
		int maxScore = 0, maxScoreLoc = 0;
		for (int i = 0; i < countOfPossibilities; i++) 
			if (allPossibleGuessses[i][0] == UNELIMINATED_GUESS){
				System.arraycopy(allPossibleGuessses[i], GUESS_START_POSITION, tempGuessHolder, 0, totalPositions);
				
				// we always want the initial score to be the benchmark, thus always use new score
				int score = CountRemovables(tempGuessHolder, responsePool.get(0)); 
				
				// we use cases 1 ~ 13 only, excludes (0,0) and (4,0) case: they don't help
				for (int j = 1; j < countOfResponses; j++)									
					if (CountRemovables(tempGuessHolder, responsePool.get(j))>0)
					score = Math.min(score, CountRemovables(tempGuessHolder, responsePool.get(j)));
				allPossibleGuessses[i][1] = score;
				if (maxScore < score) {
					maxScore = score;
					maxScoreLoc = i;
				}
			}
		return maxScoreLoc;
	}
}
