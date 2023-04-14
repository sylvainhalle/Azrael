package ca.uqac.lif.azrael.buffy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.json.JsonStringPrinter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;

import de.undercouch.bson4jackson.BsonFactory;

public class SelfDescribedTest 
{
	public static void main(String[] args) throws PrintException, IOException
	{
		/* Create the object. */
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("a", true);
		map.put("b", "hello world");
		map.put("c", Arrays.asList(new Map[] {
				getMap(1, 2, 3), getMap(4, 5, 6), getMap(7, 8, 9)}));

		/* Option 1: create a Buffy schema for this object, and serialize it. */
		{
			FixedMapSchema s = new FixedMapSchema(new VariantSchema()
					.add(String.class, SmallsciiSchema.instance)
					.add(Boolean.class, BooleanSchema.instance)
					.add(List.class, new ListSchema(
							new FixedMapSchema(IntSchema.int4, "foo", "bar", "baz"))),
					"a", "b", "c");
			SelfDescribedObject sdo = new SelfDescribedObject(s, map);
			BitSequence seq = SelfDescribedObject.schema.print(sdo);
			System.out.println(seq);
			System.out.println("With Buffy: " + seq.size());
			ReflectiveSchema rs = new ReflectiveSchema();
			BitSequence schema_s = rs.print(sdo.m_schema);
			System.out.println("Only schema: " + schema_s.size());
		}

		/* Option 2: print the object in an ObjectOutputStream. */
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(map);
			oos.close();
			System.out.println("With an ObjectOutputStream: " + baos.toByteArray().length * 8);
		}

		/* Option 3: serialize the object using Azrael JSON. */
		{
			System.out.println("As an Azrael JSON: " + JsonStringPrinter.toJson(map).length() * 8);
			System.out.println(JsonStringPrinter.toJson(map));
		}

		/* Option 4: serialize the object using bson4jackson. */
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectMapper mapper = new ObjectMapper(new BsonFactory());
			mapper.writeValue(baos, map);
			System.out.println("With bson4jackson: " + baos.toByteArray().length * 8);
		}

		/* Option 5: serialize the object using Smile. */
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			SmileFactory smileFactory = new SmileFactory();
			ObjectMapper smileMapper = new ObjectMapper(smileFactory);
			smileMapper.writeValue(baos, map);
			System.out.println("With Smile: " + baos.toByteArray().length * 8);
		}
		
		/* Option 6: serialize the object using CBOR. */
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			CBORFactory cborFactory = new CBORFactory();
			ObjectMapper cborMapper = new ObjectMapper(cborFactory);
			cborMapper.writeValue(baos, map);
			System.out.println("With CBOR: " + baos.toByteArray().length * 8);
		}

	}

	protected static Map<String,Integer> getMap(int foo, int bar, int baz)
	{
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("foo", foo);
		map.put("bar", bar);
		map.put("baz", baz);
		return map;
	}
}