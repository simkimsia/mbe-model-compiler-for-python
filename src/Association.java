import java.util.ArrayList;

public class Association {
	// General description
	// Model compiler
	// Implements class Association in the Meta-Model
	// Invariants
	// none
	// Implementation notes
	// none
	// Application notes
	// TBD-For now, this version does not support association classes.
	// TBD-For now, this version also does not support n-ary associations: just
	// binary
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

	public static String tagAssociationStart = "<association>";
	public static String tagAssociationEnd = "</association>";

	// Static (class) variables

	private static ArrayList<Association> associationSet = new ArrayList<Association>();

	// Static (class) methods

	public static Association parseAssociation() {
		// requires
		// Input model stream is open and ready to read first line after "<association>"
		// guarantees
		// returns a populated instance of association (or null, if nothing/error in
		// model file)
		// and input model string is right after "</association>"
		Association newAssociation = new Association(JALInput.nextLine());
		Context.setAssociation(newAssociation);

		// check if next line is association description
		String line = JALInput.nextLine();
		if (line.contains(NameService.tagDescriptionStart)) {
			newAssociation.setDescription(JALInput.nextLine());
			line = JALInput.nextLine();
		}

		// now look for association participations
		while (!line.contains(Association.tagAssociationEnd)) {
			if (line.contains(AssociationParticipation.tagAssociationParticipationStart)) {
				AssociationParticipation aParticipation = AssociationParticipation.parseAssociationParticipation();
				newAssociation.addAssociationParticipation(aParticipation);
			}
			line = JALInput.nextLine();
		}
		Context.clearAssociation();
		return newAssociation;
	}

	public static ArrayList<Association> allAssociations() {
		// requires
		// none
		// guarantees
		// returns (reference to) ArrayList<> of all associations
		return associationSet;
	}

	public static Association associationNamed(String anAssociationName) {
		// description
		// a meta-model operation that tries to find an association named the same as
		// anAssociationName
		// requires
		// none
		// guarantees
		// (a reference to) the association with the same name as anAssociationName has
		// been returned
		// returns null if it doesn't find one...
		Association theNamedAssociation = null;
		for (Association anAssociation : associationSet) {
			if (anAssociation.name().equals(anAssociationName)) {
				theNamedAssociation = anAssociation;
			}
		}
		return theNamedAssociation;
	}

	// Instance variables

	// Meta-model instance variables
	private String name;
	private String description;
	private ArrayList<AssociationParticipation> associationParticipationSet;

	// PIM Overlay instance variables
	// none

	// Model compiler instance variables
	// none

	// Constructor(s)

	public Association(String anAssociationName) {
		// description
		// default constructor
		// requires
		// anAssociationName is unique among all associations between the participating
		// classes
		// guarantees
		// a new Association exists
		name = anAssociationName;
		description = "none";
		associationParticipationSet = new ArrayList<AssociationParticipation>();
		associationSet.add(this);
	}

	// Accessors

	public String name() {
		// description
		// a meta-model operation that returns the name for this Association
		// requires
		// none
		// guarantees
		// the plain as-in-the-model name is returned
		return name;
	}

	public String description() {
		// description
		// a meta-model operation that returns the description for this Association
		// requires
		// none
		// guarantees
		// the description is returned
		return description;
	}

	public boolean involvesClass(MMClass aClass) {
		// description
		// a meta-model operation that tells whether this association involves aClass
		// requires
		// aClass <> null
		// guarantees
		// returns true only when aClass participates in this association
		boolean participatesIn = false;
		for (AssociationParticipation aParticipation : associationParticipationSet) {
			participatesIn = participatesIn || (aParticipation.participatingClass() == aClass);
		}
		return participatesIn;
	}

	public boolean isBinaryAndReflexive() {
		// description
		// a meta-model operation that tells whether this association is binary and
		// reflexive
		// requires
		// none
		// guarantees
		// returns true only when this association is reflexive
		boolean binaryAndReflexive = associationParticipationSet.size() == 2;
		if (binaryAndReflexive) {
			AssociationParticipation participation1 = associationParticipationSet.get(0);
			AssociationParticipation participation2 = associationParticipationSet.get(1);
			binaryAndReflexive = participation1.participatingClass() == participation2.participatingClass();
		}
		return binaryAndReflexive;
	}

