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

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

public class ListSchema implements Schema
{
	protected final Schema m_elementSchema;
	
	public ListSchema(Schema element_schema)
	{
		super();
		m_elementSchema = element_schema;
	}
	
	@Override
	public List<?> read(Object o) throws ReadException
	{
		if (!(o instanceof BitSequence))
		{
			throw new ReadException("Expected a bit sequence");
		}
		BitSequence t = (BitSequence) o;
		int size = IntSchema.int16.read(t).intValue();
		if (t.size() < size)
		{
			throw new ReadException("Not enough bits to read");
		}
		return read(t, size);
	}
	
	protected List<Object> read(Object o, int size) throws ReadException
	{
		if (!(o instanceof BitSequence))
		{
			throw new ReadException("Expected a bit sequence");
		}
		BitSequence t = (BitSequence) o;
		List<Object> list = new ArrayList<Object>(size);
		for (int i = 0; i < size; i++)
		{
			Object o2 = m_elementSchema.read(t);
			list.add(o2);
		}
		return list;
	}

	@Override
	public BitSequence print(Object o) throws PrintException
	{
		if (!(o instanceof List))
		{
			throw new PrintException("Expected a list");
		}
		List<?> list = (List<?>) o;
		BitSequence out = IntSchema.int16.print(list.size());
		for (Object e : list)
		{
			out.addAll(m_elementSchema.print(e));
		}
		return out;
	}
}
