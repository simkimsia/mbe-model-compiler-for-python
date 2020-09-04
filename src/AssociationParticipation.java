
public class AssociationParticipation {
	// General description
	// Model compiler
	// Implements class Association Participation in the Meta-Model
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

	public static final String tagAssociationParticipationStart = "<associationparticipation>";

	// Static (class) variables

	// none

	// Static (class) methods

	public static AssociationParticipation parseAssociationParticipation() {
		// requires
		// Input model stream is open and ready to read at first line after
		// "<associationparticipation>"
		// Context.model() <> null
		// lower bound is parse-able ( normally '0' or '1', but could be some other
		// positive int )
		// upper bound is parse-able ( normally '1' or some other positive int. use '-1'
		// to mean many (*) )
		// guarantees
		// returns a populated instance of association participation (or null, if
		// nothing/error in model file)
		// and input model string is right after participation declaration
		String roleName = JALInput.nextLine();
		MMClass participatingClass = MMClass.classNamed(JALInput.nextLine());
		int lowerBound = Integer.parseInt(JALInput.nextLine());
		int upperBound = Integer.parseInt(JALInput.nextLine());
		return new AssociationParticipation(roleName, participatingClass, lowerBound, upperBound);
	}

	// Instance variables

	// Meta-model instance variables
	private String roleName;
	private MMClass participatingClass;
	private int lowerBound;
	private int upperBound;

	// PIM Overlay instance variables
	// none

	// Model compiler instance variables
	// none

	// Constructor(s)

	public AssociationParticipation(String aRoleName, MMClass aClass, int aLowerBound, int anUpperBound) {
		// description
		// default constructor
		// requires
		// aClass <> null
		// while not truly required (yet), aLowerBound should be <= anUpperBound
		// (beware, -1 == 'many')
		// guarantees
		// a new AssociationParticipation exists
		roleName = aRoleName;
		participatingClass = aClass;
		lowerBound = aLowerBound;
		upperBound = anUpperBound;
	}

	// Accessors

	public String roleName() {
		// description
		// a meta-model operation that returns the role name for this Association
		// Participation
		// requires
		// none
		// guarantees
		// the role name is returned
		return roleName;
	}

	public MMClass participatingClass() {
		// description
		// a meta-model operation that returns the participating class for this
		// Association Participation
		// requires
		// none
		// guarantees
		// the participating class is returned
		return participatingClass;
	}

	public int lowerBound() {
		// description
		// a meta-model operation that returns the lower bound for this Association
		// Participation
		// requires
		// none
		// guarantees
		// returns lower bound
		return lowerBound;
	}

	public boolean isLowerBoundZero() {
		// description
		// a meta-model operation that tells whether the lower bound for this
		// Association Participation is zero
		// requires
		// none
		// guarantees
		// returns true when the lower bound is zero, otherwise false
		return lowerBound == 0;
	}

	public int upperBound() {
		// description
		// a meta-model operation that returns the upper bound for this Association
		// Participation
		// requires
		// none
		// guarantees
		// upper bound is returned (remember about -1 meaning 'many')
		return upperBound;
	}

	public boolean isUpperBoundMany() {
		// description
		// a meta-model operation that tells whether the upper bound for this
		// Association Participation is many
		// requires
		// none
		// guarantees
		// returns true when the upper bound is many, otherwise false
		return upperBound != 1;
	}

	// Modifiers

	public void setRoleName(String aRoleName) {
		// description
		// a meta-model operation that overwrites the role name for this Association
		// Participation
		// requires
		// aRoleName not null (and unique among roles in other class???)
		// guarantees
		// roleName has been set to the designated new role name
		roleName = aRoleName;
	}

	public void setLowerBound(int aLowerBound) {
		// description
		// a meta-model operation that overwrites the lower bound for this Association
		// Participation
		// requires
		// aLowerBound == 0, 1, or some other positive int
		// guarantees
		// lowerBound has been set to the designated new lower bound
		lowerBound = aLowerBound;
	}

