Azrael: a universal serialization library for Java
==================================================

![Azrael the cat](azrael.jpg?raw=true)

<img src="http://leduotang.ca/Azrael.svg" height="16" alt="Downloads"/>

Azrael is a [serialization](https://en.wikipedia.org/wiki/Serialization)
library for Java. It allows Java objects to be saved ("serialized") to
some data format, and to be reconstructed ("deserialized") from that saved
data at a later time.

The formt to which objects are serialized is parameterizable. 
By default, Azrael comes with a serializer for
[JSON](https://en.wikipedia.org/wiki/JSON) and XML, but its
API allows you to easily implement serialization to your own custom data
format.

[Read the API documentation](https://sylvainhalle.github.io/Azrael/javadoc)

How it works
------------

Suppose you have the following class:

```java
class MyClass {
  int x = 0;
  String s = "";
  List<Float> f = new LinkedList<Float>();
  
  public add(Float x) {
	f.add(x);
  }
}
```

You would like to serialize the state of objects of class `MyClass` to
JSON. With Azrael, you can do the following:

```java
// Create an object and fill with values
MyClass my_obj = new MyClass();
my_obj.x = 1; my_obj.s = "abc"; my_obj.add(1.5);

// Create a serializer for JSON
JsonPrinter p = new JsonPrinter();
JsonElement e = p.print(my_obj);
```

The contents of `my_obj` are saved in a JSON element, which you can save
somewhere as a string using its `toString()` method.

Now suppose you want to reconstruct an object of `MyClass` with the exact
data that was contained in the saved JSON. You first reconstruct the JSON
element `e` (from a String, etc.), and then call the `read()` method:

```java
JsonReader d = new JsonReader();
MyClass my_new_obj = (MyClass) d.read(e);
```

You could check for yourself that the member fields of `my_obj` and
`my_new_obj` are identical. Note that you don't need to use the same
instance serializer for both operations, or be in the same program when
saving and loading.

This, in a nutshell, is how Azrael works (and how other serialization
libraries work too, although with some peculiarities).

Features
--------

Azrael was developed out of insatisfaction with existing (JSON)
serialization libraries, mostly
[Java Google Gson](https://code.google.com/p/google-gson/) (Gson) and
[Genson](http://owlike.github.io/genson/). Here are a couple of features
of Azrael for which other libraries didn't fit the author's needs.

### Truly generic

Existing serialization libraries are tied to a single specific output
format. Hence, if you want to serialize objects to another format than JSON
(XML, strings, whatever custom format you wish), a JSON library can't help
you. You need to use yet another library (such as
[XStream](http://x-stream.github.io/) for XML).

**Not so with Azrael.** Its default `ObjectPrinter` and `ObjectReader`
classes take care of much of the work of decomposing an object and
determining type information. JSON is just one possible way of implementing
the abstract methods of these two classes. To serialize to another format,
simply override them in a different way to produce the output you wish. (As
a matter of fact, the code specific to JSON serialization is a mere 600
lines long.) The serialized object is not even aware of the format it is
being serialized into.

This means that, in the example above, if you want to serialize `my_obj`
into XML, you simply pass `my_obj` to another type of object printer:

```java
XmlPrinter p = new XmlSerializer();
XmlElement e = p.print(my_obj);
```

Another nice consequence of Azrael's structure is that you can write
serializers that do not even perform serialization. For example:

- The `Core` folder provides an implementation of a serializer that prints
  an object as itself. The deserializer deserializes primitive values as
  themselves, and deserializes other objects as new instances of themselves.
  The end result is a process that performs a
  [deep copy](https://en.wikipedia.org/wiki/Object_copying) of an object.
- The `Size` folder contains an implementation of a serializer that turns
  any object into a number. This can be used to compute the size of an
  arbitrary object using just 350 lines of code.
  
### Not forced to use reflection

Most libraries use reflection to serialize an object. Some may argue that
this is a "brutal" process: the object is not aware it is being serialized,
its internal contents are revealed bare (totally disregarding visibility
modifiers), and has no control over what and how things are serialized. The
reverse operation creates an empty object skeleton, and forcefully populates
its member fields --effectively treating the object as an inert
"[bag of data](https://www.yegor256.com/2016/07/06/data-transfer-object.html)".

In contrast, an object can choose to cooperate with Azrael by implementing
the `Printable` interface to serialize itself of its own will, and the
`Readable` interface to create a new instance from a serialized version.
What is more, the object does not need to be aware of the format to which it
is serialized: Azrael takes care of that.

As an example, consider again the class `MyClass`:

```java
class MyClass implements Printable, Readable {
  int x = 0;
  String s = "";
  List<Float> f = new LinkedList<Float>();
  
  public add(Float x) {
	f.add(x);
  }
  
  public Object print(ObjectPrinter<?> printer) {
	List<Object> list = new ArrayList<Object>();
	list.add(System.currentTimeMillis() / 1000);
	list.add(x);
	list.add(s);
	list.add(f);
	return printer.print(list);
  }
  
  public Object read(ObjectReader<?> reader, Object o) throws ReadException {
	List<Object> list = (List<Object>) reader.read(o);
	long now = System.currentTimeMillis() / 1000;
	if (now - (Long) list.get(0) > 600) {
	  throw new ReadException("Copy is too old");
	}
	MyClass mc = new MyClass((Integer) list.get(1), (String) list.get(2));
	for (float f : (List<Float>) list.get(3)) {
	  mc.add(f);
	}
	return mc;
  }
}
```

This time, the class implements `Printable`, and decides what and how to
print its contents. In this case, the choice is to use a list; notice how
the first element of that list is *not* even part of the object's state
(in this case, a timestamp indicating when the serialization was made).
The object simply asks an anonymous `ObjectPrinter` to print the contents of
this list --whether this is done with JSON, XML or something else is
completely irrelevant to the class.

Similarly, the `read` method implements the `Readable` interface. Notice
how the object asks an `ObjectReader` to deserialize an arbitrary object
`o`, which recovers the list that was saved by `print`. Again, exactly what
is `o` (XML? JSON? something else?) is irrelevant. The contents of the
list are used to recreate a new instance of `MyClass`, but through means
that the object itself controls. Remark how the timestamp that was
serialized by the object is used to throw an exception when the copy is
too old.

### No reliance on declared type

When serializing member fields of an object, Azrael inserts information
about the actual type of an object, and not the type that is declared in
a class. Consider the following example:

```java
abstract class A { 
}

class B extends A {
  int x = 0;
}

class C {
  A my_b = new B();
}
```

When deserializing an object of class `C`, other libraries run into a
problem, as they try to instantiate an object of class `A`, since this is
the *declared* type of field `my_b`. But `A` is an abstract class, and
cannot be instantiated. To handle this case with Gson, you need to write
yet more custom code to take care of this (not quite) exceptional
situation.

**Not so with Azrael**, which takes care of adding to the serialization that
the actual class of `my_b` is `B`, enabling it to properly deserialize the
object. Works out of the box, period.

This means that you can serialize generic collections such as lists, sets
and maps easily. In many other serialization libraries, "collections require special
treatment since the Collections are of generic type and the type information
is lost while converting to JSON due to Java type erasure" (says the [Gson
documentation](http://www.studytrails.com/java/json/java-google-json-introduction.jsp)).

**Not so with Azrael**, which *does* takes care of serializing the exact
class of each element in a collection (list, map, set). Hence you can write,
as you would for any other object:

```java
List<Integer> list1 = new LinkedList<Integer>();
(...Fill the list with stuff...)
Object o = serializer.print(list1);
List<Integer> list2 = (List<Integer>) serializer.read(o);
```

The contents of `list2` recreate precisely the original objects with their
*actual* (not declared) type. No custom code is needed (contrary to what
Gson requires).

Specifying class loaders
------------------------

If you use Azrael as a library within your own project, it cannot
instantiate objects outside of the basic Java classes out of the box.
You need to give one or more *class loaders* that will enable it to
create instances of your objects.

Suppose for example that you have a package called `my.package`; to
help Azrael create objects from this package, do the following:

```java
my_serializer.addClassLoader(my.package.MyClass.class.getClassLoader());
```

where `MyClass` is any of the classes of `my.package`. This should normally
be enough for all classes of that package. You can add more than one
class loader to Azrael; when it attempts to instantiate an object, it tries
them all until one of them works.

Custom handlers
---------------

If you would like to serialize objects that do not implement `Printable` and
`Readable` in a special way, you can do so by creating a custom
`PrintHandler` and `ReadHandler`. The print handler must implement a method
called `canHandle`, which must return `true` when given an object it can
serialize (and `false` otherwise). The method `handle` should contain custom
code that takes care of printing the content of that object. Conversely, the
read handler also has a method called `canHandle`, and another called
`read` which should should contain custom code that takes care of reading
the content of that object.

Dependencies
------------

This project is separated in two parts:

- The `Core` folder generates a small JAR file that only defines the
  *interfaces* to support serialization. If you develop a library and
  want your objects to be serialized, simply include this JAR in your
  project.
- The other folders implement serialization in a variety of formats. For
  example, the `Json` folder provides a JSON serializer; the `Xml`
  folder provides an XML serializer. These JARs
  may themselves have dependencies; for example, the library
  [json-lif](https://github.com/liflab/json-lif) should be in your
  classpath to use JSON serialization.

Projects that use Azrael
------------------------

- [Cornipickle](https://github.com/liflab/cornipickle), a web testing tool
- [LabPal](https://liflab.github.io/labpal), a library for running
  experiments on a computer
- The [Serialization palette](https://github.com/liflab/beepbeep-3-palettes)
  and [Hibernate palette](https://github.com/liflab/beepbeep-3-palettes)
  of the [BeepBeep 3](https://liflab.github.io/beepbeep-3) event stream
  processing engine.

About the name
--------------

All the letters of "Azrael" are contained in the word "serialization".
Anything to add?

About the author                                                   {#about}
----------------

Azrael was written by Sylvain Hallé, professor at Université
du Québec à Chicoutimi, Canada.

<!-- :maxLineLen=76: -->