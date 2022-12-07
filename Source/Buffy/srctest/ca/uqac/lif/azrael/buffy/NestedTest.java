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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;
import ca.uqac.lif.azrael.buffy.BitSequence;
import ca.uqac.lif.azrael.buffy.ListSchema;
import ca.uqac.lif.azrael.buffy.FixedMapSchema;
import ca.uqac.lif.azrael.buffy.Schema;
import ca.uqac.lif.azrael.buffy.SmallsciiSchema;

public class NestedTest
{
	public static final Schema schema1 = new FixedMapSchema(new ListSchema(SmallsciiSchema.instance), "foo", "bar", "baz");
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSchema1_1() throws PrintException, ReadException
	{
		Map<String,List<String>> my_map = new HashMap<>();
		my_map.put("foo", Arrays.asList("hello", "world"));
		my_map.put("bar", Arrays.asList("21st", "century", "schizoid man"));
		my_map.put("baz", Arrays.asList("and all"));
		BitSequence bs = schema1.print(my_map);
		System.out.println(bs.size());
		Map<String,?> recovered = (Map<String, ?>) schema1.read(bs);
		List<?> l;
		l = (List<?>) recovered.get("foo");
		assertEquals(2, l.size());
		assertEquals("world", l.get(1));
		l = (List<?>) recovered.get("bar");
		assertEquals(3, l.size());
	}
}
