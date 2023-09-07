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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

public class FixedMapSchema implements Schema
{
	/*@ non_null @*/ protected final List<String> m_keys;

	/*@ non_null @*/ protected final Map<String,Schema> m_valueTypes;
	
	public FixedMapSchema(/*@ non_null @*/ List<String> keys)
	{
		super();
		m_keys = keys;
		Collections.sort(keys);
		m_valueTypes = new HashMap<String,Schema>(keys.size());
	}
	
	public FixedMapSchema(/*@ non_null @*/ String ... keys)
	{
		this(Arrays.asList(keys));
	}

	public FixedMapSchema(/*@ non_null @*/ Schema value_type, /*@ non_null @*/ List<String> keys)
	{
		this(keys);
		for (String k : keys)
		{
			m_valueTypes.put(k, value_type);
		}
	}

	public FixedMapSchema(/*@ non_null @*/ Schema value_type, /*@ non_null @*/ String ... keys)
	{
		this(value_type, Arrays.asList(keys));
	}
	
	public FixedMapSchema setSchema(String key, Schema value_type)
	{
		m_valueTypes.put(key, value_type);
		return this;
	}
	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		out.append("M([");
		for (int i = 0; i < m_keys.size(); i++)
		{
			if (i > 0)
			{
				out.append(",");
			}
			String k = m_keys.get(i);
			if (!m_valueTypes.containsKey(k))
			{
				out.append(k + "->?");
			}
			out.append(k + "->" + m_valueTypes.get(k).toString());
		}
		out.append("]");
		out.append(")");
		return out.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public BitSequence print(Object o) throws PrintException
	{
		if (!(o instanceof Map))
		{
			throw new PrintException("Expected a Map");
		}
		BitSequence s = new BitSequence();
		Map<String,?> map = (Map<String,?>) o;
		for (String k : map.keySet())
		{
			if (!m_keys.contains(k))
			{
				throw new PrintException("Unknown key: " + k);
			}
		}
		for (String k : m_keys)
		{
			if (!m_keys.contains(k))
			{
				throw new PrintException("Unknown key: " + k);
			}
			Object bo = map.get(k);
			if (bo == null)
			{
				s.add(false);
			}
			else
			{
				s.add(true);
				if (!m_valueTypes.containsKey(k))
				{
					throw new PrintException("No schema defined for key " + k);
				}
				Schema sch = m_valueTypes.get(k);
				s.addAll(sch.print(bo));
			}
		}
		return s;
	}

	@Override
	public Map<String,?> read(Object o) throws ReadException
	{
		if (!(o instanceof BitSequence))
		{
			throw new ReadException("Expected a bit sequence");
		}
		BitSequence s = (BitSequence) o;
		Map<String,Object> map = new HashMap<String,Object>();
		for (String k : m_keys)
		{
			BitSequence has_entry = s.truncatePrefix(1);
			if (has_entry.get(0))
			{
				Schema sch = m_valueTypes.get(k);
				Object o2 = sch.read(s);
				map.put(k, o2);
			}
			else
			{
				map.put(k, null);
			}
		}
		return map;
	}
}