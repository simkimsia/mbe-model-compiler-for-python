
public class Context {
	// General description
	// Model compiler
	// This is a helper class to manage model compilation context
	// Invariants
	// none
	// Implementation notes
	// none
	// Application notes
	// I'm not happy with how tightly coupled this makes everything, but the
	// alternative
	// was to pass context parameters around on all of the rules and that seems even
	// uglier. So we'll run with this for now and see how it works out...
	// Outer rule just invokes any inner rules
	// Inner rule on a new-context thing should Context.set<Thing>( this )
	// deeper embedded rules can now refer to Context.<thing>() to get easy
	// reference
	// Inner rule needs to Context.clear<Thing>() on exit
	// Context.clear<Thing>() is important to avoid residual values causing problems
	// later
	// When there are no further/deeper embedded rules, should be safe to not set
	// context
	// Note: there are lots of violations of Law of Demeter on the getters, but hey,
	// that's life...:
	// Context.<thing>().doSomething()
	// Another alternative is to put context as static methods on specific classes:
	// Model.setContext();
	// Model.context();
	// Model.clearContext();
	// MMClass.setContext();
	// MMClass.context();
	// MMClass.clearContext();
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

	// Static (class) variables

	private static Model modelContext;
	private static MMClass mMClassContext;
	private static State stateContext;
	private static Event eventContext;
	private static Association associationContext;
	private static AssociationParticipation associationParticipationContext;
	private static CodeOutput codeOutputContext;
	private static String targetContext;

	// Static (class) methods

	public static Model model() {
		// description
		// returns the Model context
		// requires
		// none
		// guarantees
		// a reference to the Model being compiled is returned
		return modelContext;
	}

	public static MMClass mMClass() {
		// description
		// returns the class context
		// requires
		// none
		// guarantees
		// a reference to the MMClass being compiled is returned
		return mMClassContext;
	}

	public static State state() {
		// description
		// returns the state context
		// requires
		// none
		// guarantees
		// a reference to the state being compiled is returned
		return stateContext;
	}

	public static Event event() {
		// description
		// returns the event context
		// requires
		// none
		// guarantees
		// a reference to the event being compiled is returned
		return eventContext;
	}

	public static Association association() {
		// description
		// returns the association context
		// requires
		// none
		// guarantees
		// a reference to the association being compiled is returned
		return associationContext;
	}

	public static AssociationParticipation associationParticipation() {
		// description
		// returns the association participation context
		// requires
		// none
		// guarantees
		// a reference to the association participation being compiled is returned
		return associationParticipationContext;
	}

	public static CodeOutput codeOutput() {
		// description
		// returns the CodeOutputService
		// requires
		// none
		// guarantees
		// a reference to the CodeOutput being compiled is returned
		return codeOutputContext;
	}

	public static String target() {
		// description
		// returns the target
		// requires
		// none
		// guarantees
		// a reference to the CodeOutput being compiled is returned
		return targetContext;
	}

	public static void setModel(Model aModel) {
		// description
		// sets the Model context
		// requires
		// aModel <> null
		// guarantees
		// modelContext == aModel
		modelContext = aModel;
	}

	public static void clearModel() {
		// description
		// sets the Model context to null
		// requires
		// none
		// guarantees
		// modelContext == null
		modelContext = null;
	}

	public static void setMMClass(MMClass aClass) {
		// description
		// sets the MMClass context
		// requires
		// aClass <> null
		// guarantees
		// mMClassContext == aClass
		mMClassContext = aClass;
	}

	public static void clearMMClass() {
		// description
		// sets the MMClass context to null
		// requires
		// none
		// guarantees
		// mMClassContext == null
		mMClassContext = null;
	}

	public static void setState(State aState) {
		// description
		// sets the State context
		// requires
		// aState <> null
		// guarantees
		// stateContext == aClass
		stateContext = aState;
	}

	public static void clearState() {
		// description
		// sets the State context to null
		// requires
		// none
		// guarantees
		// stateContext == null
		stateContext = null;
	}

	public static void setEvent(Event anEvent) {
		// description
		// sets the Event context
		// requires
		// anEvent <> null
		// guarantees
		// eventContext == anEvent
		eventContext = anEvent;
	}

	public static void clearEvent() {
		// description
		// sets the Event context to null
		// requires
		// none
		// guarantees
		// eventContext == null
		eventContext = null;
	}

	public static void setAssociation(Association anAssociation) {
		// description
		// sets the Association context
		// requires
		// anAssociation <> null
		// guarantees
		// associationContext == anAssociation
		associationContext = anAssociation;
	}

	public static void clearAssociation() {
		// description
		// sets the Association context to null
		// requires
		// none
		// guarantees
		// associationContext == null
		associationContext = null;
	}

	public static void setAssociationParticipation(AssociationParticipation anAssociationParticipation) {
		// description
		// sets the Association Participation context
		// requires
		// anAssociationParticipation <> null
		// guarantees
		// associationParticipationContext == anAssociationParticipation
		associationParticipationContext = anAssociationParticipation;
	}

	public static void clearAssociationParticipation() {
		// description
		// sets the Association participation context to null
		// requires
		// none
		// guarantees
		// associationParticipationContext == null
		associationParticipationContext = null;
	}

	public static void setCodeOutput(CodeOutput aCodeOutput) {
		// description
		// sets the CodeOutput context
		// requires
		// aCodeOutput <> null
		// guarantees
		// codeOutputContext == aCodeOutput
		codeOutputContext = aCodeOutput;
	}

	public static void setTarget(String target) {
		// description
		// sets the CodeOutput context
		// requires
		// aCodeOutput <> null
		// guarantees
		// codeOutputContext == aCodeOutput
		targetContext = target;
	}

	// Instance variables

	// none

	// Constructor(s)

	// none

	// Accessors

	// none

	// Modifiers

	// none

	// Private methods

	// none

}
