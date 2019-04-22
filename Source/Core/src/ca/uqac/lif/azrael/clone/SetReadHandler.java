/*
    Azrael, a serializer for Java objects
    Copyright (C) 2016-2019 Sylvain Hallé
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
package ca.uqac.lif.azrael.clone;

import java.util.Set;

import ca.uqac.lif.azrael.ObjectReader;
import ca.uqac.lif.azrael.ReadException;

/**
 * Print handler that returns a copy of the list that is passed to it
 * @author Sylvain Hallé
 */
public class SetReadHandler extends CloneReadHandler
{
	public SetReadHandler(ObjectReader<Object> reader)
	{
		super(reader);
	}
	
	@Override
	public boolean canHandle(Object o) throws ReadException
	{
		return o instanceof Set;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<?> handle(Object o) throws ReadException 
	{
		Set<Object> in_set =  (Set<Object>) o;
		Set<Object> set = (Set<Object>) m_reader.getInstance(o.getClass());
		set.addAll(in_set);
		return set;
	}
}
