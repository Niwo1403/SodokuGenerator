package de.nicolai.sodoku_generator;

import java.util.Random;

/**
 * A sodoku offering the possibility to solve it.
 * @author Nicolai
 *
 */
public class SolvableSodoku extends Sodoku {
	/**
	 * Used to get random doubles to get probabilities.
	 */
	private static final Random RAND = new Random();
	/**
	 * Multiplied to a random double, to get percentage.
	 */
	private static final double PROCENT_RANGE = 100.0;
	/**
	 * Contains a solution for the sodoku.
	 */
	private final String[][] solvedSodoku;
	/**
	 * Contains the current guessed and set values.
	 */
	private final SodokuField[][] openSodoku
			= new SodokuField[SodokuGenerator.LINE_COUNT][SodokuGenerator.LINE_LENGTH];

	// Constructor

	/**
	 * Creates a solvable sodoku from an existing solved one.
	 * @param sodoku a solved sodoku,
	 * which content is used to create this solvable sodoku
	 * @param emptyProbability probability of a field to be empty
	 */
	public SolvableSodoku(final Sodoku sodoku, final double emptyProbability) {
		super();
		solvedSodoku = sodoku.getContentCopy();
		createOpenSodoku(emptyProbability);
	}

	// Methods

	/**
	 * Removes the passed amount of elements out of the sodoku, if possible.
	 * @param emptyProbability probability of a field to be empty
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops") // method exists to initialize these objects
	private void createOpenSodoku(final double emptyProbability) {
		for (int i = 0; i < SodokuGenerator.LINE_COUNT; i++)
			for (int j = 0; j < SodokuGenerator.LINE_LENGTH; j++)
				if (emptyProbability < RAND.nextDouble() * PROCENT_RANGE)
					openSodoku[i][j] = new SodokuField(solvedSodoku[i][j]);
				else
					openSodoku[i][j] = new SodokuField(null);
	}

	/**
	 * Tests if the sodoku is complete and correct.
	 * @return true, if the sodoku is correct
	 */
	public boolean isSolved() {
		final String[][] contentBuilder = new String[SodokuGenerator.LINE_COUNT][SodokuGenerator.LINE_LENGTH];
		for (int i = 0; i < SodokuGenerator.LINE_COUNT; i++)
			for (int j = 0; j < SodokuGenerator.LINE_LENGTH; j++)
				if (openSodoku[i][j].getValue() == null)
					return false; // not all fields filled
				else
					contentBuilder[i][j] = openSodoku[i][j].getValue();
		setContent(contentBuilder);
		return testLines() && test();
	}

	/**
	 * Returns the value of the field in the passed line and row.
	 * @param line the line of the field
	 * @param row the row of the field
	 * @return the value of the specified field
	 */
	public String getValue(final int line, final int row) {
		return openSodoku[line][row].getValue();
	}

	/**
	 * Returns the correct value of the field in the passed line and row.
	 * @param line the line of the field
	 * @param row the row of the field
	 * @return the value of the specified field
	 */
	public String getSolution(final int line, final int row) {
		return solvedSodoku[line][row];
	}

	/**
	 * Sets the value for the field in the passed line and row.
	 * @param line the line of the field
	 * @param row the row of the field
	 * @param newValue the new value of the field
	 * @return true, if the value were changed
	 */
	public boolean trySetValue(final int line, final int row, final String newValue) {
		return openSodoku[line][row].trySetValue(newValue);
	}

	/**
	 * Get the sodoku field at the passed position.
	 * @param line the line of the field
	 * @param row the row of the field
	 * @return the sodoku field
	 */
	public SodokuField getField(final int line, final int row) {
		return openSodoku[line][row];
	}

	// inner classes

	/**
	 * A single number in the sodoku.
	 * @author Nicolai
	 *
	 */
	public static class SodokuField {
		/**
		 * True, if the field could be changed.
		 */
		private final boolean changeable;
		/**
		 * The value of the field.
		 */
		private String value;

		// Constructors

		/**
		 * Creates a field in the sodoku with the passed value.
		 * @param fValue the value of the field, 0 if not set and changeable
		 */
		public SodokuField(final String fValue) {
			changeable = fValue == null;
			value = fValue;
		}

		// Getter / Setter

		/**
		 * Returns the value of the field.
		 * @return the value of the field
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Sets the new value.
		 * @param newValue the new value
		 * @return true, if the value could be changed
		 */
		public boolean trySetValue(final String newValue) {
			if (changeable)
				value = newValue;
			return changeable;
		}
	}
}
