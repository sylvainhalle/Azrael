Buffy: store structured data in compact binary form
===================================================

Buffy is an extension of the [Azrael](https://github.com/sylvainhalle/Azrael) serialization library that aims to store data structures using as few bytes as possible. As an example, consider a data structure which, in JSON notation, would be represented as follows:

```json
{
  "a" : true,
  "b" : "hello world"
  "c" : [
      {"foo" : 1, "bar" : 2, "baz" : 3},
	  {"foo" : 4, "bar" : 5, "baz" : 6},
	  {"foo" : 7, "bar" : 8, "baz" : 9}
  ]
}
```

The table shows how such a Java object (fundamentally a `Map`) could be saved in various formats:

| Format                | Size (bits)   |
| :-------------------- | ------------: |
| ObjectOutputStream    |         4,296 |
| Azrael JSON           |         2,912 |
| bson4jackson          |         1,128 |
| Plain JSON            |           888 |
| CBOR                  |           584 |
| Smile                 |           480 |
| **Buffy**             |       **149** |

A plain JSON document, even stripped of all possible whitespace, ends up taking almost 6 times the space of the Buffy structure. Even the [Smile](https://en.wikipedia.org/wiki/Smile_(data_interchange_format) format produces a binary string that is more than 3 times larger. (By the way, this little benchmark is in the `Examples` folder of this project.)

In order to achieve such high compression, Buffy relies on three main principles:

1. The elementary unit of serialization is a **bit**, not a *byte*. That is, if an object requires 3 bits to be represented, then 3 bits are written --no padding to fill a byte is applied. As a rule, Buffy does not require that objects start at byte boundaries, have a size that is a multiple of 8, or other such considerations.

2. Primitive data is stored in **compact binary form**. For example, a Boolean value is stored using a single *bit*, integers can use the lowest number of bits to represent their range of values, strings using a simplified character set can be represented with 6 bits per symbol, and enumerated types with *n* values are stored using ⌈log₂*n*⌉ bits.

3. Stored objects contain **no signaling information** about their structure. That is, one cannot recover an object from a binary string without knowledge of the object's composition.

Example
-------

To create a serializer for the object above:

```java
FixedMapSchema schema = new FixedMapSchema(new MarshalledSchema()
  .add(String.class, SmallsciiSchema.instance)
  .add(Boolean.class, BooleanSchema.instance)
  .add(List.class, new ListSchema(
    new FixedMapSchema(IntSchema.int4, "foo", "bar", "baz"))),
  "a", "b", "c");
```

Assuming that `map` contains a Map of the appropriate structure, serializing it as bits (and then bytes) is done by:

```java
BitSequence s1 = schema.print(map);
byte[] as_bytes = s1.toByteArray();
```

The reverse operation is simple:

```java
BitSequence s2 = new BitSequence(as_bytes);
Map<String,?> new_map = schema.read(s1);
```

Bit sequences can be saved as byte arrays and then written in the usual way:

```java
ByteArrayOutputStream b1 = ...
b1.write(s1.toByteArray());

ByteArrayInputStream b2 = ...
BitSequence seq = new BitSequence(b2.getBytes());
```

If a bit sequence does not correspond to a round number of bytes, it is padded with zeros when saving to a byte array (the only time any padding occurs in the system). However, the deserialization is robust to this padding.

Schemas
-------

To avoid saving information about the structure of an object, Buffy introduces the concept of a `Schema`, which can be seen as a binary serializer/deserializer specific to a single type of object.

Each schema defines a method called `print()`, which takes an object and produces a `BitSequence`. In turn, each schema also has a method called `read()`, which takes a `BitSequence` and outputs an object of the given type. What differs in each class implementing `Schema` is the kind of objects that are accepted as input, and how they convert these objects into a sequence of bits. To serialize a complex object, you typically create a schema by composing (i.e. nesting) multiple schemas together.

### `BooleanSchema`

The `BooleanSchema` is the simplest of all: it serializes a Boolean value by writing a single bit depending on whether the input is false (0) or true (1).

```java
BitSequence s = BooleanSchema.instance.print(false); // s contains 0
Boolean b = BooleanSchema.instance.read(s); // b == false
BooleanSchema.instance.read("hello"); // Throws a PrintException
```

This code example also shows that a `PrintException` is thrown when the object passed to `print` is not handled by this specific schema.

### `IntSchema`

The `IntSchema` serializes an integer value. It comes in various versions depending on the number of bits (impacting its range) and whether it is signed or not. For example, `IntSchema.uInt4` corresponds to an unsigned integer of 4 bits (range 0-15), while `IntSchema.sInt64` is a signed 64-bit integer (i.e. a Java `Long`).

```java
BitSequence s = IntSchema.uint4.print(11); // s contains 1011
Integer i = IntSchema.uint16.read(s); // Throws a ReadException
```

The second instruction throws a `ReadException` saying "Not enough bits to read". Indeed, the schema for a 16-bit integer is asked to read a sequence of 4 bits.

### `EnumSchema`

It is possible to serialize fixed Java objects by simply associating them with a short binary sequence. The `EnumSchema` acts as the bridge between an enumeration of objects and a binary representation.

```java
List<Integer> my_list = Arrays.asList(3, 1, 4);
EnumSchema schema = new EnumSchema("foo", false, my_list);
BitSequence s = schema.print(my_list); // s contains 10
Object recovered = schema.read(s); // recovered == my_list
schema.read(new BitSequence("11")); // Throws a ReadException
```

In the code above, `EnumSchema` is given three objects (a string, a Boolean and a list), and will serialize each of them respectively as the numerical values 0, 1 and 2. The enum uses as few bits as possible for the number of values (here, two bits are sufficient). The last line throws an exception, as no object corresponds to number 3 (`11`).

### `BlobSchema`

The blob schema takes a sequence of bits and serializes it as is. It prepends to this sequence the length in bits, encoded as a 32-bit unsigned integer. Thus the largest blob that can be saved is exactly 512 megabytes.

```java
BitSequence s = BlobSchema.instance.print(new BitSequence("10110101011101"));
```

The blob schema can be used as a "catch-all" format to serialize objects that are not directly supported by Buffy.

### `StringSchema`

The `StringSchema` serializes a string. It comes in three flavors depending on how the string is written.

- `StringBlobSchema` takes a string and dumps it as a blob (see above). The encoding of the string is irrelevant as it is printed as a byte array.
- `SmallsciiSchema` uses a restricted character set made of lowercase letters, numbers and punctuation. Such strings can be printed using 6 bits per character, instead of 8 or 16 for other character sets. These strings are terminated 
- `HuffStringSchema` uses [Huffman coding](https://en.m.wikipedia.org/wiki/Huffman_coding) to encode a string of text, using a predetermined Huffman tree (which must be calculated beforehand).

### `ListSchema`

The `ListSchema` serializes a list. It is the first example of a *compound* schema: it is parameterized with the schema of the elements of the list.

```java
ListSchema schema = new ListSchema(IntSchema.uint4);
BitSequence s = schema.print(Arrays.asList(3, 1, 4));
```

The serialization of this list produces the following binary string:

<table border="1">
<tr><td>0000000000000011</td><td>0011</td><td>0001</td><td>0100</td></tr>
<tr><td>Length: 3</td><td>3</td><td>1</td><td>4</td></tr>
</table>

The length is encoded on 16 bits, meaning that a list can have at most 65,536 elements.

The `NupleSchema` is a variant of a list where the length is known in advance. It spares the schema from writing this length, thus saving those 16 bits.

### `FixedMap`

The fixed map serializes an associative map whose set of keys is fixed and known in advance. This is one example where considerable space savings can be obtained, as only values need to be serialized.

```java
FixedMapSchema schema = new FixedMapSchema(
  IntSchema.uint4, "foo", "bar", "baz");
Map<String,Integer> my_map = new HashMap<>();
my_map.put("foo", 3);
my_map.put("bar", 6);
BitSequence s = schema.print(my_map);
```

This produces a schema for a map with three keys, associated to values that are expected to be Boolean.

The serialization of this map produces the following binary string:

<table border="1">
<tr><td>1</td><td>0110</td><td>0</td><td>1</td><td>0011</td></tr>
<tr><td>bar is defined</td><td>bar = 6</td><td>baz is not defined</td><td>foo is defined</td><td>foo = 3</td></tr>
</table>

Note how only 11 bits are necessary to encode the map. Values are stored in sorted key order. Also note that a map can have undefined (i.e. null) values for some of its keys:

```java
Map<String,?> recovered = schema.read(s);
boolean b = recovered.containsKey("baz"); // b == false
```

### `MarshalledSchema`

There are situations where the object to serialize/deserialize is not of a fixed type, but may be chosen from a finite set of possible types. The `MarshalledSchema` can take care of this: it is given a list of possible schemas, and takes care of serializing an object by advertising (i.e. "marshalling") its type before serializing it.

```java
MarshalledSchema schema = new MarshalledSchema(
  String.class, SmallsciiSchema.instance,
  List.class, new ListSchema(IntSchema.uint4)
);
BitSequence s1 = schema.print(Arrays.asList(3, 1, 4));
BitSequence s2 = schema.print("hello");
```

The bit sequence `s1` produced in the previous example is the following:

<table border="1">
<tr><td>0001</td><td>0000000000000011</td><td>0011</td><td>0001</td><td>0100</td></tr>
<tr><td>Schema: 1</td><td>Length: 3</td><td>3</td><td>1</td><td>4</td></tr>
</table>

Note that the list is preceded by a sequence of 4 bits indicating the schema number (thus at most 16 different schemas can be provided). This information is used at deserialization to use the appropriate schema to recover the object.

```java
Object o1 = schema.read(s1); // o1 == [3, 1, 4]
Object o2 = schema.read(s2); // o2 == "hello"
```

### Custom schemas

One can also write custom schemas for arbitrary objects. It suffices to define a class that implements the `Schema` interface (i.e. methods `print` and `read`). This is typically done as a static inner class to the class to be serialized.

```java
class C {
  int x = 0;
  String y = "";
  
  public C(int x, String y) {
    this.x = x;
    this.y = y;
  }
  
  static class CSchema implements Schema {
  
    public BitSequence print(Object o) throws PrintException {
      BitSequence s = IntSchema.uint32.print(((MyClass) o).x);
      s.addAll(SmallsciiSchema.instance.print(((MyClass) o).y));
      return s;
    }
    
    public C read(BitSequence s) throws ReadException {
      int x = IntSchema.uint32.read(s);
      String y = SmallsciiSchema.instance.read(s);
      return new C(x, y);
    }
  }
}
```

Once created, the schema can be used to write objects of class `C`:

```java
BitSequence s = C.CSchema.print(new C(3, "foo"));
C c = C.CSchema.read(s);
```

Serializing Schemas
-------------------

A `Schema` is like any other object, meaning that it, too, can be serialized. Buffy provides the `ReflectiveSchema`, which can convert the other schemas of the library to bit sequences. Thus:

```java
```

An object that is paired with the schema used to recover it is called a `SelfDescribedObject`.

Compression
-----------

To assess how compact a Buffy structure is, we can run a small experiment which consists of serializing it, and then attempting to compress it using a classical utility, such as `gzip`. To this end, we generated a structure similar to the example at the very top of this page, where the key "c" in the map is associated to a list of 1,000 foo-bar-baz maps of randomly generated integers.


| Format                | Size (bits)   | Compressed size (bits) | Saving   |
| :-------------------- | ------------: | ---------------------: | -------: |
| Smile                 |        64,400 |                 21,952 |    66.3% |
| Buffy                 |         1,910 |                  1,888 |     0.3% |

As one can see, gzip cannot compress the Buffy binary file any further, indicating it contains almost no redundancy. In contrast, the Smile file can still be reduced by almost two thirds. This is of course anecdotal evidence, as it depends on the structure and the data it contains, but it shows that the tricks used by Buffy can *sometimes* be as efficient as applying dictionary-based compression such as LZW. (But not always; for example, replacing random numbers by a repetitive pattern results in a file that can still be compressed by a large margin. However, the Buffy compressed file is still smaller than the Smile one.)

Related Work
------------

Buffy can be seen as an improved and simplified successor to [BufferTannen](https://github.com/sylvainhalle/BufferTannen), another library with the same design goals.

About the Author
----------------

Buffy is developed by Sylvain Hallé, Full Professor at [Université du Québec à Chicoutimi](https://www.uqac.ca/), Canada  and head of [LIF](https://liflab.ca/), the Laboratory of Formal Computer Science ("Laboratoire d'informatique formelle").

<!-- :wrap=soft:mode=markdown: -->