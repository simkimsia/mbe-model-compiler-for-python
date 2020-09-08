import java.util.ArrayList;

public class State {
	// General description
	// Model compiler
	// This class implements class State in the Meta-Model
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

	public static final String defaultStateAttributeName = "state";
	public static final String defaultNotExistsStateName = "Doesn't exist";
	public static final String defaultExistsStateName = "Exists";
	public static String tagStateStart = "<state>";
	public static String tagStateEnd = "</state>";

	// Static (class) variables

	// none

	// Static (class) methods

	public static State parseState() {
		// requires
		// Input model stream is open and ready to read first line after "<state>"
		// guarantees
		// returns a populated instance of state (or null, if nothing/error in model
		// file)
		// and input model string is right after "</state>"
		State newState = new State(JALInput.nextLine());
		Context.setState(newState);
		// check if next line is state description
		String line = JALInput.nextLine();
		if (line.contains(NameService.tagDescriptionStart)) {
			newState.setDescription(JALInput.nextLine());
			line = JALInput.nextLine();
		}
		// now look for modifiers: state actions will go here eventually, ...
		while (!line.contains(State.tagStateEnd)) {
			if (line.contains(Action.tagStateAction)) {
				Action anAction = Context.mMClass().actionNamed(JALInput.nextLine());
				newState.addStateAction(anAction);
			}
			line = JALInput.nextLine();
		}
		Context.clearState();
		return newState;
	}

	// Instance variables

	// Meta-model instance variables
	private String name;
	private String description;
	private ArrayList<Action> stateActionSet;

	// PIM Overlay instance variables
	// none

	// Model compiler instance variabls
	// none

	// Constructor(s)

	public State(String aName) {
		// description
		// default constructor
		// requires
		// aState <> null and is unique among all States in this class
		// guarantees
		// an instance of State has been created and initialized
		name = aName;
		description = "none";
		stateActionSet = new ArrayList<Action>();
		stateActionSet.clear();
	}

	// Accessors

	public String name() {
		// description
		// a meta-model operation that returns the state name as a string
		// requires
		// nothing
		// guarantees
		// the state's name is been returned
		return name;
	}

	public String description() {
		// description
		// a meta-model operation that returns the state description as a string
		// requires
		// nothing
		// guarantees
		// the state's description is been returned
		return description;
	}

	public String nameAsENUM() {
		// description
		// a compiler operation that formats the state's given name to be usable as a
		// Java ENUM
		// removes non-alpha and non-numeric characters
		// makes it all upper case
		// requires
		// none
		// guarantees
		// returns the formatted name
		return NameService.asUPPERCASE(name);
	}

	public String nameAsQualifiedENUM() {
		// description
		// a compiler operation that qualifies the state name with 'ClassName_states.'
		// requires
		// Context.mMClass <> null
		// guarantees
		// returns the qualified state name
		return NameService.asClassLevelName(Context.mMClass().name()) + "."
				+ Context.mMClass().stateAttributeEnumRTType() + "." + this.nameAsENUM();
	}

	public ArrayList<Action> allStateActions() {
		// description
		// a meta-model operation that returns the set of state actions
		// requires
		// nothing
		// guarantees
		// the state's action set is been returned
		return stateActionSet;
	}

	// Modifiers

	public void resetName(String aName) {
		// description
		// a meta-model operation that overwrites the existing name for the state,
		// as long as it's not the "Not Exists" state, that name can't change
		// requires
		// aName <> null, or it could cause problems later
		// TBD should also involve some kind of uniqueness check among the class
		// guarantees
		// state name has been reset to aName
		if (name != defaultNotExistsStateName) {
			name = aName;
		}
	}

	public void setDescription(String aDescription) {
		// description
		// a meta-model operation that overwrites the existing description for the state
		// requires
		// none
		// guarantees
		// state description has been reset to aDescription
		description = aDescription;
	}

	public void addStateAction(Action stateActionToAdd) {
		// description
		// a meta-model operation that adds the state action to this state
		// requires
		// stateActionToAdd <> null and is unique among all Actions in this class
		// guarantees
		// stateActionToAdd has been added to this state's actionSet
		stateActionSet.add(stateActionToAdd);
	}

	// Private methods

	// none

	// ******MODEL COMPILER******
	// Everything from here down is specific to the model compiler itself

	public void ruleCallStateTimeSlice() {
		// description
		// if there are any actions in aState, this rule emits the timeSlice() call
		//
		// State.#CALL_STATE_TIME_SLICE -->
		// if( there are any state actions in this state )
		// 'if( state == ' + qualified state name + ' ) {' +
		// foreach action in this state
		// #CALL_ACTION
		// '}'
		//
		// requires
		// none
		// guarantees
		// a call to (private) actions implementing aState's behavior have been emitted
		Context.setState(this);
		if (!stateActionSet.isEmpty()) {
			Context.codeOutput().indent();
			Context.codeOutput().println("if( state == " + this.nameAsQualifiedENUM() + " ) {");
			Context.codeOutput().indentMore();
			for (Action anAction : stateActionSet) {
				anAction.ruleCallAction();
			}
			Context.codeOutput().indentLess();
			Context.codeOutput().indent();

		}
		Context.clearState();
	}

}
