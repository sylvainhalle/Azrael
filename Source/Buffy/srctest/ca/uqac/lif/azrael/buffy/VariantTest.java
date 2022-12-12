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

public class VariantTest
{
	@Test
	public void test1() throws PrintException, ReadException
	{
		Object recovered;
		BitSequence seq;
		VariantSchema ms = new VariantSchema();
		ms.add(String.class, SmallsciiSchema.instance);
		ms.add(Integer.class, IntSchema.int8);
		seq = ms.print("foo bar baz");
		recovered = ms.read(seq);
		assertTrue(recovered instanceof String);
		assertEquals("foo bar baz", (String) recovered);
	}
	
	@Test
	public void test2() throws PrintException, ReadException
	{
		Object recovered;
		BitSequence seq;
		VariantSchema ms = new VariantSchema();
		ms.add(String.class, SmallsciiSchema.instance);
		ms.add(Integer.class, IntSchema.int8);
		seq = ms.print(42);
		recovered = ms.read(seq);
		assertTrue(recovered instanceof Number);
		assertEquals(42, ((Number) recovered).intValue());
	}
	
	@Test
	public void test3() throws PrintException, ReadException
	{
		Object recovered;
		BitSequence seq;
		VariantSchema ms = new VariantSchema();
		ms.add(FixedMapInt.class, FixedMapIntSchema.instance);
		ms.add(FixedMapString.class, FixedMapStringSchema.instance);
		FixedMapInt fmi = new FixedMapInt();
		fmi.put("abc", 42);
		seq = ms.print(fmi);
		recovered = ms.read(seq);
		assertTrue(recovered instanceof FixedMapInt);
		assertEquals(42, (Number) ((FixedMapInt) recovered).get("abc"));
	}
	
	@Test
	public void test4() throws PrintException, ReadException
	{
		Object recovered;
		BitSequence seq;
		VariantSchema ms = new VariantSchema();
		ms.add(FixedMapInt.class, FixedMapIntSchema.instance);
		ms.add(FixedMapString.class, FixedMapStringSchema.instance);
		FixedMapString fmi = new FixedMapString();
		fmi.put("foo", "hello");
		seq = ms.print(fmi);
		recovered = ms.read(seq);
		assertTrue(recovered instanceof FixedMapString);
		assertEquals("hello", (String) ((FixedMapString) recovered).get("foo"));
	}
	
	protected static class FixedMapIntSchema extends FixedMapSchema
	{
		public static FixedMapIntSchema instance = new FixedMapIntSchema();
				
		protected FixedMapIntSchema()
		{
			super(IntSchema.int8, "abc", "def");
		}
		
		@Override
		public Map<String,?> read(BitSequence s) throws ReadException
		{
			FixedMapInt map = new FixedMapInt();
			for (String k : m_keys)
			{
				BitSequence has_entry = s.truncatePrefix(1);
				if (has_entry.get(0))
				{
					Object o = m_valueType.read(s);
					map.put(k, ((Number) o).intValue());
				}
				else
				{
					map.put(k, null);
				}
			}
			return map;
		}
	}
	
	protected static class FixedMapStringSchema extends FixedMapSchema
	{
		public static FixedMapStringSchema instance = new FixedMapStringSchema();
				
		protected FixedMapStringSchema()
		{
			super(SmallsciiSchema.instance, "foo", "bar");
		}

		@Override
		public Map<String,?> read(BitSequence s) throws ReadException
		{
			FixedMapString map = new FixedMapString();
			for (String k : m_keys)
			{
				BitSequence has_entry = s.truncatePrefix(1);
				if (has_entry.get(0))
				{
					Object o = m_valueType.read(s);
					map.put(k, (String) o);
				}
				else
				{
					map.put(k, null);
				}
			}
			return map;
		}
	}
	
	protected static class FixedMapString extends HashMap<String,String>
	{
		private static final long serialVersionUID = 1L;		
	}
	
	protected static class FixedMapInt extends HashMap<String,Integer>
	{
		private static final long serialVersionUID = 1L;		
	}
	
	
}
