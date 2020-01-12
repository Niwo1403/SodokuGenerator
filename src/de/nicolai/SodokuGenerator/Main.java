/**
 * Package for SodokuGenerator.
 */
package de.nicolai.SodokuGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

/**
 * Start of program.
 * @author Nicolai
 *
 */
public class Main {
	/**
	 * Used as help message, if -h is passed.
	 */
	static final String HELP_INFORMATION = "Call like: \n"
		+ "SodokuCreator.jar [-o <OUTFILE>, -c COUNT]\n"
		+ "\t-h          - show this help\n"
		+ "\t-c COUNT    - count of sodokus to generate\n"
		+ "\t-o OUTFILE  - redirect output to OUTFILE";
	
	/**
	 * Processes the passed arguments, create the sodoku and print them.
	 * @param args arguments passed to the program
	 */
	public static void main(String[] args) {
		boolean outParmExist = false, helpParmExist = false, countParamExist = false;
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
			else if (countParamExist)
				count = Integer.parseInt(arg);
			else if (outParmExist && outFile.equals("")) // first argument after -o
				outFile = arg;
		if (helpParmExist)
			System.out.println(HELP_INFORMATION);
		else if (outParmExist)
			if (outFile.equals("")) // -o passed, but no OUFILE set
				System.out.println("No output file entered. write -h to get information about the arguments.");
			else // OUTFILE is set
				System.out.println("Output directed to \"" + outFile + "\"");
		
		
		// Create sodoku
		SodokuGenerator sg = new SodokuGenerator();
		// initial call of generateLines
		sg.generateLines();
		// generate needed count of sodokus
		for (int i = 0; i < count; i++)
			sg.generateSodoku();
		
		
		// Write to outFile or Console, if outFile not passed.
		if (!outFile.equals("")) { // print to outFile
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
				sg.print((arg) -> {
					try {
						bw.write(arg);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				bw.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else // print to Console
			sg.print(System.out::println); // alternative: System.out.println(sg);
	}
}
