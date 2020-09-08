import java.util.ArrayList;
import java.util.Iterator;

public class Action {
	// General description
	// Model compiler
	// Implements class Action in the Meta-Model
	// Invariants
	// none
	// Implementation notes
	// I decided to use a slightly-modified Java as the action language (Java Action
	// Language = JAL). Here's why:
	// with a few minor modifications to Java to make it an action language, the
	// developer has the full power
	// of Java available in their actions. They can literally do anything that Java
	// can do. The other great
	// benefit is that a developer need not learn yet-another-language (SMALL, TALL,
	// Alf, etc.) to take advantage
	// of model-based development. It also takes a huge load off of this model
	// compiler because now it doesn't
	// have to bother with the action language except for the few minor tweaks. The
	// down side is losing
	// portability that's inherent in those other action languages. They can be
	// compiled into a ton of different
	// concrete languages, Java, C++, C, ... JAL is limited to running on Java
	// platforms unless someone has
	// a handy cross-compiler available.
	// Application notes
	// JAL's few tweaks to standard Java are:
	// when you want to use a string, instead of saying "some string", you need to
	// say @ssome string@.
	// be careful, if you say @s some string @ then the Java is " some string ".
	// Everything between
	// the @s and the following @ is considered part of the Java version of the
	// string.
	// (hint, the code @s was used because "s" suggests "String"
	// when you want to refer to an attribute, use @i attribute name @. this will
	// allow JAL and the
	// model compiler to translate the name you see in the model in the same way
	// that the model
	// compiler itself does. the compiler could change underlying naming conventions
	// and the
	// JAL action spec won't be affected in any way.
	// (hint, the code @i was used because "i" suggests "instance level naming
	// conventions"
	// when you want to refer to a class, use @c class name @. again, this allows
	// JAL and the model
	// compiler to translate it into the same internal naming convention and be
	// invisible to the
	// PIM action spec developer
	// (hint, the code @c was used because "c" suggests "Class level naming
	// conventions"
	// along the same lines, using @e some string @, JAL and the compiler will turn
	// it into the appropriately-
	// styled ENUM (e.g., a state name).
	// If you need to do a state comparison in JAL, this is probably the best way
	// for now: assume you are
	// writing JAL action specs for a semantic business model class named "an
	// example class". It has two
	// states, "one state" and "the other state". It also uses the default "state"
	// instance variable.
	// if you need to see if an object is in "one state" in a JAL action spec, you
	// should be able to
	// do it like this
	// if( @i state @ == @c an example class @_states.@e one state @ ) {
	// this model compiler would translate that JAL into this generated code
	// if( state == AnExampleClass_states.ONESTATE ) {
	// and this is exactly how the compiler does it in model-generated Java code.
	// there may
	// be a more JAL convenient way to to do it later, but this at least works for
	// now
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
	public static final String defaultInitializeActionName = "initializer";
	public static final String defaultFinalizeActionName = "finalizer";
	public static String tagActionStart = "<action>";
	public static String tagActionEnd = "</action>";
	public static String tagTransitionAction = "<transitionaction>";
	public static String tagStateAction = "<stateaction";
	public static String tagPIMReturnDataType = "<pimreturndatatype>";
	public static String tagPIMActionStart = "<pimaction>";
	public static String tagPIMActionEnd = "</pimaction>";
	public static String tagPIMHelperStart = "<pimhelper>";
	public static String tagPIMHelperEnd = "</pimhelper>";

	// Static (class) variables

	// none

	// Static (class) methods

	public static Action parseAction() {
		// requires
		// Input model stream is open and ready to read at the first line after
		// "<action>"
		// guarantees
		// returns a populated instance of action (or null, if nothing/error in model
		// file)
		// and the input model string is right after "</action>"
		Action newAction = new Action(JALInput.nextLine());

		// check if next line is action description
		String line = JALInput.nextLine();
		if (line.contains(NameService.tagDescriptionStart)) {
			newAction.setDescription(JALInput.nextLine());
			line = JALInput.nextLine();
		}

		// now look for modifiers: requires, guarantees, PIM action spec, ...
		while (!line.contains(Action.tagActionEnd)) {
			if (line.contains(Parameter.tagParameterStart)) {
				newAction.addParameter(Parameter.parseParameter());
			}
			if (line.contains(Condition.tagRequiresStart)) {
				newAction.addRequires(Condition.parseCondition());
			}
			if (line.contains(Condition.tagGuaranteesStart)) {
				newAction.addGuarantees(Condition.parseCondition());
			}
			if (line.contains(Action.tagPIMReturnDataType)) {
				newAction.setRange(Range.rangeNamed(JALInput.nextLine()));
				if (newAction.returnRange() == null) {
					System.out.println(" ***** Cannot find declared Range for Action " + newAction.name() + " on class "
							+ Context.mMClass().name());
				}
			}
			if (line.contains(Action.tagPIMActionStart)) {
				newAction.clearPIMActionSpec();
				line = JALInput.nextLine();
				while (!line.contains(Action.tagPIMActionEnd)) {
					newAction.addPIMActionStmt(line.substring(1));
					line = JALInput.nextLine();
				}
			}
			line = JALInput.nextLine();
		}
		return newAction;
	}

