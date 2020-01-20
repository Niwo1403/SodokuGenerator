package de.nicolai.sodoku_generator;

import java.util.Arrays;

/**
 * @author Nicolai
 *
 */
@SuppressWarnings("PMD.AtLeastOneConstructor") // no Constructor needed
public class Sodoku {

	// Variables

	/**
	 * saves the added numbers.
	 */
	private String[][] content = new String[SodokuGenerator.LINE_COUNT][SodokuGenerator.LINE_LENGTH];
	/**
	 * Count of the correct line.
	 */
	private int currentLine;


	// Methods

	/**
	 * Tests if the current lines are a possible solution.
	 * @return true if all current rows could be a possible solution
	 */
	private boolean test() {
		// lines couldn't be wrong

		final String[] existingElements = new String[SodokuGenerator.LINE_COUNT];
		// Check rows
		for (int i = 0; i < SodokuGenerator.LINE_COUNT; i++) {
			Arrays.fill(existingElements, null); // reset Array (contains wrong data from last iteration)
			for (int j = 0; j <= currentLine; j++)
				if (include(existingElements, content[j][i]))
					return false;
				else
					existingElements[j] = content[j][i];
		}

		// Check squares
		for (int i = 0; i < currentLine; i += SodokuGenerator.SQUARE_STEPS) // big lines
			for (int j = 0; j < SodokuGenerator.LINE_LENGTH;
					j += SodokuGenerator.SQUARE_STEPS) { // big rows
				Arrays.fill(existingElements, null); // reset Array (contains wrong data)
				int index = 0;
				for (int k = i;	k < i + SodokuGenerator.LINE_COUNT
						/ SodokuGenerator.SQUARE_STEPS; k++) // lines
					for (int l = j; l < j +	SodokuGenerator.LINE_LENGTH
							/ SodokuGenerator.SQUARE_STEPS;	l++) // rows
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
		for (final String elm:arr)
			if (elm != null && elm.equals(searchFor))
				return true;
		return false;
	}

	/**
	 * Try to add a line.
	 * @param line the line to add to the sodoku.
	 * @return if the line could be added.
	 */
	public boolean addLine(final String... line) {
		if (line.length != SodokuGenerator.LINE_LENGTH) // check if the line length is correct
			return false;
		if (isComplete()) // if sodoku is complete
			return false;

		// add line to content
		System.arraycopy(line, 0, content[currentLine], 0, SodokuGenerator.LINE_COUNT);

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
	 * @return a sting representation of the sodoku,
	 * including the number in the headline
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
		return currentLine == SodokuGenerator.LINE_COUNT;
	}

	/**
	 * Resets the sodoku, to an empty one. (like new initialized)
	 */
	public void reset() {
		setLine(0);
		content = new String[SodokuGenerator.LINE_COUNT][SodokuGenerator.LINE_LENGTH];
	}


	// Overrides

	@Override
	public final String toString() {
		String ret = "";
		for (int i = 0; i < SodokuGenerator.LINE_COUNT; i++)
			ret += String.join(", ", content[i]) + "\n";
		return ret;
	}

	// Getter, Setter

	/**
	 * Set the currentLine.
	 * @param newCurrentLine new value of the CurrentLine;
	 */
	public void setLine(final int newCurrentLine) {
		if (newCurrentLine >= 0 && newCurrentLine <= SodokuGenerator.LINE_COUNT)
			// if LINE_COUNT equals newCurrentLine the sodoku is complete, so <= instead of <
			currentLine = newCurrentLine;
	}

	/**
	 * Increases the currentLine by one.
	 */
	public void increaseLines() {
		setLine(currentLine + 1);
	}

}
