/*
    Azrael, a serializer for Java objects
    Copyright (C) 2016-2019 Sylvain Hallé
    Laboratoire d'informatique formelle
    Université du Québec à Chicoutimi, Canada
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.
    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.azrael.json;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ca.uqac.lif.azrael.ObjectPrinter;
import ca.uqac.lif.azrael.ObjectReader;
import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.Printable;
import ca.uqac.lif.azrael.ReadException;
import ca.uqac.lif.azrael.Readable;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonString;

public class JsonTest 
{
	@Test
	public void testJson1() throws PrintException, ReadException
	{
		SimpleObject mp = new SimpleObject(3, "abc");
		JsonPrinter printer = new JsonPrinter();
		JsonElement je = printer.print(mp);
		assertNotNull(je);
		JsonReader reader = new JsonReader();
		Object o = reader.read(je);
		assertNotNull(o);
		assertTrue(o instanceof SimpleObject);
		SimpleObject so = (SimpleObject) o;
		assertEquals(3, so.m_x);
		assertEquals("abc", so.m_y);
	}
	
	@Test
	public void testJson2() throws PrintException, ReadException
	{
		ComplexObject co = new ComplexObject();
		co.add(new SimpleObject(3, "foo"));
		co.add(new SimpleObject(5, "bar"));
		JsonPrinter printer = new JsonPrinter();
		JsonElement je = printer.print(co);
		assertNotNull(je);
		JsonReader reader = new JsonReader();
		Object o = reader.read(je);
		assertNotNull(o);
		assertTrue(o instanceof ComplexObject);
		ComplexObject nco = (ComplexObject) o;
		assertEquals(2, nco.m_objects.size());
		assertEquals(3, nco.m_objects.get(0).m_x);
		assertEquals("foo", nco.m_objects.get(0).m_y);
		assertEquals(5, nco.m_objects.get(1).m_x);
		assertEquals("bar", nco.m_objects.get(1).m_y);
	}
	
	@Test
	public void testJson3() throws PrintException, ReadException
	{
		JsonMap map = new JsonMap();
		map.put("a", new JsonString("foo"));
		JsonList list = new JsonList();
		list.add(new JsonNumber(3));
		list.add(new JsonNumber(5));
		map.put("b", list);
		JsonPrinter printer = new JsonPrinter();
		JsonElement je = printer.print(map);
		assertNotNull(je);
		JsonReader reader = new JsonReader();
		Object o = reader.read(je);
		assertNotNull(o);
		assertTrue(o instanceof JsonMap);
		JsonMap nco = (JsonMap) o;
		assertEquals(2, nco.size());
		assertEquals(new JsonString("foo"), nco.get("a"));
	}
	
	@Test
	public void testJson4() throws PrintException, ReadException
	{
		NonPrintableObject co = new NonPrintableObject();
		co.add(new SimpleObject(3, "foo"));
		co.add(new SimpleObject(5, "bar"));
		JsonPrinter printer = new JsonPrinter();
		JsonElement je = printer.print(co);
		assertNotNull(je);
		JsonReader reader = new JsonReader();
		Object o = reader.read(je);
		assertNotNull(o);
		assertTrue(o instanceof NonPrintableObject);
		NonPrintableObject nco = (NonPrintableObject) o;
		assertEquals(2, nco.m_objects.size());
		assertEquals(3, nco.m_objects.get(0).m_x);
		assertEquals("foo", nco.m_objects.get(0).m_y);
		assertEquals(5, nco.m_objects.get(1).m_x);
		assertEquals("bar", nco.m_objects.get(1).m_y);
	}
	
	@Test
	public void testEnum1() throws PrintException, ReadException
	{
		EnumObject eo = new EnumObject();
		JsonPrinter printer = new JsonPrinter();
		JsonElement je = printer.print(eo);
		assertNotNull(je);
		JsonReader reader = new JsonReader();
		Object o = reader.read(je);
		assertNotNull(o);
		assertTrue(o instanceof EnumObject);
		EnumObject nco = (EnumObject) o;
		assertEquals(nco.me, EnumObject.MyEnum.FOO); 
	}
	
	@Test
	public void testByteArray1() throws PrintException, ReadException
	{
		JsonPrinter printer = new JsonPrinter();
		JsonElement je = printer.print(new byte[] {0, 1, 2});
		assertNotNull(je);
		JsonReader reader = new JsonReader();
		Object o = reader.read(je);
		assertNotNull(o);
		assertTrue(o instanceof byte[]);
		byte[] nco = (byte[]) o;
		assertEquals(3, nco.length); 
	}
	
	protected static class SimpleObject implements Printable, Readable
	{
		int m_x;
		
		String m_y;
		
		/**
		 * A readable object must have a no-args constructor
		 */
		protected SimpleObject()
		{
			this(0, "");
		}
		
		public SimpleObject(int x, String y)
		{
			super();
			m_x = x;
			m_y = y;
		}
		
		@Override
		public Object print(ObjectPrinter<?> printer) throws PrintException
		{
			Map<String,Object> contents = new HashMap<String,Object>();
			contents.put("x", m_x);
			contents.put("y", m_y);
			return printer.print(contents);
		}

		@SuppressWarnings("unchecked")
		@Override
		public SimpleObject read(ObjectReader<?> reader, Object o) throws ReadException 
		{
			Map<Object,Object> contents = (Map<Object,Object>) reader.read(o);
			int x = ((Number) contents.get("x")).intValue();
			String y = (String) contents.get("y");
			return new SimpleObject(x, y);
		}
	}
	
	protected static class ComplexObject implements Printable, Readable
	{
		List<SimpleObject> m_objects;
		
		/**
		 * A readable object must have a no-args constructor
		 */
		public ComplexObject()
		{
			super();
			m_objects = new ArrayList<SimpleObject>();
		}
		
		public void add(SimpleObject o)
		{
			m_objects.add(o);
		}
		
		@Override
		public Object print(ObjectPrinter<?> printer) throws PrintException
		{
			return printer.print(m_objects);
		}

		@Override
		public ComplexObject read(ObjectReader<?> reader, Object o) throws ReadException 
		{
			List<?> contents = (List<?>) reader.read(o);
			ComplexObject co = new ComplexObject();
			for (Object l_o : contents)
			{
				SimpleObject s_o = (SimpleObject) l_o;
				co.add(s_o);
			}
			return co;
		}
	}
	
	protected static class NonPrintableObject
	{
		List<SimpleObject> m_objects;
		
		/**
		 * A readable object must have a no-args constructor
		 */
		public NonPrintableObject()
		{
			super();
			m_objects = new ArrayList<SimpleObject>();
		}
		
		public void add(SimpleObject o)
		{
			m_objects.add(o);
		}
	}
	
	protected static class EnumObject
	{
		public static enum MyEnum {FOO, BAR}
		
		public MyEnum me = MyEnum.FOO;
	}
}