	// Instance variables

	// Meta-model instance variables
	private String name;
	private String description;
	private ArrayList<Parameter> parameterSet;
	private ArrayList<Condition> requiresSet;
	private ArrayList<Condition> guaranteesSet;
	private Range returnRange;

	// PIM Overlay instance variables
	private ArrayList<String> pIMOverlayActionSpec;

	// Model compiler instance variables
	// none

	// Constructor(s)

	public Action(String aName) {
		// description
		// default constructor
		// requires
		// aName <> null and is unique among all Actions in this class
		// guarantees
		// an Action has been created with an empty parameter set
		name = aName;
		description = "none";
		parameterSet = new ArrayList<Parameter>();
		requiresSet = new ArrayList<Condition>();
		guaranteesSet = new ArrayList<Condition>();
		returnRange = null;
		pIMOverlayActionSpec = new ArrayList<String>();
	}

	// Accessors

	public String name() {
		// description
		// a meta-model operation that returns the name of this Action as a String
		// requires
		// none
		// guarantees
		// the name of this Action has been returned as a String
		return name;
	}

	public String description() {
		// description
		// a meta-model operation that returns the description of this Action as a
		// String
		// requires
		// none
		// guarantees
		// the description of this Action has been returned as a String
		return description;
	}

	public ArrayList<Parameter> parameterSet() {
		// description
		// a meta-model operation that returns the parameter set for this Action
		// requires
		// none
		// guarantees
		// the parameter set for this Action has been returned
		return parameterSet;
	}

	public ArrayList<Condition> requiresSet() {
		// description
		// a meta-model operation that returns the requires (i.e., pre-condition) set
		// for this Action
		// requires
		// none
		// guarantees
		// the requires set for this Action has been returned
		return requiresSet;
	}

	public ArrayList<Condition> guaranteesSet() {
		// description
		// a meta-model operation that returns the guarantees (i.e., post-condition) set
		// for this Action
		// requires
		// none
		// guarantees
		// the guarantees set for this Action has been returned
		return guaranteesSet;
	}

	public Range returnRange() {
		// description
		// a meta-model operation that returns the defined range (if any) for this
		// Action
		// requires
		// none
		// guarantees
		// the return value Range for this Action has been returned
		return returnRange;
	}

	// Modifiers

	public void setDescription(String aDescription) {
		// description
		// a meta-model operation that overwrites the description of this action
		// requires
		// none
		// guarantees
		// the action description is overwritten with aDescription
		description = aDescription;
	}

	public void addParameter(Parameter parameterToAdd) {
		// description
		// a meta-model operation that adds the given parameter to the set of parameters
		// on this Action
		// requires
		// parameterToAdd <> null and is unique among all Parameters on this Action
		// guarantees
		// parameterToAdd has been added to the set
		parameterSet.add(parameterToAdd);
	}

	public void addRequires(Condition conditionToAdd) {
		// description
		// a meta-model operation that adds the given condition to the requires (i.e.,
		// pre-condition) set on this Action
		// requires
		// conditionToAdd <> null and is unique among all requires Conditions on this
		// Action
		// guarantees
		// conditionToAdd has been added to the set
		requiresSet.add(conditionToAdd);
	}

	public void addGuarantees(Condition conditionToAdd) {
		// description
		// a meta-model operation that adds the given condition to the guarantees (i.e.,
		// post-condition) set on this Action
		// requires
		// conditionToAdd <> null and is unique among all guarantees Conditions on this
		// Action
		// guarantees
		// conditionToAdd has been added to the set
		guaranteesSet.add(conditionToAdd);
	}

