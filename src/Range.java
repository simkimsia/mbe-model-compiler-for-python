import java.util.ArrayList;

public class Range {
	// General description
	// Model compiler
	// This class implements class Range in the Meta-Model
	// Invariants
	// none
	// Implementation notes
	// FOR NOW: DOESN"T ADDRESS ENUMERATED RANGES YET (NEED TO DO THAT LATER)
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

	public static String tagRangeStart = "<range>";
	public static String tagSpanType = "<span>";
	public static String tagReferenceType = "<reference>";
	public static String tagRTType = "<rttype>";
	public static String tagRTTypeInitialValue = "<rttypeinitialvalue>";
	public static String tagRangeEnd = "</range>";
	public static String tagRangeAssertStart = "<assert>";

	// enumeration of the sub-types of Range
	// private enum RangeType { SPAN, ENUMERATION, REFERENCE, DERIVED,
	// UNCONSTRIAINED }; -- for full implementation later!
	private enum RangeType {
		SPAN, REFERENCE, DERIVED
	};

	// Static (class) variables

	private static ArrayList<Range> rangeSet = new ArrayList<Range>();

	// Static (class) methods

	public static Range parseRange() {
		// requires
		// Input model stream is open and ready to read at the first line after
		// "<range>"
		// guarantees
		// returns a populated instance of range (or null, if nothing/error in model
		// file)
		// and input model string is right after "</range>"
		Range newRange = new Range(JALInput.nextLine());
		// now look for modifiers: type, rt type, ...
		String line = JALInput.nextLine();
		while (!line.contains(Range.tagRangeEnd)) {
			if (line.contains(Range.tagSpanType)) {
				newRange.becomeSpan();
			}
			if (line.contains(Range.tagReferenceType)) {
				newRange.becomeReference(JALInput.nextLine());
			}
			if (line.contains(Range.tagRTType)) {
				newRange.setPIMRunTimeType(JALInput.nextLine());
			}
			if (line.contains(Range.tagRTTypeInitialValue)) {
				newRange.setPIMInitialValue(JALInput.nextLine());
			}
			if (line.contains(Range.tagRangeAssertStart)) {
				newRange.setPIMOverlayAssertionExpression(JALInput.nextLine());
			}
			line = JALInput.nextLine();
		}
		return newRange;
	}

	public static ArrayList<Range> allRanges() {
		// requires
		// none
		// guarantees
		// returns (reference to) ArrayList<> of all class members
		return rangeSet;
	}

	public static Range rangeNamed(String aRangeName) {
		// description
		// a meta-model operation that tries to find a range named the same as
		// aRangeName
		// requires
		// aRangeName is not null
		// guarantees
		// (a reference to) the Range with the same name as aRangeName has been returned
		// returns null if it doesn't find one with that name
		Range theNamedRange = null;
		for (Range aRange : rangeSet) {
			if (aRange.name().equals(aRangeName)) {
				theNamedRange = aRange;
			}
		}
		return theNamedRange;
	}

	// Instance variables

	// Meta-model instance variables
	private String name;

	// Range meta-model instance variables
	private String referencedSpecification;
	private RangeType rangeType;
	private String derivationPolicy;

	// PIM Overlay instance variables
	private String pIMRunTimeType;
	private String pIMInitialValue;
	private String pIMAssertExpression;
	private Action pIMDerivationImpl;

	// Model compiler instance variables
	// none

	// Constructor(s)

	public Range(String aName) {
		// description
		// default constructor
		// requires
		// aName <> null and is unique among all ranges of this model
		// guarantees
		// an instance has been created and initialized
		// defaults to a Span type of Range
		name = aName;
		rangeType = RangeType.SPAN;
		referencedSpecification = null;
		derivationPolicy = null;
		pIMRunTimeType = "*** Unspecified run-time type";
		pIMInitialValue = "";
		pIMDerivationImpl = null;
		pIMAssertExpression = null;
		rangeSet.add(this);
	}

	// Accessors

	public String name() {
		// description
		// a meta-model operation that returns the name of this range
		// requires
		// none
		// guarantees
		// the range name is returned as a string
		return name;
	}

	public boolean isSpan() {
		// description
		// a meta-model operation that returns whether this is a Span subtype of Range
		// requires
		// none
		// guarantees
		// returns true only when this is a Span subtype of Range
		return rangeType == RangeType.SPAN;
	}

	public boolean isReference() {
		// description
		// a meta-model operation that returns whether this is a Reference subtype of
		// Range
		// requires
		// none
		// guarantees
		// returns true only when this is a Reference subtype of Range
		return rangeType == RangeType.REFERENCE;
	}

	public boolean isDerived() {
		// description
		// a meta-model operation that returns whether this is a Derived subtype of
		// Range
		// requires
		// none
		// guarantees
		// returns true only when this is a Derived subtype of Range
		return rangeType == RangeType.DERIVED;
	}

	public String referencedSpec() {
		// description
		// a meta-model operation that returns the referenced spec for a Reference
		// subtype of range
		// requires
		// none
		// guarantees
		// returns referencedSpecification only when this is a Reference subtype of
		// Range
		// otherwise returns null
		if (rangeType == RangeType.REFERENCE) {
			return referencedSpecification;
		} else {
			return null;
		}
	}

