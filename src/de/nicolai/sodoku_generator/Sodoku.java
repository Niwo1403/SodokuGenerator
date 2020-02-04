package de.nicolai.sodoku_generator;

import java.util.Arrays;

/**
 * Used to create sodoku and check if it's valid.
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
	 * Creates a copy of the content and returns it.
	 * @return the copy of the content
	 */
	protected String[][] getContentCopy() {
		final String[][] contentCopy = new String[SodokuGenerator.LINE_COUNT][SodokuGenerator.LINE_LENGTH];
		for (int i = 0; i < SodokuGenerator.LINE_COUNT; i++)
			System.arraycopy(content[i], 0, contentCopy[i], 0, SodokuGenerator.LINE_LENGTH);
		return contentCopy;
	}

	/**
	 * Get the missing elements in a row.
	 * @param row to get the missing elements from
	 * @return the missing elements of the row
	 */
	public final String[] getMissingElementsOfRow(final int row) {
		if (isComplete())
			return new String[0];

		int mElementPos = 0;
		final String[] missingElements = new String[SodokuGenerator.LINE_LENGTH - currentLine];
		final String[] existingElements = getRow(row);
		for (int i = 0; i < SodokuGenerator.LINE_LENGTH; i++)
			if (!include(existingElements, SodokuGenerator.POSSIBLE_ELEMENTS[i]))
				missingElements[mElementPos++] = SodokuGenerator.POSSIBLE_ELEMENTS[i];
		return missingElements;
	}

	/**
	 * Gets a specific row of the sodoku.
	 * @param row row from the sodoku to get
	 * @return the elements in the passed row
	 */
	private String[] getRow(final int row) {
		String[] elements = new String[currentLine];
		for (int i = 0; i < currentLine; i++)
			elements[i] = content[i][row];
		return elements;
	}

	/**
	 * Tests if the current lines are a possible solution.
	 * @return true if all current rows could be a possible solution
	 */
	protected boolean test() {
		return
		// lines couldn't be wrong
		// Check rows
		testRows()
		// Check squares
		&& testSquares();
	}

	/**
	 * Checks if the squares are correct so far.
	 * @return true if all squares are correct so far
	 */
	private boolean testSquares() {
		final String[] existingElements = new String[SodokuGenerator.LINE_COUNT];
		for (int i = 0; i < currentLine; i += SodokuGenerator.SQUARE_STEPS) // big lines
			for (int j = 0; j < SodokuGenerator.LINE_LENGTH;
					j += SodokuGenerator.SQUARE_STEPS) { // big rows
				Arrays.fill(existingElements, null); // reset Array (contains wrong data)
				int index = 0;
				for (int k = i;	k < i + SodokuGenerator.LINE_COUNT
						/ SodokuGenerator.SQUARE_STEPS; k++) // lines
					for (int l = j; l < j +	SodokuGenerator.LINE_LENGTH
							/ SodokuGenerator.SQUARE_STEPS;	l++, index++) // rows
						if (!addElementIfNotIncluded(existingElements, content[k][l], index))
							return false;
			}
		return true;
	}

	/**
	 * Add element in existingElements at position index,
	 * if element isn't included in existingElements.
	 * @param existingElements array to search in
	 * @param element element to check
	 * @param index position to add element in existingElements, if it isn't included
	 * @return true, if element was added at position index
	 */
	private boolean addElementIfNotIncluded(final String[] existingElements,
			final String element, final int index) {
		if (include(existingElements, element))
			return false;
		existingElements[index] = element;
		return true;
	}

	/**
	 * Checks if the rows are correct so far.
	 * @return true if all rows are correct so far
	 */
	private boolean testRows() {
		final String[] existingElements = new String[currentLine + 1];
		for (int i = 0; i < SodokuGenerator.LINE_LENGTH; i++) {
			Arrays.fill(existingElements, null); // reset Array (contains wrong data from last iteration)
			for (int j = 0; j <= currentLine; j++)
				if (include(existingElements, content[j][i]))
					return false;
				else
					existingElements[j] = content[j][i];
		}
		return true;
	}

	/**
	 * Tests if all lines are correct;
	 * only used in subclass,
	 * since lines created by generating the sodoku couldn't be wrong.
	 * @return true, if all lines are correct
	 */
	protected boolean testLines() {
		final String[] existingElements = new String[SodokuGenerator.LINE_LENGTH];
		for (int i = 0; i < SodokuGenerator.LINE_COUNT; i++) {
			Arrays.fill(existingElements, null); // reset Array (contains wrong data from last iteration)
			for (int j = 0; j < SodokuGenerator.LINE_LENGTH; j++)
				if (include(existingElements, content[i][j]))
					return false;
				else
					existingElements[j] = content[i][j];
		}
		return true;
	}

	/**
	 * Checks if arr contains searchFor. Stops searching, if element is null.
	 * @param arr array to search in
	 * @param searchFor	string to search for
	 * @return true, if arr contains searchFor
	 */
	public static final boolean include(final String[] arr, final String searchFor) {
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
		}
		return false;
	}

	/**
	 * Creates a text representation of the sodoku.
	 * @param number number to include in headline for numbering
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
		final StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < SodokuGenerator.LINE_COUNT; i++) {
			sBuilder.append(String.join(", ", content[i]));
			sBuilder.append('\n');
		}
		return sBuilder.toString();
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
	 * Sets the content of the sodoku.
	 * @param newContent the new content for the sodoku
	 */
	protected void setContent(final String[]... newContent) {
		for (int i = 0; i < SodokuGenerator.LINE_COUNT; i++)
			System.arraycopy(newContent[i], 0, content[i], 0, SodokuGenerator.LINE_LENGTH);
	}

	/**
	 * Increases the currentLine by one.
	 */
	public void increaseLines() {
		setLine(currentLine + 1);
	}

}
