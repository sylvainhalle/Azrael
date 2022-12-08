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

A plain JSON document, even stripped of all possible whitespace, ends up taking almost 6 times the space of the Buffy structure. Even the [Smile](https://en.wikipedia.org/wiki/Smile_(data_interchange_format) format produces a binary string that is more than 3 times larger.

In order to achieve such high compression, Buffy relies on three main principles:

1. The elementary unit of serialization is a **bit**, not a *byte*. That is, if an object requires 3 bits to be represented, then 3 bits are written --no padding to fill a round number of bytes is applied. As a rule, Buffy does not care that objects do not start at byte boundaries, have a size that is a power of 2, or other such considerations.

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
Map<?,?> new_map = schema.read(s1);
```

Schemas
-------

To avoid saving information about the structure of an object, Buffy introduces the concept of a `Schema`, which can be seen as a binary serializer/deserializer specific to a single type of object.

Each schema defines a method called `print()`, which takes an object and produces a `BitSequence`. In turn, each schema also has a method called `read()`, which takes a `BitSequence` and outputs an object of the given type. What differs in each class implementing `Schema` is the kind of objects that are accepted as input, and how they convert these objects into a sequence of bits.

### Elementary Schemas

Let us first examine

The `StringSchema` 

<!-- :wrap=soft:mode=markdown: -->