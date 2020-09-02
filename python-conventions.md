# Python conventions

Python culturally doesn't implement access modifiers such as `public`, `private`, `protected` like
in Java. See this [SO question](https://stackoverflow.com/questions/997482/does-java-support-default-parameter-values) for example of such discussion.

Python treats client code as responsible users. So if they choose to ignore the convention, Python
assumes they know what they are doing.

See https://docs.python-guide.org/writing/style/#we-are-all-responsible-users for details about this
convention.

## How do we then treat Python class and instance attributes

This model compiler written by Steve Tockey originally targets Java as the target language. So some
Java conventions and assumptions are baked into the compiler code.

This document will state clearly what Python conventions are assumed and therefore how the compiler
will then generate the code accordingly.

### First, a clear definition of what's directly readable and writable

In this document, when I say directly readable or simply readable, this is what I mean:

> Access using `instance.instance_attribute` or `Class.ClassAttribute` outside the class

In this document, when I say directly writable or simply writable, this is what I mean:

> Assign values using `instance.instance_attribute = <new_value>` outside the class

Yes, I did not mention class attributes to be writable. This will be covered later in this document.


### Private-public distinction only applies to instance not class

There are a few useful distinctions in terms of practice.

We want to distinguish between what's usable outside the class.

So `private` means used ONLY inside the class. `public` means usable outside the class as well.


Note: Usable outside the class but not inside the class makes no sense, so we ignore this scenario


| Usable \ Modifiers                               | Private | Public |
| ------------------------------------------------ | ------- | ------ |
| directly readable and writable inside the class  | ✅       | ✅      |
| directly readable and writable outside the class | ❌       | ✅      |

For public attributes, we also want to distinguish what's read-only (aka accessible) and what's writeable (aka mutable) *when outside the class*.

| When outside the class \ Modifiers                   | Private                                                     | Public                                     |
| ---------------------------------------------------- | ----------------------------------------------------------- | ------------------------------------------ |
| Not even readable                                    | Leave it as private                                         | Not applicable                             |
| Readable as `instance.attribute`                     | Create a public accessor method using `@property` decorator | Directly access using `instance.attribute` |
| Writeable as `instance.attribute = <some_new_value>` | Not applicable or might as well make it public              | ✅                                          |
| Writeable but inside another public method           | ✅                                                           | ✅                                          |

With this in mind, we want to say that private-public distinction applies to instance level properties (my umbrella term for both attributes and methods). Not class properties. Though I admit there's practically no such thing as a mutable method, but just to be safe, I use the term "properties".

**Assumption 1: Only instance properties have private-public distinctions. Not class-level properties**


### Underscores

In Python, there are two ways to indicate private methods or attributes.

- leading single underscore (as in `_var`)
- leading double underscore (as in `__var`)

Another way to say double underscore is "dunder". Henceforth, I will use "dunder" for rest of this document.

See https://dbader.org/blog/meaning-of-underscores-in-python last section for a summary of conventions for underscores.

When using leading dunder, it can trigger a phenomenon known as "name mangling" which makes it harder to directly access the attribute or method.

However, this model compiler will NOT implement leading dunder at all.

**Assumption 2: This compiler does NOT implement leading dunder at all for private methods and attributes.**

Or another way to say this is

**Assumption 2: This compiler always implements leading single underscore for private methods and attributes**

| underscores \ Modifiers                | Private | Public |
| -------------------------------------- | ------- | ------ |
| leading single underscores like `_var` | ✅       | ❌      |
| leading dunders like `__var`           | ❌       | ❌      |


### Instance attribute

In Python, there's no strong need to declare public instance attributes. This compiler takes the convention that all instance attributes are to be declared regardless.

An example Python class showing how there's not declaring instance attribute will still work

```python
# this works but not the accepted convention
class ExampleClass(object):
  class_attr = 0
  def __init__(self, instance_attr):
    self.instance_attr = instance_attr
```

To help distinguish during declaration which is class attribute and which is instance attribute the convention is to use snake_style for instance attribute and CamelCase for class attribute

**Assumption 2: All instance attributes must be declared, private or not**

```python
# the accepted convention
class ExampleClass(object):
  ClassAttr:int = 0
  instance_attr:int
  def __init__(self, instance_attr):
    self.instance_attr = instance_attr
```

And that all instance attributes are default to be public unless stated otherwise.

The only exception are `state` attributes. They should default to private. Therefore, `_state`

**Assumption 3: All instance attributes default to public unless stated otherwise. The exception is `_state` attribute which should default to private.**

Private instance attributes are by convention assumed to not available even for readonly access.

If there's a need to provide read access to private attributes, use a public instance method using `@property` decorator.

**Assumption 4: All private instance attributes are not even accessible for readonly access. To allow read access, create a public instance method using `@property` decorator**

### Class attribute

Another issue is class attributes.

See this https://dzone.com/articles/python-class-attributes-vs-instance-attributes for details. Previously, I reuse example code above in this article.

Below I will reuse more code and quoted a few lines from the same article.

```python
if __name__ == '__main__':
foo = ExampleClass(1)
bar = ExampleClass(2)
    # print the instance attribute of the object foo
    print (foo.instance_attr)
    #1
    #print the instance attribute of the object var
    print (bar.instance_attr)
    #2
    #print the class attribute of the class ExampleClass as a property of the class itself
    print (ExampleClass.ClassAttr)
    #0
    #print the classattribute  of the class as a proporty of the objects foo,bar
    print (bar.ClassAttr)
    #0
    print (foo.ClassAttr)
    #0
    # try to print instance attribute as a class property
    print (ExampleClass.instance_attr)
    #AttributeError: type object 'ExampleClass' has no attribute 'instance_attr'
```

> In Python, the class attribute (ClassAttr) is accessible as both a property of the class and as a property of objects, as it is shared between all of them.

Furthermore, class attributes can mutate to be instance attributes. Which complicates matters.

Basically, Python gives a lot of latitude to developers.

Therefore, this leads to the next assumption this model compiler adopts for Python.

**Assumption 5: Class attributes are always assumed as immutable outside the class**

| Action at where \ Attribute                                                                                                             | Private  Instance                                           | Public Instance                            | Class                                                                                   |
| --------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------- | ------------------------------------------ | --------------------------------------------------------------------------------------- |
| Not even readable *outside* the Class                                                                                                   | Leave it as private                                         | Not applicable                             | Not applicable                                                                          |
| Readable *outside* the Class as `instance.attribute`                                                                                    | Create a public accessor method using `@property` decorator | Directly access using `instance.attribute` | Directly access using `Class.ClassAttribute`                                            |
| Writeable *outside* the Class as `instance.attribute = <some_new_value>`                                                                | ❌ or might as well make it public                           | ✅                                          | ❌ <br/>No direct modification allowed. Provide a class method to modify it if necessary |
| Writeable *outside* the Class but as one small part of a longer, public method                                                          | ✅                                                           | ✅                                          | ✅                                                                                       |
| Declare with data type but see [example code](#define-class-attribute-with-the-class-as-data-type) when data type is the Class itself ✅ | e.g., `_instance_attribute: int`                            | e.g., `instance_attribute: int`            | e.g., `ClassAttribute: ClassVar[int]`                                                   |


#### Define Class Attribute with the Class as data type

if Python >= 3.10

```python
from typing import ClassVar, List
class PythonicSimpleClass:
    # ClassAttribute that holds all class member
    ClassAttribute: ClassVar[List[PythonicSimpleClass]] = []
```

if Python >= 3.7 but < 3.10

```python
# see https://stackoverflow.com/a/33533514/80353 on why this is for Class using itself as data type
# for >= Python 3.7 and below 3.10
from __future__ import annotations
from typing import ClassVar, List
class PythonicSimpleClass:
    # ClassAttribute that holds all class member
    ClassAttribute: ClassVar[List[PythonicSimpleClass]] = []
```


Since class attributes are treated as immutable outside the class, there's no need to add a leading single underscore.

**Assumption 5.1: Therefore, this compiler does NOT add leading underscores to class attributes or methods**

Since this compiler assumes class attributes are immutable. It is then consistent that this compiler assumes developers have no need to ever mutate a class attribute into a instance attribute.

**Assumption 5.1.1: No need to ever mutate a class attribute**

And finally, since Python treats developers as responsible users, creating a static method that simply returns a class attribute is redundant code. This model compiler assumes direct access to any class attribute.

**Assumption 5.2: Static methods that return one class attributes are redundant because this compiler assumes direct access to all class attributes for read purposes**

If we recall that the private-public distinction applies to instance level attributes and methods. So this means the opposite is also true

**Assumption 1.1: No such thing as a class attribute or method that's not meant to be read**

## Private-public distinction only applies to instance attributes and methods

Summarizing everything from above, we get the following:

1. private-public distinction only applies to instance attributes and methods. Not class level.
2. ALways use leading single underscores for private attributes and methods
3. All instance attributes must be declared explicitly in the class
4. All instance attributes are by convention public except `state` which is always by default private
5. If want to provide read access to private attribute, then need to provide public instance method using `@property` decorator
6. Class attributes are always treated as readonly and immutable.


Below is a clear summary in a table format of all the assumptions articulated from above.

One table is for attributes. One table is for methods.

### Attribute Summary

| Action at where \ Attribute                                                                                                             | Private  Instance   (leading single underscore ONLY) | Public Instance                             | Class  (No public or private distinction)                                              |
| --------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------- | ------------------------------------------- | -------------------------------------------------------------------------------------- |
| Readable *inside* the class in *any instance* method  ✅                                                                                 | `self._instance_attribute`                           | `self.instance_attribute`                   | `Class.ClassAttribute`                                                                 |
| Readable *inside* the class in *any Class* method  ✅                                                                                    | `instance._instance_attribute`                       | `instance.instance_attribute`               | `cls.ClassAttribute`                                                                   |
| Writable *inside* the class in *any instance* method  ✅                                                                                 | `self._instance_attribute = <new_value>`             | `self.instance_attribute = <new_value>`     | `Class.ClassAttribute = <new_value>`                                                   |
| Writable *inside* the class in *any Class* method  ✅                                                                                    | `instance._instance_attribute = <new_value>`         | `instance.instance_attribute = <new_value>` | `cls.ClassAttribute = <new_value>`                                                     |
| Directly Readable *outside* the class                                                                                                   | ❌                                                    | `instance.instance_attribute`               | `Class.ClassAttribute`                                                                 |
| Directly Writable *outside* the class                                                                                                   | ❌                                                    | `instance.instance_attribute = <new_value>` | ❌ or make a new Class method with `@classmethod` decorator if there's a strong need to |
| Declare with data type but see [example code](#define-class-attribute-with-the-class-as-data-type) when data type is the Class itself ✅ | e.g., `_instance_attribute: int`                     | e.g., `instance_attribute: int`             | e.g., `ClassAttribute: ClassVar[int]`                                                  |

### Method Summary

| Used at where \ Method                                | Private Instance    (leading single underscore ONLY)                                                              | Public Instance                                                                                                                                                                 | (No public or private distinction) Class method works with ClassAttribute                                                  | Class method does NOT work with ClassAttribute (aka static)                                                                        |
| ----------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| Called *Inside* the class in *any instance* method  ✅ | `self._instance_method()`                                                                                         | `self.instance_method()`                                                                                                                                                        | `Class.class_method()`                                                                                                     | `Class.static_method()`                                                                                                            |
| Called *Inside* the class in *any Class* method  ✅    | `instance._instance_method()`                                                                                     | `instance.instance_method()`                                                                                                                                                    | `cls.class_method()`                                                                                                       | `cls.static_method()`                                                                                                              |
| Called *Outside* the class                            | ❌                                                                                                                 | `instance.instance_method()`                                                                                                                                                    | `Class.class_method()`                                                                                                     | `Class.static_method()`                                                                                                            |
| Define the method  ✅                                  | <ul><li>no decorator needed</li> <li>always `self` as first argument e.g., `def _instance_method(self)`</li></ul> | <ul><li>`@property` decorator needed for getter of private instance attribute else no need</li> <li>always `self` as first argument e.g., `def instance_method(self)`</li></ul> | <ul><li>always use `@classmethod` decorator</li><li>always `cls` as first argument e.g., `def class_method(cls)`</li></ul> | <ul><li>always use `@staticmethod` decorator</li><li>NO special first argument such as `cls` e.g., `def static_method()`</li></ul> |
