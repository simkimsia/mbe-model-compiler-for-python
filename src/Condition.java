
public class Condition {
	// General description
	// Model compiler
	// Implements class Condition in the Meta-Model
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
	// This material is Copyright � 2018 by Stephen R. Tockey
	// Permission is hereby given to copy, adapt, and distribute this material as
	// long as this notice is included on all such materials and the materials are
	// not sold, licensed, or otherwise distributed for commercial gain.
	//
	// This tool is being distributed "as-is". No warrantee is expressed or implied.
	// While the author believes that this tool gives correct answers, the user
	// assumes all risk of use.

	// Constants

	public static String tagRequiresStart = "<requires>";
	public static String tagRequiresEnd = "</requires>";
	public static String tagGuaranteesStart = "<guarantees>";
	public static String tagGuaranteesEnd = "</guarantees>";
	public static String tagConditionAssertStart = "<assert>";
	public static String tagConditionAssertMessageStart = "<assertmessage>";

	// Static (class) variables

	// none

	// Static (class) methods

	public static Condition parseCondition() {
		// requires
		// Input model stream is open and ready to read first line after "<requires>" or
		// "<guarantees>"
		// guarantees
		// returns a populated instance of condition (or null, if nothing/error in model
		// file)
		// and input model string is right after condition definition
		Condition newCondition = new Condition(JALInput.nextLine());

		String line = JALInput.nextLine();

		// now look for modifiers: assertion expression, assertion message
		while (!(line.contains(Condition.tagRequiresEnd) || line.contains(Condition.tagGuaranteesEnd))) {
			if (line.contains(Condition.tagConditionAssertStart)) {
				newCondition.setPIMOverlayAssertionExpression(JALInput.nextLine());
			}
			if (line.contains(Condition.tagConditionAssertMessageStart)) {
				newCondition.setPIMOverlayAssertionMessage(JALInput.nextLine());
			}
			line = JALInput.nextLine();
		}
		return newCondition;
	}

	// Instance variables

	// Meta-model instance variables
	private String expression;
	// private String description;

	// PIM Overlay instance variables
	private String pIMOverlayAssertExpression;
	private String pIMOverlayAssertMessage;

	// Model compiler instance variables
	// none

	// Constructor(s)

	public Condition(String anExpression) {
		// description
		// default constructor
		// requires
		// anExpression <> null
		// guarantees
		// a new Condition exists
		expression = anExpression;
		pIMOverlayAssertExpression = null;
		pIMOverlayAssertMessage = null;
	}

	// Accessors

	public String expression() {
		// description
		// a meta-model operation that returns the expression for this Condition
		// requires
		// none
		// guarantees
		// the condition expression is returned
		return expression;
	}

	public boolean isAssertable() {
		// description
		// a PIM Overlay operation that returns whether this condition is assert-able or
		// not
		// requires
		// none
		// guarantees
		// returns true only when pIMOverlayAssertExpression != null
		return pIMOverlayAssertExpression != null;
	}

	public String pIMOverlayAssertionExpression() {
		// description
		// returns the PIM Overlay assertion expression
		// requires
		// none
		// guarantees
		// returns pIMOverlayAssertExpression, event when it is null
		return pIMOverlayAssertExpression;
	}

	public boolean hasAssertionMessage() {
		// description
		// a PIM Overlay operation that returns whether this condition has an assertion
		// message or not
		// requires
		// none
		// guarantees
		// returns true only when pIMOverlayAssertMessage != null
		return pIMOverlayAssertMessage != null;
	}

	public String pIMOverlayAssertionMessage() {
		// description
		// returns the PIM Overlay assertion message
		// requires
		// none
		// guarantees
		// returns pIMOverlayAssertMessage, event when it is null
		return pIMOverlayAssertMessage;
	}

	// Modifiers

	public void setPIMOverlayAssertionExpression(String anAssertionExpression) {
		// description
		// sets the PIM Overlay assertion expression
		// requires
		// none
		// guarantees
		// pIMOverlayAssertExpression has been overwritten with anAssertionExpression
		pIMOverlayAssertExpression = anAssertionExpression;
	}

	public void setPIMOverlayAssertionMessage(String anAssertionMessage) {
		// description
		// sets the PIM Overlay assertion message
		// requires
		// none
		// guarantees
		// pIMOverlayAssertMessage has been overwritten by anAssertionMessage
		pIMOverlayAssertMessage = anAssertionMessage;
	}

	// Private methods

	// none

	// ******MODEL COMPILER******
	// Everything from here down is specific to the model compiler itself

	public void ruleContractCondition() {
		// description
		// This rule emits a contract condition as a comment
		//
		// Condition.#CONTRACT_CONDITION -->
		// '// ' + expression
		//
		// requires
		// none
		// guarantees
		// this contract condition for an Action has been emitted
		Context.codeOutput().indent();
		Context.codeOutput().println("#   " + expression);
	}

	public void ruleAssertCondition() {
		// description
		// This rule emits an assertion for this condition, if it has one
		//
		// Condition.#ASSERT_CONDITION -->
		// if( is assertable )
		// then 'assert( ' + assertion expression ')' +
		// if( has assertion message )
		// ': ' + assertion message +
		// ';'
		//
		// requires
		// none
		// guarantees
		// the assertion for this Condition has been emitted
		if (this.isAssertable()) {
			Context.codeOutput().print("assert( " + NameService.formatActionStmt(pIMOverlayAssertExpression) + " )");
			if (this.hasAssertionMessage()) {
				Context.codeOutput().print(": \"" + NameService.formatActionStmt(pIMOverlayAssertMessage) + "\"");
			}
			Context.codeOutput().println(";");
			Context.codeOutput().indent();
		}
	}

}
