package de.nicolai.sodoku_generator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * Displays a solvable sodoku and offers the possibility to solve it.
 * @author Nicolai
 *
 */
public class SodokuFrame {
	/**
	 * Size of a square in the sodoku.
	 */
	private static final int SQUARE_SIZE = 50;
	/**
	 * Added to frame width to display the frame better.
	 */
	private static final int ADD_WIDTH = 18;
	/**
	 * Added to frame height to display the frame better.
	 */
	private static final int ADD_HEIGHT = 40;
	/**
	 * Reference for the size of the free space surround the buttons.
	 */
	private static final int BIG_SQUARE_PAD = 5;
	/**
	 * Element count in a row / line of a big square.
	 */
	private static final int BIG_SQUARE_SIZE = 3;
	/**
	 * The width of a buttons.
	 */
	private static final int BUTTON_WIDTH = 150;
	/**
	 * The height of the buttons.
	 */
	private static final int BUTTON_HEIGHT = 30;
	/**
	 * The half distance between the buttons.
	 */
	private static final int BUTTON_PADDING = 20;
	/**
	 * The background color for the TextFields after completing the sodoku.
	 */
	private static final Color CORRECT_FIELD = new Color(200, 250, 200);
	/**
	 * Background color for the editable JTextFields of the sodoku.
	 */
	private static final Color EDITABLE_FIELD = Color.WHITE;
	/**
	 * Background color for the not editable JTextFields of the sodoku.
	 */
	private static final Color UNEDITABLE_FIELD = new Color(230, 230, 230);
	/**
	 * The frame, the sodoku is displayed in.
	 */
	private final JFrame frame;
	/**
	 * The fields of the sodoku.
	 */
	private final JTextField[][] fields = new JTextField[SodokuGenerator.LINE_COUNT][SodokuGenerator.LINE_LENGTH];
	/**
	 * The sodoku the frame is for.
	 */
	private final SolvableSodoku sodoku;
	/**
	 * Used to generate random numbers for random sodoku fields.
	 */
	private final Random rand = new Random();

	// Constructor

	/**
	 * Creates an object of SodokuFrame, by displaying it on a JFrame.
	 * @param initSodoku the sodoku to solve
	 */
	public SodokuFrame(final SolvableSodoku initSodoku) {
		sodoku = initSodoku;
		frame = createFrame();
	}

	// static methods

	/**
	 * Creates a SodokuFrame and displays the passed sodoku.
	 * @param sodoku the sodoku to display
	 * @param emptyProbability the probability of a sodoku field to be empty
	 * @return the created SodokuFrame
	 */
	public static SodokuFrame displaySodoku(final Sodoku sodoku, final double emptyProbability) {
		return new SodokuFrame(new SolvableSodoku(sodoku, emptyProbability));
	}

	// Methods

	/**
	 * Creates a JFrame, displaying the sodoku to solve.
	 * @return the created JFrame
	 */
	private JFrame createFrame() {
		final JFrame newFrame = new JFrame("Sodoku");
		final int neededSpace = SQUARE_SIZE + BIG_SQUARE_PAD / BIG_SQUARE_SIZE;
		final int sodokuWidth = SodokuGenerator.LINE_COUNT * neededSpace;
		final int sodokuHeight = SodokuGenerator.LINE_LENGTH * neededSpace;
		final int tmpHeight = getGreater(sodokuHeight, (BUTTON_HEIGHT + BUTTON_PADDING) * 3);
		final int finalWidth = sodokuWidth + ADD_WIDTH + BUTTON_WIDTH + BUTTON_PADDING * 2;
		final int finalHeight = tmpHeight + ADD_HEIGHT;
		final int buttonX = sodokuWidth + BUTTON_PADDING;
		final int buttonY = (tmpHeight - BUTTON_HEIGHT) / 2;

		newFrame.add(createPanel(finalWidth, finalHeight, buttonX, buttonY));
		newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newFrame.setSize(finalWidth, finalHeight);
		newFrame.setResizable(false);
		newFrame.setVisible(true);
		return newFrame;
	}

