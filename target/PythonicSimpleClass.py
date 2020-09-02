"""
doc string for models module
"""
from enum import Enum
from typing import ClassVar, List


class PythonicSimpleClass:
    """
    This is handwritten
    First Created: 2020-08-31 21:52 UTC+8
    Last Modified: 2020-08-31 22:10 UTC+8
    """

    # Class description
    # none

    # PIM constants
    # none

    SimpleClass_states = Enum("SimpleClass_states", "EXISTS DOESNTEXIST")

    # Attribute instance variables
    # Python doesn't treat private variables too strictly
    # by convention it uses _ for private variables
    # https://docs.python.org/3/tutorial/classes.html#private-variables
    # and it treats client code as responsible users if they choose to ignore the convention
    # https://docs.python-guide.org/writing/style/#we-are-all-responsible-users
    _x: int
    _state: SimpleClass_states

    # Association participation instance variables
    # none

    #   Constructor
    def __init__(self) -> None:
        # requires
        #     none
        # guarantees
        #    --> x has been set to zero and state == Exists
        self._x = 0
        self._state = PythonicSimpleClass.SimpleClass_states.EXISTS
        PythonicSimpleClass.simpleClassSet.append(self)

    # Derived attributes
    #   none

    # Pushed events

    def destroy(self) -> None:
        # requires
        #    none
        # guarantees
        #   state was Exists --> state == Doesn't exist
        if self._state == PythonicSimpleClass.SimpleClass_states.EXISTS:
            self._state = PythonicSimpleClass.SimpleClass_states.DOESNTEXIST
            PythonicSimpleClass.simpleClassSet.remove(self)

    def update(self, newX: int) -> None:
        # requires
        #    none
        # guarantees
        #    state was Exists --> x has been set to new x
        if self._state == PythonicSimpleClass.SimpleClass_states.EXISTS:
            self._x = newX

    # Private transition actions
    #   none

    # PIM Overlay helper code

    #    comment 1
    #       helper comment 2
    #       helper comment 3
    #       comment 4

    #  Association participation link and unlink services

    #    none


# ClassVariable that holds all class member
# https://mypy.readthedocs.io/en/stable/class_basics.html#class-attribute-annotations => ClassVar
# https://stackoverflow.com/a/40244539/80353 => How to define ClassVar whose type is its own class
PythonicSimpleClass.simpleClassSet: ClassVar[List[PythonicSimpleClass]] = []  # Class variable only