	public ArrayList<AssociationParticipation> participationSet() {
		// description
		// a meta-model operation that provides the set of association participations
		// requires
		// none
		// guarantees
		// returns the association participation set
		return associationParticipationSet;
	}

	// Modifiers

	public void setDescription(String aDescription) {
		// description
		// a meta-model operation that sets the description for this Association
		// requires
		// none
		// guarantees
		// the description has been set to aDescription
		description = aDescription;
	}

	public void addAssociationParticipation(AssociationParticipation anAssociationParticipation) {
		// description
		// a meta-model operation that adds another class as being a participant in this
		// Association
		// requires
		// anAssociationParticipation <> null
		// guarantees
		// the participation for that class has been added
		associationParticipationSet.add(anAssociationParticipation);
	}

	public void removeAssociationParticipation(AssociationParticipation anAssociationParticipation) {
		// description
		// a meta-model operation that removes an (presumably) existing participation
		// from this association
		// requires
		// none
		// guarantees
		// the given participation will have been removed if it was present
		int index = associationParticipationSet.indexOf(anAssociationParticipation);
		if (index != -1) {
			associationParticipationSet.remove(index);
		}
	}

	// Private methods

	// none

	// ******MODEL COMPILER******
	// Everything from here down is specific to the model compiler itself

	public void ruleDeclareAssociationInstVar() {
		// description
		// This rule emits the instance variable declaration for an association that
		// some class participates in
		// TBD: FOR NOW, THIS ONLY HANDLES BINARY ASSOCIATIONS!!!
		//
		// Association.#DECLARE_ASSOCIATION_INST_VAR -->
		// if( this association is reflexive )
		// then #DECLARE_BINARY_REFLEXIVE_ASSOCIATION_INST_VARS
		// else #DECLARE_BINARY_NON_REFLEXIVE_ASSOCIATION_INST_VAR
		//
		// requires
		// Context.class() <> null
		// guarantees
		// the instance variable definition has been emitted, considering multiplicity
		// to the other participating class
		Context.setAssociation(this);
		if (this.isBinaryAndReflexive()) {
			this.ruleDeclareBinaryReflexiveAssociationInstVars();
		} else {
			this.ruleDeclareBinaryNonReflexiveAssociationInstVar();
		}
		Context.clearAssociation();
	}

	public void ruleDeclareBinaryReflexiveAssociationInstVars() {
		// description
		// This rule emits instance variable declarations for a binary reflexive
		// association that some class participates in
		//
		// Association.#DECLARE_BINARY_REFLEXIVE_ASSOCIATION_INST_VARS -->
		// participation1.#DECLARE_PARTICIPATION_INST_VAR
		// participation2.#DECLARE_PARTICIPATION_INST_VAR
		//
		// requires
		// Association being compiled is binary and reflexive
		// guarantees
		// the instance variable definition has been emitted, considering multiplicity
		// to the other participating class
		AssociationParticipation participant = associationParticipationSet.get(0);
		participant.ruleDeclareParticipationInstVar();
		participant = associationParticipationSet.get(1);
		participant.ruleDeclareParticipationInstVar();
	}

	public void ruleDeclareBinaryNonReflexiveAssociationInstVar() {
		// description
		// This rule emits instance variable declarations for a binary non-reflexive
		// association that some class participates in
		//
		// Association.#DECLARE_BINARY_NON_REFLEXIVE_ASSOCIATION_INST_VARS -->
		// if participation1 is not for Context.Class
		// then participation1.#DECLARE_PARTICIPATION_INST_VAR
		// else participation2.#DECLARE_PARTICIPATION_INST_VAR
		//
		// requires
		// Context.class() <> null
		// Association being compiled is binary and not reflexive association
		// guarantees
		// the instance variable definition has been emitted, considering multiplicity
		// to the other participating class
		AssociationParticipation participant = associationParticipationSet.get(0);
		if (participant.participatingClass() != Context.mMClass()) {
			participant.ruleDeclareParticipationInstVar();
		} else {
			participant = associationParticipationSet.get(1);
			participant.ruleDeclareParticipationInstVar();
		}
	}

