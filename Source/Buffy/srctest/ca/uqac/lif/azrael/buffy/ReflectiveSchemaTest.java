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

public class ReflectiveSchemaTest
{
	@SuppressWarnings("unchecked")
	@Test
	public void testBootstrap1() throws PrintException, ReadException
	{
		ListSchema s1 = new ListSchema(StringBlobSchema.instance);
		BitSequence bs1 = s1.print(Arrays.asList("foo", "bar", "baz"));
		ReflectiveSchema rs = new ReflectiveSchema();
		BitSequence bs2 = rs.print(s1);
		Schema recovered_s = (Schema) rs.read(bs2);
		List<String> recovered_o = (List<String>) recovered_s.read(bs1);
		assertEquals(3, recovered_o.size());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testBootstrap2() throws PrintException, ReadException
	{
		FixedMapSchema s1 = new FixedMapSchema(BooleanSchema.instance, "foo", "bar", "baz");
		Map<String,Boolean> map = new HashMap<>(); 
		map.put("foo", true);
		map.put("bar", false);
		map.put("baz", true);
		BitSequence bs1 = s1.print(map);
		ReflectiveSchema rs = new ReflectiveSchema();
		BitSequence bs2 = rs.print(s1);
		Schema recovered_s = (Schema) rs.read(bs2);
		Map<String,Boolean> recovered_o = (Map<String,Boolean>) recovered_s.read(bs1);
		assertEquals(3, recovered_o.size());
	}
}
