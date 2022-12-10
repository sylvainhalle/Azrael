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

	/*@ non_null @*/ protected final Schema m_valueType;

	public FixedMapSchema(/*@ non_null @*/ Schema value_type, /*@ non_null @*/ List<String> keys)
	{
		super();
		m_keys = keys;
		Collections.sort(m_keys);
		m_valueType = value_type;
	}

	public FixedMapSchema(/*@ non_null @*/ Schema value_type, /*@ non_null @*/ String ... keys)
	{
		this(value_type, Arrays.asList(keys));
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
			out.append(m_keys.get(i));
		}
		out.append("]->");
		out.append(m_valueType.toString());
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
				s.addAll(m_valueType.print(bo));
			}
		}
		return s;
	}

	@Override
	public Map<String,?> read(BitSequence s) throws ReadException
	{
		Map<String,Object> map = new HashMap<String,Object>();
		for (String k : m_keys)
		{
			BitSequence has_entry = s.truncatePrefix(1);
			if (has_entry.get(0))
			{
				Object o = m_valueType.read(s);
				map.put(k, o);
			}
			else
			{
				map.put(k, null);
			}
		}
		return map;
	}
}