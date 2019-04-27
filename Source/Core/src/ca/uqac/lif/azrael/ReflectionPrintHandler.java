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
package ca.uqac.lif.azrael;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Print handler that uses reflection to serialize the fields of an object.
 * @author Sylvain Hallé
 *
 * @param <T> The type to which objects are serialized
 */
public class ReflectionPrintHandler<T> implements PrintHandler<T>
{
	/**
	 * The internal object printer
	 */
	protected ObjectPrinter<T> m_printer;

	/**
	 * Whether the fields marked as <tt>transient</tt> should be ignored
	 */
	protected boolean m_ignoreTransient = true;

	/**
	 * Creates a new reflection print handler
	 * @param p The internal object printer
	 */
	public ReflectionPrintHandler(ObjectPrinter<T> p)
	{
		super();
		m_printer = p;
	}
	@Override
	public boolean canHandle(Object o)
	{
		return true;
	}

	@Override
	public T handle(Object o) throws PrintException
	{
		Map<String,Object> contents = new HashMap<String,Object>();
		for (Field field : getAllFields(o.getClass()))
		{
			// Is this field declared as transient?
			if (m_ignoreTransient && Modifier.isTransient(field.getModifiers()))
			{
				// Yes: don't serialize this field
				continue; 
			}
			field.setAccessible(true);
			try
			{
				contents.put(field.getName(), field.get(o));
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
		return encapsulateFields(o, contents);
	}

	protected T encapsulateFields(Object o, Map<String,Object> contents) throws PrintException
	{
		return m_printer.wrap(o, m_printer.print(contents));
	}

	protected static List<Field> getAllFields(List<Field> fields, Class<?> type)
	{
		fields.addAll(Arrays.asList(type.getDeclaredFields()));
		if (type.getSuperclass() != null)
		{
			fields = getAllFields(fields, type.getSuperclass());
		}
		return fields;
	}

	protected static List<Field> getAllFields(Class<?> type)
	{
		List<Field> fields = getAllFields(new LinkedList<Field>(), type);
		Iterator<Field> it = fields.iterator();
		while (it.hasNext())
		{
			Field f = it.next();
			if (f.getClass().isEnum() || f.getName().startsWith("$") || f.getName().startsWith(("!")))
			{
				it.remove();
			}
		}
		return fields;
	}

	protected static Field getFromAllFields(String name, Class<?> type) throws NoSuchFieldException
	{
		List<Field> fields = getAllFields(type);
		for (Field f : fields)
		{
			if (f.getName().compareTo(name) == 0)
			{
				return f;
			}
		}
		throw new NoSuchFieldException();
	}

	@Override
	public void reset()
	{
		// Nothing to do
	}
}
