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
	public static void main(final String[] args) {
		boolean outParmExist = false;
		boolean helpParmExist = false;
		boolean countParamExist = false;
		String outFile = "";
		int count = 1;

		// get arguments
		for (String arg:args)
			if (arg.equals("-h"))
				helpParmExist = true;
			else if (arg.equals("-c"))
				countParamExist = true;
			else if (arg.equals("-o"))
				outParmExist = true;
			else if (countParamExist) { // first argument after -c
				count = Integer.parseInt(arg);
				countParamExist = false;
			} else if (outParmExist && outFile.equals("")) { // first argument after -o
				outFile = arg;
				outParmExist = false;
			}
		if (helpParmExist)
			System.out.println(HELP_INFORMATION);
		else if (!outFile.equals(""))
			System.out.println("Output directed to \"" + outFile + "\"");


		// Create sodoku
		final SodokuGenerator sg = new SodokuGenerator();
		// initial call of generateLines
		sg.generateLines();
		// generate needed count of sodokus
		for (int i = 0; i < count; i++)
			sg.generateSodoku();


		// Write to outFile or Console, if outFile not passed.
		if (!outFile.equals("")) { // print to outFile
			try {
				final BufferedWriter bw = new BufferedWriter(Files.newBufferedWriter(Paths.get(outFile)));
				sg.print((arg) -> {
					try {
						bw.write(arg);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else // print to Console
			sg.print(System.out::println); // alternative: System.out.println(sg);
	}
}
