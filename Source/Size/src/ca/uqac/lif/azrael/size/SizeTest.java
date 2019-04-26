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
package ca.uqac.lif.azrael.size;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import ca.uqac.lif.azrael.PrintException;

public class SizeTest
{
	protected SizePrinter m_printer = new SizePrinter();
	
	@Test
	public void testString() throws PrintException
	{
		String s = "Hello world";
		int size = (Integer) m_printer.print(s);
		assertTrue(size > 0);
	}
	
	@Test
	public void testList() throws PrintException
	{
		List<SimpleObject> list = new LinkedList<SimpleObject>();
		
		SimpleObject o1 = new SimpleObject(0, "foo");
		int so_size = (Integer) m_printer.print(o1);
		m_printer.reset();
		SimpleObject o2 = new SimpleObject(10, "bar");
		list.add(o1);
		int size1 = (Integer) m_printer.print(list);
		assertTrue(size1 > 0);
		list.add(o2);
		m_printer.reset();
		int size2 = (Integer) m_printer.print(list);
		assertTrue(size2 > size1);
		list.add(o1);
		m_printer.reset();
		int size3 = (Integer) m_printer.print(list);
		assertTrue(size3 > size2);
		// This ensures that the size of o1 is not counted twice
		assertTrue(size3 - size2 < so_size);
	}
	
	@Test
	public void testArray1() throws PrintException
	{
		int[] arr = new int[] {1, 2, 3};
		int so_size = (Integer) m_printer.print(arr);
		assertTrue(so_size > 0);
	}
	
	public static class SimpleObject
	{
		int m_x;
		
		String m_y;
		
		public SimpleObject(int x, String y)
		{
			super();
			m_x = x;
			m_y = y;
		}
	}
}
