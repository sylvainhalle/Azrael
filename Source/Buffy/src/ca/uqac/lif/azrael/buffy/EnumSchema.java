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

public class EnumSchema implements Schema
{
	protected final Map<BitSequence,Object> m_fromBits;
	
	protected final Map<Object,BitSequence> m_toBits;
	
	protected final IntSchema m_intSchema;
	
	public EnumSchema(Object ... entries)
	{
		super();
		m_fromBits = new HashMap<BitSequence,Object>(entries.length);
		m_toBits = new HashMap<Object,BitSequence>(entries.length);
		m_intSchema = IntSchema.getSchemaFor(entries.length);
		for (int i = 0; i < entries.length; i++)
		{
			try
			{
				BitSequence id = m_intSchema.print(i);
				m_fromBits.put(id, entries[i]);
				m_toBits.put(entries[i], id);
			}
			catch (PrintException e)
			{
				// Should not happen
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public Object read(BitSequence t) throws ReadException
	{
		BitSequence k = t.truncatePrefix(m_intSchema.getWidth());
		if (!m_fromBits.containsKey(k))
		{
			throw new ReadException("Id " + k + " is not part of the enum");
		}
		return m_fromBits.get(k);
	}

	@Override
	public BitSequence print(Object o) throws PrintException
	{
		if (!m_toBits.containsKey(o))
		{
			throw new PrintException("Object " + o + " is not part of the enum");
		}
		BitSequence bs = new BitSequence();
		bs.addAll(m_toBits.get(o));
		return bs;
	}
	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		out.append("E(");
		boolean first = true;
		for (Object o : m_toBits.keySet())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				out.append(",");
			}
			out.append(o);
		}
		out.append(")");
		return out.toString();
	}

}
