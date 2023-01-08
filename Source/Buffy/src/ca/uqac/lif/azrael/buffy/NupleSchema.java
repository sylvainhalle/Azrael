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

import java.util.List;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

public class NupleSchema extends ListSchema
{
	/**
	 * The size of the n-uple.
	 */
	protected final int m_size;
	
	public NupleSchema(Schema element_schema, int size)
	{
		super(element_schema);
		m_size = size;
	}
	
	@Override
	public List<?> read(Object o) throws ReadException
	{
		return read(o, m_size);
	}
	
	@Override
	public BitSequence print(Object o) throws PrintException
	{
		if (!(o instanceof List))
		{
			throw new PrintException("Expected a list");
		}
		List<?> list = (List<?>) o;
		BitSequence out = new BitSequence();
		for (Object e : list)
		{
			out.addAll(m_elementSchema.print(e));
		}
		return out;
	}

}