	public void ruleInitializeAssociationInstVar() {
		// description
		// This rule emits instance variable initialization for an association that some
		// class participates in
		// this is embedded into the constructor, right after the contract and going-in
		// assertions
		// TBD: ONLY HANDLES BINARY ASSOCIATIONS FOR NOW!!!
		//
		// Association.#INITIALIZE_ASSOCIATION_INST_VAR -->
		// if( this association is reflexive )
		// then #INITIALIZE_BINARY_REFLEXIVE_ASSOCIATION_INST_VARS
		// else #INITIALIZE_BINARY_NON_REFLEXIVE_ASSOCIATION_INST_VAR
		//
		// requires
		// Context.class() <> null
		// guarantees
		// the instance variable definition has been emitted, considering multiplicity
		// to the other participating class
		Context.setAssociation(this);
		if (this.isBinaryAndReflexive()) {
			this.ruleInitializeBinaryReflexiveAssociationInstVars();
		} else {
			this.ruleInitializeBinaryNonReflexiveAssociationInstVar();
		}
		Context.clearAssociation();
	}

	public void ruleInitializeBinaryReflexiveAssociationInstVars() {
		// description
		// This rule emits instance variable declarations for a binary reflexive
		// association that some class participates in
		//
		// Association.#INITIALIZE_BINARY_REFLEXIVE_ASSOCIATION_INST_VARS -->
		// participation1.#INITIALIZE_PARTICIPATION_INST_VAR
		// participation2.#INITIALIZE_PARTICIPATION_INST_VAR
		//
		// requires
		// Association being compiled is binary and reflexive
		// guarantees
		// the instance variable definition has been emitted, considering multiplicity
		// to the other participating class
		AssociationParticipation participant = associationParticipationSet.get(0);
		participant.ruleInitializeParticipationInstVar();
		participant = associationParticipationSet.get(1);
		participant.ruleInitializeParticipationInstVar();
	}

	public void ruleInitializeBinaryNonReflexiveAssociationInstVar() {
		// description
		// This rule emits instance variable declarations for a binary non-reflexive
		// association that some class participates in
		//
		// Association.#INITIALIZE_BINARY_NON_REFLEXIVE_ASSOCIATION_INST_VARS -->
		// if participation1 is not for Context.Class
		// then participation1.#INITIALIZE_PARTICIPATION_INST_VAR
		// else participation2.#INITIALIZE_PARTICIPATION_INST_VAR
		//
		// requires
		// Context.class() <> null
		// Association being compiled is binary and not reflexive association
		// guarantees
		// the instance variable definition has been emitted, considering multiplicity
		// to the other participating class
		AssociationParticipation participant = associationParticipationSet.get(0);
		if (participant.participatingClass() != Context.mMClass()) {
			participant.ruleInitializeParticipationInstVar();
		} else {
			participant = associationParticipationSet.get(1);
			participant.ruleInitializeParticipationInstVar();
		}
	}

	public void ruleAssociationInvariantsCheck() {
		// description
		// This rule emits the invariants check for an association that some class
		// participates in
		// TBD: ONLY HANDLES BINARY ASSOCIATIONS FOR NOW!!!
		//
		// Association.#ASSOCIATION_INVARIANTS_CHECK -->
		// if this association is reflexive
		// then #BINARY_REFLEXIVE_ASSOCIATION_INVARIANTS_CHECK
		// else #BINARY_NON_REFLEXIVE_ASSOCIATION_INVARIANTS_CHECK
		//
		// requires
		// Context.class() <> null
		// guarantees
		// the invariants check code has been emitted
		Context.setAssociation(this);
		if (this.isBinaryAndReflexive()) {
			this.ruleBinaryReflexiveAssociationInvariantsCheck();
		} else {
			this.ruleBinaryNonReflexiveAssociationInvariantsCheck();
		}
		Context.clearAssociation();
	}

	public void ruleBinaryReflexiveAssociationInvariantsCheck() {
		// description
		// This rule emits the invariants check for a binary non-reflexive association
		// that some class participates in
		//
		// Association.#BINARY_REFLEXIVE_ASSOCIATION_INVARIANTS_CHECK -->
		// participation1.#PARTICIPATION_INVARIANTS_CHECK
		// participation2.#PARTICIPATION_INVARIANTS_CHECK
		//
		// requires
		// Context.class() <> null
		// Association being compiled is binary and not reflexive association
		// guarantees
		// the invariants check has been emitted
		AssociationParticipation participant = associationParticipationSet.get(0);
		participant.ruleParticipationInvariantsCheck();
		participant = associationParticipationSet.get(1);
		participant.ruleParticipationInvariantsCheck();
	}

