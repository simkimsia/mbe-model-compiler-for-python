# mbe-model-compiler-for-python
This is an attempt to build a semantic model compiler which is forked from the v5.3 of the java model compiler by Steve Tockey based on his book How to Engineer Software.

The generated code from the semantic model is meant to be correct for Python 3.7.7 and above.

## Map of what's going on

This is fundamentally a Java project.

Its purpose is to take in a .jal.txt (which holds semantic models) and then generate the corresponding
python class in a python file.

The `.jal.txt` files are in `JAL` folder

The `target` folder contains the expected python files.

Target language is Python 3.7.7 and above.

The `output` folder contains the actual generated code from the semantic models.

## How to run this

In the real world, there are as many ways as there are IDEs and editors that allow you to run Java.

But in this README, I will cover just two: eclipse and visual studio code.

For brevity's sake for this README, these are in two separate READMEs.

- [How to run in vs code](how-to-vscode.md)
- [How to run in eclipse](how-to-eclipse.md)

The author mostly uses vs code. So that's more likely to stay correct and updated over time.

##