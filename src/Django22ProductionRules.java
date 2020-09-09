public class Django22ProductionRules implements ProductionRules {
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
}
