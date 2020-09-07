"""
doc_string for test_python_class
"""
import random

from HelloWorld import HelloWorld


class TestHelloWorld:
    """
    doc_string
    """

    def teardown_method(self, test_method):
        HelloWorld.HelloWorldSet.clear()

    def test_states(self):
        """
        test states
        """
        assert HelloWorld.HelloWorld_states.EXISTS == HelloWorld.HelloWorld_states.EXISTS
        assert HelloWorld.HelloWorld_states.DOESNTEXIST == HelloWorld.HelloWorld_states.DOESNTEXIST

    def test_main(self, capsys):
        HelloWorld.main()
        captured = capsys.readouterr()
        expected = "Hello World, in model-based form\n"
        expected += "Hello, world!\n"
        expected += "Goodbye, cruel world\n"
        expected += "Done...\n"
        assert captured.out == expected
        # assert captured.err == "world\n"
