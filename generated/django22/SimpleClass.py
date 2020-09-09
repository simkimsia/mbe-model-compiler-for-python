from __future__ import annotations

from enum import Enum
from typing import ClassVar, List
# see MMClass.rulePIMImportsList for implementation

class SimpleClass:

    # generated 2020/09/09 13:01:43 by JAL open model compiler v5.3
    
    
    # Class description
    # see MMClass.ruleClassDescription for implementation
    
        # none
    
    
    # PIM constants
    # see MMClass.rulePIMConstantsList for implementation
    
        # none
    
    
    # State Enum Declaration
    # see MMClass.ruleStateEnumDeclaration for implementation
    
    SimpleClass_states = Enum("SimpleClass_states", "EXISTS DOESNTEXIST" )
    
    
    # Attribute instance variables
    # see MMClass.ruleAttributeInstVarList for implementation
    # Python doesn't treat private variables too strictly
    # by convention it uses _ for private variables
    # https://docs.python.org/3/tutorial/classes.html#private-variables
    # and it treats client code as responsible users if they choose to ignore the convention
    # https://docs.python-guide.org/writing/style/#we-are-all-responsible-users
    
    _x: int
    _state: SimpleClass_states


    # Class level attribute
    # All class members accessor
    
    SimpleClassSet: ClassVar[List[SimpleClass]] = []


    # Association participation instance variables
    # see MMClass.ruleAssociationInstVarList
    
        # none
    
    
    # Constructor
    # See MMClass.ruleConstructorOperation
    
    # See constructEvent.ruleConstructorOperation
    def __init__(self):
        # requires
        #    none
        # guarantees
        #    --> x has been set to zero and state == Exists
        self._initializer()
        self._state = SimpleClass.SimpleClass_states.EXISTS
        SimpleClass.SimpleClassSet.append( self )


    # Attribute getters
    
    @property
    def x(self) -> int:
        # requires
        #   none
        # guarantees
        #   returns the x
        return self._x
    
    @property
    def state(self) -> SimpleClass_states:
        # requires
        #   none
        # guarantees
        #   returns the state
        return self._state
    

    # Derived attributes
    
        # none
        
    
    # Pushed events
    
    def destroy(self) -> None :
        # requires
        #    none
        # guarantees
        #   state was Exists --> state == Doesn't exist
        if self._state == SimpleClass.SimpleClass_states.EXISTS:
            self._state = SimpleClass.SimpleClass_states.DOESNTEXIST
            SimpleClass.SimpleClassSet.remove( self )
        
    
    def update(self, new_x:int ) -> None :
        # requires
        #    none
        # guarantees
        #   state was Exists --> x has been set to new x 
        if self._state == SimpleClass.SimpleClass_states.EXISTS:
            self._update_x( new_x )
        
    

    # Private transition actions
    
    def _initializer(self):
        # requires
        #   none
        # guarantees
        #   x has been set to zero
        self._x = 0
    
    def _update_x(self, new_x:int):
        # requires
        #   none
        # guarantees
        #   x has been set to new x
        self._x = new_x
    

    # PIM Overlay helper code
    
    # comment 1
    # helper comment 2
    	# helper comment 3
    # comment 4
    

    # Association participation link and unlink services
    
        # none
    


