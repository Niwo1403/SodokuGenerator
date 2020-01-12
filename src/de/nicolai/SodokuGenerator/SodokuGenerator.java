package de.nicolai.SodokuGenerator;

import java.util.ArrayList;
import java.util.function.Consumer;


/**
 * Used to create sodokus.
 * @author Nicolai
 *
 */
public class SodokuGenerator {


	// Variables

	/**
	 * All possible elements for the permutation for lines.
	 */
	private final String[] POSSIBLE_ELEMENTS = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
	/**
	 * Count of elements in POSSIBLE_ELEMENTS.
	 */
	private final int POSSIBLE_ELEMENTS_COUNT_FACTORIAL = 362_880; // 362_880 == 9!
	/**
	 * Max count of tries to find a new possible line in the sodoku.
	 */
	private final int MAX_TRYS = 5_000;
	/**
	 * Count of lines in the sodoku.
	 */
	private final int LINE_COUNT = 9;
	/**
	 * Saves the all possible solutions.
	 */
	private ArrayList<String> solutions = new ArrayList<String>();
	/**
	 * Saves all possible combinations of different lines.
	 */
	private String[][] lines = new String[POSSIBLE_ELEMENTS_COUNT_FACTORIAL][9];
	/**
	 * Keeps the position where to insert the next line in lines array.
	 */
	private int position = 0;
	/**
	 * Saves if the lines are already generated.
	 */
	private boolean linesGenerated = false;


	// Methods

	/**
	 * Generates a new sodoku.
	 */
	public void generateSodoku() {
		// Generate lines if they aren't now
		generateLines();
		// Generate sodokus
		Sodoku s = new Sodoku();

		// find solution
		while (!s.isComplete()) { // new try for each iteration
			s.reset();
			boolean reset = false;
			for (int i = 0; i < 9 && !reset; i++) { // try to find 9 solutions
				reset = true; // ends the last loop if no solution is found fast enough
				for (int j = 0; j < (i*i + 1) * MAX_TRYS; j++)
					if (s.addLine(lines[getRandomLineNumber()])) {
						// possible solution found
						reset = false; // block reset
						break;
					}
			}
		}


		// add sodoku to solutions
		solutions.add(s.getStringRepresentation(solutions.size() + 1));
	}

	/**
	 * Create a random index in range of the lines.
	 * @return the random index of the line
	 */
	private int getRandomLineNumber() {
		return (int) (Math.random() * POSSIBLE_ELEMENTS_COUNT_FACTORIAL);
	}

	/**
	 * Generates all possible lines.
	 */
	public void generateLines() {
		if (!linesGenerated) {
			permute(POSSIBLE_ELEMENTS, 0, LINE_COUNT - 1);
			linesGenerated = true;
		}
	}


	/**
	 * Adds permutations to lines array.
	 * @param elements strings to build permutation of
	 * @param l left point
	 * @param r right point
	 */
    private void permute(final String[] elements, final int l, final int r) {
        if (l == r) {
        	// add new line
            System.arraycopy(elements, 0, lines[position], 0, LINE_COUNT);
            position++;
        }else
            for (int i = l; i <= r; i++) { 
            	// create copy for recursion
            	String[] elementsCopy = new String[LINE_COUNT];
            	System.arraycopy(elements, 0, elementsCopy, 0, LINE_COUNT);
            	// switch elements
            	String tmp = elementsCopy[l];
            	elementsCopy[l] = elementsCopy[i];
            	elementsCopy[i] = tmp;
            	// recursive call
                permute(elementsCopy, l + 1, r);
            }
    }

	/**
	 * Prints the created sodoku to the passed method.
	 * @param outFile Consumer object, which gets the content.
	 */
	public void print(final Consumer<String> outFile) {
		// Redirect to ArrayList
		solutions.forEach(outFile);
	}

	// Overrides

	/**
	 * Return a string representation of the sodokus.
	 * @return the sodoku solutions joined to a string
	 */
	@Override
	public String toString() {
		return String.join("", solutions);
	}


	//Classes

	/**
	 * Builds a sodoku.
	 * @author Nicolai
	 */
	private class Sodoku {

		// Variables

		/**
		 * saves the added numbers.
		 */
		private String[][] content = new String[LINE_COUNT][LINE_COUNT];
		/**
		 * Count of the correct line.
		 */
		private int currentLine = 0;


		// Methods

		/**
		 * Tests if the current lines are a possible solution.
		 * @return true if all current rows could be a possible solution
		 */
		private boolean test() {
			// lines couldn't be wrong
			// Check rows
			for (int i = 0; i < LINE_COUNT; i++) {
				String[] existingElements = new String[LINE_COUNT];
				for (int j = 0; j <= currentLine; j++)
					if (include(existingElements, content[j][i]))
						return false;
					else
						existingElements[j] = content[j][i];
			}

			// Check squares
			for (int i = 0; i < currentLine; i += 3) // big lines
				for (int j = 0; j < LINE_COUNT; j += 3) { // big rows
					String[] existingElements = new String[LINE_COUNT];
					int index = 0;
					for (int k = i; k < i + 3; k++) // lines
						for (int l = j; l < j + 3; l++) // rows
							if (include(existingElements, content[k][l]))
								return false;
							else
								existingElements[index++] = content[k][l];

				}

			// nothing failed -> return true
			return true;
		}

		/**
		 * Checks if arr contains searchFor. Stops searching, if element is null.
		 * @param arr array to search in
		 * @param searchFor	string to search for
		 * @return true, if arr contains searchFor
		 */
		private boolean include(final String[] arr, final String searchFor) {
			for (String elm:arr)
				if (elm != null && elm.equals(searchFor))
					return true;
			return false;
		}

		/**
		 * Try to add a line.
		 * @param line the line to add to the sodoku.
		 * @return if the line could be added.
		 */
		public boolean addLine(final String[] line) {
			// add line to content
			if (isComplete()) // if sodoku is complete
				return false;
			System.arraycopy(line, 0, content[currentLine], 0, LINE_COUNT);

			// check if line could be added
			if (test()) {
				increaseLines();
				return true;
			} else
				return false;
		}

		/**
		 * Creates a text representation of the sodoku.
		 * @param number
		 * @return a sting representation of the sodoku, including the number in the headline
		 */
		public String getStringRepresentation(final int number) {
			if (isComplete())
				return "Sodoku number " + number + ":\n" + toString() + "\n";
			else
				return "";
		}

		/**
		 * Checks if the sodoku is complete.
		 * @return true if the sodoku got 9 correct lines
		 */
		public boolean isComplete() {
			return currentLine == LINE_COUNT;
		}

		/**
		 * Resets the sodoku, to an empty one. (like new initialized)
		 */
		public void reset() {
			setLine(0);
			content = new String[LINE_COUNT][LINE_COUNT];
		}


		// Overrides

		@Override
		public String toString() {
			String ret = "";
			for (int i = 0; i < LINE_COUNT; i++)
				ret += String.join(", ", content[i]) + "\n";
			return ret;
		}

		// Getter, Setter

		/**
		 * Set the currentLine.
		 * @param newCurrentLine new value of the CurrentLine;
		 */
		public void setLine(final int newCurrentLine) {
			if (newCurrentLine >= 0 && newCurrentLine <= LINE_COUNT) // LINE_COUNT lines means that the sodoku is complete
				currentLine = newCurrentLine;
		}

		/**
		 * Increases the currentLine by one.
		 */
		public void increaseLines() {
			setLine(currentLine + 1);
		}

	}

}
