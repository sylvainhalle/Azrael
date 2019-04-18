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
package ca.uqac.lif.azrael.xml;

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
import ca.uqac.lif.xml.XmlElement;
import ca.uqac.lif.xml.TextElement;

public class XmlTest 
{
	@Test
	public void testJson1() throws PrintException, ReadException
	{
		SimpleObject mp = new SimpleObject(3, "abc");
		XmlPrinter printer = new XmlPrinter();
		XmlElement je = printer.print(mp);
		assertNotNull(je);
		XmlReader reader = new XmlReader();
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
		XmlPrinter printer = new XmlPrinter();
		XmlElement je = printer.print(co);
		assertNotNull(je);
		XmlReader reader = new XmlReader();
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
	public void testJson4() throws PrintException, ReadException
	{
		NonPrintableObject co = new NonPrintableObject();
		co.add(new SimpleObject(3, "foo"));
		co.add(new SimpleObject(5, "bar"));
		XmlPrinter printer = new XmlPrinter();
		XmlElement je = printer.print(co);
		assertNotNull(je);
		XmlReader reader = new XmlReader();
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
}
