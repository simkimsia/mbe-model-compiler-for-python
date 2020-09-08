import java.util.ArrayList;
import java.util.Iterator;

public class Event {
	// General description
	// Model compiler
	// This class implements class Event from the Meta-Model
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

	public static final String defaultNewEventName = "<<new>>";
	public static final String defaultDestroyEventName = "<<destroy>>";
	public static String tagEventStart = "<event>";
	public static String tagEventEnd = "</event>";

	// Static (class) variables

	// none

	// Static (class) methods

	public static Event parseEvent() {
		// requires
		// Input model stream is open and ready to read first line after "<event>"
		// guarantees
		// returns a populated instance of event (or null, if nothing/error in model
		// file)
		// and input model string is right after "</event>"
		Event newEvent = new Event(JALInput.nextLine());
		Context.setEvent(newEvent);

		// check if next line is event description
		String line = JALInput.nextLine();
		if (line.contains(NameService.tagDescriptionStart)) {
			newEvent.setDescription(JALInput.nextLine());
			line = JALInput.nextLine();
		}
		if (line.contains(Action.tagPIMReturnDataType)) {
			newEvent.setReturnRange(Range.rangeNamed(JALInput.nextLine()));
			if (newEvent.returnRange() == null) {
				System.out.println(" ***** Cannot find declared Range for Event " + newEvent.name() + " on class "
						+ Context.mMClass().name());
			}
			line = JALInput.nextLine();
		}
		while (!line.contains(Event.tagEventEnd)) {
			line = JALInput.nextLine();
		}
		Context.clearEvent();
		return newEvent;
	}

	// Instance variables

	// Meta-model instance variables
	private String name;
	private String description;

	// PIM Overlay instance variables
	private Range returnRange;
	private String zzzpIMReturnRTTypeInitialValue;

	// Model compiler instance variables
	// none

	// Constructor(s)

	public Event(String aName) {
		// description
		// default constructor
		// requires
		// aName <> null and is unique among all Events for this class
		// guarantees
		// an instance of Event has been created
		name = aName;
		description = "none";
		returnRange = null;
	}

	// Accessors

	public String name() {
		// description
		// a meta-model operation that returns the name of this Event as a String
		// requires
		// none
		// guarantees
		// the name of this Event has been returned as a String
		return name;
	}

	public String description() {
		// description
		// a meta-model operation that returns the event description as a string
		// requires
		// nothing
		// guarantees
		// the event's description is been returned
		return description;
	}

	public Range returnRange() {
		// description
		// a meta-model operation that returns the declared PIM return run time type
		// requires
		// nothing
		// guarantees
		// the event's return RT type is been returned
		return returnRange;
	}

	// Modifiers

	public void setDescription(String aDescription) {
		// description
		// a meta-model operation that overwrites the existing description for the event
		// requires
		// none
		// guarantees
		// event description has been reset to aDescription
		description = aDescription;
	}

	public void setReturnRange(Range aReturnRange) {
		// description
		// a PIM Overlay operation that sets the PIM return type for this event
		// requires
		// none
		// guarantees
		// the PIM return type for this Event has been set to newReturnRange
		returnRange = aReturnRange;
	}

	// Private methods

	// none

	// ******MODEL COMPILER******
	// Everything from here down is specific to the model compiler itself

