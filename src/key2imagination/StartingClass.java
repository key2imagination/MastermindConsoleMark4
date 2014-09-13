/* 
 * Coded by key2imagination, for the evaluation of the 8th Light apprenticeship program.
 * 
 * MARK 3 Mastermind Console
 * 
 * Update of Mark 3: singled out various functions to form MastermindUI and MastermindUtilities 
 * Update of Mark 2: removed 'history' and 'feedback' LinkedList because they seem unnecessary
 * 
 * This console-based game is designed specifically to allow computer player to solve human player's Mastermind game code.
 * The limitation against a human player's involvement is intentional; as it saves the trouble to check for unintended inputs.  
 * 	
 */

package key2imagination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StartingClass {

	/*  
	 * 	StaringClass keeps track of the game progress.
	 *  'userInput' holds human user's riddle, enables graphics and helps in assessing validity of guesses
	 *  'guessHolder' holds the latest respHolderonse, serves a logistic purpose for 
	 *  'resp' holds the latest feedback
	 *	'state' records the stage of the game, shapes the game's flow.
	 */
	
	private static MastermindUI ui;
	private static MastermindUtilities util;
	private static WikiSolver WikiKnuth;
	
	private static int typesOfPegs = 6;
	private static int totalPositions = 4;
	private static int numberOfTries = 10;
	private static final int TYPES_OF_RESPONES = 2;
	
	private int[] userInput = new int[totalPositions];
	private int[] guessHolder = new int[totalPositions];
	private int[] resp = new int[TYPES_OF_RESPONES];
	
	public enum GameState {
		going, end
	}
	private static GameState state = GameState.going;
	

	public static void main(String[] args) {

		StartingClass sc = new StartingClass();
		
		StartingClass.ui.printWelcomeMessages();
		while(state.equals(GameState.going)) 
			sc.gameHost(WikiKnuth);
	}
	
	public StartingClass(){
		ui = new MastermindUI(totalPositions, typesOfPegs, numberOfTries);
		util = new MastermindUtilities();
		WikiKnuth = new WikiSolver(typesOfPegs, totalPositions);
	}
	
	// ************************ Game functions *************************** 

	private void gameHost(Solver sol) {
		/*
		 * The cycle:
		 * 0. game host involves a player (Solver) to function (part of the argument)
		 * 1. ask user to selected his code
		 * 2. ask solver for guesses, give evaluations to guesses and change game state
		 * 3. if the solver run out of guesses or cracked the code, end the game
		 * 4. repeat step 2
		 */

		int[] respHolder = new int[] {-1, -1};			// constantly holds the latest respHolderonse
		
		// 1. get user's input to make the game code
		String[] tempStringHolder = getUserInput("userinput").split("");
		for (int i = 0; i < totalPositions; i++ ) userInput[i] = Integer.valueOf(tempStringHolder[i]);
		
		ui.printStartInterface(userInput);
		
		// 4. the main check loop
		for (int k = 1; k <= numberOfTries; k++) {
			guessHolder = new int[totalPositions];

			// 2. call for guesses
			callForGuesses(sol, respHolder);
			recordLatestResponse(respHolder);
			ui.printTheLatestGuess(k, guessHolder, respHolder); // update the console for output

			// 3. check if the game terminates; respHolder[0] == 4 means cracked
			if (k == numberOfTries && respHolder[0] != 4) ui.printAlgorithmDefeatMessage();
			else if (respHolder[0] == 4) {
				ui.printAlgorithmWonMessage();
				break;
			}

		} // main check loop end
		
		checkUserExitIntention();
	} // function end

	private void callForGuesses(Solver sol, int[] respHolder) {				
		System.arraycopy(sol.Solve(respHolder), 0, guessHolder, 0, totalPositions);
	}
	
	private void resetGame(){
		// This function resets key game components thus allow the game to run again
		userInput = new int[] {0,0,0,0};
		WikiKnuth = new WikiSolver(typesOfPegs, totalPositions);
	} 

	private void checkUserExitIntention(){
		
		String line = null;
		
		 // this function asks if the user wants to keep playing.
		line = getUserInput("exit");
		
		// print an empty line for aesthetic purpose
		ui.printEmptyLine();
		
		// check if the user wants to quit, then either reset the game for another round or quit
		state = exitOrResetGame(line);

	} // function end	
	
	private GameState exitOrResetGame(String yOrN) {
		
		// 'line' should only be "y" or "n"
		if (yOrN.toLowerCase().equals("y")) {
			resetGame();
			return GameState.going;
		} else {
			if (!yOrN.toLowerCase().equals("n")) ui.printMisuseFunctionExitOrResetGame();
			ui.printExitMessage();
			return GameState.end;
		} 
	}
	
	private String getUserInput(String type){
		
		/*
		 * this functions takes user input to form the riddle code.
		 * the bulk of the code is just check if the user enters something undesirable.
		 * I could make it read user inputs that comes in like "red, potato,Green and purple,"
		 * but only if you request that I do.
		 */
		
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		String[] line = null;
		String tempStringHolder = "";
		
		try {
			
			if (type.equals("userinput")) {
				ui.printAskFor4DigitCode();
				line = bufferRead.readLine().split("");				// this line store split each input character into a separate string 
				
				/* 
				 * this do-while makes sure that each character is numeric
				 * if an input character is not an integer, Integer.valueOf(String) throws an NumberFormatException
				 * if the value of an integer is out of range, we throw an NumberFormatException
				 * when a NumberFormatException is thrown, we will run the do-while loop again to get new user input
				 */
				do {
					try {
						// line == null only when we throw a NumberFormatException
						if (line == null) line = bufferRead.readLine().split("");
						if (line.length != totalPositions) throw(new NumberFormatException());
						
						for (int i = 0; i < totalPositions; i++) 
							if (Integer.valueOf((line[i])) > typesOfPegs || Integer.valueOf((line[i])) < 1) throw(new NumberFormatException());
					} catch(NumberFormatException e) {
						ui.printAskFor4DigitCodeAgain();
						line = null; 	// allows the do-while loop to go again, because we didn't get the right input
					}
				} while (line == null);
				for (int i = 0; i < line.length; i++) tempStringHolder += line[i];
				
				return tempStringHolder;
				
			} else if (type.equals("exit")){
				
				// this do-while ensures we return an 'y' or 'n'
				do {
					try {
						ui.printPlayAgainPrompt();
						line = bufferRead.readLine().split("");
					} catch (IOException e) {
						e.printStackTrace();
					}
				} while (line == null || line.length != 1 || (!line[0].toLowerCase().equals("y") && (!line[0].toLowerCase().equals("n"))));
				tempStringHolder = line[0];
				return tempStringHolder;
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("Error in calling getUserInput function!");
		return new String("error");
	} // function end
	
	private void recordLatestResponse(int[] respHolder) {
		// record evaluation to both 'feedback', and save evaluation to 'respHolder' for next Solver run
		resp = util.giveFeedback(guessHolder, userInput);
		System.arraycopy(resp, 0, respHolder, 0, TYPES_OF_RESPONES);
	}
}
