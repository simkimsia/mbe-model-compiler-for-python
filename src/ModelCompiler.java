
public class ModelCompiler {
	// General description
	// Model Compiler
	// This is the top level of a proof-of-concept demonstration of an open model
	// compiler
	// Invariants
	// none
	// Implementation notes
	// standard entry point into a Java program
	// Application notes
	// none
	//
	// This material is copied and/or adapted from the (to be)
	// How to Engineer Software website
	//
	// http://www.Construx.com/howtoengineersw
	//
	// This material is Copyright � 2019 by Stephen R. Tockey
	// Permission is hereby given to copy, adapt, and distribute this material as
	// long as this notice is included on all such materials and the materials are
	// not sold, licensed, or otherwise distributed for commercial gain.
	//
	// This tool is being distributed "as-is". No warrantee is expressed or implied.
	// While the author believes that this tool gives correct answers, the user
	// assumes all risk of use.

	// Constants

	public static final String versionId = "v5.3";
	public static final String DEFAULT_JAL_IMPORT_PATH = "/Users/kim/eclipse-workspace/mbe-python-model-compiler/JAL/";
	public static final String DEFAULT_JAVA_EXPORT_PATH = "/Users/kim/eclipse-workspace/mbe-python-model-compiler/generated/";

	// Static (class) variables

	// none

	// Static (class) methods

	public static void main(String[] args) {
		// description
		// the main entry point for the open model compiler
		// requires
		// none
		// guarantees
		// the CIM/PIM has been loaded (populated) and compiled

		System.out.println();
		System.out.println("JAL Model Compiler " + versionId);
		System.out.println();

		boolean success = JALInput.openModelFile(DEFAULT_JAL_IMPORT_PATH);
		if (success) {
			System.out.println();
			System.out.println("*** Starting to parse the model input file");
			if (JALInput.nextLine().contains(Model.tagModelStart)) {
				Model theModel = Model.parseModel();

				System.out.println("*** Done parsing the model input file");
				JALInput.close();

				theModel.ruleCompile();
			} else {
				System.out.println();
				System.out.println("*** Selected file is not a JAL model input file ");
				System.out.println();
			}
		} else {
			System.out.println();
			System.out.println("*** Cannot open selected input file ");
			System.out.println();
		}
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

	// none

}