	public void ruleBinaryNonReflexiveAssociationInvariantsCheck() {
		// description
		// This rule emits the invariants check for a binary non-reflexive association
		// that some class participates in
		//
		// Association.#BINARY_NON_REFLEXIVE_ASSOCIATION_INVARIANTS_CHECK -->
		// if participant1 is not for Context.Class
		// then participant1.#PARTICIPATION_INVARIANTS_CHECK
		// else participant2.#PARTICIPATION_INVARIANTS_CHECK
		//
		// requires
		// Context.class() <> null
		// Association being compiled is binary and not reflexive association
		// guarantees
		// the invariants check has been emitted
		AssociationParticipation participant = associationParticipationSet.get(0);
		if (participant.participatingClass() != Context.mMClass()) {
			participant.ruleParticipationInvariantsCheck();
		} else {
			participant = associationParticipationSet.get(1);
			participant.ruleParticipationInvariantsCheck();
		}
	}

	public void ruleAssociationReferenceCheck() {
		// description
		// This rule emits the reference check for an association that some class
		// participates in
		//
		// Association.#REFERENCE_CHECK -->
		// if( this association is reflexive )
		// then #BINARY_REFLEXIVE_ASSOCIATION_REFRENCE_CHECK
		// else #BINARY_NON_REFLEXIVE_ASSOCIATION_REFERENCE_CHECK
		//
		// requires
		// Context.class() <> null
		// guarantees
		// the reference check code has been emitted
		Context.setAssociation(this);
		if (this.isBinaryAndReflexive()) {
			this.ruleBinaryReflexiveAssociationReferenceCheck();
		} else {
			this.ruleBinaryNonReflexiveAssociationReferenceCheck();
		}
		Context.clearAssociation();
	}

	public void ruleBinaryReflexiveAssociationReferenceCheck() {
		// description
		// This rule emits the reference check for a binary reflexive association that
		// some class participates in
		//
		// Association.#BINARY_REFLEXIVE_ASSOCIATION_REFERENCE_CHECK -->
		// participation1.#BINARY_REFLEXIVE_PARTICIPATION_REFERENCE_CHECK
		// participation2.#BINARY_REFLEXIVE_PARTICIPATION_REFERENCE_CHECK
		//
		// requires
		// Context.class() <> null
		// Association being compiled is binary and not reflexive association
		// guarantees
		// the reference check has been emitted
		AssociationParticipation participation1 = associationParticipationSet.get(0);
		AssociationParticipation participation2 = associationParticipationSet.get(1);
		Context.setAssociationParticipation(participation2);
		participation1.ruleBinaryReflexiveParticipationReferenceCheck();
		Context.clearAssociationParticipation();
		Context.setAssociationParticipation(participation1);
		participation2.ruleBinaryReflexiveParticipationReferenceCheck();
		Context.clearAssociationParticipation();
	}

	public void ruleBinaryNonReflexiveAssociationReferenceCheck() {
		// description
		// This rule emits the reference check for a binary non-reflexive association
		// that some class participates in
		//
		// Association.#BINARY_NON_REFLEXIVE_ASSOCIATION_REFERENCE_CHECK -->
		// if participation1 is not for Context.Class
		// then participation1.#BINARY_NON_REFLEXIVE_PARTICIPATION_REFERENCE_CHECK
		// else participation2.#BINARY_NON_REFLEXIVE_PARTICIPATION_REFERENCE_CHECK
		//
		// requires
		// Context.class() <> null
		// Association being compiled is binary and not reflexive association
		// guarantees
		// the reference check has been emitted
		AssociationParticipation participation1 = associationParticipationSet.get(0);
		AssociationParticipation participation2 = associationParticipationSet.get(1);
		if (participation1.participatingClass() == Context.mMClass()) {
			Context.setAssociationParticipation(participation2);
			participation2.ruleBinaryNonReflexiveParticipationReferenceCheck();
			Context.clearAssociationParticipation();
		} else {
			Context.setAssociationParticipation(participation1);
			participation1.ruleBinaryNonReflexiveParticipationReferenceCheck();
			Context.clearAssociationParticipation();
		}
	}

