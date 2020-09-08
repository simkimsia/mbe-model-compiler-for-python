import java.util.ArrayList;
import java.util.Iterator;

public class StateEventBehavior {
	// General description
	// Model compiler
	// This class implements State Event Behavior from the meta-model
	// Invariants
	// none
	// Implementation notes
	// Collapses all three meta-model subtypes into the base class to allow
	// dynamic re-clasification (i.e., subtype migration). This would be much
	// more complex and require State pattern to do it with real inheritance
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

	// enumeration of the sub-types of StateEventBehavior
	private enum BehaviorType {
		TRANSITION, IGNORE, IMPOSSIBLE
	};

	public static String tagTransitionStart = "<transition>";
	public static String tagGuard = "<guard>";
	public static String tagTransitionEnd = "</transition>";
	public static String tagIgnoreStart = "<ignore>";
	public static String tagIgnoreEnd = "</ignore>";
	public static String tagImpossibleStart = "<impossible>";
	public static String tagImpossibleEnd = "</impossible>";

	// Static (class) variables

	// none

	// Static (class) methods

	public static StateEventBehavior parseTransition() {
		// requires
		// Input model stream is open and ready to read first line after "<transition>"
		// guarantees
		// returns a populated instance of state event behavior (or null, if
		// nothing/error in model file)
		// and input model string is right after "</transition>"
		State fromState = Context.mMClass().stateNamed(JALInput.nextLine());
		Event onEvent = Context.mMClass().eventNamed(JALInput.nextLine());
		StateEventBehavior newTransition = new StateEventBehavior(fromState, onEvent);
		State toState = Context.mMClass().stateNamed(JALInput.nextLine());
		newTransition.becomeTransition(toState);
		String line = JALInput.nextLine();

		// now look for modifiers: guards, actions, ...
		while (!line.contains(StateEventBehavior.tagTransitionEnd)) {
			if (line.contains(StateEventBehavior.tagGuard)) {
				newTransition.setGuard(JALInput.nextLine());
				newTransition.setPIMGuard(JALInput.nextLine());
			}
			if (line.contains(Action.tagTransitionAction)) {
				Action anAction = Context.mMClass().actionNamed(JALInput.nextLine());
				newTransition.addAction(anAction);
			}
			line = JALInput.nextLine();
		}
		return newTransition;
	}

	public static StateEventBehavior parseIgnore() {
		// requires
		// Input model stream is open and ready to read first line after "<ignore>"
		// guarantees
		// returns a populated instance of state event behavior (or null, if
		// nothing/error in model file)
		// and input model string is right after "</ignore>"
		State fromState = Context.mMClass().stateNamed(JALInput.nextLine());
		Event onEvent = Context.mMClass().eventNamed(JALInput.nextLine());
		StateEventBehavior newIgnore = new StateEventBehavior(fromState, onEvent);
		newIgnore.becomeIgnore();
		String line = JALInput.nextLine();
		// now look for modifier: guard
		while (!line.contains(StateEventBehavior.tagIgnoreEnd)) {
			if (line.contains(StateEventBehavior.tagGuard)) {
				newIgnore.setGuard(JALInput.nextLine());
				newIgnore.setPIMGuard(JALInput.nextLine());
			}
			line = JALInput.nextLine();
		}
		return newIgnore;
	}

	public static StateEventBehavior parseImpossible() {
		// requires
		// Input model stream is open and ready to read first line after "<impossible>"
		// guarantees
		// returns a populated instance of state event behavior (or null, if
		// nothing/error in model file)
		// and input model string is right after "</impossible>"
		State fromState = Context.mMClass().stateNamed(JALInput.nextLine());
		Event onEvent = Context.mMClass().eventNamed(JALInput.nextLine());
		StateEventBehavior newImpossible = new StateEventBehavior(fromState, onEvent);
		newImpossible.becomeImpossible();
		String line = JALInput.nextLine();
		// now look for modifier: guard
		while (!line.contains(StateEventBehavior.tagImpossibleEnd)) {
			if (line.contains(StateEventBehavior.tagGuard)) {
				newImpossible.setGuard(JALInput.nextLine());
				newImpossible.setPIMGuard(JALInput.nextLine());
			}
			line = JALInput.nextLine();
		}
		return newImpossible;
	}

	// Instance variables

	// Meta-model instance variables
	private State fromState;
	private Event onEvent;
	private String guard;
	private BehaviorType type;

	// When behavior type = Transition
	private State toState;
	private ArrayList<Action> transitionActionSet;

