/**
 * Package for SodokuGenerator.
 * @author Nicolai
 * @version 1.0
 */
package de.nicolai.sodoku_generator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Start of program.
 * @author Nicolai
 *
 */
@SuppressWarnings("PMD.ShortClassName") // isn't a normal class
public final class Main {

	/**
	 * Used as help message, if -h is passed.
	 */
	private static final String HELP_INFORMATION = "Call like: \n"
				+ "SodokuCreator.jar [-o <OUTFILE>, -c COUNT]\n"
				+ "\t-h          - show this help\n"
				+ "\t-c COUNT    - count of sodokus to generate\n"
				+ "\t-o OUTFILE  - redirect output to OUTFILE";

	private Main() { }

	/**
	 * Processes the passed arguments, create the sodoku and print them.
	 * @param args arguments passed to the program
	 */
	@SuppressWarnings({"PMD.ModifiedCyclomaticComplexity", "PMD.NPathComplexity",
			"squid:S3776"}) // for main to check arguments
	public static void main(final String[] args) {
		boolean outParmExist = false;
		boolean helpParmExist = false;
		boolean countParamExist = false;
		String outFile = "";
		int count = 1;

		// get arguments
		for (final String arg:args)
			if ("-h".equals(arg))
				helpParmExist = true;
			else if ("-c".equals(arg))
				countParamExist = true;
			else if ("-o".equals(arg))
				outParmExist = true;
			else if (countParamExist) { // first argument after -c
				count = Integer.parseInt(arg);
				countParamExist = false;
			} else if (outParmExist && "".equals(outFile)) { // first argument after -o
				outFile = arg;
				outParmExist = false;
			}
		if (helpParmExist)
			println(HELP_INFORMATION);
		else if (!"".equals(outFile))
			println("Output directed to \"", outFile, "\"");


		// Create sodoku
		final SodokuGenerator sGenerator = new SodokuGenerator(true); // including initial call of generateLines

		if (count >= 2) { // create sodokus
			// generate needed count of sodokus
			for (int i = 0; i < count; i++)
				sGenerator.generateSodoku();

			// Write to outFile or Console, if outFile not passed.
			if ("".equals(outFile)) { // print to console
				sGenerator.print(Main::println);
			} else { // print to file
				try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outFile))) {
					sGenerator.print(arg -> {
						try {
							writer.write(arg);
						} catch (IOException e) {
							println("Error, couldn't write to file.");
						}
					});
				} catch (IOException e) {
					println("Error, file not found.");
				}
			}
		} else { // create one sodoku and solve it
			final double difficulty = 30.0;
			SodokuFrame.displaySodoku(sGenerator.getSodoku(), difficulty);
		}
	}

	/**
	 * Prints the passed arguments joined by "".
	 * @param out arguments to print
	 */
	@SuppressWarnings({"squid:S106", "PMD.SystemPrintln"})// program meant to print to console
	private static void println(final String... out) {
		System.out.println(String.join("", out));
	}
}
