import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Model {
	// General description
	// Model compiler
	// this class implements class Model in the Meta-Model
	// Invariants
	// none
	// Implementation notes
	// Note that there are differences between the production rules as stated in the
	// book vs. here
	// That difference is mostly--if not all--due to a need for clarity in book vs.
	// the need to put rules on proper classes here (cohesion of rule with
	// meta-model class)
	// There is also a lot of what appears to be copy-paste repetition in many of
	// the rules:
	// at least for this proof-of-concept, I want to keep the rules as
	// self-contained as possible
	// In general, there's also an attempt to separate meta-model code from compiler
	// code. Compiler-
	// specific code should be concentrated at the bottom of each compiler related
	// class. The same
	// holds for PIM Overlay code, there's an attempt to separate it too.
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

	// none
	public static String tagModelStart = "<jalmodel>";
	public static String tagModelEnd = "</jalmodel>";
	public static final String tagJavaSourceCodePath = "<javasourcecodedirectory>";
	public static final String tagIsVerboseOn = "<isverboseon>";
	public static final String tagIsAssertionsOn = "<isassertionson>";
	public static final String tagIsSafeModeOn = "<issafemodeon>";
	public static final String tagIncludePackageName = "<includepackagename>";
	public static final String tagLanguageFrameworkOutput = "<languageframeworkoutput>";

	// Static (class) variables

	private static String target = null;

	// Static (class) methods

	public static String target() {
		return target;
	}

	public static Model parseModel() {
		// requires
		// Input model stream is open and ready to read at the first line after
		// "<model>"
		// guarantees
		// returns an instance of Model (possibly only partially populated if there's an
		// error in input file)
		// and input model string is right after "</model>"
		Model newModel = new Model(JALInput.nextLine());
		Context.setModel(newModel);

		// check if next line is a model description
		String line = JALInput.nextLine();
		if (line.contains(NameService.tagDescriptionStart)) {
			newModel.setDescription(JALInput.nextLine());
			line = JALInput.nextLine();
		}

		// now look for settings, ranges, classes, associations, ...
		while (!line.contains(Model.tagModelEnd)) {
			if (line.contains(Model.tagJavaSourceCodePath)) {
				newModel.setJavaSourceCodePath(JALInput.nextLine());
			}
			if (line.contains(Model.tagIsVerboseOn)) {
				newModel.setIsVerboseOn(JALInput.nextLine().equals("true"));
			}
			if (line.contains(Model.tagIsAssertionsOn)) {
				newModel.setIsAssertionsOn(JALInput.nextLine().equals("true"));
			}
			if (line.contains(Model.tagIsSafeModeOn)) {
				newModel.setIsSafeModeOn(JALInput.nextLine().equals("true"));
			}
			if (line.contains(Model.tagIncludePackageName)) {
				newModel.setIncludePackageName(JALInput.nextLine().equals("true"));
			}
			if (line.contains(Model.tagLanguageFrameworkOutput)) {
				target = JALInput.nextLine().trim();
				Context.setTarget(target);
				Context.setCodeOutput(codeOutputFactory());
			}
			if (line.contains(Range.tagRangeStart)) {
				Range newRange = Range.parseRange();
			}
			if (line.contains(MMClass.tagClassStart)) {
				MMClass newMMClass = MMClass.parseClass();
			}
			if (line.contains(Association.tagAssociationStart)) {
				Association newAssociation = Association.parseAssociation();
			}

			line = JALInput.nextLine();
		}
		Context.clearModel();
		return newModel;
	}

	public static CodeOutput codeOutputFactory() {
		// requires
		// none
		// guarantees
		// returns an instance of the right subclass of the abstract class CodeOutput
		if (target.equals("python37")) {
			return new Python37Output();
		}
		if (target.equals("django22")) {
			return new Django22Output();
		}
		return null;
	}

	// Instance variables

	// Meta-model instance variables
	protected String name;
	protected String description;

	// PIM Overlay instance variables
	private String javaSourceCodePath;
	private boolean isVerboseOn;
	private boolean isAssertionsOn;
	private boolean isSafeModeOn;
	private boolean includePackageName;

	// python specific PIM Overlay instance variables
	private boolean includeFutureAnnotations = true;

	// Model compiler instance variables
	// none

	// Constructor(s)

	public Model(String aName) {
		// description
		// default constructor
		// requires
		// aName <> null (because it affects code paths and generated package names)
		// guarantees
		// an instance has been created and initialized to empty
		name = aName;
		description = "No description has been specified";
	}

	// Accessors

	public String name() {
		// description
		// returns the model name as a String
		// requires
		// none
		// guarantees
		// the model's name is returned as a String
		return name;
	}

	public String description() {
		// description
		// returns the model description as a String, possible for verbose code
		// generation
		// requires
		// none
		// guarantees
		// the model's description is returned as a String
		return description;
	}

	public String javaSourceCodePath() {
		// requires
		// none
		// guarantees
		// returns the currently specified java source code path for this model
		return javaSourceCodePath;
	}

	public boolean isVerbose() {
		// description
		// This operation is about enabling / disabling verbose vs. terse code
		// generation
		// in verbose mode, commentary & descriptive model content is included in
		// generated source code
		// this includes things like the class description, contracts, ... Verbose mode
		// code is a
		// lot easier to read if there's some reason you need to be looking inside the
		// generated
		// source code. In terse mode, essentially only code-to-be-compiled is generated
		// requires
		// none
		// guarantees
		// returns true when verbose mode is enabled, otherwise returns false
		return isVerboseOn;
	}

	public boolean isAssertionsOn() {
		// description
		// This operation is about enabling / disabling assertions vs. not in generated
		// code
		// in assertions on mode, as many assertions as possible are generated into the
		// source code
		// requires
		// none
		// guarantees
		// returns true when AssertionsOn mode is enabled, otherwise returns false
		return isAssertionsOn;
	}

	public boolean isSafeModeOn() {
		// description
		// This operation is about enabling / disabling invariants vs. not in generated
		// code
		// in safe mode, as many relevant instance invariants as possible are generated
		// into the source code
		// requires
		// none
		// guarantees
		// returns true when InvariantsOn mode is enabled, otherwise returns false
		return isSafeModeOn;
	}

	public boolean includePackageName() {
		// description
		// this operation is about including 'package <modelname>' in generated source
		// code
		// requires
		// none
		// guarantees
		// returns the current 'include package name' setting for this model
		return includePackageName;
	}

	public boolean includeFutureAnnotations() {
		// description
		// this operation is about including 'package <modelname>' in generated source
		// code
		// requires
		// none
		// guarantees
		// returns the current 'include package name' setting for this model
		return includeFutureAnnotations;
	}

	// Modifiers

	public void setDescription(String aDescription) {
		// description
		// over-writes the description for the model
		// requires
		// none
		// guarantees
		// description has been set to aDescription
		description = aDescription;
	}

	public void setJavaSourceCodePath(String newJavaSourceCodePath) {
		// requires
		// newJavaSourceCodePath <> null and is a valid path name
		// guarantees
		// the java source code path for this model has been overwritten
		javaSourceCodePath = newJavaSourceCodePath;
	}

	public void setIsVerboseOn(boolean newIsVerboseOn) {
		// requires
		// none
		// guarantees
		// the 'isVerboseOn' setting for this model has been overwritten
		isVerboseOn = newIsVerboseOn;
	}

	public void setIsAssertionsOn(boolean newIsAssertionsOn) {
		// requires
		// none
		// guarantees
		// the 'isAssertionsOn' setting for this modelhas been overwritten
		isAssertionsOn = newIsAssertionsOn;
	}

	public void setIsSafeModeOn(boolean newIsSafeModeOn) {
		// requires
		// none
		// guarantees
		// the 'isSafeModeOn' setting for this model has been overwritten
		isSafeModeOn = newIsSafeModeOn;
	}

	public void setIncludePackageName(boolean newIncludePackageName) {
		// requires
		// none
		// guarantees
		// the 'isSafeModeOn' setting for this model has been overwritten
		includePackageName = newIncludePackageName;
	}

	// Private methods

	// none

	// ******MODEL COMPILER******
	// Everything from here down is specific to the model compiler itself

	public void ruleCompile() {
		// description
		// This is "rule zero", the highest level rule in the compiler. Everything
		// starts here.
		//
		// Model.#COMPILE -->
		// foreach aClass in aModel
		// aClass.#JAVA_CLASS_FILE
		// if is safe mode
		// aModel.#RULE_SAFE_MODE_CLASS
		//
		// requires
		// a populated semantic model exists to generate code from
		// guarantees
		// Java code for all (compile-able) classes in this Model has been emitted
		// some various error messages might have been generated along the way
		System.out.println();
		System.out.println("*** Compiling model: '" + name + "' ***");
		System.out.println();
		Context.setModel(this);
		Context.codeOutput().clearErrorCount();
		for (MMClass aClass : MMClass.allMMClasses()) {
			aClass.rulePythonClassFile();
		}
		if (isSafeModeOn) {
			this.ruleSafeModeClass();
		}
		Context.clearModel();
		System.out.println(
				"*** DONE compiling model ***    Number of compiler errors = " + Context.codeOutput().errorCount());
	}

	public void ruleSafeModeClass() {
		// description
		// This rule emits class SafeMode when safe mode is enabled for the model
		//
		// Model.#SAFE_MODE_CLASS -->
		// open the SafeMode.java file
		// if include package name
		// 'package ' aModel.#MODEL_NAME
		// 'public class SafeMode'
		// if is verbose
		// '// Implements top level SafeMode behavior for' + model name
		// 'public static void safeModeCheck() {'
		// if is verbose
		// '// requires'
		// '// none'
		// '// guarantees'
		// '// all "safe mode" class invariants checks have been run-time checked in '
		// model name
		// foreach aClass in aModel
		// 'for( ' + class name + ' a' + class name + ': ' +
		// class name + '.all' + class name + 's() ) {'
		// 'a' + class name + '.classInvariantsCheck();'
		// '}'
		// '}'
		// '}'
		// close the SafeMode.java file
		//
		// requires
		// none
		// guarantees
		// Java code for the top-level safe mode functionality has been emitted
		String outputJALFileName = Context.model().javaSourceCodePath() + "SafeMode.java";
		System.out.println("--compiling: " + outputJALFileName);
		Context.codeOutput().openJALOutputFile(outputJALFileName);
		Context.codeOutput().indentNone();
		if (Context.model().includePackageName()) {
			Context.codeOutput().indent();
			Context.codeOutput().println("package " + NameService.asClassLevelName(Context.model().name()) + ";");
			Context.codeOutput().println("");
		}
		if (Context.model().includeFutureAnnotations()) {
			Context.codeOutput().println("from __future__ import annotations");
			Context.codeOutput().println("");
		}
		Context.codeOutput().indent();
		Context.codeOutput().println("public class SafeMode {");
		Context.codeOutput().println("");
		Context.codeOutput().indentMore();
		DateFormat aDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String todayString = aDateFormat.format(new Date());
		Context.codeOutput().indent();
		Context.codeOutput()
				.println("// generated " + todayString + " by JAL open model compiler " + ModelCompiler.versionId);
		Context.codeOutput().println("");
		Context.codeOutput().println("");
		if (isVerboseOn) {
			Context.codeOutput().indent();
			Context.codeOutput().println("// Implements top level SafeMode behavior for " + Context.model().name());
			Context.codeOutput().println("");
			Context.codeOutput().println("");
		}
		Context.codeOutput().indent();
		Context.codeOutput().println("public static void safeModeCheck() {");
		if (isVerboseOn) {
			Context.codeOutput().indent();
			Context.codeOutput().println("# requires");
			Context.codeOutput().println("#    none");
			Context.codeOutput().println("# requires");
			Context.codeOutput().println("#    all 'safe mode' class invariant checks have been run-time checked in "
					+ Context.model().name());
		}
		Context.codeOutput().indentMore();
		for (MMClass aClass : MMClass.allMMClasses()) {
			Context.codeOutput().indent();
			Context.codeOutput().print("for( " + NameService.asClassLevelName(aClass.name()) + " a"
					+ NameService.asClassLevelName(aClass.name()) + ": ");
			Context.codeOutput().println(NameService.asClassLevelName(aClass.name()) + ".all"
					+ NameService.asClassLevelName(aClass.name()) + "s() ) {");
			Context.codeOutput().indentMore();
			Context.codeOutput().indent();
			Context.codeOutput()
					.println("a" + NameService.asClassLevelName(aClass.name()) + ".classInvariantsCheck();");
			Context.codeOutput().indentLess();
			Context.codeOutput().indent();

		}
		Context.codeOutput().indentLess();
		Context.codeOutput().indent();

		Context.codeOutput().println("");
		Context.codeOutput().println("");
		Context.codeOutput().indentLess();
		Context.codeOutput().indent();

		Context.codeOutput().println("");
		Context.codeOutput().println("");
		Context.codeOutput().closeJALOutputFile();
		System.out.println("--done compiling: " + outputJALFileName);
		System.out.println();
	}

}