	// PIM Overlay instance variables
	private String aPIMguard;

	// Model compiler instance variables
	// none

	// Constructor(s)

	public StateEventBehavior(State sourceState, Event triggeringEvent) {
		// description
		// default constructor
		// requires
		// sourceState <> null
		// triggeringEvent <> null
		// guarantees
		// a StateEventBehavior has been created for that source state and that event
		// the instance defaults to subclass "Ignore" and must be changed (below) as
		// needed
		fromState = sourceState;
		onEvent = triggeringEvent;
		guard = "true";
		type = BehaviorType.IGNORE;
		toState = null;
		transitionActionSet = new ArrayList<Action>();
		aPIMguard = "";
	}

	// Accessors

	public State fromState() {
		// description
		// a meta-model operation that returns a reference to the from (i.e., source)
		// state for this StateEventBehavior
		// requires
		// none
		// guarantees
		// returns the fromState as an object reference
		return fromState;
	}

	public String fromStateName() {
		// description
		// a meta-model operation that returns the string name of the from (i.e.,
		// source) state
		// requires
		// none
		// guarantees
		// returns the from state name as a string
		return fromState.name();
	}

	public Event onEvent() {
		// description
		// a meta-model operation that returns a reference to the event for this
		// StateEventBehavior
		// requires
		// none
		// guarantees
		// returns the onEvent as an object reference
		return onEvent;
	}

	public String onEventName() {
		// description
		// a meta-model operation that returns the string name of the event
		// requires
		// none
		// guarantees
		// returns the on event name as a string
		return onEvent.name();
	}

	public String guard() {
		// description
		// a meta-model operation that returns the guard text associated with this
		// StateEventBehavior
		// requires
		// none
		// guarantees
		// returns the guard as a string
		return guard;
	}

	public String pIMGuard() {
		// description
		// a meta-model operation that returns the PIM overlay guard text associated
		// with this StateEventBehavior
		// requires
		// none
		// guarantees
		// returns the PIM guard as a string
		return aPIMguard;
	}

	public boolean isTransition() {
		// description
		// a meta-model operation that returns whether this is a Transition subtype of
		// StateEventBehavior
		// requires
		// none
		// guarantees
		// returns true only when this is a Transition subtype of StateEventBehavior
		return type == BehaviorType.TRANSITION;
	}

	public boolean isIgnore() {
		// description
		// a meta-model operation that returns whether this is an Ignore subtype of
		// StateEventBehavior
		// requires
		// none
		// guarantees
		// returns true only when this is an Ignore subtype of StateEventBehavior
		return type == BehaviorType.IGNORE;
	}

	public boolean isImpossible() {
		// description
		// a meta-model operation that returns whether this is an Impossible subtype of
		// StateEventBehavior
		// requires
		// none
		// guarantees
		// returns true only when this is an Impossible subtype of StateEventBehavior
		return type == BehaviorType.IMPOSSIBLE;
	}

	public State toState() {
		// description
		// a meta-model operation that returns a reference to the target (to, or
		// destination) state of a Transition subtype
		// requires
		// none
		// guarantees
		// if this is a Transition subtype
		// then returns a reference to the target state
		// otherwise returns null
		if (this.isTransition()) {
			return toState;
		} else {
			return null;
		}
	}

	public String toStateName() {
		// description
		// a meta-model operation that returns the name of the target (to, or
		// destination) state of a Transition subtype
		// requires
		// none
		// guarantees
		// if this is a Transition subtype
		// then returns the name of the target state as a string
		// otherwise returns a null string
		if (this.isTransition()) {
			return toState.name();
		} else {
			return null;
		}
	}

	public ArrayList<Action> transitionActionSet() {
		// description
		// a meta-model operation that returns the set of Transaction Actions associated
		// with this Transition
		// requires
		// null
		// guarantees
		// if this is a Transition subtype
		// then returns an ArrayList of the TransitionActions on this Transition
		// otherwise returns an empty ArrayList
		if (this.isTransition()) {
			return transitionActionSet;
		} else {
			return new ArrayList<Action>();
		}
	}

	public ArrayList<Condition> transitionActionsRequiresSet() {
		// description
		// a meta-model operation that returns the union of all requires clauses on all
		// Transaction Actions for this Transition
		// requires
		// null
		// guarantees
		// if this is a Transition subtype
		// then returns an ArrayList of the TransitionActions on this Transition
		// otherwise returns an empty ArrayList
		ArrayList<Condition> aTransitionActionsRequiresSet = new ArrayList<Condition>();
		if (this.isTransition()) {
			for (Action anAction : transitionActionSet) {
				for (Condition aRequiresCondition : anAction.requiresSet()) {
					if (!aTransitionActionsRequiresSet.contains(aRequiresCondition)) {
						aTransitionActionsRequiresSet.add(aRequiresCondition);
					}
				}
			}
		}
		return aTransitionActionsRequiresSet;
	}

