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
package ca.uqac.lif.azrael.clone;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.junit.Test;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

/**
 * Unit tests for the clone reader and printer.
 * @author Sylvain Hallé
 *
 */
public class CloneTest 
{
	/**
	 * The object printer used for the tests
	 */
	protected ClonePrinter m_printer = new ClonePrinter();
	
	/**
	 * The object reader used for the tests
	 */
	protected CloneReader m_reader = new CloneReader();
	
	@SuppressWarnings("unchecked")
	@Test
	public void testMap1() throws PrintException, ReadException
	{
		Map<String,Object> map_o = new HashMap<String,Object>();
		map_o.put("foo", "bar");
		map_o.put("baz", 10);
		map_o.put("abc", null);
		Object printed = m_printer.print(map_o);
		assertNotNull(printed);
		Object read = m_reader.read(printed);
		assertNotNull(read);
		assertFalse(printed == read);
		assertTrue(read instanceof HashMap);
		HashMap<String,?> converted = (HashMap<String,?>) read;
		assertEquals(3, converted.size());
	}
	
	@Test
	public void testList1() throws PrintException, ReadException
	{
		List<Object> map_o = new ArrayList<Object>();
		map_o.add("foo");
		map_o.add(10);
		Object printed = m_printer.print(map_o);
		assertNotNull(printed);
		Object read = m_reader.read(printed);
		assertNotNull(read);
		assertFalse(printed == read);
		assertTrue(read instanceof ArrayList);
		ArrayList<?> converted = (ArrayList<?>) read;
		assertEquals(2, converted.size());
	}
	
	@Test
	public void testQueue1() throws PrintException, ReadException
	{
		Queue<Object> map_o = new ArrayDeque<Object>();
		map_o.add("foo");
		map_o.add(10);
		Object printed = m_printer.print(map_o);
		assertNotNull(printed);
		Object read = m_reader.read(printed);
		assertNotNull(read);
		assertFalse(printed == read);
		assertTrue(read instanceof ArrayDeque);
		ArrayDeque<?> converted = (ArrayDeque<?>) read;
		assertEquals(2, converted.size());
	}
	
	@Test
	public void testSet1() throws PrintException, ReadException
	{
		Set<Object> map_o = new HashSet<Object>();
		map_o.add("foo");
		map_o.add(10);
		Object printed = m_printer.print(map_o);
		assertNotNull(printed);
		Object read = m_reader.read(printed);
		assertNotNull(read);
		assertFalse(printed == read);
		assertTrue(read instanceof HashSet);
		HashSet<?> converted = (HashSet<?>) read;
		assertEquals(2, converted.size());
	}
	
	@Test
	public void testInteger1() throws PrintException, ReadException
	{
		int i = 123;
		Object printed = m_printer.print(i);
		assertNotNull(printed);
		Object read = m_reader.read(printed);
		assertNotNull(read);
		assertTrue(read instanceof Integer);
		Integer converted = (Integer) read;
		assertEquals(123, converted.intValue());
	}
	
	@Test
	public void testString1() throws PrintException, ReadException
	{
		String i = "foo";
		Object printed = m_printer.print(i);
		assertNotNull(printed);
		Object read = m_reader.read(printed);
		assertNotNull(read);
		assertTrue(read instanceof String);
		String converted = (String) read;
		assertEquals("foo", converted);
	}
	
	@Test
	public void testBoolean1() throws PrintException, ReadException
	{
		Boolean i = false;
		Object printed = m_printer.print(i);
		assertNotNull(printed);
		Object read = m_reader.read(printed);
		assertNotNull(read);
		assertTrue(read instanceof Boolean);
		Boolean converted = (Boolean) read;
		assertEquals(false, converted);
	}
	
	@Test
	public void testNull1() throws PrintException, ReadException
	{
		Object i = null;
		Object printed = m_printer.print(i);
		assertNull(printed);
		Object read = m_reader.read(printed);
		assertNull(read);
	}
}
