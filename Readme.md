Azrael: a universal serialization library for Java
==================================================

![Azrael the cat](azrael.jpg?raw=true)

Azrael is a [serialization](https://en.wikipedia.org/wiki/Serialization)
library for Java. It allows Java objects to be saved ("serialized") to
some data format, and to be reconstructed ("deserialized") from that saved
data at a later time.

By default, Azrael comes with a serializer for
[JSON](https://en.wikipedia.org/wiki/JSON), but its
API allows you to easily implement serialization to your own custom data
format.

How it works
------------

Suppose you have the following class:

    class MyClass {
      int x = 0;
      String s = "";
      List<Float> f = new LinkedList<Float>();
      
      public add(Float x) {
        f.add(x);
      }
    }

You would like to serialize the state of objects of class `MyClass` to
JSON. With Azrael, you can do the following:

    // Create an object and fill with values
    MyClass my_obj = new MyClass();
    my_obj.x = 1; my_obj.s = "abc"; my_obj.add(1.5);
    
    // Create a serializer for JSON
    JsonSerializer ser = new JsonSerializer();
    JsonElement e = ser.serialize(my_obj);

The contents of `my_obj` are saved in a JSON element, which you can save
somewhere as a string using its `toString()` method.

Now suppose you want to reconstruct an object of `MyClass` with the exact
data that was contained in the saved JSON. You first reconstruct the JSON
element `e` (from a String, etc.), and then call the `deserialize()` method:

    MyClass my_new_obj = (MyClass) ser.deserializeAs(e, MyClass.class);

You could check for yourself that the member fields of `my_obj` and
`my_new_obj` are identical. Note that you don't need to use the same
instance serializer for both operations, or be in the same program when
saving and loading.

This, in a nutshell, is how Azrael works (and how other serialization
libraries work too, although with some peculiarities).

Dependencies
------------

The library [json-lif](https://github.com/liflab/json-lif) should be in your
classpath to use JSON serialization.

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

Specifying class loaders
------------------------

If you use Azrael as a library within your own project, it cannot
instantiate objects outside of the basic Java classes out of the box.
You need to give one or more *class loaders* that will enable it to
create instances of your objects.

Suppose for example that you have a package called `my.package`; to
help Azrael create objects from this package, do the following:

    my_serializer.addClassLoader(my.package.MyClass.class.getClassLoader());

where `MyClass` is any of the classes of `my.package`. This should normally
be enough for all classes of that package. You can add more than one
class loader to Azrael; when it attempts to instantiate an object, it tries
them all until one of them works.

Projects that use Azrael
------------------------

- [Cornipickle](https://github.com/liflab/cornipickle), a web testing tool
- [LabPal](https://liflab.github.io/labpal), a library for running
  experiments on a computer
- The [Serialization palette](https://github.com/liflab/beepbeep-3-palettes)
  of the [BeepBeep 3](https://liflab.github.io/beepbeep-3) event stream
  processing engine.

About the author                                                   {#about}
----------------

Azrael was written by Sylvain Hallé, associate professor at Université
du Québec à Chicoutimi, Canada.