	public void setRange(Range aReturnRange) {
		// description
		// a PIM Overlay operation that sets the PIM return type for this Action
		// requires
		// none
		// guarantees
		// the PIM return type for this Action has been set to newReturnType
		returnRange = aReturnRange;
	}

	public void clearPIMActionSpec() {
		// description
		// a PIM Overlay operation that clears the action language specification for
		// this Action
		// requires
		// none
		// guarantees
		// the action language spec for this Action has been set to empty
		pIMOverlayActionSpec.clear();
	}

	public void addPIMActionStmt(String newActionStmt) {
		// description
		// a PIM Overlay operation that appends this action language statement to the
		// action language specification for this Action
		// requires
		// none, null statements should not hurt
		// guarantees
		// the new ction language statement has been added to the action language spec
		// for this Action
		pIMOverlayActionSpec.add(newActionStmt);
	}

	// Private methods

	// none

	// ******MODEL COMPILER******
	// Everything from here down is specific to the model compiler itself

	public void ruleCallAction() {
		// description
		// This rule emits code for a call to this Action (e.g., on a Transition)
		// including the list of actual parameters, if any
		//
		// Action.#CALL_ACTION -->
		// if this action has a non-blank PIM return run time type
		// then ' aResult = ' +
		// 'this.' + formatted action name +
		// if this action has parameters
		// then '( ' + foreach aParameter on action
		// aParameter.#ACTUAL_PARAMETER + ', ' (except after last one) +
		// ');'
		// otherwise '();'
		//
		// requires
		// none
		// guarantees
		// the code for an invocation (call to) this Action has been emitted
		if (returnRange != null) {
			Context.codeOutput().print(" aResult = ");
		}
		// originally it was asInstanceLevelName but I replace with SnakeStyle
		// Context.codeOutput().print("this." + NameService.asInstanceLevelName(name));
		Context.codeOutput().print("self._" + NameService.asSnakeStyleName(name));
		Iterator<Parameter> parameterIterator = parameterSet.iterator();
		if (parameterIterator.hasNext()) {
			Context.codeOutput().print("( ");
			while (parameterIterator.hasNext()) {
				Parameter aParameter = parameterIterator.next();
				aParameter.ruleActualParameter();
				if (parameterIterator.hasNext()) {
					Context.codeOutput().print(", ");
				}
			}
			Context.codeOutput().println(" )");
		} else {
			Context.codeOutput().println("()");
		}
	}

	public void ruleDefinePrivateAction() {
		// description
		// this rule emits the code that defines (implements) the action as a private
		// method
		//
		// Action.#DEFINE_PRIVATE_ACTION -->
		// 'private ' +
		// if this action has a non-blank PIM return run time type
		// then PIM return run time type + ' ' +
		// otherwise 'void '
		// formatted action name + #ACTION_FORMAL_PARAMETERS +
		// this.#SPECIFY_CONTRACT +
		// this.#ENTRY_ASSERTIONS +
		// this.#ACTION_BODY +
		// this.#EXIT_ASSERTIONS +
		// '}'
		//
		// requires
		// none
		// guarantees
		// the code for the private method implementation of this Action has been
		// emitted
		Context.codeOutput().indent();
		Context.codeOutput().print("def _");
		Context.codeOutput().print(NameService.asSnakeStyleName(name));
		this.ruleActionFormalParameters();
		this.ruleEndReturnTypeForAction(returnRange);
		Context.codeOutput().println(":");

		this.ruleSpecifyContract();
		Context.codeOutput().indent();
		if (Context.model().isAssertionsOn()) {
			this.ruleEntryAssertions();
		}
		this.ruleActionBody();
		if (Context.model().isAssertionsOn()) {
			this.ruleExitAssertions();
		}
		Context.codeOutput().indentLess();
		Context.codeOutput().indent();
		Context.codeOutput().println("");
	}

	public void ruleEndReturnTypeForAction(Range returnRange) {
		// description
		// this rule emits the formal parameters for the implementation of an Action
		//
		// Action.#ACTION_FORMAL_PARAMETERS -->
		// if this action has parameters
		// then '(self,' + foreach aParameter on action
		// aParameter.#FORMAL_PARAMETER + ', ' (except after last one) +
		// ')' +
		// otherwise '(self)'
		//
		// requires
		// none
		// guarantees
		// the code for the private method implementation of this Action has been
		// emitted
		if (returnRange != null) {
			Context.codeOutput().print(" -> " + returnRange.pIMRunTimeType());
		}
	}

