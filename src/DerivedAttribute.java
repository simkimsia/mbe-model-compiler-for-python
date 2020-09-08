
public class DerivedAttribute {
	// General description
	// Model compiler
	// This class implements class Derived Attribute in the Meta-Model
	// Invariants
	// none
	// Implementation notes
	// even though it's not shown in the meta-model (because it is PIM Overlay level
	// stuff),
	// Derived Attributes are specified in terms of an Action. This is to give the
	// full power of
	// Actions including JAL.
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

	public static String tagDerivedAttributeStart = "<derivedattribute>";
	public static String tagDerivedAttributeDataType = "<range>";
	public static String tagDerivationAction = "<derivationaction>";
	public static String tagDerivedAttributeEnd = "</derivedattribute>";

	// Static (class) variables

	// none

	// Static (class) methods

	public static DerivedAttribute parseDerivedAttribute() {
		// requires
		// Input model stream is open and ready to read at the first line after
		// "<derivedattribute>"
		// guarantees
		// returns a populated instance of derived attribute (or null, if nothing/error
		// in model file)
		// and input model string is right after "</attribute>"
		DerivedAttribute newDerivedAttribute = new DerivedAttribute(JALInput.nextLine());

		// now look for modifiers: description, range type, ...
		String line = JALInput.nextLine();
		while (!line.contains(DerivedAttribute.tagDerivedAttributeEnd)) {
			if (line.contains(NameService.tagDescriptionStart)) {
				newDerivedAttribute.setDescription(JALInput.nextLine());
			}
			if (line.contains(DerivedAttribute.tagDerivedAttributeDataType)) {
				newDerivedAttribute.setDerivedDataType(Range.rangeNamed(JALInput.nextLine()));
			}
			if (line.contains(DerivedAttribute.tagDerivationAction)) {
				Action derivingAction = Context.mMClass().actionNamed(JALInput.nextLine());
				newDerivedAttribute.setDerivationAction(derivingAction);
			}
			line = JALInput.nextLine();
		}
		return newDerivedAttribute;
	}

	// Instance variables

	// Meta-model instance variables
	private String name;
	private String description;
	private Range derivedDataType; // this is actually the data type of the dependent variable!!!!

	// PIM Overlay instance variables
	private Action derivationAction;

	// Model compiler instance variables
	// none

	// Constructor(s)

	public DerivedAttribute(String aName) {
		// description
		// default constructor
		// requires
		// aName <> null and is unique among all attributes of this class
		// guarantees
		// an instance has been created and initialized
		// defaults to a Span type of Range
		name = aName;
		description = "none";
		derivedDataType = null;
		derivationAction = null;
	}

	// Accessors

	public String name() {
		// description
		// a meta-model operation that returns the name of this attribute
		// requires
		// none
		// guarantees
		// the attribute name is returned as a string
		return name;
	}

	public String description() {
		// description
		// a meta-model operation that returns the description of this attribute
		// requires
		// none
		// guarantees
		// the attribute description is returned as a string
		return description;
	}

	public Range derivedDataType() {
		// description
		// a meta-model operation that returns the defined Range of this attribute
		// requires
		// none
		// guarantees
		// returns the defined Range
		return derivedDataType;
	}

	public Action derivationAction() {
		// description
		// a meta-model operation that returns the derivation action for this derived
		// attribute
		// requires
		// none
		// guarantees
		// returns the referenced derivation action
		return derivationAction;
	}

	// Modifiers

	public void setName(String aName) {
		// description
		// a meta-model operation that overwrites the name of this derived attribute
		// requires
		// none
		// guarantees
		// the assigned name is overwritten with aName
		name = aName;
	}

	public void setDescription(String aDescription) {
		// description
		// a meta-model operation that overwrites the description of this attribute
		// requires
		// none
		// guarantees
		// the attribute description is overwritten with aDescription
		description = aDescription;
	}

	public void setDerivedDataType(Range aDataType) {
		// description
		// a meta-model operation to assign the Range
		// requires
		// none
		// guarantees
		// definedRange has been overwritten with aRange
		derivedDataType = aDataType;
	}

	public void setDerivationAction(Action aDerivationAction) {
		// description
		// a meta-model operation to assign the derivation action
		// requires
		// none
		// guarantees
		// derivationAction has been overwritten with aDerivationAction
		derivationAction = aDerivationAction;
	}

	// Private methods

	// none

	// ******MODEL COMPILER******
	// Everything from here down is specific to the model compiler itself

	public void ruleDerivedGetter() {
		// description
		// This rule emits the public getter for this derived attribute
		//
		// Attribute.#DERIVED_GETTER -->
		// 'public ' + #PIM_OVERLAY_RTTYPE + this.instanceVariableName() +
		// + #ACTION_FORMAL_PARAMETERS +
		// #ACTION_BODY +
		// ' return result;' +
		// '}'
		//
		// requires
		// this is a derived attribute
		// guarantees
		// the public compute-on-demand derived attribute getter code for this attribute
		// has been emitted
		Context.codeOutput().indent();
		Context.codeOutput().print("public " + NameService.formatActionStmt(derivedDataType.pIMRunTimeType()));
		Context.codeOutput().print(" " + NameService.asInstanceLevelName(name));
		derivationAction.ruleActionFormalParameters();
		derivationAction.ruleSpecifyContract();
		Context.codeOutput().indentMore();
		Context.codeOutput().indent();
		if (Context.model().isAssertionsOn()) {
			derivationAction.ruleEntryAssertions();
		}
		derivationAction.ruleActionBody();
		if (Context.model().isAssertionsOn()) {
			derivationAction.ruleExitAssertions();
		}
		Context.codeOutput().indentLess();
		Context.codeOutput().indent();

		Context.codeOutput().println("");
	}

}
