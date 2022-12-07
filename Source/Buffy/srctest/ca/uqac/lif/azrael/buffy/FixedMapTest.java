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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

public class FixedMapTest
{
	@Test
	public void testPrint1() throws PrintException
	{
		FixedMapSchema schema = new FixedMapSchema(SmallsciiSchema.instance, "foo", "bar");
		Map<String,String> map = new HashMap<String,String>();
		map.put("foo", "hello");
		BitSequence bs = schema.print(map);
		assertEquals(38, bs.size());
		System.out.println(bs);
	}
	
	@Test(expected = PrintException.class)
	public void testPrint2() throws PrintException
	{
		FixedMapSchema schema = new FixedMapSchema(SmallsciiSchema.instance, "foo", "bar");
		Map<String,String> map = new HashMap<String,String>();
		map.put("baz", "hello");
		schema.print(map);
	}
	
	@Test
	public void testRead1() throws ReadException
	{
		BitSequence bs = new BitSequence("01001000000101001100001100001111000000");
		FixedMapSchema schema = new FixedMapSchema(SmallsciiSchema.instance, "foo", "bar");
		Map<String,?> map = schema.read(bs);
		assertEquals("hello", map.get("foo").toString());
		assertNull(map.get("bar"));
	}
}
