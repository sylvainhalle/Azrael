/*
    Azrael, a serializer for Java objects
    Copyright (C) 2016-2022 Sylvain Hallé
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
package ca.uqac.lif.azrael.buffy;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

public class EnumSchemaTest
{
	@Test
	public void test1() throws PrintException, ReadException
	{
		EnumSchema s = new EnumSchema("foo bar", 73, false);
		BitSequence seq = s.print("foo bar");
		assertEquals(2, seq.size());
		Object recovered = s.read(seq);
		assertEquals("foo bar", (String) recovered);
	}
	
	@Test
	public void test2() throws PrintException, ReadException
	{
		EnumSchema s = new EnumSchema("foo bar", 73, false);
		BitSequence seq = s.print(73);
		assertEquals(2, seq.size());
		Object recovered = s.read(seq);
		assertEquals(73, (Number) recovered);
	}
	
	@Test
	public void test3() throws PrintException, ReadException
	{
		EnumSchema s = new EnumSchema("foo bar", 73, false);
		BitSequence seq = s.print(false);
		assertEquals(2, seq.size());
		Object recovered = s.read(seq);
		assertEquals(false, (Boolean) recovered);
	}
	
	@Test(expected = PrintException.class)
	public void test4() throws PrintException, ReadException
	{
		EnumSchema s = new EnumSchema("foo bar", 73, false);
		s.print(true);
	}
	
	@Test(expected = ReadException.class)
	public void test5() throws PrintException, ReadException
	{
		EnumSchema s = new EnumSchema("foo bar", 73, false);
		BitSequence seq = new BitSequence("11");
		s.read(seq);
	}
}