	public ArrayList<Condition> transitionActionsGuaranteesSet() {
		// description
		// a meta-model operation that returns the union of all guarantees clauses on
		// all Transaction Actions for this Transition
		// requires
		// null
		// guarantees
		// if this is a Transition subtype
		// then returns an ArrayList of the TransitionActions on this Transition
		// otherwise returns an empty ArrayList
		ArrayList<Condition> aTransitionActionsGuaranteesSet = new ArrayList<Condition>();
		if (this.isTransition()) {
			for (Action anAction : transitionActionSet) {
				for (Condition aGuaranteesCondition : anAction.guaranteesSet()) {
					if (!aTransitionActionsGuaranteesSet.contains(aGuaranteesCondition)) {
						aTransitionActionsGuaranteesSet.add(aGuaranteesCondition);
					}
				}
			}
		}
		return aTransitionActionsGuaranteesSet;
	}

	public ArrayList<Parameter> transitionParameterSet() {
		// description
		// a meta-model operation that returns the union-ed set of parameters associated
		// with this Transition
		// requires
		// none
		// guarantees
		// the parameter set for this Transition has been returned
		ArrayList<Parameter> transitionParameterSet = new ArrayList<Parameter>();
		transitionParameterSet.clear();
		if (this.isTransition()) {
			Iterator<Action> actionIterator = transitionActionSet.iterator();
			while (actionIterator.hasNext()) {
				Action anAction = actionIterator.next();
				ArrayList<Parameter> actionParameterSet = anAction.parameterSet();
				Iterator<Parameter> actionParameterIterator = actionParameterSet.iterator();
				while (actionParameterIterator.hasNext()) {
					Parameter anActionParameter = actionParameterIterator.next();
					if (!transitionParameterSet.contains(anActionParameter)) {
						transitionParameterSet.add(anActionParameter);
					}
				}
			}
			return transitionParameterSet;
		} else {
			return new ArrayList<Parameter>();
		}
	}

	// Modifiers

	public void addAction(Action actionToAdd) {
		// description
		// a meta-model operation that adds another action to the set of transition
		// actions
		// requires
		// actionToAdd <> null
		// guarantees
		// if this is a Transition subtype
		// then adds this Action to this Transition
		// otherwise nothing happened
		if (this.isTransition()) {
			transitionActionSet.add(actionToAdd);
		}
	}

	public void setGuard(String guardCondition) {
		// description
		// a meta-model operation that sets the guard text on a state event behavior
		// requires
		// guardCondition <> null
		// guarantees
		// over-writes the guard condition text for this state event behavior
		// otherwise nothing happened
		guard = guardCondition;
	}

	public void setPIMGuard(String aPIMguardCondition) {
		// description
		// a PIM Overlay operation that sets the PIM guard text on a state event
		// behavior
		// requires
		// guardCondition <> null
		// guarantees
		// over-writes the PIM guard condition text for this state event behavior
		aPIMguard = aPIMguardCondition;
	}

	public void becomeTransition(State destinationState) {
		// description
		// a meta-model operation that turns this StateEventBehavior into a Transition
		// subtype
		// requires
		// destinationState <> null
		// guarantees
		// if this is not a Transition subtype
		// then turns it into a Transition with the given destinationState,
		// guard = "true", and Transition Action set empty
		// otherwise does nothing
		if (!this.isTransition()) {
			toState = destinationState;
			guard = "true";
			aPIMguard = "";
			transitionActionSet.clear();
			type = BehaviorType.TRANSITION;
		}
	}

	public void becomeIgnore() {
		// description
		// a meta-model operation that turns this StateEventBehavior into an Ignore
		// subtype
		// requires
		// none
		// guarantees
		// if this is not an Ignore subtype
		// then turns it into an Ignore with destinationState = null,
		// guard = "none", and Transition Action set empty
		// otherwise does nothing
		if (!this.isIgnore()) {
			toState = null;
			guard = "true";
			aPIMguard = "";
			transitionActionSet.clear();
			type = BehaviorType.IGNORE;
		}
	}