	public void setUpperBound(int anUpperBound) {
		// description
		// a meta-model operation that overwrites the upper bound for this Association
		// Participation
		// requires
		// anUpperBound == 1, some other positive int, or -1 to mean 'many'
		// guarantees
		// upperBound has been set to the designated new upper bound
		upperBound = anUpperBound;
	}

	// Private methods

	// none

	// ******MODEL COMPILER******
	// Everything from here down is specific to the model compiler itself

	public void ruleDeclareParticipationInstVar() {
		// description
		// This rule emits the private instance variable for an association
		// participation
		//
		// AssociationParticipation.#DECLARE_PARTICIPATION_INST_VAR -->
		// if this is -to-at-most-one
		// then 'private' +
		// otherwise 'private ArrayList<' +
		// formatted participating class name +
		// if this is -to-at-most-one
		// then ' ' +
		// otherwise '> '
		// this formatted participation role name + ';'
		//
		// requires
		// none
		// guarantees
		// the private inst var declaration has been emitted
		PythonOutput.indent();
		PythonOutput.print("def _");
		PythonOutput.print(NameService.asSnakeStyleName(participatingClass.name()));
		if (!this.isUpperBoundMany()) {
			PythonOutput.print("def ");
		} else {
			PythonOutput.print("private ArrayList<");
		}

		if (!this.isUpperBoundMany()) {
			PythonOutput.print(" ");
		} else {
			PythonOutput.print("> ");
		}
		PythonOutput.println(NameService.asInstanceLevelName(roleName) + ";");
	}

	public void ruleInitializeParticipationInstVar() {
		// description
		// This rule emits the instance variable initialization for an association
		// participation
		//
		// AssociationParticipation.#INITIALIZE_PARTICIPATION_INST_VAR -->
		// formatted participation role name + ' = '
		// if this is -to-at-most-one
		// then 'null;' +
		// otherwise 'new ArrayList<' + participating class name + '>();'
		//
		// requires
		// none
		// guarantees
		// the private inst var declaration has been emitted
		PythonOutput.print(NameService.asInstanceLevelName(roleName));
		if (!this.isUpperBoundMany()) {
			PythonOutput.println(" = null;");
		} else {
			PythonOutput.print(" = new ArrayList<");
			PythonOutput.print(NameService.asClassLevelName(participatingClass.name()));
			PythonOutput.println(">();");
		}
		PythonOutput.indent();
	}

	public void ruleParticipationInvariantsCheck() {
		// description
		// This rule emits the participation invariants check for an association
		// participation
		//
		// AssociationParticipation.#PARTICIPATION_INVARIANTS_CHECK -->
		// if( lower bound is 1 or greater ) {
		// 'assert ' + <participation role name> +
		// if( upper bound is 1 ) {
		// ' != null: the multiplicity for association role ' + participation role name
		// + ' is out of range (=0);'
		// } else {
		// '.size() >= ' + lower bound +
		// ': "the multiplicity for association role ' + participation role name +
		// ' is out of range (<' + lower bound + ');'
		// }
		// }
		// if( upper bound > 1 ) {
		// 'assert ' + <participation role name> + '.size() <= ' + upper bound +
		// ': "the multiplicity for association role ' + participation role name +
		// ' is out of range (>' + upper bound + ')";'
		// }
		//
		// requires
		// none
		// guarantees
		// the participation invariants check has been emitted
		if (lowerBound > 0) {
			PythonOutput.indent();
			PythonOutput.print("assert " + NameService.asInstanceLevelName(roleName));
			if (upperBound == 1) {
				PythonOutput.println(
						" != null: \"the multiplicity for association role '" + roleName + "' is out of range (=0)\";");
			} else {
				PythonOutput
						.print(".size() >= " + lowerBound + ": \"the multiplicity for association role '" + roleName);
				PythonOutput.println("' is out of range (<" + lowerBound + ")\";");
			}
		}
		if (upperBound > 1) {
			PythonOutput.indent();
			PythonOutput.print("assert " + NameService.asInstanceLevelName(roleName));
			PythonOutput.print(".size() <= " + upperBound + ": \"the multiplicity for association role '" + roleName);
			PythonOutput.println("' is out of range (>" + upperBound + ")\";");
		}
	}

