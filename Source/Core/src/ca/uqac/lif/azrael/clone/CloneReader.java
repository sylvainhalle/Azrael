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

import ca.uqac.lif.azrael.ObjectReader;
import ca.uqac.lif.azrael.ReadException;

/**
 * Reads an object and creates a copy of its data structures. The end result
 * is a deep clone of the original object.
 * @author Sylvain Hallé
 *
 */
public class CloneReader extends ObjectReader<Object>
{
	public CloneReader()
	{
		super();
		//m_handlers.add(new ReadableReadHandler(this));
		m_handlers.add(new NullReadHandler(this));
		m_handlers.add(new ListReadHandler(this));
		m_handlers.add(new MapReadHandler(this));
		m_handlers.add(new QueueReadHandler(this));
		m_handlers.add(new SetReadHandler(this));
		m_handlers.add(new IdentityReadHandler());
	}
	
	@Override
	protected Class<?> unwrapType(Object t) throws ReadException 
	{
	  return ((WrappedObject) t).getInnerClass();
	}

	@Override
	protected Object unwrapContents(Object t) throws ReadException 
	{
		return ((WrappedObject) t).getInnerObject();
	}

	@Override
	protected boolean isWrapped(Object t) 
	{
		return t instanceof WrappedObject;
	}
}