	public void becomeImpossible() {
		// description
		// a meta-model operation that turns this StateEventBehavior into an Impossible
		// subtype
		// requires
		// none
		// guarantees
		// if this is not an Impossible subtype
		// then turns it into an Impossible with destinationState = null,
		// guard = "none", and Transition Action set empty
		// otherwise does nothing
		if (!this.isImpossible()) {
			toState = null;
			guard = "true";
			aPIMguard = "";
			transitionActionSet.clear();
			type = BehaviorType.IMPOSSIBLE;
		}
	}

	// Private methods

	// none

	// ******MODEL COMPILER******
	// Everything from here down is specific to the model compiler itself

	public void ruleConstructorBehavior() {
		// description
		// This rule compiles a single State-Event Behavior **for a constructor**
		// it only generates code for S-E-B subtype Transition and Impossible
		// we know that fromState() == default not exists state
		// and onEvent() == default new event (<<new>>)
		// at this point we only need to deal with the remainder: guard, action(s), end
		// state
		// vs. being an Impossible type behavior
		//
		// StateEventBehavior.#CONSTRUCTOR_BEHAVIOR -->
		// if( this is not an ignore type behavior ) {
		// if( there is a guard ) {
		// 'if( ' + the PIM Overlay guard code + ' ) {"
		// if( this is a transition type behavior ) {
		// #TRANSITION_ACTIONS_LIST
		// 'state = ' + to state
		// } else {
		// 'throw new IllegalStateException();'
		// }
		// if( there is a guard ) {
		// '}'
		// }
		//
		// requires
		// none
		// guarantees
		// proper if-then code is emitted for the given **constructor** State-Event
		// Behavior
		// subtype Ignore generates no code (because they can be safely ignored)
		// subtype Impossible generates a fatal exception
		// subtype Transition generates calls to the (private) transition actions
		// Guards are included in the if-then logic when necessary
		if (!this.isIgnore()) {
			if (this.guard() != "true") {
				Context.codeOutput().println("if( " + NameService.formatActionStmt(this.pIMGuard()) + " ) {");
				Context.codeOutput().indentMore();
				Context.codeOutput().indent();
			}
			if (this.isTransition()) {
				this.ruleTransitionActionsList();
				Context.codeOutput().println("self._state = " + toState.nameAsQualifiedENUM());
			} else {
				if (this.isImpossible()) {
					Context.codeOutput().println(
							"throw new IllegalStateException( \"Event is declared impossible in this state\" );");
				}
			}
			if (this.guard() != "true") {
				Context.codeOutput().indentLess();
				Context.codeOutput().indent();
			} else {
				Context.codeOutput().indent();
			}
		}
	}

	public void rulePushEventBehavior() {
		// description
		// This rule compiles a single State-Event Behavior, outside of a constructor
		// we should be able to count on fromState() != default not exists state
		// and onEvent() != default new event (<<new>>)
		// at this point we only need to deal with the remainder: guard, action(s), end
		// state
		// vs. it being an Impossible type behavior
		// Handles Transitions, Ignores, and Impossibles as appropriate
		//
		// StateEventBehavior.#PUSH_EVENT_BEHAVIOR -->
		// if( this is not an ignore type behavior ) {
		// 'if( state == ' + fromState +
		// #OPTIONAL_GUARD +
		// ' ) {" +
		// if( this is a transition type behavior ) {
		// #TRANSITION_ACTIONS_LIST +
		// if( fromState != to state ) {
		// 'state = ' + to state +
		// if( to state = NOTEXISTS ) {
		// class name + 'Set.remove( this );'
		// }
		// } else {
		// if( this is an impossible type behavior ) {
		// 'throw new IllegalStateException();'
		// }
		// '}'
		//
		// requires
		// none
		// guarantees
		// proper If-Then code is emitted for the given State-Event Behavior
		// subtype Ignore generates no code (because they can be safely ignored)
		// subtype Impossible generates a fatal exception
		// subtype Transition generates calls to the (private) transition actions
		// Guards are included in the if-then logic when necessary
		if (!this.isIgnore()) {
			Context.codeOutput().print("if self._state == " + fromState.nameAsQualifiedENUM());
			this.ruleOptionalGuard();
			Context.codeOutput().println(":");
			if (this.isTransition()) {
				Context.codeOutput().indentMore();
				Context.codeOutput().indent();
				this.ruleTransitionActionsList();
				if (this.fromState() != this.toState()) {
					Context.codeOutput().println("self._state = " + toState.nameAsQualifiedENUM());
					if (this.toState() == Context.mMClass().stateNamed(State.defaultNotExistsStateName)) {
						Context.codeOutput().indent();
						// instead of using the class method to add to the set i simply add to the class
						// attribute
						// Context.codeOutput().println(NameService.asInstanceLevelName(Context.mMClass().name())
						// + "Set.remove( self )");
						// this should be ClassName.ClassNameSet.remove(self)
						Context.codeOutput().println(NameService.asClassLevelName(Context.mMClass().name()) + "."
								+ NameService.asClassLevelName(Context.mMClass().name()) + "Set.remove( self )");

					}
				}
				Context.codeOutput().indentLess();
				Context.codeOutput().indent();
			} else {
				if (this.isImpossible()) {
					Context.codeOutput().indentMore();
					Context.codeOutput().indent();
					Context.codeOutput().println(
							"throw new IllegalStateException( \"Event is declared impossible in this state\" );");
					Context.codeOutput().indentLess();
					Context.codeOutput().indent();
					Context.codeOutput().print("}");
				}
			}
		}
	}

