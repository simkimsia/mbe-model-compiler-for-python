
public class Attribute {
	// General description
	// Model compiler
	// This class implements class Attribute in the Meta-Model
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
	// This material is Copyright � 2019 by Stephen R. Tockey
	// Permission is hereby given to copy, adapt, and distribute this material as
	// long as this notice is included on all such materials and the materials are
	// not sold, licensed, or otherwise distributed for commercial gain.
	//
	// This tool is being distributed "as-is". No warrantee is expressed or implied.
	// While the author believes that this tool gives correct answers, the user
	// assumes all risk of use.

	// Constants

	public static String tagAttributeStart = "<attribute>";
	public static String tagAttributeRangeType = "<range>";
	public static String tagAttributeEnd = "</attribute>";

	// Static (class) variables

	// none

	// Static (class) methods

	public static Attribute parseAttribute() {
		// requires
		// Input model stream is open and ready to read at the first line after
		// "<attribute>"
		// guarantees
		// returns a populated instance of attribute (or null, if nothing/error in model
		// file)
		// if the parsed attribute's name is 'state'
		// then a Range with it's run time type = ClassName_states has been created
		// and input model string is right after "</attribute>"
		Attribute newAttribute = new Attribute(JALInput.nextLine());

		// now look for modifiers: description, range type, ...
		String line = JALInput.nextLine();
		while (!line.contains(Attribute.tagAttributeEnd)) {
			if (line.contains(NameService.tagDescriptionStart)) {
				newAttribute.setDescription(JALInput.nextLine());
			}
			if (line.contains(Attribute.tagAttributeRangeType)) {
				newAttribute.setRange(Range.rangeNamed(JALInput.nextLine()));
				if (newAttribute.range() == null) {
					System.out.println(" ***** Cannot find declared Range for Attribute " + newAttribute.name()
							+ " on class " + Context.mMClass().name());
				}
			}
			line = JALInput.nextLine();
		}
		if (newAttribute.name().equals(State.defaultStateAttributeName)) {
			Range stateAttributeRange = new Range(newAttribute.name() + "states");
			stateAttributeRange.becomeReference("none, for now... this is a bit of a hack really");
			stateAttributeRange.setPIMRunTimeType(NameService.asClassLevelName(Context.mMClass().name()) + "_states");
			newAttribute.setRange(stateAttributeRange);
		}
		return newAttribute;
	}

	// Instance variables

	// Meta-model instance variables
	private String name;
	private String description;
	private Range definedRange;

	// PIM Overlay instance variables
	// none

	// Model compiler instance variables
	// none

	// Constructor(s)

	public Attribute(String aName) {
		// description
		// default constructor
		// requires
		// aName <> null and is unique among all attributes of this class
		// guarantees
		// an instance has been created and initialized
		// defaults to a Span type of Range
		name = aName;
		description = "none";
		definedRange = null;
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

	public Range range() {
		// description
		// a meta-model operation that returns the defined Range of this attribute
		// requires
		// none
		// guarantees
		// returns the defined Range
		return definedRange;
	}

	public boolean isAssertable() {
		// description
		// a PIM Overlay operation that returns whether this attribute is assert-able or
		// not
		// requires
		// none
		// guarantees
		// returns true only when pIMOverlayAssertExpression != null
		return definedRange.isAssertable();
	}

	// Modifiers

	public void setDescription(String aDescription) {
		// description
		// a meta-model operation that overwrites the description of this attribute
		// requires
		// none
		// guarantees
		// the attribute description is overwritten with aDescription
		description = aDescription;
	}

	public void setRange(Range aRange) {
		// description
		// a meta-model operation to assign the Range
		// requires
		// none
		// guarantees
		// definedRange has been overwritten with aRange
		definedRange = aRange;
	}

	// Private methods

	// none

	// ******MODEL COMPILER******
	// Everything from here down is specific to the model compiler itself

	public void ruleDefineInstVarAsPublic() {
		ruleDefineInstVar(false);
	}

	public void ruleDefineInstVarAsPrivate() {
		ruleDefineInstVar(true);
	}

	public void ruleDefineInstVar(boolean is_private) {
		// description
		// This rule emits the instance variable definition for an attribute
		//
		// Attribute.#DEFINE_INST_VAR -->
		// 'private ' + #PIM_OVERLAY_RTTYPE + this.instanceVariableName() +
		// if this attribute has a non blank PIM initial value
		// then ' = ' + PIM initial value +
		// ';'
		//
		// requires
		// attribute name is not null
		// guarantees
		// the instance variable declaration code for this attribute has been emitted
		if (is_private) {
			Context.codeOutput().print("_" + NameService.asInstanceLevelName(name));
		} else {
			Context.codeOutput().print(NameService.asInstanceLevelName(name));
		}
		Context.codeOutput().print(": " + definedRange.pIMRunTimeType());

		if (definedRange.pIMInitialValue() != "") {
			Context.codeOutput().print(" = " + definedRange.pIMInitialValue());
		}
		Context.codeOutput().println("");
	}

	public void ruleGetter() {
		// description
		// This rule emits the public getter for this attribute
		//
		// Attribute.#GETTER -->
		// '@property'
		// 'def ' + this.instanceVariableName() + '() ->' + #PIM_OVERLAY_RTTYPE + ':' +
		// ' return self._' + this.InstanceVariableName()
		//
		// requires
		// this is not a derived attribute
		// guarantees
		// the public instance variable getter code for this attribute has been emitted
		Context.codeOutput().indent();
		Context.codeOutput().println("@property");
		Context.codeOutput().print("def " + NameService.asInstanceLevelName(name) + "(self) -> ");
		Context.codeOutput().println(definedRange.pIMRunTimeType() + ":");
		if (Context.model().isVerbose()) {
			Context.codeOutput().indent();
			Context.codeOutput().indentMore();
			Context.codeOutput().println("# requires");
			Context.codeOutput().indent();
			Context.codeOutput().println("#   none");
			Context.codeOutput().indent();
			Context.codeOutput().println("# guarantees");
			Context.codeOutput().indent();
			Context.codeOutput().println("#   returns the " + name);
		}
		Context.codeOutput().indent();
		Context.codeOutput().println("return self._" + NameService.asInstanceLevelName(name) + "");
		Context.codeOutput().indentLess();
		Context.codeOutput().indent();

		Context.codeOutput().println("");
	}

	public void ruleInstVarInvariantsCheck() {
		// description
		// This rule emits an instance variable invariants check for an attribute, if
		// any
		//
		// Attribute.#INST_VAR_INVARIANTS_CHECK -->
		// 'assert ' + the substituted assertion expression +
		// ': "the value for attribute '" + name + "' is out of range";'
		//
		// requires
		// variable name not null
		// guarantees
		// the instance variable declaration code for this attribute has been emitted
		Context.codeOutput().indent();
		Context.codeOutput().print("assert ");
		Context.codeOutput().print(definedRange.pIMAssertionExpression(NameService.asInstanceLevelName(name)));
		Context.codeOutput().println(": \"the value for attribute '" + name + "' is out of range\";");
	}

}
