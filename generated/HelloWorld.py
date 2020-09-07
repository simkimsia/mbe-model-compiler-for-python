from __future__ import annotations

from enum import Enum
from typing import ClassVar, List
# see MMClass.rulePIMImportsList for implementation

class HelloWorld:

    # generated 2020/09/07 21:29:55 by JAL open model compiler v5.3
    
    
    # Class description
    # see MMClass.ruleClassDescription for implementation
    
        # none
    
    
    # PIM constants
    # see MMClass.rulePIMConstantsList for implementation
    
        # none
    
    
    # State Enum Declaration
    # see MMClass.ruleStateEnumDeclaration for implementation
    
    HelloWorld_states = Enum("HelloWorld_states", "EXISTS DOESNTEXIST" )
    
    
    # Attribute instance variables
    # see MMClass.ruleAttributeInstVarList for implementation
    # Python doesn't treat private variables too strictly
    # by convention it uses _ for private variables
    # https://docs.python.org/3/tutorial/classes.html#private-variables
    # and it treats client code as responsible users if they choose to ignore the convention
    # https://docs.python-guide.org/writing/style/#we-are-all-responsible-users
    
    _state: HelloWorld_states


    # Class level attribute
    # All class members accessor
    
    HelloWorldSet: ClassVar[List[HelloWorld]] = []


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
        #    --> "Hello" has been said and state == Exists
        self._say_hello()
        self._state = HelloWorld.HelloWorld_states.EXISTS
        HelloWorld.HelloWorldSet.append( self )


    # Attribute getters
    
    @property
    def state(self) -> HelloWorld_states:
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
        #   state was Exists --> "Goodbye" has been said and state == Doesn't exist
        if self._state == HelloWorld.HelloWorld_states.EXISTS:
            self._say_goodbye()
            self._state = HelloWorld.HelloWorld_states.DOESNTEXIST
            HelloWorld.HelloWorldSet.remove( self )
        
    

    # Private transition actions
    
    def _say_hello(self):
        # requires
        #   none
        # guarantees
        #   "Hello" has been said
        print( "Hello, world!" )
    
    def _say_goodbye(self):
        # requires
        #   none
        # guarantees
        #   "Goodbye" has been said
        print( "Goodbye, cruel world" )
    

    # PIM Overlay helper code
    
    
    def main():
        """
        Just a way to print lines
        """
        print("Hello World, in model-based form")
        a_hello = HelloWorld()
        a_hello.destroy()
        print("Done...")
    
    

    # Association participation link and unlink services
    
        # none
    


