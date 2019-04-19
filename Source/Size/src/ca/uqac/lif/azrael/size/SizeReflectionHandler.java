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
import java.util.Map;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReflectionPrintHandler;

public class SizeReflectionHandler extends ReflectionPrintHandler<Number>
{
	protected IdentityHashMap<Object,Integer> m_seenObjects;

	public SizeReflectionHandler(SizePrinter s) 
	{
		super(s);
		m_ignoreTransient = false;
		m_seenObjects = new IdentityHashMap<Object,Integer>();
	}

	@Override
	public Number handle(Object o) throws PrintException
	{
		// We count objects only once
		if (m_seenObjects.containsKey(o))
		{
			return 0;
		}
		m_seenObjects.put(o, 1);
		return super.handle(o);
	}
	
	@Override
	public Number encapsulateFields(Object o, Map<String,Object> contents) throws PrintException
	{
		int size = 24; // Basic overhead of a Java object
		for (Object f_v : contents.values())
		{
			if (SizePrinter.isPrimitive(f_v))
			{
				size += (Integer) m_printer.print(f_v);
			}
			else
			{
				size += SizePrinter.OBJREF_SIZE;
				size += (Integer) m_printer.print(f_v);
			}
		}
		return size;
	}
	
	@Override
	public void reset()
	{
		m_seenObjects.clear();
	}
}
