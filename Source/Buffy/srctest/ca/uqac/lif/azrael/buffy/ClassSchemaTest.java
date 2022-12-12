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

import java.util.List;

import org.junit.Test;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

/**
 * Unit tests for {@link ClassSchema}.
 */
public class ClassSchemaTest
{
	@Test
	public void test1() throws PrintException, ReadException
	{
		ClassSchema schema = new ClassSchema(String.class, Integer.class);
		BitSequence bs = schema.print(String.class);
		assertEquals(2, bs.size());
		Class<?> c = (Class<?>) schema.read(bs);
		assertEquals(String.class, c);
	}
	
	@Test
	public void test2() throws PrintException, ReadException
	{
		ClassSchema schema = new ClassSchema(String.class, Integer.class);
		BitSequence bs = schema.print(List.class);
		assertEquals(129, bs.size()); // 0 + 16 bits + "java.util.List"
		Class<?> c = (Class<?>) schema.read(bs);
		assertEquals(List.class, c);
	}
}
