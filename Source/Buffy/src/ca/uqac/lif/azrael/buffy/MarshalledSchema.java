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

import java.util.HashMap;
import java.util.Map;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

public class MarshalledSchema implements Schema
{
	protected final Map<BitSequence,SchemaEntry> m_schemaMap;
	
	protected int m_idCounter = 1;
	
	protected static final IntSchema s_int = IntSchema.int4;
	
	public MarshalledSchema()
	{
		super();
		m_schemaMap = new HashMap<BitSequence,SchemaEntry>();
	}
	
	public MarshalledSchema add(Class<?> c, Schema s)
	{
		int id = m_idCounter++;
		SchemaEntry e = new SchemaEntry(c, s);
		try
		{
			m_schemaMap.put(s_int.print(id), e);
		}
		catch (PrintException ex)
		{
			// Should not happen
			ex.printStackTrace();
		}
		return this;
	}
	
	@Override
	public Object read(BitSequence t) throws ReadException
	{
		BitSequence b_id = t.truncatePrefix(s_int.getWidth());
		if (!m_schemaMap.containsKey(b_id))
		{
			throw new ReadException("No schema for byte identifier " + b_id);
		}
		Schema s = m_schemaMap.get(b_id).getSchema();
		return s.read(t);
	}

	@Override
	public BitSequence print(Object o) throws PrintException
	{
		for (Map.Entry<BitSequence,SchemaEntry> entry : m_schemaMap.entrySet())
		{
			SchemaEntry e = entry.getValue();
			if (e.matches(o))
			{
				BitSequence bs = new BitSequence();
				bs.addAll(entry.getKey());
				bs.addAll(e.getSchema().print(o));
				return bs;
			}
		}
		throw new PrintException("No schema for class " + o.getClass().getName());
	}
	
	protected static class SchemaEntry
	{
		protected final Class<?> m_class;
		
		protected final Schema m_schema;
		
		public SchemaEntry(Class<?> c, Schema s)
		{
			super();
			m_class = c;
			m_schema = s;
		}
		
		public Schema getSchema()
		{
			return m_schema;
		}
		
		public boolean matches(Object o)
		{
			if (o == null)
			{
				return false;
			}
			return m_class.isAssignableFrom(o.getClass());
		}
	}

}
