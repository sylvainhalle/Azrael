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
import java.util.List;

import org.junit.Test;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

public class SelfDescribedObjectTest
{
	@SuppressWarnings("unchecked")
	@Test
	public void test1() throws PrintException, ReadException
	{
		ListSchema s1 = new ListSchema(StringBlobSchema.instance);
		SelfDescribedObject sdo = new SelfDescribedObject(s1, Arrays.asList("foo", "bar", "baz"));
		BitSequence seq = SelfDescribedObject.schema.print(sdo);
		SelfDescribedObject recovered = SelfDescribedObject.schema.read(seq);
		List<String> list = (List<String>) recovered.getObject();
		assertEquals(3, list.size());
	}
}