	public void ruleImplementAssociationReferenceCheck() {
		// description
		// This rule emits the reference check operation for an association that some
		// class participates in
		//
		// Association.#IMPLEMENT_ASSOCIATION_REFERENCE_CHECK -->
		// if this is binary and reflexive
		// then participation1.#IMPLEMENT_REFERENCE_CHECK
		// participation2.#IMPLEMENT_REFERENCE_CHECK
		// else if participation1 is for the context class
		// then participation2.#IMPLEMENT_REFERENCE_CHECK
		// else participation1.#IMPLEMENT_REFERENCE_CHECK
		//
		// requires
		// Context.class() <> null
		// guarantees
		// the reference check operation has been emitted
		AssociationParticipation participation1 = associationParticipationSet.get(0);
		AssociationParticipation participation2 = associationParticipationSet.get(1);
		Context.setAssociation(this);
		if (this.isBinaryAndReflexive()) {
			participation1.ruleImplementReferenceCheck();
			participation2.ruleImplementReferenceCheck();
		} else {
			if (participation1.participatingClass() == Context.mMClass()) {
				participation2.ruleImplementReferenceCheck();
			} else {
				participation1.ruleImplementReferenceCheck();
			}
		}
		Context.clearAssociation();
	}

	public void ruleLinkUnlinkServices() {
		// description
		// This rule emits the link & unlink services for an association that some class
		// participates in
		// TBD: ONLY HANDLES BINARY ASSOCIATIONS FOR NOW!!!
		//
		// Association.#LINK_UNLINK_SERVICES -->
		// if( this association is reflexive )
		// then #BINARY_REFLEXIVE_ASSOCIATION_LINK_UNLINK_SERVICES
		// else #BINARY_NON_REFLEXIVE_ASSOCIATION_LINK_UNLINK_SERVICES
		//
		// requires
		// Context.class() <> null
		// guarantees
		// the link & unlink services have been emitted
		Context.setAssociation(this);
		Context.codeOutput().indent();
		Context.codeOutput().println("// link and unlink services for: " + name);
		Context.codeOutput().println("");
		Context.codeOutput().indentMore();
		if (this.isBinaryAndReflexive()) {
			this.ruleBinaryReflexiveAssociationLinkUnlinkServices();
		} else {
			this.ruleBinaryNonReflexiveAssociationLinkUnlinkServices();
		}
		Context.codeOutput().indentLess();
		Context.clearAssociation();
	}

	public void ruleBinaryReflexiveAssociationLinkUnlinkServices() {
		// description
		// This rule emits link & unlink services for a binary reflexive association
		// that some class participates in
		//
		// Association.#BINARY_REFLEXIVE_ASSOCIATION_LINK_UNLINK_SERVICES -->
		// participation1.#PARTICIPATION_LINK_UNLINK_SERVICE
		// participation2.#PARTICIPATION_LINK_UNLINK_SERVICE
		//
		// requires
		// Association being compiled is binary and reflexive
		// guarantees
		// the link & unlink services been emitted
		AssociationParticipation participant = associationParticipationSet.get(0);
		Context.setAssociationParticipation(participant);
		participant.rulePublicLinkService();
		participant.rulePublicUnlinkService();
		Context.clearAssociationParticipation();
		participant = associationParticipationSet.get(1);
		Context.setAssociationParticipation(participant);
		participant.rulePublicLinkService();
		participant.rulePublicUnlinkService();
		Context.clearAssociationParticipation();
	}

	public void ruleBinaryNonReflexiveAssociationLinkUnlinkServices() {
		// description
		// This rule emits link & unlink services for a binary non-reflexive association
		// that some class participates in
		//
		// Association.#BINARY_NON_REFLEXIVE_ASSOCIATION_LINK_UNLINK_SERVICES -->
		// if participation1 is not for Context.Class
		// then participation1.#PARTICIPATION_LINK_UNLINK_SERVICE
		// else participation2.#PARTICIPATION_LINK_UNLINK_SERVICE
		//
		// requires
		// Context.class() <> null
		// Association being compiled is binary and not reflexive association
		// guarantees
		// the link & unlink services been emitted
		AssociationParticipation participation1 = associationParticipationSet.get(0);
		AssociationParticipation participation2 = associationParticipationSet.get(1);
		if (participation1.participatingClass() == Context.mMClass()) {
			Context.setAssociationParticipation(participation1);
			participation2.rulePublicLinkService();
			participation2.rulePublicUnlinkService();
			Context.clearAssociationParticipation();
		} else {
			Context.setAssociationParticipation(participation2);
			participation1.rulePublicLinkService();
			participation1.rulePublicUnlinkService();
			Context.clearAssociationParticipation();
		}
	}

}
