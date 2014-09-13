/* 
 * Coded by key2imagination, for the evaluation of the 8th Light apprenticeship program.
 * 
 * MastermindUI prints all communications with its users. 
 * Function names shadow their responsibilities.
 * 	
 */

package key2imagination;

public class MastermindUI {
	
	private int positions, variations, tries;
	
	public MastermindUI(int pos, int vars, int t){
		positions = pos;
		variations = vars;
		tries = t;
	}
	
	public void printPlayAgainPrompt(){
		System.out.print("Play again? Y/N ");
	}
	
	public void printWelcomeMessages() {
		System.out.println("Welcome to Mastermind!");
		System.out.println("In this game you will give a "+positions+"-digit code for the algorithm to crack.");
		System.out.println("Each digit could be 1 ~ "+variations+", and the algorithm is allowed "+tries+" tries.");
		System.out.println();
	}
	
	public void printExitMessage() {
		System.out.println("Have a nice day!");
	}
	
	public void printAskFor4DigitCode() {
		System.out.print("Please input a "+positions+"-digit code: ");
	}
	
	public void printAskFor4DigitCodeAgain() {
		System.out.println("Please enter a "+positions+"-digit code consists of "+positions+" integers, each range from 1 to "+variations+".");
		System.out.print("Please try again: ");
	}
	
	public void printStartInterface(int[] userInput) {
		System.out.println("Game starts!");
		System.out.print("   ");
		for (int i = 0; i < positions; i++) System.out.print("   "+userInput[i]);
		System.out.println();
		System.out.print("---");
		for (int i = 0; i < positions; i++) System.out.print("----");
		System.out.print("-------------------");
		System.out.println();
		System.out.print("    ");
		for (int i = 1; i <= positions; i++) System.out.print(" ("+i+")");
		System.out.print(" | Black | White");
		System.out.println();
	}
	
	public void printAlgorithmDefeatMessage() {
		System.out.print("---");
		for (int i = 0; i < positions; i++) System.out.print("----");
		System.out.print("-------------------");
		System.out.println();
		System.out.println("The algorithm was unable to crack your code!");
	}
	
	public void printTheLatestGuess(int k, int[] guessHolder, int[] respHolder) {
		if (k<10) System.out.print(k+".    ");
		else System.out.print(k+".   ");
		for (int i = 0; i < positions; i++) System.out.print(guessHolder[i]+"   ");
		System.out.print("   "+respHolder[0]+"       "+respHolder[1]);
		System.out.println();
	}
	
	public void printAlgorithmWonMessage() {
		System.out.print("---");
		for (int i = 0; i < positions; i++) System.out.print("----");
		System.out.print("-------------------");
		System.out.println();
		System.out.println("The algorithm cracked your code!");
	}

	public void printEmptyLine() {
		// add another line; aesthetic purpose
		System.out.println();
	}

	public void printMisuseFunctionExitOrResetGame() {
		System.out.println("Misuse GameState \'exitOrResetGame(String)\'");
	}
	
}