	public void ruleConstructorOperation() {
		// description
		// This rule emits the constructor and its method implementation code
		//
		// Event.#CONSTRUCTOR_OPERATION -->
		// 'def __init__(self' + #OPERATION_FORMAL_PARAMETERS + '):'
		// #OPERATION_CONTRACT_REQUIRES_CLAUSE
		// #OPERATION_CONTRACT_GUARANTEES_CLAUSE
		// #CONSTRUCTOR_METHOD_BODY
		// '}'
		//
		// requires
		// Context.mMClass() <> null
		// guarantees
		// the operation and method code for this Event have been emitted
		Context.setEvent(this);
		Context.codeOutput().indent();
		Context.codeOutput().print("def __init__(self");
		this.ruleOperationFormalParameters();
		Context.codeOutput().println("):");
		if (Context.model().isVerbose()) {
			this.ruleOperationContractRequiresClause();
			this.ruleOperationContractGuaranteesClause();
		}
		Context.codeOutput().indent();
		if (Context.model().isAssertionsOn()) {
			this.ruleEntryAssertions();
		}
		Context.mMClass().ruleInitializeAssociationInstVars();
		this.ruleConstructorMethodBody();
		if (Context.model().isAssertionsOn()) {
			this.ruleExitAssertions();
		}
		// instead of using the class method to add to the set i simply add to the class
		// attribute
		// Context.codeOutput().println(NameService.asInstanceLevelName(Context.mMClass().name())
		// + "Set.add( self )");
		// this should be ClassName.ClassNameSet.append(self)
		Context.codeOutput().println(NameService.asClassLevelName(Context.mMClass().name()) + "."
				+ NameService.asClassLevelName(Context.mMClass().name()) + "Set.append( self )");
		Context.codeOutput().indentLess();
		Context.codeOutput().indent();

		Context.clearEvent();
	}

	public void ruleConstructorMethodBody() {
		// description
		// This rule emits the method body for the constructor operation...
		//
		// Event.#CONSTRUCTOR_METHOD_BODY -->
		// foreach state-event-behavior for this event
		// #CONSTRUCTOR_EVENT_COMPILE
		//
		// requires
		// Context.class <> null
		// Context.event == this
		// guarantees
		// method body code has been emitted
		ArrayList<StateEventBehavior> aBehaviorSet = Context.mMClass().eventBehaviorSet();
		Iterator<StateEventBehavior> behaviorIterator = aBehaviorSet.iterator();
		while (behaviorIterator.hasNext()) {
			StateEventBehavior aBehavior = behaviorIterator.next();
			aBehavior.ruleConstructorBehavior();
			if (behaviorIterator.hasNext()) {
				Context.codeOutput().print(" else ");
			}
		}
		if (aBehaviorSet.size() > 1) {
			Context.codeOutput().println("");
			Context.codeOutput().indent();
		}
	}

	public void rulePushedEventOperation() {
		// description
		// This rule emits the push-event operation signature and method implementation
		// code for this Event
		// including the operation signature
		//
		// Event.#PUSHED_EVENT_OPERATION -->
		// 'def ' +
		// if this event has a non-blank PIM return run time type
		// then PIM return run time type + ' ' +
		// otherwise 'void ' +
		// event name + #OPERATION_FORMAL_PARAMETERS + ' {'
		// #OPERATION_CONTRACT_REQUIRES_CLAUSE
		// #OPERATION_CONTRACT_GUARANTEES_CLAUSE
		// if( class invariants are turned on ) { #CLASS_INVARIANTS }
		// if( assertions are turned on ) { #ENTRY_ASSERTIONS }
		// if this event has a non-blank PIM return run time type
		// then PIM return run time type + ' aResult' +
		// if this event has a non blank PIM return data type initial value
		// then ' = ' + PIM return data type initial value +
		// ';'
		// #EVENT_METHOD_BODY
		// if( assertions are turned on ) { #EXIT_ASSERTIONS }
		// if( class invariants are turned on ) { #CLASS_INVARIANTS }
		// if this event has a non-blank PIM return run time type
		// then 'return aResult;'
		// '}'
		//
		// requires
		// none
		// guarantees
		// the operation and method code for this Event have been emitted
		Context.setEvent(this);
		Context.codeOutput().indent();
		Context.codeOutput().print("def ");
		// method name here
		Context.codeOutput().print(NameService.asInstanceLevelName(name));
		// has self, because instance method
		Context.codeOutput().print("(self");
		// in case got params so need to add ,
		if (Context.mMClass().eventParameterSet().size() > 0) {
			Context.codeOutput().print(",");
		}
		// parameters
		this.ruleOperationFormalParameters();
		// has self, because instance method
		Context.codeOutput().print(")");
		// return
		Context.codeOutput().print(" -> ");
		if (returnRange != null) {
			Context.codeOutput().print(returnRange.pIMRunTimeType() + " ");
		} else {
			Context.codeOutput().print("None");
		}

		Context.codeOutput().println(" :");
		if (Context.model().isVerbose()) {
			this.ruleOperationContractRequiresClause();
			this.ruleOperationContractGuaranteesClause();
		}
		Context.codeOutput().indent();
		if (Context.model().isAssertionsOn()) {
			this.ruleEntryAssertions();
		}
		if (returnRange != null) {
			Context.codeOutput().print(returnRange.pIMRunTimeType() + " aResult");
			if (returnRange.pIMInitialValue() != null) {
				Context.codeOutput().print(" = " + returnRange.pIMInitialValue());
			} else {
				System.out.println();
				System.out.println("****** ERROR in Event, no return data type initial value specified! ******");
				System.out.println("Class = " + Context.mMClass().name() + ", event = " + name);
				System.out.println();
				Context.codeOutput().incrementErrorCount();
			}
			Context.codeOutput().println(";");
		}
		this.ruleEventMethodBody();
		if (Context.model().isAssertionsOn()) {
			this.ruleExitAssertions();
		}
		if (returnRange != null) {
			Context.codeOutput().println("return aResult;");
		}
		Context.codeOutput().indentLess();
		Context.codeOutput().indent();

		Context.codeOutput().println("");
		Context.clearEvent();
	}

