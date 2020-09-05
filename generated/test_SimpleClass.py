"""
doc_string for test_python_class
"""
import random

from SimpleClass import SimpleClass


class TestSimpleClass:
    """
    doc_string
    """

    def teardown_method(self, test_method):
        SimpleClass.SimpleClassSet.clear()

    def test_states(self):
        """
        test states
        """
        assert SimpleClass.SimpleClass_states.EXISTS == SimpleClass.SimpleClass_states.EXISTS
        assert (
            SimpleClass.SimpleClass_states.DOESNTEXIST == SimpleClass.SimpleClass_states.DOESNTEXIST
        )

    def test_constructor(self):
        """
        test the constructor
        """
        # this tests that the constructor will append the new instance into the class variable
        assert len(SimpleClass.SimpleClassSet) == 0
        s = SimpleClass()
        assert len(SimpleClass.SimpleClassSet) == 1
        assert SimpleClass.SimpleClassSet[0] == s

        # this tests the _state instance attribute
        assert s.state == SimpleClass.SimpleClass_states.EXISTS

        # this tests the initializer method which sets _x
        assert s.x == 0

    def test_destroy(self):
        """
        test the destroy
        """
        assert len(SimpleClass.SimpleClassSet) == 0
        s = SimpleClass()
        assert len(SimpleClass.SimpleClassSet) == 1
        assert SimpleClass.SimpleClassSet[0] == s
        s.destroy()
        assert len(SimpleClass.SimpleClassSet) == 0

    def test_update(self):
        """
        test the update
        """
        s = SimpleClass()
        assert s.x == 0
        new_x = random.randint(1, 1000000)
        s.update(new_x)
        assert s.x == new_x