	/**
	 * Creates the JPanel including the sodoku and the three buttons.
	 * @param finalWidth the width of the JPanel
	 * @param finalHeight the height of the JPanel
	 * @param buttonX the x position of the button in the middle
	 * @param buttonY the y position of the button in the middle
	 * @return the created JPanel
	 */
	private JPanel createPanel(final int finalWidth, final int finalHeight, final int buttonX, final int buttonY) {
		final JPanel panel = new JPanel(null);
		final JButton showField = new JButton("Show Field");
		final JButton showSolution = new JButton("Show Solution");
		final JButton exitSodoku = new JButton("Exit");
		final int buttonDistance = BUTTON_HEIGHT + BUTTON_PADDING;
		showField.setBounds(buttonX, buttonY - buttonDistance, BUTTON_WIDTH, BUTTON_HEIGHT);
		showSolution.setBounds(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
		exitSodoku.setBounds(buttonX, buttonY + buttonDistance, BUTTON_WIDTH, BUTTON_HEIGHT);
		showField.addActionListener(e -> help());
		showSolution.addActionListener(e -> solve());
		exitSodoku.addActionListener(e -> frame.dispose());
		initJTextFields();
		mapFields(fields);
		panel.setBounds(0, 0, finalWidth, finalHeight);
		for (final JTextField[] fieldLines:fields)
			for (final JTextField field:fieldLines)
				panel.add(field);
		panel.add(showField);
		panel.add(showSolution);
		panel.add(exitSodoku);
		return panel;
	}

	/**
	 * Returns the greater number.
	 * @param firstNumber the first number
	 * @param secondNumber the second number
	 * @return the greater number
	 */
	private int getGreater(final int firstNumber, final int secondNumber) {
		if (firstNumber >= secondNumber)
			return firstNumber;
		else
			return secondNumber;
	}

	/**
	 * Initializes the passed JTextField array and sets their bounds.
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops") // method exists to initialize the JTextFields
	private void initJTextFields() {
		for (int i = 0; i < fields.length; i++)
			for (int j = 0; j < fields[i].length; j++) {
				fields[i][j] = new JTextField("");
				fields[i][j].setBounds(SQUARE_SIZE * j + getPadding(j), SQUARE_SIZE * i + getPadding(i),
						SQUARE_SIZE, SQUARE_SIZE);
				fields[i][j].setHorizontalAlignment(SwingConstants.CENTER);
			}
	}

	/**
	 * Returns the required padding for the specified elementCount.
	 * @param elementCount the count of the elements before the required padding
	 * @return the required padding
	 */
	private int getPadding(final int elementCount) {
		return (elementCount / BIG_SQUARE_SIZE) * BIG_SQUARE_PAD;
	}

	/**
	 * Maps the JTextFields to the sodoku fields.
	 * @param textFields the text fields
	 * @return true if map were successful
	 */
	private boolean mapFields(final JTextField[]... textFields) {
		try {
			for (int i = 0; i < textFields.length; i++)
				for (int j = 0; j < textFields[i].length; j++) {
					if (sodoku.getValue(i, j) == null) {
						textFields[i][j].setEditable(true);
						textFields[i][j].setBackground(EDITABLE_FIELD);
						textFields[i][j].addKeyListener(getKeyListener(textFields[i][j], i, j));
					} else {
						textFields[i][j].setEditable(false);
						textFields[i][j].setText(sodoku.getValue(i, j));
						textFields[i][j].setBackground(UNEDITABLE_FIELD);
					}
				}
		} catch (IndexOutOfBoundsException ex) {
			return false;
		}
		return true;
	}

	/**
	 * Creates a KeyListener for a TextField and the matching sodoku Field.
	 * @param tField the TextField the KeyListener is for
	 * @param line the line of the field
	 * @param row the row of the field
	 * @return the created KeyListener
	 */
	private KeyListener getKeyListener(final JTextField tField, final int line, final int row) {
		return new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent event) {
				final char inp = event.getKeyChar();
				tField.setText(""); // char will be added after this method
				if (inp >= '0' && inp <= '9') {
					sodoku.getField(line, row).trySetValue(Character.toString(inp));
					if (sodoku.isSolved()) {
						sodokuSolved();
						tField.setText(Character.toString(inp));
						tField.setFocusable(false);
					}
				}
			}
		};
	}

	/**
	 * Should be called, if the sodoku were solved.
	 */
	private void sodokuSolved() {
		for (final JTextField[] fieldRows:fields)
			for (final JTextField field:fieldRows)
				markAsSolved(field);
	}

	/**
	 * Disables the field and set the background to light green.
	 * @param field the field of the sodoku to edit
	 */
	private void markAsSolved(final JTextField field) {
		field.setEditable(false);
		field.setBackground(CORRECT_FIELD);
	}

	/**
	 * Set the correct values in the TextFields.
	 */
	private void solve() {
		for (int i = 0; i < fields.length; i++)
			for (int j = 0; j < fields[i].length; j++)
				fields[i][j].setText(sodoku.getSolution(i, j));
		sodokuSolved();
	}

	/**
	 * Reveals a field.
	 */
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops") // to save two elements in ArrayList instead of one
	private void help() {
		final ArrayList<int[]> insertedFields = new ArrayList<>();
		final ArrayList<int[]> emptyFields = new ArrayList<>();
		for (int i = 0; i < fields.length; i++)
			for (int j = 0; j < fields[i].length; j++)
				if (fields[i][j].isEditable()) {
					if (fields[i][j].getText().equals(""))
						emptyFields.add(new int[] {i, j});
					else
						insertedFields.add(new int[] {i, j});
				}
		if (insertedFields.isEmpty() && emptyFields.isEmpty())
			solve(); // no open fields left
		else if (emptyFields.isEmpty())
			solveField(insertedFields.get(rand.nextInt(insertedFields.size())));
		else
			solveField(emptyFields.get(rand.nextInt(emptyFields.size())));
	}

	/**
	 * Solve the field at the passed position.
	 * @param positions an array, containing the x and y coordinate.
	 */
	private void solveField(final int... positions) {
		final JTextField field =  fields[positions[0]][positions[1]];
		final String newValue = sodoku.getSolution(positions[0], positions[1]);
		field.setText(newValue);
		sodoku.trySetValue(positions[0], positions[1], newValue);
		markAsSolved(field);
		if (sodoku.isSolved())
			sodokuSolved();
	}
}