	public void ruleActionFormalParameters() {
		// description
		// this rule emits the formal parameters for the implementation of an Action
		//
		// Action.#ACTION_FORMAL_PARAMETERS -->
		// if this action has parameters
		// then '(self,' + foreach aParameter on action
		// aParameter.#FORMAL_PARAMETER + ', ' (except after last one) +
		// ')' +
		// otherwise '(self)'
		//
		// requires
		// none
		// guarantees
		// the code for the private method implementation of this Action has been
		// emitted
		Iterator<Parameter> parameterIterator = parameterSet.iterator();
		if (parameterIterator.hasNext()) {
			Context.codeOutput().print("(self, ");
			while (parameterIterator.hasNext()) {
				Parameter aParameter = parameterIterator.next();
				aParameter.ruleFormalParameter();
				if (parameterIterator.hasNext()) {
					Context.codeOutput().print(", ");
				}
			}

			Context.codeOutput().print(")");
		} else {
			Context.codeOutput().print("(self)");
		}
	}

	public void ruleSpecifyContract() {
		// description
		// This rule emits the contract for an Action (into the private method
		// definition)
		//
		// Action.#SPECIFY_CONTRACT -->
		// '// requires' +
		// if this action has any requires clauses
		// then foreach aRequiresClause on this action
		// aRequiresClause.#CONTRACT_CONDITION +
		// otherwise '// none' +
		// '// guarantees' +
		// if this action has any guarantees clauses
		// then foreach aGuaranteesClause on this action
		// aGuaranteesClause.#CONTRACT_CONDITION +
		// otherwise '// none'
		//
		// requires
		// none
		// guarantees
		// the contract for the method has been emitted
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().indentMore();
			Context.codeOutput().println("# requires");
			if (!requiresSet.isEmpty()) {
				for (Condition aRequiresClause : requiresSet) {
					aRequiresClause.ruleContractCondition();
				}
			} else {
				Context.codeOutput().indent();
				Context.codeOutput().println("#   none");
			}
			Context.codeOutput().indent();
			Context.codeOutput().println("# guarantees");
			if (!guaranteesSet.isEmpty()) {
				for (Condition aGuaranteesClause : guaranteesSet) {
					aGuaranteesClause.ruleContractCondition();
				}
			} else {
				Context.codeOutput().indent();
				Context.codeOutput().println("#   none");
			}
		}
	}

	public void ruleEntryAssertions() {
		// description
		// This rule emits the entry assertions for an Action (into the private method
		// definition)
		//
		// Action.#ENTRY_ASSERTIONS -->
		// foreach aParameter on this action
		// if aParameter is assertable
		// then aParameter.#ASSERT_PARAMETER +
		// foreach aRequiresClause on this action
		// aRequiresClause.#ASSERT_CONDITION +
		//
		// requires
		// none
		// guarantees
		// the going-in assertions for the private method has been emitted
		for (Parameter aParameter : parameterSet) {
			if (aParameter.isAssertable()) {
				aParameter.ruleAssertParameter();
			}
		}
		for (Condition aRequiresClause : requiresSet) {
			aRequiresClause.ruleAssertCondition();
		}
	}

	public void ruleActionBody() {
		// description
		// This rule emits the PIM Overlay implementation for an Action (into the
		// private method definition)
		//
		// Action.#ACTION_BODY -->
		// foreach aJALstatement in the PIM Overlay for this action
		// #FORMAT_ACTION_STATEMENT
		//
		// requires
		// none
		// guarantees
		// the PIM Overlay action body for the method has been emitted
		for (String actionStmt : pIMOverlayActionSpec) {
			Context.codeOutput().println(NameService.formatActionStmt(actionStmt));
			Context.codeOutput().indent();
		}
	}

	public void ruleExitAssertions() {
		// description
		// This rule emits the exit assertions for an Action (into the private method
		// definition)
		//
		// Action.#EXIT_ASSERTIONS -->
		// if this action has any guarantees clauses
		// then foreach aGuaranteesClause on this action
		// aGuaranteesClause.#ASSERT_CONDITION +
		//
		// requires
		// none
		// guarantees
		// the going-in assertions for the private method has been emitted
		if (!guaranteesSet.isEmpty()) {
			for (Condition aGuaranteesClause : guaranteesSet) {
				aGuaranteesClause.ruleAssertCondition();
			}
		}
	}

}