	public void ruleOperationFormalParameters() {
		// description
		// This rule emits the formal parameter list for an operation signature
		// this is used for both the constructor and push-event operations
		//
		// Event.#OPERATION_FORMAL_PARAMETERS -->
		// '( ' +
		// foreach unique parameter in all actions on all transitions for this event
		// #FORMAL_PARAMETER + ', ' (except after last one)
		// ' )'
		//
		// requires
		// Context.class <> null
		// Context.event == this
		// guarantees
		// the formal parameters list, including parens for this operation has been
		// emitted
		// Implementation notes
		// needs to be done with Iterator to avoid trailing comma...
		ArrayList<Parameter> parameterSet = Context.mMClass().eventParameterSet();
		Iterator<Parameter> parameterIterator = parameterSet.iterator();
		while (parameterIterator.hasNext()) {
			Parameter aParameter = parameterIterator.next();
			Context.codeOutput().print(" ");
			aParameter.ruleFormalParameter();
			if (parameterIterator.hasNext()) {
				Context.codeOutput().print(",");
			} else {
				Context.codeOutput().print(" ");
			}
		}
	}

	public void ruleOperationContractRequiresClause() {
		// description
		// This rule emits the contract requires block for an event operation.
		// this is used for both the constructor and push-event operations
		//
		// Event.#OPERATION_CONTRACT_REQUIRES_CLAUSE -->
		// '// requires' +
		// if( any action--on all transitions for this event--have any requires clauses
		// )
		// foreach unique requires condition in all actions on all transitions for this
		// event
		// '// ' + that condition's expression
		// otherwise '// none'
		//
		// NOTE: there's a potential logic issue with doing contracts this way. Say
		// there is some event e1.
		// assume states s1 and s2 both have outgoing transitions on e1. The s1-e1
		// transition has
		// action a1 which requires p. the s2-e1 transition has action a2 which requires
		// !p. now
		// operation e1() actually requires both p and !p, which can never be true. to
		// be 100%
		// theoretically correct, operation e1() contract should probably look more
		// like:
		//
		// public void e1() {
		// // when state == s1: requires p --> guarantees ...
		// // when state == s2: requires !p --> guarantees ...
		//
		// requires
		// Context.class <> null
		// Context.event == this
		// guarantees
		// operation contract requires clause has been emitted
		Context.codeOutput().indent();
		Context.codeOutput().indentMore();
		Context.codeOutput().println("# requires");
		ArrayList<Condition> requiresConditionSet = Context.mMClass().eventRequiresSet();
		if (!requiresConditionSet.isEmpty()) {
			for (Condition aCondition : requiresConditionSet) {
				Context.codeOutput().indent();
				Context.codeOutput().println("#    " + aCondition.expression());
			}
		} else {
			Context.codeOutput().indent();
			Context.codeOutput().println("#    none");
		}
	}

