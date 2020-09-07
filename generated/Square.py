from __future__ import annotations

from enum import Enum
from typing import ClassVar, List
# see MMClass.rulePIMImportsList for implementation

class Square:

    # generated 2020/09/06 22:33:57 by JAL open model compiler v5.3
    
    
    # Class description
    # see MMClass.ruleClassDescription for implementation
    
        # none
    
    
    # PIM constants
    # see MMClass.rulePIMConstantsList for implementation
    
        private static int maxAge = 10;
    
    
    # State Enum Declaration
    # see MMClass.ruleStateEnumDeclaration for implementation
    
    Square_states = Enum("Square_states", "EXISTS DOESNTEXIST READY" )
    
    
    # Attribute instance variables
    # see MMClass.ruleAttributeInstVarList for implementation
    # Python doesn't treat private variables too strictly
    # by convention it uses _ for private variables
    # https://docs.python.org/3/tutorial/classes.html#private-variables
    # and it treats client code as responsible users if they choose to ignore the convention
    # https://docs.python-guide.org/writing/style/#we-are-all-responsible-users
    
    _isOccupied: boolean
    _age: int
    _willBeOccupied: boolean
    _state: Square_states


    # Class level attribute
    # All class members accessor
    
    SquareSet: ClassVar[List[Square]] = []


    # Association participation instance variables
    # see MMClass.ruleAssociationInstVarList
    
        def _squaredef  n;
        def _squaredef  s;
        def _squaredef  sw;
        def _squaredef  ne;
        def _squaredef  e;
        def _squaredef  w;
        def _squaredef  se;
        def _squaredef  nw;
        def _colonydef  mayBeNw;
    
    
    # Constructor
    # See MMClass.ruleConstructorOperation
    
    # See constructEvent.ruleConstructorOperation
    def __init__(self):
        # requires
        #    none
        # guarantees
        #    --> this square has been initialized and state == Exists
        n = null;
        s = null;
        sw = null;
        ne = null;
        e = null;
        w = null;
        se = null;
        nw = null;
        mayBeNw = null;
        self._initializer()
        self._state = Square.Square_states.EXISTS
        Square.SquareSet.append( self )


    # Attribute getters
    
    @property
    def isOccupied(self) -> boolean:
        # requires
        #   none
        # guarantees
        #   returns the is occupied
        return self._isOccupied
    
    @property
    def age(self) -> int:
        # requires
        #   none
        # guarantees
        #   returns the age
        return self._age
    
    @property
    def willBeOccupied(self) -> boolean:
        # requires
        #   none
        # guarantees
        #   returns the will be occupied
        return self._willBeOccupied
    
    @property
    def state(self) -> Square_states:
        # requires
        #   none
        # guarantees
        #   returns the state
        return self._state
    

    # Derived attributes
    
        public int livingNeighbors(self)# requires
            #   none
            # guarantees
            #   returns the number of living neighbors around this square
                int neighbors = 0;
                if( n != null && n.is_occupied() ) {
                   neighbors++;
                }
                if( ne != null && ne.is_occupied() ) {
                   neighbors++;
                }
                if( e != null && e.is_occupied() ) {
                   neighbors++;
                }
                if( se != null && se.is_occupied() ) {
                   neighbors++;
                }
                if( s != null && s.is_occupied() ) {
                   neighbors++;
                }
                if( sw != null && sw.is_occupied() ) {
                   neighbors++;
                }
                if( w != null && w.is_occupied() ) {
                   neighbors++;
                }
                if( nw != null && nw.is_occupied() ) {
                   neighbors++;
                }
                return neighbors;
            
            public Square east(self)# requires
                #   none
                # guarantees
                #   the east square of this one has been returned
                    return e;
                
                public int cellCount(self)# requires
                    #   none
                    # guarantees
                    #   returned the number of living cells in the colony including, and after, this
                        int liveCells = 0;
                        
                        # count how many living cells there are after this one
                        if( e != null ) {
                            liveCells = e.cell_count();
                        }
                        if( w == null && s != null ) {
                           liveCells += s.cell_count();
                        }
                        
                        # count this cell, if it is alive
                        if( is_occupied() ) {
                           liveCells++;
                        }
                        
                        return liveCells;
                    
                
                # Pushed events
                
                def destroy(self) -> None :
                    # requires
                    #    none
                    # guarantees
                    #   state was Ready --> state == Doesn't exist
                    if self._state == Square.Square_states.READY:
                        self._state = Square.Square_states.DOESNTEXIST
                        Square.SquareSet.remove( self )
                    
                
                def initialized(self) -> None :
                    # requires
                    #    none
                    # guarantees
                    #   state was Exists --> this square's E neighbor, if any, has also been made ready, if this square is W most, its S neighbor has also been made ready and state == Ready
                    if self._state == Square.Square_states.EXISTS:
                        self._get_ready()
                        self._state = Square.Square_states.READY
                    
                
                def connectN(self, a_square:Square ) -> None :
                    # requires
                    #    a square is not null
                    # guarantees
                    #   state was Exists --> this square is connected with its north neighbor 
                    if self._state == Square.Square_states.EXISTS:
                        self._connect_north( a_square )
                    
                
                def connectNe(self, a_square:Square ) -> None :
                    # requires
                    #    a square is not null
                    # guarantees
                    #   state was Exists --> this square is connected with its northeast neighbor 
                    if self._state == Square.Square_states.EXISTS:
                        self._connect_northeast( a_square )
                    
                
                def setNextGeneration(self) -> None :
                    # requires
                    #    none
                    # guarantees
                    #   state was Ready --> will be occupied has been properly set for the next generation, this square's E neighbor, if any, has also been set, if this square is W most, its S neighbor is also been set 
                    if self._state == Square.Square_states.READY:
                        self._compute_next_generation()
                    
                
                def advance(self) -> None :
                    # requires
                    #    none
                    # guarantees
                    #   state was Ready --> this square has advanced to the next generation, this square's E neighbor, if any, has also advanced, if this square is W most, its S neighbor has also advanced 
                    if self._state == Square.Square_states.READY:
                        self._move_to_next_generation()
                    
                
                def display(self) -> None :
                    # requires
                    #    none
                    # guarantees
                    #   state was Ready --> this square has displayed its current status, this square's E neighbor, if any, has also displayed, if this square is W most, its S neighbor has also displayed 
                    if self._state == Square.Square_states.READY:
                        self._show_square_status()
                    
                
                def connectNw(self, a_square:Square ) -> None :
                    # requires
                    #    a square is not null
                    # guarantees
                    #   state was Exists --> this square is connected with its northwest neighbor 
                    if self._state == Square.Square_states.EXISTS:
                        self._connect_northwest( a_square )
                    
                
                def connectW(self, a_square:Square ) -> None :
                    # requires
                    #    a square is not null
                    # guarantees
                    #   state was Exists --> this square is connected with its west neighbor 
                    if self._state == Square.Square_states.EXISTS:
                        self._connect_west( a_square )
                    
                
                def set(self, how_far_across:int, how_far_down:int ) -> None :
                    # requires
                    #    0 <= how far across <= width
                    #    0 <= how far down <= height
                    # guarantees
                    #   state was Ready --> if this is the one it has been set, otherwise request forwarded 
                    if self._state == Square.Square_states.READY:
                        self._set_a_square( how_far_across, how_far_down )
                    
                
            
                # Private transition actions
                
                def _initializer(self):
                    # requires
                    #   none
                    # guarantees
                    #   this square has been initialized
                    is_occupied = false;
                    age = 0;
                    will_be_occupied = false;
                
                def _get_ready(self):
                    # requires
                    #   none
                    # guarantees
                    #   this square's E neighbor, if any, has also been made ready
                    #   if this square is W most, its S neighbor has also been made ready
                    if( e != null ) {
                        e.initialized();
                    }
                    if( w == null && s != null ) {
                       s.initialized();
                    }
                
                def _connect_northwest(self, a_square:Square):
                    # requires
                    #   a square is not null
                    # guarantees
                    #   this square is connected with its northwest neighbor
                    nw = a_square;
                    a_square.link_se( this );
                
                def _connect_north(self, a_square:Square):
                    # requires
                    #   a square is not null
                    # guarantees
                    #   this square is connected with its north neighbor
                    n = a_square;
                    a_square.link_s( this );
                
                def _connect_northeast(self, a_square:Square):
                    # requires
                    #   a square is not null
                    # guarantees
                    #   this square is connected with its northeast neighbor
                    ne = a_square;
                    a_square.link_sw( this );
                
                def _connect_west(self, a_square:Square):
                    # requires
                    #   a square is not null
                    # guarantees
                    #   this square is connected with its west neighbor
                    w = a_square;
                    a_square.link_e( this );
                
                def _set_a_square(self, how_far_across:int, how_far_down:int):
                    # requires
                    #   0 <= how far across <= width
                    #   0 <= how far down <= height
                    # guarantees
                    #   if this is the one it has been set, otherwise request forwarded
                    if( how_far_down == 0 ) {
                       if( how_far_across == 0 ) {
                          is_occupied = true;
                       } else {
                          e.set( how_far_across - 1, how_far_down );
                       }
                    } else {
                       s.set( how_far_across , how_far_down - 1 );
                    }
                
                def _compute_next_generation(self):
                    # requires
                    #   none
                    # guarantees
                    #   will be occupied has been properly set for the next generation
                    #   this square's E neighbor, if any, has also been set
                    #   if this square is W most, its S neighbor is also been set
                    # first, figure out what happens to this square going into the next generation
                    if( is_occupied ) {
                       # this square has a living cell in it
                       if( ( living_neighbors() == 2 || living_neighbors() == 3 ) && age < maxAge ) {
                          # the cell has the right number of living neighbors and is not to old, so it survives
                          will_be_occupied = true;
                       } else {
                          # the cell doesn't have the right number of living neighbors or is too old, so it dies
                          will_be_occupied = false;
                       }
                    } else {
                       # this square does not have a living cell in it
                       if( living_neighbors() == 3 ) {
                          # there are the right number of living neighbors, so a new cell is born
                          will_be_occupied = true;
                       } else {
                          # there are not the right number of living neighbors, so this square stays empty
                          will_be_occupied = false;
                       }
                    }
                    
                    # then figure out the rest of the colony
                    if( e != null ) {
                        e.set_next_generation();
                    }
                    if( w == null && s != null ) {
                       s.set_next_generation();
                    }
                
                def _move_to_next_generation(self):
                    # requires
                    #   none
                    # guarantees
                    #   this square has advanced to the next generation
                    #   this square's E neighbor, if any, has also advanced
                    #   if this square is W most, its S neighbor has also advanced
                    # first, advance this square to the next generation
                    if( is_occupied && will_be_occupied ) {
                       # this cell survives, so increase its age
                       age++;
                    } else {
                       if( !is_occupied && will_be_occupied ) {
                          # a new cell was born, set its age to zero
                          is_occupied = true;
                          age = 0;
                       } else {
                          # the previous cell died or the square remains empty
                          is_occupied = false;
                          age = 0;
                       }
                    }
                    will_be_occupied = false;
                    
                    # then advance the rest of the colony
                    if( e != null ) {
                        e.advance();
                    }
                    if( w == null && s != null ) {
                       s.advance();
                    }
                
                def _show_square_status(self):
                    # requires
                    #   none
                    # guarantees
                    #   this square has displayed its current status
                    #   this square's E neighbor, if any, has also displayed
                    #   if this square is W most, its S neighbor has also displayed
                    # first, display the status of this square
                    if( is_occupied ) {
                       System.out.print( "O " );
                    } else {
                       System.out.print( ". " );
                    }
                    # then display the rest of the colony
                    if( e != null ) {
                        e.display();
                    } else {
                       System.out.println();
                    }
                    if( w == null && s != null ) {
                       s.display();
                    }
                
            
                # PIM Overlay helper code
                
                # none
                
            
                # Association participation link and unlink services
                
                // link and unlink services for: above
                
                    public void linkN( Square aSquare ) {
                    # requires
                    //   aSquare <> null
                    # guarantees
                    //   both this and aSquare are linked to each other
                        n = aSquare;
                    
                    public void unlinkN( Square aSquare ) {
                    # requires
                    //   aSquare <> null
                    # guarantees
                    //   this and aSquare are unlinked
                        n = null;
                    
                    public void linkS( Square aSquare ) {
                    # requires
                    //   aSquare <> null
                    # guarantees
                    //   both this and aSquare are linked to each other
                        s = aSquare;
                    
                    public void unlinkS( Square aSquare ) {
                    # requires
                    //   aSquare <> null
                    # guarantees
                    //   this and aSquare are unlinked
                        s = null;
                    
                // link and unlink services for: up diagonal
                
                    public void linkSw( Square aSquare ) {
                    # requires
                    //   aSquare <> null
                    # guarantees
                    //   both this and aSquare are linked to each other
                        sw = aSquare;
                    
                    public void unlinkSw( Square aSquare ) {
                    # requires
                    //   aSquare <> null
                    # guarantees
                    //   this and aSquare are unlinked
                        sw = null;
                    
                    public void linkNe( Square aSquare ) {
                    # requires
                    //   aSquare <> null
                    # guarantees
                    //   both this and aSquare are linked to each other
                        ne = aSquare;
                    
                    public void unlinkNe( Square aSquare ) {
                    # requires
                    //   aSquare <> null
                    # guarantees
                    //   this and aSquare are unlinked
                        ne = null;
                    
                // link and unlink services for: beside
                
                    public void linkE( Square aSquare ) {
                    # requires
                    //   aSquare <> null
                    # guarantees
                    //   both this and aSquare are linked to each other
                        e = aSquare;
                    
                    public void unlinkE( Square aSquare ) {
                    # requires
                    //   aSquare <> null
                    # guarantees
                    //   this and aSquare are unlinked
                        e = null;
                    
                    public void linkW( Square aSquare ) {
                    # requires
                    //   aSquare <> null
                    # guarantees
                    //   both this and aSquare are linked to each other
                        w = aSquare;
                    
                    public void unlinkW( Square aSquare ) {
                    # requires
                    //   aSquare <> null
                    # guarantees
                    //   this and aSquare are unlinked
                        w = null;
                    
                // link and unlink services for: down diag
                
                    public void linkSe( Square aSquare ) {
                    # requires
                    //   aSquare <> null
                    # guarantees
                    //   both this and aSquare are linked to each other
                        se = aSquare;
                    
                    public void unlinkSe( Square aSquare ) {
                    # requires
                    //   aSquare <> null
                    # guarantees
                    //   this and aSquare are unlinked
                        se = null;
                    
                    public void linkNw( Square aSquare ) {
                    # requires
                    //   aSquare <> null
                    # guarantees
                    //   both this and aSquare are linked to each other
                        nw = aSquare;
                    
                    public void unlinkNw( Square aSquare ) {
                    # requires
                    //   aSquare <> null
                    # guarantees
                    //   this and aSquare are unlinked
                        nw = null;
                    
                // link and unlink services for: may be NW corner
                
                    public void linkIsNwCorner( Colony aColony ) {
                    # requires
                    //   aColony <> null
                    # guarantees
                    //   both this and aColony are linked to each other
                        mayBeNw = aColony;
                    
                    public void unlinkIsNwCorner( Colony aColony ) {
                    # requires
                    //   aColony <> null
                    # guarantees
                    //   this and aColony are unlinked
                        mayBeNw = null;
                    
            
        
        
