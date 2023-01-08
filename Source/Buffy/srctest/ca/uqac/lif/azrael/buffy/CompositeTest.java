/*
    Azrael, a serializer for Java objects
    Copyright (C) 2016-2023 Sylvain Hallé
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.json.JsonStringPrinter;

public class CompositeTest 
{
	@Test
	public void test1() throws PrintException, IOException
	{
		FixedMapSchema s = new FixedMapSchema(new VariantSchema()
				.add(String.class, SmallsciiSchema.instance)
				.add(Boolean.class, BooleanSchema.instance)
				.add(List.class, new ListSchema(
						new FixedMapSchema(IntSchema.int4, "foo", "bar", "baz"))),
				"a", "b", "c");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("a", true);
		map.put("b", "hello world");
		map.put("c", Arrays.asList(new Map[] {getMap(1, 2, 3), getMap(4, 5, 6), getMap(7, 8, 9)}));
		BitSequence seq = s.print(map);
		System.out.println(seq.size());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(map);
		oos.close();
		System.out.println(baos.toByteArray().length);
		System.out.println(JsonStringPrinter.toJson(map).length() * 8);
		
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