	public void ruleOperationContractGuaranteesClause() {
		// description
		// This rule emits the guarantees clause for an event operation...
		// this is used for both the constructor and push-event operations
		//
		// Event.#OPERATION_CONTRACT_GUARANTEES_CLAUSE -->
		// '// guarantees' +
		// foreach state-event-behavior on this event {
		// #BEHAVIOR_GUARANTEES_CLAUSE
		// }
		//
		// NOTE: minor issue in current version is that it will put '-- or --' between
		// any pair of
		// state-event behaviors, but Ignore types don't output anything. Maybe solution
		// is
		// to only be explicit about transitions and Impossible, default to Ignore
		// (implicit)
		//
		// requires
		// Context.class <> null
		// Context.event == this
		// guarantees
		// operation contract requires clause has been emitted
		// Implementation notes
		// none
		Context.codeOutput().indent();
		Context.codeOutput().println("# guarantees");
		Iterator<StateEventBehavior> behaviorIterator = Context.mMClass().eventBehaviorSet().iterator();
		while (behaviorIterator.hasNext()) {
			StateEventBehavior aBehavior = behaviorIterator.next();
			aBehavior.ruleContractGuaranteesClause();
			if (behaviorIterator.hasNext()) {
				Context.codeOutput().print(", -- or --");
			}
			Context.codeOutput().println("");
		}
	}

	public void ruleEntryAssertions() {
		// description
		// This rule emits the entry assertions for an event operation...
		// this is used for both the constructor and push-event operations
		//
		// Event.#ENTRY_ASSERTIONS -->
		// foreach aParameter
		// if aParameter is assertable
		// then aParameter.#ASSERT_PARAMETER +
		// if this Event has any requires clauses
		// then foreach aRequiresClause on this Event
		// aRequiresClause.#ASSERT_CONDITION +
		//
		// requires
		// Context.mMClass <> null
		// Context.event == this
		// guarantees
		// all entry assertions, if any, for this event have been emitted
		ArrayList<Parameter> parameterSet = Context.mMClass().eventParameterSet();
		for (Parameter aParameter : parameterSet) {
			if (aParameter.isAssertable()) {
				aParameter.ruleAssertParameter();
			}
		}
		for (Condition aCondition : Context.mMClass().eventRequiresSet()) {
			aCondition.ruleAssertCondition();
		}
	}

	public void ruleEventMethodBody() {
		// description
		// This rule emits the method body for an event operation...
		//
		// Event.#EVENT_METHOD_BODY -->
		// foreach state-event-behavior for this event
		// #PUSH_EVENT_COMPILE
		//
		// requires
		// Context.class <> null
		// Context.event == this
		// guarantees
		// method body code has been emitted
		ArrayList<StateEventBehavior> aBehaviorSet = Context.mMClass().eventBehaviorSet();
		Iterator<StateEventBehavior> behaviorIterator = aBehaviorSet.iterator();
		while (behaviorIterator.hasNext()) {
			StateEventBehavior aBehavior = behaviorIterator.next();
			aBehavior.rulePushEventBehavior();
			if (behaviorIterator.hasNext()) {
				Context.codeOutput().print(" else ");
			}
		}
		Context.codeOutput().println("");
	}

	public void ruleExitAssertions() {
		// description
		// This rule emits the exit assertions for an event operation...
		// this is used for both the constructor and push-event operations
		//
		// Event.#EXIT_ASSERTIONS -->
		// if this Event has any guarantees clauses
		// then foreach aGuaranteesClause on this Event
		// aGuaranteesClause.#ASSERT_CONDITION +
		//
		// requires
		// Context.mMClass <> null
		// Context.event == this
		// guarantees
		// all exit assertions, if any, for this event have been emitted
		for (Condition aCondition : Context.mMClass().eventGuaranteesSet()) {
			aCondition.ruleAssertCondition();
		}
	}

}
