
public class Parameter {
	// General description
	// Model compiler
	// This class implements Parameter in the Meta-Model
	// Invariants
	// none
	// Implementation notes
	// none
	// Application notes
	// none
	//
	// This material is copied and/or adapted from the (to be)
	// How to Engineer Software website
	//
	// http://www.Construx.com/howtoengineersw
	//
	// This material is Copyright � 2016 by Stephen R. Tockey
	// Permission is hereby given to copy, adapt, and distribute this material as
	// long as this notice is included on all such materials and the materials are
	// not sold, licensed, or otherwise distributed for commercial gain.
	//
	// This tool is being distributed "as-is". No warrantee is expressed or implied.
	// While the author believes that this tool gives correct answers, the user
	// assumes all risk of use.

	// Constants

	public static String tagParameterStart = "<parameter>";

	// Static (class) variables

	// none

	// Static (class) methods

	public static Parameter parseParameter() {
		// requires
		// Input model stream is open and ready to read first line after "<parameter>"
		// guarantees
		// returns a populated instance of parameter (or null, if nothing/error in model
		// file)
		// and input model string is right after run time type
		Parameter newParameter = new Parameter(JALInput.nextLine(), Range.rangeNamed(JALInput.nextLine()));
		return newParameter;
	}

	// Instance variables

	// Meta-model instance variables
	private String name;
	// private String description;
	private Range definedRange;

	// PIM Overlay instance variables
	// none

	// Model compiler instance variables
	// none

	// Constructor(s)

	public Parameter(String aName, Range aRange) {
		// description
		// default constructor
		// requires
		// aName <> null
		// anRTType <> null
		// guarantees
		// an instance of Parameter has been created and initialized
		name = aName;
		definedRange = aRange;
	}

	// Accessors

	public boolean isAssertable() {
		// description
		// a PIM Overlay operation that returns whether this parameter is assert-able or
		// not
		// requires
		// none
		// guarantees
		// returns true only when pIMOverlayAssertExpression != null
		return definedRange.isAssertable();
	}

	// Modifiers

	// none

	// Private methods

	// none

	// ******MODEL COMPILER******
	// Everything from here down is specific to the model compiler itself

	public void ruleFormalParameter() {
		// description
		// This rule generates the formal parameter name in the signature of the
		// (private) method implementing the Action
		//
		// Parameter.#FORMAL_PARAMETER -->
		// output formatted parameter name + ":" + PIM Overlay RT Type
		//
		// requires
		// none
		// guarantees
		// the run-time type (from the PIM Overlay) and the parameter name have been
		// emitted
		Context.codeOutput().print(
				NameService.asSnakeStyleName(name) + ":" + NameService.formatActionStmt(definedRange.pIMRunTimeType()));
	}

	public void ruleAssertParameter() {
		// description
		// This rule generates an assertion on the formal parameter
		//
		// Parameter.#ASSERT_PARAMETER -->
		// 'assert( ' + PIM overlay assertion expression + ' );'
		//
		// requires
		// none
		// guarantees
		// the parameter assertion has been emitted
		Context.codeOutput().println(
				"assert( " + definedRange.pIMAssertionExpression(NameService.asInstanceLevelName(name)) + " );");
		Context.codeOutput().indent();
	}

	public void ruleActualParameter() {
		// description
		// This rule generates the actual parameter name in an invocation (call to) the
		// containing Action
		//
		// Parameter.#ACTUAL_PARAMETER -->
		// output parameter name as an instance level name
		//
		// requires
		// none
		// guarantees
		// the parameter name, as an actual parameter, has been emitted
		Context.codeOutput().print(NameService.asSnakeStyleName(name));
	}

}
