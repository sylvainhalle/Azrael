Azrael: a universal serialization library for Java
==================================================

![Azrael the cat](azrael.jpg?raw=true)

Azrael is a [serialization](https://en.wikipedia.org/wiki/Serialization)
library for Java. It allows Java objects to be saved ("serialized") to
some data format, and to be reconstructed ("deserialized") from that saved
data at a later time.

By default, Azrael allows objects to be serialized to
[JSON](https://en.wikipedia.org/wiki/JSON), but its
API allows you to easily implement serialization to your own custom data
format.

Dependencies
------------

The package `ca.uqac.lif.cornipickle.json` should be in your classpath to
use JSON serialization. (It shall be bundled with Azrael in the near
future.)

Features
--------

Azrael was developed out of insatisfaction with existing (JSON)
serialization libraries, mostly
[Java Google Gson](https://code.google.com/p/google-gson/) (Gson) and
[Genson](http://owlike.github.io/genson/). Here are a couple of features
of Azrael for which other libraries didn't fit the author's needs.

### Serialize list, maps and sets

In other serialization libraries, "collections require special treatment
since the Collections are of generic type and the type information is lost
while converting to JSON due to Java type erasure" (says the
[Gson documentation](http://www.studytrails.com/java/json/java-google-json-introduction.jsp)).

**Not so with Azrael**, which *does* takes care of serializing the exact
class of each element in a collection (list, map, set). Hence you can write,
as you would for any other object:

    List<Integer> list1 = new LinkedList<Integer>();
    (...Fill the list with stuff...)
    Object o = serializer.serialize(list1);
    List<Integer> list2 = (List<Integer>) serializer.deserializeAs(
      o, LinkedList.class);

The contents of `list2` recreate precisely the original objects with their
*actual* (not declared) type. No custom code is needed (contrarily to what
Gson requires).

### No reliance on declared type

When serializing member fields of an object, Azrael compares the declared
type of the field with the actual type of the object, and adds type
information to the serialization whenever they differ. Consider the
following example:

    abstract class A { 
    }
    
    class B extends A {
      int x = 0;
    }
    
    class C {
      A my_b = new B();
    }

When deserializing an object of class `C`, other libraries run into a
problem, as they try to instantiate an object of class `A`, since this is
the *declared* type of field `my_b`. But `A` is an abstract class, and
cannot be instantiated. To handle this case with Gson, you need to write
yet more custom code to take care of this (not quite) exceptional
situation.

**Not so with Azrael**, which takes care of adding to the serialization that
the actual class of `my_b` is `B`, enabling it to properly deserialize the
object. Note that the type information is only added when it differs from
the declaration, to avoid cluttering the output when there is no possible
ambiguity. Works out of the box, period.

### Not tied to JSON

If you want to serialize objects to another format than JSON (XML, strings,
whatever custom format you wish), other JSON libraries can't help you.

**Not so with Azrael.** Its default `Serializer` and `Handler` classes take
care of much of the work of decomposing an object and determining type
information. JSON is just one possible way of implementing the abstract
methods of these two classes. To serialize to another format, simply
override them in a different way to produce the output you wish. (As a
matter of fact, the code specific to JSON serialization is a mere 350 lines
long.)

About the author                                                   {#about}
----------------

Azrael was written by Sylvain Hallé, associate professor at Université
du Québec à Chicoutimi, Canada.
