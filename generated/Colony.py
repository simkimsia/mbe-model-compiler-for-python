from __future__ import annotations

from enum import Enum
from typing import ClassVar, List
# see MMClass.rulePIMImportsList for implementation

class Colony:

    # generated 2020/09/06 22:33:57 by JAL open model compiler v5.3
    
    
    # Class description
    # see MMClass.ruleClassDescription for implementation
    
        # none
    
    
    # PIM constants
    # see MMClass.rulePIMConstantsList for implementation
    
        # none
    
    
    # State Enum Declaration
    # see MMClass.ruleStateEnumDeclaration for implementation
    
    Colony_states = Enum("Colony_states", "EXISTS DOESNTEXIST" )
    
    
    # Attribute instance variables
    # see MMClass.ruleAttributeInstVarList for implementation
    # Python doesn't treat private variables too strictly
    # by convention it uses _ for private variables
    # https://docs.python.org/3/tutorial/classes.html#private-variables
    # and it treats client code as responsible users if they choose to ignore the convention
    # https://docs.python-guide.org/writing/style/#we-are-all-responsible-users
    
    _height: int
    _width: int
    _generation: int
    _state: Colony_states


    # Class level attribute
    # All class members accessor
    
    ColonySet: ClassVar[List[Colony]] = []


    # Association participation instance variables
    # see MMClass.ruleAssociationInstVarList
    
        def _squaredef  isNwCorner;
    
    
    # Constructor
    # See MMClass.ruleConstructorOperation
    
    # See constructEvent.ruleConstructorOperation
    def __init__(self a_width:int, a_height:int ):
        # requires
        #    a width > 0
        #    a height > 0
        # guarantees
        #    --> this colony has been initialized and state == Exists
        isNwCorner = null;
        self._initializer( a_width, a_height )
        self._state = Colony.Colony_states.EXISTS
        Colony.ColonySet.append( self )


    # Attribute getters
    
    @property
    def height(self) -> int:
        # requires
        #   none
        # guarantees
        #   returns the height
        return self._height
    
    @property
    def width(self) -> int:
        # requires
        #   none
        # guarantees
        #   returns the width
        return self._width
    
    @property
    def generation(self) -> int:
        # requires
        #   none
        # guarantees
        #   returns the generation
        return self._generation
    
    @property
    def state(self) -> Colony_states:
        # requires
        #   none
        # guarantees
        #   returns the state
        return self._state
    

    # Derived attributes
    
        public int cellCount(self)# requires
            #   none
            # guarantees
            #   returns the number of living cells in the colony
                self._self._return is_nw_corner.cell_count();
            
        
        # Pushed events
        
        def destroy(self) -> None :
            # requires
            #    none
            # guarantees
            #   state was Exists --> state == Doesn't exist
            if self._state == Colony.Colony_states.EXISTS:
                self._state = Colony.Colony_states.DOESNTEXIST
                Colony.ColonySet.remove( self )
            
        
        def display(self) -> None :
            # requires
            #    none
            # guarantees
            #   state was Exists --> the whole colony has been displayed 
            if self._state == Colony.Colony_states.EXISTS:
                self._display_colony()
            
        
        def setSquare(self, an_x:int, a_y:int ) -> None :
            # requires
            #    1 <= an x <= width
            #    1<= a y <= height
            # guarantees
            #   state was Exists --> the square with coordinates x,y has been set to alive 
            if self._state == Colony.Colony_states.EXISTS:
                self._set_a_square( an_x, a_y )
            
        
        def nextGeneration(self) -> None :
            # requires
            #    none
            # guarantees
            #   state was Exists --> the next generation of the colony has been computed 
            if self._state == Colony.Colony_states.EXISTS:
                self._compute_next_generation()
            
        
    
        # Private transition actions
        
        def _initializer(self, a_width:int, a_height:int):
            # requires
            #   a width > 0
            #   a height > 0
            # guarantees
            #   this colony has been initialized
            # initialize the colony attributes
            self._width = a_width;
            self._height = a_height;
            self._generation = 0;
            
            # create the first square in the colony and link to it
            self._is_nw_corner = new Square();
            self._is_nw_corner.link_is_nw_corner( this );
            
            # create the rest of the first row in the colony
            Square previousSquareOfThisRow = is_nw_corner;
            for( int widthCount = 2; widthCount <= width; widthCount++ ) {
               Square newSquare = new Square();
               newSquare.connect_w( previousSquareOfThisRow );
               previousSquareOfThisRow = newSquare;
            }
            
            # get ready to create the rest of the rows
            Square westMostSquareOfRowAbove = is_nw_corner;
            
            # now build the rest of the colony
            for( int heightCount = 2; heightCount <= height; heightCount++ ) {
            
               # create a new row of squares, starting with the first square in the row
               Square westMostSquareOfThisRow = new Square();
               previousSquareOfThisRow = westMostSquareOfThisRow;
               westMostSquareOfThisRow.connect_n( westMostSquareOfRowAbove );
               if( westMostSquareOfRowAbove.east() != null ) {
                  westMostSquareOfThisRow.connect_ne( westMostSquareOfRowAbove.east() );
               }
            
               # get ready to create the rest of this new row
               Square nWSquareOfRowAbove = westMostSquareOfRowAbove;
               Square nSquareOfRowAbove = nWSquareOfRowAbove.east();
               Square nESquareOfRowAbove = null;
               if( nSquareOfRowAbove.east() != null ) {
                  nESquareOfRowAbove = nSquareOfRowAbove.east();
               }
            
               # create the rest of this row
               for( int widthCount = 2; widthCount <= width; widthCount++ ) {
            
                  # make the next square in the row and connect it on its W side
                  Square newSquare = new Square();
                  newSquare.connect_w( previousSquareOfThisRow );
                  previousSquareOfThisRow = newSquare;
            
                  # also connect it to its neighbor squares in the row above
                  if( nWSquareOfRowAbove != null ) {
                     newSquare.connect_nw( nWSquareOfRowAbove );
                  }
                  newSquare.connect_n( nSquareOfRowAbove );
                  if( nESquareOfRowAbove != null ) {
                     newSquare.connect_ne( nESquareOfRowAbove );
                  }
            
                  # get ready to connect the next new square to the row above
                  nWSquareOfRowAbove = nSquareOfRowAbove;
                  nSquareOfRowAbove = nESquareOfRowAbove;
                  if( nSquareOfRowAbove != null && nESquareOfRowAbove.east() != null ) {
                     nESquareOfRowAbove = nESquareOfRowAbove.east();
                  } else {
                     nESquareOfRowAbove = null;
                  }
            
               }
            
               # the new row of squares has been completed, get ready for the next new row (if any)
               westMostSquareOfRowAbove = westMostSquareOfThisRow;
               nWSquareOfRowAbove = null;
               nSquareOfRowAbove = westMostSquareOfRowAbove;
               if( nSquareOfRowAbove.east() != null ) {
                  nESquareOfRowAbove = nSquareOfRowAbove.east();
               } else {
                  nESquareOfRowAbove = null;
               }
            
            }
            
            is_nw_corner.initialized();
        
        def _display_colony(self):
            # requires
            #   none
            # guarantees
            #   the whole colony has been displayed
            System.out.println();
            System.out.println( "-- Generation " + generation + "      cell count = " + this.cell_count() );
            System.out.println();
            is_nw_corner.display();
            System.out.println();
        
        def _set_a_square(self, an_x:int, a_y:int):
            # requires
            #   1 <= an x <= width
            #   1<= a y <= height
            # guarantees
            #   the square with coordinates x,y has been set to alive
            is_nw_corner.set( an_x - 1, height - a_y );
        
        def _compute_next_generation(self):
            # requires
            #   none
            # guarantees
            #   the next generation of the colony has been computed
            is_nw_corner.set_next_generation();
            is_nw_corner.advance();
            generation++;
        
    
        # PIM Overlay helper code
        
        # none
        
    
        # Association participation link and unlink services
        
        // link and unlink services for: may be NW corner
        
            public void linkMayBeNw( Square aSquare ) {
            # requires
            //   aSquare <> null
            # guarantees
            //   both this and aSquare are linked to each other
                isNwCorner = aSquare;
            
            public void unlinkMayBeNw( Square aSquare ) {
            # requires
            //   aSquare <> null
            # guarantees
            //   this and aSquare are unlinked
                isNwCorner = null;
            
    