	public String derivationPolicy() {
		// description
		// a meta-model operation that returns the derivation policy for a Derived
		// subtype of range
		// requires
		// none
		// guarantees
		// returns derivationPolicy only when this is a Derived subtype of Range
		// otherwise returns null
		if (rangeType == RangeType.DERIVED) {
			return derivationPolicy;
		} else {
			return null;
		}
	}

	public String pIMRunTimeType() {
		// description
		// a PIM Overlay operation that returns the declared run time type for this
		// range
		// requires
		// none
		// guarantees
		// returns declared run time type
		return pIMRunTimeType;
	}

	public String pIMInitialValue() {
		// description
		// a PIM Overlay operation that returns the declared initial value for this
		// range
		// requires
		// none
		// guarantees
		// returns declared run time type
		return pIMInitialValue;
	}

	public boolean isAssertable() {
		// description
		// a PIM Overlay operation that returns whether this range is assert-able or not
		// requires
		// none
		// guarantees
		// returns true only when pIMAssertExpression != null
		return pIMAssertExpression != null;
	}

	public String pIMAssertionExpression(String aName) {
		// description
		// a model compiler operation that returns the PIM Overlay assertion expression
		// with aName substituted in place of all occurrences of "$"
		// requires
		// none
		// guarantees
		// returns pIMAssertExpression, even when it is null
		return pIMAssertExpression.replace("$", aName);
	}

	public Action pIMDerivationCode() {
		// description
		// a PIM Overlay operation that returns the derivation policy for a Derived
		// subtype of range
		// requires
		// none
		// guarantees
		// returns derivationPolicy only when this is a Derived subtype of Range
		// otherwise returns null
		if (rangeType == RangeType.DERIVED) {
			return pIMDerivationImpl;
		} else {
			return null;
		}
	}

	// Modifiers

	public void becomeSpan() {
		// description
		// a meta-model operation that turns this Range into a Span subtype
		// requires
		// none
		// guarantees
		// if this is not already a Span subtype
		// then turns it into a Span,
		// otherwise does nothing
		if (!this.isSpan()) {
			referencedSpecification = null;
			derivationPolicy = null;
			pIMDerivationImpl = null;
			rangeType = RangeType.SPAN;
		}
	}

	public void becomeReference(String aReferencedSpecification) {
		// description
		// a meta-model operation that turns this Range into a Reference subtype
		// requires
		// none
		// guarantees
		// if this is not a Reference subtype
		// then turns it into a Reference,
		// otherwise does nothing
		if (!this.isReference()) {
			referencedSpecification = aReferencedSpecification;
			derivationPolicy = null;
			pIMDerivationImpl = null;
			rangeType = RangeType.REFERENCE;
		}
	}

	public void becomeDerived(String aDerivationPolicy) {
		// description
		// a meta-model operation that turns this Range into a Reference subtype
		// requires
		// none
		// guarantees
		// if this is not a Reference subtype
		// then turns it into a Reference
		// otherwise does nothing
		if (!this.isDerived()) {
			referencedSpecification = null;
			derivationPolicy = aDerivationPolicy;
			pIMDerivationImpl = null;
			rangeType = RangeType.DERIVED;
		}
	}

	public void setPIMRunTimeType(String anRTType) {
		// description
		// a PIM Overlay operation that overwrites the PIM Overlay RT-type with the
		// specified type
		// requires
		// aNewRTType <> null (or it will cause problems later)
		// guarantees
		// the PIM Overlay RT-type has been set to aNewRTType
		pIMRunTimeType = anRTType;
	}

	public void setPIMInitialValue(String anInitialValue) {
		// description
		// a PIM Overlay operation that overwrites the PIM Overlay RT-type initial value
		// with the specified value
		// requires
		// none (anInitialValue can be null)
		// guarantees
		// the PIM Overlay RT-type has been set to aNewRTType
		pIMInitialValue = anInitialValue;
	}

	public void setPIMDerivationCode(Action anAction) {
		// description
		// a PIM Overlay operation that sets the derivation action code for a Derived
		// subtype of range
		// requires
		// none
		// guarantees
		// this Derived Attribute's PIM derivation action has been set to anAction
		if (rangeType == RangeType.DERIVED) {
			pIMDerivationImpl = anAction;
		}
	}

	public void setPIMOverlayAssertionExpression(String anAssertionExpression) {
		// description
		// sets the PIM Overlay assertion expression
		// requires
		// none
		// guarantees
		// pIMAssertExpression has been overwritten with anAssertionExpression
		pIMAssertExpression = anAssertionExpression;
	}

	// Private methods

	// none

	// ******MODEL COMPILER******
	// Everything from here down is specific to the model compiler itself

	public void ruleInstVarInvariantsCheck() {
		// description
		// This rule emits an instance variable invariants check for a range, if any
		//
		// Range.#INST_VAR_INVARIANTS_CHECK -->
		// definedRange.#INST_VAR_INVARIANTS_CHECK
		//
		// requires
		// variable name not null
		// guarantees
		// the instance variable declaration code for this attribute has been emitted
		Context.codeOutput().indent();
		Context.codeOutput().print("DUMMY DUMMY--not implemented yet");
	}

}
