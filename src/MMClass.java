import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MMClass {
	// General description
	// Model compiler
	// This class implements class Class in the Meta-Model
	// Invariants
	// none
	// Implementation notes
	// This class had to be named MMClass because Java won't allow a class named
	// Class
	// the name stands for "Meta-Model Class"
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

	public static final String tagClassStart = "<class>";
	public static final String tagClassEnd = "</class>";
	public static final String tagPIMImportStart = "<pimimport>";
	public static final String tagPIMImportEnd = "</pimimport>";
	public static final String tagPIMConstantsStart = "<pimconstant>";
	public static final String tagPIMConstantsEnd = "</pimconstant>";

	// Static (class) variables

	private static ArrayList<MMClass> mMClassSet = new ArrayList<MMClass>();

	// Static (class) methods

	public static MMClass parseClass() {
		// requires
		// the model input stream is open and ready to read at the first line after a
		// "<class>" tag
		// guarantees
		// returns a populated instance of MMClass (or empty instance, if nothing or
		// there is an error in the stream)
		// and the input model stream is right after a "</class>" tag
		MMClass newClass = new MMClass(JALInput.nextLine());
		Context.setMMClass(newClass);

		// check if the next line is a class description
		String line = JALInput.nextLine();
		if (line.contains(NameService.tagDescriptionStart)) {
			newClass.setDescription(JALInput.nextLine());
			line = JALInput.nextLine();
		}
		while (!line.contains(MMClass.tagClassEnd)) {
			if (line.contains(MMClass.tagPIMImportStart)) {
				newClass.clearPIMImports();
				line = JALInput.nextLine();
				while (!line.contains(MMClass.tagPIMImportEnd)) {
					newClass.addPIMImport(line);
					line = JALInput.nextLine();
				}
			}
			if (line.contains(MMClass.tagPIMConstantsStart)) {
				newClass.clearPIMConstants();
				line = JALInput.nextLine();
				while (!line.contains(MMClass.tagPIMConstantsEnd)) {
					newClass.addPIMConstant(line);
					line = JALInput.nextLine();
				}
			}
			if (line.contains(Attribute.tagAttributeStart)) {
				newClass.addAttribute(Attribute.parseAttribute());
			}
			if (line.contains(Action.tagActionStart)) {
				newClass.addAction(Action.parseAction());
			}
			if (line.contains(DerivedAttribute.tagDerivedAttributeStart)) {
				newClass.addDerivedAttribute(DerivedAttribute.parseDerivedAttribute());
			}
			if (line.contains(State.tagStateStart)) {
				newClass.addState(State.parseState());
			}
			if (line.contains(Event.tagEventStart)) {
				newClass.addEvent(Event.parseEvent());
			}
			if (line.contains(StateEventBehavior.tagTransitionStart)) {
				newClass.addStateEventBehavior(StateEventBehavior.parseTransition());
			}
			if (line.contains(StateEventBehavior.tagIgnoreStart)) {
				newClass.addStateEventBehavior(StateEventBehavior.parseIgnore());
			}
			if (line.contains(StateEventBehavior.tagImpossibleStart)) {
				newClass.addStateEventBehavior(StateEventBehavior.parseImpossible());
			}
			if (line.contains(Action.tagPIMHelperStart)) {
				newClass.clearPIMHelperFunctions();
				line = JALInput.nextLine();
				while (!line.contains(Action.tagPIMHelperEnd)) {
					newClass.addPIMHelperFunctions(line.substring(1));
					line = JALInput.nextLine();
				}
			}
			line = JALInput.nextLine();
		}
		Context.clearMMClass();
		return newClass;
	}

	public static ArrayList<MMClass> allMMClasses() {
		// requires
		// none
		// guarantees
		// returns (reference to) ArrayList<> of all class members
		return mMClassSet;
	}

	public static MMClass classNamed(String aClassName) {
		// description
		// a meta-model operation that tries to find a class named the same as
		// aClassName
		// requires
		// aClassName <> null
		// guarantees
		// (a reference to) the class with the same name as aClassName has been returned
		// returns null if it doesn't find one...
		MMClass theNamedClass = null;
		for (MMClass aClass : mMClassSet) {
			if (aClass.name().equals(aClassName)) {
				theNamedClass = aClass;
			}
		}
		return theNamedClass;
	}

	// Instance variables

	// Meta-model instance variables
	private String name;
	private String description;
	// private boolean isActor; TBD--to be implemented later, if needed
	private ArrayList<Attribute> attributeSet;
	private ArrayList<DerivedAttribute> derivedAttributeSet;
	private ArrayList<Action> actionSet;
	private ArrayList<State> stateSet;
	private ArrayList<Event> eventSet;
	private ArrayList<StateEventBehavior> stateEventBehaviorSet;

	// PIM Overlay instance variables
	private ArrayList<String> pIMImportSet;
	private ArrayList<String> pIMConstantSet;
	private ArrayList<String> pIMHelperCode;

	// Model compiler instance variables
	// none

	// Constructor(s)

	public MMClass(String className) {
		// description
		// default constructor for Meta-Model Classes
		// requires
		// className <> null and is unique among all Meta-Model Classes in this model
		// NOTE: will eventually have to implement a unique-ness check, but haven't done
		// it yet
		// guarantees
		// an instance of Meta-Model Class has been created and initialized as empty
		name = className;
		description = "none";
		attributeSet = new ArrayList<Attribute>();
		derivedAttributeSet = new ArrayList<DerivedAttribute>();
		actionSet = new ArrayList<Action>();
		stateSet = new ArrayList<State>();
		eventSet = new ArrayList<Event>();
		stateEventBehaviorSet = new ArrayList<StateEventBehavior>();
		stateEventBehaviorSet.clear();
		pIMImportSet = new ArrayList<String>();
		pIMConstantSet = new ArrayList<String>();
		pIMHelperCode = new ArrayList<String>();
		mMClassSet.add(this);
	}

	// Accessors

	public String name() {
		// description
		// a meta-model operation that returns the assigned name of a Meta-Model class
		// requires
		// none
		// guarantees
		// assigned name has been returned
		return name;
	}

	public String description() {
		// description
		// a meta-model operation that returns the description of a Meta-Model class
		// requires
		// none
		// guarantees
		// description has been returned
		return description;
	}

	public Attribute attributeNamed(String anAttributeName) {
		// description
		// a meta-model operation that tries to find an attribute named the same as
		// anAttributeName
		// requires
		// an attribute name <> null
		// guarantees
		// (a reference to) the attribute with the same name as anAttributeName has been
		// returned
		// returns null if it doesn't find one with that name
		Attribute theNamedAttribute = null;
		for (Attribute anAttribute : attributeSet) {
			if (anAttribute.name().equals(anAttributeName)) {
				theNamedAttribute = anAttribute;
			}
		}
		return theNamedAttribute;
	}

	public String stateAttributeEnumRTType() {
		// description
		// a model compiler helper operation that tries to return the state attribute
		// enum type on this class
		// requires
		// none
		// guarantees
		// the run time type of the state attribute for this class has been returned
		Attribute stateAttribute = this.attributeNamed(State.defaultStateAttributeName);
		Range stateAttributeRange = stateAttribute.range();
		return stateAttributeRange.pIMRunTimeType();
	}

	public Action actionNamed(String anActionName) {
		// description
		// a meta-model operation that tries to find an action named the same as
		// anActionName
		// requires
		// none
		// guarantees
		// (a reference to) the action with the same name as anActionName has been
		// returned
		// returns null if it doesn't find one with that name
		Action theNamedAction = null;
		for (Action anAction : actionSet) {
			if (anAction.name().equals(anActionName)) {
				theNamedAction = anAction;
			}
		}
		return theNamedAction;
	}

	public State stateNamed(String aStateName) {
		// description
		// a meta-model operation that tries to find a state with the same name as
		// aStateName
		// requires
		// none
		// guarantees
		// (a reference to) the state with the same name as aStateName has been returned
		// returns null if it doesn't find one with that name
		State theNamedState = null;
		for (State aState : stateSet) {
			if (aState.name().equals(aStateName)) {
				theNamedState = aState;
			}
		}
		return theNamedState;
	}

	public Event eventNamed(String anEventName) {
		// description
		// a meta-model operation that tries to find an event named the same as
		// anEventName
		// requires
		// none
		// guarantees
		// (a reference to) the event with the same name as anEventName has been
		// returned
		// returns null if it doesn't find one with that name
		Event theNamedEvent = null;
		for (Event anEvent : eventSet) {
			if (anEvent.name().equals(anEventName)) {
				theNamedEvent = anEvent;
			}
		}
		return theNamedEvent;
	}

	public Action transitionActionNamed(String aTransitionActionName) {
		// description
		// a meta-model operation that exposes the named transition action, if it's
		// there.
		// Keep in mind that someone may have renamed something else so we may not be
		// able to find it
		// requires
		// the named action is defined on some transition, otherwise it can't be found
		// guarantees
		// (a reference to) the transition action with the given name has been returned
		// returns null if it doesn't find one with that name
		Action theNamedAction = null;
		Iterator<StateEventBehavior> stateEventBehaviorIterator = stateEventBehaviorSet.iterator();
		while (stateEventBehaviorIterator.hasNext() && theNamedAction == null) {
			StateEventBehavior aStateEventBehavior = stateEventBehaviorIterator.next();
			for (Action anAction : aStateEventBehavior.transitionActionSet()) {
				if (anAction.name().equals(aTransitionActionName)) {
					theNamedAction = anAction;
				}
			}
		}
		return theNamedAction;
	}

	public ArrayList<Parameter> eventParameterSet() {
		// description
		// a model compiler helper function that returns a list of (input) parameters
		// for
		// all actions triggered by some event
		// requires
		// Context.event() <> null
		// guarantees
		// returns the collection of parameters for the event in Context.event()
		ArrayList<Parameter> aParameterList = new ArrayList<Parameter>();
		for (StateEventBehavior aBehavior : stateEventBehaviorSet) {
			if (aBehavior.onEvent() == Context.event() && aBehavior.isTransition()) {
				for (Parameter aParameter : aBehavior.transitionParameterSet()) {
					if (!aParameterList.contains(aParameter)) {
						aParameterList.add(aParameter);
					}
				}
			}
		}
		return aParameterList;
	}

	public ArrayList<StateEventBehavior> eventBehaviorSet() {
		// description
		// a model compiler helper function that returns a list of
		// StateEventBehaviors triggered by specified event
		// requires
		// Context.event() <> null
		// guarantees
		// returns the collection of StateEventBehaviors for the event in
		// Context.event()
		ArrayList<StateEventBehavior> aBehaviorSet = new ArrayList<StateEventBehavior>();
		for (StateEventBehavior aBehavior : stateEventBehaviorSet) {
			if (aBehavior.onEvent() == Context.event()) {
				aBehaviorSet.add(aBehavior);
			}
		}
		return aBehaviorSet;
	}

	public ArrayList<Condition> eventRequiresSet() {
		// description
		// a model compiler helper function that returns the union of all requires
		// clauses
		// on all Transaction Actions for this Event
		// requires
		// Context.event() <> null
		// guarantees
		// if this Event causes any Transitions
		// then returns an ArrayList of the requires conditions for all
		// TransitionActions on this Event
		// otherwise returns an empty ArrayList
		ArrayList<Condition> anEventRequiresSet = new ArrayList<Condition>();
		for (StateEventBehavior aBehavior : stateEventBehaviorSet) {
			if (aBehavior.onEvent() == Context.event()) {
				for (Condition aRequiresCondition : aBehavior.transitionActionsRequiresSet()) {
					if (!anEventRequiresSet.contains(aRequiresCondition)) {
						anEventRequiresSet.add(aRequiresCondition);
					}
				}
			}
		}
		return anEventRequiresSet;
	}

	public ArrayList<Condition> eventGuaranteesSet() {
		// description
		// a model compiler helper function that returns the union of all guarantees
		// clauses
		// on all Transaction Actions for this Event
		// requires
		// Context.event() <> null
		// guarantees
		// if this Event causes any Transitions
		// then returns an ArrayList of the guarantees conditions for all
		// TransitionActions on this Event
		// otherwise returns an empty ArrayList
		ArrayList<Condition> anEventGuaranteesSet = new ArrayList<Condition>();
		for (StateEventBehavior aBehavior : stateEventBehaviorSet) {
			if (aBehavior.onEvent() == Context.event()) {
				for (Condition aGuaranteesCondition : aBehavior.transitionActionsGuaranteesSet()) {
					if (!anEventGuaranteesSet.contains(aGuaranteesCondition)) {
						anEventGuaranteesSet.add(aGuaranteesCondition);
					}
				}
			}
		}
		return anEventGuaranteesSet;
	}

	public boolean hasStateActions() {
		// description
		// a model compiler helper function that tells whether or not there are any
		// state actions
		// on the state model for this class
		// requires
		// none
		// guarantees
		// if this class' state model has any state actions
		// then returns true
		// otherwise returns false
		boolean hasAnyStateActions = false;
		for (State aState : stateSet) {
			if (!aState.allStateActions().isEmpty()) {
				hasAnyStateActions = true;
			}
		}
		return hasAnyStateActions;
	}

	public ArrayList<String> pIMImports() {
		// description
		// a PIM Overlay layer helper function that returns all of the imports for this
		// class
		// requires
		// none
		// guarantees
		// returns the PIM Overlay import list
		return pIMImportSet;
	}

	public ArrayList<String> pIMConstants() {
		// description
		// a PIM Overlay layer helper function that returns all of the constants for
		// this class
		// requires
		// none
		// guarantees
		// returns the PIM Overlay constant list
		return pIMConstantSet;
	}

	public ArrayList<String> pIMHelperFunctions() {
		// description
		// a PIM Overlay layer helper function that returns all of the helper code for
		// this class
		// requires
		// none
		// guarantees
		// returns the PIM Overlay helper code
		return pIMHelperCode;
	}

	// Modifiers

	public void setDescription(String aDescription) {
		// description
		// a meta-model operation that sets the description of a Meta-Model class
		// requires
		// none, a null description shouldn't hurt anything
		// guarantees
		// description has been set, as specified
		description = aDescription;
	}

	public void addAttribute(Attribute attributeToAdd) {
		// description
		// a meta-model operation that adds an attribute to this Meta-Model Class
		// requires
		// attributeToAdd <> null and is unique among all attributes in this class
		// guarantees
		// attributeToAdd has been added to the attribute set
		attributeSet.add(attributeToAdd);
	}

	public void addDerivedAttribute(DerivedAttribute derivedAttributeToAdd) {
		// description
		// a meta-model operation that adds a derived attribute to this Meta-Model Class
		// requires
		// derivedAttributeToAdd <> null and is unique among all derived attributes in
		// this class
		// guarantees
		// derivedAttributeToAdd has been added to the derived attribute set
		derivedAttributeSet.add(derivedAttributeToAdd);
	}

	public void addAction(Action actionToAdd) {
		// description
		// a meta-model operation that adds an action to this Meta-Model Class
		// requires
		// actionToAdd <> null and is unique among all actions in this class
		// guarantees
		// actionToAdd has been added to the action set
		actionSet.add(actionToAdd);
	}

	public void addState(State stateToAdd) {
		// description
		// a meta-model operation that adds a state to this Meta-Model Class
		// requires
		// stateToAdd <> null and is unique among all states in this class
		// guarantees
		// stateToAdd has been added to the state set
		stateSet.add(stateToAdd);
	}

	public void addEvent(Event eventToAdd) {
		// description
		// a meta-model operation that adds an event to this Meta-Model Class
		// requires
		// eventToAdd <> null and is unique among all events in this class
		// guarantees
		// eventToAdd has been added to the event set
		eventSet.add(eventToAdd);
	}

	public void addStateEventBehavior(StateEventBehavior behaviorToAdd) {
		// description
		// a meta-model operation that adds a State-Event Behavior to this Meta-Model
		// Class
		// e.g., a Transition, Ignore, or Impossible from the Meta-Model
		// requires
		// behaviorToAdd <> null
		// guarantees
		// behaviorToAdd has been added to the state-event behavior set
		stateEventBehaviorSet.add(behaviorToAdd);
	}

	public void clearPIMImports() {
		// description
		// a PIM Overlay layer helper function that clears all of the imports for this
		// class
		// requires
		// none
		// guarantees
		// PIM Overlay import list has been set to an empty ArrayList
		pIMImportSet = new ArrayList<String>();
	}

	public void addPIMImport(String anImportStatement) {
		// description
		// a PIM Overlay layer helper function that appends the given import statement
		// for this class
		// requires
		// none
		// guarantees
		// the given line of PIM Overlay import statement has been appended
		pIMImportSet.add(anImportStatement);
	}

	public void clearPIMConstants() {
		// description
		// a PIM Overlay layer helper function that clears all of the constants for this
		// class
		// requires
		// none
		// guarantees
		// PIM Overlay constant list has been set to an empty ArrayList
		pIMConstantSet = new ArrayList<String>();
	}

	public void addPIMConstant(String aConstantStatement) {
		// description
		// a PIM Overlay layer helper function that appends the given constant statement
		// for this class
		// requires
		// none
		// guarantees
		// the given line of PIM Overlay constant statement has been appended
		pIMConstantSet.add(aConstantStatement);
	}

	public void clearPIMHelperFunctions() {
		// description
		// a PIM Overlay layer helper function that clears all of the helper code for
		// this class
		// requires
		// none
		// guarantees
		// PIM Overlay helper code has been set to an empty ArrayList
		pIMHelperCode = new ArrayList<String>();
	}

	public void addPIMHelperFunctions(String aLineOfHelperCode) {
		// description
		// a PIM Overlay layer helper function that appends the given helper code for
		// this class
		// requires
		// none
		// guarantees
		// the given line of PIM Overlay helper code has been appended
		pIMHelperCode.add(aLineOfHelperCode);
	}

	// Private methods

	// none

	// ******MODEL COMPILER******
	// Everything from here down is specific to the model compiler itself

	public void rulePythonClassFile() {
		// description
		// This is the top-level rule that compiles an entire CIM/PIM class into Python
		// this rule really only handles the .py file open and close, it doesn't
		// generate any source code
		//
		// Class.#PYTHON_CLASS_FILE -->
		// <Open the output file here>
		// #PYTHON_CLASS_FRAME
		// <Close the output file here>
		//
		// requires
		// none
		// guarantees
		// the Python source code for a CIM/PIM class has been emitted, presumably into
		// the designated file
		String outputJALFileName = Context.model().javaSourceCodePath() + NameService.asClassLevelName(name) + ".py";
		System.out.println("--compiling: " + outputJALFileName);
		Context.codeOutput().openJALOutputFile(outputJALFileName);
		Context.codeOutput().indentNone();
		this.rulePythonClassFrame();
		Context.codeOutput().closeJALOutputFile();
		System.out.println("--done compiling: " + outputJALFileName);
		System.out.println();
	}

	public void rulePythonClassFrame() {
		// description
		// This rule emits the implementation code for a Meta-Model Class
		// according to the book this is a frame not a rule, but any difference is
		// negligible in practice
		//
		// Class.#PYTHON_CLASS_FRAME -->
		// if include package name
		// 'package ' aModel.#MODEL_NAME
		// #PIM_IMPORTS_LIST
		// 'public class ' + this.#CLASS_NAME + ' {'
		// #CLASS_DESCRIPTION
		// #PIM_CONSTANTS_LIST
		// 'public enum ' this.#CLASS_NAME + '_states { ' + this.#STATE_ENUM_LIST + ' };
		// #ATTRIBUTE_INST_VAR_LIST
		// #ASSOCIATION_INST_VAR_LIST
		// #CONSTRUCTOR_OPERATION
		// #ATTRIBUTE_GETTERS_LIST
		// ...
		// '}'
		//
		// requires
		// none
		// guarantees
		// the code for a Meta-model CIM/PIM class have been emitted
		Context.setMMClass(this);
		if (Context.model().includeFutureAnnotations()) {
			Context.codeOutput().println("from __future__ import annotations");
			Context.codeOutput().println("");
		}
		if (Context.model().includePackageName()) {
			Context.codeOutput().indent();
			Context.codeOutput().println("package " + NameService.asClassLevelName(Context.model().name()) + ";");
			Context.codeOutput().println("");
		}
		this.rulePIMImportsList();
		Context.codeOutput().indent();
		Context.codeOutput().println("class " + NameService.asClassLevelName(name) + ":");
		Context.codeOutput().println("");
		Context.codeOutput().indentMore();
		DateFormat aDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String todayString = aDateFormat.format(new Date());
		Context.codeOutput().indent();
		Context.codeOutput()
				.println("# generated " + todayString + " by JAL open model compiler " + ModelCompiler.versionId);
		Context.codeOutput().println("");
		Context.codeOutput().println("");
		this.ruleClassDescription();
		this.rulePIMConstantsList();
		this.ruleStateEnumDeclaration();
		this.ruleAttributeInstVarList();
		this.ruleClassVarList();
		this.ruleAssociationInstVarList();
		this.ruleConstructorOperation();
		this.ruleAttributeGettersList();
		this.ruleDerivedAttributeGettersList();
		this.rulePushedEventOperationsList();
		this.rulePrivateTransitionActionsList();
		if (this.hasStateActions()) {
			this.ruleStateActionsTimeSlice();
			this.rulePrivateStateActionsList();
		}
		if (Context.model().isSafeModeOn()) {
			this.ruleInvariantsCheckOperation();
			this.ruleImplementReferenceCheckOperations();
		}
		this.rulePIMHelperCode();
		// this.ruleAllMembersAccessor(); this is now replaced with the ruleClassVarList
		this.ruleAssociationLinkUnlinkServices();
		Context.codeOutput().indentLess();
		Context.codeOutput().println("");
		Context.codeOutput().println("");
		Context.clearMMClass();
	}

	public void rulePIMImportsList() {
		// description
		// This rule emits the set of standard and PIM-specific import statements
		// e.g., "import java.util.ArrayList;
		// for now, ArrayList is the only standard import needed. Can add more later if
		// relevant
		//
		// Class.#PIM_IMPORTS_LIST -->
		// 'import java.util.ArrayList;'
		// foreach aPIMImport in this class
		// aPIMImport
		//
		// requires
		// none
		// guarantees
		// the set of standard and PIM class-specific imports have been emitted
		Context.codeOutput().indent();
		for (String anImportStatement : pIMImportSet) {
			Context.codeOutput().indent();
			Context.codeOutput().println(anImportStatement);
		}
		Context.codeOutput().println("# see MMClass.rulePIMImportsList for implementation");
		Context.codeOutput().println("");
	}

	public void ruleClassDescription() {
		// description
		// this rule emits the class description as a comment, but only in verbose mode
		// requires
		// none
		// guarantees
		// the class description text has been emitted
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().println("# Class description");
			Context.codeOutput().println("# see MMClass.ruleClassDescription for implementation");
			Context.codeOutput().println("");
			Context.codeOutput().indentMore();
			Context.codeOutput().indent();
			Context.codeOutput().println("# " + description);
			Context.codeOutput().indentLess();
			Context.codeOutput().println("");
			Context.codeOutput().println("");
		}
	}

	public void rulePIMConstantsList() {
		// description
		// This rule emits the set of PIM-specific constant statements
		//
		// Class.#PIM_CONSTANTS_LIST -->
		// if( in verbose mode )
		// '// PIM constants' +
		// '// ' +
		// if( class has PIM constants )
		// then foreach aPIMConstant in this class
		// aPIMConstant
		// otherwise if( in verbose mode )
		// '// none'
		//
		// requires
		// none
		// guarantees
		// the set of PIM class-specific constants have been emitted
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().println("# PIM constants");
			Context.codeOutput().println("# see MMClass.rulePIMConstantsList for implementation");
			Context.codeOutput().println("");
			Context.codeOutput().indentMore();
			if (!pIMConstantSet.isEmpty()) {
				for (String aConstantStatement : pIMConstantSet) {
					Context.codeOutput().indent();
					Context.codeOutput().println(aConstantStatement + ";");
				}
			} else {
				if (Context.model().isVerbose()) {
					Context.codeOutput().indent();
					Context.codeOutput().println("# none");
				}
			}
			Context.codeOutput().indentLess();
		} else {
			for (String aConstantStatement : pIMConstantSet) {
				Context.codeOutput().indent();
				Context.codeOutput().println(aConstantStatement + ";");
			}
		}
		Context.codeOutput().println("");
		Context.codeOutput().println("");
	}

	public void ruleStateEnumDeclaration() {
		// description
		// This rule emits the state names as enums
		// e.g., "<classname>_states = Enum("<classname>_states", "STATE1 STATE2")
		//
		// Class.#STATE_ENUM_DECLARATION -->
		// state attribute rt type + ' { ' +
		// #STATE_ENUM_LIST +
		// ' };'
		//
		// requires
		// none
		// guarantees
		// the state enum declaration for this class has been emitted
		Context.codeOutput().indent();
		Context.codeOutput().println("# State Enum Declaration");
		Context.codeOutput().println("# see MMClass.ruleStateEnumDeclaration for implementation");
		Context.codeOutput().println("");
		Context.codeOutput().print(this.stateAttributeEnumRTType() + " = Enum(");
		// this is uniquely for Python when stating Enum
		Context.codeOutput().print("\"" + this.stateAttributeEnumRTType() + "\", ");
		// this is uniquely for Python when stating Enum
		// open the enumlist
		Context.codeOutput().print("\"");
		this.ruleStateEnumList();
		// this is uniquely for Python when stating Enum
		// close the enumlist
		Context.codeOutput().print("\"");
		Context.codeOutput().println(" )");
		Context.codeOutput().println("");
		Context.codeOutput().println("");
	}

	public void ruleStateEnumList() {
		// description
		// This rule emits the state names as enums
		// e.g., "<classname>_states = Enum("<classname>_states", "STATE1 STATE2")
		//
		// Class.#STATE_ENUM_LIST -->
		// foreach aState in the state model
		// aState name as all caps + ', ' (except after last one)
		//
		// requires
		// none
		// guarantees
		// the set of state enums for this class has been emitted
		// Implementation notes
		// needs to be done with Iterator to avoid trailing space
		Iterator<State> stateIterator = stateSet.iterator();
		while (stateIterator.hasNext()) {
			State aState = stateIterator.next();
			Context.codeOutput().print(aState.nameAsENUM());
			if (stateIterator.hasNext()) {
				Context.codeOutput().print(" ");
			}
		}
	}

	public void ruleAttributeInstVarList() {
		// description
		// this rule emits the set of (private) instance variable declarations, if any
		//
		// Class.#ATTRIBUTE_INST_VAR_LIST -->
		// foreach anAttribute in class
		// anAttribute.#DEFINE_INST_VAR
		//
		// requires
		// none
		// guarantees
		// all attributes of this class have been declared as instance variable of the
		// PIM Overlay run-time type
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().println("# Attribute instance variables");
			Context.codeOutput().println("# see MMClass.ruleAttributeInstVarList for implementation");
			Context.codeOutput().println("# Python doesn't treat private variables too strictly");
			Context.codeOutput().println("# by convention it uses _ for private variables");
			Context.codeOutput().println("# https://docs.python.org/3/tutorial/classes.html#private-variables");
			Context.codeOutput().println(
					"# and it treats client code as responsible users if they choose to ignore the convention");
			Context.codeOutput().println("# https://docs.python-guide.org/writing/style/#we-are-all-responsible-users");
			Context.codeOutput().println("");
			if (!attributeSet.isEmpty()) {
				for (Attribute anAttribute : attributeSet) {
					anAttribute.ruleDefineInstVarAsPrivate();
				}
			} else {
				if (Context.model().isVerbose()) {
					Context.codeOutput().indent();
					Context.codeOutput().println("# none");
				}
			}
			Context.codeOutput().indentLess();
		} else {
			for (Attribute anAttribute : attributeSet) {
				anAttribute.ruleDefineInstVarAsPrivate();
			}
		}
		Context.codeOutput().println("");
		Context.codeOutput().println("");
	}

	public void ruleAssociationInstVarList() {
		// description
		// This rule emits the set of instance variables to support all associations
		// this class participates in
		//
		// Class.#ASSOCIATION_INST_VAR_LIST -->
		// foreach anAssociation this class participates in
		// anAssociation.#DECLARE_ASSOCIATION_INST_VAR
		//
		// requires
		// Context.mMClass() = this
		// guarantees
		// the association instance variables, if any, have been emitted
		boolean participatesInAnyAssociations = false;
		for (Association anAssociation : Association.allAssociations()) {
			participatesInAnyAssociations = participatesInAnyAssociations || anAssociation.involvesClass(this);
		}
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().indentMore();
			Context.codeOutput().println("# Association participation instance variables");
			Context.codeOutput().println("# see MMClass.ruleAssociationInstVarList");
			Context.codeOutput().println("");
			Context.codeOutput().indentMore();
			if (participatesInAnyAssociations) {
				for (Association anAssociation : Association.allAssociations()) {
					if (anAssociation.involvesClass(this)) {
						anAssociation.ruleDeclareAssociationInstVar();
					}
				}
			} else {
				if (Context.model().isVerbose()) {
					Context.codeOutput().indent();
					Context.codeOutput().println("# none");
				}
			}
			Context.codeOutput().indentLess();
		} else {
			for (Association anAssociation : Association.allAssociations()) {
				if (anAssociation.involvesClass(this)) {
					anAssociation.ruleDeclareAssociationInstVar();
				}
			}
		}
		Context.codeOutput().println("");
		Context.codeOutput().println("");
	}

	public void ruleConstructorOperation() {
		// description
		// this rule emits the constructor for the class
		//
		// Class.#CONSTRUCTOR_OPERATION -->
		// with anEvent = "<<new>>": anEvent.#CONSTRUCTOR_OPERATION
		//
		// requires
		// none
		// guarantees
		// if an event named "<<new>>" exists in the CIM state model
		// then the class' constructor code has been emitted
		// otherwise a compiler error is reported (in generated source code, for now)
		Event constructEvent = this.eventNamed(Event.defaultNewEventName);
		if (constructEvent != null) {
			if (Context.model().isVerbose()) {
				Context.codeOutput().indent();
				Context.codeOutput().println("# Constructor");
				Context.codeOutput().println("# See MMClass.ruleConstructorOperation");

				Context.codeOutput().println("");
				Context.codeOutput().println("# See constructEvent.ruleConstructorOperation");
				constructEvent.ruleConstructorOperation();
				Context.codeOutput().indentLess();
			} else {
				constructEvent.ruleConstructorOperation();
				Context.codeOutput().indentLess();
			}
		} else {
			Context.codeOutput().println("****** ERROR: Can't find an event named" + Event.defaultNewEventName);
			Context.codeOutput().println("******        Can't generate code for a constructor!!!!");
			System.out.println("****** ERROR: Can't find an event named" + Event.defaultNewEventName);
			System.out.println("******        Can't generate code for a constructor!!!!");
			Context.codeOutput().incrementErrorCount();
		}
		Context.codeOutput().println("");
		Context.codeOutput().println("");
	}

	public void ruleInitializeAssociationInstVars() {
		// description
		// This rule emits initializers (in the constructor) for instance variables that
		// support
		// all associations this class participates in
		// This rule is part of Event.#CONSTRUCTOR_OPERATION
		//
		// Class.#INITIALIZE_ASSOCIATION_INST_VARS -->
		// foreach anAssociation this class participates in
		// #INITIALIZE_ASSOCIATION
		//
		// requires
		// Context.mMClass() = this
		// guarantees
		// the association instance variable initializations, if any, have been emitted
		for (Association anAssociation : Association.allAssociations()) {
			if (anAssociation.involvesClass(this)) {
				anAssociation.ruleInitializeAssociationInstVar();
			}
		}
	}

	public void ruleAttributeGettersList() {
		// description
		// this rule emits the code for getters for all attributes that are not derived
		// attributes
		// TBD--a smarter rule would only emit getters that were used, an enhancement
		// for later...
		//
		// Class.ATTRIBUTE_GETTERS_LIST -->
		// foreach anAttribute of this class that is not a derived attribute
		// #GETTER
		//
		// requires
		// none
		// guarantees
		// code implementing getters for all attributes have been emitted
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().indentMore();
			Context.codeOutput().println("# Attribute getters");
			Context.codeOutput().println("");
			if (!attributeSet.isEmpty()) {
				for (Attribute anAttribute : attributeSet) {
					anAttribute.ruleGetter();
				}
			} else {
				if (Context.model().isVerbose()) {
					Context.codeOutput().indent();
					Context.codeOutput().println("# none");
					Context.codeOutput().println("");
				}
			}
			Context.codeOutput().indentLess();
		} else {
			for (Attribute anAttribute : attributeSet) {
				anAttribute.ruleGetter();
			}
		}
		Context.codeOutput().println("");
	}

	public void ruleDerivedAttributeGettersList() {
		// description
		// this rule emits the set of (public) derived attribute getters, if any
		//
		// Class.#DERIVED_ATTRIBUTE_GETTERS -->
		// foreach aDerivedAttribute on this class
		// #DERIVED_GETTER
		//
		// requires
		// none
		// guarantees
		// all derived attributes of this class have been implemented as compute on
		// demand
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().indentMore();
			Context.codeOutput().println("# Derived attributes");
			Context.codeOutput().println("");
			Context.codeOutput().indentMore();
			if (!derivedAttributeSet.isEmpty()) {
				for (DerivedAttribute aDerivedAttribute : derivedAttributeSet) {
					aDerivedAttribute.ruleDerivedGetter();
				}
			} else {
				if (Context.model().isVerbose()) {
					Context.codeOutput().indent();
					Context.codeOutput().println("# none");
					Context.codeOutput().println("");
				}
			}
			Context.codeOutput().indentLess();
		} else {
			for (DerivedAttribute aDerivedAttribute : derivedAttributeSet) {
				aDerivedAttribute.ruleDerivedGetter();
			}
		}
		Context.codeOutput().println("");
	}

	public void rulePushedEventOperationsList() {
		// description
		// this rule emits code that implements all events (except the constructor) as
		// push operations
		// e.g., event <x> --> "public void <x>() { if( state == <STARTSTATE> && guard )
		// ) { ... } }"
		//
		// Class.#PUSHED_EVENT_OPERATIONS_LIST -->
		// foreach anEvent on this class that is not "<<new>>"
		// #PUSHED_EVENT_OPERATION
		//
		// requires
		// none
		// guarantees
		// all events (except <<new>>) have been implemented as push operations &
		// methods
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().println("# Pushed events");
			Context.codeOutput().println("");

			if ((eventSet.size() > 1)) {
				for (Event anEvent : eventSet) {
					if (!anEvent.name().equals(Event.defaultNewEventName)) {
						anEvent.rulePushedEventOperation();
					}
				}
			} else {
				if (Context.model().isVerbose()) {
					Context.codeOutput().indentMore();
					Context.codeOutput().indent();
					Context.codeOutput().println("# none");
					Context.codeOutput().println("");
				}
			}
			Context.codeOutput().indentLess();
		} else {
			for (Event anEvent : eventSet) {
				if (!anEvent.name().equals(Event.defaultNewEventName)) {
					anEvent.rulePushedEventOperation();
				}
			}
		}
		Context.codeOutput().println("");
	}

	public void rulePrivateTransitionActionsList() {
		// description
		// this rule emits the (private) methods that implement each transition action
		//
		// Class.#PRIVATE_TRANSITION_ACTIONS_LIST -->
		// foreach anAction on any transition in this class
		// #DEFINE_PRIVATE_ACTION
		//
		// requires
		// none
		// guarantees
		// Each transition action has been implemented by a (private) operation and
		// method
		ArrayList<Action> actionSet = new ArrayList<Action>();
		for (StateEventBehavior aBehavior : stateEventBehaviorSet) {
			if (aBehavior.isTransition()) {
				for (Action anAction : aBehavior.transitionActionSet()) {
					if (!actionSet.contains(anAction)) {
						actionSet.add(anAction);
					}
				}
			}
		}
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().indentMore();
			Context.codeOutput().println("# Private transition actions");
			Context.codeOutput().println("");

			if (!actionSet.isEmpty()) {
				for (Action anAction : actionSet) {
					anAction.ruleDefinePrivateAction();
				}
			} else {
				if (Context.model().isVerbose()) {
					Context.codeOutput().indent();
					Context.codeOutput().println("# none");
					Context.codeOutput().println("");
				}
			}
			Context.codeOutput().indentLess();
		} else {
			for (Action anAction : actionSet) {
				anAction.ruleDefinePrivateAction();
			}
		}
		Context.codeOutput().println("");
	}

	public void ruleStateActionsTimeSlice() {
		// description
		// this rule emits the timeSlice() operation and method for state actions
		// this rule assumes it doesn't get invoked unless there is at least one state
		// action
		// (i.e., it doesn't need to do the check for "# none"
		//
		// Class.#STATE_ACTIONS_TIME_SLICE -->
		// 'public void timeSlide() {'
		// foreach aState in state model
		// #CALL_STATE_TIME_SLICE
		// '}'
		//
		// requires
		// none
		// guarantees
		// the timeSlice() operation and method implementation has been emitted
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().println("# timeSlice() for state actions");
			Context.codeOutput().println("");
			Context.codeOutput().indentMore();
		}
		Context.codeOutput().indent();
		Context.codeOutput().println("public void timeSlice() {");
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().println("# requires");
			Context.codeOutput().indent();
			Context.codeOutput().println("#   none");
			Context.codeOutput().indent();
			Context.codeOutput().println("# guarantees");
			Context.codeOutput().indent();
			Context.codeOutput().println("#   one cycle of state-appropriate processing has been done");
		}
		Context.codeOutput().indentMore();
		for (State aState : stateSet) {
			aState.ruleCallStateTimeSlice();
		}
		Context.codeOutput().indentLess();
		Context.codeOutput().indent();

		if (Context.model().isVerbose()) {
			Context.codeOutput().indentLess();
		}
		Context.codeOutput().println("");
		Context.codeOutput().println("");
	}

	public void rulePrivateStateActionsList() {
		// description
		// this rule emits the (private) operations and methods that implement each
		// state action
		// this rule assumes it doesn't get invoked unless there is at least one state
		// action
		// (i.e., it doesn't need to do the check for "# none"
		//
		// Class.#PRIVATE_STATE_ACTIONS_LIST -->
		// foreach anAction on this class's state model
		// #DEFINE_PRIVATE_ACTION
		//
		// requires
		// none
		// guarantees
		// Each state action has been implemented by a (private) operation and method
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().println("# Private state actions");
			Context.codeOutput().println("");
			Context.codeOutput().indentMore();
		}
		ArrayList<Action> stateActionSet = new ArrayList<Action>();
		for (State aState : stateSet) {
			for (Action anAction : aState.allStateActions()) {
				if (!stateActionSet.contains(anAction)) {
					stateActionSet.add(anAction);
				}
			}
		}
		for (Action anAction : stateActionSet) {
			anAction.ruleDefinePrivateAction();
		}
		if (Context.model().isVerbose()) {
			Context.codeOutput().indentLess();
		}
		Context.codeOutput().println("");
	}

	public void ruleInvariantsCheckOperation() {
		// description
		// this rule emits an operation to check class invariants at run time
		//
		// Class.#INVARIANTS_CHECK_OPERATION -->
		// 'public void classInvariantsCheck() {'
		// '// requires'
		// '// none'
		// '// guarantees'
		// '// all verifiable run-time invariants checks have passed
		// foreach anAttribute
		// anAttribute.#RANGE_CHECK
		// foreach anAssociation that involves this class
		// anAssociation.#MULTIPLICITY_CHECK
		// anAssociation.#REFERENCE_CHECK
		// '}'
		//
		// requires
		// none
		// guarantees
		// code to verify all instance level invariant checks has been emitted
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().println("# safe mode class invariants checks");
			Context.codeOutput().indentMore();
			Context.codeOutput().println("");
		}
		Context.codeOutput().indent();
		Context.codeOutput().println("public void classInvariantsCheck() {");
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().println("# requires");
			Context.codeOutput().indent();
			Context.codeOutput().println("#   none");
			Context.codeOutput().indent();
			Context.codeOutput().println("# guarantees");
			Context.codeOutput().indent();
			Context.codeOutput().println("#   all verifiable run-time invariants checks have passed");
		}
		Context.codeOutput().indentMore();
		for (Attribute anAttribute : attributeSet) {
			if (anAttribute.isAssertable()) {
				anAttribute.ruleInstVarInvariantsCheck();
			}
		}
		for (Association anAssociation : Association.allAssociations()) {
			if (anAssociation.involvesClass(this)) {
				anAssociation.ruleAssociationInvariantsCheck();
				anAssociation.ruleAssociationReferenceCheck();
			}
		}
		Context.codeOutput().indentLess();
		Context.codeOutput().indent();

		Context.codeOutput().println("");
	}

	public void ruleImplementReferenceCheckOperations() {
		// description
		// this rule emits an operation to check class invariants at run time
		//
		// Class.#IMPLEMENT_REFERENCE_CHECK_OPERATION -->
		// foreach anAssociation that involves this class
		// anAssociation.#IMPLEMENT_REFERENCE_CHECK
		// '}'
		//
		// requires
		// none
		// guarantees
		// code to verify all instance level invariant checks has been emitted
		for (Association anAssociation : Association.allAssociations()) {
			if (anAssociation.involvesClass(this)) {
				anAssociation.ruleImplementAssociationReferenceCheck();
			}
		}
		Context.codeOutput().println("");
		if (Context.model().isVerbose()) {
			Context.codeOutput().indentLess();
		}
	}

	public void rulePIMHelperCode() {
		// description
		// this rule emits the (private) PIM Overlay Layer helper code
		//
		// Class.#PIM_HELPER_CODE -->
		// foreach line of helper code
		// write it out
		//
		// requires
		// none
		// guarantees
		// the PIM overlay helper code has been emitted
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().indentMore();
			Context.codeOutput().println("# PIM Overlay helper code");
			Context.codeOutput().println("");
			if (!pIMHelperCode.isEmpty()) {
				for (String aLineOfHelperCode : pIMHelperCode) {
					Context.codeOutput().indent();
					Context.codeOutput().println(NameService.formatActionStmt(aLineOfHelperCode));
				}
				Context.codeOutput().println("");
			} else {
				if (Context.model().isVerbose()) {
					Context.codeOutput().indent();
					Context.codeOutput().println("# none");
					Context.codeOutput().println("");
				}
			}
			Context.codeOutput().indentLess();
		} else {
			for (String aLineOfHelperCode : pIMHelperCode) {
				Context.codeOutput().indent();
				Context.codeOutput().println(NameService.formatActionStmt(aLineOfHelperCode));
			}
		}
		Context.codeOutput().println("");
	}

	public void ruleClassVarList() {
		// description
		// this rule emits code to provide ClassVar primarily to replace the All member
		// accessor
		// the class
		//
		// Class.#All_MEMBERS_ACCESSOR -->
		// ClassNameSet: ClassVar[List[ClassName]] = []
		//
		//
		// Note that members are automatically added to ClassNameSet in generated
		// constructor code. No method is needed for accessor to keep things pythonic
		// There is, however, no automatic removal. One option is when app client code
		// is doing the
		// iteration it can ignore objects in the set whose state is "DOESNTEXIST", as
		// in:
		//
		//
		// code that provides access to all members of the class has been emitted
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().indentMore();
			Context.codeOutput().println("# Class level attribute");
			Context.codeOutput().println("# All class members accessor");
			Context.codeOutput().println("");
		}
		Context.codeOutput().indent();

		Context.codeOutput().println(NameService.asClassLevelName(name) + "Set" + ": ClassVar[List["
				+ NameService.asClassLevelName(name) + "]] = []");
		Context.codeOutput().indent();
		Context.codeOutput().indentLess();
		Context.codeOutput().println("");
		Context.codeOutput().println("");
	}

	public void ruleAllMembersAccessor() {
		// description
		// this rule emits code to let anyone access the set of all run-time objects in
		// the class
		//
		// Class.#All_MEMBERS_ACCESSOR -->
		// private static ArrayList<ClassName> classNameSet = new
		// ArrayList<ClassName>();
		//
		// public static ArrayList<ClassName> allClassNames() {
		// return classNameSet;
		// }
		//
		// Note that members are automatically added to classNameSet in generated
		// constructor code
		// There is, however, no automatic removal. One option is when app client code
		// is doing the
		// iteration it can ignore objects in the set whose state is "DOESNTEXIST", as
		// in:
		//
		// for( ClassName aMember: ClassName.allClassNames() ) {
		// if( aMember.state() != ClassName.ClassName_states.DOESNTEXIST ) {
		// // process aMember as a valid instance here
		// }
		// }
		//
		// Another alternative is to include removal code in any destructor / finalizer
		// of this class, as in:
		//
		// int memberIndex = classNameSet.indexOf( this );
		// if( memberIndex != -1 )
		// classNameSet.remove( memberIndex );
		// }
		//
		// requires
		// none
		// guarantees
		// code that provides access to all members of the class has been emitted
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().println("# All class members accessor");
			Context.codeOutput().println("");
			Context.codeOutput().indentMore();
		}
		Context.codeOutput().indent();
		Context.codeOutput().print("private static ArrayList<" + NameService.asClassLevelName(name) + "> ");
		Context.codeOutput().print(NameService.asInstanceLevelName(name) + "Set = new ArrayList<");
		Context.codeOutput().println(NameService.asClassLevelName(name) + ">();");
		Context.codeOutput().println("");
		Context.codeOutput().indent();
		Context.codeOutput().print("public static ArrayList<" + NameService.asClassLevelName(name) + "> all");
		Context.codeOutput().println(NameService.asClassLevelName(name) + "s() {");
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().println("# requires");
			Context.codeOutput().indent();
			Context.codeOutput().println("#   none");
			Context.codeOutput().indent();
			Context.codeOutput().println("# guarantees");
			Context.codeOutput().indent();
			Context.codeOutput().println("# returns (reference to) ArrayList<> of all class members");
		}
		Context.codeOutput().indentMore();
		Context.codeOutput().indent();
		Context.codeOutput().println("return " + NameService.asInstanceLevelName(name) + "Set;");
		Context.codeOutput().indentLess();
		Context.codeOutput().indent();

		Context.codeOutput().indentLess();
		Context.codeOutput().println("");
		Context.codeOutput().println("");
	}

	public void ruleAssociationLinkUnlinkServices() {
		// description
		// This rule emits the link and unlink services for instance variables that
		// support all associations this class participates in
		//
		// Class.#ASSOCIATION_LINK_UNLINK_SERVICES -->
		// foreach anAssociation this class participates in
		// anAssociation.#LINK_UNLINK_SERVICES
		//
		// requires
		// Context.mMClass() = this
		// guarantees
		// the link and unlink services for association instance variables, if any, have
		// been emitted
		boolean participatesInAnyAssociations = false;
		for (Association anAssociation : Association.allAssociations()) {
			participatesInAnyAssociations = participatesInAnyAssociations || anAssociation.involvesClass(this);
		}
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().indentMore();
			Context.codeOutput().println("# Association participation link and unlink services");
			Context.codeOutput().println("");
			if (participatesInAnyAssociations) {
				for (Association anAssociation : Association.allAssociations()) {
					if (anAssociation.involvesClass(this)) {
						anAssociation.ruleLinkUnlinkServices();
					}
				}
			} else {
				if (Context.model().isVerbose()) {
					Context.codeOutput().indent();
					Context.codeOutput().indentMore();
					Context.codeOutput().println("# none");
				}
			}
			Context.codeOutput().indentLess();
		} else {
			for (Association anAssociation : Association.allAssociations()) {
				if (anAssociation.involvesClass(this)) {
					anAssociation.ruleLinkUnlinkServices();
				}
			}
		}
		Context.codeOutput().println("");
	}

}
