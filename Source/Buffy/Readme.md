Buffy: store structured data in compact binary form
===================================================

Buffy is an extension of the [Azrael](https://github.com/sylvainhalle/Azrael) serialization library that aims to store data structures using as few bytes as possible. It relies on two main principles:

1. Primitive data is stored in **compact binary form**. For example, a Boolean value is stored using a single *bit*, strings using a simplified character set can be represented with 6 bits per symbol, and enumerated types with *n* values are stored using ⌈log₂*n*⌉ bits.

2. Stored objects contain **no signaling information** about their structure. That is, one cannot recover an object from a binary string without knowledge of the object's composition. To this end, Buffy introduces the concept of a `Schema`, which can be seen as a binary serializer/deserializer specific to a single type of object.

As an example, consider a data structure which, in JSON notation, would be represented as follows:

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

+-----------------------+---------------+
| Format                | Size (bits)   |
+-----------------------+---------------+
| ObjectOutputStream    | 4,296         |
| Azrael JSON (gzipped) | 2,912 (1,416) |
| bson4jackson          | 1,128         |
| Plain JSON (gzipped)  | 888 (824)     |
| **Buffy**             | **149**       |
+-----------------------+---------------+

A plain JSON document, stripped of all possible whitespace, ends up taking almost 6 times the space of the Buffy structure.

<!-- :wrap=soft:mode=markdown: -->