	public void ruleBinaryNonReflexiveParticipationReferenceCheck() {
		// description
		// This rule emits the participation reference check for a binary non-reflexive
		// association participation
		//
		// AssociationParticipation.#BINARY_NON_REFLEXIVE_PARTICIPATION_REFERENCE_CHECK
		// -->
		// if upper bound == 1
		// if lower bound != 1
		// 'if( ' + role name + ' != null ) {'
		// participation role name + '.' + other role name + 'ReferenceCheck( this );'
		// if lower bound != 1
		// '}'
		// otherwise
		// 'for( ' + <thatClass> + 'a' + <thatClass> + ': ' + role name + ') {'
		// 'a' + <thatClass> + '.' + other role name + 'ReferenceCheck( this );'
		// '}'
		//
		// requires
		// none
		// guarantees
		// the participation reference check has been emitted
		MMClass theClass = Context.associationParticipation().participatingClass();
		String otherRoleName;
		if (Context.association().participationSet().get(0) != this) {
			otherRoleName = Context.association().participationSet().get(0).roleName();
		} else {
			otherRoleName = Context.association().participationSet().get(1).roleName();
		}
		if (upperBound == 1) {
			if (lowerBound != 1) {
				PythonOutput.indent();
				PythonOutput.println("if( " + NameService.asInstanceLevelName(roleName) + " != null ) {");
				PythonOutput.indentMore();
			}
			PythonOutput.indent();
			PythonOutput.println(NameService.asInstanceLevelName(roleName) + "."
					+ NameService.asInstanceLevelName(otherRoleName) + "ReferenceCheck( this );");
			if (lowerBound != 1) {
				PythonOutput.indentLess();
				PythonOutput.indent();

			}
		} else {
			PythonOutput.indent();
			PythonOutput.println("for( " + NameService.asClassLevelName(theClass.name()) + " a"
					+ NameService.asClassLevelName(theClass.name()) + ": " + NameService.asInstanceLevelName(roleName)
					+ " ) {");
			PythonOutput.indentMore();
			PythonOutput.indent();
			PythonOutput.println("a" + NameService.asClassLevelName(theClass.name()) + "."
					+ NameService.asInstanceLevelName(otherRoleName) + "ReferenceCheck( this );");
			PythonOutput.indentLess();
			PythonOutput.indent();

		}
	}

	public void ruleBinaryReflexiveParticipationReferenceCheck() {
		// description
		// This rule emits the participation reference check for a binary reflexive
		// association participation
		//
		// AssociationParticipation.#BINARY_REFLEXIVE_PARTICIPATION_REFERENCE_CHECK -->
		// if upper bound == 1
		// if lower bound != 1
		// 'if( ' + role name + ' != null ) {'
		// role name + '.' + other role name + 'ReferenceCheck( this );'
		// if lower bound != 1
		// '}'
		// otherwise
		// 'for( ' + <thatClass> + 'a' + <thatClass> + ': ' + role name + ') {'
		// 'a' + <thatClass> + '.' + other role name + 'ReferenceCheck( this );'
		// '}'
		//
		// requires
		// none
		// guarantees
		// the participation reference check has been emitted
		MMClass theClass = Context.associationParticipation().participatingClass();
		String otherRoleName = Context.associationParticipation().roleName();
		if (upperBound == 1) {
			if (lowerBound != 1) {
				PythonOutput.indent();
				PythonOutput.println("if( " + NameService.asInstanceLevelName(roleName) + " != null ) {");
				PythonOutput.indentMore();
			}
			PythonOutput.indent();
			PythonOutput.println(NameService.asInstanceLevelName(roleName) + "."
					+ NameService.asInstanceLevelName(otherRoleName) + "ReferenceCheck( this );");
			if (lowerBound != 1) {
				PythonOutput.indentLess();
				PythonOutput.indent();

			}
		} else {
			PythonOutput.indent();
			PythonOutput.println("for( " + NameService.asClassLevelName(theClass.name()) + " a"
					+ NameService.asClassLevelName(theClass.name()) + ": " + NameService.asInstanceLevelName(roleName)
					+ " ) {");
			PythonOutput.indentMore();
			PythonOutput.indent();
			PythonOutput.println("a" + NameService.asClassLevelName(theClass.name()) + "."
					+ NameService.asInstanceLevelName(otherRoleName) + "ReferenceCheck( this );");
			PythonOutput.indentLess();
			PythonOutput.indent();

		}
	}