	public void ruleContractGuaranteesClause() {
		// description
		// This rule outputs a guarantees clause for this behavior (assuming it's not an
		// ignore type)
		//
		// StateEventBehavior.#BEHAVIOR_GUARANTEES_CLAUSE -->
		// if this behavior is not Ignore type {
		// '// ' +
		// if from state is not default not exists state {
		// 'state was ' + from state name +
		// if guard is not "true" {
		// ' and ' +
		// }
		// }
		// if guard is not "true" {
		// the guard +
		// }
		// ' --> ' +
		// if this behavior is Transition type {
		// foreach guarantees condition {
		// that condition + ', ' +
		// if( from state is not same as to state ) {
		// if( any conditions were output ) {
		// ' and ' +
		// }
		// 'to state == ' + to state name
		// }
		// } else {
		// ' fatal exception thrown'
		// }
		//
		// requires
		// context.class <> null
		// guarantees
		// if not an Ignore type behavior
		// if a Transition type behavior
		// then transition guarantees clause was emitted
		// otherwise fatal exception clause was emitted
		if (!this.isIgnore()) {
			Context.codeOutput().indent();
			Context.codeOutput().print("#   ");
			if (!fromState.name().equals(State.defaultNotExistsStateName)) {
				Context.codeOutput().print("state was " + fromState.name());
				if (!this.guard().equals("true")) {
					Context.codeOutput().print(" and ");
				}
			}
			if (!this.guard().equals("true")) {
				Context.codeOutput().print(this.guard());
			}
			Context.codeOutput().print(" --> ");
			if (this.isTransition()) {
				boolean outputAnyCondition = false;
				ArrayList<Condition> guaranteesConditionSet = this.transitionActionsGuaranteesSet();
				Iterator<Condition> conditionIterator = guaranteesConditionSet.iterator();
				while (conditionIterator.hasNext()) {
					Condition aCondition = conditionIterator.next();
					Context.codeOutput().print(aCondition.expression());
					outputAnyCondition = true;
					if (conditionIterator.hasNext()) {
						Context.codeOutput().print(", ");
					} else {
						Context.codeOutput().print(" ");
					}
				}
				if (fromState != toState) {
					if (outputAnyCondition) {
						Context.codeOutput().print("and ");
					}
					Context.codeOutput().print("state == " + toState.name());
				}
			} else {
				Context.codeOutput().print("fatal exception thrown ");
			}
		}
	}

	public void ruleOptionalGuard() {
		// description
		// This rule outputs a guard condition into the if( state ==, if one is needed
		//
		// StateEventBehavior.#OPTIONAL_GUARD -->
		// if this behavior has a guard
		// ' && ' + guard expression
		//
		// requires
		// none???
		// guarantees
		// if a guard is needed
		// then it's been emitted
		// otherwise nothing emitted
		if (this.guard() != "true") {
			Context.codeOutput().print(" && " + NameService.formatActionStmt(this.pIMGuard()));
		}
	}

	public void ruleTransitionActionsList() {
		// description
		// This rule compiles the set of calls to the (private) actions for a valid
		// Transition
		//
		// StateEventBehavior.#TRANSITION_ACTIONS_LIST -->
		// foreach anAction on this transition
		// anAction.#CALL_ACTION
		//
		// requires
		// none
		// guarantees
		// the set of calls to (private) Actions on this Transition have been emitted
		for (Action anAction : transitionActionSet) {
			anAction.ruleCallAction();
			Context.codeOutput().indent();
		}
	}

}
