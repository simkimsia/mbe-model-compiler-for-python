import java.io.File;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.filechooser.*;

public class JALInput {
	// General description
	// Model compiler
	// This is a view-controller class that manages the input file stream for the
	// model compiler
	// Invariants
	// none
	// Implementation notes
	// none, for now--maybe later
	// Application notes
	// none
	//
	// This material is copied and/or adapted from the (to be)
	// How to Engineer Software website
	//
	// http://www.Construx.com/howtoengineersw
	//
	// This material is Copyright � 2018 by Stephen R. Tockey
	// Permission is hereby given to copy, adapt, and distribute this material as
	// long as this notice is included on all such materials and the materials are
	// not sold, licensed, or otherwise distributed for commercial gain.
	//
	// This tool is being distributed "as-is". No warrantee is expressed or implied.
	// While the author believes that this tool gives correct answers, the user
	// assumes all risk of use.

	// Constants

	// none

	// Static (class) variables

	private static File selectedFile;
	private static Scanner inputScanner;

	// Static (class) methods

	public static boolean openModelFile(String inputDirectory) {
		// description
		// tries to open a model file
		// requires
		// none
		// guarantees
		// returns true if a model file was opened

		final JFileChooser fileChooser = new JFileChooser(inputDirectory);
		fileChooser.setAcceptAllFileFilterUsed(false);
		JALInput dummyJALInput = new JALInput();
		FileFilter smTxtFilter = dummyJALInput.new DotSmDotTxtFilter();
		fileChooser.addChoosableFileFilter(smTxtFilter);
		fileChooser.setDialogTitle("JAL Model Compiler: select a JAL model ('*.jal.txt') to compile");
		int response = fileChooser.showOpenDialog(null);
		if (response == JFileChooser.APPROVE_OPTION) {
			selectedFile = fileChooser.getSelectedFile();
			try {
				inputScanner = new Scanner(selectedFile);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
	}

	public static boolean hasNext() {
		// description
		// shows whether there is more text in the input model file
		// requires
		// none
		// guarantees
		// returns true if a model file was opened
		return inputScanner.hasNextLine();
	}

	public static String nextLine() {
		// description
		// tries to get the next line from the input model file
		// requires
		// none
		// guarantees
		// if another line is available in the input model file
		// then that line is returned
		// otherwise an error message is output and null is returned
		if (inputScanner.hasNextLine()) {
			return inputScanner.nextLine().trim();
		} else {
			System.out.println("*** tried to read beyond end of model file ");
			return null;
		}
	}

	public static void close() {
		// description
		// closes the input model file
		// requires
		// none
		// guarantees
		// the input model file has been closed
		inputScanner.close();
	}

	// Instance variables

	// none

	// Constructor(s)

	// none

	// Accessors

	// none

	// Modifiers

	// none

	// Private methods

	private class DotSmDotTxtFilter extends FileFilter {

		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			} else {
				if (f.getName().contains(".jal.txt")) {
					return true;
				}
			}
			return false;
		}

		public String getDescription() {
			return "*.jal.txt";
		}

	}

}