	public void ruleImplementReferenceCheck() {
		// description
		// This rule emits the participation reference check for an association
		// participation
		//
		// AssociationParticipation.#IMPLEMENT_REFERENCE_CHECK -->
		// 'public void ' + role name + 'ReferenceCheck( ' + <thatClass> + ' a' +
		// <thatClass> + ' ) {'
		// if is verbose
		// '// requires'
		// '// a' + <thatClass> + ' != null'
		// '// guarantees'
		// '// assertion failure when this object does not have a reference back to the
		// calling object'
		// 'assert ' + participation role name +
		// if upperBound == 1
		// ' == a' + <thatClass> +
		// otherwise
		// '.indexOf( a' + <thatClass> + ' ) != -1' +
		// ': "this object does not have a reference ...";'
		// '}'
		//
		// requires
		// none
		// guarantees
		// the participation reference check has been emitted
		PythonOutput.indent();
		PythonOutput.print("public void " + NameService.asInstanceLevelName(roleName) + "ReferenceCheck( "
				+ NameService.asClassLevelName(participatingClass().name()));
		PythonOutput.println(" a" + NameService.asClassLevelName(participatingClass().name()) + " ) {");
		if (Context.model().isVerbose()) {
			PythonOutput.indent();
			PythonOutput.println("# requires");
			PythonOutput.indent();
			PythonOutput.println("#   a" + NameService.asClassLevelName(participatingClass().name()) + " != null");
			PythonOutput.indent();
			PythonOutput.println("# guarantees");
			PythonOutput.indent();
			PythonOutput.println(
					"#   assertion failure when this object does not have a reference back to the calling object");
		}
		PythonOutput.indentMore();
		PythonOutput.indent();
		PythonOutput.print("assert " + NameService.asInstanceLevelName(roleName));
		if (upperBound == 1) {
			PythonOutput.print(" == a" + NameService.asClassLevelName(participatingClass().name()));
		} else {
			PythonOutput.print(".indexOf( a" + NameService.asClassLevelName(participatingClass().name()) + " ) != -1 ");
		}
		PythonOutput.println(": \"this object does not have a reference back to the calling object\";");
		PythonOutput.indentLess();
		PythonOutput.indent();

		PythonOutput.println("");
	}

	public void rulePublicLinkService() {
		// description
		// This rule emits the public link service operation for an association
		// participation
		// this rule looks messy because of all of the generated names floating around,
		// however
		// the code it actually produces is reasonably straightforward
		//
		// AssociationParticipation.#PUBLIC_LINK_SERVICE -->
		// 'private void link' + other participation role name +
		// '( ' + participating class name + ' a' + participating class name + ' ) {' +
		// '// requires' +
		// '// a' + participating class name + ' <> null' +
		// '// guarantees
		// '// both this and a' + participating class name + ' are linked to each other'
		// +
		// role name +
		// if this is -at-most-to-one
		// then ' = a' + participating class name + ';'
		// else '.add( ' + participating class name + ' );'
		// "}'
		//
		// requires
		// Context.class() <> null (the class we're doing it all on)
		// Context.association() <> null (the association being worked with)
		// Context.associationParticipation() <> null (the association participation
		// being worked with)
		// guarantees
		// the public link service been emitted
		PythonOutput.indent();
		String otherRoleName = Context.associationParticipation().roleName();
		PythonOutput.print("public void link" + NameService.asClassLevelName(otherRoleName));
		PythonOutput.print("( " + NameService.asClassLevelName(participatingClass.name()));
		PythonOutput.println(" a" + NameService.asClassLevelName(participatingClass.name()) + " ) {");
		if (Context.model().isVerbose()) {
			PythonOutput.indent();
			PythonOutput.println("# requires");
			PythonOutput.indent();
			PythonOutput.println("//   a" + NameService.asClassLevelName(participatingClass.name()) + " <> null");
			PythonOutput.indent();
			PythonOutput.println("# guarantees");
			PythonOutput.indent();
			PythonOutput.println("//   both this and a" + NameService.asClassLevelName(participatingClass.name())
					+ " are linked to each other");
		}
		PythonOutput.indentMore();
		if (Context.model().isAssertionsOn()) {
			PythonOutput.indent();
			PythonOutput.println("assert( a" + NameService.asClassLevelName(participatingClass.name()) + " != null );");
		}
		PythonOutput.indent();
		PythonOutput.print(NameService.asInstanceLevelName(roleName));
		if (!this.isUpperBoundMany()) {
			PythonOutput.println(" = a" + NameService.asClassLevelName(participatingClass.name()) + ";");
		} else {
			PythonOutput.print(".add( a");
			PythonOutput.print(NameService.asClassLevelName(participatingClass.name()));
			PythonOutput.println(" );");
		}
		PythonOutput.indentLess();
		PythonOutput.indent();

		PythonOutput.println("");
	}

