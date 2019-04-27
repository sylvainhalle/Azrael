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
package ca.uqac.lif.azrael.size;

import java.util.IdentityHashMap;

import ca.uqac.lif.azrael.PrintHandler;

/**
 * Print handler that returns a fixed size value for all objects matching
 * a given class name. Adding such a handler to a size printer can be useful
 * to cut off the recursive decomposition of "strange" objects.
 * 
 * @author Sylvain Hallé
 */
public class RotePrintHandler implements PrintHandler<Number>
{
	/**
	 * A map of already seen objects
	 */
	protected IdentityHashMap<Object,Integer> m_seenObjects;

	/**
	 * The name of the class to match
	 */
	protected String m_className;

	/**
	 * The size to return for this object
	 */
	protected int m_sizeToReturn;

	public RotePrintHandler(String class_name, int size)
	{
		super();
		m_className = class_name;
		m_sizeToReturn = size;
		m_seenObjects = new IdentityHashMap<Object,Integer>();
	}

	@Override
	public boolean canHandle(Object o)
	{
		return o != null && o.getClass().getSimpleName().compareTo(m_className) == 0;
	}

	@Override
	public Number handle(Object o)
	{
		// We count objects only once
		if (m_seenObjects.containsKey(o))
		{
			return 0;
		}
		m_seenObjects.put(o, 1);
		return m_sizeToReturn;
	}

	@Override
	public void reset()
	{
		m_seenObjects.clear();
	}
}