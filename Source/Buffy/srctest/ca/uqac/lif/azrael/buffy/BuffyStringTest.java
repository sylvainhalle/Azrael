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

public class BuffyStringTest
{
	@Test(expected = PrintException.class)
	public void testInvalid1() throws PrintException
	{
		SmallsciiSchema.instance.print("Foo");
	}
	
	@Test
	public void testPrint1() throws PrintException
	{
		BitSequence bs = SmallsciiSchema.instance.print("foo");
		assertEquals(24, bs.size());
		System.out.println(bs.toString());
	}
	
	@Test
	public void testRead1() throws ReadException
	{
		BitSequence bs = new BitSequence("000110001111001111000000");
		Object bo = SmallsciiSchema.instance.read(bs);
		assertTrue(bo instanceof String);
		assertEquals("foo", bo.toString());
		assertEquals(0, bs.size());
	}
}
