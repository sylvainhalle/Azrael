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
| Azrael JSON (gzipped) | 2,912 (1,416) |
| bson4jackson          |         1,128 |
| Plain JSON (gzipped)  |     888 (824) |
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

It is possible to associate arbitrary Java objects 

```java
List<Integer> my_list = Arrays.asList(3, 1, 4);
EnumSchema schema = new EnumSchema("foo", false, my_list);
BitSequence s = schema.print(my_list); // s contains 10
Object recovered = schema.read(s); // recovered == my_list
schema.read(new BitSequence("11")); // Throws a ReadException
```

The last line throws an exception, as the enumeration has only three associated objects (numbered 0-1-2), so no object corresponds to number 3 (11).

### `StringSchema`

The `StringSchema` serializes a string. It comes in three flavors depending on how the string is written.

- `StringBlobSchema` takes a string and dumps it as a blob. The encoding of the string is irrelevant as it is printed as a byte array.
- `SmallsciiSchema` uses a restricted character set made of lowercase letters, numbers and punctuation. Such strings can be printed using 6 bits per character, instead of 8 or 16 for other character sets.
- `HuffStringSchema` uses [Huffman coding](https://en.m.wikipedia.org/wiki/Huffman_coding) to encode a string of text, using a predetermined Huffman tree (which must be calculated beforehand).


<!-- :wrap=soft:mode=markdown: -->