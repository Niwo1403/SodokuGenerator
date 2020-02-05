package de.nicolai.sodoku_generator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Shows a frame, to get the settings for the solvable Sodoku.
 * @author Nicolai
 *
 */
public class StartFrame {
	/**
	 * Used to generate sodokus.
	 */
	private static final SodokuGenerator S_GENERATOR = new SodokuGenerator(true);
	/**
	 * Horizontal position (should be between 0-3).
	 */
	private static final double BUTTON_POS_REF = 2.5;
	/**
	 * The lettered ticks.
	 */
	private static final int MAJOR_TICKS = 25;
	/**
	 * The displayed ticks.
	 */
	private static final int MINOR_TICKS = 5;
	/**
	 * The height of a row in the JPanel.
	 */
	private static final int ROW_HEIGHT = 30;
	/**
	 * The width of a row in the JPanel.
	 */
	private static final int ROW_WIDTH = 400;
	/**
	 * The extra space surround a row in the JPanel.
	 */
	private static final int ROW_PADDING = 10;
	/**
	 * The JFrame to get the sodoku settings.
	 */
	private final JFrame settingsFrame;

	/**
	 * Initializes the object by creating the frame.
	 */
	public StartFrame() {
		settingsFrame = createStartFrame();
		showFrame();
	}

	/**
	 * Creates the JFrame containing the JPanel created by the createPanel method.
	 * @return the created JFrame
	 */
	private JFrame createStartFrame() {
		final int width = ROW_WIDTH + ROW_PADDING * 2 + SodokuFrame.ADD_WIDTH;
		final int height = ROW_HEIGHT * 4 + ROW_PADDING * 6 + SodokuFrame.ADD_HEIGHT;
		final JFrame frame = new JFrame("Sodoku Settings");
		frame.add(createPanel(width, height));
		frame.setResizable(false);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return frame;
	}

	/**
	 * Creates the JPanel containing the necessary buttons and input fields.
	 * @param width the width of the panel
	 * @param height the height of the panel
	 * @return the created JPanel
	 */
	private JPanel createPanel(final int width, final int height) {
		final JPanel panel = new JPanel(null);
		final JTextField info = new JTextField("Probability of a sodoku field to be empty:");
		final JSlider slider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 50);
		final JButton start = new JButton("Start");
		final int buttonWidth = ROW_WIDTH / 4;
		final int lineCount = 3;
		info.setBounds(ROW_PADDING, ROW_PADDING, ROW_WIDTH, ROW_HEIGHT);
		info.setEditable(false);
		info.setBorder(null);
		slider.setBounds(ROW_PADDING, ROW_PADDING * 2 + ROW_HEIGHT, ROW_WIDTH, ROW_HEIGHT * 2);
		slider.setMajorTickSpacing(MAJOR_TICKS);
		slider.setMinorTickSpacing(MINOR_TICKS);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		start.setBounds(ROW_PADDING + (int) (buttonWidth * BUTTON_POS_REF),
				ROW_PADDING + (ROW_PADDING + ROW_HEIGHT) * lineCount,
				buttonWidth, ROW_HEIGHT);
		start.addActionListener(e -> start((double) slider.getValue()));
		panel.setBounds(0, 0, width, height);
		panel.add(info);
		panel.add(slider);
		panel.add(start);
		return panel;
	}

	/**
	 * Shows the JFrame.
	 */
	public final void showFrame() {
		settingsFrame.setVisible(true);
	}

	/**
	 * Called to start to solve a sodoku.
	 * @param emptyProbability the probability of a sodoku field to be empty
	 */
	private void start(final double emptyProbability) {
		SodokuFrame.displaySodoku(S_GENERATOR.getSodoku(), emptyProbability).onExitBtnClick(this::showFrame);
		hideFrame();
	}

	/**
	 * Hides the JFrame.
	 */
	public final void hideFrame() {
		settingsFrame.setVisible(false);
	}
}
