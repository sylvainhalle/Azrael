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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
		int size = SizePrinter.OBJECT_SHELL_SIZE; // Basic overhead of a Java object
		for (Field field : getAllFields(o.getClass()))
		{
			// Is this field declared as transient?
			if (m_ignoreTransient && Modifier.isTransient(field.getModifiers()))
			{
				// Yes: don't serialize this field
				continue; 
			}
			try
			{
				field.setAccessible(true);
			}
			catch (RuntimeException e)
			{
				// Must check exception by its name, as the actual class only exists in Java 9+
				if (e.getClass().getSimpleName().contains("InaccessibleObjectException") && m_ignoreAccessChecks)
				{
					continue;
				}
				else
				{
					throw new PrintException(e);
				}
			}
			try
			{
				Object f_v = field.get(o);
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
			catch (IllegalArgumentException e)
			{
				throw new PrintException(e);
			} 
			catch (IllegalAccessException e)
			{
				throw new PrintException(e);
			}
		}
		return size;
	}
	
	@Override
	public Number encapsulateFields(Object o, Map<String,Object> contents) throws PrintException
	{
		return 0;
	}
	
	@Override
	public void reset()
	{
		m_seenObjects.clear();
	}
}