	public void rulePublicUnlinkService() {
		// description
		// This rule emits the public unlink service operation for an association
		// participation
		// this rule looks messy because of all of the generated names floating around,
		// however
		// the code it actually produces is reasonably straightforward
		//
		// AssociationParticipation.#PUBLIC_UNLINK_SERVICE -->
		// 'private void link' + other participation role name +
		// '( ' + participating class name + ' a' + participating class name + ' ) {' +
		// '// requires' +
		// '// a' + participating class name + ' <> null' +
		// '// guarantees
		// '// both this and a' + participating class name + ' are linked to each other'
		// +
		// if this is -at-most-to-one
		// then role name + ' = null;'
		// else 'int index = ' + role name + '.indexOf( a' + participating class name +
		// ' );'
		// 'if( index != -1 ) {'
		// role name + '.remove( index );'
		// '}'
		// "}'
		//
		// requires
		// Context.class() <> null (the class we're doing it all on)
		// Context.association() <> null (the association being worked with)
		// Context.associationParticipation() <> null (the association participation
		// being worked with)
		// guarantees
		// the public unlink service been emitted
		PythonOutput.indent();
		String otherRoleName = Context.associationParticipation().roleName();
		PythonOutput.print("public void unlink" + NameService.asClassLevelName(otherRoleName));
		PythonOutput.print("( " + NameService.asClassLevelName(participatingClass.name()));
		PythonOutput.println(" a" + NameService.asClassLevelName(participatingClass.name()) + " ) {");
		if (Context.model().isVerbose()) {
			PythonOutput.indent();
			PythonOutput.println("# requires");
			PythonOutput.indent();
			PythonOutput.println("//   a" + NameService.asClassLevelName(participatingClass.name()) + " <> null");
			PythonOutput.indent();
			PythonOutput.println("# guarantees");
			PythonOutput.indent();
			PythonOutput.println(
					"//   this and a" + NameService.asClassLevelName(participatingClass.name()) + " are unlinked");
		}
		PythonOutput.indentMore();
		if (Context.model().isAssertionsOn()) {
			PythonOutput.indent();
			PythonOutput.println("assert( a" + NameService.asClassLevelName(participatingClass.name()) + " != null );");
		}
		if (!this.isUpperBoundMany()) {
			PythonOutput.indent();
			PythonOutput.println(NameService.asInstanceLevelName(roleName) + " = null;");
		} else {
			PythonOutput.indent();
			PythonOutput.print("int index = " + NameService.asInstanceLevelName(roleName));
			PythonOutput.println(".indexOf( a" + NameService.asClassLevelName(participatingClass.name()) + " );");
			PythonOutput.indent();
			PythonOutput.println("if( index != -1 ) {");
			PythonOutput.indentMore();
			PythonOutput.indent();
			PythonOutput.println(NameService.asInstanceLevelName(roleName) + ".remove( index );");
			PythonOutput.indentLess();
			PythonOutput.indent();

		}
		PythonOutput.indentLess();
		PythonOutput.indent();

		PythonOutput.println("");
	}

}
